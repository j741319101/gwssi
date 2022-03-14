package com.dstz.base.db.api.table;

import com.dstz.base.db.api.table.model.Column;
import com.dstz.base.db.api.table.model.Table;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITableOperator extends IDbType {
  String getColumnType(String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  String getColumnType(Column paramColumn);
  
  void createTable(Table paramTable) throws SQLException;
  
  void dropTable(String paramString) throws SQLException;
  
  void updateTableComment(String paramString1, String paramString2) throws SQLException;
  
  void addColumn(String paramString, Column paramColumn) throws SQLException;
  
  void updateColumn(String paramString1, String paramString2, Column paramColumn) throws SQLException;
  
  void addForeignKey(String paramString1, String paramString2, String paramString3, String paramString4);
  
  void dropForeignKey(String paramString1, String paramString2);
  
  List<String> getPKColumns(String paramString) throws SQLException;
  
  Map<String, List<String>> getPKColumns(List<String> paramList) throws SQLException;
  
  boolean isTableExist(String paramString);
  
  boolean isExsitPartition(String paramString1, String paramString2);
  
  void createPartition(String paramString1, String paramString2);
  
  boolean supportPartition(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/ITableOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */