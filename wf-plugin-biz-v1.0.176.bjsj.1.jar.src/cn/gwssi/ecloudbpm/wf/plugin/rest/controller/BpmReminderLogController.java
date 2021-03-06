/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.dstz.base.rest.BaseController;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/bpm/bpmReminderLog"})
/*    */ public class BpmReminderLogController
/*    */   extends BaseController<BpmReminderLog>
/*    */ {
/*    */   @Resource
/*    */   BpmReminderLogManager bpmReminderLogManager;
/*    */   
/*    */   protected String getModelDesc() {
/* 38 */     return "流程催办日志";
/*    */   }
/*    */ 
/*    */   
/*    */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 43 */     QueryFilter queryFilter = getQueryFilter(request);
/*    */     
/* 45 */     List<BpmReminderLog> pageList = this.bpmReminderLogManager.query(queryFilter);
/* 46 */     return new PageResult(pageList);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmReminderLogController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */