/*    */ package com.dstz.bpm.engine.parser.node;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.context.IExtendNodePluginContext;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.core.util.RequestContext;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class NodePluginsParser
/*    */   extends AbsNodeParse<BpmPluginContext>
/*    */ {
/*    */   public Object parseDef(BaseBpmNodeDef userNodeDef, String json) {
/* 27 */     JSONObject plugins = JSON.parseObject(json);
/* 28 */     ArrayList<BpmPluginContext> pluginContextList = new ArrayList<>();
/* 29 */     setDefaultPlugins(plugins);
/* 30 */     for (String pluginName : plugins.keySet()) {
/* 31 */       BpmPluginContext pluginContext = (BpmPluginContext)AppUtil.getBean(pluginName + "PluginContext");
/* 32 */       if (pluginContext == null) {
/* 33 */         this.LOG.error("插件解析失败，不存在的插件：{}", pluginName + "PluginContext");
/*    */         continue;
/*    */       } 
/* 36 */       if (pluginContext instanceof BpmPluginContext) {
/* 37 */         Object object = plugins.get(pluginName);
/*    */         try {
/* 39 */           validate(pluginContext.parse((JSON)object));
/* 40 */         } catch (Exception e) {
/*    */           
/* 42 */           if (RequestContext.getHttpServletRequest() != null && RequestContext.getHttpServletRequest().getRequestURI().startsWith("/model/") && RequestContext.getHttpServletRequest().getRequestURI().endsWith("/save")) {
/* 43 */             throw new BusinessException(String.format("节点【%s】插件【%s】解析失败！%s", new Object[] { userNodeDef.getName(), pluginContext.getTitle(), e.getMessage() }), e);
/*    */           }
/* 45 */           this.LOG.error("插件{}解析失败:{}！", new Object[] { pluginContext.getTitle(), e.getMessage(), e });
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 50 */       pluginContextList.add(pluginContext);
/*    */     } 
/* 52 */     return pluginContextList;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 57 */     return "plugins";
/*    */   }
/*    */ 
/*    */   
/*    */   public String validate(Object o) {
/* 62 */     return super.validate(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
/* 67 */     ArrayList<BpmPluginContext> pluginContextList = (ArrayList<BpmPluginContext>)object;
/*    */     
/* 69 */     userNodeDef.setBpmPluginContexts(pluginContextList);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setDefaultPlugins(JSONObject plugins) {
/* 76 */     List<IExtendNodePluginContext> extentGlobalPluginContexts = AppUtil.getImplInstanceArray(IExtendNodePluginContext.class);
/* 77 */     extentGlobalPluginContexts.stream().filter(context -> !plugins.containsKey(context.getKey())).forEach(context -> plugins.put(context.getKey(), new JSONObject()));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/node/NodePluginsParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */