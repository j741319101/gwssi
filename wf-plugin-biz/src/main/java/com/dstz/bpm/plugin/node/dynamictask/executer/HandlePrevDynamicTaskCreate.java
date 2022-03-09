/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class HandlePrevDynamicTaskCreate
/*     */ {
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   DynamicInstTaskAction dynamicInstTaskAction;
/*     */   
/*     */   public void prevDynamicTaskCreate(DefaultBpmTaskPluginSession pluginSession) {
/*  35 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  36 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/*  37 */     String[] destinationNodes = model.getDestinations();
/*  38 */     BpmNodeDef bpmNodeDef = null;
/*  39 */     String nodeId = "";
/*  40 */     boolean idInstDy = false;
/*  41 */     if (destinationNodes != null) {
/*  42 */       if (destinationNodes.length == 1) {
/*  43 */         String destination = destinationNodes[0];
/*  44 */         bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), destination);
/*  45 */         if (this.dynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
/*  46 */           nodeId = destination;
/*  47 */         } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession)) {
/*  48 */           nodeId = destination;
/*  49 */           idInstDy = true;
/*     */         } 
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } else {
/*  55 */       List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()).getOutcomeNodes();
/*  56 */       if (bpmNodeDefs.size() == 1) {
/*  57 */         bpmNodeDef = bpmNodeDefs.get(0);
/*  58 */         bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), bpmNodeDef.getNodeId());
/*  59 */         if (this.dynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
/*  60 */           nodeId = bpmNodeDef.getNodeId();
/*  61 */         } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession)) {
/*  62 */           nodeId = bpmNodeDef.getNodeId();
/*  63 */           idInstDy = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*  67 */     if (StringUtils.isNotEmpty(nodeId)) {
/*  68 */       List<SysIdentity> identities = null;
/*  69 */       if (!DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
/*     */         return;
/*     */       }
/*  72 */       if (idInstDy) {
/*  73 */         if (bpmNodeDef instanceof CallActivityNodeDef) {
/*  74 */           CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)bpmNodeDef;
/*  75 */           List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getStartNodes(this.bpmProcessDefService
/*  76 */               .getDefinitionByKey(callActivityNodeDef.getFlowKey()).getId());
/*  77 */           if (bpmNodeDefs.size() != 1) {
/*  78 */             throw new BusinessException("子流程开启动态多实例 启动节点后置任务要唯一");
/*     */           }
/*  80 */           for (BpmNodeDef bpmNodeDef1 : bpmNodeDefs) {
/*  81 */             identities = model.getBpmIdentity(bpmNodeDef1.getNodeId());
/*  82 */             if (CollectionUtil.isNotEmpty(identities)) {
/*     */               break;
/*     */             }
/*     */           } 
/*  86 */           if (CollectionUtil.isEmpty(identities)) {
/*  87 */             identities = model.getBpmIdentity(((BpmNodeDef)bpmNodeDefs.get(0)).getNodeId());
/*  88 */             if (CollectionUtil.isEmpty(identities)) {
/*  89 */               DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/*  90 */               taskModel.setBizDataMap(model.getBizDataMap());
/*  91 */               taskModel.setBusData(model.getBusData());
/*  92 */               taskModel.setActionName("start");
/*  93 */               identities = UserCalcPreview.calcNodeUsers(bpmNodeDefs.get(0), taskModel);
/*     */             } 
/*     */           } 
/*     */         } else {
/*  97 */           identities = model.getBpmIdentity(nodeId);
/*  98 */           if (CollectionUtil.isEmpty(identities)) {
/*  99 */             identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 104 */         identities = model.getBpmIdentity(nodeId);
/* 105 */         if (CollectionUtil.isEmpty(identities)) {
/* 106 */           identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
/*     */         }
/*     */       } 
/* 109 */       if (identities.isEmpty()) {
/* 110 */         throw new BusinessException("任务【" + bpmNodeDef.getName() + "】预计算候选人为空，无法生成多实例任务");
/*     */       }
/* 112 */       List<String> destinations = new ArrayList<>();
/* 113 */       for (int i = 0; i < identities.size(); i++) {
/* 114 */         destinations.add(nodeId);
/* 115 */         BpmContext.pushDynamictaskIndex(Integer.valueOf(i));
/* 116 */         BpmContext.pushDynamictaskIdentities(identities.get(i));
/*     */       } 
/* 118 */       model.setDestinations(destinations.<String>toArray(new String[destinations.size()]));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/HandlePrevDynamicTaskCreate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */