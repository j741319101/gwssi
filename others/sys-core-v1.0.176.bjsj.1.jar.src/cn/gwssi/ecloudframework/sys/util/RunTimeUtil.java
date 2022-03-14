/*    */ package com.dstz.sys.util;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.RuntimeMXBean;
/*    */ import java.util.Random;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunTimeUtil
/*    */ {
/* 14 */   private static AtomicInteger index = new AtomicInteger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getPid() {
/* 20 */     String info = getRunTimeInfo();
/* 21 */     int pid = (new Random()).nextInt();
/* 22 */     int index = info.indexOf("@");
/* 23 */     if (index > 0) {
/* 24 */       pid = Integer.parseInt(info.substring(0, index));
/*    */     }
/*    */     
/* 27 */     return pid;
/*    */   }
/*    */   
/*    */   public static String getRunTimeInfo() {
/* 31 */     RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
/* 32 */     String info = runtime.getName();
/* 33 */     return info;
/*    */   }
/*    */   
/*    */   public static String getRocketMqUniqeInstanceName() {
/* 37 */     return "pid" + getPid() + "_index" + index.incrementAndGet();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/RunTimeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */