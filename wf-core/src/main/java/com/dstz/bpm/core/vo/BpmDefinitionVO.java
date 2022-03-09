/*    */ package com.dstz.bpm.core.vo;
/*    */ 
/*    */ import com.dstz.bpm.api.model.form.BpmForm;
/*    */ import com.dstz.bpm.core.model.BpmDefinition;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.dstz.base.core.util.BeanCopierUtils;
/*    */
import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ public class BpmDefinitionVO
/*    */   extends BpmDefinition
/*    */ {
/*    */   private String nodeTypeKey;
/*    */   private String nodeTypeName;
/*    */   @ApiModelProperty("流程实例表单")
/* 15 */   private BpmForm instForm = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiModelProperty("全局PC表单")
/* 21 */   private BpmForm globalForm = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiModelProperty("全局移动端表单")
/* 27 */   private BpmForm globalMobileForm = null;
/*    */ 
/*    */   
/*    */   public BpmDefinitionVO(DefaultBpmProcessDef processDef, BpmDefinition bpmDefinition) {
/* 31 */     BeanCopierUtils.copyProperties(bpmDefinition, this);
/* 32 */     setGlobalForm(processDef.getGlobalForm());
/* 33 */     setGlobalMobileForm(processDef.getGlobalMobileForm());
/* 34 */     setInstForm(processDef.getInstForm());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNodeTypeKey() {
/* 39 */     return this.nodeTypeKey;
/*    */   }
/*    */   public BpmDefinitionVO() {}
/*    */   public void setNodeTypeKey(String nodeTypeKey) {
/* 43 */     this.nodeTypeKey = nodeTypeKey;
/*    */   }
/*    */   
/*    */   public String getNodeTypeName() {
/* 47 */     return this.nodeTypeName;
/*    */   }
/*    */   
/*    */   public void setNodeTypeName(String nodeTypeName) {
/* 51 */     this.nodeTypeName = nodeTypeName;
/*    */   }
/*    */   
/*    */   public BpmForm getInstForm() {
/* 55 */     return this.instForm;
/*    */   }
/*    */   
/*    */   public void setInstForm(BpmForm instForm) {
/* 59 */     this.instForm = instForm;
/*    */   }
/*    */   
/*    */   public BpmForm getGlobalForm() {
/* 63 */     return this.globalForm;
/*    */   }
/*    */   
/*    */   public void setGlobalForm(BpmForm globalForm) {
/* 67 */     this.globalForm = globalForm;
/*    */   }
/*    */   
/*    */   public BpmForm getGlobalMobileForm() {
/* 71 */     return this.globalMobileForm;
/*    */   }
/*    */   
/*    */   public void setGlobalMobileForm(BpmForm globalMobileForm) {
/* 75 */     this.globalMobileForm = globalMobileForm;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/vo/BpmDefinitionVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */