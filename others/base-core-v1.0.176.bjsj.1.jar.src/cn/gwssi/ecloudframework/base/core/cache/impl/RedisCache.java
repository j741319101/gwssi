/*    */ package com.dstz.base.core.cache.impl;
/*    */ 
/*    */ import com.dstz.base.api.exception.SerializeException;
/*    */ import com.dstz.base.core.cache.ICache;
/*    */ import java.util.Collection;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.dao.DataAccessException;
/*    */ import org.springframework.data.redis.connection.RedisConnection;
/*    */ import org.springframework.data.redis.core.RedisTemplate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RedisCache<T>
/*    */   implements ICache<T>
/*    */ {
/* 20 */   private Logger logger = LoggerFactory.getLogger(RedisCache.class);
/*    */   
/*    */   @Autowired
/*    */   private RedisTemplate redisTemplate;
/*    */ 
/*    */   
/*    */   public synchronized void add(String key, T obj) {
/* 27 */     this.redisTemplate.boundValueOps(key).set(obj);
/* 28 */     this.logger.info("redis add " + key);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void add(String key, T obj, int timeout) {
/* 33 */     this.redisTemplate.boundValueOps(key).set(obj, timeout);
/* 34 */     this.logger.info("redis add " + key + " timeout " + timeout);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void delByKey(String key) {
/* 39 */     this.redisTemplate.delete(key);
/* 40 */     this.logger.info("redis delByKey " + key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearAll() {
/* 45 */     this.redisTemplate.execute(redisConnection -> {
/*    */           redisConnection.flushDb();
/*    */           return null;
/*    */         });
/* 49 */     this.logger.info("redis flushDB");
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized T getByKey(String key) {
/*    */     try {
/* 55 */       return (T)this.redisTemplate.opsForValue().get(key);
/* 56 */     } catch (SerializeException e) {
/* 57 */       delByKey(key);
/* 58 */       this.logger.warn("获取缓存对象失败，反序列化失败，已经删除缓存：{}", key, e);
/*    */       
/* 60 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean containKey(String key) {
/* 65 */     return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void add2Region(String region, String key, T obj) {
/* 71 */     add(key, obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public T getByKey(String region, String key) {
/* 76 */     return getByKey(key);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clearRegion(String region) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void delByKey(String region, String key) {
/* 86 */     delByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containKey(String region, String key) {
/* 91 */     return containKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> keys(String region) {
/* 96 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/cache/impl/RedisCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */