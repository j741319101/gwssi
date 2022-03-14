package com.dstz.bpm.plugin.node.sign.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
import com.dstz.base.core.id.IdUtil;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddSignActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   public ActionType getActionType() {
      return ActionType.ADDSIGN;
   }

   public int getSn() {
      return 3;
   }

   public String getConfigPage() {
      return "/bpm/task/addSignActionDialog.html";
   }

   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
   }

   public void doAction(DefualtTaskActionCmd actionModel) {
      JSON bpmTaskJson = (JSON)JSON.toJSON(actionModel.getBpmTask());
      JSONObject jsonObject = actionModel.getExtendConf();
      JSONArray users = jsonObject.getJSONArray("users");
      BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getBpmTask().getId());
      String mainTaskId = taskOpinion.getSignId();
      users.forEach((obj) -> {
         JSONObject json = (JSONObject)obj;
         BpmTask task = (BpmTask)JSON.toJavaObject(bpmTaskJson, BpmTask.class);
         task.setId(IdUtil.getSuid());
         task.setTaskType(TaskType.ADD_SIGN.getKey());
         task.setParentId(actionModel.getBpmTask().getParentId());
         task.setAssigneeId(json.getString("id"));
         task.setAssigneeNames(json.getString("name"));
         List<SysIdentity> identityList = new ArrayList();
         identityList.add(new DefaultIdentity(json.getString("id"), json.getString("name"), "user", json.getString("orgId")));
         this.bpmTaskOpinionManager.createOpinion(task, actionModel.getBpmInstance(), identityList, actionModel.getOpinion(), actionModel.getActionName(), actionModel.getFormId(), mainTaskId, taskOpinion.getTrace(), taskOpinion.getVersion());
         this.taskIdentityLinkManager.createIdentityLink(task, identityList);
         this.bpmTaskManager.create(task);
      });
   }

   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      NodeType nodeType = nodeDef.getType();
      return nodeType != NodeType.USERTASK && nodeType != NodeType.SIGNTASK ? false : true;
   }
}
