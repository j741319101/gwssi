/*    */ package cn.gwssi.ecloudbpm.bus.executor.parseval;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*    */ import java.util.Map;
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
/*    */ public class ParseValParam
/*    */ {
/*    */   private String key;
/*    */   private Object value;
/*    */   private Map<String, Object> data;
/*    */   private BusTableRel busTableRel;
/*    */   
/*    */   public ParseValParam(String key, Object value, Map<String, Object> data, BusTableRel busTableRel) {
/* 24 */     this.key = key;
/* 25 */     this.value = value;
/* 26 */     this.data = data;
/* 27 */     this.busTableRel = busTableRel;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 31 */     return this.key;
/*    */   }
/*    */   
/*    */   public Object getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getData() {
/* 39 */     return this.data;
/*    */   }
/*    */   
/*    */   public BusTableRel getBusTableRel() {
/* 43 */     return this.busTableRel;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/executor/parseval/ParseValParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */