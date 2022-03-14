/*    */ package com.dstz.bpm.core.manager.impl;
/*    */ 
/*    */ import com.dstz.bpm.core.dao.BpmExecutionLinkDao;
/*    */ import com.dstz.bpm.core.manager.BpmExecutionLinkManager;
/*    */ import com.dstz.bpm.core.model.BpmExecutionLink;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("bpmExecutionLinkManager")
/*    */ public class BpmExecutionLinkManagerImpl
/*    */   extends BaseManager<String, BpmExecutionLink>
/*    */   implements BpmExecutionLinkManager
/*    */ {
/*    */   @Resource
/*    */   BpmExecutionLinkDao bpmExecutionLinkDao;
/*    */   
/*    */   public List<BpmExecutionLink> getByParam(String executionId, String objId, String type, String remark) {
/* 28 */     Map<String, Object> map = new HashMap<>();
/* 29 */     map.put("executionId", executionId);
/* 30 */     map.put("objId", objId);
/* 31 */     map.put("type", type);
/* 32 */     map.put("remark", remark);
/* 33 */     return this.bpmExecutionLinkDao.getByParam(map);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByParam(String executionId, String objId, String type, String remark) {
/* 38 */     Map<String, Object> map = new HashMap<>();
/* 39 */     map.put("executionId", executionId);
/* 40 */     map.put("objId", objId);
/* 41 */     map.put("type", type);
/* 42 */     map.put("remark", remark);
/* 43 */     this.bpmExecutionLinkDao.removeByParam(map);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmExecutionLinkManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */