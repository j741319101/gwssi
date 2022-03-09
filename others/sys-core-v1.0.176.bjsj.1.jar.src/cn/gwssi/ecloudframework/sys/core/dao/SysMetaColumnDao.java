package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.core.model.MetaColumn;
import org.apache.ibatis.annotations.Param;

public interface SysMetaColumnDao extends BaseDao<String, MetaColumn> {
  void updateStatus(@Param("id") String paramString, @Param("enabled") int paramInt);
  
  void updateGroupId(@Param("id") String paramString1, @Param("groupId") String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SysMetaColumnDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */