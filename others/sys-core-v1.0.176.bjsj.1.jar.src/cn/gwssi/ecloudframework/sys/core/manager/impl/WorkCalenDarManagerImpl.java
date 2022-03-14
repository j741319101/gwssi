/*     */ package com.dstz.sys.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.sys.api.model.calendar.WorkCalenDar;
/*     */ import com.dstz.sys.core.dao.WorkCalenDarDao;
/*     */ import com.dstz.sys.core.manager.HolidayConfManager;
/*     */ import com.dstz.sys.core.manager.WorkCalenDarManager;
/*     */ import com.dstz.sys.core.model.HolidayConf;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.github.pagehelper.PageHelper;
/*     */ import java.io.Serializable;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service("workCalenDarManager")
/*     */ public class WorkCalenDarManagerImpl
/*     */   extends BaseManager<String, WorkCalenDar>
/*     */   implements WorkCalenDarManager
/*     */ {
/*     */   @Resource
/*     */   WorkCalenDarDao workCalenDarDao;
/*     */   @Resource
/*     */   HolidayConfManager holidayConfManager;
/*     */   
/*     */   public void initWorkCalenDarRecord(int year) {
/*  42 */     Calendar calendarStart = Calendar.getInstance();
/*  43 */     calendarStart.set(year, 0, 1);
/*     */     
/*  45 */     Calendar calendarEnd = Calendar.getInstance();
/*  46 */     calendarEnd.set(year + 1, 0, 1);
/*     */     
/*  48 */     List<WorkCalenDar> workCalenDarList = getByTime(calendarStart.getTime());
/*  49 */     if (CollectionUtil.isNotEmpty(workCalenDarList)) {
/*  50 */       throw new BusinessMessage("当前年份已经初始化过");
/*     */     }
/*     */     
/*  53 */     while (calendarStart.before(calendarEnd)) {
/*  54 */       int week = calendarStart.get(7) - 1;
/*  55 */       WorkCalenDar calenDar = new WorkCalenDar();
/*  56 */       calenDar.setId(IdUtil.getSuid());
/*  57 */       calenDar.setDay(calendarStart.getTime());
/*     */ 
/*     */       
/*  60 */       if (week == 6 || week == 0) {
/*  61 */         calenDar.setIsWorkDay("0");
/*  62 */         calenDar.setType("DR");
/*     */       } else {
/*  64 */         calenDar.setIsWorkDay("1");
/*  65 */         calenDar.setType("DW");
/*     */       } 
/*  67 */       this.workCalenDarDao.create(calenDar);
/*  68 */       calendarStart.add(5, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkCalenDar> getByTime(Date startDay, Date endDay) {
/*  76 */     return this.workCalenDarDao.getByPeriod(startDay, endDay);
/*     */   }
/*     */   
/*     */   public List<WorkCalenDar> getByPeriodWork(Date startDay, Date endDay) {
/*  80 */     return this.workCalenDarDao.getByPeriodWork(startDay, endDay);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkCalenDar> getByTime(Date startDay, Date endDay, String system) {
/*  87 */     return this.workCalenDarDao.getByPeriodAndSystem(startDay, endDay, system);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkCalenDar> getByTimeContainPublic(Date startDay, Date endDay, String system) {
/*  94 */     return this.workCalenDarDao.getByTimeContainPublic(startDay, endDay, system);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkCalenDar> getByTime(Date day) {
/* 101 */     return this.workCalenDarDao.getByDay(day);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getWorkDayByDays(Date startDay, int days) {
/* 109 */     PageHelper.offsetPage(days, 1);
/*     */     
/* 111 */     List<WorkCalenDar> day = this.workCalenDarDao.getWorkDayByDays(startDay);
/*     */     
/* 113 */     return day.isEmpty() ? null : ((WorkCalenDar)day.get(0)).getDay();
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getWorkDayByDays(Date startDay, int days, String system) {
/* 118 */     PageHelper.offsetPage(days, 1);
/*     */     
/* 120 */     List<WorkCalenDar> day = this.workCalenDarDao.getWorkDayByDays(startDay, system);
/*     */     
/* 122 */     return day.isEmpty() ? null : ((WorkCalenDar)day.get(0)).getDay();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateWhenHolidayConfCreate(HolidayConf conf) {
/* 130 */     Calendar start = Calendar.getInstance();
/* 131 */     Calendar end = Calendar.getInstance();
/* 132 */     start.setTime(conf.getStartDay());
/* 133 */     end.setTime(conf.getEndDay());
/*     */     
/* 135 */     if (start.compareTo(end) > 0) {
/* 136 */       throw new BusinessMessage("开始日期大于结束日期");
/*     */     }
/*     */     
/* 139 */     List<WorkCalenDar> workCalenDars = getByTime(conf.getStartDay(), conf.getEndDay(), conf.getSystem());
/*     */ 
/*     */     
/* 142 */     if ("public".equals(conf.getSystem())) {
/*     */       
/* 144 */       if (workCalenDars.isEmpty() || ((WorkCalenDar)workCalenDars.get(0)).getDay().compareTo(conf.getStartDay()) != 0 || ((WorkCalenDar)workCalenDars.get(workCalenDars.size() - 1)).getDay().compareTo(conf.getEndDay()) != 0) {
/* 145 */         initWorkCalenDarRecord(conf.getYear().intValue());
/*     */       }
/*     */       
/* 148 */       for (WorkCalenDar workCalenDar : workCalenDars) {
/* 149 */         String type = workCalenDar.getType();
/*     */         
/* 151 */         if (type.length() > 2 && !type.substring(type.length() - 2, type.length()).equals(conf.getType())) {
/* 152 */           throw new BusinessMessage("该时间段有日期已设定过不同法定节假日类型，请删除后添加或直接更新");
/*     */         }
/*     */       } 
/*     */       
/* 156 */       for (WorkCalenDar workCalenDar : workCalenDars)
/*     */       {
/* 158 */         if ("LR".equals(conf.getType())) {
/* 159 */           workCalenDar.setIsWorkDay("0");
/* 160 */         } else if ("LW".equals(conf.getType())) {
/* 161 */           workCalenDar.setIsWorkDay("1");
/*     */         } else {
/* 163 */           throw new BusinessMessage("系统与类型不一致");
/*     */         } 
/* 165 */         workCalenDar.setType(workCalenDar.getType() + conf.getType());
/* 166 */         this.workCalenDarDao.update(workCalenDar);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 171 */       if (!workCalenDars.isEmpty()) {
/* 172 */         throw new BusinessMessage("该时间段有日期已设定过公司节假日，请删除后添加或直接更新");
/*     */       }
/*     */       
/* 175 */       while (start.compareTo(end) <= 0) {
/* 176 */         WorkCalenDar workCalenDar = new WorkCalenDar();
/*     */         
/* 178 */         workCalenDar.setDay(start.getTime());
/* 179 */         if ("CR".equals(conf.getType())) {
/* 180 */           workCalenDar.setIsWorkDay("0");
/*     */         }
/* 182 */         else if ("CW".equals(conf.getType())) {
/* 183 */           workCalenDar.setIsWorkDay("1");
/*     */         } else {
/* 185 */           throw new BusinessMessage("系统与类型不一致");
/*     */         } 
/* 187 */         workCalenDar.setType(conf.getType());
/* 188 */         workCalenDar.setSystem(conf.getSystem());
/* 189 */         this.workCalenDarDao.create(workCalenDar);
/* 190 */         start.add(5, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateWhenHolidayConfUpd(HolidayConf oldConf, HolidayConf newConf) {
/* 204 */     clearWorkCalenDarByHoliday(oldConf);
/* 205 */     updateWhenHolidayConfCreate(newConf);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWorkCalenDarByHoliday(HolidayConf conf) {
/* 210 */     List<WorkCalenDar> workCalenDars = getByTime(conf.getStartDay(), conf.getEndDay(), conf.getSystem());
/* 211 */     if (!workCalenDars.isEmpty())
/*     */     {
/* 213 */       if ("public".equals(conf.getSystem())) {
/* 214 */         for (WorkCalenDar workCalenDar : workCalenDars) {
/* 215 */           String type = workCalenDar.getType();
/*     */           
/* 217 */           if (type.length() > 2) {
/* 218 */             type = type.substring(0, type.length() - 2);
/* 219 */             String keyType = type.substring(type.length() - 2, type.length());
/* 220 */             if (keyType.equals("LR") || keyType.equals("DR")) {
/* 221 */               workCalenDar.setIsWorkDay("0");
/*     */             } else {
/* 223 */               workCalenDar.setIsWorkDay("1");
/*     */             } 
/* 225 */             workCalenDar.setType(type);
/* 226 */             this.workCalenDarDao.update(workCalenDar);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 230 */         for (WorkCalenDar workCalenDar : workCalenDars) {
/* 231 */           this.workCalenDarDao.remove(workCalenDar.getId());
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(WorkCalenDar entity) {
/* 240 */     throw new BusinessMessage("不支持创建", BaseStatusCode.NO_ACCESS);
/*     */   }
/*     */   
/*     */   public void update(WorkCalenDar entity) {
/* 244 */     throw new BusinessMessage("不支持更新", BaseStatusCode.NO_ACCESS);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorkCalenDar getByDayAndSystem(Date day, String system) {
/* 249 */     List<WorkCalenDar> workCalenDars = this.workCalenDarDao.getByDayAndSystem(day, system);
/* 250 */     if (null != workCalenDars && !workCalenDars.isEmpty()) {
/* 251 */       return workCalenDars.get(0);
/*     */     }
/* 253 */     List<WorkCalenDar> workCalenDars1 = this.workCalenDarDao.getByDayAndSystem(day, "public");
/* 254 */     if (null != workCalenDars1 && !workCalenDars1.isEmpty()) {
/* 255 */       return workCalenDars1.get(0);
/*     */     }
/* 257 */     throw new BusinessMessage("年份未初始化", BaseStatusCode.NO_ACCESS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateWorkType(Date startDay, Date endDay, String isWorkDay, String type) {
/* 263 */     this.workCalenDarDao.updateWorkType(startDay, endDay, isWorkDay, type);
/*     */   }
/*     */   
/*     */   private boolean initialized(Date day) {
/* 267 */     List<WorkCalenDar> workCalenDars = this.workCalenDarDao.getByDayAndSystem(day, "public");
/* 268 */     if (null != workCalenDars && !workCalenDars.isEmpty()) {
/* 269 */       return true;
/*     */     }
/* 271 */     return false;
/*     */   }
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
/*     */   public static void main(String[] args) {
/* 291 */     int year = 2017;
/* 292 */     Calendar calendarStart = Calendar.getInstance();
/* 293 */     calendarStart.set(year, 0, 1);
/*     */     
/* 295 */     Calendar calendarEnd = Calendar.getInstance();
/* 296 */     calendarEnd.set(year + 1, 0, 1);
/* 297 */     int a = 0;
/* 298 */     while (calendarStart.before(calendarEnd)) {
/* 299 */       a++;
/* 300 */       int week = calendarStart.get(7) - 1;
/* 301 */       WorkCalenDar calenDar = new WorkCalenDar();
/* 302 */       calenDar.setDay(calendarStart.getTime());
/*     */ 
/*     */       
/* 305 */       if (week == 6 || week == 0) {
/* 306 */         calenDar.setIsWorkDay("0");
/* 307 */         calenDar.setType("周末");
/*     */       } else {
/* 309 */         calenDar.setIsWorkDay("1");
/* 310 */         calenDar.setType("工作日");
/*     */       } 
/*     */ 
/*     */       
/* 314 */       System.out.println(DateUtil.formatDate(calenDar.getDay()));
/* 315 */       System.out.println(calenDar);
/*     */       
/* 317 */       calendarStart.add(5, 1);
/*     */     } 
/*     */     
/* 320 */     System.out.println(a);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/WorkCalenDarManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */