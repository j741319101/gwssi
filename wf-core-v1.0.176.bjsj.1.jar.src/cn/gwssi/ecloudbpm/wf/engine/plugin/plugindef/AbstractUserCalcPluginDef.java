/*    */ package cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ExtractType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.constant.LogicType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractUserCalcPluginDef
/*    */   implements BpmUserCalcPluginDef
/*    */ {
/* 14 */   private ExtractType extractType = ExtractType.EXACT_NOEXACT;
/* 15 */   private LogicType logicType = LogicType.OR;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtractType getExtract() {
/* 24 */     return this.extractType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExtract(ExtractType type) {
/* 35 */     this.extractType = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LogicType getLogicCal() {
/* 45 */     return this.logicType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogicCal(LogicType logicType) {
/* 55 */     this.logicType = logicType;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/plugindef/AbstractUserCalcPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */