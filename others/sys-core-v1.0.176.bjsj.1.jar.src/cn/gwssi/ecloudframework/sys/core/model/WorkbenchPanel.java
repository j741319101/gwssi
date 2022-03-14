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
/*     */ public class WorkbenchPanel
/*     */   extends BaseModel
/*     */ {
/*     */   public static final String DATA_TYPE_INTERFACE = "interface";
/*     */   public static final String DATA_TYPE_CUST_QUERY = "custQuery";
/*     */   protected String alias;
/*     */   protected String name;
/*     */   protected String type;
/*     */   protected String desc;
/*     */   protected String dataType;
/*     */   protected String dataSource;
/*     */   protected Integer autoRefresh;
/*     */   protected Integer width;
/*     */   protected Integer height;
/*     */   protected String displayContent;
/*     */   protected String moreUrl;
/*     */   protected String system;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   protected String deleteFlag;
/*     */   protected Integer custWidth;
/*     */   protected Integer custHeight;
/*     */   protected String custLayOutId;
/*     */   
/*     */   public void setId(String id) {
/* 117 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 127 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/* 131 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlias() {
/* 140 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 144 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 153 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 157 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 166 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 170 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 179 */     return this.desc;
/*     */   }
/*     */   
/*     */   public Integer getCustWidth() {
/* 183 */     return this.custWidth;
/*     */   }
/*     */   
/*     */   public void setCustWidth(Integer custWidth) {
/* 187 */     this.custWidth = custWidth;
/*     */   }
/*     */   
/*     */   public Integer getCustHeight() {
/* 191 */     return this.custHeight;
/*     */   }
/*     */   
/*     */   public void setCustHeight(Integer custHeight) {
/* 195 */     this.custHeight = custHeight;
/*     */   }
/*     */   
/*     */   public void setDataType(String dataType) {
/* 199 */     this.dataType = dataType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDataType() {
/* 208 */     return this.dataType;
/*     */   }
/*     */   
/*     */   public void setDataSource(String dataSource) {
/* 212 */     this.dataSource = dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDataSource() {
/* 221 */     return this.dataSource;
/*     */   }
/*     */   
/*     */   public void setAutoRefresh(Integer autoRefresh) {
/* 225 */     this.autoRefresh = autoRefresh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getAutoRefresh() {
/* 234 */     return this.autoRefresh;
/*     */   }
/*     */   
/*     */   public void setWidth(Integer width) {
/* 238 */     this.width = width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getWidth() {
/* 247 */     return this.width;
/*     */   }
/*     */   
/*     */   public String getCustLayOutId() {
/* 251 */     return this.custLayOutId;
/*     */   }
/*     */   
/*     */   public void setCustLayOutId(String custLayOutId) {
/* 255 */     this.custLayOutId = custLayOutId;
/*     */   }
/*     */   
/*     */   public void setHeight(Integer height) {
/* 259 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getHeight() {
/* 268 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setDisplayContent(String displayContent) {
/* 272 */     this.displayContent = displayContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayContent() {
/* 281 */     return this.displayContent;
/*     */   }
/*     */   
/*     */   public void setMoreUrl(String moreUrl) {
/* 285 */     this.moreUrl = moreUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMoreUrl() {
/* 294 */     return this.moreUrl;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 299 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 309 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 314 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 324 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 329 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 339 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 344 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 354 */     return this.updateBy;
/*     */   }
/*     */   
/*     */   public void setDeleteFlag(String deleteFlag) {
/* 358 */     this.deleteFlag = deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeleteFlag() {
/* 367 */     return this.deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 375 */     return (new ToStringBuilder(this))
/* 376 */       .append("id", this.id)
/* 377 */       .append("alias", this.alias)
/* 378 */       .append("name", this.name)
/* 379 */       .append("type", this.type)
/* 380 */       .append("desc", this.desc)
/* 381 */       .append("dataType", this.dataType)
/* 382 */       .append("dataSource", this.dataSource)
/* 383 */       .append("autoRefresh", this.autoRefresh)
/* 384 */       .append("width", this.width)
/* 385 */       .append("height", this.height)
/* 386 */       .append("displayContent", this.displayContent)
/* 387 */       .append("moreUrl", this.moreUrl)
/* 388 */       .append("createTime", this.createTime)
/* 389 */       .append("createBy", this.createBy)
/* 390 */       .append("updateTime", this.updateTime)
/* 391 */       .append("updateBy", this.updateBy)
/* 392 */       .append("deleteFlag", this.deleteFlag)
/* 393 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/WorkbenchPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */