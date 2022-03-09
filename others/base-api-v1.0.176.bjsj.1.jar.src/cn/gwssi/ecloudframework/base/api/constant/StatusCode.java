/*    */ package cn.gwssi.ecloudframework.base.api.constant;
/*    */ 
/*    */ public class StatusCode
/*    */   implements IStatusCode {
/*    */   private String code;
/*    */   private String system;
/*    */   private String desc;
/*    */   
/*    */   public StatusCode(IStatusCode statusCode) {
/* 10 */     this.code = statusCode.getCode();
/* 11 */     this.system = statusCode.getSystem();
/* 12 */     this.desc = statusCode.getDesc();
/*    */   }
/*    */ 
/*    */   
/*    */   public StatusCode() {}
/*    */   
/*    */   public StatusCode(String code, String desc) {
/* 19 */     this.code = code;
/* 20 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public StatusCode(String code, String desc, String system) {
/* 24 */     this.code = code;
/* 25 */     this.desc = desc;
/* 26 */     this.system = system;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 34 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String getSystem() {
/* 38 */     return this.system;
/*    */   }
/*    */   
/*    */   public void setSystem(String system) {
/* 42 */     this.system = system;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 46 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 50 */     this.desc = desc;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/constant/StatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */