/*     */ package com.dstz.sys.service.impl;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.sys.api.model.calendar.WorkCalenDar;
/*     */ import com.dstz.sys.api.service.CalendarService;
/*     */ import com.dstz.sys.core.manager.WorkCalenDarManager;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class DefaultCalendarService
/*     */   implements CalendarService
/*     */ {
/*  28 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private WorkCalenDarManager workCalenDarManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr("获取日历信息失败")
/*     */   public ResultMsg<WorkCalenDar> getWorkCalenDarByDay(Date day) {
/*  40 */     WorkCalenDar workCalenDar = this.workCalenDarManager.getByDayAndSystem(day, "public");
/*  41 */     return new ResultMsg(workCalenDar);
/*     */   }
/*     */   
/*     */   @CatchErr("获取日历信息失败")
/*     */   public ResultMsg<WorkCalenDar> getWorkCalenDarByDay(Date day, String system) {
/*  46 */     WorkCalenDar workCalenDar = this.workCalenDarManager.getByDayAndSystem(day, system);
/*  47 */     return new ResultMsg(workCalenDar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg<List<WorkCalenDar>> getWorkCalenDars(Date startDay, Date endDay) {
/*  59 */     if (startDay.after(endDay)) {
/*  60 */       throw new BusinessMessage("开始日期不应该晚于结束日期", BaseStatusCode.PARAM_ILLEGAL);
/*     */     }
/*  62 */     List workCalenDars = this.workCalenDarManager.getByTime(startDay, endDay);
/*     */     
/*  64 */     return new ResultMsg(workCalenDars);
/*     */   }
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg<List<WorkCalenDar>> getWorkCalenDars(Date startDay, Date endDay, String system) {
/*  69 */     if (startDay.after(endDay)) {
/*  70 */       throw new BusinessMessage("开始日期不应该晚于结束日期", BaseStatusCode.PARAM_ILLEGAL);
/*     */     }
/*  72 */     List workCalenDars = this.workCalenDarManager.getByTimeContainPublic(startDay, endDay, system);
/*  73 */     return new ResultMsg(workCalenDars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg<Date> getEndWorkDay(Date startDay, int days) {
/*  84 */     Date date = this.workCalenDarManager.getWorkDayByDays(startDay, days);
/*  85 */     return new ResultMsg(date);
/*     */   }
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg<Date> getEndWorkDay(Date startDay, int days, String system) {
/*  91 */     Date date = this.workCalenDarManager.getWorkDayByDays(startDay, days, system);
/*  92 */     return new ResultMsg(date);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultMsg<Date> getEndWorkDayByMinute(Date startDay, int minute) {
/*  97 */     if (minute < 1) throw new BusinessMessage("minute  canot be null ");
/*     */     
/*  99 */     Calendar startDayHours = Calendar.getInstance();
/* 100 */     startDayHours.setTime(startDay);
/*     */     
/* 102 */     minute += startDayHours.get(11) * 60;
/* 103 */     minute += startDayHours.get(12);
/*     */ 
/*     */     
/* 106 */     int days = (new Double(Math.floor((minute / 1440)))).intValue();
/* 107 */     int hours = (new Double(Math.floor(((minute - days * 1440) / 60)))).intValue();
/* 108 */     int minutes = minute - days * 1440 - hours * 60;
/*     */     
/* 110 */     Date calcDate = this.workCalenDarManager.getWorkDayByDays(startDay, days);
/* 111 */     if (calcDate == null) {
/* 112 */       this.LOG.warn("日期计算异常！ getEndWorkDayByMinute  {} {} ", startDay, Integer.valueOf(minute));
/* 113 */       return new ResultMsg(null);
/*     */     } 
/*     */     
/* 116 */     Calendar calcCalendar = Calendar.getInstance();
/* 117 */     calcCalendar.setTime(calcDate);
/* 118 */     calcCalendar.set(11, hours);
/* 119 */     calcCalendar.set(12, minutes);
/*     */     
/* 121 */     return new ResultMsg(calcCalendar.getTime());
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 125 */     Calendar startDayHours = Calendar.getInstance();
/* 126 */     startDayHours.setTime(new Date());
/* 127 */     int a = startDayHours.get(12);
/*     */     
/* 129 */     System.out.println(a);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DefaultCalendarService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */