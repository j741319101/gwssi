package com.dstz.sys.core.dao;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.WorkbenchPanel;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface WorkbenchPanelDao extends BaseDao<String, WorkbenchPanel> {
  List<WorkbenchPanel> getUsablePanelsByUserRight(QueryFilter paramQueryFilter);
  
  List<WorkbenchPanel> getByUser(Map<String, Object> paramMap);
  
  List<WorkbenchPanel> getBylayoutKey(@Param("layoutKey") String paramString1, @Param("dbType") String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/WorkbenchPanelDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */