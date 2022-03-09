/*    */ package cn.gwssi.ecloudframework.sys.util;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.PropertyService;
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
/*    */ public class SysPropertyUtil
/*    */ {
/*    */   public static String getByAlias(String alias) {
/* 20 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 21 */     return service.getByAlias(alias);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getByAlias(String alias, String defaultValue) {
/* 33 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 34 */     if (service == null) return defaultValue; 
/* 35 */     return service.getByAlias(alias, defaultValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Integer getIntByAlias(String alias) {
/* 46 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 47 */     return service.getIntByAlias(alias);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Integer getIntByAlias(String alias, Integer defaulValue) {
/* 59 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 60 */     return service.getIntByAlias(alias, defaulValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Long getLongByAlias(String alias) {
/* 70 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 71 */     return service.getLongByAlias(alias);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean getBooleanByAlias(String alias) {
/* 82 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 83 */     return service.getBooleanByAlias(alias);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean getBooleanByAlias(String alias, boolean defaulValue) {
/* 95 */     PropertyService service = (PropertyService)AppUtil.getBean(PropertyService.class);
/* 96 */     return service.getBooleanByAlias(alias, defaulValue);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/SysPropertyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */