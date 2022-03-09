/*     */ package cn.gwssi.ecloudbpm.wf.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.act.img.BpmProcessDiagramGenerator;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmImageService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.Paint;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.engine.ProcessEngineConfiguration;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class DefaultInstHistImgService
/*     */   implements BpmImageService
/*     */ {
/*     */   @Autowired
/*     */   private RepositoryService repositoryService;
/*     */   @Autowired
/*     */   private ProcessEngineConfiguration processEngineConfiguration;
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Autowired
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public InputStream draw(String actDefId, String actInstId) throws Exception {
/*  42 */     if (StringUtil.isEmpty(actDefId)) {
/*  43 */       throw new BusinessException("流程定义actDefId不能缺失", BpmStatusCode.PARAM_ILLEGAL);
/*     */     }
/*  45 */     if (StringUtil.isEmpty(actInstId)) {
/*  46 */       return drawByDefId(actDefId);
/*     */     }
/*     */     
/*  49 */     Map<String, Paint> nodeMap = new HashMap<>();
/*  50 */     Map<String, Paint> flowMap = new HashMap<>();
/*  51 */     Map<String, Paint> gateMap = new HashMap<>();
/*  52 */     BpmInstance instance = this.bpmInstanceManager.getByActInstId(actInstId);
/*  53 */     List<BpmTaskStack> stacks = this.bpmTaskStackManager.getByInstanceId(instance.getId());
/*  54 */     Map<String, BpmTaskStack> stackMap = new HashMap<>();
/*  55 */     stacks.forEach(statck -> stackMap.put(statck.getId(), statck));
/*     */ 
/*     */ 
/*     */     
/*  59 */     for (BpmTaskStack stack : stacks) {
/*  60 */       if ("sequenceFlow".equals(stack.getNodeType())) {
/*     */         
/*  62 */         String actionName = stack.getActionName();
/*  63 */         if (actionName.equals(ActionType.CREATE.getKey())) {
/*  64 */           actionName = ActionType.START.getKey();
/*     */         }
/*  66 */         flowMap.put(stack.getNodeId(), getOpinionColar(actionName)); continue;
/*  67 */       }  if ("exclusiveGateway".equals(stack.getNodeType())) {
/*  68 */         gateMap.put(stack.getNodeId(), getOpinionColar(stack.getActionName())); continue;
/*     */       } 
/*  70 */       nodeMap.put(stack.getNodeId(), getOpinionColar(stack.getActionName()));
/*     */     } 
/*     */ 
/*     */     
/*  74 */     ThreadMapUtil.put("DefaultInstHistImgService_nodeMap", nodeMap);
/*  75 */     ThreadMapUtil.put("DefaultInstHistImgService_flowMap", flowMap);
/*  76 */     ThreadMapUtil.put("DefaultInstHistImgService_gateMap", gateMap);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     InputStream imageStream = null;
/*     */     
/*     */     try {
/*  84 */       BpmnModel bpmnModel = this.repositoryService.getBpmnModel(actDefId);
/*     */       
/*  86 */       BpmProcessDiagramGenerator diagramGenerator = new BpmProcessDiagramGenerator();
/*  87 */       imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", nodeMap, flowMap);
/*     */     } finally {
/*  89 */       IOUtils.closeQuietly(imageStream);
/*     */     } 
/*  91 */     return imageStream;
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
/*     */   private Paint getOpinionColar(String action) {
/* 103 */     if (StringUtil.isEmpty(action)) {
/* 104 */       return Color.GREEN;
/*     */     }
/* 106 */     ActionType status = ActionType.fromKey(action);
/* 107 */     if (status == null) {
/* 108 */       return Color.GREEN;
/*     */     }
/* 110 */     switch (status) {
/*     */       case START:
/* 112 */         return this.success;
/*     */       case AGREE:
/* 114 */         return this.success;
/*     */       case SIGNAGREE:
/* 116 */         return this.success;
/*     */       case OPPOSE:
/* 118 */         return Color.PINK;
/*     */       case SIGNOPPOSE:
/* 120 */         return Color.PINK;
/*     */       case REJECT:
/* 122 */         return Color.PINK;
/*     */       case REJECT2START:
/* 124 */         return Color.PINK;
/*     */       case RECOVER:
/* 126 */         return Color.PINK;
/*     */       case DISPENDSE:
/* 128 */         return Color.BLUE;
/*     */       case MANUALEND:
/* 130 */         return Color.DARK_GRAY;
/*     */       case RECALL:
/* 132 */         return Color.DARK_GRAY;
/*     */       case CREATE:
/* 134 */         return this.todo;
/*     */       case END:
/* 136 */         return this.success;
/*     */       case TASKCANCELLED:
/* 138 */         return this.cancelled;
/*     */     } 
/* 140 */     return Color.RED;
/*     */   }
/*     */ 
/*     */   
/* 144 */   private Color success = new Color(26, 179, 148);
/* 145 */   private Color cancelled = new Color(28, 132, 198);
/* 146 */   private Color todo = new Color(237, 85, 101);
/*     */ 
/*     */   
/*     */   private InputStream drawByDefId(String actDefId) {
/* 150 */     BpmnModel bpmnModel = this.repositoryService.getBpmnModel(actDefId);
/* 151 */     return this.processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", this.processEngineConfiguration.getActivityFontName(), this.processEngineConfiguration.getLabelFontName(), this.processEngineConfiguration.getAnnotationFontName(), this.processEngineConfiguration.getClassLoader());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/service/DefaultInstHistImgService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */