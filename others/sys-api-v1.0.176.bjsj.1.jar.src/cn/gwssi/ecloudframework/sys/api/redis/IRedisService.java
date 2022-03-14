package com.dstz.sys.api.redis;

import java.util.Set;

public interface IRedisService {
  long del(String... paramVarArgs);
  
  void set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong);
  
  void set(String paramString1, String paramString2, long paramLong);
  
  void set(String paramString1, String paramString2);
  
  void set(String paramString, Object paramObject);
  
  void set(String paramString, Object paramObject, long paramLong);
  
  void set(String paramString, byte[] paramArrayOfbyte);
  
  void set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  String get(String paramString);
  
  Object getObject(String paramString);
  
  byte[] getBytes(String paramString);
  
  String get(String paramString1, String paramString2);
  
  Set<String> keys(String paramString);
  
  boolean exists(String paramString);
  
  String flushDB();
  
  long dbSize();
  
  String ping();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/redis/IRedisService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */