/*     */ package com.dstz.base.db.model.query;
/*     */ 
/*     */ import com.dstz.base.api.Page;
/*     */ import com.dstz.base.api.query.Direction;
/*     */ import com.dstz.base.api.query.FieldLogic;
/*     */ import com.dstz.base.api.query.FieldSort;
/*     */ import com.dstz.base.api.query.QueryField;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.query.WhereClause;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultQueryFilter
/*     */   implements QueryFilter
/*     */ {
/*  26 */   private Page page = new DefaultPage();
/*     */ 
/*     */ 
/*     */   
/*  30 */   private List<FieldSort> fieldSortList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*  34 */   private Map<String, Object> params = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  38 */   private FieldLogic fieldLogic = new DefaultFieldLogic();
/*     */ 
/*     */   
/*     */   public Page getPage() {
/*  42 */     return this.page;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPage(Page page) {
/*  47 */     this.page = page;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getParams() {
/*  52 */     initParams(this.fieldLogic);
/*  53 */     return this.params;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultQueryFilter() {}
/*     */   
/*     */   public DefaultQueryFilter(boolean noPage) {
/*  60 */     if (noPage) {
/*  61 */       setPage(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public DefaultQueryFilter(FieldLogic fieldLogic) {
/*  66 */     this.fieldLogic = fieldLogic;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldLogic getFieldLogic() {
/*  71 */     return this.fieldLogic;
/*     */   }
/*     */   
/*     */   public void setFieldLogic(FieldLogic fieldLogic) {
/*  75 */     this.fieldLogic = fieldLogic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initParams(FieldLogic fedLog) {
/*  86 */     List<WhereClause> wcs = fedLog.getWhereClauses();
/*  87 */     for (WhereClause wc : wcs) {
/*  88 */       if (wc instanceof QueryField) {
/*  89 */         QueryField qf = (QueryField)wc;
/*  90 */         if (qf.getCompare() == QueryOP.IS_NULL || qf.getCompare() == QueryOP.NOTNULL) {
/*     */           continue;
/*     */         }
/*     */         
/*  94 */         String fn = qf.getField();
/*     */         
/*  96 */         if (fn.contains("."))
/*     */         {
/*  98 */           if (fn.contains("~")) {
/*  99 */             String[] fields = fn.split("~");
/* 100 */             StringBuilder fieldParam = new StringBuilder();
/* 101 */             for (String field : fields) {
/* 102 */               if (field.contains(".")) {
/* 103 */                 fieldParam.append(field.replace(".", "")).append("~");
/*     */               } else {
/* 105 */                 fieldParam.append(field).append("~");
/*     */               } 
/*     */             } 
/* 108 */             fn = fieldParam.substring(0, fieldParam.length() - 1);
/*     */           } else {
/* 110 */             fn = fn.replace(".", "");
/*     */           } 
/*     */         }
/*     */         
/* 114 */         this.params.put(fn + "_" + qf.getValueHashCode(), qf.getValue()); continue;
/* 115 */       }  if (wc instanceof FieldLogic) {
/* 116 */         FieldLogic fl = (FieldLogic)wc;
/* 117 */         initParams(fl);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FieldSort> getFieldSortList() {
/* 124 */     return this.fieldSortList;
/*     */   }
/*     */   
/*     */   public void setFieldSortList(List<FieldSort> fieldSortList) {
/* 128 */     this.fieldSortList = fieldSortList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFieldSort(String orderField, String orderSeq) {
/* 139 */     this.fieldSortList.add(new DefaultFieldSort(orderField, Direction.fromString(orderSeq)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFilter(String name, Object obj, QueryOP queryType) {
/* 144 */     this.fieldLogic.getWhereClauses().add(new DefaultQueryField(name, queryType, obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParamsFilter(String key, Object obj) {
/* 149 */     this.params.put(key, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(Map<String, Object> params) {
/* 154 */     this.params.putAll(params);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWhereSql() {
/* 159 */     String dynamicWhereSql = getFieldLogic().getSql();
/*     */     
/* 161 */     String defaultWhere = this.params.containsKey("defaultWhere") ? this.params.get("defaultWhere").toString() : "";
/* 162 */     if (StringUtils.isNotEmpty(defaultWhere)) {
/* 163 */       dynamicWhereSql = StringUtils.isNotEmpty(dynamicWhereSql) ? (dynamicWhereSql + " and " + defaultWhere) : defaultWhere;
/*     */     }
/* 165 */     return dynamicWhereSql;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrderBySql() {
/* 170 */     if (getFieldSortList().size() > 0) {
/* 171 */       StringBuilder sb = new StringBuilder();
/* 172 */       for (FieldSort fieldSort : getFieldSortList()) {
/* 173 */         sb.append(fieldSort.getField()).append(" ").append(fieldSort.getDirection()).append(",");
/*     */       }
/* 175 */       sb.deleteCharAt(sb.length() - 1);
/* 176 */       return sb.toString();
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/query/DefaultQueryFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */