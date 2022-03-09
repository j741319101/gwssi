/*    */ package cn.gwssi.ecloudbpm.wf.engine.plugin.factory;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmUserCalcPluginSession;
/*    */ import org.activiti.engine.delegate.VariableScope;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BpmPluginSessionFactory
/*    */ {
/*    */   public static BpmExecutionPluginSession buildExecutionPluginSession(InstanceActionCmd actionModel, EventType eventType) {
/* 23 */     DefaultBpmExecutionPluginSession executionPluginSession = new DefaultBpmExecutionPluginSession();
/* 24 */     executionPluginSession.setBoDatas(actionModel.getBizDataMap());
/* 25 */     executionPluginSession.setBpmInstance(actionModel.getBpmInstance());
/* 26 */     executionPluginSession.setEventType(eventType);
/* 27 */     executionPluginSession.setVariableScope((VariableScope)((DefaultInstanceActionCmd)actionModel).getExecutionEntity());
/* 28 */     return (BpmExecutionPluginSession)executionPluginSession;
/*    */   }
/*    */   
/*    */   public static BpmExecutionPluginSession buildTaskPluginSession(TaskActionCmd actionModel, EventType eventType) {
/* 32 */     DefualtTaskActionCmd taskActionModel = (DefualtTaskActionCmd)actionModel;
/*    */     
/* 34 */     DefaultBpmTaskPluginSession session = new DefaultBpmTaskPluginSession();
/* 35 */     session.setBoDatas(actionModel.getBizDataMap());
/* 36 */     session.setBpmInstance(actionModel.getBpmInstance());
/* 37 */     session.setEventType(eventType);
/* 38 */     session.setVariableScope((VariableScope)taskActionModel.getDelagateTask());
/* 39 */     session.setBpmTask(taskActionModel.getBpmTask());
/* 40 */     session.setCurrentOrgId(taskActionModel.getApproveOrgId());
/* 41 */     session.setTaskIdentityLink(taskActionModel.getTaskIdentityLink());
/* 42 */     session.put("actionName", taskActionModel.getActionName());
/* 43 */     return (BpmExecutionPluginSession)session;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BpmUserCalcPluginSession buildBpmUserCalcPluginSession(BpmPluginSession pluginSession) {
/* 53 */     DefaultBpmExecutionPluginSession plugin = (DefaultBpmExecutionPluginSession)pluginSession;
/*    */     
/* 55 */     DefaultBpmUserCalcPluginSession userCalcPluginSession = new DefaultBpmUserCalcPluginSession();
/* 56 */     userCalcPluginSession.setBoDatas(pluginSession.getBoDatas());
/* 57 */     userCalcPluginSession.setVariableScope(plugin.getVariableScope());
/* 58 */     userCalcPluginSession.setBpmInstance(pluginSession.getBpmInstance());
/* 59 */     userCalcPluginSession.put("actionName", plugin.get("actionName"));
/* 60 */     if (pluginSession instanceof DefaultBpmTaskPluginSession) {
/* 61 */       DefaultBpmTaskPluginSession taskSession = (DefaultBpmTaskPluginSession)pluginSession;
/*    */       
/* 63 */       userCalcPluginSession.setBpmTask(taskSession.getBpmTask());
/*    */     } 
/* 65 */     ActionCmd action = BpmContext.getActionModel();
/* 66 */     if (action != null && action instanceof TaskActionCmd) {
/*    */       
/* 68 */       TaskActionCmd taskCmd = (TaskActionCmd)action;
/* 69 */       userCalcPluginSession.setBpmTask(taskCmd.getBpmTask());
/*    */     } 
/*    */     
/* 72 */     return (BpmUserCalcPluginSession)userCalcPluginSession;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/factory/BpmPluginSessionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */