/*     */ package com.dstz.bpm.act.img;
/*     */ 
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import java.awt.Paint;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.bpmn.model.Activity;
/*     */ import org.activiti.bpmn.model.Artifact;
/*     */ import org.activiti.bpmn.model.BaseElement;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.FlowElement;
/*     */ import org.activiti.bpmn.model.FlowElementsContainer;
/*     */ import org.activiti.bpmn.model.FlowNode;
/*     */ import org.activiti.bpmn.model.Gateway;
/*     */ import org.activiti.bpmn.model.GraphicInfo;
/*     */ import org.activiti.bpmn.model.Lane;
/*     */ import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
/*     */ import org.activiti.bpmn.model.Pool;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.bpmn.model.SequenceFlow;
/*     */ import org.activiti.bpmn.model.SubProcess;
/*     */ import org.activiti.engine.ProcessEngineConfiguration;
/*     */ import org.activiti.image.impl.DefaultProcessDiagramGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmProcessDiagramGenerator
/*     */   extends DefaultProcessDiagramGenerator
/*     */ {
/*     */   private static ProcessEngineConfiguration processEngineConfiguration;
/*     */   
/*     */   public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, Map<String, Paint> nodeMap, Map<String, Paint> flowMap) {
/*  57 */     System.out.println(nodeMap);
/*  58 */     System.out.println(flowMap);
/*  59 */     prepareBpmnModel(bpmnModel);
/*     */     
/*  61 */     BpmProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType, processEngineConfiguration().getActivityFontName(), processEngineConfiguration().getActivityFontName(), processEngineConfiguration().getAnnotationFontName(), processEngineConfiguration().getClassLoader());
/*     */ 
/*     */     
/*  64 */     for (Pool pool : bpmnModel.getPools()) {
/*  65 */       GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
/*  66 */       processDiagramCanvas.drawPoolOrLane(pool.getName(), graphicInfo);
/*     */     } 
/*     */ 
/*     */     
/*  70 */     for (Process process : bpmnModel.getProcesses()) {
/*  71 */       for (Lane lane : process.getLanes()) {
/*  72 */         GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
/*  73 */         processDiagramCanvas.drawPoolOrLane(lane.getName(), graphicInfo);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  78 */     for (Process process : bpmnModel.getProcesses()) {
/*  79 */       for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class)) {
/*  80 */         drawActivity(processDiagramCanvas, bpmnModel, flowNode, nodeMap, flowMap, 1.0D);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  85 */     for (Process process : bpmnModel.getProcesses()) {
/*     */       
/*  87 */       for (Artifact artifact : process.getArtifacts()) {
/*  88 */         drawArtifact(processDiagramCanvas, bpmnModel, artifact);
/*     */       }
/*     */       
/*  91 */       List<SubProcess> subProcesses = process.findFlowElementsOfType(SubProcess.class, true);
/*  92 */       if (subProcesses != null) {
/*  93 */         for (SubProcess subProcess : subProcesses) {
/*  94 */           for (Artifact subProcessArtifact : subProcess.getArtifacts()) {
/*  95 */             drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 101 */     return processDiagramCanvas.generateImage(imageType);
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawActivity(BpmProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode, Map<String, Paint> nodes, Map<String, Paint> flows, double scaleFactor) {
/* 106 */     ThreadMapUtil.put("BpmProcessDiagramGenerator_flowNode", flowNode);
/*     */     
/* 108 */     DefaultProcessDiagramGenerator.ActivityDrawInstruction drawInstruction = (DefaultProcessDiagramGenerator.ActivityDrawInstruction)this.activityDrawInstructions.get(flowNode.getClass());
/* 109 */     if (drawInstruction != null) {
/* 110 */       drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);
/*     */ 
/*     */       
/* 113 */       boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
/* 114 */       if (flowNode instanceof Activity) {
/* 115 */         Activity activity = (Activity)flowNode;
/* 116 */         MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
/* 117 */         if (multiInstanceLoopCharacteristics != null) {
/* 118 */           multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
/* 119 */           multiInstanceParallel = !multiInstanceSequential;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 124 */       GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
/* 125 */       if (flowNode instanceof SubProcess) {
/* 126 */         collapsed = (graphicInfo.getExpanded() != null && !graphicInfo.getExpanded().booleanValue());
/* 127 */       } else if (flowNode instanceof org.activiti.bpmn.model.CallActivity) {
/* 128 */         collapsed = true;
/*     */       } 
/*     */       
/* 131 */       if (scaleFactor == 1.0D)
/*     */       {
/* 133 */         processDiagramCanvas.drawActivityMarkers((int)graphicInfo.getX(), (int)graphicInfo.getY(), (int)graphicInfo.getWidth(), (int)graphicInfo.getHeight(), multiInstanceSequential, multiInstanceParallel, collapsed);
/*     */       }
/*     */ 
/*     */       
/* 137 */       if (nodes.keySet().contains(flowNode.getId())) {
/* 138 */         drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()), nodes.get(flowNode.getId()));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 144 */     for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
/* 145 */       boolean highLighted = flows.keySet().contains(sequenceFlow.getId());
/* 146 */       String defaultFlow = null;
/* 147 */       if (flowNode instanceof Activity) {
/* 148 */         defaultFlow = ((Activity)flowNode).getDefaultFlow();
/* 149 */       } else if (flowNode instanceof Gateway) {
/* 150 */         defaultFlow = ((Gateway)flowNode).getDefaultFlow();
/*     */       } 
/*     */       
/* 153 */       boolean isDefault = false;
/* 154 */       if (defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
/* 155 */         isDefault = true;
/*     */       }
/* 157 */       boolean drawConditionalIndicator = (sequenceFlow.getConditionExpression() != null && !(flowNode instanceof Gateway));
/*     */       
/* 159 */       String sourceRef = sequenceFlow.getSourceRef();
/* 160 */       String targetRef = sequenceFlow.getTargetRef();
/* 161 */       FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
/* 162 */       FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
/* 163 */       List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
/* 164 */       if (graphicInfoList != null && graphicInfoList.size() > 0) {
/* 165 */         graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, (BaseElement)sourceElement, (BaseElement)targetElement, graphicInfoList);
/* 166 */         int[] xPoints = new int[graphicInfoList.size()];
/* 167 */         int[] yPoints = new int[graphicInfoList.size()];
/*     */         
/* 169 */         for (int i = 1; i < graphicInfoList.size(); i++) {
/* 170 */           GraphicInfo graphicInfo = graphicInfoList.get(i);
/* 171 */           GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);
/*     */           
/* 173 */           if (i == 1) {
/* 174 */             xPoints[0] = (int)previousGraphicInfo.getX();
/* 175 */             yPoints[0] = (int)previousGraphicInfo.getY();
/*     */           } 
/* 177 */           xPoints[i] = (int)graphicInfo.getX();
/* 178 */           yPoints[i] = (int)graphicInfo.getY();
/*     */         } 
/*     */ 
/*     */         
/* 182 */         processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault, highLighted, flows.get(sequenceFlow.getId()), scaleFactor);
/*     */ 
/*     */ 
/*     */         
/* 186 */         if (StringUtil.isNotEmpty(sequenceFlow.getName()) && !sequenceFlow.getName().startsWith("连线")) {
/* 187 */           GraphicInfo info = new GraphicInfo();
/* 188 */           info.setX(((xPoints[0] + xPoints[1]) / 2));
/* 189 */           info.setY(((yPoints[0] + yPoints[1]) / 2 - 15));
/* 190 */           processDiagramCanvas.drawLabel(sequenceFlow.getName(), info, false);
/*     */         } 
/*     */ 
/*     */         
/* 194 */         GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
/* 195 */         if (labelGraphicInfo != null) {
/* 196 */           processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 202 */     if (flowNode instanceof FlowElementsContainer) {
/* 203 */       for (FlowElement nestedFlowElement : ((FlowElementsContainer)flowNode).getFlowElements()) {
/* 204 */         if (nestedFlowElement instanceof FlowNode) {
/* 205 */           drawActivity(processDiagramCanvas, bpmnModel, (FlowNode)nestedFlowElement, nodes, flows, scaleFactor);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 211 */     if (flowNode instanceof org.activiti.bpmn.model.ExclusiveGateway) {
/* 212 */       GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
/* 213 */       GraphicInfo info = new GraphicInfo();
/* 214 */       info.setX(graphicInfo.getX() + 40.0D);
/* 215 */       info.setY(graphicInfo.getY() + 40.0D);
/* 216 */       processDiagramCanvas.drawLabel(flowNode.getName(), info, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawHighLight(BpmProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo, Paint paint) {
/* 222 */     processDiagramCanvas.drawHighLight((int)graphicInfo.getX(), (int)graphicInfo.getY(), (int)graphicInfo.getWidth(), (int)graphicInfo.getHeight(), paint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static BpmProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
/* 229 */     double minX = Double.MAX_VALUE;
/* 230 */     double maxX = 0.0D;
/* 231 */     double minY = Double.MAX_VALUE;
/* 232 */     double maxY = 0.0D;
/*     */     
/* 234 */     for (Pool pool : bpmnModel.getPools()) {
/* 235 */       GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
/* 236 */       minX = graphicInfo.getX();
/* 237 */       maxX = graphicInfo.getX() + graphicInfo.getWidth();
/* 238 */       minY = graphicInfo.getY();
/* 239 */       maxY = graphicInfo.getY() + graphicInfo.getHeight();
/*     */     } 
/*     */     
/* 242 */     List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
/* 243 */     for (FlowNode flowNode : flowNodes) {
/*     */       
/* 245 */       GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
/*     */ 
/*     */       
/* 248 */       if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
/* 249 */         maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
/*     */       }
/* 251 */       if (flowNodeGraphicInfo.getX() < minX) {
/* 252 */         minX = flowNodeGraphicInfo.getX();
/*     */       }
/*     */       
/* 255 */       if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
/* 256 */         maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
/*     */       }
/* 258 */       if (flowNodeGraphicInfo.getY() < minY) {
/* 259 */         minY = flowNodeGraphicInfo.getY();
/*     */       }
/*     */       
/* 262 */       for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
/* 263 */         List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
/* 264 */         if (graphicInfoList != null) {
/* 265 */           for (GraphicInfo graphicInfo : graphicInfoList) {
/*     */             
/* 267 */             if (graphicInfo.getX() > maxX) {
/* 268 */               maxX = graphicInfo.getX();
/*     */             }
/* 270 */             if (graphicInfo.getX() < minX) {
/* 271 */               minX = graphicInfo.getX();
/*     */             }
/*     */             
/* 274 */             if (graphicInfo.getY() > maxY) {
/* 275 */               maxY = graphicInfo.getY();
/*     */             }
/* 277 */             if (graphicInfo.getY() < minY) {
/* 278 */               minY = graphicInfo.getY();
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
/* 286 */     for (Artifact artifact : artifacts) {
/*     */       
/* 288 */       GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());
/*     */       
/* 290 */       if (artifactGraphicInfo != null) {
/*     */         
/* 292 */         if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
/* 293 */           maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
/*     */         }
/* 295 */         if (artifactGraphicInfo.getX() < minX) {
/* 296 */           minX = artifactGraphicInfo.getX();
/*     */         }
/*     */         
/* 299 */         if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
/* 300 */           maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
/*     */         }
/* 302 */         if (artifactGraphicInfo.getY() < minY) {
/* 303 */           minY = artifactGraphicInfo.getY();
/*     */         }
/*     */       } 
/*     */       
/* 307 */       List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
/* 308 */       if (graphicInfoList != null) {
/* 309 */         for (GraphicInfo graphicInfo : graphicInfoList) {
/*     */           
/* 311 */           if (graphicInfo.getX() > maxX) {
/* 312 */             maxX = graphicInfo.getX();
/*     */           }
/* 314 */           if (graphicInfo.getX() < minX) {
/* 315 */             minX = graphicInfo.getX();
/*     */           }
/*     */           
/* 318 */           if (graphicInfo.getY() > maxY) {
/* 319 */             maxY = graphicInfo.getY();
/*     */           }
/* 321 */           if (graphicInfo.getY() < minY) {
/* 322 */             minY = graphicInfo.getY();
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 328 */     int nrOfLanes = 0;
/* 329 */     for (Process process : bpmnModel.getProcesses()) {
/* 330 */       for (Lane l : process.getLanes()) {
/*     */         
/* 332 */         nrOfLanes++;
/*     */         
/* 334 */         GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
/*     */         
/* 336 */         if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
/* 337 */           maxX = graphicInfo.getX() + graphicInfo.getWidth();
/*     */         }
/* 339 */         if (graphicInfo.getX() < minX) {
/* 340 */           minX = graphicInfo.getX();
/*     */         }
/*     */         
/* 343 */         if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
/* 344 */           maxY = graphicInfo.getY() + graphicInfo.getHeight();
/*     */         }
/* 346 */         if (graphicInfo.getY() < minY) {
/* 347 */           minY = graphicInfo.getY();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 353 */     if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
/*     */       
/* 355 */       minX = 0.0D;
/* 356 */       minY = 0.0D;
/*     */     } 
/*     */     
/* 359 */     return new BpmProcessDiagramCanvas((int)maxX + 10, (int)maxY + 10, (int)minX, (int)minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
/*     */   }
/*     */   
/*     */   private static ProcessEngineConfiguration processEngineConfiguration() {
/* 363 */     if (processEngineConfiguration == null) {
/* 364 */       processEngineConfiguration = (ProcessEngineConfiguration)AppUtil.getBean(ProcessEngineConfiguration.class);
/*     */     }
/* 366 */     return processEngineConfiguration;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/img/BpmProcessDiagramGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */