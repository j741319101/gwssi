package com.dstz.bpm.plugin.node.addDo.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddDoAgreeActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   public ActionType getActionType() {
      return ActionType.ADDDOAGREE;
   }

   public int getSn() {
      return 1;
   }

   public String getConfigPage() {
      return "/bpm/task/taskOpinionDialog.html";
   }

   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
   }

   public void doAction(DefualtTaskActionCmd actionModel) {
      this.bpmTaskOpinionManager.commonUpdate(actionModel.getTaskId(), OpinionStatus.ADD_DO_AGREE, actionModel.getOpinion());
      this.bpmTaskManager.remove(actionModel.getTaskId());
      BpmTask sourceTask = (BpmTask)this.bpmTaskManager.get(actionModel.getBpmTask().getParentId());
      sourceTask.setAssigneeId(sourceTask.getAssigneeId().substring(1));
      sourceTask.setStatus(TaskStatus.ADDDOED.getKey());
      this.bpmTaskManager.update(sourceTask);
      BpmTaskOpinion sourceOpinion = this.bpmTaskOpinionManager.getByTaskId(sourceTask.getId());
      sourceOpinion.setTaskId("-" + sourceTask.getId());
      this.bpmTaskOpinionManager.update(sourceOpinion);
      this.bpmTaskOpinionManager.createOpinion(sourceTask, actionModel.getBpmInstance(), (List)null, (String)null, ActionType.ADDDOAGREE.getKey(), actionModel.getFormId());
   }

   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      return false;
   }
}
