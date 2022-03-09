/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.IBaseModel;
/*     */ import java.util.Date;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*     */ public class BpmTaskCandidate
/*     */   implements IBaseModel
/*     */ {
/*     */   protected String id;
/*     */   protected String taskId;
/*     */   protected String type;
/*     */   protected String executor;
/*     */   protected String instId;
/*     */   
/*     */   public void setId(String id) {
/*  44 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  52 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  56 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/*  64 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  68 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  76 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setExecutor(String executor) {
/*  80 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExecutor() {
/*  88 */     return this.executor;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  92 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 100 */     return this.instId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return (new ToStringBuilder(this))
/* 107 */       .append("id", this.id)
/* 108 */       .append("taskId", this.taskId)
/* 109 */       .append("type", this.type)
/* 110 */       .append("executor", this.executor)
/* 111 */       .append("instId", this.instId)
/* 112 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createtime) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updatetime) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public void setUpdateBy(String updateBy) {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTaskCandidate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */