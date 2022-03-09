package cn.gwssi.ecloudframework.base.api.model;

import java.util.List;

public interface Tree<C extends Tree<?>> {
  String getId();
  
  String getParentId();
  
  List<C> getChildren();
  
  void setChildren(List<C> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/model/Tree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */