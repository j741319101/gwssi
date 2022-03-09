/*    */ package com.dstz.bpm.plugin.node.sign.executer;
/*    */ 
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.bpm.api.constant.TaskType;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class SignTaskPluginAction
/*    */   extends DefaultExtendTaskAction
/*    */ {
/*    */   public void canFreeJump(IBpmTask bpmTask) {
/* 15 */     if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN.getKey()) || 
/* 16 */       StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN_SOURCE.getKey()))
/* 17 */       throw new BusinessException("会签节点禁止跳转"); 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/sign/executer/SignTaskPluginAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */