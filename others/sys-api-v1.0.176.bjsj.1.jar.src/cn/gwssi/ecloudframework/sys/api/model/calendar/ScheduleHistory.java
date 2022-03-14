/*    */ package com.dstz.sys.api.model.calendar;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScheduleHistory
/*    */ {
/*    */   private String id;
/*    */   private String scheduleId;
/*    */   private int rateProgress;
/*    */   private String submit;
/*    */   private String submitName;
/*    */   private Date submitTime;
/*    */   
/*    */   public String getId() {
/* 17 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getScheduleId() {
/* 25 */     return this.scheduleId;
/*    */   }
/*    */   
/*    */   public void setScheduleId(String scheduleId) {
/* 29 */     this.scheduleId = scheduleId;
/*    */   }
/*    */   
/*    */   public int getRateProgress() {
/* 33 */     return this.rateProgress;
/*    */   }
/*    */   
/*    */   public void setRateProgress(int rateProgress) {
/* 37 */     this.rateProgress = rateProgress;
/*    */   }
/*    */   
/*    */   public String getSubmit() {
/* 41 */     return this.submit;
/*    */   }
/*    */   
/*    */   public void setSubmit(String submit) {
/* 45 */     this.submit = submit;
/*    */   }
/*    */   
/*    */   public String getSubmitName() {
/* 49 */     return this.submitName;
/*    */   }
/*    */   
/*    */   public void setSubmitName(String submitName) {
/* 53 */     this.submitName = submitName;
/*    */   }
/*    */   
/*    */   public Date getSubmitTime() {
/* 57 */     return this.submitTime;
/*    */   }
/*    */   
/*    */   public void setSubmitTime(Date submitTime) {
/* 61 */     this.submitTime = submitTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return "ScheduleHistory{id='" + this.id + '\'' + ", scheduleId='" + this.scheduleId + '\'' + ", submit='" + this.submit + '\'' + ", submitName='" + this.submitName + '\'' + ", submitTime=" + this.submitTime + '}';
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/calendar/ScheduleHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */