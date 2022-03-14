/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.sys.api.model.ISysDataSource;
/*    */ import com.dstz.sys.api.service.ISysDataSourceService;
/*    */ import com.dstz.sys.core.manager.SysDataSourceManager;
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.stereotype.Service;
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
/*    */ @Service
/*    */ public class SysDataSourceService
/*    */   implements ISysDataSourceService
/*    */ {
/*    */   @Autowired
/*    */   SysDataSourceManager sysDataSourceManager;
/*    */   
/*    */   public ISysDataSource getByKey(String key) {
/* 28 */     return (ISysDataSource)this.sysDataSourceManager.getByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public DataSource getDataSourceByKey(String key) {
/* 33 */     return this.sysDataSourceManager.getDataSourceByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public JdbcTemplate getJdbcTemplateByKey(String key) {
/* 38 */     return this.sysDataSourceManager.getJdbcTemplateByKey(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/SysDataSourceService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */