package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.api.model.SysNodeOrderParam;
import com.dstz.sys.core.model.SysTreeNode;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface SysTreeNodeDao extends BaseDao<String, SysTreeNode> {
  void removeByTreeId(String paramString);
  
  void removeByPath(String paramString);
  
  int chageOrder(SysNodeOrderParam paramSysNodeOrderParam);
  
  Integer getCountByStartWithPath(String paramString);
  
  void removeAll();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SysTreeNodeDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */