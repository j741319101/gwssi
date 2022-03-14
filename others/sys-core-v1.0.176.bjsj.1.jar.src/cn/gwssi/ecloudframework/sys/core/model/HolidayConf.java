/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import java.util.Date;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
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
/*     */ public class HolidayConf
/*     */   implements IDModel
/*     */ {
/*     */   protected String id;
/*     */   @NotBlank
/*     */   protected String name;
/*     */   protected String system;
/*     */   @NotEmpty
/*     */   protected Integer year;
/*     */   @NotEmpty
/*     */   protected Date startDay;
/*     */   @NotEmpty
/*     */   protected Date endDay;
/*     */   @NotBlank
/*     */   protected String type;
/*     */   protected String remark;
/*     */   
/*     */   public void setId(String id) {
/*  63 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  71 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  75 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  83 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setSystem(String system) {
/*  87 */     this.system = system;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystem() {
/*  95 */     return this.system;
/*     */   }
/*     */   
/*     */   public void setYear(Integer year) {
/*  99 */     this.year = year;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getYear() {
/* 107 */     return this.year;
/*     */   }
/*     */   
/*     */   public void setStartDay(Date startDay) {
/* 111 */     this.startDay = startDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getStartDay() {
/* 119 */     return this.startDay;
/*     */   }
/*     */   
/*     */   public void setEndDay(Date endDay) {
/* 123 */     this.endDay = endDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getEndDay() {
/* 131 */     return this.endDay;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 135 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 143 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemark() {
/* 149 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/* 153 */     this.remark = remark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return (new ToStringBuilder(this))
/* 161 */       .append("id", this.id)
/* 162 */       .append("name", this.name)
/* 163 */       .append("system", this.system)
/* 164 */       .append("year", this.year)
/* 165 */       .append("startDay", this.startDay)
/* 166 */       .append("endDay", this.endDay)
/* 167 */       .append("type", this.type)
/* 168 */       .append("remark", this.remark)
/* 169 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/HolidayConf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */