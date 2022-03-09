/*     */ package cn.gwssi.ecloudbpm.bus.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.bus.executor.assemblyval.AssemblyValExecuteChain;
/*     */ import cn.gwssi.ecloudbpm.bus.executor.assemblyval.AssemblyValParam;
/*     */ import cn.gwssi.ecloudbpm.bus.executor.parseval.ParseValExecuteChain;
/*     */ import cn.gwssi.ecloudbpm.bus.executor.parseval.ParseValParam;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.executor.ExecutorFactory;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class BusinessDataService
/*     */   implements IBusinessDataService
/*     */ {
/*     */   @Autowired
/*     */   BusinessObjectManager businessObjectManager;
/*     */   
/*     */   public void saveNewFormDefData(JSONObject data, IBusinessPermission businessPermission) throws BusinessException {
/*  39 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)data.entrySet()) {
/*  40 */       String boKey = entry.getKey();
/*  41 */       JSONObject boData = (JSONObject)entry.getValue();
/*  42 */       BusinessData businessData = (BusinessData)parseBusinessData(boData, boKey);
/*  43 */       Object pk = businessData.getPk();
/*  44 */       if (pk != null && 
/*  45 */         !pk.toString().equals("")) {
/*  46 */         throw new BusinessException("当前接口不允许编辑数据", FormStatusCode.PARAM_ILLEGAL);
/*     */       }
/*     */       
/*  49 */       businessData.getBusTableRel().getBusObj().setPermission((BusObjPermission)businessPermission.getBusObj(boKey));
/*  50 */       BusinessDataPersistenceServiceFactory.saveData(businessData);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveFormDefData(JSONObject data, IBusinessPermission businessPermission) {
/*  56 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)data.entrySet()) {
/*  57 */       String boKey = entry.getKey();
/*  58 */       JSONObject boData = (JSONObject)entry.getValue();
/*  59 */       BusinessData businessData = (BusinessData)parseBusinessData(boData, boKey);
/*  60 */       businessData.getBusTableRel().getBusObj().setPermission((BusObjPermission)businessPermission.getBusObj(boKey));
/*  61 */       BusinessDataPersistenceServiceFactory.saveData(businessData);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getFormDefData(IBusinessObject businessObject, Object id) {
/*  67 */     BusinessData businessData = (BusinessData)loadData(businessObject, id, true);
/*  68 */     JSONObject data = new JSONObject();
/*     */     
/*  70 */     assemblyFormDefData(data, (IBusinessData)businessData);
/*  71 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getFormDefData(IBusinessObject businessObject) {
/*  76 */     BusinessData businessData = (BusinessData)loadData(businessObject, Boolean.valueOf(true));
/*  77 */     JSONObject data = new JSONObject();
/*  78 */     assemblyFormDefData(data, (IBusinessData)businessData);
/*  79 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initFormDefData(BusinessData businessData) {
/*  91 */     BusTableRel busTableRel = businessData.getBusTableRel();
/*  92 */     businessData.setDbData(busTableRel.getTable().initDbData());
/*     */ 
/*     */     
/*  95 */     for (BusTableRel rel : busTableRel.getChildren()) {
/*  96 */       if (BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType())) {
/*  97 */         BusinessData childData = new BusinessData();
/*  98 */         childData.setBusTableRel(rel);
/*  99 */         initFormDefData(childData);
/* 100 */         businessData.addChildren(childData);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONObject assemblyFormDefData(IBusinessData businessData) {
/* 108 */     JSONObject data = new JSONObject();
/* 109 */     assemblyFormDefData(data, businessData);
/* 110 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assemblyFormDefData(JSONObject data, IBusinessData ibusinessData) {
/* 122 */     BusinessData businessData = (BusinessData)ibusinessData;
/*     */     
/* 124 */     AssemblyValParam param = new AssemblyValParam(data, businessData);
/* 125 */     ExecutorFactory.execute(AssemblyValExecuteChain.class, param);
/*     */ 
/*     */     
/* 128 */     for (Map.Entry<String, List<BusinessData>> entry : (Iterable<Map.Entry<String, List<BusinessData>>>)businessData.getChildren().entrySet()) {
/* 129 */       String tableKey = entry.getKey();
/* 130 */       List<BusinessData> children = entry.getValue();
/* 131 */       if (BusTableRelType.ONE_TO_ONE.equalsWithKey(((BusinessData)children.get(0)).getBusTableRel().getType())) {
/* 132 */         JSONObject cData = new JSONObject();
/* 133 */         if (!children.isEmpty()) {
/* 134 */           cData = new JSONObject(((BusinessData)children.get(0)).getData());
/*     */         }
/* 136 */         assemblyFormDefData(cData, (IBusinessData)children.get(0));
/* 137 */         data.put(tableKey, cData); continue;
/*     */       } 
/* 139 */       JSONArray dataList = new JSONArray();
/* 140 */       for (BusinessData bd : children) {
/* 141 */         JSONObject cData = new JSONObject(bd.getData());
/* 142 */         assemblyFormDefData(cData, (IBusinessData)bd);
/* 143 */         dataList.add(cData);
/*     */       } 
/* 145 */       data.put(tableKey + "List", dataList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BusinessData analysisFormDefData(Object object, BusTableRel relation) {
/* 160 */     BusinessData businessData = new BusinessData();
/* 161 */     businessData.setBusTableRel(relation);
/*     */     
/* 163 */     if (object instanceof JSONObject) {
/* 164 */       JSONObject jsonObject = (JSONObject)object;
/* 165 */       Map<String, Object> bdData = new HashMap<>();
/* 166 */       for (Map.Entry<String, Object> enty : (Iterable<Map.Entry<String, Object>>)jsonObject.entrySet()) {
/*     */         
/* 168 */         if (!(enty.getValue() instanceof JSONArray) && !(enty.getValue() instanceof JSONObject)) {
/* 169 */           ParseValParam param = new ParseValParam(enty.getKey(), enty.getValue(), bdData, relation);
/* 170 */           ExecutorFactory.execute(ParseValExecuteChain.class, param);
/*     */         } 
/*     */ 
/*     */         
/* 174 */         if (enty.getValue() instanceof JSONArray) {
/* 175 */           String tableKey = ((String)enty.getKey()).substring(0, ((String)enty.getKey()).length() - 4);
/* 176 */           BusTableRel rel = relation.find(tableKey);
/* 177 */           for (Object obj : enty.getValue()) {
/* 178 */             businessData.addChildren(analysisFormDefData(obj, rel));
/*     */           }
/*     */         } 
/*     */         
/* 182 */         if (enty.getValue() instanceof JSONObject) {
/* 183 */           BusTableRel rel = relation.find(enty.getKey());
/* 184 */           businessData.addChildren(analysisFormDefData(enty.getValue(), rel));
/*     */         } 
/*     */       } 
/* 187 */       businessData.setData(bdData);
/*     */     } 
/* 189 */     return businessData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeData(IBusinessObject businessObject, Object id) {
/* 194 */     BusinessDataPersistenceServiceFactory.removeData((BusinessObject)businessObject, id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveData(IBusinessData businessData) {
/* 199 */     BusinessDataPersistenceServiceFactory.saveData((BusinessData)businessData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveData(IBusinessData businessData, JSONObject instData) {
/* 204 */     BusinessDataPersistenceServiceFactory.saveData((BusinessData)businessData, instData);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBusinessData loadData(IBusinessObject businessObject, Object id) {
/* 209 */     return loadData(businessObject, id, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBusinessData loadData(IBusinessObject businessObject, Object id, boolean init) {
/* 214 */     BusinessData businessData = BusinessDataPersistenceServiceFactory.loadData((BusinessObject)businessObject, id);
/* 215 */     if (id == null && init) {
/* 216 */       initFormDefData(businessData);
/*     */     }
/* 218 */     return (IBusinessData)businessData;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBusinessData parseBusinessData(JSONObject jsonObject, String boKey) {
/* 223 */     BusinessObject businessObject = this.businessObjectManager.getFilledByKey(boKey);
/* 224 */     return (IBusinessData)analysisFormDefData(jsonObject, businessObject.getRelation());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessDataService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */