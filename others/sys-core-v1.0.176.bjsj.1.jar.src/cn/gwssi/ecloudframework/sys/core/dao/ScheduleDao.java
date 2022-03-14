package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.api.model.calendar.Schedule;
import com.dstz.sys.core.model.ParticipantScheduleDO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ScheduleDao extends BaseDao<String, Schedule> {
  List<Schedule> getByPeriodAndOwner(@Param("startTime") Date paramDate1, @Param("endTime") Date paramDate2, @Param("ownerName") String paramString1, @Param("owner") String paramString2);
  
  List<Schedule> getByPeriodAndSource(@Param("startTime") Date paramDate1, @Param("endTime") Date paramDate2, @Param("source") String paramString);
  
  void deleteByBizId(String paramString);
  
  void dragUpdate(Schedule paramSchedule);
  
  void updateSchedule(@Param("bizId") String paramString1, @Param("startTime") Date paramDate1, @Param("endTime") Date paramDate2, @Param("owner") String paramString2);
  
  List<Schedule> getByBizId(String paramString);
  
  void updateOnlySchedule(Schedule paramSchedule);
  
  List<ParticipantScheduleDO> getParticipantEvents(Map<String, Object> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/ScheduleDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */