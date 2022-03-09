/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.base.rest.BaseController;
/*    */ import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RequestMapping({"/bpm/leaderOption"})
/*    */ @RestController
/*    */ public class BpmLeaderTaskOptionController
/*    */   extends BaseController<BpmLeaderOptionLog>
/*    */ {
/*    */   protected String getModelDesc() {
/* 35 */     return "领导任务";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmLeaderTaskOptionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */