package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.SysDataSource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public interface SysDataSourceManager extends Manager<String, SysDataSource> {
  DataSource tranform2DataSource(SysDataSource paramSysDataSource);
  
  SysDataSource getByKey(String paramString);
  
  DataSource getDataSourceByKey(String paramString, boolean paramBoolean);
  
  DataSource getDataSourceByKey(String paramString);
  
  JdbcTemplate getJdbcTemplateByKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysDataSourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */