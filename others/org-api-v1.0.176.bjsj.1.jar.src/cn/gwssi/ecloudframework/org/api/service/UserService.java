package com.dstz.org.api.service;

import com.dstz.base.api.response.impl.PageResultDto;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.IUserRole;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.api.model.dto.UserDTO;
import com.dstz.org.api.model.dto.UserQueryDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
  IUser getUserById(String paramString);
  
  IUser getUserByAccount(String paramString);
  
  List<? extends IUser> getUserListByGroup(String paramString1, String paramString2);
  
  List<? extends IUserRole> getUserRole(String paramString);
  
  List<? extends IUser> getAllUser();
  
  List<? extends IUser> getUsersByUsername(String paramString);
  
  List<? extends IUser> getUserListByGroupPath(String paramString);
  
  IUser getUserByOpenid(String paramString);
  
  void updateUserOpenId(String paramString1, String paramString2);
  
  Map<String, ? extends IUser> getUserByIds(String paramString);
  
  List<? extends IUser> getUsersByOrgIds(String paramString);
  
  List<? extends IUser> getUsersByRoleIds(String paramString);
  
  List<? extends IUser> getUsersByPostIds(String paramString);
  
  Integer countEnabledUser();
  
  List<? extends IUser> getUsersByUserName(String paramString1, String paramString2, Integer paramInteger1, Integer paramInteger2);
  
  List<? extends IUser> getUsersByOrgPath(String paramString);
  
  List<? extends IUser> getUserByAccounts(String paramString);
  
  List<? extends IUser> getUsersByGroup(String paramString1, String paramString2);
  
  List<? extends IUser> getUsersByMobiles(String paramString);
  
  PageResultDto getUsersByUserQuery(UserQueryDTO paramUserQueryDTO);
  
  String addUser(UserDTO paramUserDTO);
  
  Integer editUser(UserDTO paramUserDTO);
  
  boolean isAdmin(IUser paramIUser);
  
  List<BpmUserDTO> getUserOrgInfos(String paramString);
  
  Map<String, String> getUserMapByUserIds(Set<String> paramSet);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/service/UserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */