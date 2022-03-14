/*     */ package com.dstz.bpm.api.util;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import sun.reflect.ConstructorAccessor;
/*     */ import sun.reflect.FieldAccessor;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicEnumUtil
/*     */ {
/*  17 */   private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException, IllegalAccessException {
/*  23 */     field.setAccessible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  28 */     Field modifiersField = Field.class.getDeclaredField("modifiers");
/*  29 */     modifiersField.setAccessible(true);
/*  30 */     int modifiers = modifiersField.getInt(field);
/*     */ 
/*     */     
/*  33 */     modifiers &= 0xFFFFFFEF;
/*  34 */     modifiersField.setInt(field, modifiers);
/*     */     
/*  36 */     FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
/*  37 */     fa.set(target, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
/*  42 */     for (Field field : Class.class.getDeclaredFields()) {
/*  43 */       if (field.getName().contains(fieldName)) {
/*  44 */         AccessibleObject.setAccessible((AccessibleObject[])new Field[] { field }, true);
/*  45 */         setFailsafeFieldValue(field, enumClass, null);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
/*  52 */     blankField(enumClass, "enumConstantDirectory");
/*  53 */     blankField(enumClass, "enumConstants");
/*     */   }
/*     */ 
/*     */   
/*     */   private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws NoSuchMethodException {
/*  58 */     Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
/*  59 */     parameterTypes[0] = String.class;
/*  60 */     parameterTypes[1] = int.class;
/*  61 */     System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
/*  62 */     return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception {
/*  67 */     Object[] parms = new Object[additionalValues.length + 2];
/*  68 */     parms[0] = value;
/*  69 */     parms[1] = Integer.valueOf(ordinal);
/*  70 */     System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
/*     */     
/*  72 */     return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
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
/*     */   public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] additionalTypes, Object[] additionalValues) {
/*  86 */     if (!Enum.class.isAssignableFrom(enumType)) {
/*  87 */       throw new RuntimeException("class " + enumType + " is not an instance of Enum");
/*     */     }
/*     */ 
/*     */     
/*  91 */     Field valuesField = null;
/*  92 */     Field[] fields = enumType.getDeclaredFields();
/*  93 */     for (Field field : fields) {
/*  94 */       if (field.getName().contains("$VALUES")) {
/*  95 */         valuesField = field;
/*     */         break;
/*     */       } 
/*     */     } 
/*  99 */     AccessibleObject.setAccessible((AccessibleObject[])new Field[] { valuesField }, true);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 104 */       Enum[] arrayOfEnum = (Enum[])valuesField.get(enumType);
/* 105 */       List<T> values = new ArrayList<>(Arrays.asList((T[])arrayOfEnum));
/*     */ 
/*     */       
/* 108 */       Enum enum_ = (Enum)makeEnum(enumType, enumName, values.size(), additionalTypes, additionalValues);
/*     */ 
/*     */       
/* 111 */       values.add((T)enum_);
/*     */ 
/*     */       
/* 114 */       setFailsafeFieldValue(valuesField, null, values.toArray((Enum[])Array.newInstance(enumType, 0)));
/*     */ 
/*     */       
/* 117 */       cleanEnumCache(enumType);
/* 118 */       return (T)enum_;
/* 119 */     } catch (Exception e) {
/* 120 */       e.printStackTrace();
/* 121 */       throw new RuntimeException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/util/DynamicEnumUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */