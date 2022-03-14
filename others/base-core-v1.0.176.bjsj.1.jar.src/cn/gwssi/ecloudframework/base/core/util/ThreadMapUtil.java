/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.function.Function;
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
/*    */ public class ThreadMapUtil
/*    */ {
/* 18 */   private static ThreadLocal<Map<String, Object>> threadLocalMap = new ThreadLocal<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Map<String, Object> map() {
/* 25 */     Map<String, Object> map = threadLocalMap.get();
/* 26 */     if (map == null) {
/* 27 */       threadLocalMap.set(new ConcurrentHashMap<>());
/* 28 */       map = threadLocalMap.get();
/*    */     } 
/* 30 */     return map;
/*    */   }
/*    */   
/*    */   public static void put(String key, Object value) {
/* 34 */     map().put(key, value);
/*    */   }
/*    */   
/*    */   public static Object get(String key) {
/* 38 */     return map().get(key);
/*    */   }
/*    */   
/*    */   public static Object remove(String key) {
/* 42 */     Object obj = get(key);
/* 43 */     map().remove(key);
/* 44 */     if (map().isEmpty()) {
/* 45 */       threadLocalMap.remove();
/*    */     }
/* 47 */     return obj;
/*    */   }
/*    */   
/*    */   public static Map getMap() {
/* 51 */     return threadLocalMap.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void remove() {
/* 59 */     threadLocalMap.remove();
/*    */   }
/*    */   
/*    */   public static Object getOrDefault(String key, Object defaultValue) {
/* 63 */     return map().getOrDefault(key, defaultValue);
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
/*    */   public static Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
/* 75 */     return map().computeIfAbsent(key, mappingFunction);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ThreadMapUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */