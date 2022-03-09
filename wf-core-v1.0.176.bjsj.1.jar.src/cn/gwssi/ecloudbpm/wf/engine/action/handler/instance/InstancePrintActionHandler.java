/*    */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.instance;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.InstanceStatus;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.BuiltinActionHandler;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class InstancePrintActionHandler
/*    */   implements BuiltinActionHandler<BaseActionCmd>
/*    */ {
/*    */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 22 */     return (bpmInstance != null && StringUtil.isNotEmpty(bpmInstance.getStatus()) && 
/* 23 */       !StringUtils.equals(InstanceStatus.STATUS_DRAFT.getKey(), bpmInstance.getStatus()) && 
/* 24 */       !StringUtils.equals(InstanceStatus.STATUS_DISCARD.getKey(), bpmInstance.getStatus()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(BaseActionCmd model) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ActionType getActionType() {
/* 34 */     return ActionType.PRINT;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSn() {
/* 39 */     return 7;
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 44 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean isDefault() {
/* 49 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConfigPage() {
/* 54 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultGroovyScript() {
/* 59 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultBeforeScript() {
/* 64 */     return "_BtnThis.printForm(); return false;";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstancePrintActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */