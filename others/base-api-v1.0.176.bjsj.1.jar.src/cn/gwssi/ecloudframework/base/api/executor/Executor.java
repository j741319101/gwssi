package cn.gwssi.ecloudframework.base.api.executor;

public interface Executor<T> extends Comparable<Executor<T>> {
  String getKey();
  
  String getName();
  
  String type();
  
  String getCheckerKey();
  
  int getSn();
  
  void execute(T paramT);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/executor/Executor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */