/*    */ package cn.gwssi.ecloudbpm.wf.api.model.remote;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApiModel(description = "表单处理器执行后返回结果")
/*    */ public class FormHandlerResult
/*    */ {
/*    */   @ApiModelProperty("需要设置更新的流程变量")
/* 17 */   private Map<String, Object> variables = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiModelProperty("第一次保存后需要反回 业务数据的主键")
/*    */   private String bizId;
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getVariables() {
/* 27 */     return this.variables;
/*    */   }
/*    */   
/*    */   public void setVariables(Map<String, Object> variables) {
/* 31 */     this.variables = variables;
/*    */   }
/*    */   
/*    */   public String getBizId() {
/* 35 */     return this.bizId;
/*    */   }
/*    */   
/*    */   public void setBizId(String bizId) {
/* 39 */     this.bizId = bizId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/remote/FormHandlerResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */