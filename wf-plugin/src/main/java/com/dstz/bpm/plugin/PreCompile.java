package com.dstz.bpm.plugin;

import com.dstz.base.core.util.FileUtil;

import java.io.File;

public class PreCompile {
   public static void main(String[] args) throws Exception {
      File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
      System.out.println("复制 wf-core 包");
      String path = file.getAbsolutePath();
      String resource = path + "/wf-core/target/classes_proguard_base/cn";
      String target = path + "/wf-plugin-biz/target/classes/cn";
      FileUtil.copyFolder(resource, target);
      System.out.println("复制 wf-plugin 包");
      resource = path + "/wf-plugin/target/classes_proguard_base/cn";
      FileUtil.copyFolder(resource, target);
   }
}
