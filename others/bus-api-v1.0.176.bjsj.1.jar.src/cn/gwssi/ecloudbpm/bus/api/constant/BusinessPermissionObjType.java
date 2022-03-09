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
/*    */ 
/*    */ public enum BusinessPermissionObjType
/*    */ {
/* 18 */   FORM("form", "表单"),
/*    */ 
/*    */ 
/*    */   
/* 22 */   FLOW("flow", "流程"),
/*    */ 
/*    */ 
/*    */   
/* 26 */   FLOW_NODE("flowNode", "流程节点");
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */ 
/*    */   
/*    */   BusinessPermissionObjType(String key, String desc) {
/* 37 */     this.key = key;
/* 38 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 42 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 46 */     return this.desc;
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
/* 58 */     return this.key.equals(key);
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
/*    */   public static BusinessPermissionObjType getByKey(String key) {
/* 70 */     for (BusinessPermissionObjType value : values()) {
/* 71 */       if (value.key.equals(key)) {
/* 72 */         return value;
/*    */       }
/*    */     } 
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusinessPermissionObjType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */