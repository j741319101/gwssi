package cn.gwssi.ecloudframework.base.db.api.table;

import cn.gwssi.ecloudframework.base.api.query.FieldSort;
import java.util.List;

public interface IDialect {
  boolean supportsLimit();
  
  boolean supportsLimitOffset();
  
  String getLimitString(String paramString, int paramInt1, int paramInt2);
  
  String getLimitString(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3);
  
  String getCountString(String paramString);
  
  String getSortString(String paramString, List<FieldSort> paramList);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/api/table/IDialect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */