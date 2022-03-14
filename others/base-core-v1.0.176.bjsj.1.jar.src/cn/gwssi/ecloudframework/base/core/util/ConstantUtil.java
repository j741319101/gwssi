/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.HashMap;
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
/*    */ public class ConstantUtil
/*    */ {
/*    */   public static JSONObject get(String classPath, String key) {
/* 31 */     JSONObject json = get(classPath).get(key);
/* 32 */     if (json != null) {
/* 33 */       return json;
/*    */     }
/* 35 */     throw new RuntimeException("类中" + classPath + "获取不到常量[" + key + "]");
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
/*    */   public static Map<String, JSONObject> get(String classPath) {
/*    */     try {
/* 49 */       Map<String, JSONObject> map = new HashMap<>();
/* 50 */       Class<?> clazz = Class.forName(classPath);
/* 51 */       Field[] fileds = clazz.getFields();
/* 52 */       for (Field f : fileds) {
/* 53 */         if (Modifier.isPublic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
/* 54 */           JSONObject json = new JSONObject();
/* 55 */           json.put("key", f.getName());
/* 56 */           json.put("value", f.get(clazz));
/* 57 */           json.put("type", f.getType());
/* 58 */           map.put(f.getName(), json);
/*    */         } 
/*    */       } 
/* 61 */       return map;
/* 62 */     } catch (Exception e) {
/* 63 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ConstantUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */