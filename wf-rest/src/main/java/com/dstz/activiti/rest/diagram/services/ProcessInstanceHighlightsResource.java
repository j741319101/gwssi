/*     */ package com.dstz.activiti.rest.diagram.services;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.engine.HistoryService;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.RuntimeService;
/*     */ import org.activiti.engine.history.HistoricActivityInstance;
/*     */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*     */ import org.activiti.engine.impl.pvm.PvmTransition;
/*     */ import org.activiti.engine.impl.pvm.process.ActivityImpl;
/*     */ import org.activiti.engine.runtime.ProcessInstance;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RestController;
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
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ public class ProcessInstanceHighlightsResource
/*     */ {
/*     */   @Autowired
/*     */   private RuntimeService runtimeService;
/*     */   @Autowired
/*     */   private RepositoryService repositoryService;
/*     */   @Autowired
/*     */   private HistoryService historyService;
/*  47 */   protected ObjectMapper objectMapper = new ObjectMapper();
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/process-instance/{processInstanceId}/highlights"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*     */   public ObjectNode getHighlighted(@PathVariable String processInstanceId) {
/*  52 */     ObjectNode responseJSON = this.objectMapper.createObjectNode();
/*     */     
/*  54 */     responseJSON.put("processInstanceId", processInstanceId);
/*     */     
/*  56 */     ArrayNode activitiesArray = this.objectMapper.createArrayNode();
/*  57 */     ArrayNode flowsArray = this.objectMapper.createArrayNode();
/*     */     
/*     */     try {
/*  60 */       ProcessInstance processInstance = (ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
/*  61 */       ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)this.repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
/*     */       
/*  63 */       responseJSON.put("processDefinitionId", processInstance.getProcessDefinitionId());
/*     */       
/*  65 */       List<String> highLightedActivities = this.runtimeService.getActiveActivityIds(processInstanceId);
/*  66 */       List<String> highLightedFlows = getHighLightedFlows(processDefinition, processInstanceId);
/*     */       
/*  68 */       for (String activityId : highLightedActivities) {
/*  69 */         activitiesArray.add(activityId);
/*     */       }
/*     */       
/*  72 */       for (String flow : highLightedFlows) {
/*  73 */         flowsArray.add(flow);
/*     */       }
/*     */     }
/*  76 */     catch (Exception e) {
/*  77 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  80 */     responseJSON.put("activities", (JsonNode)activitiesArray);
/*  81 */     responseJSON.put("flows", (JsonNode)flowsArray);
/*     */     
/*  83 */     return responseJSON;
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
/*     */   private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
/*  96 */     List<String> highLightedFlows = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     List<HistoricActivityInstance> historicActivityInstances = this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
/*     */     
/* 104 */     LinkedList<HistoricActivityInstance> hisActInstList = new LinkedList<>();
/* 105 */     hisActInstList.addAll(historicActivityInstances);
/*     */     
/* 107 */     getHighlightedFlows(processDefinition.getActivities(), hisActInstList, highLightedFlows);
/*     */     
/* 109 */     return highLightedFlows;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getHighlightedFlows(List<ActivityImpl> activityList, LinkedList<HistoricActivityInstance> hisActInstList, List<String> highLightedFlows) {
/* 127 */     List<ActivityImpl> startEventActList = new ArrayList<>();
/* 128 */     Map<String, ActivityImpl> activityMap = new HashMap<>(activityList.size());
/* 129 */     for (ActivityImpl activity : activityList) {
/*     */       
/* 131 */       activityMap.put(activity.getId(), activity);
/*     */       
/* 133 */       String actType = (String)activity.getProperty("type");
/* 134 */       if (actType != null && actType.toLowerCase().indexOf("startevent") >= 0) {
/* 135 */         startEventActList.add(activity);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     HistoricActivityInstance firstHistActInst = hisActInstList.getFirst();
/* 144 */     String firstActType = firstHistActInst.getActivityType();
/* 145 */     if (firstActType != null && firstActType.toLowerCase().indexOf("startevent") < 0) {
/* 146 */       PvmTransition startTrans = getStartTransaction(startEventActList, firstHistActInst);
/* 147 */       if (startTrans != null) {
/* 148 */         highLightedFlows.add(startTrans.getId());
/*     */       }
/*     */     } 
/*     */     
/* 152 */     while (!hisActInstList.isEmpty()) {
/* 153 */       HistoricActivityInstance histActInst = hisActInstList.removeFirst();
/* 154 */       ActivityImpl activity = activityMap.get(histActInst.getActivityId());
/* 155 */       if (activity != null) {
/* 156 */         boolean isParallel = false;
/* 157 */         String type = histActInst.getActivityType();
/* 158 */         if ("parallelGateway".equals(type) || "inclusiveGateway".equals(type)) {
/* 159 */           isParallel = true;
/* 160 */         } else if ("subProcess".equals(histActInst.getActivityType())) {
/* 161 */           getHighlightedFlows(activity.getActivities(), hisActInstList, highLightedFlows);
/*     */         } 
/*     */         
/* 164 */         List<PvmTransition> allOutgoingTrans = new ArrayList<>();
/* 165 */         allOutgoingTrans.addAll(activity.getOutgoingTransitions());
/* 166 */         allOutgoingTrans.addAll(getBoundaryEventOutgoingTransitions(activity));
/* 167 */         List<String> activityHighLightedFlowIds = getHighlightedFlows(allOutgoingTrans, hisActInstList, isParallel);
/* 168 */         highLightedFlows.addAll(activityHighLightedFlowIds);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PvmTransition getStartTransaction(List<ActivityImpl> startEventActList, HistoricActivityInstance firstActInst) {
/* 181 */     for (ActivityImpl startEventAct : startEventActList) {
/* 182 */       for (PvmTransition trans : startEventAct.getOutgoingTransitions()) {
/* 183 */         if (trans.getDestination().getId().equals(firstActInst.getActivityId())) {
/* 184 */           return trans;
/*     */         }
/*     */       } 
/*     */     } 
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<PvmTransition> getBoundaryEventOutgoingTransitions(ActivityImpl activity) {
/* 198 */     List<PvmTransition> boundaryTrans = new ArrayList<>();
/* 199 */     for (ActivityImpl subActivity : activity.getActivities()) {
/* 200 */       String type = (String)subActivity.getProperty("type");
/* 201 */       if (type != null && type.toLowerCase().indexOf("boundary") >= 0) {
/* 202 */         boundaryTrans.addAll(subActivity.getOutgoingTransitions());
/*     */       }
/*     */     } 
/* 205 */     return boundaryTrans;
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
/*     */   private List<String> getHighlightedFlows(List<PvmTransition> pvmTransitionList, LinkedList<HistoricActivityInstance> hisActInstList, boolean isParallel) {
/* 218 */     List<String> highLightedFlowIds = new ArrayList<>();
/*     */     
/* 220 */     PvmTransition earliestTrans = null;
/* 221 */     HistoricActivityInstance earliestHisActInst = null;
/*     */     
/* 223 */     for (PvmTransition pvmTransition : pvmTransitionList) {
/*     */       
/* 225 */       String destActId = pvmTransition.getDestination().getId();
/* 226 */       HistoricActivityInstance destHisActInst = findHisActInst(hisActInstList, destActId);
/* 227 */       if (destHisActInst != null) {
/* 228 */         if (isParallel) {
/* 229 */           highLightedFlowIds.add(pvmTransition.getId()); continue;
/* 230 */         }  if (earliestHisActInst == null || earliestHisActInst.getId().compareTo(destHisActInst.getId()) > 0) {
/* 231 */           earliestTrans = pvmTransition;
/* 232 */           earliestHisActInst = destHisActInst;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 237 */     if (!isParallel && earliestTrans != null) {
/* 238 */       highLightedFlowIds.add(earliestTrans.getId());
/*     */     }
/*     */     
/* 241 */     return highLightedFlowIds;
/*     */   }
/*     */   
/*     */   private HistoricActivityInstance findHisActInst(LinkedList<HistoricActivityInstance> hisActInstList, String actId) {
/* 245 */     for (HistoricActivityInstance hisActInst : hisActInstList) {
/* 246 */       if (hisActInst.getActivityId().equals(actId)) {
/* 247 */         return hisActInst;
/*     */       }
/*     */     } 
/* 250 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/activiti/rest/diagram/services/ProcessInstanceHighlightsResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */