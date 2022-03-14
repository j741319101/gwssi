package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.api.model.calendar.Schedule;
import com.dstz.sys.api.model.calendar.ScheduleHistory;
import com.dstz.sys.core.model.ParticipantScheduleDO;
import java.util.Date;
import java.util.List;

public interface ScheduleManager extends Manager<String, Schedule> {
  List<Schedule> getByPeriodAndOwner(Date paramDate1, Date paramDate2, String paramString1, String paramString2);
  
  void saveSchedule(Schedule paramSchedule);
  
  List<Schedule> getByPeriodAndSource(Date paramDate1, Date paramDate2, String paramString);
  
  void deleteByBizId(String paramString);
  
  void dragUpdate(Schedule paramSchedule);
  
  void updateSchedule(String paramString1, Date paramDate1, Date paramDate2, String paramString2);
  
  List<Schedule> getByBizId(String paramString);
  
  void updateOnlySchedule(Schedule paramSchedule);
  
  List<ParticipantScheduleDO> getParticipantEvents(Date paramDate1, Date paramDate2, String paramString1, String paramString2);
  
  List<ScheduleHistory> queryHistory(String paramString);
  
  void updateScheduleHistory(Schedule paramSchedule);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/ScheduleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */