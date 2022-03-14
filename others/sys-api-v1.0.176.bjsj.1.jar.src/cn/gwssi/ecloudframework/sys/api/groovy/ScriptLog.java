/*    */ package com.dstz.sys.api.groovy;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ScriptLog
/*    */ {
/* 12 */   private static Logger logger = LoggerFactory.getLogger(ScriptLog.class);
/*    */   public void info(Object message) {
/* 14 */     logger.info(String.valueOf(message));
/*    */   }
/*    */   public void debug(Object message) {
/* 17 */     logger.debug(String.valueOf(message));
/*    */   }
/*    */   public void error(Object message) {
/* 20 */     logger.error(String.valueOf(message));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/groovy/ScriptLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */