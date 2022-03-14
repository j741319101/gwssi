/*    */ package cn.gwssi.ecloudbpm.form.api.constant;
/*    */ 
/*    */ import com.dstz.base.api.constant.IStatusCode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum FormStatusCode
/*    */   implements IStatusCode
/*    */ {
/* 10 */   FORM_ELEMENT_GENERATOR_ERROR("f10001", "表单element解析失败"),
/*    */ 
/*    */ 
/*    */   
/* 14 */   SUCCESS("200", "成功"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   SYSTEM_ERROR("500", "系统异常"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   TIMEOUT("401", "访问超时"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   NO_ACCESS("403", "访问受限"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   PARAM_ILLEGAL("100", "参数校验不通过"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   DATA_EXISTS("101", "数据已存在");
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
/*    */   FormStatusCode(String code, String description) {
/* 57 */     this.code = code;
/* 58 */     this.desc = description;
/* 59 */     this.system = "FORM";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 64 */     return this.code;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 69 */     return this.desc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSystem() {
/* 74 */     return this.system;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/constant/FormStatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */