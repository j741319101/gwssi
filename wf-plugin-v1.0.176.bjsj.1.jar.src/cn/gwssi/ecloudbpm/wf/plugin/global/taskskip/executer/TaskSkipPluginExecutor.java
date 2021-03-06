/*     */ package com.dstz.bpm.plugin.global.taskskip.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.util.StringUtils;
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
/*  42 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*     */ 
/*     */     
/*  45 */     if (model.isHasSkipThisTask() == TaskSkipType.NO_SKIP) {
/*  46 */       TaskSkipType isSkip = getSkipResult(pluginSession, pluginDef);
/*  47 */       model.setHasSkipThisTask(isSkip);
/*     */     } 
/*  49 */     return null;
/*     */   }
/*     */   
/*     */   private TaskSkipType getSkipResult(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
/*  53 */     TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
/*     */     
/*  55 */     TaskSkipType skipResult = TaskSkipType.NO_SKIP;
/*     */     
/*  57 */     if (StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
/*  58 */       return skipResult;
/*     */     }
/*  60 */     String[] skip = pluginDef.getSkipTypeArr().split(",");
/*     */     
/*  62 */     for (String typeStr : skip) {
/*  63 */       List<SysIdentity> identityList, identityList1; TaskSkipType type = TaskSkipType.fromKey(typeStr);
/*  64 */       switch (type) {
/*     */         case ALL_SKIP:
/*  66 */           skipResult = TaskSkipType.ALL_SKIP;
/*     */           break;
/*     */         case SCRIPT_SKIP:
/*  69 */           if (!StringUtils.isEmpty(pluginDef.getScript())) {
/*  70 */             boolean isSkip = this.scriptEngine.executeBoolean(pluginDef.getScript(), (Map)pluginSession);
/*  71 */             if (isSkip) {
/*  72 */               skipResult = TaskSkipType.SCRIPT_SKIP;
/*     */             }
/*     */           } 
/*     */           break;
/*     */         
/*     */         case SAME_USER_SKIP:
/*  78 */           identityList = model.getBpmIdentity(model.getBpmTask().getNodeId());
/*  79 */           if (CollectionUtil.isNotEmpty(identityList)) {
/*  80 */             String userId = ContextUtil.getCurrentUserId();
/*  81 */             SysIdentity identity = identityList.get(0);
/*     */             
/*  83 */             if (identity.getId().equals(userId)) {
/*  84 */               skipResult = TaskSkipType.SAME_USER_SKIP;
/*     */             }
/*     */           } 
/*     */           break;
/*     */         
/*     */         case USER_EMPTY_SKIP:
/*  90 */           identityList1 = model.getBpmIdentity(model.getBpmTask().getNodeId());
/*  91 */           if (CollectionUtil.isEmpty(identityList1)) {
/*  92 */             skipResult = TaskSkipType.USER_EMPTY_SKIP;
/*     */           }
/*     */           break;
/*     */         case FIRSTNODE_SKIP:
/*  96 */           if (ActionType.START.getKey().equals(BpmContext.submitActionModel().getActionName())) {
/*  97 */             List<BpmNodeDef> list = this.processDefService.getStartNodes(model.getBpmTask().getDefId());
/*  98 */             for (BpmNodeDef def : list) {
/*  99 */               if (def.getNodeId().equals(model.getBpmTask().getNodeId())) {
/* 100 */                 skipResult = TaskSkipType.FIRSTNODE_SKIP;
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 108 */       if (skipResult != TaskSkipType.NO_SKIP) {
/* 109 */         this.LOG.info("{}?????????{}???????????????{}???????????????????????????", new Object[] { model.getBpmTask().getId(), model.getBpmTask().getName(), skipResult.getValue() });
/* 110 */         return skipResult;
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     return skipResult;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/taskskip/executer/TaskSkipPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */