/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.dstz.base.rest.BaseController;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
/*    */ import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
/*    */ import com.github.pagehelper.Page;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/bpm/userAgencyLog"})
/*    */ public class BpmUserAgencyLogController
/*    */   extends BaseController<BpmUserAgencyLog>
/*    */ {
/*    */   @Autowired
/*    */   private BpmUserAgencyLogManager bpmUserAgencyLogManager;
/*    */   
/*    */   protected String getModelDesc() {
/* 31 */     return "业务流程用户代理日志";
/*    */   }
/*    */ 
/*    */   
/*    */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 36 */     QueryFilter queryFilter = getQueryFilter(request);
/* 37 */     String tab = request.getParameter("tab");
/* 38 */     Page<BpmUserAgencyLogVO> pageList = null;
/* 39 */     if ("target".equals(tab)) {
/* 40 */       pageList = (Page<BpmUserAgencyLogVO>)this.bpmUserAgencyLogManager.selectBpmTargetUserAgencyLogList(queryFilter);
/*    */     } else {
/* 42 */       pageList = (Page<BpmUserAgencyLogVO>)this.bpmUserAgencyLogManager.selectBpmUserAgencyLogList(queryFilter);
/*    */     } 
/* 44 */     return new PageResult((List)pageList);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmUserAgencyLogController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */