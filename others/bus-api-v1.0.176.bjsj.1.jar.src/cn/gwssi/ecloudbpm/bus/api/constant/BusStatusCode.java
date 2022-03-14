/*    */ package cn.gwssi.ecloudbpm.bus.api.constant;
/*    */ 
/*    */ import com.dstz.base.api.constant.IStatusCode;
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
/*    */ public enum BusStatusCode
/*    */   implements IStatusCode
/*    */ {
/* 17 */   PARAM_ILLEGAL("100", "参数校验不通过"),
/*    */ 
/*    */ 
/*    */   
/* 21 */   PERSISTENCE_HTTP_ERR("101", "http持久方式异常"),
/*    */ 
/*    */ 
/*    */   
/* 25 */   PERSISTENCE_BEAN_ERR("101", "serviceBean持久方式异常"),
/*    */ 
/*    */ 
/*    */   
/* 29 */   BUS_DATA_LOSE("60001", "业务数据丢失");
/*    */   
/*    */   private String code;
/*    */   private String desc;
/*    */   private String system;
/*    */   
/*    */   BusStatusCode(String code, String desc) {
/* 36 */     this.code = code;
/* 37 */     this.desc = desc;
/* 38 */     this.system = "BUS";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 43 */     return this.code;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 48 */     return this.desc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSystem() {
/* 53 */     return this.system;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusStatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */