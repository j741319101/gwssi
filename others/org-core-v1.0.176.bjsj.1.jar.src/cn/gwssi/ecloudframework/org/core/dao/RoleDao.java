package com.dstz.org.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.org.core.model.Role;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface RoleDao extends BaseDao<String, Role> {
  Role getByAlias(String paramString);
  
  Integer isRoleExist(Role paramRole);
  
  List<Role> getByUserId(String paramString);
  
  void updateByPrimaryKeySelective(Role paramRole);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/dao/RoleDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */