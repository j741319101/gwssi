/*     */ package cn.gwssi.ecloudbpm.bus.service.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusStatusCode;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessObjectPersistenceType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.remote.RemoteBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusinessDataPersistenceService;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusinessDataService;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessError;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RequestContext;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RestTemplateUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class BusinessDataPersistenceHttpService
/*     */   implements BusinessDataPersistenceService
/*     */ {
/*     */   @Autowired
/*     */   protected BusinessDataService businessDataService;
/*     */   
/*     */   public String type() {
/*  37 */     return BusinessObjectPersistenceType.HTTP.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveData(BusinessData businessData) {
/*  42 */     BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
/*     */     
/*  44 */     RemoteBusinessData<JSONObject> remoteBusinessData = new RemoteBusinessData();
/*  45 */     JSONObject data = this.businessDataService.assemblyFormDefData((IBusinessData)businessData);
/*  46 */     remoteBusinessData.setData(data);
/*  47 */     remoteBusinessData.setBoKey(businessObject.getKey());
/*     */ 
/*     */     
/*  50 */     JSONObject config = JSON.parseObject(businessObject.getPerTypeConfig());
/*  51 */     String url = RequestContext.getUrl(config.getString("saveDataUrl"));
/*     */ 
/*     */     
/*  54 */     ResultMsg<String> result = (ResultMsg<String>)RestTemplateUtil.post(url, remoteBusinessData, ResultMsg.class);
/*  55 */     if (!result.getIsOk().booleanValue()) {
/*  56 */       throw new BusinessError("saveDataUrl请求返回isOk为false", BusStatusCode.PERSISTENCE_HTTP_ERR);
/*     */     }
/*  58 */     businessData.setPk(result.getData());
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveData(BusinessData businessData, JSONObject instData) {
/*  63 */     saveData(businessData);
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessData loadData(BusinessObject businessObject, Object id) {
/*  68 */     BusinessData businessData = new BusinessData();
/*     */     
/*  70 */     BusTableRel busTableRel = businessObject.getRelation();
/*  71 */     businessData.setBusTableRel(busTableRel);
/*  72 */     if (id == null) {
/*  73 */       return businessData;
/*     */     }
/*     */ 
/*     */     
/*  77 */     JSONObject config = JSON.parseObject(businessObject.getPerTypeConfig());
/*  78 */     String url = RequestContext.getUrl(config.getString("loadDataUrl"));
/*     */ 
/*     */     
/*  81 */     RemoteBusinessData remoteBusinessData = new RemoteBusinessData();
/*  82 */     remoteBusinessData.setBoKey(businessObject.getKey());
/*  83 */     remoteBusinessData.setId(id);
/*     */     
/*  85 */     JSONObject result = (JSONObject)RestTemplateUtil.post(url, remoteBusinessData, JSONObject.class);
/*  86 */     if (!result.getBooleanValue("isOk")) {
/*  87 */       throw new BusinessError("loadDataUrl请求返回isOk为false", BusStatusCode.PERSISTENCE_HTTP_ERR);
/*     */     }
/*  89 */     JSONObject data = result.getJSONObject("data").getJSONObject("data");
/*  90 */     businessData = (BusinessData)this.businessDataService.parseBusinessData(data, businessObject.getKey());
/*     */     
/*  92 */     return businessData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeData(BusinessObject businessObject, Object id) {
/*  98 */     JSONObject config = JSON.parseObject(businessObject.getPerTypeConfig());
/*  99 */     String url = RequestContext.getUrl(config.getString("removeDataUrl"));
/*     */ 
/*     */     
/* 102 */     RemoteBusinessData remoteBusinessData = new RemoteBusinessData();
/* 103 */     remoteBusinessData.setBoKey(businessObject.getKey());
/* 104 */     remoteBusinessData.setId(id);
/*     */     
/* 106 */     JSONObject result = (JSONObject)RestTemplateUtil.post(url, remoteBusinessData, JSONObject.class);
/* 107 */     if (!result.getBooleanValue("isOk")) {
/* 108 */       throw new BusinessError("removeDataUrl请求返回isOk为false", BusStatusCode.PERSISTENCE_HTTP_ERR);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 113 */     JSONObject json = JSON.parseObject("{\"code\":\"200\",\"isOk\":true,\"page\":1,\"pageSize\":10,\"rows\":[{\"a\":\"1\",\"id\":409729659240185857}],\"total\":1}");
/* 114 */     System.out.println(json.toJSONString());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/impl/BusinessDataPersistenceHttpService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */