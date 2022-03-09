/*    */ package cn.gwssi.ecloudframework.base.core.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.cglib.beans.BeanCopier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanCopierUtils
/*    */ {
/* 17 */   protected static final Logger LOG = LoggerFactory.getLogger(AppUtil.class);
/*    */   
/* 19 */   public static Map<String, BeanCopier> beanCopierMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public static void copyProperties(Object source, Object target) {
/* 23 */     String beanKey = generateKey(source.getClass(), target.getClass());
/* 24 */     BeanCopier copier = null;
/* 25 */     if (!beanCopierMap.containsKey(beanKey)) {
/* 26 */       copier = BeanCopier.create(source.getClass(), target.getClass(), false);
/* 27 */       beanCopierMap.put(beanKey, copier);
/*    */     } else {
/* 29 */       copier = beanCopierMap.get(beanKey);
/*    */     } 
/* 31 */     copier.copy(source, target, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> T transformBean(Object source, Class<T> clazz) {
/* 41 */     if (source == null) return null;
/*    */     
/*    */     try {
/* 44 */       T target = clazz.newInstance();
/*    */       
/* 46 */       String beanKey = generateKey(source.getClass(), clazz);
/*    */       
/* 48 */       BeanCopier copier = null;
/* 49 */       if (!beanCopierMap.containsKey(beanKey)) {
/* 50 */         copier = BeanCopier.create(source.getClass(), target.getClass(), false);
/* 51 */         beanCopierMap.put(beanKey, copier);
/*    */       } else {
/* 53 */         copier = beanCopierMap.get(beanKey);
/*    */       } 
/*    */       
/* 56 */       copier.copy(source, target, null);
/* 57 */       return target;
/* 58 */     } catch (InstantiationException|IllegalAccessException e) {
/* 59 */       LOG.warn("类转换出错", e);
/*    */       
/* 61 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> List<T> transformList(List sourceList, Class<T> clazz) {
/* 72 */     List<T> list = new ArrayList<>();
/*    */     
/* 74 */     for (Object source : sourceList) {
/* 75 */       T target = transformBean(source, clazz);
/*    */       
/* 77 */       if (target != null) list.add(target);
/*    */     
/*    */     } 
/*    */     
/* 81 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   private static String generateKey(Class<?> class1, Class<?> class2) {
/* 86 */     return class1.toString() + class2.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/BeanCopierUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */