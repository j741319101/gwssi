/*    */ package com.dstz.org.sync.service;
/*    */ 
/*    */ import com.dstz.base.api.aop.annotion.CatchErr;
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.rest.ControllerTools;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/org/Sync"})
/*    */ public class OrgSyncController
/*    */   extends ControllerTools
/*    */ {
/*    */   @Resource
/*    */   OrgSyncService orgSyncService;
/*    */   
/*    */   @RequestMapping({"fullOrgSync"})
/*    */   @CatchErr("同步用户失败")
/*    */   public ResultMsg<String> fullOrgSync() throws Exception {
/* 24 */     this.orgSyncService.fullSyncOrg();
/*    */     
/* 26 */     return getSuccessResult("同步成功");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/sync/service/OrgSyncController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */