/*    */ package cn.gwssi.ecloudbpm.form.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Util {
/*    */   public static <T> Map<String, T> map(String key, T value) {
/* 12 */     Map<String, T> map = new HashMap<>();
/* 13 */     map.put(key, value);
/* 14 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> List<T> list(T... value) {
/* 19 */     if (value == null) {
/* 20 */       return null;
/*    */     }
/* 22 */     List<T> list = new ArrayList<>();
/* 23 */     for (T t : value) {
/* 24 */       list.add(t);
/*    */     }
/* 26 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T[] array(T... value) {
/* 31 */     return value;
/*    */   }
/*    */   
/*    */   public static int[] intArray(int... value) {
/* 35 */     return value;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> Set<T> set(T... value) {
/* 40 */     if (value == null) {
/* 41 */       return null;
/*    */     }
/* 43 */     Set<T> set = new HashSet<>();
/* 44 */     for (T t : value) {
/* 45 */       set.add(t);
/*    */     }
/* 47 */     return set;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> boolean contains(Object data, T... compareDatas) {
/* 52 */     Set<T> set = set(compareDatas);
/* 53 */     if (set == null) {
/* 54 */       return false;
/*    */     }
/* 56 */     return set.contains(data);
/*    */   }
/*    */   
/*    */   public static boolean isEqual(Object data1, Object data2) {
/* 60 */     if (data1 == null && data2 == null) {
/* 61 */       return true;
/*    */     }
/* 63 */     if (data1 == null) {
/* 64 */       return false;
/*    */     }
/* 66 */     if (data2 == null) {
/* 67 */       return false;
/*    */     }
/* 69 */     return data1.toString().equals(data2.toString());
/*    */   }
/*    */   
/*    */   public static boolean equals(Object data1, Object data2) {
/* 73 */     return isEqual(data1, data2);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/util/Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */