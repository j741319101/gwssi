/*    */ package cn.gwssi.ecloudframework.base.core.cache.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryCache<T>
/*    */   implements ICache<T>
/*    */ {
/* 18 */   private Map<String, T> map = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   public void add(String key, T obj) {
/* 22 */     if (key == null) {
/*    */       return;
/*    */     }
/* 25 */     this.map.put(key, obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public void delByKey(String key) {
/* 30 */     if (key == null) {
/*    */       return;
/*    */     }
/* 33 */     this.map.remove(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearAll() {
/* 38 */     this.map.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public T getByKey(String key) {
/* 43 */     if (key == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     return this.map.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containKey(String key) {
/* 51 */     if (key == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     return this.map.containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(String key, T obj, int timeout) {
/* 59 */     throw new BusinessException(BaseStatusCode.NOT_SUPPORT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add2Region(String region, String key, T obj) {
/* 64 */     add(key, obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public T getByKey(String region, String key) {
/* 69 */     return getByKey(key);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clearRegion(String region) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void delByKey(String region, String key) {
/* 79 */     delByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containKey(String region, String key) {
/* 84 */     return containKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> keys(String region) {
/* 89 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/cache/impl/MemoryCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */