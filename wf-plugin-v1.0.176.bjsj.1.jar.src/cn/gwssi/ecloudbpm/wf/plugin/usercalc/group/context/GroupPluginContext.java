/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.group.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractUserCalcPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.group.def.GroupPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.group.executer.GroupPluginExecutor;
/*    */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class GroupPluginContext
/*    */   extends AbstractUserCalcPluginContext
/*    */ {
/*    */   private static final long serialVersionUID = -6084686546165511275L;
/*    */   
/*    */   public String getDescription() {
/* 24 */     GroupPluginDef def = (GroupPluginDef)getBpmPluginDef();
/* 25 */     if (def == null) return "";
/*    */     
/* 27 */     return String.format("%s[%s]", new Object[] { def.getTypeName(), def.getGroupName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 32 */     return "组";
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 37 */     return (Class)GroupPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BpmUserCalcPluginDef parseJson(JSONObject pluginJson) {
/* 42 */     GroupPluginDef def = new GroupPluginDef();
/* 43 */     String groupType = JsonUtil.getString(pluginJson, "type");
/* 44 */     String groupTypeName = JsonUtil.getString(pluginJson, "typeName");
/*    */     
/* 46 */     def.setType(groupType);
/* 47 */     def.setTypeName(groupTypeName);
/*    */     
/* 49 */     String groupKey = JsonUtil.getString(pluginJson, "groupKey");
/* 50 */     String groupName = JsonUtil.getString(pluginJson, "groupName");
/* 51 */     def.setGroupKey(groupKey);
/* 52 */     def.setGroupName(groupName);
/* 53 */     return (BpmUserCalcPluginDef)def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/group/context/GroupPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */