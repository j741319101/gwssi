/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusDataFlowRel;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusDataFlowRel
/*    */   implements Serializable, IBusDataFlowRel
/*    */ {
/*    */   private String tableKey;
/*    */   private List<BusFormula> formulas;
/*    */   private List<BusDataFlowRel> dataFlowLinks;
/*    */   
/*    */   public String getTableKey() {
/* 36 */     return this.tableKey;
/*    */   }
/*    */   
/*    */   public void setTableKey(String tableKey) {
/* 40 */     this.tableKey = tableKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BusFormula> getFormulas() {
/* 45 */     if (this.formulas == null) {
/* 46 */       return Collections.emptyList();
/*    */     }
/* 48 */     return this.formulas;
/*    */   }
/*    */   
/*    */   public BusDataFlowRel setFormulas(List<BusFormula> formulas) {
/* 52 */     this.formulas = formulas;
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BusDataFlowRel> getDataFlowLinks() {
/* 58 */     if (this.dataFlowLinks == null) {
/* 59 */       return Collections.emptyList();
/*    */     }
/* 61 */     return this.dataFlowLinks;
/*    */   }
/*    */   
/*    */   public BusDataFlowRel setDataFlowLinks(List<BusDataFlowRel> dataFlowLinks) {
/* 65 */     this.dataFlowLinks = dataFlowLinks;
/* 66 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusDataFlowRel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */