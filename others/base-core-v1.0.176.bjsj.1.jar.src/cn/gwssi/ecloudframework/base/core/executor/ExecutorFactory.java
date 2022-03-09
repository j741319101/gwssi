/*    */ package cn.gwssi.ecloudframework.base.core.executor;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.executor.Executor;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutorFactory
/*    */ {
/* 20 */   private static final Map<Class, Map> sourceMap = (Map)new HashMap<>();
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
/*    */   private static <T> Map<String, Executor<T>> executorMap(Class<? extends Executor<T>> cls) {
/* 32 */     Map<String, Executor<T>> executorMap = sourceMap.get(cls);
/* 33 */     if (executorMap != null) {
/* 34 */       return executorMap;
/*    */     }
/* 36 */     executorMap = new LinkedHashMap<>();
/* 37 */     Map<String, ? extends Executor<T>> map = AppUtil.getImplInstance(cls);
/* 38 */     List<Executor<T>> list = new ArrayList<>();
/* 39 */     for (Map.Entry<String, ? extends Executor<T>> entry : map.entrySet()) {
/* 40 */       Executor<T> executor = entry.getValue();
/* 41 */       list.add(executor);
/*    */     } 
/* 43 */     Collections.sort(list);
/* 44 */     for (Executor<T> executor : list) {
/* 45 */       executorMap.put(executor.getKey(), executor);
/*    */     }
/*    */ 
/*    */     
/* 49 */     sourceMap.put(cls, executorMap);
/*    */     
/* 51 */     return executorMap;
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
/*    */   
/*    */   public static <T> void execute(Class<? extends Executor<T>> cls, T param) {
/* 65 */     for (Map.Entry<String, Executor<T>> entry : (Iterable<Map.Entry<String, Executor<T>>>)executorMap(cls).entrySet()) {
/* 66 */       Executor<T> executor = entry.getValue();
/* 67 */       executor.execute(param);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/executor/ExecutorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */