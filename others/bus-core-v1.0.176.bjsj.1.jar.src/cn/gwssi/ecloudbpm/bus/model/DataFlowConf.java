/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowConf;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataFlowConf
/*    */   implements IDataFlowConf
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   private Set<String> delOpr;
/*    */   private Set<String> modOpr;
/*    */   private Set<String> addOpr;
/*    */   
/*    */   public Set<String> getDelOpr() {
/* 32 */     return this.delOpr;
/*    */   }
/*    */   
/*    */   public void setDelOpr(Set<String> delOpr) {
/* 36 */     this.delOpr = delOpr;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getModOpr() {
/* 41 */     return this.modOpr;
/*    */   }
/*    */   
/*    */   public void setModOpr(Set<String> modOpr) {
/* 45 */     this.modOpr = modOpr;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getAddOpr() {
/* 50 */     return this.addOpr;
/*    */   }
/*    */   
/*    */   public void setAddOpr(Set<String> addOpr) {
/* 54 */     this.addOpr = addOpr;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/DataFlowConf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */