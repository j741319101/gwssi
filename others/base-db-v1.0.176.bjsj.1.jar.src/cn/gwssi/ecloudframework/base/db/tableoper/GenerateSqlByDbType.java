package com.dstz.base.db.tableoper;

import com.dstz.base.db.model.table.Table;
import java.util.Map;

public interface GenerateSqlByDbType {
  boolean getCreateTableSql(StringBuilder paramStringBuilder, Table paramTable);
  
  boolean getUpdateTableSql(StringBuilder paramStringBuilder, Table paramTable);
  
  boolean checkUpdateTableColumn(Map<String, Object> paramMap, Table paramTable);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/GenerateSqlByDbType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */