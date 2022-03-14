package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.SerialNo;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface SerialNoDao extends BaseDao<String, SerialNo> {
  Integer isAliasExisted(@Param("id") String paramString1, @Param("alias") String paramString2);
  
  SerialNo getByAlias(String paramString);
  
  int updByAlias(SerialNo paramSerialNo);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SerialNoDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */