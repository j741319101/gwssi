/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.calendar.Schedule;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.calendar.ScheduleHistory;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.calendar.ScheduleParticipant;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.ScheduleDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.ScheduleHistoryDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.ScheduleParticipantDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.ScheduleManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.ParticipantScheduleDO;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("scheduleManager")
/*     */ public class ScheduleManagerImpl
/*     */   extends BaseManager<String, Schedule>
/*     */   implements ScheduleManager
/*     */ {
/*     */   @Resource
/*     */   ScheduleDao scheduleDao;
/*     */   @Resource
/*     */   ScheduleParticipantDao scheduleParticipantDao;
/*     */   @Resource
/*     */   private ScheduleHistoryDao scheduleHistoryDao;
/*     */   
/*     */   public void create(Schedule schedule) {
/*  50 */     super.create((Serializable)schedule);
/*  51 */     String pk = schedule.getId();
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
/*  68 */     if ("share".equals(schedule.getType()) || "ilka".equals(schedule.getType())) {
/*  69 */       List<ScheduleParticipant> scheduleParticipantList = schedule.getScheduleParticipantList();
/*     */       
/*  71 */       boolean flag = false;
/*  72 */       for (ScheduleParticipant p : scheduleParticipantList) {
/*  73 */         if (p.getParticipantor().equals(schedule.getOwner())) {
/*  74 */           flag = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*  78 */       if (!flag) {
/*     */         
/*  80 */         ScheduleParticipant participant = new ScheduleParticipant();
/*  81 */         participant.setParticipantor(schedule.getOwner());
/*  82 */         participant.setParticipantorName(schedule.getOwnerName());
/*  83 */         scheduleParticipantList.add(participant);
/*     */       } 
/*  85 */       for (ScheduleParticipant scheduleParticipant : scheduleParticipantList) {
/*  86 */         scheduleParticipant.setId(IdUtil.getSuid());
/*  87 */         scheduleParticipant.setScheduleId(pk);
/*  88 */         scheduleParticipant.setRateProgress(Integer.valueOf(0));
/*  89 */         scheduleParticipant.setCreateTime(schedule.getCreateTime());
/*  90 */         scheduleParticipant.setActualStartTime(schedule.getActualStartTime());
/*  91 */         scheduleParticipant.setUpdateTime(schedule.getUpdateTime());
/*  92 */         this.scheduleParticipantDao.create(scheduleParticipant);
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
/*     */   public void remove(String entityId) {
/* 104 */     super.remove(entityId);
/* 105 */     this.scheduleParticipantDao.delByMainId(entityId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeByIds(String[] entityIds) {
/* 113 */     for (String id : entityIds) {
/* 114 */       remove(id);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schedule get(String entityId) {
/* 122 */     Assert.notBlank("日程 ID 不能为空！");
/* 123 */     Schedule schedule = (Schedule)super.get(entityId);
/* 124 */     List<ScheduleParticipant> scheduleParticipantList = this.scheduleParticipantDao.getScheduleParticipantList(entityId);
/* 125 */     if (schedule != null) {
/* 126 */       schedule.setScheduleParticipantList(scheduleParticipantList);
/*     */     }
/* 128 */     return schedule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Schedule schedule) {
/* 136 */     String userId = ContextUtil.getCurrentUserId();
/* 137 */     String userName = ContextUtil.getCurrentUser().getFullname();
/* 138 */     int mainRate = 0;
/*     */     
/* 140 */     if (schedule.getRateProgress().intValue() >= 100) {
/* 141 */       schedule.setRateProgress(Integer.valueOf(100));
/* 142 */       schedule.setCompleteTime(new Date());
/*     */     } 
/* 144 */     List<ScheduleParticipant> participants = schedule.getScheduleParticipantList();
/* 145 */     if (userId.equals(schedule.getOwner())) {
/* 146 */       if ("ilka".equals(schedule.getType()))
/*     */       {
/* 148 */         boolean flag = true;
/* 149 */         for (ScheduleParticipant participant : participants) {
/* 150 */           if (participant.getRateProgress().intValue() == 100) {
/* 151 */             participant.setCompleteTime(new Date());
/*     */           } else {
/* 153 */             flag = false;
/*     */           } 
/* 155 */           this.scheduleParticipantDao.update(participant);
/* 156 */           mainRate += (int)Math.round(((participant.getRateProgress().intValue() > 100) ? 100 : participant.getRateProgress().intValue()) * 1.0D / participants.size());
/*     */         } 
/* 158 */         if (flag) {
/* 159 */           mainRate = 100;
/* 160 */           schedule.setCompleteTime(new Date());
/*     */         } 
/* 162 */         schedule.setRateProgress(Integer.valueOf(mainRate));
/*     */       }
/*     */     
/*     */     }
/* 166 */     else if ("ilka".equals(schedule.getType())) {
/* 167 */       for (ScheduleParticipant participant : participants) {
/* 168 */         if (participant.getParticipantor().equals(userId)) {
/* 169 */           if (participant.getRateProgress().intValue() > 100) {
/* 170 */             participant.setRateProgress(Integer.valueOf(100));
/*     */           }
/* 172 */           if (participant.getRateProgress().intValue() == 100) {
/* 173 */             participant.setCompleteTime(new Date());
/*     */           }
/* 175 */           this.scheduleParticipantDao.update(participant);
/*     */           break;
/*     */         } 
/*     */       } 
/* 179 */       List<ScheduleParticipant> participantList = this.scheduleParticipantDao.getScheduleParticipantList(schedule.getId());
/* 180 */       for (ScheduleParticipant participant : participantList) {
/* 181 */         mainRate += (int)Math.round(participant.getRateProgress().intValue() * 1.0D / participantList.size());
/*     */       }
/*     */       
/* 184 */       if (mainRate == 100) {
/* 185 */         schedule.setCompleteTime(new Date());
/*     */       }
/* 187 */       schedule.setRateProgress(Integer.valueOf(mainRate));
/*     */     } 
/*     */     
/* 190 */     if ("share".equals(schedule.getType()))
/*     */     {
/* 192 */       for (ScheduleParticipant participant : participants) {
/* 193 */         if (schedule.getRateProgress().intValue() == 100) {
/* 194 */           participant.setCompleteTime(new Date());
/*     */         }
/* 196 */         participant.setRateProgress(schedule.getRateProgress());
/* 197 */         this.scheduleParticipantDao.update(participant);
/*     */       } 
/*     */     }
/* 200 */     schedule.setUpdateTime(new Date());
/* 201 */     schedule.setSubmitter(userId);
/* 202 */     schedule.setSubmitNamer(userName);
/* 203 */     this.scheduleDao.update(schedule);
/*     */ 
/*     */     
/* 206 */     updateScheduleHistory(schedule);
/*     */     
/* 208 */     if (schedule.getRateProgress().intValue() == 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Schedule> getByPeriodAndOwner(Date start, Date end, String ownerName, String owner) {
/* 219 */     return this.scheduleDao.getByPeriodAndOwner(start, end, ownerName, owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveSchedule(Schedule schedule) {
/* 224 */     if (schedule.getStartTime().compareTo(schedule.getEndTime()) > 0) {
/* 225 */       throw new BusinessMessage("日程开始时间不能大于结束时间");
/*     */     }
/* 227 */     create(schedule);
/*     */   }
/*     */   
/*     */   public List<Schedule> getByPeriodAndSource(Date start, Date end, String source) {
/* 231 */     return this.scheduleDao.getByPeriodAndSource(start, end, source);
/*     */   }
/*     */   
/*     */   public void deleteByBizId(String bizId) {
/* 235 */     this.scheduleDao.deleteByBizId(bizId);
/*     */   }
/*     */   
/*     */   public void dragUpdate(Schedule schedule) {
/* 239 */     this.scheduleDao.dragUpdate(schedule);
/*     */   }
/*     */   
/*     */   public void updateSchedule(String biz_id, Date start, Date end, String ownerAccount) {
/* 243 */     this.scheduleDao.updateSchedule(biz_id, start, end, ownerAccount);
/*     */   }
/*     */   
/*     */   public List<Schedule> getByBizId(String biz_id) {
/* 247 */     return this.scheduleDao.getByBizId(biz_id);
/*     */   }
/*     */   
/*     */   public void updateOnlySchedule(Schedule schedule) {
/* 251 */     this.scheduleDao.updateOnlySchedule(schedule);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ParticipantScheduleDO> getParticipantEvents(Date startDate, Date endDate, String name, String id) {
/* 256 */     Map<String, Object> map = new HashMap<>();
/* 257 */     map.put("startTime", startDate);
/* 258 */     map.put("endTime", endDate);
/* 259 */     map.put("participantName", name);
/* 260 */     map.put("participant", id);
/*     */     
/* 262 */     return this.scheduleDao.getParticipantEvents(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ScheduleHistory> queryHistory(String scheduleId) {
/* 267 */     return this.scheduleHistoryDao.queryHistory(scheduleId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateScheduleHistory(Schedule schedule) {
/* 275 */     String userId = ContextUtil.getCurrentUserId();
/* 276 */     String userName = ContextUtil.getCurrentUser().getFullname();
/* 277 */     Date currentTime = new Date();
/* 278 */     ScheduleHistory scheduleHistory = new ScheduleHistory();
/* 279 */     scheduleHistory.setId(IdUtil.getSuid());
/* 280 */     scheduleHistory.setScheduleId(schedule.getId());
/* 281 */     scheduleHistory.setRateProgress(schedule.getRateProgress().intValue());
/* 282 */     scheduleHistory.setSubmit(userId);
/* 283 */     scheduleHistory.setSubmitName(userName);
/* 284 */     scheduleHistory.setSubmitTime(currentTime);
/* 285 */     this.scheduleHistoryDao.create(scheduleHistory);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/ScheduleManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */