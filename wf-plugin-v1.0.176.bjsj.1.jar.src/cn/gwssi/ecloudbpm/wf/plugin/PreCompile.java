/*    */ package com.dstz.bpm.plugin;
/*    */ 
/*    */ import com.dstz.base.core.util.FileUtil;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreCompile
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 13 */     File file = (new File(PreCompile.class.getResource("").getPath())).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
/* 14 */     System.out.println("复制 wf-core 包");
/* 15 */     String path = file.getAbsolutePath();
/* 16 */     String resource = path + "/wf-core/target/classes_proguard_base/cn";
/* 17 */     String target = path + "/wf-plugin-biz/target/classes/cn";
/* 18 */     FileUtil.copyFolder(resource, target);
/* 19 */     System.out.println("复制 wf-plugin 包");
/* 20 */     resource = path + "/wf-plugin/target/classes_proguard_base/cn";
/* 21 */     FileUtil.copyFolder(resource, target);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/PreCompile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */