/*    */ package cn.gwssi.ecloudbpm.bus.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.Map;
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
/*    */ public class BusinessDataPersistenceServiceFactory
/*    */ {
/*    */   private static BusinessDataPersistenceService getService(String type) {
/* 35 */     Map<String, BusinessDataPersistenceService> map = AppUtil.getImplInstance(BusinessDataPersistenceService.class);
/* 36 */     for (Map.Entry<String, BusinessDataPersistenceService> entry : map.entrySet()) {
/* 37 */       if (((BusinessDataPersistenceService)entry.getValue()).type().equals(type)) {
/* 38 */         return entry.getValue();
/*    */       }
/*    */     } 
/* 41 */     throw new BusinessException(String.format("找不到类型[%s]的业务数据持久化的实现类", new Object[] { type }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void saveData(BusinessData businessData) {
/* 52 */     BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
/* 53 */     BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
/* 54 */     businessDataPersistenceService.saveData(businessData);
/*    */   }
/*    */   
/*    */   public static void saveData(BusinessData businessData, JSONObject instData) {
/* 58 */     BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
/* 59 */     BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
/* 60 */     businessDataPersistenceService.saveData(businessData);
/*    */   }
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
/*    */   public static BusinessData loadData(BusinessObject businessObject, Object id) {
/* 73 */     BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
/* 74 */     return businessDataPersistenceService.loadData(businessObject, id);
/*    */   }
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
/*    */   public static void removeData(BusinessObject businessObject, Object id) {
/* 88 */     BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
/* 89 */     businessDataPersistenceService.removeData(businessObject, id);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessDataPersistenceServiceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */