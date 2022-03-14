/*    */ package com.dstz.bpm.engine.parser.flow;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.def.BpmDefProperties;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FlowPropertiesParse
/*    */   extends AbsFlowParse<BpmDefProperties>
/*    */ {
/*    */   public void parse(DefaultBpmProcessDef def, JSONObject flowConf) {
/* 17 */     JSONObject properties = (JSONObject)JSONObject.toJSON(def.getExtProperties());
/* 18 */     if (flowConf.containsKey(getKey())) {
/* 19 */       properties = flowConf.getJSONObject(getKey());
/*    */     }
/* 21 */     BpmDefProperties bpmDefproperties = (BpmDefProperties)JSONObject.toJavaObject((JSON)properties, BpmDefProperties.class);
/* 22 */     def.setExtProperties(bpmDefproperties);
/* 23 */     flowConf.put(getKey(), properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 28 */     return "properties";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 34 */     BpmDefProperties properties = (BpmDefProperties)object;
/* 35 */     bpmProcessDef.setExtProperties(properties);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowPropertiesParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */