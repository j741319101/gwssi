/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.sign.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.DefaultExtendTaskAction;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.context.SignTaskPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.def.SignTaskPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class SignTaskPluginAction extends DefaultExtendTaskAction {
/*    */   public void canFreeJump(IBpmTask bpmTask) {
/* 16 */     if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN.getKey()) || 
/* 17 */       StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
/* 18 */       throw new BusinessException("会签节点暂不适用跳转");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSignTask(BpmNodeDef bpmNodeDef) {
/* 24 */     SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class);
/* 25 */     return (signTaskPluginContext != null && ((SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef()).isSignMultiTask());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/sign/executer/SignTaskPluginAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */