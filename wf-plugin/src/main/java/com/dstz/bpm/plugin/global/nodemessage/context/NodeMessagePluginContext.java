/*    */ package com.dstz.bpm.plugin.global.nodemessage.context;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.constant.EventType;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmPluginDef;
/*    */ import com.dstz.bpm.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import com.dstz.bpm.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessage;
/*    */ import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
/*    */ import com.dstz.bpm.plugin.global.nodemessage.executer.NodeMessagePluginExecutor;
/*    */
import com.dstz.bpm.plugin.node.userassign.context.UserAssignPluginContext;
/*    */ import com.dstz.bpm.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class NodeMessagePluginContext
/*    */   extends AbstractBpmPluginContext<NodeMessagePluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = -8171025388788811778L;
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 32 */     List<EventType> list = new ArrayList<>();
/* 33 */     list.add(EventType.TASK_POST_CREATE_EVENT);
/* 34 */     list.add(EventType.START_EVENT);
/* 35 */     list.add(EventType.END_EVENT);
/* 36 */     list.add(EventType.TASK_COMPLETE_EVENT);
/* 37 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 42 */     return (Class) NodeMessagePluginExecutor.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected NodeMessagePluginDef parseFromJson(JSON pluginJson) {
/* 48 */     JSONArray array = (JSONArray)pluginJson;
/* 49 */     List<NodeMessage> messageList = new ArrayList<>();
/*    */     
/* 51 */     for (int i = 0; i < array.size(); i++) {
/* 52 */       JSONObject msgJson = array.getJSONObject(i);
/* 53 */       NodeMessage nodeMessage = (NodeMessage)JSON.toJavaObject((JSON)msgJson, NodeMessage.class);
/* 54 */       if (StringUtil.isNotEmpty(msgJson.getString("userRules"))) {
/* 55 */         UserAssignPluginContext userPluginContext = (UserAssignPluginContext)AppUtil.getBean(UserAssignPluginContext.class);
/* 56 */         userPluginContext.parse(msgJson.getString("userRules"));
/* 57 */         nodeMessage.setUserRules(((UserAssignPluginDef)userPluginContext.getBpmPluginDef()).getRuleList());
/*    */       } 
/*    */       
/* 60 */       messageList.add(nodeMessage);
/*    */     } 
/*    */ 
/*    */     
/* 64 */     return new NodeMessagePluginDef(messageList);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 70 */     return "节点消息";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/nodemessage/context/NodeMessagePluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */