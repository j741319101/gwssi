/*     */ package cn.gwssi.ecloudbpm.wf.engine.parser;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.BaseBpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.GateWayBpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.ServiceTaskNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.UserTaskNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.activiti.bpmn.model.Activity;
/*     */ import org.activiti.bpmn.model.CallActivity;
/*     */ import org.activiti.bpmn.model.EndEvent;
/*     */ import org.activiti.bpmn.model.ExclusiveGateway;
/*     */ import org.activiti.bpmn.model.FlowElement;
/*     */ import org.activiti.bpmn.model.InclusiveGateway;
/*     */ import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
/*     */ import org.activiti.bpmn.model.ParallelGateway;
/*     */ import org.activiti.bpmn.model.SequenceFlow;
/*     */ import org.activiti.bpmn.model.ServiceTask;
/*     */ import org.activiti.bpmn.model.StartEvent;
/*     */ import org.activiti.bpmn.model.SubProcess;
/*     */ import org.activiti.bpmn.model.UserTask;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class BpmDefNodeHandler {
/*  39 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessDefNodes(DefaultBpmProcessDef bpmProcessDef, Collection<FlowElement> collection) {
/*  49 */     setProcessDefNodes(null, collection, bpmProcessDef);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setProcessDefNodes(BpmNodeDef parentNodeDef, Collection<FlowElement> flowElementList, DefaultBpmProcessDef bpmProcessDef) {
/*  54 */     Map<String, FlowElement> nodeMap = getNodeList(flowElementList);
/*     */     
/*  56 */     List<SequenceFlow> seqList = getSequenceFlowList(flowElementList);
/*     */     
/*  58 */     Map<String, BpmNodeDef> nodeDefMap = getBpmNodeDef(nodeMap, parentNodeDef, bpmProcessDef);
/*     */ 
/*     */     
/*  61 */     setRelateNodeDef(nodeDefMap, seqList);
/*     */ 
/*     */     
/*  64 */     List<BpmNodeDef> nodeDefList = new ArrayList<>(nodeDefMap.values());
/*     */ 
/*     */     
/*  67 */     bpmProcessDef.setBpmnNodeDefs(nodeDefList);
/*     */     
/*  69 */     for (BpmNodeDef nodeDef : nodeDefList) {
/*  70 */       BaseBpmNodeDef node = (BaseBpmNodeDef)nodeDef;
/*  71 */       node.setBpmProcessDef((BpmProcessDef)bpmProcessDef);
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
/*     */ 
/*     */   
/*     */   private Map<String, BpmNodeDef> getBpmNodeDef(Map<String, FlowElement> nodeMap, BpmNodeDef parentNodeDef, DefaultBpmProcessDef bpmProcessDef) {
/*  85 */     Map<String, BpmNodeDef> map = new HashMap<>();
/*  86 */     Set<Map.Entry<String, FlowElement>> set = nodeMap.entrySet();
/*  87 */     for (Iterator<Map.Entry<String, FlowElement>> it = set.iterator(); it.hasNext(); ) {
/*  88 */       Map.Entry<String, FlowElement> ent = it.next();
/*  89 */       FlowElement flowEl = ent.getValue();
/*     */       
/*  91 */       BaseBpmNodeDef nodeDef = getNodeDef(parentNodeDef, flowEl, bpmProcessDef);
/*     */       
/*  93 */       map.put(ent.getKey(), nodeDef);
/*     */     } 
/*  95 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRelateNodeDef(Map<String, BpmNodeDef> nodeDefMap, List<SequenceFlow> seqList) {
/* 106 */     for (SequenceFlow seq : seqList) {
/* 107 */       BpmNodeDef sourceDef = nodeDefMap.get(seq.getSourceRef());
/* 108 */       BpmNodeDef targetDef = nodeDefMap.get(seq.getTargetRef());
/*     */       
/* 110 */       if (sourceDef == null || targetDef == null) {
/* 111 */         this.LOG.warn("========待完善的节点类型==={}=====", seq.toString());
/*     */         continue;
/*     */       } 
/* 114 */       sourceDef.addOutcomeNode(targetDef);
/* 115 */       targetDef.addIncomeNode(sourceDef);
/*     */       
/* 117 */       if (sourceDef instanceof GateWayBpmNodeDef && 
/* 118 */         StringUtil.isNotEmpty(seq.getConditionExpression())) {
/* 119 */         ((GateWayBpmNodeDef)sourceDef).getOutGoingConditions().put(targetDef.getNodeId(), seq.getConditionExpression());
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
/*     */   private Map<String, FlowElement> getNodeList(Collection<FlowElement> flowElementList) {
/* 132 */     Map<String, FlowElement> map = new HashMap<>();
/* 133 */     for (FlowElement flowElement : flowElementList) {
/* 134 */       addNode(flowElement, map, (Class<? extends FlowElement>[])this.aryNodeElement);
/*     */     }
/* 136 */     return map;
/*     */   }
/*     */ 
/*     */   
/* 140 */   private Class<FlowElement>[] aryNodeElement = new Class[] { StartEvent.class, EndEvent.class, ParallelGateway.class, InclusiveGateway.class, ExclusiveGateway.class, UserTask.class, ServiceTask.class, CallActivity.class, SubProcess.class };
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
/*     */   
/*     */   private BaseBpmNodeDef getNodeDef(BpmNodeDef parentNodeDef, FlowElement flowElement, DefaultBpmProcessDef bpmProcessDef) {
/*     */     GateWayBpmNodeDef gateWayBpmNodeDef;
/* 168 */     BaseBpmNodeDef nodeDef = null;
/* 169 */     if (flowElement instanceof Activity) {
/* 170 */       String multi = getNodeDefLoop((Activity)flowElement);
/*     */       
/* 172 */       if (flowElement instanceof UserTask) {
/* 173 */         if (multi == null) {
/* 174 */           UserTaskNodeDef userTaskDef = new UserTaskNodeDef();
/* 175 */           UserTaskNodeDef userTaskNodeDef1 = userTaskDef;
/* 176 */           userTaskNodeDef1.setType(NodeType.USERTASK);
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 182 */       else if (flowElement instanceof ServiceTask) {
/* 183 */         ServiceTaskNodeDef serviceTaskNodeDef = new ServiceTaskNodeDef();
/* 184 */         serviceTaskNodeDef.setType(NodeType.SERVICETASK);
/*     */       
/*     */       }
/* 187 */       else if (flowElement instanceof CallActivity) {
/*     */         
/* 189 */         CallActivityNodeDef callNodeDef = new CallActivityNodeDef();
/* 190 */         CallActivity call = (CallActivity)flowElement;
/* 191 */         String flowKey = call.getCalledElement();
/* 192 */         callNodeDef.setType(NodeType.CALLACTIVITY);
/* 193 */         callNodeDef.setFlowKey(flowKey);
/*     */         
/* 195 */         CallActivityNodeDef callActivityNodeDef1 = callNodeDef;
/*     */ 
/*     */       
/*     */       }
/* 199 */       else if (flowElement instanceof SubProcess) {
/* 200 */         SubProcessNodeDef subProcessDef = new SubProcessNodeDef();
/*     */         
/* 202 */         SubProcessNodeDef subProcessNodeDef1 = subProcessDef;
/* 203 */         subProcessNodeDef1.setNodeId(flowElement.getId());
/* 204 */         subProcessNodeDef1.setName(flowElement.getName());
/* 205 */         subProcessNodeDef1.setParentBpmNodeDef(parentNodeDef);
/*     */         
/* 207 */         subProcessDef.setBpmProcessDef((BpmProcessDef)bpmProcessDef);
/* 208 */         SubProcess subProcess = (SubProcess)flowElement;
/*     */         
/* 210 */         handSubProcess((BaseBpmNodeDef)subProcessNodeDef1, subProcess, bpmProcessDef);
/*     */       } 
/* 212 */     } else if (flowElement instanceof StartEvent) {
/* 213 */       nodeDef = new BaseBpmNodeDef();
/* 214 */       nodeDef.setType(NodeType.START);
/* 215 */     } else if (flowElement instanceof EndEvent) {
/* 216 */       nodeDef = new BaseBpmNodeDef();
/* 217 */       nodeDef.setType(NodeType.END);
/* 218 */     } else if (flowElement instanceof org.activiti.bpmn.model.Gateway) {
/* 219 */       gateWayBpmNodeDef = new GateWayBpmNodeDef();
/*     */       
/* 221 */       if (flowElement instanceof ParallelGateway) {
/* 222 */         gateWayBpmNodeDef.setType(NodeType.PARALLELGATEWAY);
/*     */       
/*     */       }
/* 225 */       else if (flowElement instanceof InclusiveGateway) {
/* 226 */         gateWayBpmNodeDef.setType(NodeType.INCLUSIVEGATEWAY);
/*     */       
/*     */       }
/* 229 */       else if (flowElement instanceof ExclusiveGateway) {
/* 230 */         gateWayBpmNodeDef.setType(NodeType.EXCLUSIVEGATEWAY);
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     gateWayBpmNodeDef.setParentBpmNodeDef(parentNodeDef);
/* 235 */     gateWayBpmNodeDef.setNodeId(flowElement.getId());
/* 236 */     gateWayBpmNodeDef.setName(flowElement.getName());
/* 237 */     return (BaseBpmNodeDef)gateWayBpmNodeDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handSubProcess(BaseBpmNodeDef nodeDef, SubProcess subProcess, DefaultBpmProcessDef parentProcessDef) {
/* 248 */     DefaultBpmProcessDef bpmProcessDef = new DefaultBpmProcessDef();
/* 249 */     bpmProcessDef.setProcessDefinitionId(subProcess.getId());
/* 250 */     bpmProcessDef.setName(subProcess.getName());
/* 251 */     bpmProcessDef.setDefKey(subProcess.getId());
/* 252 */     bpmProcessDef.setParentProcessDef(parentProcessDef);
/*     */     
/* 254 */     SubProcessNodeDef subNodeDef = (SubProcessNodeDef)nodeDef;
/*     */     
/* 256 */     subNodeDef.setBpmProcessDef((BpmProcessDef)parentProcessDef);
/* 257 */     subNodeDef.setChildBpmProcessDef((BpmProcessDef)bpmProcessDef);
/* 258 */     Collection<FlowElement> list = subProcess.getFlowElements();
/* 259 */     setProcessDefNodes((BpmNodeDef)nodeDef, list, bpmProcessDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addNode(FlowElement flowElement, Map<String, FlowElement> map, Class<? extends FlowElement>... flowTypes) {
/* 270 */     for (Class<? extends FlowElement> flowType : flowTypes) {
/* 271 */       if (flowType.isInstance(flowElement)) {
/* 272 */         map.put(flowElement.getId(), flowElement);
/*     */         break;
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
/*     */   private String getNodeDefLoop(Activity flowElement) {
/* 285 */     MultiInstanceLoopCharacteristics jaxbloop = flowElement.getLoopCharacteristics();
/* 286 */     if (jaxbloop == null) return null; 
/* 287 */     return jaxbloop.isSequential() ? "sequence" : "parallel";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<SequenceFlow> getSequenceFlowList(Collection<FlowElement> flowElementList) {
/* 297 */     List<SequenceFlow> nodeList = new ArrayList<>();
/* 298 */     for (FlowElement flowElement : flowElementList) {
/* 299 */       if (flowElement instanceof SequenceFlow) {
/* 300 */         nodeList.add((SequenceFlow)flowElement);
/*     */       }
/*     */     } 
/* 303 */     return nodeList;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/BpmDefNodeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */