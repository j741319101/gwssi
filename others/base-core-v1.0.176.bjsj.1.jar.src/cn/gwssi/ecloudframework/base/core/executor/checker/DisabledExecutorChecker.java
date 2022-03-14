/*    */ package com.dstz.base.core.executor.checker;
/*    */ 
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class DisabledExecutorChecker
/*    */   extends AbstractExecutorChecker
/*    */ {
/*    */   public String getName() {
/* 15 */     return "不可用的校验器";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check(String pluginKey) {
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/executor/checker/DisabledExecutorChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */