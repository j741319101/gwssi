/*    */ package com.dstz.base.core.id.snowflake;
/*    */ 
/*    */ import com.dstz.base.core.id.IdGenerator;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SnowflakeIdGenerator
/*    */   implements IdGenerator
/*    */ {
/*    */   private SnowflakeIdMeta idMeta;
/*    */   private AtomicReference<Variant> variant;
/*    */   
/*    */   public SnowflakeIdGenerator(SnowflakeIdMeta idMeta) {
/* 31 */     this.variant = new AtomicReference<>(new Variant());
/*    */     Assert.notNull(idMeta, "idMeta is not null");
/*    */     this.idMeta = idMeta;
/*    */   } private long tillNextTimestamp(long lastTimestamp) {
/* 35 */     long timestamp = System.currentTimeMillis();
/* 36 */     while (timestamp < lastTimestamp) {
/* 37 */       timestamp = System.currentTimeMillis();
/*    */     }
/* 39 */     return timestamp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getUId() {
/*    */     Variant oldVariant, newVariant;
/*    */     do {
/* 47 */       oldVariant = this.variant.get();
/*    */       
/* 49 */       long lastTimestamp = oldVariant.lastTimestamp;
/*    */       
/* 51 */       long timestamp = tillNextTimestamp(lastTimestamp);
/* 52 */       long sequence = oldVariant.sequence;
/*    */       
/* 54 */       if (lastTimestamp == timestamp) {
/* 55 */         sequence = sequence + 1L & this.idMeta.getSequenceMask();
/* 56 */         if (sequence == 0L) {
/* 57 */           timestamp = tillNextTimestamp(lastTimestamp);
/*    */         }
/*    */       } else {
/* 60 */         sequence = 0L;
/*    */       } 
/*    */       
/* 63 */       newVariant = new Variant();
/* 64 */       newVariant.sequence = sequence;
/* 65 */       newVariant.lastTimestamp = timestamp;
/*    */     }
/* 67 */     while (!this.variant.compareAndSet(oldVariant, newVariant));
/*    */ 
/*    */ 
/*    */     
/* 71 */     long uid = this.idMeta.getMachine();
/* 72 */     uid |= newVariant.sequence << (int)this.idMeta.getSequenceStartPos();
/* 73 */     uid |= newVariant.lastTimestamp << (int)this.idMeta.getTimeStartPos();
/* 74 */     return Long.valueOf(uid);
/*    */   } private static class Variant {
/*    */     long sequence; private Variant() {}
/*    */     long lastTimestamp = -1L; }
/*    */   public String getSuid() {
/* 79 */     return Long.toString(getUId().longValue());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/id/snowflake/SnowflakeIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */