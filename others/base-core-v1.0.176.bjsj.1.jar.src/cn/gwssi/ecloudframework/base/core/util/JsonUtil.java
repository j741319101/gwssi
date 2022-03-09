/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonUtil
/*     */ {
/*  18 */   protected static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JSONObject obj, String key, String defaultValue) {
/*  29 */     if (obj == null || !obj.containsKey(key)) return defaultValue; 
/*  30 */     return obj.getString(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JSONObject obj, String key) {
/*  41 */     return getString(obj, key, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JSONObject obj, String key) {
/*  52 */     if (obj == null || !obj.containsKey(key)) return 0; 
/*  53 */     return obj.getIntValue(key);
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
/*     */   public static int getInt(JSONObject obj, String key, int defaultValue) {
/*  65 */     if (obj == null || !obj.containsKey(key)) return defaultValue; 
/*  66 */     return obj.getIntValue(key);
/*     */   }
/*     */   
/*     */   public static boolean getBoolean(JSONObject obj, String key) {
/*  70 */     if (obj == null || !obj.containsKey(key)) return false; 
/*  71 */     return obj.getBoolean(key).booleanValue();
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
/*     */   public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
/*  83 */     if (obj == null || !obj.containsKey(key)) return defaultValue; 
/*  84 */     return obj.getBoolean(key).booleanValue();
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
/*     */   public static <T> T parseObject(String jsonStr, Class<T> cls) {
/*  99 */     if (StringUtil.isEmpty(jsonStr)) {
/* 100 */       return null;
/*     */     }
/* 102 */     return (T)JSON.parseObject(jsonStr, cls);
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
/*     */   public static <T> List<T> parseArray(String jsonStr, Class<T> cls) {
/* 115 */     if (StringUtil.isEmpty(jsonStr)) {
/* 116 */       return null;
/*     */     }
/* 118 */     return JSON.parseArray(jsonStr, cls);
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
/*     */   public static String toJSONString(Object obj) {
/* 130 */     if (obj == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return JSON.toJSONString(obj);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 137 */     String str = JSON.toJSONString(null);
/* 138 */     System.out.println();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/JsonUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */