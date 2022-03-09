/*     */ package com.dstz.activiti.rest.diagram.services;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.bpmn.model.ErrorEventDefinition;
/*     */ import org.activiti.engine.ActivitiException;
/*     */ import org.activiti.engine.HistoryService;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.RuntimeService;
/*     */ import org.activiti.engine.history.HistoricActivityInstance;
/*     */ import org.activiti.engine.history.HistoricActivityInstanceQuery;
/*     */ import org.activiti.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
/*     */ import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
/*     */ import org.activiti.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
/*     */ import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*     */ import org.activiti.engine.impl.pvm.PvmTransition;
/*     */ import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
/*     */ import org.activiti.engine.impl.pvm.process.ActivityImpl;
/*     */ import org.activiti.engine.impl.pvm.process.Lane;
/*     */ import org.activiti.engine.impl.pvm.process.LaneSet;
/*     */ import org.activiti.engine.impl.pvm.process.ParticipantProcess;
/*     */ import org.activiti.engine.impl.pvm.process.TransitionImpl;
/*     */ import org.activiti.engine.repository.ProcessDefinition;
/*     */ import org.activiti.engine.runtime.Execution;
/*     */ import org.activiti.engine.runtime.ProcessInstance;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class BaseProcessDefinitionDiagramLayoutResource
/*     */ {
/*     */   @Autowired
/*     */   private RuntimeService runtimeService;
/*     */   @Autowired
/*     */   private RepositoryService repositoryService;
/*     */   @Autowired
/*     */   private HistoryService historyService;
/*     */   
/*     */   public ObjectNode getDiagramNode(String processInstanceId, String processDefinitionId) {
/*  60 */     List<String> highLightedFlows = Collections.emptyList();
/*  61 */     List<String> highLightedActivities = Collections.emptyList();
/*     */     
/*  63 */     Map<String, ObjectNode> subProcessInstanceMap = new HashMap<>();
/*     */     
/*  65 */     ProcessInstance processInstance = null;
/*  66 */     if (processInstanceId != null) {
/*  67 */       processInstance = (ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
/*  68 */       if (processInstance == null) {
/*  69 */         throw new RuntimeException("Process instance could not be found");
/*     */       }
/*  71 */       processDefinitionId = processInstance.getProcessDefinitionId();
/*     */ 
/*     */ 
/*     */       
/*  75 */       List<ProcessInstance> subProcessInstances = this.runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
/*     */       
/*  77 */       for (ProcessInstance subProcessInstance : subProcessInstances) {
/*  78 */         String subDefId = subProcessInstance.getProcessDefinitionId();
/*     */ 
/*     */         
/*  81 */         String superExecutionId = ((ExecutionEntity)subProcessInstance).getSuperExecutionId();
/*  82 */         ProcessDefinitionEntity subDef = (ProcessDefinitionEntity)this.repositoryService.getProcessDefinition(subDefId);
/*     */         
/*  84 */         ObjectNode processInstanceJSON = (new ObjectMapper()).createObjectNode();
/*  85 */         processInstanceJSON.put("processInstanceId", subProcessInstance.getId());
/*  86 */         processInstanceJSON.put("superExecutionId", superExecutionId);
/*  87 */         processInstanceJSON.put("processDefinitionId", subDef.getId());
/*  88 */         processInstanceJSON.put("processDefinitionKey", subDef.getKey());
/*  89 */         processInstanceJSON.put("processDefinitionName", subDef.getName());
/*     */         
/*  91 */         subProcessInstanceMap.put(superExecutionId, processInstanceJSON);
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (processDefinitionId == null) {
/*  96 */       throw new RuntimeException("No process definition id provided");
/*     */     }
/*     */     
/*  99 */     ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)this.repositoryService.getProcessDefinition(processDefinitionId);
/*     */     
/* 101 */     if (processDefinition == null) {
/* 102 */       throw new ActivitiException("Process definition " + processDefinitionId + " could not be found");
/*     */     }
/*     */     
/* 105 */     ObjectNode responseJSON = (new ObjectMapper()).createObjectNode();
/*     */ 
/*     */     
/* 108 */     JsonNode pdrJSON = getProcessDefinitionResponse(processDefinition);
/*     */     
/* 110 */     if (pdrJSON != null) {
/* 111 */       responseJSON.put("processDefinition", pdrJSON);
/*     */     }
/*     */ 
/*     */     
/* 115 */     if (processInstance != null) {
/* 116 */       ArrayNode arrayNode1 = (new ObjectMapper()).createArrayNode();
/* 117 */       ArrayNode flowsArray = (new ObjectMapper()).createArrayNode();
/*     */       
/* 119 */       highLightedActivities = this.runtimeService.getActiveActivityIds(processInstanceId);
/* 120 */       highLightedFlows = getHighLightedFlows(processInstanceId, processDefinition);
/*     */       
/* 122 */       for (String activityName : highLightedActivities) {
/* 123 */         arrayNode1.add(activityName);
/*     */       }
/*     */       
/* 126 */       for (String flow : highLightedFlows) {
/* 127 */         flowsArray.add(flow);
/*     */       }
/* 129 */       responseJSON.put("highLightedActivities", (JsonNode)arrayNode1);
/* 130 */       responseJSON.put("highLightedFlows", (JsonNode)flowsArray);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (processDefinition.getParticipantProcess() != null) {
/* 135 */       ParticipantProcess pProc = processDefinition.getParticipantProcess();
/*     */       
/* 137 */       ObjectNode participantProcessJSON = (new ObjectMapper()).createObjectNode();
/* 138 */       participantProcessJSON.put("id", pProc.getId());
/* 139 */       if (StringUtils.isNotEmpty(pProc.getName())) {
/* 140 */         participantProcessJSON.put("name", pProc.getName());
/*     */       } else {
/* 142 */         participantProcessJSON.put("name", "");
/*     */       } 
/* 144 */       participantProcessJSON.put("x", pProc.getX());
/* 145 */       participantProcessJSON.put("y", pProc.getY());
/* 146 */       participantProcessJSON.put("width", pProc.getWidth());
/* 147 */       participantProcessJSON.put("height", pProc.getHeight());
/*     */       
/* 149 */       responseJSON.put("participantProcess", (JsonNode)participantProcessJSON);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (processDefinition.getLaneSets() != null && !processDefinition.getLaneSets().isEmpty()) {
/* 155 */       ArrayNode laneSetArray = (new ObjectMapper()).createArrayNode();
/* 156 */       for (LaneSet laneSet : processDefinition.getLaneSets()) {
/* 157 */         ArrayNode laneArray = (new ObjectMapper()).createArrayNode();
/* 158 */         if (laneSet.getLanes() != null && !laneSet.getLanes().isEmpty()) {
/* 159 */           for (Lane lane : laneSet.getLanes()) {
/* 160 */             ObjectNode laneJSON = (new ObjectMapper()).createObjectNode();
/* 161 */             laneJSON.put("id", lane.getId());
/* 162 */             if (StringUtils.isNotEmpty(lane.getName())) {
/* 163 */               laneJSON.put("name", lane.getName());
/*     */             } else {
/* 165 */               laneJSON.put("name", "");
/*     */             } 
/* 167 */             laneJSON.put("x", lane.getX());
/* 168 */             laneJSON.put("y", lane.getY());
/* 169 */             laneJSON.put("width", lane.getWidth());
/* 170 */             laneJSON.put("height", lane.getHeight());
/*     */             
/* 172 */             List<String> flowNodeIds = lane.getFlowNodeIds();
/* 173 */             ArrayNode flowNodeIdsArray = (new ObjectMapper()).createArrayNode();
/* 174 */             for (String flowNodeId : flowNodeIds) {
/* 175 */               flowNodeIdsArray.add(flowNodeId);
/*     */             }
/* 177 */             laneJSON.put("flowNodeIds", (JsonNode)flowNodeIdsArray);
/*     */             
/* 179 */             laneArray.add((JsonNode)laneJSON);
/*     */           } 
/*     */         }
/* 182 */         ObjectNode laneSetJSON = (new ObjectMapper()).createObjectNode();
/* 183 */         laneSetJSON.put("id", laneSet.getId());
/* 184 */         if (StringUtils.isNotEmpty(laneSet.getName())) {
/* 185 */           laneSetJSON.put("name", laneSet.getName());
/*     */         } else {
/* 187 */           laneSetJSON.put("name", "");
/*     */         } 
/* 189 */         laneSetJSON.put("lanes", (JsonNode)laneArray);
/*     */         
/* 191 */         laneSetArray.add((JsonNode)laneSetJSON);
/*     */       } 
/*     */       
/* 194 */       if (laneSetArray.size() > 0) {
/* 195 */         responseJSON.put("laneSets", (JsonNode)laneSetArray);
/*     */       }
/*     */     } 
/* 198 */     ArrayNode sequenceFlowArray = (new ObjectMapper()).createArrayNode();
/* 199 */     ArrayNode activityArray = (new ObjectMapper()).createArrayNode();
/*     */ 
/*     */ 
/*     */     
/* 203 */     for (ActivityImpl activity : processDefinition.getActivities()) {
/* 204 */       getActivity(processInstanceId, activity, activityArray, sequenceFlowArray, processInstance, highLightedFlows, subProcessInstanceMap);
/*     */     }
/*     */ 
/*     */     
/* 208 */     responseJSON.put("activities", (JsonNode)activityArray);
/* 209 */     responseJSON.put("sequenceFlows", (JsonNode)sequenceFlowArray);
/*     */     
/* 211 */     return responseJSON;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> getHighLightedFlows(String processInstanceId, ProcessDefinitionEntity processDefinition) {
/* 216 */     List<String> highLightedFlows = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 220 */     List<HistoricActivityInstance> historicActivityInstances = ((HistoricActivityInstanceQuery)this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc()).list();
/*     */     
/* 222 */     List<String> historicActivityInstanceList = new ArrayList<>();
/* 223 */     for (HistoricActivityInstance hai : historicActivityInstances) {
/* 224 */       historicActivityInstanceList.add(hai.getActivityId());
/*     */     }
/*     */ 
/*     */     
/* 228 */     List<String> highLightedActivities = this.runtimeService.getActiveActivityIds(processInstanceId);
/* 229 */     historicActivityInstanceList.addAll(highLightedActivities);
/*     */ 
/*     */     
/* 232 */     for (ActivityImpl activity : processDefinition.getActivities()) {
/* 233 */       int index = historicActivityInstanceList.indexOf(activity.getId());
/*     */       
/* 235 */       if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
/*     */         
/* 237 */         List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
/* 238 */         for (PvmTransition pvmTransition : pvmTransitionList) {
/* 239 */           String destinationFlowId = pvmTransition.getDestination().getId();
/* 240 */           if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
/* 241 */             highLightedFlows.add(pvmTransition.getId());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 246 */     return highLightedFlows;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getActivity(String processInstanceId, ActivityImpl activity, ArrayNode activityArray, ArrayNode sequenceFlowArray, ProcessInstance processInstance, List<String> highLightedFlows, Map<String, ObjectNode> subProcessInstanceMap) {
/* 253 */     ObjectNode activityJSON = (new ObjectMapper()).createObjectNode();
/*     */ 
/*     */     
/* 256 */     String multiInstance = (String)activity.getProperty("multiInstance");
/* 257 */     if (multiInstance != null && 
/* 258 */       !"sequential".equals(multiInstance)) {
/* 259 */       multiInstance = "parallel";
/*     */     }
/*     */ 
/*     */     
/* 263 */     ActivityBehavior activityBehavior = activity.getActivityBehavior();
/*     */     
/* 265 */     Boolean collapsed = Boolean.valueOf(activityBehavior instanceof CallActivityBehavior);
/* 266 */     Boolean expanded = (Boolean)activity.getProperty("isExpanded");
/* 267 */     if (expanded != null) {
/* 268 */       collapsed = Boolean.valueOf(!expanded.booleanValue());
/*     */     }
/*     */     
/* 271 */     Boolean isInterrupting = null;
/* 272 */     if (activityBehavior instanceof BoundaryEventActivityBehavior) {
/* 273 */       isInterrupting = Boolean.valueOf(((BoundaryEventActivityBehavior)activityBehavior).isInterrupting());
/*     */     }
/*     */ 
/*     */     
/* 277 */     for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
/* 278 */       String flowName = (String)sequenceFlow.getProperty("name");
/* 279 */       boolean isHighLighted = highLightedFlows.contains(sequenceFlow.getId());
/*     */       
/* 281 */       boolean isConditional = (sequenceFlow.getProperty("condition") != null && !((String)activity.getProperty("type")).toLowerCase().contains("gateway"));
/*     */       
/* 283 */       boolean isDefault = (sequenceFlow.getId().equals(activity.getProperty("default")) && ((String)activity.getProperty("type")).toLowerCase().contains("gateway"));
/*     */       
/* 285 */       List<Integer> waypoints = ((TransitionImpl)sequenceFlow).getWaypoints();
/* 286 */       ArrayNode xPointArray = (new ObjectMapper()).createArrayNode();
/* 287 */       ArrayNode yPointArray = (new ObjectMapper()).createArrayNode();
/* 288 */       for (int i = 0; i < waypoints.size(); i += 2) {
/*     */ 
/*     */         
/* 291 */         xPointArray.add(waypoints.get(i));
/* 292 */         yPointArray.add(waypoints.get(i + 1));
/*     */       } 
/*     */       
/* 295 */       ObjectNode flowJSON = (new ObjectMapper()).createObjectNode();
/* 296 */       flowJSON.put("id", sequenceFlow.getId());
/* 297 */       flowJSON.put("name", flowName);
/* 298 */       flowJSON.put("flow", "(" + sequenceFlow.getSource().getId() + ")--" + sequenceFlow
/* 299 */           .getId() + "-->(" + sequenceFlow
/* 300 */           .getDestination().getId() + ")");
/*     */       
/* 302 */       if (isConditional)
/* 303 */         flowJSON.put("isConditional", isConditional); 
/* 304 */       if (isDefault)
/* 305 */         flowJSON.put("isDefault", isDefault); 
/* 306 */       if (isHighLighted) {
/* 307 */         flowJSON.put("isHighLighted", isHighLighted);
/*     */       }
/* 309 */       flowJSON.put("xPointArray", (JsonNode)xPointArray);
/* 310 */       flowJSON.put("yPointArray", (JsonNode)yPointArray);
/*     */       
/* 312 */       sequenceFlowArray.add((JsonNode)flowJSON);
/*     */     } 
/*     */ 
/*     */     
/* 316 */     ArrayNode nestedActivityArray = (new ObjectMapper()).createArrayNode();
/* 317 */     for (ActivityImpl nestedActivity : activity.getActivities()) {
/* 318 */       nestedActivityArray.add(nestedActivity.getId());
/*     */     }
/*     */     
/* 321 */     Map<String, Object> properties = activity.getProperties();
/* 322 */     ObjectNode propertiesJSON = (new ObjectMapper()).createObjectNode();
/* 323 */     for (String key : properties.keySet()) {
/* 324 */       Object prop = properties.get(key);
/* 325 */       if (prop instanceof String) {
/* 326 */         propertiesJSON.put(key, (String)properties.get(key)); continue;
/* 327 */       }  if (prop instanceof Integer) {
/* 328 */         propertiesJSON.put(key, (Integer)properties.get(key)); continue;
/* 329 */       }  if (prop instanceof Boolean) {
/* 330 */         propertiesJSON.put(key, (Boolean)properties.get(key)); continue;
/* 331 */       }  if ("initial".equals(key)) {
/* 332 */         ActivityImpl act = (ActivityImpl)properties.get(key);
/* 333 */         propertiesJSON.put(key, act.getId()); continue;
/* 334 */       }  if ("timerDeclarations".equals(key)) {
/* 335 */         ArrayList<TimerDeclarationImpl> timerDeclarations = (ArrayList<TimerDeclarationImpl>)properties.get(key);
/* 336 */         ArrayNode timerDeclarationArray = (new ObjectMapper()).createArrayNode();
/*     */         
/* 338 */         if (timerDeclarations != null)
/* 339 */           for (TimerDeclarationImpl timerDeclaration : timerDeclarations) {
/* 340 */             ObjectNode timerDeclarationJSON = (new ObjectMapper()).createObjectNode();
/*     */             
/* 342 */             timerDeclarationJSON.put("isExclusive", timerDeclaration.isExclusive());
/* 343 */             if (timerDeclaration.getRepeat() != null) {
/* 344 */               timerDeclarationJSON.put("repeat", timerDeclaration.getRepeat());
/*     */             }
/* 346 */             timerDeclarationJSON.put("retries", String.valueOf(timerDeclaration.getRetries()));
/* 347 */             timerDeclarationJSON.put("type", timerDeclaration.getJobHandlerType());
/* 348 */             timerDeclarationJSON.put("configuration", timerDeclaration.getJobHandlerConfiguration());
/*     */ 
/*     */             
/* 351 */             timerDeclarationArray.add((JsonNode)timerDeclarationJSON);
/*     */           }  
/* 353 */         if (timerDeclarationArray.size() > 0)
/* 354 */           propertiesJSON.put(key, (JsonNode)timerDeclarationArray);  continue;
/*     */       } 
/* 356 */       if ("eventDefinitions".equals(key)) {
/* 357 */         ArrayList<EventSubscriptionDeclaration> eventDefinitions = (ArrayList<EventSubscriptionDeclaration>)properties.get(key);
/* 358 */         ArrayNode eventDefinitionsArray = (new ObjectMapper()).createArrayNode();
/*     */         
/* 360 */         if (eventDefinitions != null) {
/* 361 */           for (EventSubscriptionDeclaration eventDefinition : eventDefinitions) {
/* 362 */             ObjectNode eventDefinitionJSON = (new ObjectMapper()).createObjectNode();
/*     */             
/* 364 */             if (eventDefinition.getActivityId() != null) {
/* 365 */               eventDefinitionJSON.put("activityId", eventDefinition.getActivityId());
/*     */             }
/* 367 */             eventDefinitionJSON.put("eventName", eventDefinition.getEventName());
/* 368 */             eventDefinitionJSON.put("eventType", eventDefinition.getEventType());
/* 369 */             eventDefinitionJSON.put("isAsync", eventDefinition.isAsync());
/* 370 */             eventDefinitionJSON.put("isStartEvent", eventDefinition.isStartEvent());
/* 371 */             eventDefinitionsArray.add((JsonNode)eventDefinitionJSON);
/*     */           } 
/*     */         }
/*     */         
/* 375 */         if (eventDefinitionsArray.size() > 0)
/* 376 */           propertiesJSON.put(key, (JsonNode)eventDefinitionsArray); 
/*     */         continue;
/*     */       } 
/* 379 */       if ("errorEventDefinitions".equals(key)) {
/* 380 */         ArrayList<ErrorEventDefinition> errorEventDefinitions = (ArrayList<ErrorEventDefinition>)properties.get(key);
/* 381 */         ArrayNode errorEventDefinitionsArray = (new ObjectMapper()).createArrayNode();
/*     */         
/* 383 */         if (errorEventDefinitions != null) {
/* 384 */           for (ErrorEventDefinition errorEventDefinition : errorEventDefinitions) {
/* 385 */             ObjectNode errorEventDefinitionJSON = (new ObjectMapper()).createObjectNode();
/*     */             
/* 387 */             if (errorEventDefinition.getErrorCode() != null) {
/* 388 */               errorEventDefinitionJSON.put("errorCode", errorEventDefinition.getErrorCode());
/*     */             } else {
/* 390 */               errorEventDefinitionJSON.putNull("errorCode");
/*     */             } 
/* 392 */             errorEventDefinitionJSON.put("handlerActivityId", errorEventDefinition
/* 393 */                 .getId());
/*     */             
/* 395 */             errorEventDefinitionsArray.add((JsonNode)errorEventDefinitionJSON);
/*     */           } 
/*     */         }
/*     */         
/* 399 */         if (errorEventDefinitionsArray.size() > 0) {
/* 400 */           propertiesJSON.put(key, (JsonNode)errorEventDefinitionsArray);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 405 */     if ("callActivity".equals(properties.get("type"))) {
/* 406 */       CallActivityBehavior callActivityBehavior = null;
/*     */       
/* 408 */       if (activityBehavior instanceof CallActivityBehavior) {
/* 409 */         callActivityBehavior = (CallActivityBehavior)activityBehavior;
/*     */       }
/*     */       
/* 412 */       if (callActivityBehavior != null) {
/* 413 */         propertiesJSON.put("processDefinitonKey", callActivityBehavior.getProcessDefinitonKey());
/*     */ 
/*     */ 
/*     */         
/* 417 */         ArrayNode processInstanceArray = (new ObjectMapper()).createArrayNode();
/* 418 */         if (processInstance != null) {
/*     */ 
/*     */           
/* 421 */           List<Execution> executionList = this.runtimeService.createExecutionQuery().processInstanceId(processInstanceId).activityId(activity.getId()).list();
/* 422 */           if (!executionList.isEmpty()) {
/* 423 */             for (Execution execution : executionList) {
/* 424 */               ObjectNode processInstanceJSON = subProcessInstanceMap.get(execution.getId());
/* 425 */               processInstanceArray.add((JsonNode)processInstanceJSON);
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 432 */         if (processInstanceArray.size() == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 437 */           ProcessDefinition lastProcessDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(callActivityBehavior.getProcessDefinitonKey()).latestVersion().singleResult();
/*     */ 
/*     */ 
/*     */           
/* 441 */           ObjectNode processInstanceJSON = (new ObjectMapper()).createObjectNode();
/* 442 */           processInstanceJSON.put("processDefinitionId", lastProcessDefinition.getId());
/* 443 */           processInstanceJSON.put("processDefinitionKey", lastProcessDefinition.getKey());
/* 444 */           processInstanceJSON.put("processDefinitionName", lastProcessDefinition.getName());
/* 445 */           processInstanceArray.add((JsonNode)processInstanceJSON);
/*     */         } 
/*     */         
/* 448 */         if (processInstanceArray.size() > 0) {
/* 449 */           propertiesJSON.put("processDefinitons", (JsonNode)processInstanceArray);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 454 */     activityJSON.put("activityId", activity.getId());
/* 455 */     activityJSON.put("properties", (JsonNode)propertiesJSON);
/* 456 */     if (multiInstance != null)
/* 457 */       activityJSON.put("multiInstance", multiInstance); 
/* 458 */     if (collapsed.booleanValue())
/* 459 */       activityJSON.put("collapsed", collapsed); 
/* 460 */     if (nestedActivityArray.size() > 0)
/* 461 */       activityJSON.put("nestedActivities", (JsonNode)nestedActivityArray); 
/* 462 */     if (isInterrupting != null) {
/* 463 */       activityJSON.put("isInterrupting", isInterrupting);
/*     */     }
/* 465 */     activityJSON.put("x", activity.getX());
/* 466 */     activityJSON.put("y", activity.getY());
/* 467 */     activityJSON.put("width", activity.getWidth());
/* 468 */     activityJSON.put("height", activity.getHeight());
/*     */     
/* 470 */     activityArray.add((JsonNode)activityJSON);
/*     */ 
/*     */     
/* 473 */     for (ActivityImpl nestedActivity : activity.getActivities()) {
/* 474 */       getActivity(processInstanceId, nestedActivity, activityArray, sequenceFlowArray, processInstance, highLightedFlows, subProcessInstanceMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private JsonNode getProcessDefinitionResponse(ProcessDefinitionEntity processDefinition) {
/* 480 */     ObjectMapper mapper = new ObjectMapper();
/* 481 */     ObjectNode pdrJSON = mapper.createObjectNode();
/* 482 */     pdrJSON.put("id", processDefinition.getId());
/* 483 */     pdrJSON.put("name", processDefinition.getName());
/* 484 */     pdrJSON.put("key", processDefinition.getKey());
/* 485 */     pdrJSON.put("version", processDefinition.getVersion());
/* 486 */     pdrJSON.put("deploymentId", processDefinition.getDeploymentId());
/* 487 */     pdrJSON.put("isGraphicNotationDefined", isGraphicNotationDefined(processDefinition));
/* 488 */     return (JsonNode)pdrJSON;
/*     */   }
/*     */   
/*     */   private boolean isGraphicNotationDefined(ProcessDefinitionEntity processDefinition) {
/* 492 */     return ((ProcessDefinitionEntity)this.repositoryService
/* 493 */       .getProcessDefinition(processDefinition.getId()))
/* 494 */       .isGraphicalNotationDefined();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/activiti/rest/diagram/services/BaseProcessDefinitionDiagramLayoutResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */