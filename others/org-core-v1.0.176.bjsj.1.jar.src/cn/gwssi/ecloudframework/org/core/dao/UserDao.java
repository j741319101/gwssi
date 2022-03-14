package com.dstz.org.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.org.core.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface UserDao extends BaseDao<String, User> {
  User getByAccount(String paramString);
  
  Integer isUserExist(User paramUser);
  
  List<User> getUserListByRelation(@Param("relationId") String paramString1, @Param("relationType") String paramString2);
  
  List<User> getUserListByGroupPath(@Param("path") String paramString);
  
  void removeOutSystemUser();
  
  User getByOpenid(String paramString);
  
  int updateByPrimaryKeySelective(User paramUser);
  
  String getIdByAccount(String paramString);
  
  Integer getAllEnableUserNum();
  
  List<User> getUsersByOrgPath(@Param("orgPath") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/dao/UserDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */