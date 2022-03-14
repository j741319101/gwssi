package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.recrease.handler.RecreaseTaskAction;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.rest.ControllerTools;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm"})
@RestController
public class BpmDynaMultTaskResetController extends ControllerTools {
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private RecreaseTaskAction recreaseTaskAction;
   @Resource
   private BpmProcessDefService bpmProcessDefService;

   @GetMapping({"/taskReset/getSignAndDynamicTaskList"})
   public ResultMsg<JSONObject> getSignAndDynamicTaskList(@RequestParam String taskId) {
      BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
      BpmTask bpmTask = new BpmTask();
      bpmTask.setId(bpmTaskOpinion.getTaskId());
      BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(bpmTaskOpinion.getInstId());
      new JSONObject();
      JSONObject dynamicJsonObject = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
      JSONObject tasks = dynamicJsonObject.getJSONObject("tasks");
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), bpmTaskOpinion.getTaskKey());
      List<BpmNodeDef> bpmNodeDefs = bpmNodeDef.getOutcomeTaskNodes();
      BpmNodeDef inComeNode = null;
      Iterator var11;
      if (tasks.keySet() != null && tasks.keySet().size() > 0) {
         var11 = tasks.keySet().iterator();
         if (var11.hasNext()) {
            String nodeId = (String)var11.next();
            BpmNodeDef node = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
            if (node != null) {
               List<BpmNodeDef> inComes = node.getIncomeNodes();
               if (CollectionUtil.isNotEmpty(inComes)) {
                  inComeNode = (BpmNodeDef)inComes.get(0);
               }
            }
         }
      }

      var11 = bpmNodeDefs.iterator();

      while(var11.hasNext()) {
         BpmNodeDef node = (BpmNodeDef)var11.next();
         String nodeId = node.getNodeId();
         String subNodeId = null;
         String defId = node.getBpmProcessDef().getProcessDefinitionId();
         if (nodeId.indexOf("&") > -1) {
            String[] info = nodeId.split("&");
            nodeId = info[0];
            subNodeId = info[1];
            node = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
         }

         if (tasks.get(nodeId) == null) {
            List<BpmNodeDef> inComes = node.getIncomeNodes();
            if (!CollectionUtil.isEmpty(inComes) && StringUtils.equals(((BpmNodeDef)inComes.get(0)).getNodeId(), inComeNode.getNodeId())) {
               DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)node.getPluginContext(DynamicTaskPluginContext.class);
               DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
               if (dynamicTaskPluginDef.getEnabled()) {
                  JSONObject nodeInfo = new JSONObject();
                  nodeInfo.put("defId", defId);
                  nodeInfo.put("existAliveTask", false);
                  nodeInfo.put("nodeId", nodeId);
                  if (subNodeId != null) {
                     nodeInfo.put("nodeId", subNodeId);
                  }

                  nodeInfo.put("taskName", node.getName());
                  nodeInfo.put("taskType", "dynamic");
                  nodeInfo.put("tasks", new String[0]);
                  tasks.put(nodeId, nodeInfo);
               }
            }
         }
      }

      dynamicJsonObject.put("tasks", tasks);
      return new ResultMsg(dynamicJsonObject);
   }
}
