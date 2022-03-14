/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorkbenchLayout
/*     */   extends BaseModel
/*     */ {
/*     */   public static final String DEFAULT_LAYOUT = "default_layout";
/*     */   protected String id;
/*     */   protected String panelId;
/*     */   protected Integer custWidth;
/*     */   protected Integer custHeight;
/*     */   protected Integer sn;
/*     */   protected String userId;
/*     */   protected Date createTime;
/*     */   
/*     */   public void setId(String id) {
/*  50 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  59 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setPanelId(String panelId) {
/*  63 */     this.panelId = panelId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPanelId() {
/*  72 */     return this.panelId;
/*     */   }
/*     */   
/*     */   public void setCustWidth(Integer custWidth) {
/*  76 */     this.custWidth = custWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCustWidth() {
/*  85 */     return this.custWidth;
/*     */   }
/*     */   
/*     */   public void setCustHeight(Integer custHeight) {
/*  89 */     this.custHeight = custHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCustHeight() {
/*  98 */     return this.custHeight;
/*     */   }
/*     */   
/*     */   public void setSn(Integer sn) {
/* 102 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 111 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setUserId(String userId) {
/* 115 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 124 */     return this.userId;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 128 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 137 */     return this.createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return (new ToStringBuilder(this))
/* 145 */       .append("id", this.id)
/* 146 */       .append("panelId", this.panelId)
/* 147 */       .append("custWidth", this.custWidth)
/* 148 */       .append("custHeight", this.custHeight)
/* 149 */       .append("sn", this.sn)
/* 150 */       .append("userId", this.userId)
/* 151 */       .append("createTime", this.createTime)
/* 152 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/WorkbenchLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */