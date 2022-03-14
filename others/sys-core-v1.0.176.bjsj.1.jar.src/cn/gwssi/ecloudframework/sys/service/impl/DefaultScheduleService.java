/*     */ package com.dstz.sys.service.impl;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.ParamValidate;
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.constant.IStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.model.calendar.Schedule;
/*     */ import com.dstz.sys.api.model.calendar.ScheduleParticipant;
/*     */ import com.dstz.sys.api.model.calendar.dto.CompleteScheduleModel;
/*     */ import com.dstz.sys.api.model.calendar.dto.CreateScheduleModel;
/*     */ import com.dstz.sys.api.service.ScheduleService;
/*     */ import com.dstz.sys.core.dao.ScheduleBizDao;
/*     */ import com.dstz.sys.core.dao.ScheduleParticipantDao;
/*     */ import com.dstz.sys.core.manager.ScheduleManager;
/*     */ import com.dstz.sys.core.model.ScheduleBiz;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ @Transactional
/*     */ @Service
/*     */ public class DefaultScheduleService
/*     */   implements ScheduleService
/*     */ {
/*     */   @Resource
/*     */   ScheduleManager scheduleManager;
/*     */   @Resource
/*     */   ScheduleBizDao scheduleBizDao;
/*     */   @Resource
/*     */   ScheduleParticipantDao scheduleParticipantDao;
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg<List<Schedule>> getSchedulesByTime(Date start, Date end, String source) {
/*  44 */     List<Schedule> schedules = this.scheduleManager.getByPeriodAndSource(start, end, source);
/*  45 */     if (schedules != null) {
/*  46 */       for (Schedule schedule : schedules) {
/*  47 */         if (!"single".equals(schedule.getType())) {
/*  48 */           List<ScheduleParticipant> scheduleParticipants = this.scheduleParticipantDao.getScheduleParticipantList(schedule.getId());
/*  49 */           schedule.setScheduleParticipantList(scheduleParticipants);
/*     */         } 
/*     */       } 
/*     */     }
/*  53 */     List<Schedule> list = schedules;
/*  54 */     return new ResultMsg(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @ParamValidate
/*     */   public ResultMsg createSchedule(CreateScheduleModel scheduleModel) {
/*  63 */     ResultMsg resultMsg = new ResultMsg();
/*  64 */     Schedule schedule = new Schedule();
/*  65 */     ScheduleBiz scheduleBiz = new ScheduleBiz();
/*  66 */     ScheduleParticipant scheduleParticipant = new ScheduleParticipant();
/*  67 */     String scheduleId = IdUtil.getSuid();
/*     */     
/*  69 */     schedule.setId(scheduleId);
/*  70 */     schedule.setTitle(scheduleModel.getTitle());
/*  71 */     schedule.setRemark(scheduleModel.getRemark());
/*  72 */     schedule.setRateProgress(Integer.valueOf(0));
/*  73 */     schedule.setStartTime(scheduleModel.getStartTime());
/*  74 */     schedule.setActualStartTime(scheduleModel.getStartTime());
/*  75 */     schedule.setEndTime(scheduleModel.getEndTime());
/*  76 */     if (scheduleModel.getTaskUrl() != null) {
/*  77 */       schedule.setTaskUrl(scheduleModel.getTaskUrl());
/*     */     }
/*  79 */     if (scheduleModel.getOpenType() != null) {
/*  80 */       schedule.setOpenType(scheduleModel.getOpenType());
/*     */     }
/*  82 */     if (scheduleModel.getOwner() != null) {
/*  83 */       schedule.setOwnerName(scheduleModel.getOwner().getFullname());
/*  84 */       schedule.setOwner(scheduleModel.getOwner().getAccount());
/*  85 */       schedule.setCreateBy(scheduleModel.getOwner().getFullname());
/*  86 */       schedule.setUpdateBy(scheduleModel.getOwner().getAccount());
/*     */     } 
/*  88 */     if (scheduleModel.getOwnerAccount() != null) {
/*  89 */       schedule.setOwner(scheduleModel.getOwnerAccount());
/*  90 */       schedule.setCreateBy(scheduleModel.getOwnerAccount());
/*  91 */       schedule.setUpdateBy(scheduleModel.getOwnerAccount());
/*     */     } 
/*  93 */     schedule.setType(scheduleModel.getType());
/*  94 */     schedule.setCreateTime(new Date());
/*  95 */     schedule.setUpdateTime(new Date());
/*     */     
/*  97 */     if (!"single".equals(scheduleModel.getType()) && scheduleModel.getUser() != null) {
/*  98 */       List<ScheduleParticipant> list = new ArrayList<>();
/*  99 */       for (IUser iUser : scheduleModel.getUser()) {
/* 100 */         scheduleParticipant.setScheduleId(scheduleId);
/* 101 */         scheduleParticipant.setParticipantorName(iUser.getFullname());
/* 102 */         scheduleParticipant.setParticipantor(iUser.getAccount());
/* 103 */         scheduleParticipant.setCreateTime(new Date());
/* 104 */         scheduleParticipant.setActualStartTime(scheduleModel.getStartTime());
/* 105 */         scheduleParticipant.setRateProgress(Integer.valueOf(0));
/*     */         
/* 107 */         list.add(scheduleParticipant);
/*     */       } 
/* 109 */       schedule.setScheduleParticipantList(list);
/*     */     } 
/*     */     
/* 112 */     scheduleBiz.setBizId(scheduleModel.getBizId());
/* 113 */     scheduleBiz.setScheduleId(scheduleId);
/*     */     
/* 115 */     scheduleBiz.setSource(scheduleModel.getSource());
/* 116 */     this.scheduleManager.create(schedule);
/* 117 */     this.scheduleBizDao.create(scheduleBiz);
/*     */     
/* 119 */     resultMsg.setOk(Boolean.valueOf(true));
/* 120 */     resultMsg.setMsg("创建成功");
/* 121 */     return resultMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @ParamValidate
/*     */   public ResultMsg completeSchedule(CompleteScheduleModel param) {
/* 130 */     ResultMsg resultMsg = new ResultMsg();
/* 131 */     Date date = new Date();
/* 132 */     int rate = param.getRate_progress().intValue();
/* 133 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 134 */     Date completeDate = null;
/* 135 */     if (param.getCompleteTime() != null) {
/*     */       try {
/* 137 */         completeDate = dateFormat.parse(param.getCompleteTime());
/* 138 */       } catch (ParseException e) {
/* 139 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     try {
/* 143 */       List<Schedule> list = this.scheduleManager.getByBizId(param.getBizId());
/* 144 */       if (list == null) {
/* 145 */         throw new Exception("未找到相关日程");
/*     */       }
/*     */       
/* 148 */       Schedule schedule = list.get(0);
/* 149 */       String type = schedule.getType();
/*     */       
/* 151 */       if ("share".equals(type)) {
/* 152 */         List<ScheduleParticipant> scheduleParticipants = this.scheduleParticipantDao.getScheduleParticipantList(schedule.getId());
/* 153 */         if (scheduleParticipants != null) {
/* 154 */           for (ScheduleParticipant scheduleParticipant : scheduleParticipants) {
/* 155 */             scheduleParticipant.setRateProgress(Integer.valueOf(rate));
/* 156 */             scheduleParticipant.setUpdateTime(date);
/* 157 */             if (rate >= 100) {
/* 158 */               if (completeDate != null) {
/* 159 */                 scheduleParticipant.setCompleteTime(completeDate);
/*     */               } else {
/* 161 */                 scheduleParticipant.setCompleteTime(date);
/*     */               } 
/*     */             }
/* 164 */             if ((scheduleParticipant.getParticipantor().equals(param.getOwnerAccount()) || scheduleParticipant.getParticipantor().equals(param.getOwner().getAccount())) && param.getComment() != null) {
/* 165 */               scheduleParticipant.setSubmitComment(param.getComment());
/*     */             }
/* 167 */             this.scheduleParticipantDao.update(scheduleParticipant);
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 172 */       else if ("ilka".equals(type)) {
/* 173 */         List<ScheduleParticipant> scheduleParticipants = this.scheduleParticipantDao.getScheduleParticipantList(schedule.getId());
/* 174 */         if (scheduleParticipants != null) {
/* 175 */           int numberOfParticipants = scheduleParticipants.size();
/* 176 */           ScheduleParticipant sParticipant = new ScheduleParticipant();
/* 177 */           for (ScheduleParticipant scheduleParticipant : scheduleParticipants) {
/* 178 */             if (scheduleParticipant.getParticipantor().equals(param.getOwnerAccount()) || scheduleParticipant.getParticipantor().equals(param.getOwner().getAccount())) {
/* 179 */               scheduleParticipant.setRateProgress(param.getRate_progress());
/* 180 */               scheduleParticipant.setUpdateTime(date);
/* 181 */               if (rate >= 100) {
/* 182 */                 if (completeDate != null) {
/* 183 */                   scheduleParticipant.setCompleteTime(completeDate);
/*     */                 } else {
/* 185 */                   scheduleParticipant.setCompleteTime(date);
/*     */                 } 
/*     */               }
/* 188 */               if (param.getComment() != null) {
/* 189 */                 scheduleParticipant.setSubmitComment(param.getComment());
/*     */               }
/* 191 */               this.scheduleParticipantDao.update(scheduleParticipant);
/*     */               continue;
/*     */             } 
/* 194 */             rate += scheduleParticipant.getRateProgress().intValue();
/*     */           } 
/* 196 */           rate = (int)Math.round(rate * 1.0D / numberOfParticipants);
/*     */         } 
/*     */       } 
/*     */       
/* 200 */       if (rate >= 100) {
/* 201 */         schedule.setRateProgress(Integer.valueOf(100));
/* 202 */         schedule.setCompleteTime((completeDate == null) ? date : completeDate);
/*     */       } else {
/* 204 */         schedule.setRateProgress(Integer.valueOf(rate));
/*     */       } 
/*     */       
/* 207 */       schedule.setUpdateTime(date);
/* 208 */       if (param.getOwner() != null) {
/* 209 */         schedule.setSubmitNamer(param.getOwner().getFullname());
/* 210 */         schedule.setSubmitter(param.getOwner().getAccount());
/* 211 */         schedule.setUpdateBy(param.getOwner().getAccount());
/* 212 */       } else if (param.getOwnerAccount() != null) {
/* 213 */         schedule.setSubmitter(param.getOwnerAccount());
/*     */       } 
/* 215 */       this.scheduleManager.updateOnlySchedule(schedule);
/* 216 */     } catch (Exception e) {
/* 217 */       resultMsg.setOk(Boolean.valueOf(false));
/* 218 */       resultMsg.setMsg("完成日程失败：" + e.getMessage());
/*     */     } 
/* 220 */     resultMsg.setOk(Boolean.valueOf(true));
/* 221 */     resultMsg.setMsg("完成日程成功");
/* 222 */     return resultMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr("删除失败")
/*     */   public ResultMsg deleteSchedule(String biz_id) {
/* 230 */     this.scheduleManager.deleteByBizId(biz_id);
/* 231 */     return new ResultMsg((IStatusCode)BaseStatusCode.SUCCESS, "删除成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg updateSchedule(String biz_id, Date start, Date end, String ownerAccount) {
/* 239 */     List<Schedule> list = this.scheduleManager.getByBizId(biz_id);
/* 240 */     if (list == null) {
/* 241 */       throw new BusinessMessage("未找到相关日程");
/*     */     }
/*     */     
/* 244 */     Schedule schedule = list.get(0);
/* 245 */     schedule.setOwner(ownerAccount);
/* 246 */     schedule.setActualStartTime(start);
/* 247 */     schedule.setCompleteTime(end);
/* 248 */     schedule.setUpdateTime(new Date());
/* 249 */     this.scheduleManager.updateOnlySchedule(schedule);
/*     */ 
/*     */     
/* 252 */     return new ResultMsg((IStatusCode)BaseStatusCode.SUCCESS, "修改成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg createOrUpdateSchedule(List<Schedule> schedules) {
/* 262 */     ResultMsg resultMsg = new ResultMsg();
/* 263 */     for (Schedule schedule : schedules) {
/* 264 */       List<Schedule> list = this.scheduleManager.getByBizId(schedule.getBizId());
/*     */       
/* 266 */       if (list != null && list.size() > 0) {
/* 267 */         String scheduleId = ((Schedule)list.get(0)).getId();
/* 268 */         schedule.setId(scheduleId);
/* 269 */         this.scheduleManager.updateOnlySchedule(schedule);
/*     */         
/* 271 */         if (schedule.getScheduleParticipantList() != null && schedule.getScheduleParticipantList().size() > 0) {
/* 272 */           this.scheduleParticipantDao.delByMainId(scheduleId);
/* 273 */           for (ScheduleParticipant scheduleParticipant : schedule.getScheduleParticipantList()) {
/* 274 */             if (scheduleParticipant.getId() == null) {
/* 275 */               scheduleParticipant.setId(IdUtil.getSuid());
/*     */             }
/* 277 */             if (scheduleParticipant.getScheduleId() == null) {
/* 278 */               scheduleParticipant.setScheduleId(scheduleId);
/*     */             }
/* 280 */             if (scheduleParticipant.getRateProgress() == null) {
/* 281 */               scheduleParticipant.setRateProgress(Integer.valueOf(0));
/*     */             }
/* 283 */             if (scheduleParticipant.getCreateTime() == null) {
/* 284 */               scheduleParticipant.setCreateTime(new Date());
/*     */             }
/* 286 */             if (scheduleParticipant.getActualStartTime() == null) {
/* 287 */               scheduleParticipant.setActualStartTime((schedule.getActualStartTime() == null) ? ((Schedule)list.get(0)).getActualStartTime() : schedule.getActualStartTime());
/*     */             }
/* 289 */             this.scheduleParticipantDao.create(scheduleParticipant);
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 294 */       String id = IdUtil.getSuid();
/* 295 */       schedule.setId(id);
/* 296 */       if (schedule.getStartTime() != null && schedule.getEndTime() != null && schedule
/* 297 */         .getStartTime().getTime() >= schedule.getEndTime().getTime()) {
/* 298 */         throw new BusinessMessage("预计结束时间不能小于预计开始时间");
/*     */       }
/* 300 */       if (schedule.getActualStartTime() != null && schedule.getCompleteTime() != null && schedule
/* 301 */         .getActualStartTime().getTime() >= schedule.getCompleteTime().getTime()) {
/* 302 */         throw new BusinessMessage("完成时间不能小于实际开始时间");
/*     */       }
/* 304 */       schedule.setId(IdUtil.getSuid());
/* 305 */       schedule.setRateProgress(Integer.valueOf(0));
/* 306 */       schedule.setCreateTime(new Date());
/* 307 */       schedule.setUpdateTime(new Date());
/* 308 */       if (schedule.getActualStartTime() == null) {
/* 309 */         schedule.setActualStartTime(schedule.getStartTime());
/*     */       }
/* 311 */       if (schedule.getOwnerName() != null) {
/* 312 */         schedule.setCreateBy(schedule.getOwnerName());
/* 313 */       } else if (schedule.getOwner() != null) {
/* 314 */         schedule.setCreateBy(schedule.getOwner());
/*     */       } 
/* 316 */       this.scheduleManager.create(schedule);
/* 317 */       ScheduleBiz scheduleBiz = new ScheduleBiz();
/* 318 */       scheduleBiz.setBizId(schedule.getBizId());
/* 319 */       scheduleBiz.setScheduleId(id);
/* 320 */       scheduleBiz.setId(IdUtil.getSuid());
/* 321 */       this.scheduleBizDao.create(scheduleBiz);
/*     */     } 
/*     */ 
/*     */     
/* 325 */     return new ResultMsg((IStatusCode)BaseStatusCode.SUCCESS, "更新创建成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   public ResultMsg deleteSchedule(List<Schedule> schedules) {
/* 335 */     for (Schedule schedule : schedules) {
/* 336 */       this.scheduleManager.deleteByBizId(schedule.getBizId());
/*     */     }
/*     */     
/* 339 */     return new ResultMsg((IStatusCode)BaseStatusCode.SUCCESS, "删除成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DefaultScheduleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */