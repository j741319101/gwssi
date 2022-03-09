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
/*    */ public enum BusTableRelType
/*    */ {
/* 16 */   MAIN("main", "主表"),
/*    */ 
/*    */ 
/*    */   
/* 20 */   ONE_TO_ONE("oneToOne", "一对一"),
/*    */ 
/*    */ 
/*    */   
/* 24 */   ONE_TO_MANY("oneToMany", "一对多");
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */ 
/*    */   
/*    */   BusTableRelType(String key, String desc) {
/* 35 */     this.key = key;
/* 36 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 40 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 44 */     return this.desc;
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
/* 56 */     return this.key.equals(key);
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
/*    */   public static BusTableRelType getByKey(String key) {
/* 68 */     for (BusTableRelType type : values()) {
/* 69 */       if (key.equals(type.getKey())) {
/* 70 */         return type;
/*    */       }
/*    */     } 
/* 73 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusTableRelType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */