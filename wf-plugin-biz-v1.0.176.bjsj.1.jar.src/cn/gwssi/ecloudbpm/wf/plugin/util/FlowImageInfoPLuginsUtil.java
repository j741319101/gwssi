/*    */ package cn.gwssi.ecloudbpm.wf.plugin.util;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.CallActivityNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.DynamicTaskManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.DynamicTask;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.context.MultInstPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInst;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInstPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.context.SignTaskPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.def.SignTaskPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import io.jsonwebtoken.lang.Collections;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class FlowImageInfoPLuginsUtil {
/*    */   public static void handlePlugins(String location, JSONObject nodeInfo, BpmProcessDef bpmProcessDef, String instanceId) {
/* 26 */     DynamicTaskManager dynamicTaskMananger = (DynamicTaskManager)AppUtil.getBean(DynamicTaskManager.class);
/* 27 */     BpmInstanceManager bpmInstanceManager = (BpmInstanceManager)AppUtil.getBean(BpmInstanceManager.class);
/* 28 */     BpmDefinitionManager bpmDefinitionMananger = (BpmDefinitionManager)AppUtil.getBean(BpmDefinitionManager.class);
/*    */     
/* 30 */     BpmNodeDef nodeDef = bpmProcessDef.getBpmnNodeDef(location);
/* 31 */     MultInstPluginContext mipc = (MultInstPluginContext)bpmProcessDef.getBpmPluginContext(MultInstPluginContext.class);
/*    */ 
/*    */     
/* 34 */     if (mipc != null) {
/* 35 */       for (MultInst mi : ((MultInstPluginDef)mipc.getBpmPluginDef()).getMultInsts()) {
/* 36 */         if (mi.getStartNodeKey().equals(location)) {
/* 37 */           nodeInfo.put("multInst", "start");
/* 38 */           nodeInfo.put("multInst_end", mi.getEndNodeKey());
/*    */         } 
/* 40 */         if (mi.getEndNodeKey().equals(location)) {
/* 41 */           nodeInfo.put("multInst", "end");
/* 42 */           nodeInfo.put("multInst_start", mi.getStartNodeKey());
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 47 */     if (nodeDef == null) {
/*    */       return;
/*    */     }
/* 50 */     if (nodeDef instanceof CallActivityNodeDef) {
/* 51 */       if (StringUtil.isNotEmpty(instanceId)) {
/* 52 */         List<BpmInstance> instanceList = bpmInstanceManager.getByParentId(instanceId);
/* 53 */         List<BpmInstance> callActivityNodes = (List<BpmInstance>)instanceList.stream().filter(item -> nodeDef.getNodeId().equals(item.getSuperNodeId())).collect(Collectors.toList());
/* 54 */         if (!Collections.isEmpty(callActivityNodes)) {
/* 55 */           nodeInfo.put("subInstanceList", callActivityNodes);
/*    */           return;
/*    */         } 
/*    */       } 
/* 59 */       CallActivityNodeDef node = (CallActivityNodeDef)nodeDef;
/* 60 */       BpmDefinition def = bpmDefinitionMananger.getByKey(node.getFlowKey());
/* 61 */       nodeInfo.put("subDefinition", def);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 66 */     SignTaskPluginContext stpc = (SignTaskPluginContext)nodeDef.getPluginContext(SignTaskPluginContext.class);
/* 67 */     if (stpc != null && ((SignTaskPluginDef)stpc.getBpmPluginDef()).isSignMultiTask()) {
/* 68 */       nodeInfo.put("signTask", "1");
/*    */     }
/*    */ 
/*    */     
/* 72 */     DynamicTaskPluginContext dynamicTaskContext = (DynamicTaskPluginContext)nodeDef.getPluginContext(DynamicTaskPluginContext.class);
/* 73 */     if (dynamicTaskContext != null && ((DynamicTaskPluginDef)dynamicTaskContext.getBpmPluginDef()).getIsEnabled().booleanValue()) {
/* 74 */       nodeInfo.put("dynamicTask", "1");
/* 75 */       if (StringUtil.isNotEmpty(instanceId)) {
/* 76 */         DynamicTask taskSetting = dynamicTaskMananger.getDynamicTaskSettingByInstanceId(instanceId, nodeDef.getNodeId());
/* 77 */         if (taskSetting != null) {
/* 78 */           nodeInfo.put("dynamicTaskCounts", taskSetting.getAmmount());
/* 79 */           nodeInfo.put("dynamicTaskUsers", taskSetting.getIdentityNode());
/*    */         } else {
/* 81 */           nodeInfo.put("dynamicTaskCounts", Integer.valueOf(0));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/util/FlowImageInfoPLuginsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */