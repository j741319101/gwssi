/*     */ package cn.gwssi.ecloudframework.sys.api.model.calendar;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
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
/*     */ public class WorkCalenDar
/*     */   implements IDModel
/*     */ {
/*     */   public static final String DAY_TYPE_WORKDAY = "DW";
/*     */   public static final String DAY_TYPE_WEEKEND = "DR";
/*     */   public static final String DAY_TYPE_LEGAL_HOLIDAY = "LR";
/*     */   public static final String DAY_TYPE_LEGAL_WORKDAY = "LW";
/*     */   public static final String DAY_TYPE_COMPAY_HOLIDAY = "CR";
/*     */   public static final String DAY_TYPE_COMPAY_WORKDAY = "CW";
/*     */   public static final String WORKDAY = "1";
/*     */   public static final String HOLIDAY = "0";
/*     */   public static final String SYSTEM_PUBLIC = "public";
/*     */   public static final String SYSTEM_DDJF = "ddjf";
/*     */   protected String id;
/*     */   protected Date day;
/*     */   protected String isWorkDay;
/*     */   protected String type;
/*  51 */   protected String system = "public";
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  55 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  63 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setDay(Date day) {
/*  67 */     this.day = day;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDay() {
/*  75 */     return this.day;
/*     */   }
/*     */   
/*     */   public void setIsWorkDay(String isWorkDay) {
/*  79 */     this.isWorkDay = isWorkDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIsWorkDay() {
/*  87 */     return this.isWorkDay;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  91 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  99 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setSystem(String system) {
/* 103 */     this.system = system;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystem() {
/* 111 */     return this.system;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return (new ToStringBuilder(this))
/* 118 */       .append("id", this.id)
/* 119 */       .append("day", this.day)
/* 120 */       .append("isWorkDay", this.isWorkDay)
/* 121 */       .append("type", this.type)
/* 122 */       .append("system", this.system)
/* 123 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWorkDay() {
/* 128 */     return "1".equals(this.isWorkDay);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/WorkCalenDar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */