package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.HolidayConf;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

public interface HolidayConfDao extends BaseDao<String, HolidayConf> {
  HolidayConf queryOne(@Param("name") String paramString, @Param("startDay") Date paramDate1, @Param("endDay") Date paramDate2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/HolidayConfDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */