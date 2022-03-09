/*    */ package com.dstz.bpm.engine.parser.flow;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.context.IExtendGlobalPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.core.util.RequestContext;
/*    */ import com.dstz.base.core.validate.ValidateUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */
import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class PluginsParser
/*    */   extends AbsFlowParse<BpmPluginContext>
/*    */ {
/*    */   public Object parseDef(DefaultBpmProcessDef bpmProcessDef, String json) {
/* 26 */     JSONObject plugins = JSON.parseObject(json);
/* 27 */     setDefaultPlugins(plugins);
/*    */     
/* 29 */     ArrayList<BpmPluginContext> pluginContextList = new ArrayList<>();
/* 30 */     for (String pluginName : plugins.keySet()) {
/* 31 */       BpmPluginContext pluginContext = (BpmPluginContext)AppUtil.getBean(pluginName + "PluginContext");
/* 32 */       if (pluginContext == null) {
/* 33 */         this.LOG.error("插件解析失败，不存在的插件：{}", pluginName + "PluginContext");
/*    */         
/*    */         continue;
/*    */       } 
/* 37 */       if (pluginContext instanceof BpmPluginContext) {
/*    */         try {
/* 39 */           pluginContext.parse((JSON)plugins.get(pluginName));
/* 40 */           BpmPluginDef def = pluginContext.getBpmPluginDef();
/*    */           
/* 42 */           ValidateUtil.validate(def);
/* 43 */         } catch (Exception e) {
/*    */ 
/*    */           
/* 46 */           if (RequestContext.getHttpServletRequest() != null && RequestContext.getHttpServletRequest().getRequestURI().startsWith("/model/") && RequestContext.getHttpServletRequest().getRequestURI().endsWith("/save")) {
/* 47 */             throw new BusinessException(String.format("全局插件【%s】解析失败！%s", new Object[] { pluginContext.getTitle(), e.getMessage() }), e);
/*    */           }
/* 49 */           this.LOG.error("插件{}解析失败:{}！", new Object[] { pluginContext.getTitle(), e.getMessage(), e });
/*    */         } 
/*    */       }
/*    */ 
/*    */       
/* 54 */       pluginContextList.add(pluginContext);
/*    */     } 
/*    */     
/* 57 */     return pluginContextList;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 63 */     return "plugins";
/*    */   }
/*    */ 
/*    */   
/*    */   public String validate(Object o) {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setDefaultPlugins(JSONObject plugins) {
/* 77 */     List<IExtendGlobalPluginContext> extentGlobalPluginContexts = AppUtil.getImplInstanceArray(IExtendGlobalPluginContext.class);
/* 78 */     extentGlobalPluginContexts.stream().filter(context -> !plugins.containsKey(context.getKey())).forEach(context -> plugins.put(context.getKey(), new JSONObject()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 86 */     ArrayList<BpmPluginContext> pluginContextList = (ArrayList<BpmPluginContext>)object;
/*    */     
/* 88 */     bpmProcessDef.setPluginContextList(pluginContextList);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/PluginsParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */