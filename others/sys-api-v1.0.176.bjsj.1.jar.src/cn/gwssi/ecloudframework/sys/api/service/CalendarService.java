package com.dstz.sys.api.service;

import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.sys.api.model.calendar.WorkCalenDar;
import java.util.Date;
import java.util.List;

public interface CalendarService {
  ResultMsg<WorkCalenDar> getWorkCalenDarByDay(Date paramDate);
  
  ResultMsg<WorkCalenDar> getWorkCalenDarByDay(Date paramDate, String paramString);
  
  ResultMsg<List<WorkCalenDar>> getWorkCalenDars(Date paramDate1, Date paramDate2);
  
  ResultMsg<List<WorkCalenDar>> getWorkCalenDars(Date paramDate1, Date paramDate2, String paramString);
  
  ResultMsg<Date> getEndWorkDay(Date paramDate, int paramInt);
  
  ResultMsg<Date> getEndWorkDay(Date paramDate, int paramInt, String paramString);
  
  ResultMsg<Date> getEndWorkDayByMinute(Date paramDate, int paramInt);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/CalendarService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */