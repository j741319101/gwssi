/*    */ package cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.activiti.engine.delegate.VariableScope;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultBpmExecutionPluginSession
/*    */   extends HashMap<String, Object>
/*    */   implements BpmExecutionPluginSession
/*    */ {
/*    */   private static final long serialVersionUID = 4225343560381914372L;
/*    */   private Map<String, IBusinessData> boDatas;
/*    */   private EventType eventType;
/*    */   private VariableScope variableScope;
/*    */   private IBpmInstance bpmInstance;
/*    */   
/*    */   public DefaultBpmExecutionPluginSession() {
/* 35 */     BaseActionCmd submitCmd = (BaseActionCmd)BpmContext.submitActionModel();
/* 36 */     ActionType actionType = submitCmd.getActionType();
/* 37 */     put("actionCmd", BpmContext.getActionModel());
/* 38 */     put("currentUser", ContextUtil.getCurrentUser());
/* 39 */     put("currentGroup", ContextUtil.getCurrentGroup());
/* 40 */     put("bpmDefinition", submitCmd.getBpmDefinition());
/* 41 */     put("submitActionDesc", actionType.getName());
/* 42 */     put("submitActionName", actionType.getKey());
/* 43 */     put("isTask", Boolean.valueOf(false));
/* 44 */     put("destinations", submitCmd.getDestination());
/* 45 */     if (submitCmd instanceof DefualtTaskActionCmd) {
/* 46 */       DefualtTaskActionCmd taskCmd = (DefualtTaskActionCmd)submitCmd;
/* 47 */       put("submitOpinion", taskCmd.getOpinion());
/* 48 */       if (taskCmd.getBpmTask() != null) {
/* 49 */         put("isTask", Boolean.valueOf(true));
/* 50 */         put("submitTaskName", taskCmd.getBpmTask().getName());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, IBusinessData> getBoDatas() {
/* 57 */     return this.boDatas;
/*    */   }
/*    */   
/*    */   public void setBoDatas(Map<String, IBusinessData> boDatas) {
/* 61 */     putAll(boDatas);
/* 62 */     this.boDatas = boDatas;
/*    */   }
/*    */   
/*    */   public void setBusData(JSONObject busData) {
/* 66 */     if (busData == null || busData.keySet().size() == 0) {
/*    */       return;
/*    */     }
/* 69 */     for (String key : busData.keySet()) {
/* 70 */       put(key, busData.get(key));
/*    */     }
/*    */   }
/*    */   
/*    */   public IBpmInstance getBpmInstance() {
/* 75 */     return this.bpmInstance;
/*    */   }
/*    */   
/*    */   public void setBpmInstance(IBpmInstance bpmInstance) {
/* 79 */     put("bpmInstance", bpmInstance);
/* 80 */     this.bpmInstance = bpmInstance;
/*    */   }
/*    */ 
/*    */   
/*    */   public EventType getEventType() {
/* 85 */     return this.eventType;
/*    */   }
/*    */   
/*    */   public void setEventType(EventType eventType) {
/* 89 */     put("eventType", eventType.getKey());
/* 90 */     this.eventType = eventType;
/*    */   }
/*    */   
/*    */   public VariableScope getVariableScope() {
/* 94 */     return this.variableScope;
/*    */   }
/*    */   
/*    */   public void setVariableScope(VariableScope variableScope) {
/* 98 */     put("variableScope", variableScope);
/* 99 */     this.variableScope = variableScope;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/session/impl/DefaultBpmExecutionPluginSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */