/*     */ package cn.gwssi.ecloudbpm.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowRequestParam;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmDataModel;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.ParamsBpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.service.BpmPluginService;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.util.BpmTaskShowUtil;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.util.UserCalcPreview;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.util.FlowImageInfoPLuginsUtil;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.FlowElement;
/*     */ import org.activiti.bpmn.model.GraphicInfo;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.apache.commons.collections.map.HashedMap;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class BpmSomeService
/*     */ {
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private RepositoryService repositoryService;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public JSONObject getFlowImageInfo(String instanceId, String defId, String taskId) {
/*     */     String actDefId, bpmDefId;
/*  76 */     JSONObject data = new JSONObject();
/*  77 */     if (StringUtil.isNotEmpty(instanceId)) {
/*  78 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instanceId);
/*  79 */       actDefId = inst.getActDefId();
/*  80 */       bpmDefId = inst.getDefId();
/*     */       
/*  82 */       String trace = null;
/*  83 */       if (StringUtil.isNotEmpty(taskId)) {
/*  84 */         BpmTaskOpinion bto = this.bpmTaskOpinionManager.getByTaskId(taskId);
/*  85 */         trace = bto.getTrace();
/*     */       } 
/*     */       
/*  88 */       Map<String, List<BpmTaskOpinion>> opinionMap = new HashMap<>();
/*  89 */       List<BpmTaskOpinion> ops = this.bpmTaskOpinionManager.getByInst(instanceId, null, null, trace);
/*  90 */       ops.forEach(op -> {
/*     */             String taskKey = op.getTaskKey();
/*     */             if (!StringUtil.isEmpty(taskKey)) {
/*     */               List<BpmTaskOpinion> opinions = opinionMap.computeIfAbsent(taskKey, ());
/*     */               opinions.add(op);
/*     */             } 
/*     */           });
/*  97 */       data.put("opinionMap", opinionMap);
/*     */ 
/*     */       
/* 100 */       List<BpmTaskStack> stacks = this.bpmTaskStackManager.getByInstIdAndTrace(instanceId, trace);
/* 101 */       data.put("stacks", stacks);
/*     */     } else {
/* 103 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 104 */       actDefId = def.getActDefId();
/* 105 */       bpmDefId = def.getId();
/*     */     } 
/*     */     
/* 108 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(bpmDefId);
/* 109 */     BpmnModel bpmnModel = this.repositoryService.getBpmnModel(actDefId);
/* 110 */     JSONArray flowElements = new JSONArray();
/* 111 */     Process process = bpmnModel.getProcesses().get(0);
/*     */ 
/*     */     
/* 114 */     Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
/* 115 */     Map<String, FlowElement> flowElementMap = new HashMap<>();
/* 116 */     for (FlowElement fe : process.getFlowElements()) {
/* 117 */       JSONObject json = JSON.parseObject(JSON.toJSONString(fe));
/* 118 */       json.put("type", fe.getClass().getSimpleName());
/* 119 */       flowElements.add(json);
/* 120 */       flowElementMap.put(fe.getId(), fe);
/*     */     } 
/*     */ 
/*     */     
/* 124 */     JSONObject nodeMap = new JSONObject();
/* 125 */     Set<String> locationKeySet = locationMap.keySet();
/* 126 */     for (String location : locationKeySet) {
/* 127 */       GraphicInfo graphicInfo = locationMap.get(location);
/* 128 */       JSONObject nodeInfo = JSON.parseObject(JSON.toJSONString(graphicInfo));
/* 129 */       FlowElement flowElement = flowElementMap.get(location);
/* 130 */       if (flowElement != null) {
/* 131 */         nodeInfo.put("type", flowElement.getClass().getSimpleName());
/*     */       }
/*     */       
/* 134 */       FlowImageInfoPLuginsUtil.handlePlugins(location, nodeInfo, bpmProcessDef, instanceId);
/*     */       
/* 136 */       nodeMap.put(location, nodeInfo);
/*     */     } 
/*     */     
/* 139 */     data.put("nodeMap", nodeMap);
/* 140 */     data.put("flowElements", flowElements);
/* 141 */     data.put("flowLocation", bpmnModel.getFlowLocationMap());
/* 142 */     data.put("artifacts", process.getArtifacts());
/*     */     
/* 144 */     data.put("pools", bpmnModel.getPools());
/* 145 */     data.put("lanes", process.getLanes());
/* 146 */     return data; } @Resource private BpmTaskManager bpmTaskManager; @Autowired private BpmInstanceManager bpmInstanceMananger; @Autowired
/*     */   private IBpmBusDataHandle bpmBusDataHandle; @Autowired
/*     */   private BpmPluginService bpmPluginService; @Resource
/*     */   private GroupService groupService; @Resource
/* 150 */   private IBusinessDataService iBusinessDataService; public Map<String, Object> handleNodeFreeSelectUser(FlowRequestParam flowParam) { BpmContext.cleanTread();
/* 151 */     HashedMap<String, String> hashedMap = new HashedMap();
/* 152 */     String nodeId = flowParam.getNodeId();
/*     */     
/* 154 */     if (StringUtil.isEmpty(flowParam.getTaskId()) && StringUtil.isNotEmpty(flowParam.getDefId()) && StringUtil.isEmpty(nodeId)) {
/* 155 */       getStartNodeSetting(flowParam, (Map)hashedMap);
/* 156 */       return (Map)hashedMap;
/*     */     } 
/* 158 */     BpmTask task = null;
/* 159 */     if (StringUtil.isNotEmpty(flowParam.getTaskId())) {
/* 160 */       task = (BpmTask)this.bpmTaskManager.get(flowParam.getTaskId());
/* 161 */     } else if (StringUtil.isNotEmpty(nodeId) && StringUtil.isNotEmpty(flowParam.getInstanceId())) {
/* 162 */       flowParam.setTaskId("0");
/* 163 */       task = new BpmTask();
/* 164 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceMananger.get(flowParam.getInstanceId());
/* 165 */       task.setNodeId(nodeId);
/* 166 */       task.setInstId(bpmInstance.getId());
/* 167 */       task.setDefId(bpmInstance.getDefId());
/* 168 */       task.setDefKey(bpmInstance.getDefKey());
/*     */     } 
/* 170 */     if (task == null) {
/* 171 */       throw new BusinessMessage("该任务状态已发生变化，请返回并刷新列表！", BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/* 173 */     if (StringUtil.isNotEmpty(task.getBackNode())) {
/* 174 */       hashedMap.put("type", "no");
/* 175 */       hashedMap.put("freeSelectNode", Boolean.valueOf(false));
/* 176 */       return (Map)hashedMap;
/*     */     } 
/*     */     
/* 179 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
/* 180 */     String freeSelectUser = nodeDef.getNodeProperties().getFreeSelectUser();
/* 181 */     hashedMap.put("type", freeSelectUser);
/* 182 */     boolean freeSelectNode = nodeDef.getNodeProperties().isFreeSelectNode();
/* 183 */     hashedMap.put("freeSelectNode", Boolean.valueOf(freeSelectNode));
/*     */     
/* 185 */     if (!"no".equals(freeSelectUser) || freeSelectNode) {
/* 186 */       handleNodeInfo(flowParam, task, nodeDef, (Map)hashedMap, freeSelectUser);
/*     */     }
/* 188 */     return (Map)hashedMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleNodeInfo(FlowRequestParam flowParam, BpmTask task, BpmNodeDef nodeDef, Map<String, Object> result, String freeSelectUser) {
/* 199 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceMananger.get(task.getInstId());
/* 200 */     JSONObject data = flowParam.getData();
/* 201 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/* 202 */     taskModel.setBpmTask((IBpmTask)task);
/* 203 */     taskModel.setBpmInstance(instance);
/* 204 */     if (data == null || data.isEmpty()) {
/* 205 */       Map<String, IBusinessData> map = this.bpmBusDataHandle.getInstanceBusData(task.getInstId(), null);
/* 206 */       taskModel.setBizDataMap(map);
/*     */     } else {
/* 208 */       taskModel.setBusData(data);
/*     */     } 
/* 210 */     taskModel.setApproveOrgId(flowParam.getStartOrgId());
/*     */     
/* 212 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 213 */     Map<String, String> nodeNameMap = new HashMap<>();
/* 214 */     Map<String, List<SysIdentity>> nodeIdentitysMap = new HashMap<>();
/* 215 */     Map<String, String> inComeInclusiveGatewayNode = new HashMap<>();
/*     */     
/* 217 */     Map<String, Boolean> allowSysIdentityMultipleMap = new HashMap<>();
/*     */     
/* 219 */     if (nodeDef.getOutcomeNodes().size() == 1 && ((BpmNodeDef)nodeDef.getOutcomeNodes().get(0)).getType() == NodeType.INCLUSIVEGATEWAY) {
/* 220 */       result.put("multiple", Boolean.valueOf(true));
/*     */     }
/*     */     
/* 223 */     ParamsBpmNodeDef paramsBpmNodeDef = new ParamsBpmNodeDef();
/* 224 */     BeanUtil.copyProperties(nodeDef, paramsBpmNodeDef);
/* 225 */     for (BpmNodeDef node : paramsBpmNodeDef.getOutcomeTaskNodes()) {
/* 226 */       if (CollectionUtil.isNotEmpty(node.getIncomeNodes()) && NodeType.INCLUSIVEGATEWAY
/* 227 */         .equals(((BpmNodeDef)node.getIncomeNodes().get(0)).getType())) {
/* 228 */         inComeInclusiveGatewayNode.put(node.getNodeId(), ((BpmNodeDef)node.getIncomeNodes().get(0)).getNodeId());
/*     */       }
/* 230 */       allowSysIdentityMultipleMap.put(node.getNodeId(), node.getNodeProperties().getAllowSysIdentityMultiple());
/* 231 */       nodeNameMap.put(node.getNodeId(), node.getName());
/*     */       try {
/* 233 */         List<SysIdentity> sysIdentities = BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel), node);
/* 234 */         nodeIdentitysMap.put(node.getNodeId(), sysIdentities);
/* 235 */       } catch (Exception e) {
/* 236 */         throw new BusinessMessage("自由选择候选人计算异常：" + node.getName() + " " + e.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     result.put("nodeIdentitysMap", nodeIdentitysMap);
/* 241 */     result.put("nodeNameMap", nodeNameMap);
/* 242 */     result.put("inComeInclusiveGatewayNode", inComeInclusiveGatewayNode);
/* 243 */     result.put("allowSysIdentityMultiple", allowSysIdentityMultipleMap);
/* 244 */     BpmContext.cleanTread();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getStartNodeSetting(FlowRequestParam flowParam, Map<String, Object> result) {
/* 254 */     String defId = flowParam.getDefId();
/* 255 */     List<BpmNodeDef> firstNodes = this.bpmProcessDefService.getStartNodes(defId);
/* 256 */     if (CollectionUtil.isEmpty(firstNodes)) {
/*     */       return;
/*     */     }
/*     */     
/* 260 */     BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
/* 261 */     BpmNodeDef firstNode = startNode;
/* 262 */     Boolean isFirestNode = this.bpmPluginService.isSkipFirstNode(defId);
/*     */     
/* 264 */     if (isFirestNode.booleanValue() && startNode.getOutcomeNodes().size() == 1) {
/* 265 */       firstNode = startNode.getOutcomeNodes().get(0);
/*     */     }
/*     */     
/* 268 */     String freeSelectUser = startNode.getNodeProperties().getFreeSelectUser();
/* 269 */     result.put("type", freeSelectUser);
/* 270 */     boolean freeSelectNode = startNode.getNodeProperties().isFreeSelectNode();
/* 271 */     result.put("freeSelectNode", Boolean.valueOf(freeSelectNode));
/*     */     
/* 273 */     if ("no".equals(freeSelectUser) && !freeSelectNode) {
/*     */       return;
/*     */     }
/* 276 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
/* 277 */     JSONObject busData = flowParam.getData();
/* 278 */     taskModel.setActionName("start");
/* 279 */     taskModel.setBusData(busData);
/* 280 */     taskModel.setApproveOrgId(flowParam.getStartOrgId());
/* 281 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/*     */     
/* 283 */     for (BpmDataModel dataModel : bpmProcessDef.getDataModelList()) {
/* 284 */       String modelCode = dataModel.getCode();
/* 285 */       if (busData.containsKey(modelCode)) {
/* 286 */         IBusinessData businessData = this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode);
/* 287 */         taskModel.getBizDataMap().put(modelCode, businessData);
/*     */       } 
/*     */     } 
/*     */     
/* 291 */     BpmContext.setActionModel((ActionCmd)taskModel);
/* 292 */     Map<String, String> nodeNameMap = new HashMap<>(startNode.getOutcomeTaskNodes().size());
/* 293 */     Map<String, List<SysIdentity>> nodeIdentitysMap = new HashMap<>();
/* 294 */     Map<String, String> inComeInclusiveGatewayNode = new HashMap<>();
/* 295 */     Map<String, Boolean> allowSysIdentityMultipleMap = new HashMap<>();
/*     */     
/* 297 */     ParamsBpmNodeDef paramsBpmNodeDef = new ParamsBpmNodeDef();
/* 298 */     BeanUtil.copyProperties(firstNode, paramsBpmNodeDef);
/* 299 */     for (BpmNodeDef node : paramsBpmNodeDef.getOutcomeTaskNodes()) {
/* 300 */       if (CollectionUtil.isNotEmpty(node.getIncomeNodes()) && NodeType.INCLUSIVEGATEWAY
/* 301 */         .equals(((BpmNodeDef)node.getIncomeNodes().get(0)).getType())) {
/* 302 */         inComeInclusiveGatewayNode.put(node.getNodeId(), ((BpmNodeDef)node.getIncomeNodes().get(0)).getNodeId());
/*     */       }
/* 304 */       nodeNameMap.put(node.getNodeId(), node.getName());
/* 305 */       allowSysIdentityMultipleMap.put(node.getNodeId(), node.getNodeProperties().getAllowSysIdentityMultiple());
/* 306 */       nodeIdentitysMap.put(node.getNodeId(), BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel), node));
/*     */     } 
/*     */     
/* 309 */     if (firstNode.getOutcomeNodes().size() == 1 && ((BpmNodeDef)firstNode.getOutcomeNodes().get(0)).getType() == NodeType.INCLUSIVEGATEWAY) {
/* 310 */       result.put("multiple", Boolean.valueOf(true));
/*     */     }
/* 312 */     result.put("nodeNameMap", nodeNameMap);
/* 313 */     result.put("nodeIdentitysMap", nodeIdentitysMap);
/* 314 */     result.put("inComeInclusiveGatewayNode", inComeInclusiveGatewayNode);
/* 315 */     result.put("allowSysIdentityMultiple", allowSysIdentityMultipleMap);
/* 316 */     BpmContext.cleanTread();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/service/BpmSomeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */