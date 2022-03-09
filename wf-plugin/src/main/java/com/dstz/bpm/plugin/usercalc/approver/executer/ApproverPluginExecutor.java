/*    */ package com.dstz.bpm.plugin.usercalc.approver.executer;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.model.BpmIdentity;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ApproverPluginExecutor
/*    */   extends AbstractUserCalcPlugin<ApproverPluginDef>
/*    */ {
/*    */   @Resource
/*    */   BpmTaskOpinionManager taskOpinionManager;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ApproverPluginDef pluginDef) {
/* 28 */     List<SysIdentity> bpmIdentities = new ArrayList<>();
/*    */     
/* 30 */     List<BpmTaskOpinion> taskOpinionList = this.taskOpinionManager.getByInstId(pluginSession.getBpmTask().getInstId());
/*    */     
/* 32 */     for (IBpmTaskOpinion taskOpinion : taskOpinionList) {
/* 33 */       if (StringUtil.isEmpty(taskOpinion.getApprover()))
/*    */         continue; 
/* 35 */       BpmIdentity bpmIdentity = new BpmIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), "user");
/* 36 */       bpmIdentities.add(bpmIdentity);
/*    */     } 
/*    */     
/* 39 */     return bpmIdentities;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/approver/executer/ApproverPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */