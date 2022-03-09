/*    */ package com.dstz.bpm.plugin.global.carboncopy.executor;
/*    */ 
/*    */ import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class CarbonCopyTaskAction extends DefaultExtendTaskAction {
/*    */   @Autowired
/*    */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*    */   @Autowired
/*    */   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*    */   
/*    */   public void deleteDataByInstId(String instId) {
/* 17 */     this.bpmCarbonCopyReceiveManager.removeByInstId(instId);
/* 18 */     this.bpmCarbonCopyRecordManager.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/carboncopy/executor/CarbonCopyTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */