/*     */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusColumnCtrlType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelFkType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessObjectPersistenceType;
/*     */ import cn.gwssi.ecloudbpm.bus.dao.BusinessObjectDao;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusDataFlowRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRelFk;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusinessPermissionService;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class BusinessObjectManagerImpl
/*     */   extends BaseManager<String, BusinessObject> implements BusinessObjectManager {
/*     */   @Resource
/*     */   BusinessObjectDao businessObjectDao;
/*     */   @Autowired
/*     */   BusinessTableManager businessTableManager;
/*     */   @Autowired
/*     */   BusinessPermissionService businessPermissionService;
/*     */   @Resource
/*     */   JdbcTemplate jdbcTemplate;
/*     */   
/*     */   public BusinessObject getByKey(String key) {
/*  47 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  48 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/*  49 */     return (BusinessObject)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessObject getFilledByKey(String key) {
/*  54 */     BusinessObject businessObject = getByKey(key);
/*  55 */     fill(businessObject);
/*  56 */     return businessObject;
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
/*     */   private void fill(BusinessObject businessObject) {
/*  70 */     if (businessObject == null) {
/*     */       return;
/*     */     }
/*     */     
/*  74 */     for (BusTableRel rel : businessObject.getRelation().list()) {
/*  75 */       rel.setTable(this.businessTableManager.getFilledByKey(rel.getTableKey()));
/*  76 */       rel.setBusObj(businessObject);
/*     */     } 
/*     */     
/*  79 */     handleSetParentRel(businessObject.getRelation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleSetParentRel(BusTableRel rel) {
/*  90 */     for (BusTableRel r : rel.getChildren()) {
/*  91 */       r.setParent(rel);
/*  92 */       handleSetParentRel(r);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JSONObject> boTreeData(String key) {
/*  98 */     BusinessObject businessObject = getByKey(key);
/*  99 */     BusTableRel busTableRel = businessObject.getRelation();
/* 100 */     List<JSONObject> list = new ArrayList<>();
/* 101 */     hanldeBusTableRel(busTableRel, "0", list);
/*     */     
/* 103 */     if (CollectionUtil.isNotEmpty(list)) {
/* 104 */       ((JSONObject)list.get(0)).put("alias", key);
/*     */     }
/* 106 */     return list;
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
/*     */   private void hanldeBusTableRel(BusTableRel busTableRel, String parentId, List<JSONObject> list) {
/* 119 */     BusinessTable businessTable = this.businessTableManager.getFilledByKey(busTableRel.getTableKey());
/* 120 */     JSONObject root = new JSONObject();
/* 121 */     root.put("id", businessTable.getId());
/* 122 */     root.put("key", businessTable.getKey());
/* 123 */     root.put("name", businessTable.getComment() + "(" + BusTableRelType.getByKey(busTableRel.getType()).getDesc() + "#" + businessTable.getName() + ")");
/* 124 */     root.put("comment", businessTable.getComment());
/* 125 */     root.put("parentId", parentId);
/* 126 */     root.put("nodeType", "table");
/* 127 */     root.put("relationType", busTableRel.getType());
/* 128 */     list.add(root);
/*     */ 
/*     */     
/* 131 */     for (BusinessColumn businessColumn : businessTable.getColumns()) {
/* 132 */       JSONObject columnJson = new JSONObject();
/* 133 */       columnJson.put("id", businessColumn.getId());
/* 134 */       columnJson.put("key", businessColumn.getKey());
/* 135 */       columnJson.put("name", businessColumn.getComment());
/* 136 */       columnJson.put("tableKey", businessTable.getKey());
/* 137 */       columnJson.put("parentId", businessTable.getId());
/*     */       
/* 139 */       if (businessColumn.isPrimary() || businessColumn.getCtrl() == null || BusColumnCtrlType.HIDDEN
/* 140 */         .getKey().equals(businessColumn.getCtrl().getType())) {
/* 141 */         columnJson.put("isHidden", Boolean.valueOf(true));
/*     */       }
/*     */       
/* 144 */       columnJson.put("nodeType", "column");
/* 145 */       list.add(columnJson);
/*     */     } 
/*     */     
/* 148 */     if (null != busTableRel.getDataFlowLinks()) {
/* 149 */       for (BusDataFlowRel busDataFlowRel : busTableRel.getDataFlowLinks()) {
/* 150 */         JSONObject formula = new JSONObject();
/* 151 */         formula.put("sourceTableKey", businessTable.getKey());
/* 152 */         formula.put("targetTableKey", busDataFlowRel.getTableKey());
/* 153 */         formula.put("formulas", busDataFlowRel.getFormulas());
/*     */         
/* 155 */         formula.put("nodeType", "dataFlow");
/* 156 */         list.add(formula);
/*     */       } 
/*     */     }
/*     */     
/* 160 */     for (BusTableRel rel : busTableRel.getChildren()) {
/* 161 */       hanldeBusTableRel(rel, businessTable.getId(), list);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String entityId) {
/* 172 */     BusinessObject businessObject = (BusinessObject)get(entityId);
/* 173 */     if (businessObject == null) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     List<String> names = this.jdbcTemplate.queryForList(" select name_ from form_def where bo_key_ = '" + businessObject.getKey() + "'", String.class);
/* 178 */     if (CollectionUtil.isNotEmpty(names)) {
/* 179 */       throw new BusinessMessage("表单:" + names.toString() + "还在使用业务对象， 删除业务对象失败！");
/*     */     }
/*     */     
/* 182 */     super.remove(entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateOverallArrangementByCode(String boCode, String overallArrangement) {
/* 187 */     JSONObject json = JSON.parseObject(overallArrangement);
/* 188 */     if (json.getJSONArray("groupList").isEmpty()) {
/* 189 */       overallArrangement = null;
/*     */     }
/*     */     
/* 192 */     this.businessObjectDao.updateOverallArrangementByCode(boCode, overallArrangement);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOverallArrangementByCode(String boCode) {
/* 197 */     return this.businessObjectDao.getOverallArrangementByCode(boCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusinessObject> listJsonByKey(String tableKey, String tableGroupKey) {
/* 202 */     if (StringUtil.isEmpty(tableKey)) {
/* 203 */       tableKey = null;
/*     */     }
/* 205 */     if (StringUtil.isEmpty(tableGroupKey)) {
/* 206 */       tableGroupKey = null;
/*     */     }
/* 208 */     return this.businessObjectDao.listJsonByKey(tableKey, tableGroupKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDiagramJson(String id, String diagramJson) {
/* 213 */     this.businessObjectDao.updateDiagramJson(id, diagramJson);
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer countByTypeId(String typeId) {
/* 218 */     return this.businessObjectDao.countByTypeId(typeId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSave(BusinessObject businessObject) {
/* 224 */     if (!BusinessObjectPersistenceType.DB.equalsWithKey(businessObject.getPersistenceType())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 230 */     BusinessObject bo = getFilledByKey(businessObject.getKey());
/* 231 */     bo.getRelation().list().forEach(rel -> {
/*     */           boolean b = this.businessTableManager.newTableOperator(rel.getTable()).isTableCreated();
/*     */ 
/*     */           
/*     */           if (!b) {
/*     */             throw new BusinessMessage("数据库中不存在实体【" + rel.getTable().getComment() + "】所对应的表[" + rel.getTable().getName() + "]！<br/> 请编辑实体点击生成表，或者修改业务对象的持久化方式！");
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 241 */     if (businessObject.isSaveIndex()) {
/* 242 */       handleIndex(bo.getRelation());
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
/*     */   private void handleIndex(BusTableRel rel) {
/* 254 */     if (rel == null) {
/*     */       return;
/*     */     }
/*     */     
/* 258 */     List<String> ownIndex = new ArrayList<>();
/* 259 */     List<String> parentIndex = new ArrayList<>();
/* 260 */     if (rel.getFks() != null) {
/* 261 */       rel.getFks().forEach(fk -> {
/*     */             if (BusTableRelFkType.CHILD_FIELD.equalsWithKey(fk.getType())) {
/*     */               parentIndex.add(fk.getFrom());
/*     */             } else {
/*     */               ownIndex.add(fk.getFrom());
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/* 270 */     this.businessTableManager.newTableOperator(rel.getTable()).saveIndex(ownIndex);
/* 271 */     if (rel.getParent() != null) {
/* 272 */       this.businessTableManager.newTableOperator(rel.getParent().getTable()).saveIndex(parentIndex);
/*     */     }
/*     */     
/* 275 */     rel.getChildren().forEach(this::handleIndex);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusinessObjectManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */