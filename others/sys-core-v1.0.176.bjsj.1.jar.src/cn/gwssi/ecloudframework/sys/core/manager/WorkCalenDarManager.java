package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.api.model.calendar.WorkCalenDar;
import com.dstz.sys.core.model.HolidayConf;
import java.util.Date;
import java.util.List;

public interface WorkCalenDarManager extends Manager<String, WorkCalenDar> {
  void initWorkCalenDarRecord(int paramInt);
  
  List<WorkCalenDar> getByTime(Date paramDate1, Date paramDate2);
  
  List<WorkCalenDar> getByPeriodWork(Date paramDate1, Date paramDate2);
  
  Date getWorkDayByDays(Date paramDate, int paramInt);
  
  List<WorkCalenDar> getByTime(Date paramDate);
  
  void updateWhenHolidayConfCreate(HolidayConf paramHolidayConf);
  
  void updateWhenHolidayConfUpd(HolidayConf paramHolidayConf1, HolidayConf paramHolidayConf2);
  
  WorkCalenDar getByDayAndSystem(Date paramDate, String paramString);
  
  void updateWorkType(Date paramDate1, Date paramDate2, String paramString1, String paramString2);
  
  void clearWorkCalenDarByHoliday(HolidayConf paramHolidayConf);
  
  List<WorkCalenDar> getByTime(Date paramDate1, Date paramDate2, String paramString);
  
  Date getWorkDayByDays(Date paramDate, int paramInt, String paramString);
  
  List<WorkCalenDar> getByTimeContainPublic(Date paramDate1, Date paramDate2, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/WorkCalenDarManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */