package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.core.model.SysAuthorization;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface SysAuthorizationDao extends BaseDao<String, SysAuthorization> {
  List<SysAuthorization> getByTarget(@Param("rightsObject") String paramString1, @Param("rightsTarget") String paramString2);
  
  void deleteByTarget(@Param("rightsObject") String paramString1, @Param("rightsTarget") String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SysAuthorizationDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */