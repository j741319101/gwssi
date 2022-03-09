package cn.gwssi.ecloudframework.base.manager;

import cn.gwssi.ecloudframework.base.api.Page;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import java.util.List;

public interface Manager<PK extends java.io.Serializable, T> {
  void create(T paramT);
  
  void update(T paramT);
  
  void remove(PK paramPK);
  
  T get(PK paramPK);
  
  void removeByIds(PK... paramVarArgs);
  
  List<T> query(QueryFilter paramQueryFilter);
  
  List<T> getAll();
  
  List<T> getAllByPage(Page paramPage);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/manager/Manager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */