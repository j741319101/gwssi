/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.sys.api.model.ISysFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SysFile
/*     */   extends BaseModel
/*     */   implements ISysFile
/*     */ {
/*     */   private String name;
/*     */   private String uploader;
/*     */   private String path;
/*     */   private String remark;
/*     */   private String instId;
/*     */   private String creator;
/*  51 */   private Integer type = Integer.valueOf(0);
/*     */   
/*     */   public String getCreator() {
/*  54 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(String creator) {
/*  58 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  62 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  66 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getUploader() {
/*  70 */     return this.uploader;
/*     */   }
/*     */   
/*     */   public void setUploader(String uploader) {
/*  74 */     this.uploader = uploader;
/*     */   }
/*     */   
/*     */   public String getPath() {
/*  78 */     return this.path;
/*     */   }
/*     */   
/*     */   public void setPath(String path) {
/*  82 */     this.path = path;
/*     */   }
/*     */   
/*     */   public String getRemark() {
/*  86 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/*  90 */     this.remark = remark;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/*  94 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  98 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public Integer getType() {
/* 102 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Integer type) {
/* 106 */     this.type = type;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */