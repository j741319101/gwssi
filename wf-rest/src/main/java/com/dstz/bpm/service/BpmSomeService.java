package com.dstz.bpm.service;

import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.def.BpmDataModel;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.core.model.ParamsBpmNodeDef;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.service.BpmPluginService;
import com.dstz.bpm.engine.util.BpmTaskShowUtil;
import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
import com.dstz.bpm.plugin.util.FlowImageInfoPLuginsUtil;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.StringUtil;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.org.api.service.GroupService;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Resource;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BpmSomeService {
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private RepositoryService repositoryService;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private BpmInstanceManager bpmInstanceMananger;
   @Autowired
   private IBpmBusDataHandle bpmBusDataHandle;
   @Autowired
   private BpmPluginService bpmPluginService;
   @Resource
   private GroupService groupService;
   @Resource
   private IBusinessDataService iBusinessDataService;

   public JSONObject getFlowImageInfo(String instanceId, String defId, String taskId) {
      JSONObject data = new JSONObject();
      String actDefId;
      String bpmDefId;
      if (StringUtil.isNotEmpty(instanceId)) {
         BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instanceId);
         actDefId = inst.getActDefId();
         bpmDefId = inst.getDefId();
         String trace = null;
         if (StringUtil.isNotEmpty(taskId)) {
            BpmTaskOpinion bto = this.bpmTaskOpinionManager.getByTaskId(taskId);
            trace = bto.getTrace();
         }

         Map<String, List<BpmTaskOpinion>> opinionMap = new HashMap();
         List<BpmTaskOpinion> ops = this.bpmTaskOpinionManager.getByInst(instanceId, (String)null, (String)null, trace);
         ops.forEach((op) -> {
            String taskKey = op.getTaskKey();
            if (!StringUtil.isEmpty(taskKey)) {
               List<BpmTaskOpinion> opinions = (List)opinionMap.computeIfAbsent(taskKey, (k) -> {
                  return new ArrayList();
               });
               opinions.add(op);
            }

         });
         data.put("opinionMap", opinionMap);
         List<BpmTaskStack> stacks = this.bpmTaskStackManager.getByInstIdAndTrace(instanceId, trace);
         data.put("stacks", stacks);
      } else {
         BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
         actDefId = def.getActDefId();
         bpmDefId = def.getId();
      }

      BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(bpmDefId);
      BpmnModel bpmnModel = this.repositoryService.getBpmnModel(actDefId);
      JSONArray flowElements = new JSONArray();
      Process process = (Process)bpmnModel.getProcesses().get(0);
      Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
      Map<String, FlowElement> flowElementMap = new HashMap();
      Iterator var13 = process.getFlowElements().iterator();

      while(var13.hasNext()) {
         FlowElement fe = (FlowElement)var13.next();
         JSONObject json = JSON.parseObject(JSON.toJSONString(fe));
         json.put("type", fe.getClass().getSimpleName());
         flowElements.add(json);
         flowElementMap.put(fe.getId(), fe);
      }

      JSONObject nodeMap = new JSONObject();
      Set<String> locationKeySet = locationMap.keySet();
      Iterator var29 = locationKeySet.iterator();

      while(var29.hasNext()) {
         String location = (String)var29.next();
         GraphicInfo graphicInfo = (GraphicInfo)locationMap.get(location);
         JSONObject nodeInfo = JSON.parseObject(JSON.toJSONString(graphicInfo));
         FlowElement flowElement = (FlowElement)flowElementMap.get(location);
         if (flowElement != null) {
            nodeInfo.put("type", flowElement.getClass().getSimpleName());
         }

         FlowImageInfoPLuginsUtil.handlePlugins(location, nodeInfo, bpmProcessDef, instanceId);
         nodeMap.put(location, nodeInfo);
      }

      data.put("nodeMap", nodeMap);
      data.put("flowElements", flowElements);
      data.put("flowLocation", bpmnModel.getFlowLocationMap());
      data.put("artifacts", process.getArtifacts());
      data.put("pools", bpmnModel.getPools());
      data.put("lanes", process.getLanes());
      return data;
   }

   public Map<String, Object> handleNodeFreeSelectUser(FlowRequestParam flowParam) {
      BpmContext.cleanTread();
      Map<String, Object> result = new HashedMap();
      String nodeId = flowParam.getNodeId();
      if (StringUtil.isEmpty(flowParam.getTaskId()) && StringUtil.isNotEmpty(flowParam.getDefId()) && StringUtil.isEmpty(nodeId)) {
         this.getStartNodeSetting(flowParam, result);
         return result;
      } else {
         BpmTask task = null;
         if (StringUtil.isNotEmpty(flowParam.getTaskId())) {
            task = (BpmTask)this.bpmTaskManager.get(flowParam.getTaskId());
         } else if (StringUtil.isNotEmpty(nodeId) && StringUtil.isNotEmpty(flowParam.getInstanceId())) {
            flowParam.setTaskId("0");
            task = new BpmTask();
            BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceMananger.get(flowParam.getInstanceId());
            task.setNodeId(nodeId);
            task.setInstId(bpmInstance.getId());
            task.setDefId(bpmInstance.getDefId());
            task.setDefKey(bpmInstance.getDefKey());
         }

         if (task == null) {
            throw new BusinessMessage("该任务状态已发生变化，请返回并刷新列表！", BpmStatusCode.TASK_NOT_FOUND);
         } else if (StringUtil.isNotEmpty(task.getBackNode())) {
            result.put("type", "no");
            result.put("freeSelectNode", false);
            return result;
         } else {
            BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
            String freeSelectUser = nodeDef.getNodeProperties().getFreeSelectUser();
            result.put("type", freeSelectUser);
            boolean freeSelectNode = nodeDef.getNodeProperties().isFreeSelectNode();
            result.put("freeSelectNode", freeSelectNode);
            if (!"no".equals(freeSelectUser) || freeSelectNode) {
               this.handleNodeInfo(flowParam, task, nodeDef, result, freeSelectUser);
            }

            return result;
         }
      }
   }

   private void handleNodeInfo(FlowRequestParam flowParam, BpmTask task, BpmNodeDef nodeDef, Map<String, Object> result, String freeSelectUser) {
      IBpmInstance instance = (IBpmInstance)this.bpmInstanceMananger.get(task.getInstId());
      JSONObject data = flowParam.getData();
      DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
      taskModel.setBpmTask(task);
      taskModel.setBpmInstance(instance);
      if (data != null && !data.isEmpty()) {
         taskModel.setBusData(data);
      } else {
         Map<String, IBusinessData> map = this.bpmBusDataHandle.getInstanceBusData(task.getInstId(), (IBusinessPermission)null);
         taskModel.setBizDataMap(map);
      }

      taskModel.setApproveOrgId(flowParam.getStartOrgId());
      BpmContext.setActionModel(taskModel);
      Map<String, String> nodeNameMap = new HashMap();
      Map<String, List<SysIdentity>> nodeIdentitysMap = new HashMap();
      Map<String, String> inComeInclusiveGatewayNode = new HashMap();
      Map<String, Boolean> allowSysIdentityMultipleMap = new HashMap();
      if (nodeDef.getOutcomeNodes().size() == 1 && ((BpmNodeDef)nodeDef.getOutcomeNodes().get(0)).getType() == NodeType.INCLUSIVEGATEWAY) {
         result.put("multiple", true);
      }

      ParamsBpmNodeDef paramsBpmNodeDef = new ParamsBpmNodeDef();
      BeanUtil.copyProperties(nodeDef, paramsBpmNodeDef);
      Iterator var14 = paramsBpmNodeDef.getOutcomeTaskNodes().iterator();

      while(var14.hasNext()) {
         BpmNodeDef node = (BpmNodeDef)var14.next();
         if (CollectionUtil.isNotEmpty(node.getIncomeNodes()) && NodeType.INCLUSIVEGATEWAY.equals(((BpmNodeDef)node.getIncomeNodes().get(0)).getType())) {
            inComeInclusiveGatewayNode.put(node.getNodeId(), ((BpmNodeDef)node.getIncomeNodes().get(0)).getNodeId());
         }

         allowSysIdentityMultipleMap.put(node.getNodeId(), node.getNodeProperties().getAllowSysIdentityMultiple());
         nodeNameMap.put(node.getNodeId(), node.getName());

         try {
            List<SysIdentity> sysIdentities = BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel), node);
            nodeIdentitysMap.put(node.getNodeId(), sysIdentities);
         } catch (Exception var17) {
            throw new BusinessMessage("自由选择候选人计算异常：" + node.getName() + " " + var17.getMessage());
         }
      }

      result.put("nodeIdentitysMap", nodeIdentitysMap);
      result.put("nodeNameMap", nodeNameMap);
      result.put("inComeInclusiveGatewayNode", inComeInclusiveGatewayNode);
      result.put("allowSysIdentityMultiple", allowSysIdentityMultipleMap);
      BpmContext.cleanTread();
   }

   private void getStartNodeSetting(FlowRequestParam flowParam, Map<String, Object> result) {
      String defId = flowParam.getDefId();
      List<BpmNodeDef> firstNodes = this.bpmProcessDefService.getStartNodes(defId);
      if (!CollectionUtil.isEmpty(firstNodes)) {
         BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
         BpmNodeDef firstNode = startNode;
         Boolean isFirestNode = this.bpmPluginService.isSkipFirstNode(defId);
         if (isFirestNode && startNode.getOutcomeNodes().size() == 1) {
            firstNode = (BpmNodeDef)startNode.getOutcomeNodes().get(0);
         }

         String freeSelectUser = startNode.getNodeProperties().getFreeSelectUser();
         result.put("type", freeSelectUser);
         boolean freeSelectNode = startNode.getNodeProperties().isFreeSelectNode();
         result.put("freeSelectNode", freeSelectNode);
         if (!"no".equals(freeSelectUser) || freeSelectNode) {
            DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd();
            JSONObject busData = flowParam.getData();
            taskModel.setActionName("start");
            taskModel.setBusData(busData);
            taskModel.setApproveOrgId(flowParam.getStartOrgId());
            DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
            Iterator var13 = bpmProcessDef.getDataModelList().iterator();

            while(var13.hasNext()) {
               BpmDataModel dataModel = (BpmDataModel)var13.next();
               String modelCode = dataModel.getCode();
               if (busData.containsKey(modelCode)) {
                  IBusinessData businessData = this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode);
                  taskModel.getBizDataMap().put(modelCode, businessData);
               }
            }

            BpmContext.setActionModel(taskModel);
            Map<String, String> nodeNameMap = new HashMap(startNode.getOutcomeTaskNodes().size());
            Map<String, List<SysIdentity>> nodeIdentitysMap = new HashMap();
            Map<String, String> inComeInclusiveGatewayNode = new HashMap();
            Map<String, Boolean> allowSysIdentityMultipleMap = new HashMap();
            ParamsBpmNodeDef paramsBpmNodeDef = new ParamsBpmNodeDef();
            BeanUtil.copyProperties(firstNode, paramsBpmNodeDef);
            Iterator var18 = paramsBpmNodeDef.getOutcomeTaskNodes().iterator();

            while(var18.hasNext()) {
               BpmNodeDef node = (BpmNodeDef)var18.next();
               if (CollectionUtil.isNotEmpty(node.getIncomeNodes()) && NodeType.INCLUSIVEGATEWAY.equals(((BpmNodeDef)node.getIncomeNodes().get(0)).getType())) {
                  inComeInclusiveGatewayNode.put(node.getNodeId(), ((BpmNodeDef)node.getIncomeNodes().get(0)).getNodeId());
               }

               nodeNameMap.put(node.getNodeId(), node.getName());
               allowSysIdentityMultipleMap.put(node.getNodeId(), node.getNodeProperties().getAllowSysIdentityMultiple());
               nodeIdentitysMap.put(node.getNodeId(), BpmTaskShowUtil.appendOrgUser(UserCalcPreview.calcNodeUsers(node, taskModel), node));
            }

            if (firstNode.getOutcomeNodes().size() == 1 && ((BpmNodeDef)firstNode.getOutcomeNodes().get(0)).getType() == NodeType.INCLUSIVEGATEWAY) {
               result.put("multiple", true);
            }

            result.put("nodeNameMap", nodeNameMap);
            result.put("nodeIdentitysMap", nodeIdentitysMap);
            result.put("inComeInclusiveGatewayNode", inComeInclusiveGatewayNode);
            result.put("allowSysIdentityMultiple", allowSysIdentityMultipleMap);
            BpmContext.cleanTread();
         }
      }
   }
}
