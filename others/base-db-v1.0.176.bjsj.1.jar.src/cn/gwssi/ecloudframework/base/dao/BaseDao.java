package com.dstz.base.dao;

import com.dstz.base.api.query.QueryFilter;
import java.util.List;

public interface BaseDao<PK extends java.io.Serializable, T> {
  void create(T paramT);
  
  Integer update(T paramT);
  
  void remove(PK paramPK);
  
  T get(PK paramPK);
  
  List<T> query(QueryFilter paramQueryFilter);
  
  List<T> query();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/dao/BaseDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */