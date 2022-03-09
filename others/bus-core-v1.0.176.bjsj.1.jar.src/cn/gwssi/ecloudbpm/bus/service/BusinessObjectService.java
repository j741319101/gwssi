/*    */ package cn.gwssi.ecloudbpm.bus.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*    */ import com.alibaba.fastjson.JSONObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BusinessObjectService
/*    */   implements IBusinessObjectService
/*    */ {
/*    */   @Autowired
/*    */   BusinessObjectManager businessObjectManager;
/*    */   
/*    */   public IBusinessObject getByKey(String key) {
/* 30 */     return (IBusinessObject)this.businessObjectManager.getByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public IBusinessObject getFilledByKey(String key) {
/* 35 */     return (IBusinessObject)this.businessObjectManager.getFilledByKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<JSONObject> boTreeData(String key) {
/* 40 */     return this.businessObjectManager.boTreeData(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBoOverallArrangement(String key) {
/* 45 */     return this.businessObjectManager.getOverallArrangementByCode(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessObjectService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */