package com.dstz.bpm.engine.action.handler.instance;

import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bpm.act.service.ActInstanceService;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstanceRecoverActionHandler
        implements ActionDisplayHandler<DefaultInstanceActionCmd> {
    @Autowired
    private BpmProcessDefService bpmProcessDefService;
    @Autowired
    private ActInstanceService actInstanceService;
    @Autowired
    private BpmTaskManager bpmTaskManager;
    @Autowired
    private TaskIdentityLinkManager taskIdentityLinkManager;
    @Autowired
    private BpmInstanceManager bpmInstanceManager;
    @Autowired
    private BpmTaskStackManager bpmTaskStackManager;
    @Autowired
    private BpmTaskOpinionManager bpmTaskOpinionManager;
    @Resource
    private ExecutionCommand executionCommand;
    @Resource
    IBpmBusDataHandle bpmBusDataHandle;

    public void execute(DefaultInstanceActionCmd model) {
        BpmNodeDef bpmNodeDef;
        if (model.getBpmInstance() == null) {
            model.setBpmInstance((IBpmInstance) this.bpmInstanceManager.get(model.getInstanceId()));
        }
        String currentUserId = ContextUtil.getCurrentUserId();
        if (!currentUserId.equals(model.getBpmInstance().getCreateBy()) && !ContextUtil.currentUserIsAdmin()) {
            throw new BusinessMessage("操作受限，您没有该操作权限", BpmStatusCode.NO_PERMISSION);
        }
        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(model.getDefId());
        String nodeId = model.getExtendConf().getString("nodeId");

        if (StringUtils.isNotEmpty(nodeId)) {
            bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(model.getDefId(), nodeId);
        } else {
            bpmNodeDef = bpmProcessDef.getStartEvent();
        }
        if (!isDisplay(false, bpmNodeDef, model.getBpmInstance(), null)) {
            throw new BusinessException("当前节点不支持撤销操作");
        }

        if (model.getBpmInstance().getIsForbidden().shortValue() == 1) {
            throw new BusinessMessage("流程实例已经被禁止，请联系管理员", BpmStatusCode.DEF_FORBIDDEN);
        }
        ThreadMapUtil.put("EcloudBPMDeleteInstance", "");
        handleInstanceInfo(model.getOpinion(), (BpmInstance) model.getBpmInstance(), new Date(), model);
    }


    private void handleInstanceInfo(String opinion, BpmInstance bpmInstance, Date endTime, DefaultInstanceActionCmd model) {
        List<BpmInstance> subBpmInstanceList = this.bpmInstanceManager.getByParentId(bpmInstance.getId());
        if (CollectionUtils.isNotEmpty(subBpmInstanceList)) {
            for (BpmInstance subBpmInstance : subBpmInstanceList) {
                handleInstanceInfo(opinion, subBpmInstance, endTime, model);
            }
        }

        if (this.actInstanceService.getProcessInstance(bpmInstance.getActInstId()) != null) {
            this.actInstanceService.deleteProcessInstance(bpmInstance.getActInstId(), opinion);
        }
        List<BpmTask> bpmTaskList = this.bpmTaskManager.getByInstId(bpmInstance.getId());
        for (BpmTask bpmTask : bpmTaskList) {
            BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
            if (bpmTaskOpinion.getApproveTime() == null) {
                bpmTaskOpinion.setDurMs(Long.valueOf(endTime.getTime() - bpmTaskOpinion.getCreateTime().getTime()));
                bpmTaskOpinion.setOpinion(opinion);
                bpmTaskOpinion.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
                bpmTaskOpinion.setTaskOrgId(model.getApproveOrgId());
                bpmTaskOpinion.setApprover(ContextUtil.getCurrentUserId());
                bpmTaskOpinion.setApproverName(ContextUtil.getCurrentUserName());
                bpmTaskOpinion.setApproveTime(endTime);
                this.bpmTaskOpinionManager.update(bpmTaskOpinion);
            }
        }
        InstanceActionCmd instanceActionCmd = getInstCmd(model, bpmInstance);
        BpmContext.setActionModel((ActionCmd) instanceActionCmd);
        this.executionCommand.execute(EventType.RECOVER_EVENT, instanceActionCmd);
        this.bpmTaskManager.removeByInstId(bpmInstance.getId());
        this.taskIdentityLinkManager.removeByInstId(bpmInstance.getId());
        this.bpmTaskStackManager.updateStackEndByInstId(model.getInstanceId());
        bpmInstance.setStatus(InstanceStatus.STATUS_REVOKE.getKey());
        bpmInstance.setEndTime(endTime);
        bpmInstance.setDuration(Long.valueOf(endTime.getTime() - bpmInstance.getCreateTime().getTime()));
        this.bpmInstanceManager.update(bpmInstance);
        List<IExtendTaskAction> actions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        actions.forEach(action -> action.revokeInst(bpmInstance.getId()));
    }


    private InstanceActionCmd getInstCmd(DefaultInstanceActionCmd model, BpmInstance bpmInstance) {
        DefaultInstanceActionCmd instanceActionCmd = new DefaultInstanceActionCmd();
        instanceActionCmd.setActionName(model.getActionName());
        instanceActionCmd.setActionVariables(model.getActionVariables());
        if (CollectionUtil.isEmpty(model.getBizDataMap())) {
            BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(bpmInstance);
            if (topInstance != null) {
                bpmInstance = topInstance;
            }
            Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null);
            if (CollectionUtil.isNotEmpty(data)) {
                model.setBizDataMap(data);
            }
        }
        instanceActionCmd.setBizDataMap(model.getBizDataMap());
        instanceActionCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(bpmInstance.getDefId()));
        instanceActionCmd.setBpmIdentities(model.getBpmIdentities());
        instanceActionCmd.setDynamicBpmIdentity(model.getDynamicBpmIdentity());
        instanceActionCmd.setBpmInstance((IBpmInstance) bpmInstance);
        instanceActionCmd.setBusData(model.getBusData());
        instanceActionCmd.setBusinessKey(model.getBusinessKey());
        instanceActionCmd.setDataMode(model.getDataMode());
        instanceActionCmd.setDefId(model.getDefId());
        instanceActionCmd.setDestination(model.getDestination());
        instanceActionCmd.setFormId(model.getFormId());
        instanceActionCmd.setApproveOrgId(model.getApproveOrgId());
        return (InstanceActionCmd) instanceActionCmd;
    }

    public ActionType getActionType() {
        return ActionType.RECOVER;
    }


    public int getSn() {
        return 8;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf((nodeDef == null || nodeDef.getType() == null));
    }

    public Boolean isDefault() {
        return Boolean.TRUE;
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }

    public String getDefaultGroovyScript() {
        return "";
    }

    public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
        if (bpmInstance == null) {
            return false;
        }

        if (StringUtil.isNotZeroEmpty(bpmInstance.getParentInstId())) {
            return false;
        }
        if (bpmInstance.getEndTime() != null || bpmInstance.getIsForbidden().shortValue() == 1 || "forbidden".equals(((DefaultBpmProcessDef) bpmNodeDef.getBpmProcessDef()).getExtProperties().getStatus())) {
            return false;
        }
        String currentUserId = ContextUtil.getCurrentUserId();
        if (!currentUserId.equals(bpmInstance.getCreateBy()) && !ContextUtil.currentUserIsAdmin()) {
            return false;
        }
        return true;
    }

    public String getDefaultBeforeScript() {
        return "";
    }
}

