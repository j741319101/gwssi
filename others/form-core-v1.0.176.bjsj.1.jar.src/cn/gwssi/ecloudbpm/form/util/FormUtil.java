/*    */ package cn.gwssi.ecloudbpm.form.util;
/*    */ 
/*    */ import cn.hutool.core.util.ClassUtil;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormUtil
/*    */ {
/*    */   public static String getDesignTemplatePath() throws Exception {
/* 20 */     return ClassUtil.getClassPath() + "temp" + File.separator + "design" + File.separator;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/util/FormUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */