package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.api.model.calendar.WorkCalenDar;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkCalenDarDao extends BaseDao<String, WorkCalenDar> {
  List<WorkCalenDar> getByDay(@Param("day") Date paramDate);
  
  List<WorkCalenDar> getByPeriod(@Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2);
  
  List<WorkCalenDar> getByPeriodWork(@Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2);
  
  List<WorkCalenDar> getWorkDayByDays(@Param("startDay") Date paramDate);
  
  List<WorkCalenDar> getWorkDayByDaysAndSystem(@Param("day") Date paramDate, @Param("system") String paramString);
  
  void updateWorkType(@Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2, String paramString1, String paramString2);
  
  List<WorkCalenDar> getByPeriodAndSystem(@Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2, @Param("system") String paramString);
  
  List<WorkCalenDar> getWorkDayByDays(@Param("startDay") Date paramDate, String paramString);
  
  List<WorkCalenDar> getByTimeContainPublic(@Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2, @Param("system") String paramString);
  
  List<WorkCalenDar> getByDayAndSystem(Date paramDate, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/WorkCalenDarDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */