package com.dstz.bpm.engine.action.handler.task;

import javax.annotation.Resource;

import com.dstz.base.api.constant.IStatusCode;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.bpm.act.service.ActTaskService;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.AbsActionHandler;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTaskActionHandler<T extends DefualtTaskActionCmd> extends AbsActionHandler<T> {
    @Resource
    protected ActTaskService actTaskService;
    @Resource
    protected BpmTaskManager taskManager;

    public void doAction(T actionModel) {
        BpmTask bpmTask = (BpmTask) actionModel.getBpmTask();

        String taskId = bpmTask.getTaskId();
        String destinationNode = bpmTask.getBackNode();

        String[] destinationNodes = null;
        if (StringUtil.isEmpty(destinationNode)) {
            destinationNodes = actionModel.getDestinations();
        } else {
            destinationNodes = new String[]{destinationNode};
        }

        if (StringUtil.isEmpty(destinationNode)) {
            destinationNode = actionModel.getDestination();
        }
        if (destinationNodes == null) {
            this.actTaskService.completeTask(taskId, actionModel.getActionVariables());
        } else {

            this.actTaskService.completeTask(taskId, actionModel.getActionVariables(), destinationNodes);
        }
    }

    @Resource
    protected TaskCommand taskCommand;
    @Resource
    protected TaskIdentityLinkManager taskIdentityLinkManager;

    protected boolean prepareActionDatas(T data) {
        if (data.getBpmTask() != null) return false;

        BpmTask task = (BpmTask) this.taskManager.get(data.getTaskId());
        if (task == null) {
            throw new BusinessException((IStatusCode) BpmStatusCode.TASK_NOT_FOUND);
        }
        if (StringUtils.isNotEmpty(data.getTaskLinkId())) {
            TaskIdentityLink taskIdentityLink = (TaskIdentityLink) this.taskIdentityLinkManager.get(data.getTaskLinkId());
            if (taskIdentityLink == null) {
                throw new BusinessException("taskLinkId");
            }
            data.setTaskIdentityLink(taskIdentityLink);
        }
        data.setBpmTask((IBpmTask) task);
        data.setDefId(task.getDefId());
        data.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(task.getDefId()));
        data.setBpmInstance((IBpmInstance) this.bpmInstanceManager.get(task.getInstId()));

//        parserBusinessData((BaseActionCmd)data);
        parserBusinessData(data);

        boolean isStopExecute = handelFormInit((BaseActionCmd) data, this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()));
        return isStopExecute;
    }


    public Boolean isSupport(BpmNodeDef nodeDef) {
        NodeType nodeType = nodeDef.getType();

        if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
            return Boolean.valueOf(true);
        }

        return Boolean.valueOf(false);
    }

    public Boolean isDefault(){
        return Boolean.valueOf(true);
    }


    protected void taskComplatePrePluginExecute(DefualtTaskActionCmd actionModel) {
        this.taskCommand.execute(EventType.TASK_PRE_COMPLETE_EVENT, (TaskActionCmd) actionModel);
    }


    protected void toDoActionAfter(T actionModel) {
//        doActionAfter((BaseActionCmd)actionModel);
        doActionAfter(actionModel);

        if (actionModel.isSource()) {
            BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
            if (instance.isHasUpdate()) {
                this.bpmInstanceManager.update(instance);
            }
        }
    }


    public String getDefaultGroovyScript() {
        return "";
    }



}
