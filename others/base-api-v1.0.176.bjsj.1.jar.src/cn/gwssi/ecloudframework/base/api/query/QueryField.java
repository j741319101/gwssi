package com.dstz.base.api.query;

public interface QueryField extends WhereClause {
  String getField();
  
  QueryOP getCompare();
  
  Object getValue();
  
  int getValueHashCode();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/QueryField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */