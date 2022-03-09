/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusFormula;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusFormula
/*    */   implements Serializable, IBusFormula
/*    */ {
/*    */   private String id;
/*    */   private String name;
/*    */   private String rawFormula;
/*    */   private String formula;
/*    */   
/*    */   public String getId() {
/* 41 */     return this.id;
/*    */   }
/*    */   
/*    */   public BusFormula setId(String id) {
/* 45 */     this.id = id;
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return this.name;
/*    */   }
/*    */   
/*    */   public BusFormula setName(String name) {
/* 55 */     this.name = name;
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRawFormula() {
/* 61 */     return this.rawFormula;
/*    */   }
/*    */   
/*    */   public BusFormula setRawFormula(String rawFormula) {
/* 65 */     this.rawFormula = rawFormula;
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFormula() {
/* 71 */     return this.formula;
/*    */   }
/*    */   
/*    */   public BusFormula setFormula(String formula) {
/* 75 */     this.formula = formula;
/* 76 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusFormula.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */