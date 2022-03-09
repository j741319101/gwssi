package cn.gwssi.ecloudframework.org.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.org.core.model.Group;
import java.util.List;
import java.util.Map;

public interface GroupManager extends Manager<String, Group> {
  Group getByCode(String paramString);
  
  List<Group> getByUserId(String paramString);
  
  Group getMainGroup(String paramString);
  
  void removeAll();
  
  void chageOrder(List<Group> paramList);
  
  List<Group> queryAllGroup();
  
  List<Group> getCurrentManagerOrgIds(String paramString);
  
  String findOrgId(Map<String, String> paramMap, String paramString);
  
  int updateByPrimaryKeySelective(Group paramGroup);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/GroupManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */