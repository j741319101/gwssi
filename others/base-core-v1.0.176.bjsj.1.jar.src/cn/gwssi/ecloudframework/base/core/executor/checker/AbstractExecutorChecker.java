/*    */ package com.dstz.base.core.executor.checker;
/*    */ 
/*    */ import com.dstz.base.api.executor.checker.ExecutorChecker;
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
/*    */ public abstract class AbstractExecutorChecker
/*    */   implements ExecutorChecker
/*    */ {
/*    */   public String getKey() {
/* 21 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/executor/checker/AbstractExecutorChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */