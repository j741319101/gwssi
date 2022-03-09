/*    */ package cn.gwssi.ecloudbpm.bus.api.constant;
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
/*    */ public enum BusTableRelFkType
/*    */ {
/* 15 */   PARENT_FIELD("parentField", "子表外键 对应 父实例字段"),
/*    */ 
/*    */ 
/*    */   
/* 19 */   FIXED_VALUE("fixedValue", "固定值"),
/*    */ 
/*    */ 
/*    */   
/* 23 */   CHILD_FIELD("childField", "子表字段 对应 父实例外键");
/*    */   private String key;
/*    */   private String desc;
/*    */   
/*    */   BusTableRelFkType(String key, String desc) {
/* 28 */     this.key = key;
/* 29 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 33 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 37 */     return this.desc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsWithKey(String key) {
/* 49 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusTableRelFkType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */