/*    */ package com.dstz.base.core.spring;
/*    */ 
/*    */ import org.nustaq.serialization.FSTConfiguration;
/*    */ import org.springframework.data.redis.serializer.RedisSerializer;
/*    */ import org.springframework.data.redis.serializer.SerializationException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FstRedisSerializer
/*    */   implements RedisSerializer<Object>
/*    */ {
/*    */   private final FSTConfiguration fstConfiguration;
/* 16 */   private final byte[] EMPTY_BYTES = new byte[0];
/*    */   
/*    */   public FstRedisSerializer() {
/* 19 */     this.fstConfiguration = FSTConfiguration.getDefaultConfiguration();
/* 20 */     this.fstConfiguration.setClassLoader(FstRedisSerializer.class.getClassLoader());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] serialize(Object o) throws SerializationException {
/* 25 */     if (o == null) {
/* 26 */       return this.EMPTY_BYTES;
/*    */     }
/* 28 */     return this.fstConfiguration.asByteArray(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object deserialize(byte[] bytes) throws SerializationException {
/* 33 */     if (bytes == null || bytes.length == 0) {
/* 34 */       return null;
/*    */     }
/* 36 */     return this.fstConfiguration.asObject(bytes);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/spring/FstRedisSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */