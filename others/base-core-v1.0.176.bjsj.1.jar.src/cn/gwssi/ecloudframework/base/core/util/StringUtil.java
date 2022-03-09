/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import java.util.Collections;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class StringUtil
/*     */ {
/*     */   public static String join(Iterable<String> strs) {
/*  19 */     return join(strs, ",");
/*     */   }
/*     */   
/*     */   public static String join(Iterable<String> iterable, String split) {
/*  23 */     StringBuilder sb = new StringBuilder();
/*  24 */     for (String str : iterable) {
/*  25 */       sb.append(str);
/*  26 */       sb.append(split);
/*     */     } 
/*  28 */     return sb.substring(0, sb.length() - split.length());
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
/*     */   public static String upperFirst(String str) {
/*  41 */     return toFirst(str, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotEmpty(String str) {
/*  51 */     return !isEmpty(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(String str) {
/*  62 */     if (str == null) return true;
/*     */     
/*  64 */     if (str.trim().equals("")) return true;
/*     */     
/*  66 */     return false;
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
/*     */ 
/*     */   
/*     */   public static boolean isZeroEmpty(String tmp) {
/*  83 */     boolean isEmpty = isEmpty(tmp);
/*  84 */     if (isEmpty) return true;
/*     */     
/*  86 */     return "0".equals(tmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotZeroEmpty(String tmp) {
/*  96 */     return !isZeroEmpty(tmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String lowerFirst(String str) {
/* 106 */     return toFirst(str, false);
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
/*     */   public static String toFirst(String str, boolean isUpper) {
/* 119 */     if (StringUtils.isEmpty(str))
/* 120 */       return ""; 
/* 121 */     char first = str.charAt(0);
/* 122 */     String firstChar = new String(new char[] { first });
/* 123 */     firstChar = isUpper ? firstChar.toUpperCase() : firstChar.toLowerCase();
/* 124 */     return firstChar + str.substring(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertDbFieldToField(String dbField) {
/* 133 */     return convertDbFieldToField(dbField, "_", true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertDbFieldToField(String dbField, String symbol, boolean isIgnoreFirst) {
/* 143 */     StringBuilder result = new StringBuilder();
/* 144 */     if (dbField.startsWith(symbol)) {
/* 145 */       dbField = dbField.substring(1);
/*     */     }
/* 147 */     if (dbField.endsWith(symbol)) {
/* 148 */       dbField = dbField.substring(0, dbField.length() - 1);
/*     */     }
/* 150 */     String[] arr = dbField.split(symbol);
/* 151 */     for (int i = 0; i < arr.length; i++) {
/* 152 */       String str = arr[i];
/* 153 */       if (isIgnoreFirst && i != 0) {
/* 154 */         char oldChar = str.charAt(0);
/* 155 */         char newChar = (oldChar + "").toUpperCase().charAt(0);
/* 156 */         str = newChar + str.substring(1);
/*     */       } 
/* 158 */       result.append(str);
/*     */     } 
/* 160 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static String[] getStringAryByStr(String str) {
/* 164 */     if (isEmpty(str)) {
/* 165 */       Collections.emptyList();
/*     */     }
/*     */     
/* 168 */     return str.split(",");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(String[] vals, String separator) {
/* 179 */     if (ArrayUtil.isEmpty((Object[])vals))
/* 180 */       return ""; 
/* 181 */     String val = "";
/* 182 */     for (int i = 0; i < vals.length; i++) {
/* 183 */       if (i == 0) {
/* 184 */         val = val + vals[i];
/*     */       } else {
/* 186 */         val = val + separator + vals[i];
/*     */       } 
/*     */     } 
/* 189 */     return val;
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
/*     */   public static String trimSuffix(String content, String suffix) {
/* 202 */     String resultStr = content;
/* 203 */     while (resultStr.endsWith(suffix)) {
/* 204 */       resultStr = resultStr.substring(0, resultStr.length() - suffix.length());
/*     */     }
/* 206 */     return resultStr;
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
/*     */   public static String trimPrefix(String content, String prefix) {
/* 219 */     String resultStr = content;
/* 220 */     while (resultStr.startsWith(prefix)) {
/* 221 */       resultStr = resultStr.substring(prefix.length());
/*     */     }
/* 223 */     return resultStr;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */