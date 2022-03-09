/*     */ package cn.gwssi.ecloudbpm.bus.service.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusStatusCode;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelFkType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessObjectPersistenceType;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRelFk;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusinessDataPersistenceService;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RequestContext;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.tableoper.CommonColumn;
/*     */ import cn.gwssi.ecloudframework.base.db.tableoper.TableOperator;
/*     */ import cn.gwssi.ecloudframework.base.db.transaction.AbDataSourceTransactionManager;
/*     */ import cn.gwssi.ecloudframework.sys.api.service.ISysDataSourceService;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class BusinessDataPersistenceDbService
/*     */   implements BusinessDataPersistenceService
/*     */ {
/*     */   @Autowired
/*     */   BusinessTableManager businessTableManager;
/*     */   @Autowired
/*     */   BusinessObjectManager businessObjectManager;
/*     */   @Autowired
/*     */   ISysDataSourceService sysDataSourceService;
/*     */   @Autowired
/*     */   CommonColumn commonColumn;
/*     */   
/*     */   public String type() {
/*  55 */     return BusinessObjectPersistenceType.DB.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public void saveData(BusinessData businessData) {
/*  62 */     for (String key : businessData.getBusTableRel().getBusObj().calDataSourceKeys()) {
/*  63 */       DataSource dataSource = this.sysDataSourceService.getDataSourceByKey(key);
/*  64 */       AbDataSourceTransactionManager.addDataSource(key, dataSource);
/*     */     } 
/*     */ 
/*     */     
/*  68 */     TableOperator tableOperator = this.businessTableManager.newTableOperatorCheckExist(businessData.getBusTableRel().getTable());
/*     */     
/*  70 */     String busTableRelType = businessData.getBusTableRel().getType();
/*     */ 
/*     */     
/*  73 */     if (!businessData.getBusTableRel().getBusObj().haveTableDbEditRights(businessData.getBusTableRel().getTableKey())) {
/*     */       
/*  75 */       if (businessData.getBusTableRel().getBusObj().haveTableDbReadRights(businessData.getBusTableRel().getTableKey())) {
/*  76 */         saveChildren(businessData);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  82 */     if (BusTableRelType.MAIN.equalsWithKey(busTableRelType)) {
/*     */       
/*  84 */       Object id = businessData.getPk();
/*  85 */       if (id == null) {
/*  86 */         businessData.setPk(IdUtil.getSuid());
/*  87 */         tableOperator.insertData(businessData.getDbData());
/*     */       } else {
/*  89 */         tableOperator.updateData(businessData.getDbData());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (BusTableRelType.ONE_TO_ONE.equalsWithKey(busTableRelType) || BusTableRelType.ONE_TO_MANY.equalsWithKey(busTableRelType)) {
/*  95 */       Object id = businessData.getPk();
/*  96 */       boolean newData = false;
/*  97 */       if (id == null) {
/*  98 */         businessData.setPk(IdUtil.getSuid());
/*  99 */         newData = true;
/*     */       } 
/*     */ 
/*     */       
/* 103 */       BusinessData parBusinessData = businessData.getParent();
/*     */       
/* 105 */       for (BusTableRelFk fk : businessData.getBusTableRel().getFks()) {
/*     */         
/* 107 */         if (BusTableRelFkType.FIXED_VALUE.equalsWithKey(fk.getType())) {
/* 108 */           businessData.put(fk.getFrom(), fk.getValue()); continue;
/* 109 */         }  if (BusTableRelFkType.PARENT_FIELD.equalsWithKey(fk.getType())) {
/* 110 */           Object fkValue = parBusinessData.get(fk.getValue());
/* 111 */           if (fkValue == null || StringUtil.isEmpty(fkValue.toString())) {
/* 112 */             BusinessTable mainTable = parBusinessData.getBusTableRel().getTable();
/* 113 */             BusinessTable subTable = businessData.getBusTableRel().getTable();
/* 114 */             throw new BusinessException(String.format("在子表外键对应父字段时，父表【%s（%s）】字段【%s】为空，导致子表【%s（%s）】外键字段【%s】无法关联", new Object[] { mainTable.getComment(), mainTable.getKey(), fk.getValue(), subTable.getComment(), subTable.getKey(), fk.getFrom() }));
/*     */           } 
/* 116 */           businessData.put(fk.getFrom(), fkValue); continue;
/* 117 */         }  if (BusTableRelFkType.CHILD_FIELD.equalsWithKey(fk.getType())) {
/* 118 */           Object fkValue = businessData.get(fk.getFrom());
/* 119 */           if (fkValue == null || StringUtil.isEmpty(fkValue.toString())) {
/* 120 */             BusinessTable mainTable = parBusinessData.getBusTableRel().getTable();
/* 121 */             BusinessTable subTable = businessData.getBusTableRel().getTable();
/* 122 */             throw new BusinessException(String.format("在主表外键对应子表字段时，子表【%s（%s）】字段【%s】为空，导致父表【%s（%s）】外键字段【%s】无法关联", new Object[] { subTable.getComment(), subTable.getKey(), fk.getFrom(), mainTable.getComment(), mainTable.getKey(), fk.getValue() }));
/*     */           } 
/* 124 */           parBusinessData.put(fk.getValue(), fkValue);
/*     */           
/* 126 */           this.businessTableManager.newTableOperator(parBusinessData.getBusTableRel().getTable()).updateData(parBusinessData.getDbData());
/*     */         } 
/*     */       } 
/*     */       
/* 130 */       if (newData) {
/* 131 */         tableOperator.insertData(businessData.getDbData());
/*     */       } else {
/* 133 */         tableOperator.updateData(businessData.getDbData());
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     saveChildren(businessData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveData(BusinessData businessData, JSONObject instData) {
/* 142 */     saveData(businessData);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkColumnBWRPermission(BusinessData businessData) {
/* 147 */     BusObjPermission busObjPermission = businessData.getBusTableRel().getBusObj().getPermission();
/* 148 */     busObjPermission.getRights();
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
/*     */   private void saveChildren(BusinessData businessData) {
/* 160 */     for (BusTableRel rel : businessData.getBusTableRel().getChildren()) {
/* 161 */       if (!rel.getBusObj().haveTableDbEditRights(rel.getTableKey())) {
/*     */         continue;
/*     */       }
/* 164 */       TableOperator tableOperator = this.businessTableManager.newTableOperatorCheckExist(rel.getTable());
/*     */       
/* 166 */       if (BusTableRelType.ONE_TO_MANY.equalsWithKey(rel.getType()) || BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType())) {
/* 167 */         Map<String, Object> param = new HashMap<>();
/* 168 */         for (BusTableRelFk fk : rel.getFks()) {
/*     */           
/* 170 */           if (BusTableRelFkType.FIXED_VALUE.equalsWithKey(fk.getType())) {
/* 171 */             param.put(fk.getFrom(), fk.getValue()); continue;
/* 172 */           }  if (BusTableRelFkType.PARENT_FIELD.equalsWithKey(fk.getType())) {
/* 173 */             param.put(fk.getFrom(), businessData.get(fk.getValue())); continue;
/* 174 */           }  if (BusTableRelFkType.CHILD_FIELD.equalsWithKey(fk.getType()));
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 179 */         List<Map<String, Object>> oldDatas = new ArrayList<>();
/* 180 */         if (!param.isEmpty()) {
/* 181 */           oldDatas = tableOperator.selectData(toDbMap(rel.getTable(), param));
/*     */         }
/*     */ 
/*     */         
/* 185 */         List<BusinessData> children = businessData.getChildren().computeIfAbsent(rel.getTableKey(), k -> new ArrayList());
/*     */ 
/*     */ 
/*     */         
/* 189 */         label42: for (Map<String, Object> oldData : oldDatas) {
/* 190 */           Object id = oldData.get(rel.getTable().getPkName());
/* 191 */           for (BusinessData data : children) {
/* 192 */             if (id.equals(data.getPk())) {
/*     */               continue label42;
/*     */             }
/*     */           } 
/* 196 */           removeChildren(oldData, rel);
/*     */           
/* 198 */           tableOperator.deleteData(id);
/*     */         } 
/*     */         
/* 201 */         for (BusinessData data : children) {
/* 202 */           saveData(data);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BusinessData loadData(BusinessObject businessObject, Object id) {
/* 211 */     BusinessData businessData = new BusinessData();
/*     */ 
/*     */     
/* 214 */     BusTableRel busTableRel = businessObject.getRelation();
/* 215 */     businessData.setBusTableRel(busTableRel);
/* 216 */     BusinessTable businessTable = busTableRel.getTable();
/* 217 */     if (id == null) {
/* 218 */       return businessData;
/*     */     }
/* 220 */     if (!businessObject.haveTableDbReadRights(busTableRel.getTableKey())) {
/* 221 */       return businessData;
/*     */     }
/*     */     
/* 224 */     Map<String, Object> data = this.businessTableManager.newTableOperatorCheckExist(businessTable).selectData(getReadableColumnName(busTableRel), id);
/* 225 */     if (data == null) {
/* 226 */       throw new BusinessException(" 业务数据丢失 :" + id, BusStatusCode.BUS_DATA_LOSE);
/*     */     }
/* 228 */     businessData.setDbData(data);
/* 229 */     if (data != null) {
/* 230 */       loadChildren(businessData, busTableRel);
/*     */     }
/* 232 */     return businessData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleQueryFiler(Map<String, Object> param) {
/* 241 */     HttpServletRequest request = RequestContext.getHttpServletRequest();
/* 242 */     String queryFilter = request.getParameter("queryFilter");
/* 243 */     if (StringUtil.isNotEmpty(queryFilter)) {
/* 244 */       queryFilter = HtmlUtils.htmlEscape(queryFilter);
/* 245 */       String[] queryFilters = queryFilter.split(",");
/* 246 */       for (String filter : queryFilters) {
/* 247 */         if (StringUtil.isNotEmpty(filter) && (filter.split(":")).length > 1) {
/* 248 */           param.put(filter.split(":")[0], filter.split(":")[1]);
/*     */         }
/*     */       } 
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
/*     */   private void loadChildren(BusinessData businessData, BusTableRel busTableRel) {
/* 263 */     for (BusTableRel rel : busTableRel.getChildren()) {
/* 264 */       BusinessTable table = rel.getTable();
/*     */       
/* 266 */       if (!rel.getBusObj().haveTableDbReadRights(rel.getTableKey())) {
/*     */         return;
/*     */       }
/*     */       
/* 270 */       Map<String, Object> param = new HashMap<>();
/* 271 */       for (BusTableRelFk fk : rel.getFks()) {
/*     */         
/* 273 */         if (BusTableRelFkType.FIXED_VALUE.equalsWithKey(fk.getType())) {
/* 274 */           param.put(fk.getFrom(), fk.getValue()); continue;
/* 275 */         }  if (BusTableRelFkType.PARENT_FIELD.equalsWithKey(fk.getType())) {
/* 276 */           param.put(fk.getFrom(), businessData.get(fk.getValue())); continue;
/* 277 */         }  if (BusTableRelFkType.CHILD_FIELD.equalsWithKey(fk.getType())) {
/* 278 */           param.put(fk.getFrom(), businessData.get(fk.getValue()));
/*     */         }
/*     */       } 
/* 281 */       List<Map<String, Object>> dataMapList = this.businessTableManager.newTableOperatorCheckExist(table).selectData(getReadableColumnName(rel), toDbMap(table, param));
/* 282 */       for (Map<String, Object> dataMap : dataMapList) {
/* 283 */         BusinessData data = new BusinessData();
/* 284 */         data.setBusTableRel(rel);
/* 285 */         data.setParent(businessData);
/* 286 */         data.setDbData(dataMap);
/* 287 */         businessData.addChildren(data);
/*     */ 
/*     */         
/* 290 */         loadChildren(data, rel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeData(BusinessObject businessObject, Object id) {
/* 298 */     BusTableRel busTableRel = businessObject.getRelation();
/*     */     
/* 300 */     if (!busTableRel.getBusObj().haveTableDbEditRights(busTableRel.getTableKey())) {
/*     */       return;
/*     */     }
/*     */     
/* 304 */     Map<String, Object> data = this.businessTableManager.newTableOperatorCheckExist(busTableRel.getTable()).selectData(id);
/* 305 */     if (data == null) {
/*     */       return;
/*     */     }
/* 308 */     this.businessTableManager.newTableOperator(busTableRel.getTable()).deleteData(data.get(busTableRel.getTable().getPkName()));
/* 309 */     removeChildren(data, busTableRel);
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeChildren(Map<String, Object> dbData, BusTableRel busTableRel) {
/* 314 */     for (BusTableRel rel : busTableRel.getChildren()) {
/*     */       
/* 316 */       if (!rel.getBusObj().haveTableDbEditRights(rel.getTableKey())) {
/*     */         continue;
/*     */       }
/*     */       
/* 320 */       Map<String, Object> param = new HashMap<>();
/* 321 */       Map<String, Object> data = fromDbMap(busTableRel.getTable(), dbData);
/* 322 */       for (BusTableRelFk fk : rel.getFks()) {
/*     */         
/* 324 */         if (BusTableRelFkType.FIXED_VALUE.equalsWithKey(fk.getType())) {
/* 325 */           param.put(fk.getFrom(), fk.getValue()); continue;
/* 326 */         }  if (BusTableRelFkType.PARENT_FIELD.equalsWithKey(fk.getType())) {
/* 327 */           param.put(fk.getFrom(), data.get(fk.getValue())); continue;
/* 328 */         }  if (BusTableRelFkType.CHILD_FIELD.equalsWithKey(fk.getType()))
/*     */         {
/*     */ 
/*     */           
/* 332 */           param.put(fk.getFrom(), data.get(fk.getValue()));
/*     */         }
/*     */       } 
/* 335 */       if (rel.getChildren().isEmpty()) {
/* 336 */         this.businessTableManager.newTableOperatorCheckExist(rel.getTable()).deleteData(toDbMap(rel.getTable(), param)); continue;
/*     */       } 
/* 338 */       List<Map<String, Object>> dataMapList = this.businessTableManager.newTableOperator(rel.getTable()).selectData(toDbMap(rel.getTable(), param));
/* 339 */       for (Map<String, Object> dataMap : dataMapList) {
/* 340 */         removeChildren(dataMap, rel);
/*     */       }
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
/*     */   private List<String> getReadableColumnName(BusTableRel busTableRel) {
/* 355 */     List<String> columnName = new ArrayList<>();
/* 356 */     for (BusinessColumn column : busTableRel.getTable().getColumns()) {
/* 357 */       if (busTableRel.getBusObj().haveColumnDbReadRights(busTableRel.getTableKey(), column.getKey())) {
/* 358 */         columnName.add(column.getName());
/*     */       }
/*     */     } 
/* 361 */     return columnName;
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
/*     */   private Map<String, Object> toDbMap(BusinessTable table, Map<String, Object> map) {
/* 374 */     Map<String, Object> dbData = new HashMap<>();
/* 375 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 376 */       String columnName = table.getColumnByKey(entry.getKey()).getName();
/* 377 */       dbData.put(columnName, entry.getValue());
/*     */     } 
/* 379 */     return dbData;
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
/*     */   private Map<String, Object> fromDbMap(BusinessTable table, Map<String, Object> map) {
/* 392 */     Map<String, Object> data = new HashMap<>();
/* 393 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 394 */       BusinessColumn column = (BusinessColumn)table.getColumn(entry.getKey());
/* 395 */       if (null == column) {
/* 396 */         throw new BusinessException("实体[" + table.getName() + "]中不存在[" + (String)entry.getKey() + "]字段信息,请同步实体");
/*     */       }
/* 398 */       String columnKey = column.getKey();
/* 399 */       data.put(columnKey, entry.getValue());
/*     */     } 
/* 401 */     return data;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/impl/BusinessDataPersistenceDbService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */