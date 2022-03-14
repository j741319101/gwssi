/*    */ package com.dstz.base.core.executor;
/*    */ 
/*    */ import com.dstz.base.api.executor.Executor;
/*    */ import com.dstz.base.api.executor.ExecutorType;
/*    */ import com.dstz.base.api.executor.checker.ExecutorChecker;
/*    */ import com.dstz.base.core.executor.checker.ExecutorCheckerService;
/*    */ import com.dstz.base.core.executor.checker.FreeExecutorChecker;
/*    */ import java.util.List;
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
/*    */ public abstract class AbstractExecutor<T>
/*    */   implements Executor<T>
/*    */ {
/*    */   public String getKey() {
/* 27 */     return getClass().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 39 */     return getClass().getSimpleName();
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Executor<T> executor) {
/* 44 */     return Integer.valueOf(getSn()).compareTo(Integer.valueOf(executor.getSn()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String type() {
/* 50 */     return ExecutorType.NECESSARY.getKey();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCheckerKey() {
/* 56 */     return FreeExecutorChecker.class.getSimpleName();
/*    */   }
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
/*    */   public void execute(T param) {
/* 69 */     List<ExecutorChecker> checkers = ExecutorCheckerService.getCheckers(getCheckerKey());
/* 70 */     for (ExecutorChecker checker : checkers) {
/* 71 */       if (!checker.check(getKey())) {
/*    */         return;
/*    */       }
/*    */     } 
/* 75 */     run(param);
/*    */   }
/*    */   
/*    */   protected abstract void run(T paramT);
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/executor/AbstractExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */