/*    */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.dao.SysConnectRecordDao;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysConnectRecordManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysConnectRecord;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("sysConnectRecordManager")
/*    */ public class SysConnectRecordManagerImpl
/*    */   extends BaseManager<String, SysConnectRecord>
/*    */   implements SysConnectRecordManager
/*    */ {
/*    */   @Resource
/*    */   SysConnectRecordDao sysConnectRecordDao;
/*    */   
/*    */   public List<SysConnectRecord> getByTargetId(String id, String type) {
/* 25 */     return this.sysConnectRecordDao.getByTargetId(id, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void bulkCreate(List<SysConnectRecord> list) {
/* 30 */     this.sysConnectRecordDao.bulkCreate(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeBySourceId(String sourceId, String type) {
/* 35 */     this.sysConnectRecordDao.removeBySourceId(sourceId, type);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysConnectRecordManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */