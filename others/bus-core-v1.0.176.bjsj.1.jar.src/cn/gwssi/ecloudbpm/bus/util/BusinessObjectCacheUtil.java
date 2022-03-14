/*    */ package cn.gwssi.ecloudbpm.bus.util;
/*    */ 
/*    */ import com.dstz.base.core.cache.ICache;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ public class BusinessObjectCacheUtil
/*    */ {
/*    */   private static final String BUSINESS_OBJECT_DATASOURCE_KEYS_MAP = "businessObjectDataSourceKeysMap";
/*    */   
/*    */   public static void putDataSourcesKeys(String boKey, Set<String> dsKeys) {
/* 34 */     Map<String, Set<String>> map = (Map<String, Set<String>>)((ICache)AppUtil.getBean(ICache.class)).getByKey("businessObjectDataSourceKeysMap");
/* 35 */     if (map == null) {
/* 36 */       map = new HashMap<>();
/*    */     }
/* 38 */     map.put(boKey, dsKeys);
/* 39 */     ((ICache)AppUtil.getBean(ICache.class)).add("businessObjectDataSourceKeysMap", map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Set<String> getDataSourcesKeys(String boKey) {
/* 50 */     Map<String, Set<String>> map = (Map<String, Set<String>>)((ICache)AppUtil.getBean(ICache.class)).getByKey("businessObjectDataSourceKeysMap");
/* 51 */     if (map == null) {
/* 52 */       return null;
/*    */     }
/* 54 */     return map.get(boKey);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/util/BusinessObjectCacheUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */