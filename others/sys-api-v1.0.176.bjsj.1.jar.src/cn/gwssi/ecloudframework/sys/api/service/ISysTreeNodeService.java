package com.dstz.sys.api.service;

import com.dstz.sys.api.model.ISysTreeNode;
import java.util.List;

public interface ISysTreeNodeService {
  ISysTreeNode getById(String paramString);
  
  List<? extends ISysTreeNode> getTreeNodesByType(String paramString);
  
  List<? extends ISysTreeNode> getTreeNodesByNodeId(String paramString);
  
  String creatByTreeKey(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/service/ISysTreeNodeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */