package com.dstz.sys.api.service;

import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.sys.api.model.calendar.Schedule;
import com.dstz.sys.api.model.calendar.dto.CompleteScheduleModel;
import com.dstz.sys.api.model.calendar.dto.CreateScheduleModel;
import java.util.Date;
import java.util.List;

public interface ScheduleService {
  ResultMsg<List<Schedule>> getSchedulesByTime(Date paramDate1, Date paramDate2, String paramString);
  
  ResultMsg createSchedule(CreateScheduleModel paramCreateScheduleModel);
  
  ResultMsg completeSchedule(CompleteScheduleModel paramCompleteScheduleModel);
  
  ResultMsg deleteSchedule(String paramString);
  
  ResultMsg updateSchedule(String paramString1, Date paramDate1, Date paramDate2, String paramString2);
  
  ResultMsg createOrUpdateSchedule(List<Schedule> paramList);
  
  ResultMsg deleteSchedule(List<Schedule> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/ScheduleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */