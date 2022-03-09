/*    */ package cn.gwssi.ecloudbpm.wf.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmTaskService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class DefaultBpmTaskService
/*    */   implements BpmTaskService
/*    */ {
/*    */   @Resource
/*    */   private BpmProcessDefService bpmProcessDefService;
/*    */   @Resource
/*    */   private BpmTaskManager bpmTaskManager;
/*    */   @Resource
/*    */   private IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public IBpmTask getBpmTask(String taskId) {
/* 32 */     return (IBpmTask)this.bpmTaskManager.get(taskId);
/*    */   }
/*    */ 
/*    */   
/*    */   public BpmNodeDef getBpmNodeDef(String taskId) {
/* 37 */     BpmTask bpmTask = (BpmTask)this.bpmTaskManager.get(taskId);
/* 38 */     if (bpmTask != null) {
/* 39 */       return this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/*    */     }
/* 41 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTaskReminderStr(String reminderScript, Map<String, Object> params) {
/* 46 */     List<SysIdentity> sysIdentities = BpmContext.getThreadNSysIdentities();
/* 47 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/* 48 */       return "执行操作成功";
/*    */     }
/* 50 */     if (StringUtils.isEmpty(reminderScript)) {
/* 51 */       StringBuffer remindUser = new StringBuffer("执行操作成功,已发送给");
/* 52 */       for (int i = 0; i < sysIdentities.size(); i++) {
/* 53 */         if (i == 2) {
/* 54 */           remindUser = (new StringBuffer(remindUser.substring(0, remindUser.length() - 1))).append("等人,");
/*    */           break;
/*    */         } 
/* 57 */         remindUser.append(((SysIdentity)sysIdentities.get(i)).getName() + ",");
/*    */       } 
/* 59 */       return remindUser.substring(0, remindUser.length() - 1);
/*    */     } 
/* 61 */     Map<String, Object> vars = new HashMap<>();
/* 62 */     vars.put("sysIdentities", sysIdentities);
/* 63 */     vars.putAll(params);
/* 64 */     String str = this.groovyScriptEngine.executeString(reminderScript, vars);
/* 65 */     return str;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/service/DefaultBpmTaskService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */