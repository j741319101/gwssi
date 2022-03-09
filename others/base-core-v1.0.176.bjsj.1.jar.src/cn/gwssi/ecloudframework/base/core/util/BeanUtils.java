/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.Tree;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BeanUtils
/*     */ {
/*     */   public static <T extends Tree> List<T> listToTree(List<T> list) {
/*  25 */     Map<String, Tree> tempMap = new LinkedHashMap<>();
/*  26 */     if (CollectionUtil.isEmpty(list)) return Collections.emptyList(); 
/*  27 */     if (!(list.get(0) instanceof Tree)) {
/*  28 */       throw new RuntimeException("树形转换出现异常。数据必须实现Tree接口！");
/*     */     }
/*     */     
/*  31 */     List<T> returnList = new ArrayList<>();
/*  32 */     for (Tree tree : list) {
/*  33 */       tempMap.put(tree.getId(), tree);
/*     */     }
/*     */     
/*  36 */     for (Tree obj : list) {
/*  37 */       String parentId = obj.getParentId();
/*  38 */       if (tempMap.containsKey(parentId) && !obj.getId().equals(parentId)) {
/*  39 */         if (((Tree)tempMap.get(parentId)).getChildren() == null) {
/*  40 */           ((Tree)tempMap.get(parentId)).setChildren(new ArrayList());
/*     */         }
/*  42 */         ((Tree)tempMap.get(parentId)).getChildren().add(obj); continue;
/*     */       } 
/*  44 */       returnList.add((T)obj);
/*     */     } 
/*     */ 
/*     */     
/*  48 */     return returnList;
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
/*     */   
/*     */   public static Object getValue(String columnType, QueryOP queryOP, String valStr) {
/*  63 */     Object value = null;
/*  64 */     if ("varchar".equals(columnType)) {
/*  65 */       value = valStr;
/*  66 */     } else if ("number".equals(columnType)) {
/*  67 */       value = Double.valueOf(Double.parseDouble(valStr));
/*  68 */     } else if ("date".equals(columnType)) {
/*     */       try {
/*  70 */         value = DateUtil.parse(valStr);
/*  71 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/*  75 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getValue(String typeName, String valStr) {
/*  86 */     Object o = null;
/*  87 */     if (typeName.equals("int")) {
/*  88 */       o = Integer.valueOf(Integer.parseInt(valStr));
/*  89 */     } else if (typeName.equals("short")) {
/*  90 */       o = Short.valueOf(Short.parseShort(valStr));
/*  91 */     } else if (typeName.equals("long")) {
/*  92 */       o = Long.valueOf(Long.parseLong(valStr));
/*  93 */     } else if (typeName.equals("float")) {
/*  94 */       o = Float.valueOf(Float.parseFloat(valStr));
/*  95 */     } else if (typeName.equals("double")) {
/*  96 */       o = Double.valueOf(Double.parseDouble(valStr));
/*  97 */     } else if (typeName.equals("boolean")) {
/*  98 */       o = Boolean.valueOf(Boolean.parseBoolean(valStr));
/*  99 */     } else if (typeName.equals("java.lang.String")) {
/* 100 */       o = valStr;
/*     */     } else {
/* 102 */       o = valStr;
/*     */     } 
/* 104 */     return o;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/BeanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */