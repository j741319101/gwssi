package com.dstz.bpm.engine.listener;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.ScriptType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.GroupDTO;
import com.dstz.org.api.service.GroupService;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteListener
        extends AbstractTaskListener<DefualtTaskActionCmd> {
    private static final long serialVersionUID = 6844821899585103714L;
    @Resource
    private BpmTaskManager bpmTaskManager;
    @Resource
    private TaskIdentityLinkManager taskIdentityLinkManager;
    @Resource
    private BpmTaskOpinionManager bpmTaskOpinionManager;
    @Resource
    private BpmTaskStackManager bpmTaskStackManager;
    @Resource
    private BpmProcessDefService bpmProcessDefService;
    @Resource
    private IGroovyScriptEngine groovyScriptEngine;
    @Resource
    JmsProducer jmsProducer;

    @Resource
    GroupService groupService;

    public EventType getBeforeTriggerEventType() {
        return EventType.TASK_COMPLETE_EVENT;
    }

    public EventType getAfterTriggerEventType() {
        return EventType.TASK_POST_COMPLETE_EVENT;
    }

    public void beforePluginExecute(DefualtTaskActionCmd taskActionModel) {
        this.LOG.debug("任务【{}】执行完成事件 - TaskID: {}", taskActionModel.getBpmTask().getName(), taskActionModel.getBpmTask().getId());

        Map<String, Object> actionVariables = taskActionModel.getActionVariables();
        if (CollectionUtil.isEmpty(actionVariables)) {
            return;
        }

        for (String key : actionVariables.keySet()) {
            taskActionModel.addVariable(key, actionVariables.get(key));
        }
        this.LOG.debug("设置流程变量【{}】", actionVariables.keySet().toString());
    }


    public void triggerExecute(DefualtTaskActionCmd taskActionModel) {
        DefualtTaskActionCmd complateModel = taskActionModel;
        this.LOG.trace("执行任务完成动作=====》更新任务意见状态");
        updateOpinionStatus(complateModel);
        this.LOG.trace("执行任务完成动作=====》更新任务堆栈记录");
        updateExcutionStack(complateModel);
        this.LOG.trace("执行任务完成动作=====》删除任务相关信息 - 任务、任务后续人");
        delTaskRelated(complateModel);
    }

    public void afterPluginExecute(DefualtTaskActionCmd taskActionModel) {
        BpmContext.setOptionVersion(String.valueOf(System.currentTimeMillis()));
    }

    protected ScriptType getScriptType() {
        return ScriptType.COMPLETE;
    }

    public DefualtTaskActionCmd getActionModel(TaskEntity taskEntity) {
        DefualtTaskActionCmd model = (DefualtTaskActionCmd) BpmContext.getActionModel();
        model.setDelagateTask((DelegateTask) taskEntity);
        return model;
    }

    private void updateOpinionStatus(DefualtTaskActionCmd taskActionModel) {
        InstanceStatus flowStatus = InstanceStatus.getByActionName(taskActionModel.getActionName());
        BpmInstance instance = (BpmInstance) taskActionModel.getBpmInstance();
        if (!flowStatus.getKey().equals(instance.getStatus())) {
            instance.setStatus(flowStatus.getKey());
            instance.setHasUpdate(true);
        }
        BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskActionModel.getTaskId());
        if (bpmTaskOpinion == null)
            return;
        OpinionStatus opnionStatus = OpinionStatus.getByActionName(taskActionModel.getActionName());
        bpmTaskOpinion.setStatus(opnionStatus.getKey());
        bpmTaskOpinion.setApproveTime(new Date());
        Long durMs = Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime());
        bpmTaskOpinion.setDurMs(Long.valueOf((durMs.longValue() > 0L) ? durMs.longValue() : 10L));
        bpmTaskOpinion.setOpinion(taskActionModel.getOpinion());
        IUser user = ContextUtil.getCurrentUser();
        if (user != null) {
            bpmTaskOpinion.setApprover(user.getUserId());
            bpmTaskOpinion.setApproverName(user.getFullname());
        }
        String orgId = taskActionModel.getApproveOrgId();
        TaskIdentityLink taskIdentityLink = taskActionModel.getTaskIdentityLink();
        if (taskIdentityLink != null) {
            orgId = taskIdentityLink.getOrgId();
        }
        if (StringUtils.isEmpty(orgId)) {//todo 暂时测试机构id
            GroupDTO group = (GroupDTO) groupService.getMainGroup(user.getUserId());
            if (group != null) {
                orgId = group.getGroupId();
            } else {
                orgId = "1";
            }
        }
        if (StringUtils.isEmpty(orgId)) {
            throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
        }
        bpmTaskOpinion.setTaskOrgId(orgId);
        taskActionModel.setApproveOrgId(orgId);
        if (BpmContext.isMulInstOpTraceEmpty() && StringUtil.isNotEmpty(bpmTaskOpinion.getTrace())) {
            BpmContext.pushMulInstOpTrace(bpmTaskOpinion.getTrace());
        }
        this.bpmTaskOpinionManager.update(bpmTaskOpinion);
    }

    private void updateExcutionStack(DefualtTaskActionCmd taskActionModel) {
        BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskActionModel.getTaskId());
        bpmTaskStack.setEndTime(new Date());
        bpmTaskStack.setActionName(BpmContext.getActionModel());
        this.bpmTaskStackManager.update(bpmTaskStack);
        taskActionModel.setExecutionStack((BpmExecutionStack) bpmTaskStack);
    }

    private void delTaskRelated(DefualtTaskActionCmd taskActionModel) {
        this.taskIdentityLinkManager.removeByTaskId(taskActionModel.getTaskId());
        this.bpmTaskManager.remove(taskActionModel.getTaskId());
    }

    public void systemMessage(ActionCmd cmd) {
    }
}

