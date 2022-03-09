/*    */ package cn.gwssi.ecloudframework.sys.api.constant;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ 
/*    */ public enum EnvironmentConstant
/*    */ {
/*  7 */   DEV("DEV", ",开发-默认"),
/*  8 */   SIT("SIT", "测试"),
/*  9 */   UAT("UAT", "用户测试"),
/* 10 */   GRAY("GRAY", "灰度"),
/* 11 */   PROD("PROD", "生产");
/*    */   
/*    */   private String key;
/*    */   
/*    */   private String value;
/*    */   
/*    */   EnvironmentConstant(String key, String value) {
/* 18 */     this.key = key;
/* 19 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String key() {
/* 23 */     return this.key;
/*    */   }
/*    */   
/*    */   public String value() {
/* 27 */     return this.value;
/*    */   }
/*    */   
/*    */   public static String getKes() {
/* 31 */     StringBuilder sb = new StringBuilder();
/* 32 */     for (EnvironmentConstant e : values()) {
/* 33 */       sb.append("[").append(e.key).append("]");
/*    */     }
/* 35 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public static boolean contain(String key) {
/* 39 */     if (StringUtil.isEmpty(key)) return false;
/*    */     
/* 41 */     for (EnvironmentConstant e : values()) {
/* 42 */       if (key.equals(e.key)) return true; 
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/constant/EnvironmentConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */