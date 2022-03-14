/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
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
/*     */ public class BpmSubmitDataLog
/*     */   extends BaseModel
/*     */ {
/*     */   protected String taskId;
/*     */   protected String instId;
/*     */   protected String data;
/*     */   protected String destination;
/*     */   protected String extendConf;
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  39 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/*  47 */     return this.taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInstId(String instId) {
/*  54 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/*  62 */     return this.instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(String data) {
/*  69 */     this.data = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getData() {
/*  77 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(String destination) {
/*  84 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestination() {
/*  92 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtendConf(String extendConf) {
/*  99 */     this.extendConf = extendConf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendConf() {
/* 107 */     return this.extendConf;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmSubmitDataLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */