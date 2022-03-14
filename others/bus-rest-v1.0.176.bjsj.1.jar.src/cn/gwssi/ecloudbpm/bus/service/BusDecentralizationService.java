/*    */ package cn.gwssi.ecloudbpm.bus.service;
/*    */ 
/*    */ import com.dstz.org.api.context.ICurrentContext;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.api.platform.ISysPropertiesPlatFormService;
/*    */ import javax.annotation.Resource;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ 
/*    */ @Service
/*    */ public class BusDecentralizationService
/*    */ {
/* 24 */   private Logger logger = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   @Resource
/*    */   ICurrentContext iCurrentContext;
/*    */ 
/*    */ 
/*    */   
/*    */   @Resource
/*    */   ISysPropertiesPlatFormService iSysPropertiesPlatFormService;
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean decentralizationEnable(String type) {
/* 39 */     IUser user = this.iCurrentContext.getCurrentUser();
/* 40 */     boolean ifDecentralization = false;
/*    */     
/* 42 */     if (null == user || !this.iCurrentContext.isAdmin(user)) {
/*    */       try {
/* 44 */         String decentralizationEnable = this.iSysPropertiesPlatFormService.getByAlias("decentralization.enable");
/* 45 */         if ("true".equals(decentralizationEnable)) {
/* 46 */           String decentralizationOrgEnable = this.iSysPropertiesPlatFormService.getByAlias("decentralization." + type + ".enable");
/* 47 */           if ("true".equals(decentralizationOrgEnable)) {
/* 48 */             ifDecentralization = true;
/*    */           }
/*    */         } 
/* 51 */       } catch (Exception e) {
/* 52 */         this.logger.error("获取系统属性失败", e);
/*    */       } 
/*    */     }
/* 55 */     return ifDecentralization;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusDecentralizationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */