/*    */ package com.dstz.bpm.plugin.usercalc.samenode.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.OpinionStatus;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.api.model.task.IBpmTask;
/*    */ import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class SameNodePluginExecutor
/*    */   extends AbstractUserCalcPlugin<SameNodePluginDef>
/*    */ {
/*    */   @Resource
/*    */   BpmTaskOpinionManager taskOpinionManager;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, SameNodePluginDef sameNodeDef) {
/* 33 */     List<SysIdentity> bpmIdentities = new ArrayList<>();
/* 34 */     IBpmTask task = pluginSession.getBpmTask();
/* 35 */     if (task == null) {
/* 36 */       return Collections.emptyList();
/*    */     }
/*    */     
/* 39 */     List<BpmTaskOpinion> taskOpinionList = this.taskOpinionManager.getByInstAndNode(pluginSession.getBpmTask().getInstId(), sameNodeDef.getNodeId());
/* 40 */     if (CollectionUtil.isEmpty(taskOpinionList)) return bpmIdentities;
/*    */     
/* 42 */     IBpmTaskOpinion taskOpinion = null;
/* 43 */     for (int i = taskOpinionList.size() - 1; i > -1; i--) {
/* 44 */       if (StringUtil.isNotEmpty(((BpmTaskOpinion)taskOpinionList.get(i)).getApprover()) && !StringUtils.equals(((BpmTaskOpinion)taskOpinionList.get(i)).getStatus(), OpinionStatus.RECALL.getKey())) {
/* 45 */         taskOpinion = (IBpmTaskOpinion)taskOpinionList.get(i);
/*    */         break;
/*    */       } 
/*    */     } 
/* 49 */     if (taskOpinion != null) {
/* 50 */       DefaultIdentity defaultIdentity = new DefaultIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), "user", taskOpinion.getTaskOrgId());
/* 51 */       bpmIdentities.add(defaultIdentity);
/*    */     } 
/*    */ 
/*    */     
/* 55 */     return bpmIdentities;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportPreView() {
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/samenode/executer/SameNodePluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */