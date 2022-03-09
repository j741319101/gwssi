/*    */ package com.dstz.bpm.plugin.util;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*    */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*    */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*    */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*    */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*    */ import com.dstz.bpm.core.model.BpmDefinition;
/*    */ import com.dstz.bpm.core.model.BpmInstance;
/*    */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*    */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*    */ import com.dstz.bpm.plugin.global.multinst.context.MultInstPluginContext;
/*    */ import com.dstz.bpm.plugin.global.multinst.def.MultInst;
/*    */ import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*    */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*    */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*    */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import io.jsonwebtoken.lang.Collections;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class FlowImageInfoPLuginsUtil {
/*    */   public static void handlePlugins(String location, JSONObject nodeInfo, BpmProcessDef bpmProcessDef, String instanceId) {
/* 27 */     DynamicTaskManager dynamicTaskMananger = (DynamicTaskManager)AppUtil.getBean(DynamicTaskManager.class);
/* 28 */     BpmInstanceManager bpmInstanceManager = (BpmInstanceManager)AppUtil.getBean(BpmInstanceManager.class);
/* 29 */     BpmDefinitionManager bpmDefinitionMananger = (BpmDefinitionManager)AppUtil.getBean(BpmDefinitionManager.class);
/*    */     
/* 31 */     BpmNodeDef nodeDef = bpmProcessDef.getBpmnNodeDef(location);
/* 32 */     MultInstPluginContext mipc = (MultInstPluginContext)bpmProcessDef.getBpmPluginContext(MultInstPluginContext.class);
/*    */ 
/*    */     
/* 35 */     if (mipc != null) {
/* 36 */       for (MultInst mi : ((MultInstPluginDef)mipc.getBpmPluginDef()).getMultInsts()) {
/* 37 */         if (mi.getStartNodeKey().equals(location)) {
/* 38 */           nodeInfo.put("multInst", "start");
/* 39 */           nodeInfo.put("multInst_end", mi.getEndNodeKey());
/*    */         } 
/* 41 */         if (mi.getEndNodeKey().equals(location)) {
/* 42 */           nodeInfo.put("multInst", "end");
/* 43 */           nodeInfo.put("multInst_start", mi.getStartNodeKey());
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 48 */     if (nodeDef == null) {
/*    */       return;
/*    */     }
/* 51 */     if (nodeDef instanceof CallActivityNodeDef) {
/* 52 */       if (StringUtil.isNotEmpty(instanceId)) {
/* 53 */         List<BpmInstance> instanceList = bpmInstanceManager.getByParentId(instanceId);
/* 54 */         List<BpmInstance> callActivityNodes = (List<BpmInstance>)instanceList.stream().filter(item -> nodeDef.getNodeId().equals(item.getSuperNodeId())).collect(Collectors.toList());
/* 55 */         if (!Collections.isEmpty(callActivityNodes)) {
/* 56 */           nodeInfo.put("subInstanceList", callActivityNodes);
/*    */           return;
/*    */         } 
/*    */       } 
/* 60 */       CallActivityNodeDef node = (CallActivityNodeDef)nodeDef;
/* 61 */       BpmDefinition def = bpmDefinitionMananger.getByKey(node.getFlowKey());
/* 62 */       nodeInfo.put("subDefinition", def);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 67 */     SignTaskPluginContext stpc = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 68 */     if (stpc != null && ((SignTaskPluginDef)stpc.getBpmPluginDef()).isSignMultiTask()) {
/* 69 */       nodeInfo.put("signTask", "1");
/*    */     }
/*    */ 
/*    */     
/* 73 */     DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 74 */     if (dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 75 */       nodeInfo.put("dynamicTask", "1");
/* 76 */       if (StringUtil.isNotEmpty(instanceId)) {
/* 77 */         DynamicTask taskSetting = dynamicTaskMananger.getDynamicTaskSettingByInstanceId(instanceId, nodeDef.getNodeId());
/* 78 */         if (taskSetting != null) {
/* 79 */           nodeInfo.put("dynamicTaskCounts", taskSetting.getAmmount());
/* 80 */           nodeInfo.put("dynamicTaskUsers", taskSetting.getIdentityNode());
/*    */         } else {
/* 82 */           nodeInfo.put("dynamicTaskCounts", Integer.valueOf(0));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/util/FlowImageInfoPLuginsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */