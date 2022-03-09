/*    */ package cn.gwssi.ecloudframework.base.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum BaseStatusCode
/*    */   implements IStatusCode
/*    */ {
/* 11 */   SUCCESS("200", "成功"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   SYSTEM_ERROR("500", "系统异常"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   TIMEOUT("401", "访问超时"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   NO_ACCESS("403", "访问受限"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   PARAM_ILLEGAL("100", "参数校验不通过"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   DATA_EXISTS("101", "数据已存在"),
/*    */   
/* 38 */   NOT_SUPPORT("103", "不支持的方法"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   REMOTE_ERROR("102", "远程调用服务失败"),
/*    */   
/* 45 */   TABLE_NOT_FOUND("10001", "表不存在"),
/* 46 */   VIEW_NOT_FOUND("10002", "视图不存在"),
/*    */ 
/*    */ 
/*    */   
/* 50 */   DIFFERENT_LOGIN_USERS("10003", "登录用户和后台用户信息不一致");
/*    */ 
/*    */ 
/*    */   
/*    */   private String code;
/*    */ 
/*    */ 
/*    */   
/*    */   private String desc;
/*    */ 
/*    */ 
/*    */   
/*    */   private String system;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   BaseStatusCode(String code, String description) {
/* 68 */     this.code = code;
/* 69 */     this.desc = description;
/* 70 */     this.system = "BASE";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 75 */     return this.code;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 80 */     return this.desc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSystem() {
/* 85 */     return this.system;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/BaseStatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */