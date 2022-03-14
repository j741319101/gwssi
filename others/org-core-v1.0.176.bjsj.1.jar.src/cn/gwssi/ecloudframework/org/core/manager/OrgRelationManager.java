package com.dstz.org.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.org.core.model.OrgRelation;
import java.util.List;

public interface OrgRelationManager extends Manager<String, OrgRelation> {
  List<OrgRelation> getPostByUserId(String paramString);
  
  List<OrgRelation> getUserRelation(String paramString1, String paramString2);
  
  void removeByUserId(String paramString, List<String> paramList);
  
  void removeRelationByGroupId(String paramString1, String paramString2);
  
  void updateUserGroupRelationIsMaster(String paramString1, String paramString2, String paramString3);
  
  void changeStatus(String paramString, int paramInt);
  
  void saveUserGroupRelation(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2);
  
  int saveRoleUsers(String paramString, String[] paramArrayOfString);
  
  List<OrgRelation> getUserRole(String paramString);
  
  OrgRelation getPost(String paramString);
  
  void removeCheck(String paramString);
  
  void removeAllRelation(String paramString);
  
  void modifyUserOrg(List<OrgRelation> paramList);
  
  void modifyAllUserGroup(String paramString1, String paramString2, String paramString3, String paramString4);
  
  void updateGroupIdByUserId(List<OrgRelation> paramList, String paramString);
  
  void batchAdd(List<OrgRelation> paramList);
  
  void batchRemove(List<OrgRelation> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/OrgRelationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */