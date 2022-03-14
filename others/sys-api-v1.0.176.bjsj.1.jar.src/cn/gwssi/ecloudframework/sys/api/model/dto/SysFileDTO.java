/*    */ package com.dstz.sys.api.model.dto;
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
/*    */ public class SysFileDTO
/*    */ {
/*    */   protected String id;
/*    */   private String name;
/*    */   private String uploader;
/*    */   private String path;
/*    */   private String instId;
/*    */   private String remark;
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 50 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getUploader() {
/* 54 */     return this.uploader;
/*    */   }
/*    */   
/*    */   public void setUploader(String uploader) {
/* 58 */     this.uploader = uploader;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 62 */     return this.path;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 66 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 70 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 74 */     this.path = path;
/*    */   }
/*    */   
/*    */   public String getInstId() {
/* 78 */     return this.instId;
/*    */   }
/*    */   
/*    */   public void setInstId(String instId) {
/* 82 */     this.instId = instId;
/*    */   }
/*    */   
/*    */   public String getRemark() {
/* 86 */     return this.remark;
/*    */   }
/*    */   
/*    */   public void setRemark(String remark) {
/* 90 */     this.remark = remark;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/dto/SysFileDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */