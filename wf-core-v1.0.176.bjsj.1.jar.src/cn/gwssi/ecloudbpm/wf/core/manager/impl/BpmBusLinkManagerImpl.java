/*    */ package cn.gwssi.ecloudbpm.wf.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.core.dao.BpmBusLinkDao;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmBusLinkManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmBusLink;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("bpmBusLinkManager")
/*    */ public class BpmBusLinkManagerImpl
/*    */   extends BaseManager<String, BpmBusLink>
/*    */   implements BpmBusLinkManager
/*    */ {
/*    */   @Resource
/*    */   BpmBusLinkDao bpmBusLinkDao;
/*    */   
/*    */   public List<BpmBusLink> getByInstanceId(String instanceId) {
/* 26 */     return this.bpmBusLinkDao.getByInstanceId(instanceId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmBusLinkManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */