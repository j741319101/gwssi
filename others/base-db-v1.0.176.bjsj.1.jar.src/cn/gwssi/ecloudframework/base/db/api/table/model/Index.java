package cn.gwssi.ecloudframework.base.db.api.table.model;

import java.util.List;

public interface Index {
  public static final String INDEX_TYPE_BITMAP = "BITMAP";
  
  public static final String INDEX_TYPE_BTREE = "BTREE";
  
  public static final String INDEX_TYPE_FUNCTION = "FUNCTION";
  
  public static final String INDEX_TYPE_HEAP = "HEAP";
  
  public static final String INDEX_TYPE_CLUSTERED = "CLUSTERED";
  
  public static final String INDEX_TYPE_NONCLUSTERED = "NONCLUSTERED";
  
  public static final String INDEX_TYPE_XML = "XML";
  
  public static final String INDEX_TYPE_SPATIAL = "SPATIAL";
  
  public static final String INDEX_TYPE_REG = "REGULAR";
  
  public static final String INDEX_TYPE_DIM = "DIMENSIONBLOCK";
  
  public static final String INDEX_TYPE_BLOK = "BLOCK";
  
  public static final String TABLE_TYPE_TABLE = "TABLE";
  
  public static final String TABLE_TYPE_VIEW = "VIEW";
  
  public static final String INDEX_STATUS_VALIDATE = "VALIDATE";
  
  public static final String INDEX_STATUS_INVALIDATE = "INVALIDATE";
  
  String getTableName();
  
  String getIndexName();
  
  boolean isUnique();
  
  String getIndexType();
  
  String getIndexComment();
  
  List<String> getColumnList();
  
  void setTableName(String paramString);
  
  void setTableType(String paramString);
  
  void setIndexName(String paramString);
  
  void setIndexType(String paramString);
  
  void setUnique(boolean paramBoolean);
  
  void setIndexStatus(String paramString);
  
  void setIndexDdl(String paramString);
  
  void setColumnList(List<String> paramList);
  
  void setPkIndex(boolean paramBoolean);
  
  void setIndexComment(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/model/Index.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */