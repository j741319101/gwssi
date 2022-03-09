/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.datalog.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmSubmitDataLogManager;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmSubmitDataLog;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.datalog.def.DataLogPluginDef;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class DataLogPluginExecutor
/*    */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, DataLogPluginDef>
/*    */ {
/*    */   @Resource
/*    */   BpmProcessDefService processDefService;
/*    */   @Resource
/*    */   private ThreadPoolTaskExecutor threadPoolTaskExecutor;
/*    */   @Resource
/*    */   private IBusinessDataService businessDataService;
/*    */   @Resource
/*    */   BpmSubmitDataLogManager bpmSubmitDataLogManager;
/*    */   
/*    */   public Void execute(BpmExecutionPluginSession pluginSession, DataLogPluginDef pluginDef) {
/* 38 */     BaseActionCmd cmd = (BaseActionCmd)BpmContext.getActionModel();
/*    */     
/* 40 */     JSONObject businessData = cmd.getBusData();
/*    */     
/* 42 */     if (businessData == null || businessData.isEmpty()) return null;
/*    */ 
/*    */     
/* 45 */     String defId = pluginSession.getBpmInstance().getDefId();
/*    */     
/* 47 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.processDefService.getBpmProcessDef(defId);
/* 48 */     if (!processDef.getExtProperties().isLogSubmitData()) return null;
/*    */     
/* 50 */     BpmSubmitDataLog submitDataLog = new BpmSubmitDataLog();
/* 51 */     submitDataLog.setData(businessData.toJSONString());
/* 52 */     submitDataLog.setDestination(cmd.getDestination());
/* 53 */     if (cmd.getExtendConf() != null) {
/* 54 */       submitDataLog.setExtendConf(cmd.getExtendConf().toJSONString());
/*    */     }
/*    */     
/* 57 */     if (cmd instanceof DefualtTaskActionCmd) {
/* 58 */       submitDataLog.setTaskId(((DefualtTaskActionCmd)cmd).getBpmTask().getId());
/*    */     }
/* 60 */     submitDataLog.setInstId(cmd.getInstanceId());
/*    */     
/* 62 */     this.threadPoolTaskExecutor.execute(() -> this.bpmSubmitDataLogManager.create(submitDataLog));
/*    */     
/* 64 */     this.LOG.debug("记录流程提交业务数据 ");
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/datalog/executer/DataLogPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */