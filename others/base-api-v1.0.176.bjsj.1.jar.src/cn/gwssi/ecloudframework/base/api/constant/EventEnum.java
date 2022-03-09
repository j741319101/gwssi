/*    */ package cn.gwssi.ecloudframework.base.api.constant;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EventEnum
/*    */ {
/* 14 */   ADD_USER("add_user", "org_custom", "用户模块-添加用户");
/*    */ 
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */ 
/*    */   
/*    */   private String module;
/*    */ 
/*    */ 
/*    */   
/*    */   private String key;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   EventEnum(String key, String module, String desc) {
/* 32 */     this.key = key;
/* 33 */     this.module = module;
/* 34 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 38 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 42 */     return this.desc;
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
/* 54 */     return this.key.equals(key);
/*    */   }
/*    */   
/*    */   public static EventEnum getByKey(String key) {
/* 58 */     for (EventEnum type : values()) {
/* 59 */       if (type.getKey().equals(key)) {
/* 60 */         return type;
/*    */       }
/*    */     } 
/* 63 */     throw new BusinessException(String.format("找不到key为[%s]的模块", new Object[] { key }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsWithModule(String module) {
/* 74 */     return this.module.equals(module);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/EventEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */