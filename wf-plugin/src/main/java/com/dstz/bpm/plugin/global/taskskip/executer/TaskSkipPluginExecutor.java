/*     */ package com.dstz.bpm.plugin.global.taskskip.executer;
/*     */ 
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.constant.TaskSkipType;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class TaskSkipPluginExecutor
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, TaskSkipPluginDef>
/*     */ {
/*     */   @Resource
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Resource
/*     */   BpmProcessDefService processDefService;
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
/*  41 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*     */ 
/*     */     
/*  44 */     TaskSkipType isSkip = getSkipResult(pluginSession, pluginDef);
/*     */     
/*  46 */     model.setHasSkipThisTask(isSkip);
/*     */     
/*  48 */     return null;
/*     */   }
/*     */   
/*     */   private TaskSkipType getSkipResult(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
/*  52 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/*  54 */     TaskSkipType skipResult = TaskSkipType.NO_SKIP;
/*     */     
/*  56 */     if (StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
/*  57 */       return skipResult;
/*     */     }
/*  59 */     String[] skip = pluginDef.getSkipTypeArr().split(",");
/*     */     
/*  61 */     for (String typeStr : skip) {
/*  62 */       boolean isSkip; List<SysIdentity> identityList, identityList1; TaskSkipType type = TaskSkipType.fromKey(typeStr);
/*  63 */       switch (type) {
/*     */         case ALL_SKIP:
/*  65 */           skipResult = TaskSkipType.ALL_SKIP;
/*     */           break;
/*     */         case SCRIPT_SKIP:
/*  68 */           isSkip = this.scriptEngine.executeBoolean(pluginDef.getScript(), (Map)pluginSession);
/*  69 */           if (isSkip) {
/*  70 */             skipResult = TaskSkipType.SCRIPT_SKIP;
/*     */           }
/*     */           break;
/*     */         
/*     */         case SAME_USER_SKIP:
/*  75 */           identityList = model.getBpmIdentity(model.getBpmTask().getNodeId());
/*  76 */           if (CollectionUtil.isNotEmpty(identityList)) {
/*  77 */             String userId = ContextUtil.getCurrentUserId();
/*  78 */             SysIdentity identity = identityList.get(0);
/*     */             
/*  80 */             if (identity.getId().equals(userId)) {
/*  81 */               skipResult = TaskSkipType.SAME_USER_SKIP;
/*     */             }
/*     */           } 
/*     */           break;
/*     */         
/*     */         case USER_EMPTY_SKIP:
/*  87 */           identityList1 = model.getBpmIdentity(model.getBpmTask().getNodeId());
/*  88 */           if (CollectionUtil.isEmpty(identityList1)) {
/*  89 */             skipResult = TaskSkipType.USER_EMPTY_SKIP;
/*     */           }
/*     */           break;
/*     */         case FIRSTNODE_SKIP:
/*  93 */           if (ActionType.START.getKey().equals(BpmContext.submitActionModel().getActionName())) {
/*  94 */             List<BpmNodeDef> list = this.processDefService.getStartNodes(model.getBpmTask().getDefId());
/*  95 */             for (BpmNodeDef def : list) {
/*  96 */               if (def.getNodeId().equals(model.getBpmTask().getNodeId())) {
/*  97 */                 skipResult = TaskSkipType.FIRSTNODE_SKIP;
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 105 */       if (skipResult != TaskSkipType.NO_SKIP) {
/* 106 */         this.LOG.info("{}节点【{}】设置了【{}】，将跳过当前任务", new Object[] { model.getBpmTask().getId(), model.getBpmTask().getName(), skipResult.getValue() });
/* 107 */         return skipResult;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return skipResult;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/taskskip/executer/TaskSkipPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */