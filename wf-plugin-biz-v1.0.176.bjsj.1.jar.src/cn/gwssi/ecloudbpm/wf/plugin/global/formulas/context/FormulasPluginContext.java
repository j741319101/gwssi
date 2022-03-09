/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.formulas.context;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.runtime.RunTimePlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.context.AbstractBpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.formulas.def.FormulasPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.formulas.def.OprFormula;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.formulas.executer.FormulasPluginExecuter;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Scope("prototype")
/*    */ public class FormulasPluginContext
/*    */   extends AbstractBpmPluginContext<FormulasPluginDef>
/*    */ {
/*    */   private static final long serialVersionUID = 8784633971785686365L;
/*    */   
/*    */   protected FormulasPluginDef parseFromJson(JSON json) {
/* 26 */     if (json == null || StringUtil.isEmpty(json.toJSONString())) {
/* 27 */       return new FormulasPluginDef();
/*    */     }
/* 29 */     List<OprFormula> oprFormulas = JSON.parseArray(json.toJSONString(), OprFormula.class);
/* 30 */     FormulasPluginDef formulasPluginDef = new FormulasPluginDef();
/* 31 */     formulasPluginDef.setOprFormulas(oprFormulas);
/* 32 */     return formulasPluginDef;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<EventType> getEventTypes() {
/* 37 */     List<EventType> eventTypes = new ArrayList<>();
/* 38 */     eventTypes.add(EventType.PRE_SAVE_BUS_EVENT);
/* 39 */     eventTypes.add(EventType.TASK_POST_COMPLETE_EVENT);
/* 40 */     return eventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends RunTimePlugin> getPluginClass() {
/* 45 */     return (Class)FormulasPluginExecuter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 50 */     return "数据流插件";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/formulas/context/FormulasPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */