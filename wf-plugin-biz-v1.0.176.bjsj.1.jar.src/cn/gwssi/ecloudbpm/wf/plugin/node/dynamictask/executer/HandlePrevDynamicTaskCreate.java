/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
/*     */ import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.bean.copier.CopyOptions;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
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
/*     */ @Component
/*     */ public class HandlePrevDynamicTaskCreate
/*     */ {
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   DynamicInstTaskAction dynamicInstTaskAction;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private SuperviseTaskExecuter superviseTaskExecuter;
/*     */   
/*     */   public void prevDynamicTaskCreate(DefaultBpmTaskPluginSession pluginSession) {
/*  48 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  49 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/*  50 */     String[] destinationNodes = model.getDestinations();
/*     */     
/*  52 */     Map<String, BpmNodeDef> bpmNodeDefMap = new HashMap<>();
/*     */     
/*  54 */     List<String> nodeIds = new ArrayList<>();
/*     */     
/*  56 */     Map<String, Boolean> isInstDy = new HashMap<>();
/*     */     
/*  58 */     List<String> noNodeIds = new ArrayList<>();
/*  59 */     if (destinationNodes != null) {
/*     */       
/*  61 */       for (String destination : destinationNodes) {
/*  62 */         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), destination);
/*  63 */         if (DynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
/*  64 */           nodeIds.add(destination);
/*  65 */           bpmNodeDefMap.put(destination, bpmNodeDef);
/*  66 */         } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession, true)) {
/*  67 */           nodeIds.add(destination);
/*  68 */           isInstDy.put(destination, Boolean.valueOf(true));
/*  69 */           bpmNodeDefMap.put(destination, bpmNodeDef);
/*     */         } else {
/*  71 */           noNodeIds.add(destination);
/*     */         } 
/*     */       } 
/*     */     } else {
/*  75 */       List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()).getOutcomeNodes();
/*  76 */       for (int i = 0; i < bpmNodeDefs.size(); i++) {
/*  77 */         BpmNodeDef bpmNodeDef = bpmNodeDefs.get(i);
/*  78 */         bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), bpmNodeDef.getNodeId());
/*  79 */         if (DynamicInstTaskAction.isDynamicTask(bpmNodeDef)) {
/*  80 */           nodeIds.add(bpmNodeDef.getNodeId());
/*  81 */           bpmNodeDefMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
/*  82 */         } else if (this.dynamicInstTaskAction.isBeginDynamicInstTask(bpmNodeDef, pluginSession, true)) {
/*  83 */           nodeIds.add(bpmNodeDef.getNodeId());
/*  84 */           isInstDy.put(bpmNodeDef.getNodeId(), Boolean.valueOf(true));
/*  85 */           bpmNodeDefMap.put(bpmNodeDef.getNodeId(), bpmNodeDef);
/*     */         } else {
/*  87 */           noNodeIds.add(bpmNodeDef.getNodeId());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(task.getId());
/*     */     
/*  94 */     List<BpmExecutionStack> prevBpmExecutionStacks = new ArrayList<>();
/*  95 */     if (CollectionUtil.isNotEmpty(nodeIds)) {
/*     */       
/*  97 */       this.dynamicInstTaskAction.setTaskThreadLocal(task);
/*     */       
/*  99 */       Map<String, List<SysIdentity>> identitiesMap = new HashMap<>();
/*     */       
/* 101 */       Map<String, List<List<SysIdentity>>> lidentitieLstsMap = new HashMap<>();
/* 102 */       boolean isParallel = false;
/* 103 */       List<String> destinations = new ArrayList<>();
/* 104 */       for (String nodeId : nodeIds) {
/* 105 */         BpmTaskStack bpmTaskStack1 = new BpmTaskStack();
/* 106 */         prevBpmExecutionStacks.add(bpmTaskStack1);
/* 107 */         List<SysIdentity> identities = null;
/* 108 */         List<List<SysIdentity>> lidentitieLsts = null;
/* 109 */         BpmNodeDef bpmNodeDef = bpmNodeDefMap.get(nodeId);
/* 110 */         CopyOptions copyOptions = new CopyOptions();
/* 111 */         copyOptions.setIgnoreError(true);
/* 112 */         BeanUtil.copyProperties(bpmTaskStack, bpmTaskStack1, copyOptions);
/* 113 */         bpmTaskStack1.setNodeId(nodeId);
/* 114 */         bpmTaskStack1.setInstId(task.getInstId());
/*     */ 
/*     */         
/* 117 */         if (bpmNodeDef.getParentBpmNodeDef() != null && bpmNodeDef.getParentBpmNodeDef() instanceof com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef) {
/* 118 */           bpmTaskStack1.setNodeId(bpmNodeDef.getParentBpmNodeDef().getNodeId());
/* 119 */           BpmContext.setThreadDynamictaskStack(bpmNodeDef.getParentBpmNodeDef().getNodeId(), (BpmExecutionStack)bpmTaskStack1);
/*     */         } else {
/* 121 */           BpmContext.setThreadDynamictaskStack(nodeId, (BpmExecutionStack)bpmTaskStack1);
/*     */         } 
/*     */         
/* 124 */         if (isInstDy.get(nodeId) != null && ((Boolean)isInstDy.get(nodeId)).booleanValue()) {
/* 125 */           bpmTaskStack1.setNodeName(nodeId);
/* 126 */           if (bpmNodeDef instanceof CallActivityNodeDef) {
/* 127 */             model.setDynamicSubmitTaskName(bpmNodeDef.getName() + "-" + pluginSession.get("submitTaskName"));
/* 128 */             CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)bpmNodeDef;
/* 129 */             List<BpmNodeDef> bpmNodeDefs = this.bpmProcessDefService.getStartNodes(this.bpmProcessDefService
/* 130 */                 .getDefinitionByKey(callActivityNodeDef.getFlowKey()).getId());
/* 131 */             if (bpmNodeDefs.size() != 1) {
/* 132 */               throw new BusinessException("子流程开启动态多实例 启动节点后置任务要唯一");
/*     */             }
/* 134 */             for (BpmNodeDef bpmNodeDef1 : bpmNodeDefs) {
/* 135 */               identities = model.getBpmIdentity(bpmNodeDef1.getNodeId());
/* 136 */               lidentitieLsts = model.getDynamicBpmIdentity(bpmNodeDef1.getNodeId());
/* 137 */               if (CollectionUtil.isNotEmpty(identities) || CollectionUtil.isNotEmpty(lidentitieLsts)) {
/*     */                 break;
/*     */               }
/*     */             } 
/* 141 */             if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
/* 142 */               identities = model.getBpmIdentity(((BpmNodeDef)bpmNodeDefs.get(0)).getNodeId());
/* 143 */               if (CollectionUtil.isEmpty(identities)) {
/* 144 */                 DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/* 145 */                 taskModel.setBizDataMap(model.getBizDataMap());
/* 146 */                 taskModel.setBusData(model.getBusData());
/* 147 */                 taskModel.setActionName("start");
/* 148 */                 identities = UserCalcPreview.calcNodeUsers(bpmNodeDefs.get(0), taskModel);
/*     */               } 
/*     */             } 
/*     */           } else {
/* 152 */             identities = model.getBpmIdentity(nodeId);
/* 153 */             lidentitieLsts = model.getDynamicBpmIdentity(nodeId);
/* 154 */             if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
/* 155 */               identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
/*     */             }
/*     */           } 
/*     */         } else {
/* 159 */           identities = model.getBpmIdentity(nodeId);
/* 160 */           lidentitieLsts = model.getDynamicBpmIdentity(nodeId);
/* 161 */           if (CollectionUtil.isEmpty(identities) && CollectionUtil.isEmpty(lidentitieLsts)) {
/* 162 */             identities = UserCalcPreview.calcNodeUsers(bpmNodeDef, model);
/* 163 */             model.setBpmIdentity(nodeId, identities);
/*     */           } 
/*     */         } 
/*     */         
/* 167 */         if (CollectionUtil.isNotEmpty(identities)) {
/* 168 */           identitiesMap.put(nodeId, identities);
/*     */         }
/* 170 */         else if (CollectionUtil.isNotEmpty(lidentitieLsts)) {
/* 171 */           lidentitieLstsMap.put(nodeId, lidentitieLsts);
/*     */         } else {
/* 173 */           throw new BusinessException("任务【" + bpmNodeDef.getName() + "】预计算候选人为空，无法生成多实例任务");
/*     */         } 
/*     */         
/* 176 */         if (DynamicTaskPluginExecuter.isParallel(bpmNodeDef)) {
/* 177 */           isParallel = true;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 182 */       if (CollectionUtil.isNotEmpty(noNodeIds)) {
/* 183 */         noNodeIds.forEach(noNode -> {
/*     */               BpmTaskStack bpmTaskStack = new BpmTaskStack();
/*     */               
/*     */               CopyOptions copyOptions = new CopyOptions();
/*     */               copyOptions.setIgnoreError(true);
/*     */               BeanUtil.copyProperties(bpmExecutionStack, bpmTaskStack, copyOptions);
/*     */               bpmTaskStack.setNodeId(noNode);
/*     */               bpmTaskStack.setInstId(task.getInstId());
/*     */               BpmContext.setThreadDynamictaskStack(noNode, (BpmExecutionStack)bpmTaskStack);
/*     */             });
/*     */       }
/* 194 */       if (isParallel) {
/*     */         
/* 196 */         List<String> typeSupervise = this.superviseTaskExecuter.preTaskComplete(pluginSession);
/* 197 */         if (CollectionUtil.isNotEmpty(typeSupervise) && typeSupervise.contains("dynamic")) {
/* 198 */           BpmTaskStack bpmTaskStack1 = new BpmTaskStack();
/* 199 */           CopyOptions copyOptions = new CopyOptions();
/* 200 */           BeanUtil.copyProperties(prevBpmExecutionStacks.get(0), bpmTaskStack1, copyOptions);
/* 201 */           bpmTaskStack1.setNodeId(task.getNodeId());
/* 202 */           BpmContext.setThreadDynamictaskStack(task.getNodeId(), (BpmExecutionStack)bpmTaskStack1);
/* 203 */           destinations.add(task.getNodeId());
/*     */         } 
/*     */ 
/*     */         
/* 207 */         if (CollectionUtil.isNotEmpty(lidentitieLstsMap)) {
/* 208 */           for (String nodeId : lidentitieLstsMap.keySet()) {
/* 209 */             if (nodeIds.contains(nodeId)) {
/* 210 */               List<List<SysIdentity>> lidentitieLsts = lidentitieLstsMap.get(nodeId);
/* 211 */               for (int i = 0; i < lidentitieLsts.size(); i++) {
/* 212 */                 destinations.add(nodeId);
/* 213 */                 DynamicInstTaskAction.pushDynamictaskIndex(nodeId, Integer.valueOf(i));
/* 214 */                 DynamicInstTaskAction.pushDynamictaskIdentities(nodeId, lidentitieLsts.get(i));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/* 219 */         if (CollectionUtil.isNotEmpty(identitiesMap)) {
/* 220 */           for (String nodeId : identitiesMap.keySet()) {
/* 221 */             if (nodeIds.contains(nodeId)) {
/* 222 */               List<SysIdentity> identities = identitiesMap.get(nodeId);
/* 223 */               for (int i = 0; i < identities.size(); i++) {
/* 224 */                 destinations.add(nodeId);
/* 225 */                 DynamicInstTaskAction.pushDynamictaskIndex(nodeId, Integer.valueOf(i));
/* 226 */                 DynamicInstTaskAction.pushDynamictaskIdentities(nodeId, Arrays.asList(new SysIdentity[] { identities.get(i) }));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/* 231 */         destinations.addAll(noNodeIds);
/* 232 */         model.setDestinations(destinations.<String>toArray(new String[destinations.size()]));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/HandlePrevDynamicTaskCreate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */