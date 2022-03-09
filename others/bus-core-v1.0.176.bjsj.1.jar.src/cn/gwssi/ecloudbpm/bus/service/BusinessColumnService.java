/*    */ package cn.gwssi.ecloudbpm.bus.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessColumnService;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessColumnManager;
/*    */ import java.util.List;
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
/*    */ @Service
/*    */ public class BusinessColumnService
/*    */   implements IBusinessColumnService
/*    */ {
/*    */   @Autowired
/*    */   BusinessColumnManager businessColumnManager;
/*    */   
/*    */   public List<? extends IBusinessColumn> getByTableKey(String tableKey) {
/* 26 */     return this.businessColumnManager.getByTableKey(tableKey);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessColumnService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */