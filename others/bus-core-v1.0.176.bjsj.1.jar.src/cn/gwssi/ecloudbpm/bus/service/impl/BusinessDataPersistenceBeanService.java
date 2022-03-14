/*    */ package cn.gwssi.ecloudbpm.bus.service.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.constant.BusStatusCode;
/*    */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessObjectPersistenceType;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*    */ import cn.gwssi.ecloudbpm.bus.api.remote.RemoteBusinessData;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataPersistenceBeanService;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*    */ import cn.gwssi.ecloudbpm.bus.service.BusinessDataPersistenceService;
/*    */ import cn.gwssi.ecloudbpm.bus.service.BusinessDataService;
/*    */ import com.dstz.base.api.exception.BusinessError;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
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
/*    */ public class BusinessDataPersistenceBeanService
/*    */   implements BusinessDataPersistenceService
/*    */ {
/*    */   @Autowired
/*    */   protected BusinessDataService businessDataService;
/*    */   
/*    */   public String type() {
/* 34 */     return BusinessObjectPersistenceType.BEAN.getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public void saveData(BusinessData businessData) {
/* 39 */     BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
/* 40 */     IBusinessDataPersistenceBeanService beanService = getBeanService(businessObject);
/*    */     
/* 42 */     JSONObject data = this.businessDataService.assemblyFormDefData((IBusinessData)businessData);
/* 43 */     RemoteBusinessData<JSONObject> busData = new RemoteBusinessData(data, businessObject.getKey());
/*    */     
/* 45 */     Object id = beanService.saveData(busData);
/* 46 */     businessData.setPk(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void saveData(BusinessData businessData, JSONObject instData) {
/* 51 */     BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
/* 52 */     IBusinessDataPersistenceBeanService beanService = getBeanService(businessObject);
/*    */     
/* 54 */     JSONObject data = this.businessDataService.assemblyFormDefData((IBusinessData)businessData);
/* 55 */     RemoteBusinessData<JSONObject> busData = new RemoteBusinessData(data, businessObject.getKey());
/* 56 */     busData.setBpmInstanceData(instData);
/* 57 */     Object id = beanService.saveData(busData);
/* 58 */     businessData.setPk(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public BusinessData loadData(BusinessObject businessObject, Object id) {
/* 63 */     IBusinessDataPersistenceBeanService beanService = getBeanService(businessObject);
/*    */     
/* 65 */     RemoteBusinessData<JSONObject> busData = new RemoteBusinessData(businessObject.getKey(), id);
/* 66 */     RemoteBusinessData<JSONObject> result = beanService.loadData(busData);
/*    */     
/* 68 */     return (BusinessData)this.businessDataService.parseBusinessData((JSONObject)result.getData(), businessObject.getKey());
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeData(BusinessObject businessObject, Object id) {
/* 73 */     IBusinessDataPersistenceBeanService beanService = getBeanService(businessObject);
/*    */     
/* 75 */     RemoteBusinessData remoteBusinessData = new RemoteBusinessData(businessObject.getKey(), id);
/* 76 */     beanService.removeData(remoteBusinessData);
/*    */   }
/*    */   
/*    */   private IBusinessDataPersistenceBeanService getBeanService(BusinessObject businessObject) {
/* 80 */     String name = businessObject.getPerTypeConfig();
/* 81 */     IBusinessDataPersistenceBeanService beanService = (IBusinessDataPersistenceBeanService)AppUtil.getBean(name);
/* 82 */     if (beanService == null) {
/* 83 */       throw new BusinessError("找不到名字为【" + name + "】的bean", BusStatusCode.PERSISTENCE_BEAN_ERR);
/*    */     }
/* 85 */     return beanService;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/impl/BusinessDataPersistenceBeanService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */