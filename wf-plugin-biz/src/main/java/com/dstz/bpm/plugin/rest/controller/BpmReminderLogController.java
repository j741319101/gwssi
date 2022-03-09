/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.dstz.base.rest.BaseController;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmReminderLog;
/*    */ import com.github.pagehelper.Page;
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
/* 45 */     Page<BpmReminderLog> pageList = (Page<BpmReminderLog>)this.bpmReminderLogManager.query(queryFilter);
/* 46 */     return new PageResult((List)pageList);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmReminderLogController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */