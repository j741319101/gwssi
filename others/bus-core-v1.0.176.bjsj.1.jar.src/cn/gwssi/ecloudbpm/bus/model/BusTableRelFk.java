/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRelFk;
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
/*    */ public class BusTableRelFk
/*    */   implements Serializable, IBusTableRelFk
/*    */ {
/*    */   private String from;
/*    */   private String type;
/*    */   private String value;
/*    */   
/*    */   public String getFrom() {
/* 31 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(String from) {
/* 35 */     this.from = from;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 44 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 49 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 53 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusTableRelFk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */