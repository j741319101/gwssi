/*    */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.dao.BusinessColumnDao;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessColumnManager;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BusinessColumnManagerImpl
/*    */   extends BaseManager<String, BusinessColumn>
/*    */   implements BusinessColumnManager
/*    */ {
/*    */   @Resource
/*    */   BusinessColumnDao businessColumnDao;
/*    */   @Autowired
/*    */   BusinessTableManager businessTableManager;
/*    */   
/*    */   public void removeByTableId(String tableId) {
/* 29 */     this.businessColumnDao.removeByTableId(tableId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BusinessColumn> getByTableId(String tableId) {
/* 34 */     return this.businessColumnDao.getByTableId(tableId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BusinessColumn> getByTableKey(String tableKey) {
/* 39 */     BusinessTable businessTable = this.businessTableManager.getByKey(tableKey);
/* 40 */     if (null != businessTable) {
/* 41 */       return getByTableId(businessTable.getId());
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusinessColumnManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */