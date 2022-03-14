package com.dstz.bpm.plugin.util;

import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.global.multinst.context.MultInstPluginContext;
import com.dstz.bpm.plugin.global.multinst.def.MultInst;
import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.lang.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlowImageInfoPLuginsUtil {
   public static void handlePlugins(String location, JSONObject nodeInfo, BpmProcessDef bpmProcessDef, String instanceId) {
      DynamicTaskManager dynamicTaskMananger = (DynamicTaskManager)AppUtil.getBean(DynamicTaskManager.class);
      BpmInstanceManager bpmInstanceManager = (BpmInstanceManager)AppUtil.getBean(BpmInstanceManager.class);
      BpmDefinitionManager bpmDefinitionMananger = (BpmDefinitionManager)AppUtil.getBean(BpmDefinitionManager.class);
      BpmNodeDef nodeDef = bpmProcessDef.getBpmnNodeDef(location);
      MultInstPluginContext mipc = (MultInstPluginContext)bpmProcessDef.getBpmPluginContext(MultInstPluginContext.class);
      if (mipc != null) {
         Iterator var9 = ((MultInstPluginDef)mipc.getBpmPluginDef()).getMultInsts().iterator();

         while(var9.hasNext()) {
            MultInst mi = (MultInst)var9.next();
            if (mi.getStartNodeKey().equals(location)) {
               nodeInfo.put("multInst", "start");
               nodeInfo.put("multInst_end", mi.getEndNodeKey());
            }

            if (mi.getEndNodeKey().equals(location)) {
               nodeInfo.put("multInst", "end");
               nodeInfo.put("multInst_start", mi.getStartNodeKey());
            }
         }
      }

      if (nodeDef != null) {
         if (nodeDef instanceof CallActivityNodeDef) {
            if (StringUtil.isNotEmpty(instanceId)) {
               List<BpmInstance> instanceList = bpmInstanceManager.getByParentId(instanceId);
               List<BpmInstance> callActivityNodes = (List)instanceList.stream().filter((item) -> {
                  return nodeDef.getNodeId().equals(item.getSuperNodeId());
               }).collect(Collectors.toList());
               if (!Collections.isEmpty(callActivityNodes)) {
                  nodeInfo.put("subInstanceList", callActivityNodes);
                  return;
               }
            }

            CallActivityNodeDef node = (CallActivityNodeDef)nodeDef;
            BpmDefinition def = bpmDefinitionMananger.getByKey(node.getFlowKey());
            nodeInfo.put("subDefinition", def);
         }

         SignTaskPluginContext stpc = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
         if (stpc != null && ((SignTaskPluginDef)stpc.getBpmPluginDef()).isSignMultiTask()) {
            nodeInfo.put("signTask", "1");
         }

         DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
         if (dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled()) {
            nodeInfo.put("dynamicTask", "1");
            if (StringUtil.isNotEmpty(instanceId)) {
               DynamicTask taskSetting = dynamicTaskMananger.getDynamicTaskSettingByInstanceId(instanceId, nodeDef.getNodeId());
               if (taskSetting != null) {
                  nodeInfo.put("dynamicTaskCounts", taskSetting.getAmmount());
                  nodeInfo.put("dynamicTaskUsers", taskSetting.getIdentityNode());
               } else {
                  nodeInfo.put("dynamicTaskCounts", 0);
               }
            }
         }

      }
   }
}
