/*    */ package cn.gwssi.ecloudframework.base.core.executor.checker;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.executor.checker.ExecutorChecker;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class ExecutorCheckerService
/*    */ {
/* 21 */   private static Map<String, ExecutorChecker> checkerMap = new HashMap<>();
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
/*    */   public static ExecutorChecker getChecker(String key) {
/* 40 */     if (checkerMap.isEmpty()) {
/* 41 */       Map<String, ExecutorChecker> map = AppUtil.getImplInstance(ExecutorChecker.class);
/* 42 */       for (Map.Entry<String, ExecutorChecker> entry : map.entrySet()) {
/* 43 */         ExecutorChecker checker = entry.getValue();
/* 44 */         checkerMap.put(checker.getKey(), checker);
/*    */       } 
/*    */     } 
/* 47 */     if (checkerMap.get(key) == null) {
/* 48 */       throw new RuntimeException("找不到执行器校验器[" + key + "]");
/*    */     }
/* 50 */     return checkerMap.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<ExecutorChecker> getCheckers(String keys) {
/* 55 */     List<ExecutorChecker> checkers = new ArrayList<>();
/* 56 */     for (String key : keys.split(",")) {
/* 57 */       checkers.add(getChecker(key));
/*    */     }
/* 59 */     return checkers;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/executor/checker/ExecutorCheckerService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */