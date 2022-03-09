/*    */ package cn.gwssi.ecloudbpm.wf.plugin.rest.controller;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmReminderTriggerManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmReminderTrigger;
/*    */ import cn.gwssi.ecloudframework.base.rest.BaseController;
/*    */ import javax.annotation.Resource;
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
/*    */ @RestController
/*    */ @RequestMapping({"/bpm/bpmReminderTrigger"})
/*    */ public class BpmReminderTriggerController
/*    */   extends BaseController<BpmReminderTrigger>
/*    */ {
/*    */   @Resource
/*    */   BpmReminderTriggerManager bpmReminderTriggerManager;
/*    */   
/*    */   protected String getModelDesc() {
/* 28 */     return "流程催办触发";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmReminderTriggerController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */