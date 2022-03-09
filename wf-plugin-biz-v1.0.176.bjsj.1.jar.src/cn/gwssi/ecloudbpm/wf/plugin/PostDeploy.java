/*    */ package cn.gwssi.ecloudbpm.wf.plugin;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class PostDeploy
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 11 */     File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
/* 12 */     System.out.println("deploy wf-core");
/* 13 */     String path = file.getAbsolutePath();
/* 14 */     File wfCoreDir = new File(path + "/wf-plugin-biz/target/wf-core");
/*    */     
/* 16 */     File wfCoreJar = Arrays.<File>asList(wfCoreDir.listFiles()).stream().filter(f -> f.getName().endsWith("jar")).findFirst().get();
/*    */     
/* 18 */     String version = wfCoreJar.getName().substring(8, wfCoreJar.getName().length() - 4);
/* 19 */     String cmd = "mvn deploy:deploy-file -DgroupId=cn.gwssi.ecloudbpm -DartifactId=%s -Dversion=%s -Dpackaging=jar  -Dfile=%s -Durl=http://61.135.24.220:44423/repository/%s/ -DrepositoryId=beacon -DgeneratePom=true -DpomFile=./pom.xml";
/*    */ 
/*    */     
/* 22 */     String releases = "maven-releases";
/* 23 */     if (version.equals("0.2-SNAPSHOT")) {
/* 24 */       releases = "maven-snapshots";
/*    */     }
/* 26 */     String coreCmd = String.format(cmd, new Object[] { "wf-core", version, wfCoreJar.getAbsolutePath(), releases });
/* 27 */     System.out.println(coreCmd);
/* 28 */     File wfPluginDir = new File(path + "/wf-plugin-biz/target/wf-core");
/*    */     
/* 30 */     File wfPluginJar = Arrays.<File>asList(wfPluginDir.listFiles()).stream().filter(f -> f.getName().endsWith("jar")).findFirst().get();
/* 31 */     String[] execCmd = { "cmd", "/c", " cd " + wfCoreDir.getAbsolutePath() + " && " + coreCmd };
/* 32 */     Process process = Runtime.getRuntime().exec(execCmd);
/* 33 */     BufferedReader outLine = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
/* 34 */     String line = "";
/* 35 */     while ((line = outLine.readLine()) != null) {
/* 36 */       System.out.println(line);
/*    */     }
/* 38 */     outLine.close();
/* 39 */     process.getOutputStream().close();
/*    */ 
/*    */     
/* 42 */     System.out.println("deploy wf-plugin");
/* 43 */     String pluginCmd = String.format(cmd, new Object[] { "wf-plugin", version, wfPluginJar.getAbsolutePath(), releases });
/* 44 */     System.out.println(pluginCmd);
/*    */     
/* 46 */     execCmd = new String[] { "cmd", "/c", "cd " + wfPluginDir.getAbsolutePath() + " &&  " + pluginCmd };
/* 47 */     process = Runtime.getRuntime().exec(execCmd);
/* 48 */     outLine = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
/* 49 */     while ((line = outLine.readLine()) != null) {
/* 50 */       System.out.println(line);
/*    */     }
/* 52 */     outLine.close();
/* 53 */     process.getOutputStream().close();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/PostDeploy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */