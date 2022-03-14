package com.dstz.base.db.api.table;

import com.dstz.base.api.Page;
import com.dstz.base.db.api.table.model.Table;
import java.sql.SQLException;
import java.util.List;

public interface IViewOperator extends IDbType {
  void createOrRep(String paramString1, String paramString2) throws Exception;
  
  List<String> getViews(String paramString) throws Exception;
  
  List<String> getViews(String paramString, Page paramPage) throws SQLException, Exception;
  
  Table getModelByViewName(String paramString) throws SQLException;
  
  List<Table> getViewsByName(String paramString, Page paramPage) throws Exception;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/IViewOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */