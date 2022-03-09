/*    */ package cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmVariableDef;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserTaskNodeDef
/*    */   extends BaseBpmNodeDef
/*    */ {
/*    */   private List<BpmVariableDef> variableList;
/*    */   
/*    */   public List<BpmVariableDef> getVariableList() {
/* 16 */     return this.variableList;
/*    */   }
/*    */   
/*    */   public void setVariableList(List<BpmVariableDef> variableList) {
/* 20 */     this.variableList = variableList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/impl/UserTaskNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */