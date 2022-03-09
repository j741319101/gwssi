/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.formulas.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowSql;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IDataFlowService;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.formulas.def.FormulasPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.formulas.def.OprFormula;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class FormulasPluginExecuter
/*    */   extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, FormulasPluginDef> {
/* 22 */   private final ThreadLocal<List<IDataFlowSql>> listThreadLocal = new ThreadLocal<>();
/*    */   
/*    */   @Resource
/*    */   private IDataFlowService dataFlowService;
/*    */   
/*    */   public Void execute(DefaultBpmTaskPluginSession pluginSession, FormulasPluginDef pluginDef) {
/* 28 */     ActionCmd actionCmd = BpmContext.getActionModel();
/* 29 */     List<OprFormula> oprFormulas = pluginDef.getOprFormulas();
/* 30 */     Set<String> ids = new HashSet<>();
/* 31 */     oprFormulas.forEach(oprFormula -> {
/*    */           List<String> actions = oprFormula.getAction();
/*    */           
/*    */           if (CollectionUtil.isNotEmpty(actions) && CollectionUtil.contains(actions, actionCmd.getActionName())) {
/*    */             ids.add(oprFormula.getId());
/*    */           }
/*    */         });
/* 38 */     if (EventType.PRE_SAVE_BUS_EVENT == pluginSession.getEventType()) {
/* 39 */       this.listThreadLocal.remove();
/* 40 */       if (CollectionUtil.isEmpty(ids)) {
/* 41 */         return null;
/*    */       }
/* 43 */       this.listThreadLocal.set(this.dataFlowService.analysis(actionCmd.getBusData(), ids));
/* 44 */     } else if (EventType.TASK_POST_CREATE_EVENT == pluginSession.getEventType()) {
/* 45 */       List<IDataFlowSql> dataFlowSqls = this.listThreadLocal.get();
/* 46 */       if (CollectionUtil.isNotEmpty(dataFlowSqls)) {
/* 47 */         this.dataFlowService.execute(dataFlowSqls);
/*    */       }
/* 49 */       this.listThreadLocal.remove();
/*    */     } 
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/formulas/executer/FormulasPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */