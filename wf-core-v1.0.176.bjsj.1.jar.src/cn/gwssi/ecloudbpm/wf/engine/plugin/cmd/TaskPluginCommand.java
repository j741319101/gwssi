/*    */ package cn.gwssi.ecloudbpm.wf.engine.plugin.cmd;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.cmd.TaskCommand;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.factory.BpmPluginFactory;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.factory.BpmPluginSessionFactory;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.BpmExecutionPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class TaskPluginCommand
/*    */   implements TaskCommand
/*    */ {
/* 33 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public void execute(EventType eventType, TaskActionCmd actionModel) {
/* 39 */     this.LOG.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【{}】插件执行开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓", eventType.getValue());
/*    */     
/* 41 */     String defId = actionModel.getBpmTask().getDefId();
/* 42 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 43 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, actionModel.getNodeId());
/*    */     
/* 45 */     BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession(actionModel, eventType);
/*    */     
/* 47 */     List<BpmPluginContext> allPlugins = new ArrayList<>(bpmNodeDef.getBpmPluginContexts().size() + bpmProcessDef.getBpmPluginContexts().size());
/*    */     
/* 49 */     bpmNodeDef.getBpmPluginContexts().forEach(context -> {
/*    */           if (context.getBpmPluginDef() instanceof cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmExecutionPluginDef) {
/*    */             allPlugins.add(context);
/*    */           }
/*    */         });
/*    */     
/* 55 */     bpmProcessDef.getBpmPluginContexts().forEach(globalContext -> allPlugins.add(globalContext));
/*    */ 
/*    */ 
/*    */     
/* 59 */     Collections.sort(allPlugins);
/*    */ 
/*    */     
/* 62 */     this.LOG.trace("============【节点插件】======== {} 个 =========", Integer.valueOf(allPlugins.size()));
/* 63 */     for (BpmPluginContext bpmPluginContext : allPlugins) {
/* 64 */       BpmExecutionPlugin bpmExecutionPlugin = BpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
/* 65 */       if (bpmExecutionPlugin == null)
/*    */         continue; 
/*    */       try {
/* 68 */         this.LOG.debug("==>【{}】节点执行插件【{}】", bpmNodeDef.getName(), bpmPluginContext.getTitle());
/* 69 */         bpmExecutionPlugin.execute(bpmTaskSession, bpmPluginContext.getBpmPluginDef());
/* 70 */       } catch (BusinessMessage e) {
/* 71 */         throw e;
/* 72 */       } catch (Exception e) {
/* 73 */         this.LOG.error("【{}】节点执行插件【{}】出现异常:{}", new Object[] { bpmNodeDef.getName(), bpmPluginContext.getTitle(), e.getMessage(), e });
/* 74 */         throw new BusinessException(bpmPluginContext.getTitle() + " " + e.getMessage(), BpmStatusCode.PLUGIN_ERROR, e);
/*    */       } 
/*    */     } 
/*    */     
/* 78 */     this.LOG.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【{}】插件执行完毕↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", eventType.getValue());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/cmd/TaskPluginCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */