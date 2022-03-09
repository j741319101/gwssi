/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*    */ import com.dstz.base.rest.ControllerTools;
/*    */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*    */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import com.dstz.bpm.core.model.BpmTaskStack;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RequestMapping({"/bpm/dynaMultTaskReset"})
/*    */ @RestController
/*    */ public class BpmDynaMultTaskResetController
/*    */   extends ControllerTools
/*    */ {
/*    */   @Resource
/*    */   private BpmTaskStackManager bpmTaskStackManager;
/*    */   @Resource
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   
/*    */   @RequestMapping({"/getTaskList"})
/*    */   public PageResult<BpmTask> getTaskList(HttpServletRequest request) {
/* 36 */     QueryFilter queryFilter = getQueryFilter(request);
/* 37 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByInstIdAndNodeId(queryFilter);
/* 38 */     StringBuffer sb = new StringBuffer();
/* 39 */     bpmTaskStacks.forEach(bpmTaskStack -> sb.append(bpmTaskStack.getTaskId()).append(","));
/*    */ 
/*    */     
/* 42 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 43 */     defaultQueryFilter.addFilter("id_", sb.substring(0, sb.length() - 1), QueryOP.IN);
/* 44 */     List<BpmTask> list = this.bpmTaskManager.query((QueryFilter)defaultQueryFilter);
/* 45 */     return new PageResult(list);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmDynaMultTaskResetController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */