/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.spring.IProperty;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyUtil
/*     */ {
/*  22 */   private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");
/*     */ 
/*     */   
/*  25 */   private static final Pattern SEPARATED_TO_CAMEL_CASE_PATTERN = Pattern.compile("[_\\-.]");
/*     */ 
/*     */   
/*     */   private static final String DONT = ".";
/*     */   
/*  30 */   private static IProperty propertyHolder = null;
/*     */   
/*  32 */   private static Environment environment = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getProperty(String property, String defaultValue) {
/*  42 */     if (propertyHolder == null) {
/*  43 */       getEnvironment();
/*     */     }
/*     */     
/*  46 */     String v = propertyHolder.getValue(property);
/*  47 */     if (StringUtil.isEmpty(v)) {
/*  48 */       return defaultValue;
/*     */     }
/*     */     
/*  51 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   private static synchronized void getEnvironment() {
/*  56 */     propertyHolder = (IProperty)AppUtil.getBean("custPlaceHolder");
/*     */     
/*  58 */     if (propertyHolder == null) {
/*  59 */       environment = AppUtil.<Environment>getBean(Environment.class);
/*  60 */       if (environment == null) {
/*  61 */         throw new BusinessException("Environment cannot be found");
/*     */       }
/*     */       
/*  64 */       propertyHolder = new IProperty()
/*     */         {
/*     */           public String getValue(String key) {
/*  67 */             return PropertyUtil.environment.getProperty(key);
/*     */           }
/*     */         };
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
/*     */   public static String getProperty(String property) {
/*  81 */     return getProperty(property, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Integer getIntProperty(String property) {
/*  91 */     String str = getProperty(property, "");
/*  92 */     if (StringUtil.isEmpty(str)) {
/*  93 */       return Integer.valueOf(0);
/*     */     }
/*  95 */     return Integer.valueOf(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Integer getIntProperty(String property, Integer defaultValue) {
/* 106 */     String str = getProperty(property, "");
/* 107 */     if (StringUtil.isEmpty(str)) {
/* 108 */       return defaultValue;
/*     */     }
/* 110 */     return Integer.valueOf(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolProperty(String property) {
/* 121 */     String str = getProperty(property, "");
/* 122 */     if (StringUtil.isEmpty(str)) {
/* 123 */       return false;
/*     */     }
/* 125 */     return str.equalsIgnoreCase("true");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJdbcType() {
/* 134 */     String str = getProperty("jdbc.dbType");
/* 135 */     if (StringUtil.isEmpty(str)) {
/* 136 */       str = getProperty("spring.datasource.dbType");
/*     */     }
/*     */     
/* 139 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFormDefBackupPath() {
/* 150 */     return getProperty("formDefBackupPath");
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
/*     */   public static String getRelaxStringValue(Environment environment, String key) {
/* 166 */     String value = environment.getProperty(key);
/* 167 */     if (StringUtils.isEmpty(value)) {
/* 168 */       int dontIndex = StringUtils.lastIndexOf(key, ".");
/* 169 */       String prefix = "";
/* 170 */       String name = key;
/* 171 */       if (dontIndex != -1) {
/* 172 */         prefix = StringUtils.substring(key, 0, dontIndex);
/* 173 */         name = StringUtils.substring(key, dontIndex + 1);
/*     */       } 
/* 175 */       Set<String> names = new HashSet<>();
/* 176 */       addRelaxNames(name, names);
/* 177 */       for (String searchName : names) {
/* 178 */         value = environment.getProperty(StringUtils.join((Object[])new String[] { prefix, ".", searchName }));
/* 179 */         if (StringUtils.isNotEmpty(value)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 184 */     return value;
/*     */   }
/*     */   
/*     */   private static void addRelaxNames(String name, Set<String> values) {
/* 188 */     if (values.contains(name)) {
/*     */       return;
/*     */     }
/* 191 */     for (Variation variation : Variation.values()) {
/* 192 */       for (Manipulation manipulation : Manipulation.values()) {
/* 193 */         String result = name;
/* 194 */         result = variation.apply(result);
/* 195 */         result = manipulation.apply(result);
/* 196 */         values.add(result);
/* 197 */         addRelaxNames(result, values);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum Variation
/*     */   {
/* 208 */     NONE
/*     */     {
/*     */       public String apply(String value) {
/* 211 */         return value;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 216 */     LOWERCASE
/*     */     {
/*     */       public String apply(String value) {
/* 219 */         return value.isEmpty() ? value : value.toLowerCase(Locale.ENGLISH);
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 224 */     UPPERCASE
/*     */     {
/*     */       public String apply(String value) {
/* 227 */         return value.isEmpty() ? value : value.toUpperCase(Locale.ENGLISH);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract String apply(String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum Manipulation
/*     */   {
/* 241 */     NONE
/*     */     {
/*     */       public String apply(String value) {
/* 244 */         return value;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 249 */     HYPHEN_TO_UNDERSCORE
/*     */     {
/*     */       public String apply(String value) {
/* 252 */         return (value.indexOf('-') != -1) ? value.replace('-', '_') : value;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 257 */     UNDERSCORE_TO_PERIOD
/*     */     {
/*     */       public String apply(String value) {
/* 260 */         return (value.indexOf('_') != -1) ? value.replace('_', '.') : value;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 265 */     PERIOD_TO_UNDERSCORE
/*     */     {
/*     */       public String apply(String value) {
/* 268 */         return (value.indexOf('.') != -1) ? value.replace('.', '_') : value;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 273 */     CAMELCASE_TO_UNDERSCORE
/*     */     {
/*     */       public String apply(String value) {
/* 276 */         if (value.isEmpty()) {
/* 277 */           return value;
/*     */         }
/* 279 */         Matcher matcher = PropertyUtil.CAMEL_CASE_PATTERN.matcher(value);
/* 280 */         if (!matcher.find()) {
/* 281 */           return value;
/*     */         }
/* 283 */         matcher = matcher.reset();
/* 284 */         StringBuffer result = new StringBuffer();
/* 285 */         while (matcher.find()) {
/* 286 */           matcher.appendReplacement(result, matcher.group(1) + '_' + 
/* 287 */               StringUtils.uncapitalize(matcher.group(2)));
/*     */         }
/* 289 */         matcher.appendTail(result);
/* 290 */         return result.toString();
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 295 */     CAMELCASE_TO_HYPHEN
/*     */     {
/*     */       public String apply(String value) {
/* 298 */         if (value.isEmpty()) {
/* 299 */           return value;
/*     */         }
/* 301 */         Matcher matcher = PropertyUtil.CAMEL_CASE_PATTERN.matcher(value);
/* 302 */         if (!matcher.find()) {
/* 303 */           return value;
/*     */         }
/* 305 */         matcher = matcher.reset();
/* 306 */         StringBuffer result = new StringBuffer();
/* 307 */         while (matcher.find()) {
/* 308 */           matcher.appendReplacement(result, matcher.group(1) + '-' + 
/* 309 */               StringUtils.uncapitalize(matcher.group(2)));
/*     */         }
/* 311 */         matcher.appendTail(result);
/* 312 */         return result.toString();
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 317 */     SEPARATED_TO_CAMELCASE
/*     */     {
/*     */       public String apply(String value) {
/* 320 */         return separatedToCamelCase(value, false);
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 325 */     CASE_INSENSITIVE_SEPARATED_TO_CAMELCASE
/*     */     {
/*     */       public String apply(String value) {
/* 328 */         return separatedToCamelCase(value, true);
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 333 */     UNDERSCORE_TO_HYPHEN
/*     */     {
/*     */       public String apply(String value) {
/* 336 */         return (value.indexOf('_') != -1) ? value.replace("_", "-") : value;
/*     */       }
/*     */     };
/*     */     
/* 340 */     private static final char[] SUFFIXES = new char[] { '_', '-', '.' };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static String separatedToCamelCase(String value, boolean caseInsensitive) {
/* 346 */       if (value.isEmpty()) {
/* 347 */         return value;
/*     */       }
/* 349 */       StringBuilder builder = new StringBuilder();
/* 350 */       for (String field : PropertyUtil.SEPARATED_TO_CAMEL_CASE_PATTERN.split(value)) {
/* 351 */         field = caseInsensitive ? field.toLowerCase(Locale.ENGLISH) : field;
/* 352 */         builder.append(
/* 353 */             (builder.length() != 0) ? StringUtils.capitalize(field) : field);
/*     */       } 
/* 355 */       char lastChar = value.charAt(value.length() - 1);
/* 356 */       for (char suffix : SUFFIXES) {
/* 357 */         if (lastChar == suffix) {
/* 358 */           builder.append(suffix);
/*     */           break;
/*     */         } 
/*     */       } 
/* 362 */       return builder.toString();
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public abstract String apply(String param1String);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/PropertyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */