package cn.gwssi.ecloudframework.sys.api.service;

import cn.gwssi.ecloudframework.sys.api.model.ISysDataSource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public interface ISysDataSourceService {
  ISysDataSource getByKey(String paramString);
  
  DataSource getDataSourceByKey(String paramString);
  
  JdbcTemplate getJdbcTemplateByKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/ISysDataSourceService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */