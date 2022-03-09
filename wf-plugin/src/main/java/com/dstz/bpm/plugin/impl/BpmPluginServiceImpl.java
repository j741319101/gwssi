/*    */ package com.dstz.bpm.plugin.impl;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*    */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*    */ import com.dstz.bpm.engine.constant.TaskSkipType;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import com.dstz.bpm.engine.plugin.service.BpmPluginService;
/*    */ import com.dstz.bpm.plugin.global.taskskip.context.TaskSkipPluginContext;
/*    */ import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
/*    */
import org.springframework.beans.factory.annotation.Autowired;
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/impl/BpmPluginServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */