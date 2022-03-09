/*    */ package cn.gwssi.ecloudbpm.bus.executor.assemblyval;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*    */ import com.alibaba.fastjson.JSONObject;
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
/*    */ public class AssemblyValParam
/*    */ {
/*    */   private JSONObject data;
/*    */   private BusinessData businessData;
/*    */   
/*    */   public AssemblyValParam(JSONObject data, BusinessData businessData) {
/* 21 */     this.data = data;
/* 22 */     this.businessData = businessData;
/*    */   }
/*    */   
/*    */   public JSONObject getData() {
/* 26 */     return this.data;
/*    */   }
/*    */   
/*    */   public BusinessData getBusinessData() {
/* 30 */     return this.businessData;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/executor/assemblyval/AssemblyValParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */