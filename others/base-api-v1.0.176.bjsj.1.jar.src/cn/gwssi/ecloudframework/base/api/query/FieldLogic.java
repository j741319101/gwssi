package com.dstz.base.api.query;

import java.util.List;

public interface FieldLogic extends WhereClause {
  List<WhereClause> getWhereClauses();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/FieldLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */