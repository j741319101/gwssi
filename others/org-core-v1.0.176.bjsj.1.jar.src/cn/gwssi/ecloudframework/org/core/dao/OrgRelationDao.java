package com.dstz.org.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.org.core.model.OrgRelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrgRelationDao extends BaseDao<String, OrgRelation> {
  List<OrgRelation> getUserRelation(@Param("userId") String paramString1, @Param("relationType") String paramString2);
  
  void removeByUserId(@Param("userId") String paramString, @Param("relationTypes") List<String> paramList);
  
  void removeRelationByGroupId(@Param("groupType") String paramString1, @Param("groupId") String paramString2);
  
  List<OrgRelation> getRelationsByParam(@Param("relationTypes") List<String> paramList, @Param("userId") String paramString1, @Param("groupId") String paramString2, @Param("roleId") String paramString3);
  
  int getCountByRelation(OrgRelation paramOrgRelation);
  
  List<OrgRelation> getUserRole(String paramString);
  
  OrgRelation getPost(String paramString);
  
  void removeAllRelation(@Param("relationType") String paramString);
  
  void deleteRelationByUserIdAndType(@Param("userId") String paramString1, @Param("type") String paramString2, @Param("oldGroupId") String paramString3);
  
  void updateGroupId(OrgRelation paramOrgRelation);
  
  void updateGroupIdByUserId(OrgRelation paramOrgRelation);
  
  int updateByPrimaryKeySelective(OrgRelation paramOrgRelation);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/dao/OrgRelationDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */