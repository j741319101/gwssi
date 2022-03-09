/*     */ package com.dstz.bpm.engine.action.handler.instance;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class InstanceStartActionHandler
/*     */   extends InstanceSaveActionHandler
/*     */ {
/*     */   @Resource
/*     */ BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   ActInstanceService actInstanceService;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public void doAction(DefaultInstanceActionCmd startActionModel) {
/*  43 */     String[] destinations = startActionModel.getDestinations();
/*     */     
/*  45 */     BpmInstance instance = (BpmInstance)startActionModel.getBpmInstance();
/*  46 */     instance.setStatus(InstanceStatus.STATUS_RUNNING.getKey());
/*     */     
/*  48 */     String actInstId = null;
/*  49 */     if (destinations == null || destinations.length == 0) {
/*     */       
/*  51 */       actInstId = this.actInstanceService.startProcessInstance(instance.getActDefId(), instance.getBizKey(), startActionModel.getActionVariables());
/*     */     } else {
/*  53 */       String skipFirstDestination = null;
/*  54 */       List<BpmNodeDef> startNodes = this.bpmProcessDefService.getStartNodes(instance.getDefId());
/*  55 */       for (String destination : destinations) {
/*  56 */         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(instance.getDefId(), destination);
/*  57 */         if (bpmNodeDef.getType() == NodeType.CALLACTIVITY) {
/*  58 */           JSONObject extendConf = startActionModel.getExtendConf();
/*  59 */           if (extendConf == null) {
/*  60 */             extendConf = new JSONObject();
/*     */           }
/*  62 */           JSONArray callActivityKeys = extendConf.getJSONArray("StartSubProcess");
/*  63 */           if (callActivityKeys == null) {
/*  64 */             callActivityKeys = new JSONArray();
/*  65 */             extendConf.put("StartSubProcess", callActivityKeys);
/*     */           } 
/*  67 */           callActivityKeys.add(((CallActivityNodeDef)bpmNodeDef).getFlowKey());
/*  68 */           startActionModel.setExtendConf(extendConf);
/*     */         } 
/*  70 */         startActionModel.addStartAppointDestination(destination);
/*     */         
/*  72 */         if (StringUtils.isEmpty(skipFirstDestination)) {
/*     */ 
/*     */           
/*  75 */           boolean isFirstNode = true;
/*  76 */           for (BpmNodeDef firstNode : startNodes) {
/*  77 */             if (!StringUtils.equals(firstNode.getNodeId(), destination)) {
/*  78 */               isFirstNode = false;
/*     */               break;
/*     */             } 
/*     */           } 
/*  82 */           if (!isFirstNode) {
/*  83 */             BpmNodeDef inComeNode = null;
/*  84 */             List<BpmNodeDef> incomeBpmNodeDefs = bpmNodeDef.getIncomeNodes();
/*  85 */             BpmNodeDef parentNodeDef = bpmNodeDef.getParentBpmNodeDef();
/*  86 */             if (parentNodeDef != null && parentNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef) {
/*  87 */               incomeBpmNodeDefs = parentNodeDef.getIncomeNodes();
/*     */             }
/*     */             
/*  90 */             label65: for (BpmNodeDef incomeBpmNodeDef : incomeBpmNodeDefs) {
/*  91 */               List<BpmNodeDef> incomeBpmNods = new ArrayList<>();
/*  92 */               if (incomeBpmNodeDef.getType() != NodeType.USERTASK) {
/*  93 */                 incomeBpmNods = getIncomeUserTaskNodeDef(incomeBpmNodeDef, new HashSet<>());
/*     */               } else {
/*  95 */                 incomeBpmNods.add(incomeBpmNodeDef);
/*     */               } 
/*  97 */               for (BpmNodeDef startNode : startNodes) {
/*  98 */                 for (BpmNodeDef incomeBpmNode : incomeBpmNods) {
/*  99 */                   if (StringUtils.equals(startNode.getNodeId(), incomeBpmNode.getNodeId())) {
/* 100 */                     inComeNode = startNode;
/*     */                     break label65;
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 106 */             if (inComeNode == null) {
/* 107 */               throw new BusinessException("启动流程获取第一个节点任务异常");
/*     */             }
/* 109 */             skipFirstDestination = inComeNode.getNodeId();
/*     */             
/* 111 */             startActionModel.setDynamicSubmitTaskName(inComeNode.getName());
/*     */           } else {
/*     */             
/* 114 */             skipFirstDestination = destination;
/*     */           } 
/*     */         } 
/* 117 */       }  actInstId = this.actInstanceService.startProcessInstance((IBpmInstance)instance, startActionModel
/* 118 */           .getActionVariables(), new String[] { skipFirstDestination });
/*     */     } 
/*     */     
/* 121 */     instance.setActInstId(actInstId);
/* 122 */     persistenceInstance(startActionModel);
/*     */   }
/*     */   
/*     */   private List<BpmNodeDef> getIncomeUserTaskNodeDef(BpmNodeDef bpmNodeDef, Set<BpmNodeDef> existsNodes) {
/* 126 */     if (bpmNodeDef.getType() == NodeType.USERTASK) {
/* 127 */       return Arrays.asList(new BpmNodeDef[] { bpmNodeDef });
/*     */     }
/* 129 */     List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getIncomeNodes();
/* 130 */     List<BpmNodeDef> userTasks = new ArrayList<>();
/* 131 */     if (CollectionUtil.isNotEmpty(bpmNodeDefs))
/*     */     {
/* 133 */       label20: for (BpmNodeDef bpmNodeDef1 : bpmNodeDefs) {
/* 134 */         for (BpmNodeDef existsNode : existsNodes) {
/* 135 */           if (StringUtils.equals(existsNode.getNodeId(), bpmNodeDef1.getNodeId())) {
/*     */             continue label20;
/*     */           }
/*     */         } 
/* 139 */         existsNodes.add(bpmNodeDef1);
/* 140 */         List<BpmNodeDef> comeNodeList = getIncomeUserTaskNodeDef(bpmNodeDef1, existsNodes);
/* 141 */         if (CollectionUtil.isNotEmpty(comeNodeList)) {
/* 142 */           userTasks.addAll(comeNodeList);
/*     */         }
/*     */       } 
/*     */     }
/* 146 */     return userTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 158 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 163 */     return ActionType.START;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 168 */     return Boolean.valueOf((nodeDef.getType() == NodeType.START));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceStartActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */