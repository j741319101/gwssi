package com.dstz.bpm.plugin;

//import com.dstz.base.core.util.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.dstz.bpm.plugin.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Predicate;

public class PostCompile {
   private static String separator;

   public static void main(String[] args) throws Exception {
      File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
      System.out.println("处理 wf-core 包");
      String path = file.getAbsolutePath();
      String oldClass = path + "/wf-core/target/classes/cn";
      String newClass = path + "/wf-plugin-biz/target/classes/cn";
      String targetCoreClass = path + "/wf-plugin-biz/target/wf-core";
      File targetCoreClassf = new File(targetCoreClass);
      if (!targetCoreClassf.exists()) {
         targetCoreClassf.mkdirs();
      }

      FileUtil.moveTargetFolderFromNewResource(oldClass, newClass, targetCoreClass + "/cn");
      System.out.println("处理 wf-plugin 包");
      oldClass = path + "/wf-plugin/target/classes/cn";
      String targetPluginClass = path + "/wf-plugin-biz/target/wf-plugin";
      File targetPluginClassf = new File(targetPluginClass);
      if (!targetPluginClassf.exists()) {
         targetPluginClassf.mkdirs();
      }

      FileUtil.moveTargetFolderFromNewResource(oldClass, newClass, targetPluginClass + "/cn");
      File wfCoreDir = new File(path + "/wf-core/target");
      File wfCoreJar = (File)Arrays.asList(wfCoreDir.listFiles()).stream().filter((f) -> {
         return f.getName().endsWith("jar");
      }).findFirst().get();
      File copyWfCoreJar = new File(targetCoreClass + separator + wfCoreJar.getName());
      OutputStream out = new FileOutputStream(copyWfCoreJar);
      InputStream in = new FileInputStream(wfCoreJar);
      IoUtil.copy(in, out);
      out.flush();
      in.close();
      out.close();
      String[] cmd = new String[]{"cmd", "/c", "cd " + copyWfCoreJar.getParent() + " && jar uf " + copyWfCoreJar.getName() + " ." + separator + "cn"};
      Process process = Runtime.getRuntime().exec(cmd);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));

      String errorLine;
      while((errorLine = reader.readLine()) != null) {
         System.out.println(errorLine);
      }

      reader.close();
      process.getOutputStream().close();
      File wfPluginDir = new File(path + "/wf-plugin/target");
      File wfPluginJar = (File)Arrays.asList(wfPluginDir.listFiles()).stream().filter((f) -> {
         return f.getName().endsWith("jar");
      }).findFirst().get();
      File copyWfPluginJar = new File(targetPluginClass + separator + wfPluginJar.getName());
      out = new FileOutputStream(copyWfPluginJar);
      in = new FileInputStream(wfPluginJar);
      IoUtil.copy(in, out);
      out.flush();
      in.close();
      out.close();
      cmd = new String[]{"cmd", "/c", "cd " + copyWfPluginJar.getParent() + " && jar uf " + copyWfPluginJar.getName() + " ." + separator + "cn"};
      process = Runtime.getRuntime().exec(cmd);
      reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));

      while((errorLine = reader.readLine()) != null) {
         System.out.println(errorLine);
      }

      reader.close();
      process.getOutputStream().close();
      IoUtil.copy(new FileInputStream(wfPluginDir.getParent() + separator + "pom.xml"), new FileOutputStream(targetPluginClass + separator + "pom.xml"));
      IoUtil.copy(new FileInputStream(wfCoreDir.getParent() + separator + "pom.xml"), new FileOutputStream(targetCoreClass + separator + "pom.xml"));
   }

   static {
      separator = File.separator;
   }
}
