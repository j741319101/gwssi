/*     */ package com.dstz.base.core.util;
/*     */ 
/*     */ import com.dstz.base.core.ext.IEnumExtraDataLoader;
/*     */ import com.dstz.base.core.model.EnumExtraData;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumUtil
/*     */ {
/*     */   public static JSONObject toJSON(Class<? extends Enum<?>> enumClass) {
/*     */     try {
/*  46 */       Method method = enumClass.getMethod("values", new Class[0]);
/*  47 */       Enum[] arrayOfEnum = (Enum[])method.invoke(enumClass, null);
/*  48 */       JSONObject jsonObject = new JSONObject(arrayOfEnum.length, true);
/*  49 */       for (Enum<?> e : arrayOfEnum) {
/*  50 */         jsonObject.put(e.name(), toJSON(e));
/*     */       }
/*  52 */       loadEnumExtraData(enumClass, enumExtraData -> jsonObject.put(enumExtraData.getName(), JSON.toJSON(enumExtraData)));
/*  53 */       return jsonObject;
/*  54 */     } catch (Exception e) {
/*  55 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JSONArray toJSONArray(Class<? extends Enum<?>> enumClass) {
/*     */     try {
/*  61 */       Method method = enumClass.getMethod("values", new Class[0]);
/*  62 */       Enum[] arrayOfEnum = (Enum[])method.invoke(enumClass, null);
/*  63 */       JSONArray jsonArray = new JSONArray(arrayOfEnum.length);
/*  64 */       for (Enum<?> e : arrayOfEnum) {
/*  65 */         jsonArray.add(toJSON(e));
/*     */       }
/*  67 */       loadEnumExtraData(enumClass, enumExtraData -> jsonArray.add(JSON.toJSON(enumExtraData)));
/*  68 */       return jsonArray;
/*  69 */     } catch (Exception e) {
/*  70 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JSONArray toJSONArray(Class<? extends Enum<?>> enumClass, String tag) {
/*     */     try {
/*  76 */       Method method = enumClass.getMethod("getValuesByTags", new Class[] { String.class });
/*  77 */       Enum[] arrayOfEnum = (Enum[])method.invoke(enumClass, new Object[] { tag });
/*  78 */       JSONArray jsonArray = new JSONArray(arrayOfEnum.length);
/*  79 */       for (Enum<?> e : arrayOfEnum) {
/*  80 */         jsonArray.add(toJSON(e));
/*     */       }
/*  82 */       loadEnumExtraData(enumClass, enumExtraData -> jsonArray.add(JSON.toJSON(enumExtraData)));
/*  83 */       return jsonArray;
/*  84 */     } catch (Exception e) {
/*  85 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONArray toJSONArray(String enumClassPath) {
/*     */     try {
/* 101 */       return toJSONArray((Class)Class.forName(enumClassPath));
/* 102 */     } catch (ClassNotFoundException e) {
/* 103 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JSONObject toJSON(String enumClassPath) {
/*     */     try {
/* 109 */       return toJSON((Class)Class.forName(enumClassPath));
/* 110 */     } catch (ClassNotFoundException e) {
/* 111 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JSONObject toJSON(Enum<?> e) throws Exception {
/* 125 */     JSONObject jsonObject = new JSONObject();
/* 126 */     Field[] fields = e.getClass().getDeclaredFields();
/* 127 */     for (Field field : fields) {
/* 128 */       field.setAccessible(true);
/*     */       
/* 130 */       if (!"ENUM$VALUES".equals(field.getName()) && !field.getType().equals(e.getClass())) {
/*     */ 
/*     */         
/* 133 */         Object obj = field.get(e);
/* 134 */         if (obj instanceof Enum) {
/* 135 */           jsonObject.put(field.getName(), toJSON((Enum)field.get(e)));
/*     */         } else {
/* 137 */           jsonObject.put(field.getName(), field.get(e));
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     return jsonObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadEnumExtraData(Class<?> clazz, Consumer<EnumExtraData> consumer) {
/* 151 */     ApplicationContext applicationContext = AppUtil.getApplicaitonContext();
/* 152 */     if (applicationContext == null) {
/*     */       return;
/*     */     }
/* 155 */     Map<String, IEnumExtraDataLoader> beanMap = applicationContext.getBeansOfType(IEnumExtraDataLoader.class);
/* 156 */     if (beanMap != null && !beanMap.isEmpty()) {
/* 157 */       for (IEnumExtraDataLoader enumExtraDataLoader : beanMap.values()) {
/* 158 */         if (!clazz.equals(enumExtraDataLoader.tag())) {
/*     */           continue;
/*     */         }
/* 161 */         List<EnumExtraData> enumExtraDataList = enumExtraDataLoader.load();
/* 162 */         if (enumExtraDataList != null && !enumExtraDataList.isEmpty()) {
/* 163 */           for (EnumExtraData enumExtraData : enumExtraDataList) {
/* 164 */             consumer.accept(enumExtraData);
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 172 */     System.out.println(toJSON("com.dstz.bus.api.constant.BusinessObjectPersistenceType"));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/EnumUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */