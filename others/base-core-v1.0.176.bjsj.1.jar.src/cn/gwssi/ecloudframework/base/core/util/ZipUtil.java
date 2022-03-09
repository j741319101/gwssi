/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import net.lingala.zip4j.core.ZipFile;
/*     */ import net.lingala.zip4j.model.ZipParameters;
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
/*     */ public class ZipUtil
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   
/*     */   public static File zip(ArrayList<File> sourceFileList, ZipFile zipFile) {
/*     */     try {
/*  32 */       ZipParameters parameters = new ZipParameters();
/*  33 */       parameters.setCompressionMethod(8);
/*  34 */       parameters.setCompressionLevel(5);
/*  35 */       zipFile.createZipFile(sourceFileList, parameters);
/*  36 */       return zipFile.getFile();
/*  37 */     } catch (Exception e) {
/*  38 */       throw new RuntimeException(e);
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
/*     */   public static void unzip(String zipFilePath, String destDirectory) throws IOException {
/*  51 */     File destDir = new File(destDirectory);
/*  52 */     if (!destDir.exists()) {
/*  53 */       destDir.mkdir();
/*     */     }
/*  55 */     ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
/*  56 */     ZipEntry entry = zipIn.getNextEntry();
/*     */     
/*  58 */     while (entry != null) {
/*  59 */       String filePath = destDirectory + File.separator + entry.getName();
/*  60 */       if (!entry.isDirectory()) {
/*     */         
/*  62 */         extractFile(zipIn, filePath);
/*     */       } else {
/*     */         
/*  65 */         File dir = new File(filePath);
/*  66 */         dir.mkdir();
/*     */       } 
/*  68 */       zipIn.closeEntry();
/*  69 */       entry = zipIn.getNextEntry();
/*     */     } 
/*  71 */     zipIn.close();
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
/*     */   public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
/*  83 */     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
/*  84 */     byte[] bytesIn = new byte[4096];
/*  85 */     int read = 0;
/*  86 */     while ((read = zipIn.read(bytesIn)) != -1) {
/*  87 */       bos.write(bytesIn, 0, read);
/*     */     }
/*  89 */     bos.close();
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
/*     */   public static String createFilePath(String tempPath, String fileName) {
/* 103 */     File file = new File(tempPath);
/*     */     
/* 105 */     if (!file.exists())
/* 106 */       file.mkdirs(); 
/* 107 */     return file.getPath() + File.separator + fileName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ZipUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */