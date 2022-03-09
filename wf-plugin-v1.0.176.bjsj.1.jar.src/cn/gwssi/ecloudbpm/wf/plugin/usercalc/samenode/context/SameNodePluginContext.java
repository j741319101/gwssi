/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.samenode.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractUserCalcPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.samenode.def.SameNodePluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.samenode.executer.SameNodePluginExecutor;
/*    */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class SameNodePluginContext
/*    */   extends AbstractUserCalcPluginContext<SameNodePluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 919433269116580830L;
/*    */   
/*    */   public String getDescription() {
/* 25 */     SameNodePluginDef def = (SameNodePluginDef)getBpmPluginDef();
/* 26 */     if (def == null) return ""; 
/* 27 */     return "节点：" + def.getNodeId();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 32 */     return "相同节点执行人";
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 37 */     return (Class)SameNodePluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SameNodePluginDef parseJson(JSONObject pluginJson) {
/* 42 */     SameNodePluginDef def = new SameNodePluginDef();
/* 43 */     String nodeId = JsonUtil.getString(pluginJson, "nodeId");
/* 44 */     def.setNodeId(nodeId);
/* 45 */     return def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/samenode/context/SameNodePluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */