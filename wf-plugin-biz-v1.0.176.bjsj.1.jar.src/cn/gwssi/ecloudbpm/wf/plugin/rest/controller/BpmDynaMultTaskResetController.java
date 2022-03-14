/*     */ package com.dstz.bpm.plugin.rest.controller;
/*     */ 
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.recrease.handler.RecreaseTaskAction;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.druid.util.StringUtils;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RequestMapping({"/bpm"})
/*     */ @RestController
/*     */ public class BpmDynaMultTaskResetController
/*     */   extends ControllerTools {
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private RecreaseTaskAction recreaseTaskAction;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   @GetMapping({"/taskReset/getSignAndDynamicTaskList"})
/*     */   public ResultMsg<JSONObject> getSignAndDynamicTaskList(@RequestParam String taskId) {
/*  43 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/*  44 */     BpmTask bpmTask = new BpmTask();
/*  45 */     bpmTask.setId(bpmTaskOpinion.getTaskId());
/*  46 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(bpmTaskOpinion.getInstId());
/*  47 */     JSONObject resultJson = new JSONObject();
/*  48 */     JSONObject dynamicJsonObject = this.recreaseTaskAction.getExistAliveTask((IBpmInstance)bpmInstance, (IBpmTask)bpmTask);
/*  49 */     JSONObject tasks = dynamicJsonObject.getJSONObject("tasks");
/*  50 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTaskOpinion.getTaskKey());
/*  51 */     List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getOutcomeTaskNodes();
/*  52 */     BpmNodeDef inComeNode = null;
/*  53 */     if (tasks.keySet() != null && tasks.keySet().size() > 0) {
/*  54 */       Iterator<String> iterator = tasks.keySet().iterator(); if (iterator.hasNext()) { String nodeId = iterator.next();
/*  55 */         BpmNodeDef node = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
/*  56 */         if (node != null) {
/*  57 */           List<BpmNodeDef> inComes = node.getIncomeNodes();
/*  58 */           if (CollectionUtil.isNotEmpty(inComes)) {
/*  59 */             inComeNode = inComes.get(0);
/*     */           }
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/*  65 */     for (BpmNodeDef node : bpmNodeDefs) {
/*  66 */       String nodeId = node.getNodeId();
/*  67 */       String subNodeId = null;
/*  68 */       String defId = node.getBpmProcessDef().getProcessDefinitionId();
/*  69 */       if (nodeId.indexOf("&") > -1) {
/*  70 */         String[] info = nodeId.split("&");
/*  71 */         nodeId = info[0];
/*  72 */         subNodeId = info[1];
/*  73 */         node = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
/*     */       } 
/*  75 */       if (tasks.get(nodeId) == null) {
/*  76 */         List<BpmNodeDef> inComes = node.getIncomeNodes();
/*  77 */         if (CollectionUtil.isEmpty(inComes)) {
/*     */           continue;
/*     */         }
/*  80 */         if (!StringUtils.equals(((BpmNodeDef)inComes.get(0)).getNodeId(), inComeNode.getNodeId())) {
/*     */           continue;
/*     */         }
/*  83 */         DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)node.getPluginContext(DynamicTaskPluginContext.class);
/*  84 */         DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
/*  85 */         if (dynamicTaskPluginDef.getEnabled().booleanValue()) {
/*  86 */           JSONObject nodeInfo = new JSONObject();
/*  87 */           nodeInfo.put("defId", defId);
/*  88 */           nodeInfo.put("existAliveTask", Boolean.valueOf(false));
/*  89 */           nodeInfo.put("nodeId", nodeId);
/*  90 */           if (subNodeId != null) {
/*  91 */             nodeInfo.put("nodeId", subNodeId);
/*     */           }
/*  93 */           nodeInfo.put("taskName", node.getName());
/*  94 */           nodeInfo.put("taskType", "dynamic");
/*  95 */           nodeInfo.put("tasks", new String[0]);
/*  96 */           tasks.put(nodeId, nodeInfo);
/*     */         } 
/*     */       } 
/*     */     } 
/* 100 */     dynamicJsonObject.put("tasks", tasks);
/* 101 */     return new ResultMsg(dynamicJsonObject);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmDynaMultTaskResetController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */