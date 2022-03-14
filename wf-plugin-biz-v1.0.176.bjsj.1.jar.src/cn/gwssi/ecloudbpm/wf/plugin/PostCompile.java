/*    */ package com.dstz.bpm.plugin;
/*    */ import cn.hutool.core.io.IoUtil;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class PostCompile {
/* 10 */   private static String separator = File.separator;
/*    */ 
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 14 */     File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
/* 15 */     System.out.println("处理 wf-core 包");
/* 16 */     String path = file.getAbsolutePath();
/* 17 */     String oldClass = path + "/wf-core/target/classes/cn";
/* 18 */     String newClass = path + "/wf-plugin-biz/target/classes/cn";
/* 19 */     String targetCoreClass = path + "/wf-plugin-biz/target/wf-core";
/* 20 */     File targetCoreClassf = new File(targetCoreClass);
/* 21 */     if (!targetCoreClassf.exists()) {
/* 22 */       targetCoreClassf.mkdirs();
/*    */     }
/* 24 */     FileUtil.moveTargetFolderFromNewResource(oldClass, newClass, targetCoreClass + "/cn");
/* 25 */     System.out.println("处理 wf-plugin 包");
/* 26 */     oldClass = path + "/wf-plugin/target/classes/cn";
/* 27 */     String targetPluginClass = path + "/wf-plugin-biz/target/wf-plugin";
/* 28 */     File targetPluginClassf = new File(targetPluginClass);
/* 29 */     if (!targetPluginClassf.exists()) {
/* 30 */       targetPluginClassf.mkdirs();
/*    */     }
/* 32 */     FileUtil.moveTargetFolderFromNewResource(oldClass, newClass, targetPluginClass + "/cn");
/*    */     
/* 34 */     File wfCoreDir = new File(path + "/wf-core/target");
/*    */     
/* 36 */     File wfCoreJar = Arrays.<File>asList(wfCoreDir.listFiles()).stream().filter(f -> f.getName().endsWith("jar")).findFirst().get();
/* 37 */     File copyWfCoreJar = new File(targetCoreClass + separator + wfCoreJar.getName());
/* 38 */     OutputStream out = new FileOutputStream(copyWfCoreJar);
/* 39 */     InputStream in = new FileInputStream(wfCoreJar);
/* 40 */     IoUtil.copy(in, out);
/* 41 */     out.flush();
/* 42 */     in.close();
/* 43 */     out.close();
/*    */     
/* 45 */     String[] cmd = { "cmd", "/c", "cd " + copyWfCoreJar.getParent() + " && jar uf " + copyWfCoreJar.getName() + " ." + separator + "cn" };
/* 46 */     Process process = Runtime.getRuntime().exec(cmd);
/* 47 */     BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
/*    */     String errorLine;
/* 49 */     while ((errorLine = reader.readLine()) != null) {
/* 50 */       System.out.println(errorLine);
/*    */     }
/* 52 */     reader.close();
/* 53 */     process.getOutputStream().close();
/*    */     
/* 55 */     File wfPluginDir = new File(path + "/wf-plugin/target");
/*    */     
/* 57 */     File wfPluginJar = Arrays.<File>asList(wfPluginDir.listFiles()).stream().filter(f -> f.getName().endsWith("jar")).findFirst().get();
/* 58 */     File copyWfPluginJar = new File(targetPluginClass + separator + wfPluginJar.getName());
/* 59 */     out = new FileOutputStream(copyWfPluginJar);
/* 60 */     in = new FileInputStream(wfPluginJar);
/* 61 */     IoUtil.copy(in, out);
/* 62 */     out.flush();
/* 63 */     in.close();
/* 64 */     out.close();
/* 65 */     cmd = new String[] { "cmd", "/c", "cd " + copyWfPluginJar.getParent() + " && jar uf " + copyWfPluginJar.getName() + " ." + separator + "cn" };
/* 66 */     process = Runtime.getRuntime().exec(cmd);
/* 67 */     reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
/* 68 */     while ((errorLine = reader.readLine()) != null) {
/* 69 */       System.out.println(errorLine);
/*    */     }
/* 71 */     reader.close();
/* 72 */     process.getOutputStream().close();
/* 73 */     IoUtil.copy(new FileInputStream(wfPluginDir.getParent() + separator + "pom.xml"), new FileOutputStream(targetPluginClass + separator + "pom.xml"));
/*    */     
/* 75 */     IoUtil.copy(new FileInputStream(wfCoreDir.getParent() + separator + "pom.xml"), new FileOutputStream(targetCoreClass + separator + "pom.xml"));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/PostCompile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */