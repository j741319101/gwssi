/*    */ package com.dstz.base.core.id.snowflake;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SnowflakeIdMeta
/*    */ {
/*    */   private long machine;
/*    */   private byte machineBits;
/*    */   private byte sequenceBits;
/*    */   private byte timeSequence;
/*    */   
/*    */   public SnowflakeIdMeta(long machine, byte machineBits, byte sequenceBits, byte timeSequence) {
/* 19 */     this.machine = machine;
/* 20 */     this.machineBits = machineBits;
/* 21 */     this.sequenceBits = sequenceBits;
/* 22 */     this.timeSequence = timeSequence;
/*    */   }
/*    */   
/*    */   public long getMachine() {
/* 26 */     return this.machine;
/*    */   }
/*    */   
/*    */   public byte getMachineBits() {
/* 30 */     return this.machineBits;
/*    */   }
/*    */   
/*    */   public byte getSequenceBits() {
/* 34 */     return this.sequenceBits;
/*    */   }
/*    */   
/*    */   public byte getTimeSequence() {
/* 38 */     return this.timeSequence;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSequenceMask() {
/* 43 */     return (0xFFFFFFFF ^ -1 << this.sequenceBits);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSequenceStartPos() {
/* 48 */     return this.machineBits;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTimeStartPos() {
/* 53 */     return (this.machineBits + this.sequenceBits);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/id/snowflake/SnowflakeIdMeta.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */