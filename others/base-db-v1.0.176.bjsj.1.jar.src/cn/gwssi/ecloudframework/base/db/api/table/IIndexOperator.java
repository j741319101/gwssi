package cn.gwssi.ecloudframework.base.db.api.table;

import cn.gwssi.ecloudframework.base.db.api.table.model.Index;
import java.sql.SQLException;
import java.util.List;

public interface IIndexOperator extends IDbType {
  void createIndex(Index paramIndex) throws SQLException;
  
  void dropIndex(String paramString1, String paramString2);
  
  Index getIndex(String paramString1, String paramString2) throws SQLException;
  
  List<Index> getIndexByFuzzyMatch(String paramString) throws SQLException;
  
  List<Index> getIndexsByTable(String paramString) throws SQLException;
  
  void rebuildIndex(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/IIndexOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */