/*    */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.dao.SysMetaColumnDao;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysMetaColumnManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.MetaColumn;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("sysMetaColumnManager")
/*    */ public class SysMetaColumnManagerImpl
/*    */   extends BaseManager<String, MetaColumn>
/*    */   implements SysMetaColumnManager
/*    */ {
/*    */   @Resource
/*    */   SysMetaColumnDao metaColumnDao;
/*    */   
/*    */   public void updateStatus(String id, int enabled) {
/* 21 */     this.metaColumnDao.updateStatus(id, enabled);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateGroupId(String id, String groupId) {
/* 26 */     this.metaColumnDao.updateGroupId(id, groupId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysMetaColumnManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */