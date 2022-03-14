package com.dstz.base.db.api.table.model;

public interface Column {
  public static final String COLUMN_TYPE_VARCHAR = "varchar";
  
  public static final String COLUMN_TYPE_CLOB = "clob";
  
  public static final String COLUMN_TYPE_NUMBER = "number";
  
  public static final String COLUMN_TYPE_INT = "int";
  
  public static final String COLUMN_TYPE_DATE = "date";
  
  String getFieldName();
  
  String getComment();
  
  boolean getIsPk();
  
  boolean getIsNull();
  
  String getColumnType();
  
  int getCharLen();
  
  int getIntLen();
  
  int getDecimalLen();
  
  String getDefaultValue();
  
  String getTableName();
  
  void setFieldName(String paramString);
  
  void setColumnType(String paramString);
  
  void setComment(String paramString);
  
  void setIsNull(boolean paramBoolean);
  
  void setIsPk(boolean paramBoolean);
  
  void setCharLen(int paramInt);
  
  void setIntLen(int paramInt);
  
  void setDecimalLen(int paramInt);
  
  void setDefaultValue(String paramString);
  
  void setTableName(String paramString);
  
  int getIsRequired();
  
  void setIsRequired(int paramInt);
  
  void setFormat(String paramString);
  
  String getFormat();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/model/Column.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */