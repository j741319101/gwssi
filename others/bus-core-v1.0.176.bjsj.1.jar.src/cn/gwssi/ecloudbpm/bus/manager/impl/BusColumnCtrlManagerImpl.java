/*    */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.dao.BusColumnCtrlDao;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusColumnCtrlManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusColumnCtrl;
/*    */ import com.dstz.base.api.constant.DbType;
/*    */ import com.dstz.base.db.datasource.DbContextHolder;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BusColumnCtrlManagerImpl
/*    */   extends BaseManager<String, BusColumnCtrl>
/*    */   implements BusColumnCtrlManager
/*    */ {
/*    */   @Resource
/*    */   BusColumnCtrlDao busColumnCtrlDao;
/*    */   
/*    */   public void removeByTableId(String tableId) {
/* 28 */     if (StringUtils.equals(DbContextHolder.getDbType(), DbType.DRDS.getKey())) {
/* 29 */       this.busColumnCtrlDao.selectByTableId(tableId).forEach(columnCtl -> this.busColumnCtrlDao.remove(columnCtl.getId()));
/*    */     }
/*    */     else {
/*    */       
/* 33 */       this.busColumnCtrlDao.removeByTableId(tableId);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public BusColumnCtrl getByColumnId(String columnId) {
/* 39 */     return this.busColumnCtrlDao.getByColumnId(columnId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusColumnCtrlManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */