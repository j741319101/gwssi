package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.SysFile;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysFileDao extends BaseDao<String, SysFile> {
  void updateInstid(@Param("instId") String paramString1, @Param("id") String paramString2);
  
  List<SysFile> getFileByInstId(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SysFileDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */