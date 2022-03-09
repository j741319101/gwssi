/*    */ package com.dstz.bpm.engine.plugin.context;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.ExtractType;
/*    */ import com.dstz.bpm.api.engine.constant.LogicType;
/*    */ import com.dstz.bpm.api.engine.plugin.context.PluginParse;
/*    */ import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractUserCalcPluginContext<T extends BpmUserCalcPluginDef>
/*    */   implements UserCalcPluginContext, PluginParse<T>
/*    */ {
/*    */   private static final long serialVersionUID = -4447195857368619545L;
/*    */   private T bpmPluginDef;
/*    */   
/*    */   public T getBpmPluginDef() {
/* 24 */     return this.bpmPluginDef;
/*    */   }
/*    */   
/*    */   public void setBpmPluginDef(T bpmPluginDef) {
/* 28 */     this.bpmPluginDef = bpmPluginDef;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T parse(JSON pluginDefJson) {
/* 37 */     JSONObject jsonObject = (JSONObject)pluginDefJson;
/* 38 */     T bpmPluginDef = parseJson(jsonObject);
/*    */     
/* 40 */     String extract = jsonObject.getString("extract");
/* 41 */     String logicCal = jsonObject.getString("logicCal");
/*    */     
/* 43 */     bpmPluginDef.setExtract(ExtractType.fromKey(extract));
/* 44 */     bpmPluginDef.setLogicCal(LogicType.fromKey(logicCal));
/*    */     
/* 46 */     setBpmPluginDef(bpmPluginDef);
/* 47 */     return bpmPluginDef;
/*    */   }
/*    */ 
/*    */   
/*    */   public T parse(String jsonStr) {
/* 52 */     JSON json = (JSON)JSON.parse(jsonStr);
/* 53 */     return parse(json);
/*    */   }
/*    */ 
/*    */   
/*    */   public JSON getJson() {
/* 58 */     return (JSON)JSON.toJSON(this.bpmPluginDef);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 63 */     return StringUtil.lowerFirst(getClass().getSimpleName().replaceAll("PluginContext", ""));
/*    */   }
/*    */   
/*    */   protected abstract T parseJson(JSONObject paramJSONObject);
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/context/AbstractUserCalcPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */