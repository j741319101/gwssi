/*    */ package com.dstz.bpm.core.model;
/*    */ 
/*    */ import com.dstz.base.core.model.BaseModel;
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
/*    */ 
/*    */ 
/*    */ public class BpmExecutionLink
/*    */   extends BaseModel
/*    */ {
/*    */   protected String executionId;
/*    */   protected String objId;
/*    */   protected String type;
/*    */   protected String remark;
/*    */   
/*    */   public void setExecutionId(String executionId) {
/* 35 */     this.executionId = executionId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getExecutionId() {
/* 44 */     return this.executionId;
/*    */   }
/*    */   
/*    */   public void setObjId(String objId) {
/* 48 */     this.objId = objId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getObjId() {
/* 57 */     return this.objId;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 61 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 70 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getRemark() {
/* 74 */     return this.remark;
/*    */   }
/*    */   
/*    */   public void setRemark(String remark) {
/* 78 */     this.remark = remark;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmExecutionLink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */