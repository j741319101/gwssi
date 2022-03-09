/*    */ package cn.gwssi.ecloudframework.sys.api.constant;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.IStatusCode;
/*    */ 
/*    */ public enum SysStatusCode
/*    */   implements IStatusCode {
/*  7 */   SUCCESS("200", "成功"),
/*  8 */   SYSTEM_ERROR("500", "系统异常"),
/*  9 */   TIMEOUT("401", "访问超时"),
/* 10 */   NO_ACCESS("403", "访问受限"),
/* 11 */   PARAM_ILLEGAL("100", "参数校验不通过"),
/*    */ 
/*    */   
/* 14 */   SERIALNO_EXSIT("50001", "流水号已存在"),
/* 15 */   SERIALNO_NO_EXSIT("50002", "流水号不存在");
/*    */   
/*    */   private String code;
/*    */   private String desc;
/*    */   private String system;
/*    */   
/*    */   SysStatusCode(String code, String description) {
/* 22 */     setCode(code);
/* 23 */     setDesc(description);
/* 24 */     setSystem("SYS");
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 28 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 32 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 36 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String msg) {
/* 40 */     this.desc = msg;
/*    */   }
/*    */   
/*    */   public String getSystem() {
/* 44 */     return this.system;
/*    */   }
/*    */   
/*    */   public void setSystem(String system) {
/* 48 */     this.system = system;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/constant/SysStatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */