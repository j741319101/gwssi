package cn.gwssi.ecloudframework.base.core.cache;

import java.util.Collection;

public interface ICache<T> {
  void add(String paramString, T paramT, int paramInt);
  
  void add(String paramString, T paramT);
  
  void delByKey(String paramString);
  
  void clearAll();
  
  T getByKey(String paramString);
  
  boolean containKey(String paramString);
  
  void add2Region(String paramString1, String paramString2, T paramT);
  
  T getByKey(String paramString1, String paramString2);
  
  void clearRegion(String paramString);
  
  void delByKey(String paramString1, String paramString2);
  
  boolean containKey(String paramString1, String paramString2);
  
  Collection<String> keys(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/cache/ICache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */