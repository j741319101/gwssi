/*    */ package cn.gwssi.ecloudbpm.bus.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessTable;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessTableService;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BusinessTableService
/*    */   implements IBusinessTableService
/*    */ {
/*    */   @Autowired
/*    */   BusinessTableManager businessTableManager;
/*    */   
/*    */   public IBusinessTable getByKey(String key) {
/* 26 */     return (IBusinessTable)this.businessTableManager.getByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public IBusinessTable getFilledByKey(String key) {
/* 31 */     return (IBusinessTable)this.businessTableManager.getFilledByKey(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessTableService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */