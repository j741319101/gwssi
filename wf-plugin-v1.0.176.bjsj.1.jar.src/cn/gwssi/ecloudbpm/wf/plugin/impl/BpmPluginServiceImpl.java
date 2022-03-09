/*    */ package cn.gwssi.ecloudbpm.wf.plugin.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskSkipType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.service.BpmPluginService;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.taskskip.context.TaskSkipPluginContext;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.global.taskskip.def.TaskSkipPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BpmPluginServiceImpl
/*    */   implements BpmPluginService {
/*    */   @Autowired
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public Boolean isSkipFirstNode(String defId) {
/* 21 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 22 */     BpmPluginContext context = def.getBpmPluginContext(TaskSkipPluginContext.class);
/* 23 */     if (context == null) return Boolean.valueOf(false);
/*    */     
/* 25 */     TaskSkipPluginDef pluginDef = (TaskSkipPluginDef)context.getBpmPluginDef();
/* 26 */     if (pluginDef == null || StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
/* 27 */       return Boolean.valueOf(false);
/*    */     }
/*    */     
/* 30 */     if (pluginDef.getSkipTypeArr().indexOf(TaskSkipType.FIRSTNODE_SKIP.getKey()) != -1) {
/* 31 */       return Boolean.valueOf(true);
/*    */     }
/*    */     
/* 34 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/impl/BpmPluginServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */