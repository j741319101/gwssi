/*    */ package com.dstz.sys.core.model;
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
/*    */ public class SysConnectRecord
/*    */   extends BaseModel
/*    */ {
/*    */   protected String type;
/*    */   protected String sourceId;
/*    */   protected String targetId;
/*    */   protected String notice;
/*    */   
/*    */   public void setType(String type) {
/* 35 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 43 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSourceId(String sourceId) {
/* 50 */     this.sourceId = sourceId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSourceId() {
/* 58 */     return this.sourceId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTargetId(String targetId) {
/* 65 */     this.targetId = targetId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetId() {
/* 73 */     return this.targetId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNotice(String notice) {
/* 80 */     this.notice = notice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getNotice() {
/* 88 */     return this.notice;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysConnectRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */