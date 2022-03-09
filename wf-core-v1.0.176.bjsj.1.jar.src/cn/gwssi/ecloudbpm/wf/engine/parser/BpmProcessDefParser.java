/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.parser.flow.AbsFlowParse;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.parser.node.AbsNodeParse;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class BpmProcessDefParser
/*    */ {
/*    */   private static List<AbsFlowParse> flowParsers;
/*    */   private static List<AbsNodeParse> baseNodeParsers;
/*    */   
/*    */   public static void processDefParser(DefaultBpmProcessDef bpmProcessDef, JSONObject bpmDefSetting) {
/* 22 */     JSONObject flowConf = bpmDefSetting.getJSONObject("flow");
/* 23 */     for (AbsFlowParse flowParser : getFlowParsers()) {
/* 24 */       flowParser.parse((BpmDef)bpmProcessDef, flowConf);
/*    */     }
/*    */     
/* 27 */     JSONObject nodeMap = bpmDefSetting.getJSONObject("nodeMap");
/* 28 */     for (BpmNodeDef nodeDef : bpmProcessDef.getBpmnNodeDefs()) {
/* 29 */       JSONObject nodeConfig = nodeMap.getJSONObject(nodeDef.getNodeId());
/*    */       
/* 31 */       for (AbsNodeParse nodeParser : getNodeParsers()) {
/* 32 */         if (nodeParser.isSupport(nodeDef)) {
/* 33 */           nodeParser.parse((BpmDef)nodeDef, nodeConfig);
/*    */         }
/*    */       } 
/*    */       
/* 37 */       if (nodeDef instanceof SubProcessNodeDef) {
/* 38 */         processDefParser((DefaultBpmProcessDef)((SubProcessNodeDef)nodeDef).getChildBpmProcessDef(), bpmDefSetting);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static List<AbsFlowParse> getFlowParsers() {
/* 45 */     if (CollectionUtil.isNotEmpty(flowParsers)) return flowParsers;
/*    */     
/* 47 */     Map<String, AbsFlowParse> map = AppUtil.getImplInstance(AbsFlowParse.class);
/* 48 */     flowParsers = new ArrayList<>(map.values());
/*    */     
/* 50 */     return flowParsers;
/*    */   }
/*    */ 
/*    */   
/*    */   private static List<AbsNodeParse> getNodeParsers() {
/* 55 */     if (CollectionUtil.isNotEmpty(baseNodeParsers)) return baseNodeParsers; 
/* 56 */     Map<String, AbsNodeParse> map = AppUtil.getImplInstance(AbsNodeParse.class);
/* 57 */     baseNodeParsers = new ArrayList<>(map.values());
/*    */     
/* 59 */     return baseNodeParsers;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/BpmProcessDefParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */