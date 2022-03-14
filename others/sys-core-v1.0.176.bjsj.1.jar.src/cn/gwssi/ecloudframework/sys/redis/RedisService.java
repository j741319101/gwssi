/*     */ package com.dstz.sys.redis;
/*     */ 
/*     */ import com.dstz.base.core.util.SerializeUtil;
/*     */ import com.dstz.sys.api.redis.IRedisService;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.dao.DataAccessException;
/*     */ import org.springframework.data.redis.connection.RedisConnection;
/*     */ import org.springframework.data.redis.core.RedisCallback;
/*     */ import org.springframework.data.redis.core.RedisTemplate;
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class RedisService
/*     */   implements IRedisService
/*     */ {
/*  19 */   private static String redisCode = "utf-8";
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private RedisTemplate<String, Serializable> redisTemplate;
/*     */ 
/*     */ 
/*     */   
/*     */   public long del(String... keys) {
/*  29 */     return ((Long)this.redisTemplate.execute(new RedisCallback<Long>() {
/*     */           public Long doInRedis(RedisConnection connection) throws DataAccessException {
/*  31 */             long result = 0L;
/*  32 */             for (int i = 0; i < keys.length; i++) {
/*  33 */               result = connection.del(new byte[][] { this.val$keys[i].getBytes() }).longValue();
/*     */             } 
/*  35 */             return Long.valueOf(result);
/*     */           }
/*     */         })).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(final byte[] key, final byte[] value, final long liveTime) {
/*  45 */     this.redisTemplate.execute(new RedisCallback<Long>() {
/*     */           public Long doInRedis(RedisConnection connection) throws DataAccessException {
/*  47 */             connection.set(key, value);
/*  48 */             if (liveTime > 0L) {
/*  49 */               connection.expire(key, liveTime);
/*     */             }
/*  51 */             return Long.valueOf(1L);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, String value, long liveTime) {
/*  61 */     set(key.getBytes(), value.getBytes(), liveTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, String value) {
/*  69 */     set(key, value, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, Object object) {
/*  77 */     set(key, object, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, Object object, long liveTime) {
/*  85 */     byte[] value = SerializeUtil.serialize(object);
/*  86 */     set(key.getBytes(), value, liveTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, byte[] value) {
/*  94 */     set(key.getBytes(), value, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] key, byte[] value) {
/* 102 */     set(key, value, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(final String key) {
/* 110 */     return (String)this.redisTemplate.execute(new RedisCallback<String>() {
/*     */           public String doInRedis(RedisConnection connection) throws DataAccessException {
/*     */             try {
/* 113 */               if (connection.exists(key.getBytes()).booleanValue()) {
/* 114 */                 return new String(connection.get(key.getBytes()), RedisService.redisCode);
/*     */               }
/* 116 */             } catch (UnsupportedEncodingException e) {
/* 117 */               e.printStackTrace();
/*     */             } 
/* 119 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(String key) {
/* 129 */     byte[] bytes = getBytes(key);
/* 130 */     if (bytes == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return SerializeUtil.unserialize(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes(final String key) {
/* 141 */     return (byte[])this.redisTemplate.execute(new RedisCallback<byte[]>() {
/*     */           public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
/*     */             try {
/* 144 */               if (connection.exists(key.getBytes()).booleanValue()) {
/* 145 */                 return connection.get(key.getBytes());
/*     */               }
/* 147 */             } catch (Exception e) {
/* 148 */               e.printStackTrace();
/*     */             } 
/* 150 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(final String key, final String charsetName) {
/* 160 */     return (String)this.redisTemplate.execute(new RedisCallback<String>() {
/*     */           public String doInRedis(RedisConnection connection) throws DataAccessException {
/*     */             try {
/* 163 */               if (connection.exists(key.getBytes()).booleanValue()) {
/* 164 */                 return new String(connection.get(key.getBytes(charsetName)), RedisService.redisCode);
/*     */               }
/* 166 */             } catch (UnsupportedEncodingException e) {
/* 167 */               e.printStackTrace();
/*     */             } 
/* 169 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> keys(String pattern) {
/* 179 */     return this.redisTemplate.keys(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists(final String key) {
/* 188 */     return ((Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
/*     */           public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
/* 190 */             return connection.exists(key.getBytes());
/*     */           }
/*     */         })).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String flushDB() {
/* 200 */     return (String)this.redisTemplate.execute(new RedisCallback<String>() {
/*     */           public String doInRedis(RedisConnection connection) throws DataAccessException {
/* 202 */             connection.flushDb();
/* 203 */             return "ok";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long dbSize() {
/* 213 */     return ((Long)this.redisTemplate.execute(new RedisCallback<Long>() {
/*     */           public Long doInRedis(RedisConnection connection) throws DataAccessException {
/* 215 */             return connection.dbSize();
/*     */           }
/*     */         })).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String ping() {
/* 225 */     return (String)this.redisTemplate.execute(new RedisCallback<String>() {
/*     */           public String doInRedis(RedisConnection connection) throws DataAccessException {
/* 227 */             return connection.ping();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/redis/RedisService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */