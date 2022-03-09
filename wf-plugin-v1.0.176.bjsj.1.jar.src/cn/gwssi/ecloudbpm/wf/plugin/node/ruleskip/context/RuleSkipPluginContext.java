/*    */ package cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.def.JumpRule;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.def.RuleSkipPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.ruleskip.executer.RuleSkipPluginExecutor;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class RuleSkipPluginContext
/*    */   extends AbstractBpmPluginContext<RuleSkipPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 8784633971785686365L;
/*    */   
/*    */   public List getEventTypes() {
/* 25 */     List<EventType> eventTypes = new ArrayList<>();
/* 26 */     eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
/* 27 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 32 */     return (Class)RuleSkipPluginExecutor.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 37 */     return "规则跳转";
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleSkipPluginDef parseFromJson(JSON json) {
/* 42 */     RuleSkipPluginDef def = new RuleSkipPluginDef();
/*    */     
/* 44 */     List<JumpRule> list = JSON.parseArray(json.toJSONString(), JumpRule.class);
/* 45 */     def.setJumpRules(list);
/* 46 */     return def;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/ruleskip/context/RuleSkipPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */