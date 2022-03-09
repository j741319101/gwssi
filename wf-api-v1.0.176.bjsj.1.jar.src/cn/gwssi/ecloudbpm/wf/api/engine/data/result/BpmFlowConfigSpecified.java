/*    */ package cn.gwssi.ecloudbpm.wf.api.engine.data.result;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ @ApiModel(description = "流程指定的一些配置项，用于流程提交时个性化")
/*    */ public class BpmFlowConfigSpecified
/*    */ {
/*    */   @ApiModelProperty("是否意见必填")
/*    */   private Boolean requiredOpinion;
/*    */   @ApiModelProperty("正文配置信息")
/*    */   protected Map<String, Object> officialDocument;
/*    */   protected String officialPrintTemplate;
/*    */   
/*    */   public String getOfficialPrintTemplate() {
/* 18 */     return this.officialPrintTemplate;
/*    */   }
/*    */   
/*    */   public void setOfficialPrintTemplate(String officialPrintTemplate) {
/* 22 */     this.officialPrintTemplate = officialPrintTemplate;
/*    */   }
/*    */   
/*    */   public Boolean getRequiredOpinion() {
/* 26 */     return this.requiredOpinion;
/*    */   }
/*    */   
/*    */   public void setRequiredOpinion(Boolean requiredOpinion) {
/* 30 */     this.requiredOpinion = requiredOpinion;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getOfficialDocument() {
/* 34 */     return this.officialDocument;
/*    */   }
/*    */   
/*    */   public void setOfficialDocument(Map<String, Object> officialDocument) {
/* 38 */     this.officialDocument = officialDocument;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/BpmFlowConfigSpecified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */