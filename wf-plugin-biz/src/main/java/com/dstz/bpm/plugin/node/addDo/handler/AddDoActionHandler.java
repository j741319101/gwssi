package com.dstz.bpm.plugin.node.addDo.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.id.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddDoActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   public ActionType getActionType() {
      return ActionType.ADDDO;
   }

   public int getSn() {
      return 1;
   }

   public String getConfigPage() {
      return "/bpm/task/addDoActionDialog.html";
   }

   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
   }

   public void doAction(DefualtTaskActionCmd actionModel) {
      this.handleCurentTask(actionModel);
      this.createNewTask(actionModel);
   }

   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      NodeType nodeType = nodeDef.getType();
//      return nodeType == NodeType.USERTASK;
      return false;//todo 暂时去掉按钮
   }
   public Boolean isDefault(){
      return Boolean.valueOf(false);
   }


   private void handleCurentTask(DefualtTaskActionCmd actionModel) {
      BpmTask bpmTask = (BpmTask)actionModel.getBpmTask();
      if (bpmTask.getStatus().equals(TaskStatus.ADDDOING.getKey())) {
         throw new BusinessMessage("当前任务已有处于加办中");
      } else {
         bpmTask.setStatus(TaskStatus.ADDDOING.getKey());
         bpmTask.setAssigneeId("-" + bpmTask.getAssigneeId());
         this.bpmTaskManager.update(bpmTask);
         this.bpmTaskOpinionManager.commonUpdate(bpmTask.getId(), OpinionStatus.ADD_DO, actionModel.getOpinion());
      }
   }

   private void createNewTask(DefualtTaskActionCmd actionModel) {
      JSON bpmTaskJson = (JSON)JSON.toJSON(actionModel.getBpmTask());
      JSONObject jsonObject = actionModel.getExtendConf();
      JSONObject user = jsonObject.getJSONArray("users").getJSONObject(0);
      BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
      task.setId(IdUtil.getSuid());
      task.setAssigneeId(user.getString("id"));
      task.setAssigneeNames(user.getString("name"));
      task.setParentId(actionModel.getBpmTask().getId());
      task.setStatus(TaskStatus.NORMAL.getKey());
      task.setTaskType(TaskType.ADDDO.getKey());
      this.bpmTaskManager.create(task);
      BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getBpmTask().getId());
      this.bpmTaskOpinionManager.createOpinion(task, actionModel.getBpmInstance(), (List)null, (String)null, ActionType.ADDDO.getKey(), actionModel.getFormId(), taskOpinion.getSignId(), taskOpinion.getTrace(), taskOpinion.getVersion());
   }
}
