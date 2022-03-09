/*     */ package com.dstz.bpm.core.model;
/*     */ 
/*     */ import com.dstz.bus.api.model.IBusinessData;
/*     */ import com.dstz.bus.api.service.IBusinessDataService;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmExecutionPluginSession;
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.druid.util.StringUtils;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.impl.RepositoryServiceImpl;
/*     */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*     */ import org.activiti.engine.impl.pvm.PvmTransition;
/*     */ import org.activiti.engine.impl.pvm.process.ActivityImpl;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParamsBpmNodeDef
/*     */   extends BaseBpmNodeDef
/*     */ {
/*     */   public List<BpmNodeDef> getOutcomeTaskNodes() {
/*  42 */     return getNodeDefList(getOutcomeNodes(), false, (BpmNodeDef)null, new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK, NodeType.END });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BpmNodeDef> getNodeDefList(List<BpmNodeDef> bpmNodeDefs, boolean isIn, BpmNodeDef activityNode, NodeType... nodeTypes) {
/*  56 */     if (nodeTypes == null || nodeTypes.length == 0) {
/*  57 */       nodeTypes = new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK };
/*     */     }
/*     */     
/*  60 */     List<BpmNodeDef> bpmNodeList = new ArrayList<>();
/*  61 */     for (BpmNodeDef def : bpmNodeDefs) {
/*  62 */       if (CollUtil.newArrayList((Object[])nodeTypes).contains(def.getType())) {
/*  63 */         if (activityNode != null && activityNode instanceof CallActivityNodeDef) {
/*  64 */           CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)activityNode;
/*  65 */           BaseBpmNodeDef bpmNodeDef = new BaseBpmNodeDef();
/*  66 */           BeanUtils.copyProperties(def, bpmNodeDef);
/*  67 */           bpmNodeDef.setName(callActivityNodeDef.getName() + "-" + def.getName());
/*  68 */           bpmNodeDef.setNodeId(callActivityNodeDef.getNodeId() + "&" + def.getNodeId());
/*  69 */           if (CollectionUtil.isNotEmpty(activityNode.getIncomeNodes()) && NodeType.INCLUSIVEGATEWAY
/*  70 */             .equals(((BpmNodeDef)activityNode.getIncomeNodes().get(0)).getType())) {
/*  71 */             bpmNodeDef.setIncomeNodes(activityNode.getIncomeNodes());
/*     */           }
/*  73 */           bpmNodeList.add(bpmNodeDef); continue;
/*  74 */         }  if (activityNode != null && NodeType.INCLUSIVEGATEWAY.equals(activityNode.getType())) {
/*  75 */           BaseBpmNodeDef bpmNodeDef = new BaseBpmNodeDef();
/*  76 */           BeanUtils.copyProperties(def, bpmNodeDef);
/*  77 */           bpmNodeDef.setName(activityNode.getName() + "-" + def.getName());
/*  78 */           bpmNodeDef.setNodeId(def.getNodeId());
/*  79 */           bpmNodeDef.setType(activityNode.getType());
/*  80 */           List<BpmNodeDef> incomeNodes = new ArrayList<>();
/*  81 */           incomeNodes.add(activityNode);
/*  82 */           bpmNodeDef.setIncomeNodes(incomeNodes);
/*  83 */           bpmNodeList.add(bpmNodeDef); continue;
/*     */         } 
/*  85 */         bpmNodeList.add(def); continue;
/*     */       } 
/*  87 */       if (NodeType.SUBPROCESS.equals(def.getType())) {
/*  88 */         SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)def;
/*     */         
/*  90 */         BpmNodeDef startNodeDef = subProcessNodeDef.getChildBpmProcessDef().getStartEvent();
/*  91 */         if (isIn) {
/*  92 */           bpmNodeList.addAll(getNodeDefList(startNodeDef.getIncomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */         } 
/*  94 */         bpmNodeList.addAll(getNodeDefList(startNodeDef.getOutcomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */       } 
/*  96 */       if (NodeType.END.equals(def.getType()) && def.getParentBpmNodeDef() != null && NodeType.SUBPROCESS.equals(def.getParentBpmNodeDef().getType())) {
/*  97 */         SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)def.getParentBpmNodeDef();
/*  98 */         if (isIn) {
/*  99 */           bpmNodeList.addAll(getNodeDefList(subProcessNodeDef.getIncomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */         } 
/* 101 */         bpmNodeList.addAll(getNodeDefList(subProcessNodeDef.getOutcomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */       } 
/* 103 */       if (NodeType.CALLACTIVITY.equals(def.getType())) {
/* 104 */         CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)def;
/* 105 */         BpmProcessDefService defService = AppUtil.getImplInstanceArray(BpmProcessDefService.class).get(0);
/* 106 */         IBpmDefinition bpmDefinition = defService.getDefinitionByKey(callActivityNodeDef.getFlowKey());
/* 107 */         BpmNodeDef startNodeDef = defService.getStartEvent(bpmDefinition.getId());
/* 108 */         callActivityNodeDef.setFlowName(bpmDefinition.getName());
/* 109 */         CallActivityNodeDef callActivityNodeDefGateway = new CallActivityNodeDef();
/* 110 */         BeanUtils.copyProperties(callActivityNodeDef, callActivityNodeDefGateway);
/* 111 */         if (activityNode != null && NodeType.INCLUSIVEGATEWAY.equals(activityNode.getType())) {
/* 112 */           callActivityNodeDefGateway.setName(activityNode.getName() + "-" + callActivityNodeDefGateway.getName());
/* 113 */           List<BpmNodeDef> incomeNodes = new ArrayList<>();
/* 114 */           incomeNodes.add(activityNode);
/* 115 */           callActivityNodeDefGateway.setIncomeNodes(incomeNodes);
/*     */         } 
/* 117 */         if (isIn) {
/* 118 */           bpmNodeList.addAll(getNodeDefList(startNodeDef.getIncomeNodes(), isIn, (BpmNodeDef)callActivityNodeDefGateway, nodeTypes)); continue;
/*     */         } 
/* 120 */         bpmNodeList.addAll(getNodeDefList(startNodeDef.getOutcomeNodes(), isIn, (BpmNodeDef)callActivityNodeDefGateway, nodeTypes)); continue;
/*     */       } 
/* 122 */       if (NodeType.INCLUSIVEGATEWAY.equals(def.getType())) {
/* 123 */         if (isIn) {
/* 124 */           bpmNodeList.addAll(getNodeDefList(def.getIncomeNodes(), isIn, def, nodeTypes)); continue;
/*     */         } 
/* 126 */         List<BpmNodeDef> proConditiondefOutcomeNodes = getConditionOutcomeNodes(def);
/* 127 */         bpmNodeList.addAll(getNodeDefList(proConditiondefOutcomeNodes, isIn, def, nodeTypes)); continue;
/*     */       } 
/* 129 */       if (NodeType.EXCLUSIVEGATEWAY.equals(def.getType())) {
/* 130 */         if (isIn) {
/* 131 */           bpmNodeList.addAll(getNodeDefList(def.getIncomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */         } 
/* 133 */         List<BpmNodeDef> proConditiondefOutcomeNodes = getConditionOutcomeNodes(def);
/* 134 */         bpmNodeList.addAll(getNodeDefList(proConditiondefOutcomeNodes, isIn, (BpmNodeDef)null, nodeTypes));
/*     */         continue;
/*     */       } 
/* 137 */       if (isIn) {
/* 138 */         bpmNodeList.addAll(getNodeDefList(def.getIncomeNodes(), isIn, (BpmNodeDef)null, nodeTypes)); continue;
/*     */       } 
/* 140 */       bpmNodeList.addAll(getNodeDefList(def.getOutcomeNodes(), isIn, (BpmNodeDef)null, nodeTypes));
/*     */     } 
/*     */ 
/*     */     
/* 144 */     return bpmNodeList;
/*     */   }
/*     */   
/*     */   private List<BpmNodeDef> getConditionOutcomeNodes(BpmNodeDef def) {
/* 148 */     List<BpmNodeDef> defOutcomeNodes = def.getOutcomeNodes();
/* 149 */     List<BpmNodeDef> proConditiondefOutcomeNodes = new ArrayList<>();
/* 150 */     RepositoryService repositoryService = AppUtil.getImplInstanceArray(RepositoryService.class).get(0);
/*     */     
/* 152 */     ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(((DefaultBpmProcessDef)
/* 153 */         getBpmProcessDef()).getJson().getJSONObject("bpmDefinition").getString("actDefId"));
/*     */     
/* 155 */     ActivityImpl activityImpl = processDefinitionEntity.findActivity(def.getNodeId());
/* 156 */     List<PvmTransition> outgoingTransitions = activityImpl.getOutgoingTransitions();
/* 157 */     DefaultBpmExecutionPluginSession bpmExecutionPluginSession = new DefaultBpmExecutionPluginSession();
/* 158 */     IGroovyScriptEngine groovyScriptEngine = AppUtil.getImplInstanceArray(IGroovyScriptEngine.class).get(0);
/* 159 */     IBusinessDataService iBusinessDataService = AppUtil.getImplInstanceArray(IBusinessDataService.class).get(0);
/* 160 */     for (PvmTransition pvmTransition : outgoingTransitions) {
/* 161 */       String groovyCondition = (String)pvmTransition.getProperty("conditionText");
/* 162 */       boolean isProCondition = true;
/* 163 */       if (StringUtil.isNotEmpty(groovyCondition)) {
/* 164 */         JSONObject data = BpmContext.getActionModel().getBusData();
/* 165 */         if (data != null && !data.isEmpty()) {
/* 166 */           Map<String, IBusinessData> businessDataMap = new HashMap<>();
/* 167 */           for (String key : data.keySet()) {
/* 168 */             IBusinessData businessData = iBusinessDataService.parseBusinessData(data.getJSONObject(key), key);
/* 169 */             businessDataMap.put(key, businessData);
/*     */           } 
/* 171 */           bpmExecutionPluginSession.setBoDatas(businessDataMap);
/* 172 */           bpmExecutionPluginSession.setBusData(data);
/*     */         } else {
/* 174 */           bpmExecutionPluginSession.setBoDatas(BpmContext.getActionModel().getBizDataMap());
/*     */         } 
/*     */         try {
/* 177 */           isProCondition = groovyScriptEngine.executeBoolean(groovyCondition, (Map)bpmExecutionPluginSession);
/* 178 */         } catch (Exception e) {
/* 179 */           throw new BusinessException(groovyCondition + "网关条件脚本执行错误: " + e.getMessage(), BaseStatusCode.SYSTEM_ERROR);
/*     */         } 
/*     */       } 
/* 182 */       if (isProCondition) {
/* 183 */         for (BpmNodeDef bpmNodeDef : defOutcomeNodes) {
/* 184 */           if (StringUtils.equals(bpmNodeDef.getNodeId(), pvmTransition.getDestination().getId())) {
/* 185 */             proConditiondefOutcomeNodes.add(bpmNodeDef);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return proConditiondefOutcomeNodes;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/ParamsBpmNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */