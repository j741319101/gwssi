package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.api.model.SysNodeOrderParam;
import cn.gwssi.ecloudframework.sys.core.model.SysTreeNode;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface SysTreeNodeManager extends Manager<String, SysTreeNode> {
  List<SysTreeNode> getByTreeId(String paramString);
  
  SysTreeNode getByTreeIdAndKey(String paramString1, String paramString2);
  
  List<SysTreeNode> getByParentId(String paramString);
  
  List<SysTreeNode> getByParentKey(String paramString);
  
  List<SysTreeNode> getStartWithPath(String paramString);
  
  void removeByTreeId(String paramString);
  
  void removeByPath(String paramString);
  
  int chageOrder(SysNodeOrderParam paramSysNodeOrderParam);
  
  int getCountByStartWithPath(String paramString);
  
  void importData(XSSFWorkbook paramXSSFWorkbook, Map<String, Integer> paramMap);
  
  String findSysTreeNodeId(Map<String, String> paramMap, String paramString1, String paramString2);
  
  String creatByTreeKey(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysTreeNodeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */