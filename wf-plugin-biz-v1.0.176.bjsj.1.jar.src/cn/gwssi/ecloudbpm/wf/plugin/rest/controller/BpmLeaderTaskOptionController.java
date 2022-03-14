/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
/*    */ import com.dstz.base.rest.BaseController;
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
/*    */ @RequestMapping({"/bpm/leaderOption"})
/*    */ @RestController
/*    */ public class BpmLeaderTaskOptionController
/*    */   extends BaseController<BpmLeaderOptionLog>
/*    */ {
/*    */   protected String getModelDesc() {
/* 25 */     return "领导任务";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmLeaderTaskOptionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */