/*    */ package cn.gwssi.ecloudframework.sys.service.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.api.constant.RightsObjectConstants;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.SysAuthorizationService;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysAuthorizationManager;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class DefaultSysAuthorizationService
/*    */   implements SysAuthorizationService
/*    */ {
/*    */   @Resource
/*    */   SysAuthorizationManager sysAuthorizationManager;
/*    */   
/*    */   public Set<String> getUserRights(String userId) {
/* 20 */     return this.sysAuthorizationManager.getUserRights(userId);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey) {
/* 25 */     return this.sysAuthorizationManager.getUserRightsSql(rightsObject, userId, targetKey);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DefaultSysAuthorizationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */