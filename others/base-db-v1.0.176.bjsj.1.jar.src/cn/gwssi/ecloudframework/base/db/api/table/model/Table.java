package com.dstz.base.db.api.table.model;

import java.util.List;

public interface Table {
  String getTableName();
  
  String getComment();
  
  List<Column> getColumnList();
  
  List<Column> getPrimayKey();
  
  void setTableName(String paramString);
  
  void setComment(String paramString);
  
  void setColumnList(List<Column> paramList);
  
  void addColumn(Column paramColumn);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/model/Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */