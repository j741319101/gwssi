/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.SystemPropertyUtils;
/*     */ 
/*     */ public class PackageTools
/*     */ {
/*  24 */   private static final Log log = LogFactory.getLog(PackageTools.class);
/*     */ 
/*     */   
/*     */   protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
/*     */ 
/*     */   
/*     */   public static Set<Method> findClassAnnotationMethods(String scanPackages, Class<? extends Annotation> annotation) {
/*  31 */     Set<String> clazzSet = findPackageClass(scanPackages);
/*  32 */     Set<Method> methods = new HashSet<>();
/*  33 */     Iterator<String> var4 = clazzSet.iterator();
/*     */     
/*  35 */     while (var4.hasNext()) {
/*  36 */       String clazz = var4.next();
/*     */       
/*     */       try {
/*  39 */         Set<Method> ms = findAnnotationMethods(clazz, annotation);
/*  40 */         if (ms != null) {
/*  41 */           methods.addAll(ms);
/*     */         }
/*  43 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  48 */     return methods;
/*     */   }
/*     */   
/*     */   public static Set<String> findPackageClass(String scanPackages) {
/*  52 */     if (StringUtils.isBlank(scanPackages)) {
/*  53 */       return Collections.EMPTY_SET;
/*     */     }
/*  55 */     Set<String> packages = checkPackage(scanPackages);
/*  56 */     PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
/*  57 */     CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory((ResourceLoader)pathMatchingResourcePatternResolver);
/*  58 */     Set<String> clazzSet = new HashSet<>();
/*  59 */     Iterator<String> var5 = packages.iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  64 */       if (!var5.hasNext()) {
/*  65 */         return clazzSet;
/*     */       }
/*     */       
/*  68 */       String basePackage = var5.next();
/*  69 */       if (!StringUtils.isBlank(basePackage)) {
/*     */         
/*  71 */         String packageSearchPath = "classpath*:" + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/**/*.class";
/*     */         
/*     */         try {
/*  74 */           Resource[] resources = pathMatchingResourcePatternResolver.getResources(packageSearchPath);
/*  75 */           Resource[] var9 = resources;
/*  76 */           int var10 = resources.length;
/*     */           
/*  78 */           for (int var11 = 0; var11 < var10; var11++) {
/*  79 */             Resource resource = var9[var11];
/*  80 */             String clazz = loadClassName((MetadataReaderFactory)cachingMetadataReaderFactory, resource);
/*  81 */             clazzSet.add(clazz);
/*     */           } 
/*  83 */         } catch (Exception var14) {
/*  84 */           log.error("获取包下面的类信息失败,package:" + basePackage, var14);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Set<String> checkPackage(String scanPackages) {
/*  91 */     if (StringUtils.isBlank(scanPackages)) {
/*  92 */       return Collections.EMPTY_SET;
/*     */     }
/*  94 */     Set<String> packages = new HashSet<>();
/*  95 */     Collections.addAll(packages, scanPackages.split(","));
/*  96 */     String[] var2 = packages.<String>toArray(new String[packages.size()]);
/*  97 */     int var3 = var2.length;
/*     */     
/*  99 */     for (int var4 = 0; var4 < var3; var4++) {
/* 100 */       String pInArr = var2[var4];
/* 101 */       if (!StringUtils.isBlank(pInArr) && !pInArr.equals(".") && !pInArr.startsWith(".")) {
/* 102 */         if (pInArr.endsWith(".")) {
/* 103 */           pInArr = pInArr.substring(0, pInArr.length() - 1);
/*     */         }
/*     */         
/* 106 */         Iterator<String> packageIte = packages.iterator();
/* 107 */         boolean needAdd = true;
/*     */         
/* 109 */         while (packageIte.hasNext()) {
/* 110 */           String pack = packageIte.next();
/* 111 */           if (pInArr.startsWith(pack + ".")) {
/* 112 */             needAdd = false; continue;
/* 113 */           }  if (pack.startsWith(pInArr + ".")) {
/* 114 */             packageIte.remove();
/*     */           }
/*     */         } 
/*     */         
/* 118 */         if (needAdd) {
/* 119 */           packages.add(pInArr);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 124 */     return packages;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException {
/*     */     try {
/* 130 */       if (resource.isReadable()) {
/* 131 */         MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
/* 132 */         if (metadataReader != null) {
/* 133 */           return metadataReader.getClassMetadata().getClassName();
/*     */         }
/*     */       } 
/* 136 */     } catch (Exception var3) {
/* 137 */       log.error("根据resource获取类名称失败", var3);
/*     */     } 
/*     */     
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public static Set<Method> findAnnotationMethods(String fullClassName, Class<? extends Annotation> anno) throws ClassNotFoundException {
/* 144 */     Set<Method> methodSet = new HashSet<>();
/* 145 */     Class<?> clz = Class.forName(fullClassName);
/* 146 */     Method[] methods = clz.getDeclaredMethods();
/* 147 */     Method[] var5 = methods;
/* 148 */     int var6 = methods.length;
/*     */     
/* 150 */     for (int var7 = 0; var7 < var6; var7++) {
/* 151 */       Method method = var5[var7];
/* 152 */       if (method.getModifiers() == 1) {
/* 153 */         Annotation annotation = method.getAnnotation((Class)anno);
/* 154 */         if (annotation != null) {
/* 155 */           methodSet.add(method);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     return methodSet;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/PackageTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */