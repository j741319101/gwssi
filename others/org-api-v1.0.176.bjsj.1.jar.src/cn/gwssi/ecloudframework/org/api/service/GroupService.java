package com.dstz.org.api.service;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.response.impl.PageResultDto;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.GroupQueryDTO;
import java.util.List;
import java.util.Map;

public interface GroupService {
  List<? extends IGroup> getGroupsByGroupTypeUserId(String paramString1, String paramString2);
  
  Map<String, List<? extends IGroup>> getAllGroupByAccount(String paramString);
  
  Map<String, List<? extends IGroup>> getAllGroupByUserId(String paramString);
  
  List<? extends IGroup> getGroupsByUserId(String paramString);
  
  IGroup getById(String paramString1, String paramString2);
  
  IGroup getByCode(String paramString1, String paramString2);
  
  IGroup getMainGroup(String paramString);
  
  List<? extends IGroup> getSiblingsGroups(String paramString);
  
  List<? extends IGroup> getRoleList(QueryFilter paramQueryFilter);
  
  List<? extends IGroup> getSameLevel(String paramString1, String paramString2);
  
  List<? extends IGroup> getGroupsById(String paramString1, String paramString2);
  
  List<? extends IGroup> getChildrenGroupsById(String paramString1, String paramString2);
  
  IGroup getMasterGroupByUserId(String paramString1, String paramString2);
  
  List<? extends IGroup> getGroupListByType(String paramString);
  
  PageResultDto getGroupsByGroupQuery(GroupQueryDTO paramGroupQueryDTO);
  
  String getCurrentManagerOrgIds();
  
  List<BpmOrgDTO> getOrgInfos(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/service/GroupService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */