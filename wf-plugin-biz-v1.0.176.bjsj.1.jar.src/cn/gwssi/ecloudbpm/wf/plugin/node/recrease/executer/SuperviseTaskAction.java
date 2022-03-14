/*    */ package com.dstz.bpm.plugin.node.recrease.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*    */ import com.dstz.bpm.core.model.BpmInstance;
/*    */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*    */ import com.dstz.bpm.plugin.node.recrease.handler.RecreaseTaskAction;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class SuperviseTaskAction
/*    */   extends DefaultExtendTaskAction {
/*    */   @Resource
/*    */   private RecreaseTaskAction recreaseTaskAction;
/*    */   @Resource
/*    */   private BpmInstanceManager bpmInstanceManager;
/*    */   
/*    */   public void canFreeJump(IBpmTask bpmTask) {
/* 25 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(bpmTask.getInstId());
/* 26 */     JSONObject result = this.recreaseTaskAction.getExistAliveTask((IBpmInstance)bpmInstance, bpmTask);
/* 27 */     Boolean can = result.getBoolean("existAliveTask");
/* 28 */     if (!can.booleanValue()) {
/* 29 */       JSONObject nodeTasks = result.getJSONObject("tasks");
/* 30 */       if (nodeTasks != null) {
/* 31 */         for (String nodeId : nodeTasks.keySet()) {
/* 32 */           JSONArray tasks = nodeTasks.getJSONObject(nodeId).getJSONArray("tasks");
/* 33 */           if (tasks != null) {
/* 34 */             for (Object task : tasks) {
/* 35 */               String approver = ((JSONObject)task).getString("approver");
/* 36 */               if (StringUtils.isEmpty(approver)) {
/* 37 */                 throw new BusinessException("监管任务跳转，有受监管任务未完成");
/*    */               }
/*    */             } 
/*    */           }
/*    */         } 
/*    */       }
/*    */     } else {
/* 44 */       throw new BusinessException("监管任务跳转，有受监管任务未完成");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/executer/SuperviseTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */