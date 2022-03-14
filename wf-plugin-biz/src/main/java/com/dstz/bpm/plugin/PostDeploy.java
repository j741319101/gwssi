package com.dstz.bpm.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Predicate;

public class PostDeploy {
   public static void main(String[] args) throws Exception {
      File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
      System.out.println("deploy wf-core");
      String path = file.getAbsolutePath();
      File wfCoreDir = new File(path + "/wf-plugin-biz/target/wf-core");
      File wfCoreJar = (File)Arrays.asList(wfCoreDir.listFiles()).stream().filter((f) -> {
         return f.getName().endsWith("jar");
      }).findFirst().get();
      String version = wfCoreJar.getName().substring(8, wfCoreJar.getName().length() - 4);
      String cmd = "mvn deploy:deploy-file -DgroupId=cn.gwssi.bpm -DartifactId=%s -Dversion=%s -Dpackaging=jar  -Dfile=%s -Durl=http://61.135.24.220:44423/repository/%s/ -DrepositoryId=beacon -DgeneratePom=true -DpomFile=./pom.xml";
      String releases = "maven-releases";
      if (version.equals("0.2-SNAPSHOT")) {
         releases = "maven-snapshots";
      }

      String coreCmd = String.format(cmd, "wf-core", version, wfCoreJar.getAbsolutePath(), releases);
      System.out.println(coreCmd);
      File wfPluginDir = new File(path + "/wf-plugin-biz/target/wf-core");
      File wfPluginJar = (File)Arrays.asList(wfPluginDir.listFiles()).stream().filter((f) -> {
         return f.getName().endsWith("jar");
      }).findFirst().get();
      String[] execCmd = new String[]{"cmd", "/c", " cd " + wfCoreDir.getAbsolutePath() + " && " + coreCmd};
      Process process = Runtime.getRuntime().exec(execCmd);
      BufferedReader outLine = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
      String line = "";

      while((line = outLine.readLine()) != null) {
         System.out.println(line);
      }

      outLine.close();
      process.getOutputStream().close();
      System.out.println("deploy wf-plugin");
      String pluginCmd = String.format(cmd, "wf-plugin", version, wfPluginJar.getAbsolutePath(), releases);
      System.out.println(pluginCmd);
      execCmd = new String[]{"cmd", "/c", "cd " + wfPluginDir.getAbsolutePath() + " &&  " + pluginCmd};
      process = Runtime.getRuntime().exec(execCmd);
      outLine = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

      while((line = outLine.readLine()) != null) {
         System.out.println(line);
      }

      outLine.close();
      process.getOutputStream().close();
   }
}
