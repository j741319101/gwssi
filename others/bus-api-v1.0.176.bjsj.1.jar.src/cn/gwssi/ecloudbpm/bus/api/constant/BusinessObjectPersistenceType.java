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
/*    */ 
/*    */ 
/*    */ public enum BusinessObjectPersistenceType
/*    */ {
/* 17 */   DB("db", "数据库"),
/*    */ 
/*    */ 
/*    */   
/* 21 */   HTTP("http", "远程http"),
/*    */ 
/*    */ 
/*    */   
/* 25 */   BEAN("bean", "ServiceBean"),
/*    */ 
/*    */ 
/*    */   
/* 29 */   FEIGN("feign", "Feign接口");
/*    */   private String key;
/*    */   private String desc;
/*    */   
/*    */   BusinessObjectPersistenceType(String key, String desc) {
/* 34 */     this.key = key;
/* 35 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 39 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 43 */     return this.desc;
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
/* 55 */     return this.key.equals(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusinessObjectPersistenceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */