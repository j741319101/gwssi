package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.core.model.WorkbenchLayout;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface WorkbenchLayoutDao extends BaseDao<String, WorkbenchLayout> {
  void removeByUserId(String paramString);
  
  List<WorkbenchLayout> getByUserId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/WorkbenchLayoutDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */