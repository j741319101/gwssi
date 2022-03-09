/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.plugin.core.dao.BpmLeaderOptionLogDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmLeaderOptionLogManager;
/*    */
import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service("BpmLeaderOptionLogManager")
/*    */ public class BpmLeaderOptionmanagerImpl
/*    */   extends BaseManager<String, BpmLeaderOptionLog> implements BpmLeaderOptionLogManager {
/*    */   @Resource
/*    */   private BpmLeaderOptionLogDao bpmLeaderOptionLogDao;
/*    */   
/*    */   public void removeByInstId(String instId) {
/* 17 */     this.bpmLeaderOptionLogDao.removeByInstId(instId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmLeaderOptionmanagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */