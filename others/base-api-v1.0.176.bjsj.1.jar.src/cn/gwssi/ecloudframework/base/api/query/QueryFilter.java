package cn.gwssi.ecloudframework.base.api.query;

import cn.gwssi.ecloudframework.base.api.Page;
import java.util.List;
import java.util.Map;

public interface QueryFilter {
  Page getPage();
  
  void setPage(Page paramPage);
  
  FieldLogic getFieldLogic();
  
  Map<String, Object> getParams();
  
  void addParams(Map<String, Object> paramMap);
  
  List<FieldSort> getFieldSortList();
  
  void addFilter(String paramString, Object paramObject, QueryOP paramQueryOP);
  
  void addParamsFilter(String paramString, Object paramObject);
  
  void addFieldSort(String paramString1, String paramString2);
  
  String getWhereSql();
  
  String getOrderBySql();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/QueryFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */