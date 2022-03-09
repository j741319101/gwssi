/*    */ package cn.gwssi.ecloudbpm.wf.engine.plugin.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ExtractType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.constant.LogicType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.PluginParse;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.UserCalcPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
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