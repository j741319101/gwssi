/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.def.BpmCarbonCopy;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.def.CarbonCopyPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.executor.CarbonCopyPluginExecutor;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.context.UserAssignPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.def.UserAssignPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class CarbonCopyPluginContext
/*    */   extends AbstractBpmPluginContext<CarbonCopyPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 4733258050731331492L;
/*    */   
/*    */   protected CarbonCopyPluginDef parseFromJson(JSON pluginJson) {
/* 34 */     JSONArray array = (JSONArray)pluginJson;
/* 35 */     Map<String, List<BpmCarbonCopy>> nodeEventBpmCarbonCopyMap = Collections.emptyMap();
/* 36 */     if (array != null && array.size() > 0) {
/* 37 */       nodeEventBpmCarbonCopyMap = new HashMap<>(array.size());
/* 38 */       for (int i = 0, l = array.size(); i < l; i++) {
/* 39 */         JSONObject msgJson = array.getJSONObject(i);
/* 40 */         BpmCarbonCopy bpmCarbonCopy = (BpmCarbonCopy)JSON.toJavaObject((JSON)msgJson, BpmCarbonCopy.class);
/* 41 */         if (StringUtil.isNotEmpty(msgJson.getString("userRules"))) {
/* 42 */           UserAssignPluginContext userPluginContext = (UserAssignPluginContext)AppUtil.getBean(UserAssignPluginContext.class);
/* 43 */           userPluginContext.parse(msgJson.getString("userRules"));
/* 44 */           bpmCarbonCopy.setUserRules(((UserAssignPluginDef)userPluginContext.getBpmPluginDef()).getRuleList());
/*    */         } 
/* 46 */         String key = CarbonCopyPluginDef.getMapKey(bpmCarbonCopy.getNodeId(), bpmCarbonCopy.getEvent());
/* 47 */         List<BpmCarbonCopy> bpmCarbonCopyList = nodeEventBpmCarbonCopyMap.computeIfAbsent(key, k -> new ArrayList());
/* 48 */         bpmCarbonCopyList.add(bpmCarbonCopy);
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     CarbonCopyPluginDef carbonCopyPluginDef = new CarbonCopyPluginDef();
/* 53 */     carbonCopyPluginDef.setNodeEventCarbonCopyMap(nodeEventBpmCarbonCopyMap);
/* 54 */     return carbonCopyPluginDef;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 59 */     List<EventType> eventTypeList = new ArrayList<>();
/* 60 */     eventTypeList.add(EventType.TASK_POST_CREATE_EVENT);
/* 61 */     eventTypeList.add(EventType.START_EVENT);
/* 62 */     eventTypeList.add(EventType.END_EVENT);
/* 63 */     eventTypeList.add(EventType.TASK_COMPLETE_EVENT);
/* 64 */     return eventTypeList;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 69 */     return (Class)CarbonCopyPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 74 */     return "节点消息抄送";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/context/CarbonCopyPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */