package com.dstz.bpm.engine.action.handler.task;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.sys.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class TaskLockActionHandler
        extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    public ActionType getActionType() {
        return ActionType.LOCK;
    }
    @Resource
    private BpmTaskOpinionManager bpmTaskOpinionManager;
    public void execute(DefualtTaskActionCmd model) {
        prepareActionDatas(model);
        checkFlowIsValid((BaseActionCmd) model);
        BpmTask task = (BpmTask) model.getBpmTask();
        if (!task.getAssigneeId().equals("0")) {
            throw new BusinessMessage("该任务只有一个候选人没有锁定的必要。");
        }
        if (StringUtils.isEmpty(model.getTaskLinkId())) {
            throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
        }
        TaskIdentityLink taskIdentityLink = (TaskIdentityLink) this.taskIdentityLinkManager.get(model.getTaskLinkId());
        if (taskIdentityLink == null) {
            throw new BusinessException("候选人关系记录丢失，检查 taskLinkId");
        }
        task.setAssigneeId(ContextUtil.getCurrentUserId());
        task.setAssigneeNames(ContextUtil.getCurrentUser().getFullname());
        task.setStatus(TaskStatus.LOCK.getKey());
        this.taskManager.update(task);
        BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
        taskOpinion.setAssignInfo("user-" + task.getAssigneeNames() + "-" + ContextUtil.getCurrentUserId() + "-" + taskIdentityLink.getOrgId() + ",");
        this.bpmTaskOpinionManager.update(taskOpinion);
    }
    protected void doActionAfter(DefualtTaskActionCmd actionModel) {
    }
    protected void doActionBefore(DefualtTaskActionCmd actionModel) {
    }
    public int getSn() {
        return 6;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(false);
    }
    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }
    public String getDefaultGroovyScript() {
        return "return task.getAssigneeId().equals(\"0\")";
    }
    public String getConfigPage() {
        return "";
    }
}
 