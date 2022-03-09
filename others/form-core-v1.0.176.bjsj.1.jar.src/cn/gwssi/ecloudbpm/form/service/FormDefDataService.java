/*     */ package cn.gwssi.ecloudbpm.form.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessPermissionObjType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessPermissionService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessTableService;
/*     */ import cn.gwssi.ecloudbpm.form.api.service.IFormDefDataService;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormDefHistoryManager;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormDefManager;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormDef;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormDefData;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormDefHistory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class FormDefDataService
/*     */   implements IFormDefDataService
/*     */ {
/*     */   @Autowired
/*     */   FormDefManager formDefManager;
/*     */   @Autowired
/*     */   IBusinessObjectService businessObjectService;
/*     */   @Autowired
/*     */   IBusinessTableService businessTableService;
/*     */   @Autowired
/*     */   IBusinessPermissionService businessPermissionService;
/*     */   @Autowired
/*     */   IBusinessDataService businessDataService;
/*     */   @Autowired
/*     */   FormDefHistoryManager formDefHistoryManager;
/*     */   
/*     */   public FormDefData getByFormDefKey(String formDefKey, String id) {
/*  57 */     FormDefData formDefData = new FormDefData();
/*  58 */     FormDef formDef = this.formDefManager.getByKey(formDefKey);
/*  59 */     formDefData.setHtml(formDef.getHtml());
/*  60 */     IBusinessPermission businessPermission = this.businessPermissionService.getByObjTypeAndObjVal(null, BusinessPermissionObjType.FORM
/*  61 */         .getKey(), formDef.getKey(), formDef.getBoKey(), true);
/*     */     
/*  63 */     formDefData.setPermission(businessPermission.getPermission(false));
/*  64 */     formDefData.setTablePermission(businessPermission.getTablePermission(false));
/*     */     
/*  66 */     handleInitData(formDef, formDefData);
/*  67 */     handleData(formDef, id, formDefData, businessPermission);
/*     */     
/*  69 */     return formDefData;
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
/*     */   public FormDefData getByFormDefKey(String formDefKey) {
/*  81 */     FormDefData formDefData = new FormDefData();
/*  82 */     FormDef formDef = this.formDefManager.getByKey(formDefKey);
/*  83 */     formDefData.setHtml(formDef.getHtml());
/*  84 */     IBusinessPermission businessPermission = this.businessPermissionService.getByObjTypeAndObjVal(null, BusinessPermissionObjType.FORM
/*  85 */         .getKey(), formDef.getKey(), formDef.getBoKey(), true);
/*     */     
/*  87 */     formDefData.setPermission(businessPermission.getPermission(false));
/*  88 */     formDefData.setTablePermission(businessPermission.getTablePermission(false));
/*  89 */     handleInitData(formDef, formDefData);
/*  90 */     handleData(formDef, formDefData, businessPermission);
/*  91 */     return formDefData;
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
/*     */   private void handleInitData(FormDef formDef, FormDefData formDefData) {
/* 104 */     if (formDefData.getInitData() == null) {
/* 105 */       formDefData.setInitData(new JSONObject());
/*     */     }
/* 107 */     JSONObject initData = formDefData.getInitData();
/* 108 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(formDef.getBoKey());
/* 109 */     initData.put(formDef.getBoKey(), new JSONObject());
/* 110 */     for (IBusTableRel rel : businessObject.getRelation().list()) {
/* 111 */       initData.getJSONObject(formDef.getBoKey()).put(rel.getTableKey(), getInitData(rel));
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
/*     */   private JSONObject getInitData(IBusTableRel busTableRel) {
/* 125 */     JSONObject table = new JSONObject();
/* 126 */     table.putAll(busTableRel.getTable().initData());
/* 127 */     for (IBusTableRel rel : busTableRel.getChildren()) {
/* 128 */       if (BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType()))
/*     */       {
/* 130 */         table.put(rel.getTableKey(), getInitData(rel));
/*     */       }
/*     */     } 
/* 133 */     return table;
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
/*     */   private void handleData(FormDef formDef, String id, FormDefData formDefData, IBusinessPermission businessPermission) {
/* 147 */     if (formDefData.getData() == null) {
/* 148 */       formDefData.setData(new JSONObject());
/*     */     }
/*     */     
/* 151 */     JSONObject data = formDefData.getData();
/* 152 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(formDef.getBoKey());
/* 153 */     businessObject.setPermission((IBusObjPermission)businessPermission.getBusObjMap().get(formDef.getBoKey()));
/* 154 */     data.put(formDef.getBoKey(), this.businessDataService.getFormDefData(businessObject, id));
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
/*     */   private void handleData(FormDef formDef, FormDefData formDefData, IBusinessPermission businessPermission) {
/* 167 */     if (formDefData.getData() == null) {
/* 168 */       formDefData.setData(new JSONObject());
/*     */     }
/* 170 */     JSONObject data = formDefData.getData();
/* 171 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(formDef.getBoKey());
/* 172 */     businessObject.setPermission((IBusObjPermission)businessPermission.getBusObjMap().get(formDef.getBoKey()));
/* 173 */     data.put(formDef.getBoKey(), this.businessDataService.getFormDefData(businessObject));
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
/*     */   public FormDefData getByFormDefHistoryId(String formDefKey, String id, String formDefHistoryId) {
/* 187 */     FormDefData formDefData = new FormDefData();
/* 188 */     FormDef formDef = this.formDefManager.getByKey(formDefKey);
/* 189 */     FormDefHistory formDefHistory = (FormDefHistory)this.formDefHistoryManager.get(formDefHistoryId);
/* 190 */     formDef.setHtml(formDefHistory.getHtml());
/* 191 */     formDefData.setHtml(formDef.getHtml());
/* 192 */     IBusinessPermission businessPermission = this.businessPermissionService.getByObjTypeAndObjVal(null, BusinessPermissionObjType.FORM
/* 193 */         .getKey(), formDef.getKey(), formDef.getBoKey(), true);
/*     */     
/* 195 */     formDefData.setPermission(businessPermission.getPermission(false));
/* 196 */     formDefData.setTablePermission(businessPermission.getTablePermission(false));
/*     */     
/* 198 */     handleInitData(formDef, formDefData);
/* 199 */     handleData(formDef, id, formDefData, businessPermission);
/*     */     
/* 201 */     return formDefData;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/service/FormDefDataService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */