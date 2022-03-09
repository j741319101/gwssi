package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.api.model.calendar.ScheduleParticipant;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScheduleParticipantDao extends BaseDao<String, ScheduleParticipant> {
  List<ScheduleParticipant> getScheduleParticipantList(String paramString);
  
  void delByMainId(String paramString);
  
  ScheduleParticipant getByName(String paramString);
  
  void updateByRateAndCompleteTime(@Param("scheduleId") String paramString, @Param("rate") int paramInt, @Param("completeTime") Date paramDate);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/ScheduleParticipantDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */