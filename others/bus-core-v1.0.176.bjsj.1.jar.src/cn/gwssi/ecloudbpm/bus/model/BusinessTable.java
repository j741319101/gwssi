/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusColumnCtrlType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessTable;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IBaseModel;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusinessTable
/*     */   extends Table<BusinessColumn>
/*     */   implements IBaseModel, IBusinessTable
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   @NotEmpty
/*     */   private String id;
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String dsKey;
/*     */   @NotEmpty
/*     */   private String dsName;
/*     */   private String groupId;
/*     */   private String groupName;
/*     */   private boolean external;
/*     */   private boolean createdTable;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   protected String fieldNames;
/*     */   protected String fieldTypes;
/*     */   protected String dbpartition;
/*     */   protected String tbpartition;
/*  79 */   protected String dbpartitionType = "hash";
/*     */   
/*  81 */   protected String tbpartitionType = "hash";
/*     */ 
/*     */   
/*     */   protected String tbpartitions;
/*     */ 
/*     */   
/*     */   protected String orgId;
/*     */ 
/*     */   
/*     */   public String getFieldNames() {
/*  91 */     return this.fieldNames;
/*     */   }
/*     */   
/*     */   public void setFieldNames(String fieldNames) {
/*  95 */     this.fieldNames = fieldNames;
/*     */   }
/*     */   
/*     */   public String getFieldTypes() {
/*  99 */     return this.fieldTypes;
/*     */   }
/*     */   
/*     */   public void setFieldTypes(String fieldTypes) {
/* 103 */     this.fieldTypes = fieldTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 108 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 113 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 118 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 122 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDsKey() {
/* 127 */     return this.dsKey;
/*     */   }
/*     */   
/*     */   public void setDsKey(String dsKey) {
/* 131 */     this.dsKey = dsKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDsName() {
/* 136 */     return this.dsName;
/*     */   }
/*     */   
/*     */   public void setDsName(String dsName) {
/* 140 */     this.dsName = dsName;
/*     */   }
/*     */   
/*     */   public String getGroupId() {
/* 144 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/* 148 */     this.groupId = groupId;
/*     */   }
/*     */   
/*     */   public String getGroupName() {
/* 152 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 156 */     this.groupName = groupName;
/*     */   }
/*     */   
/*     */   public boolean isExternal() {
/* 160 */     return this.external;
/*     */   }
/*     */   
/*     */   public void setExternal(boolean external) {
/* 164 */     this.external = external;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPkName() {
/* 175 */     if (getPkColumn() == null) {
/* 176 */       return "";
/*     */     }
/* 178 */     return ((BusinessColumn)getPkColumn()).getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPkKey() {
/* 189 */     if (getPkColumn() == null) {
/* 190 */       return "";
/*     */     }
/* 192 */     return ((BusinessColumn)getPkColumn()).getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumns(List<BusinessColumn> columns) {
/* 203 */     super.setColumns(columns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BusinessColumn> getColumns() {
/* 214 */     return super.getColumns();
/*     */   }
/*     */   
/*     */   public boolean isCreatedTable() {
/* 218 */     return this.createdTable;
/*     */   }
/*     */   
/*     */   public void setCreatedTable(boolean createdTable) {
/* 222 */     this.createdTable = createdTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusinessColumn> getColumnsWithoutPk() {
/* 227 */     if (this.columns == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     List<BusinessColumn> columnList = new ArrayList<>();
/* 231 */     for (BusinessColumn column : this.columns) {
/* 232 */       if (!column.isPrimary()) {
/* 233 */         columnList.add(column);
/*     */       }
/*     */     } 
/* 236 */     return columnList;
/*     */   }
/*     */   
/*     */   public List<BusinessColumn> getColumnsWithOutHidden() {
/* 240 */     if (this.columns == null) {
/* 241 */       return null;
/*     */     }
/* 243 */     List<BusinessColumn> columnList = new ArrayList<>();
/* 244 */     for (BusinessColumn column : this.columns) {
/* 245 */       if (column.isPrimary()) {
/*     */         continue;
/*     */       }
/*     */       
/* 249 */       if (column.getCtrl() == null || BusColumnCtrlType.HIDDEN.getKey().equals(column.getCtrl().getType())) {
/*     */         continue;
/*     */       }
/*     */       
/* 253 */       columnList.add(column);
/*     */     } 
/*     */     
/* 256 */     return columnList;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> initDbData() {
/* 261 */     Map<String, Object> map = new HashMap<>();
/* 262 */     for (IBusinessColumn column : getColumnsWithoutPk()) {
/* 263 */       map.put(column.getName(), column.initValue());
/*     */     }
/* 265 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> initData() {
/* 270 */     Map<String, Object> map = new HashMap<>();
/* 271 */     for (IBusinessColumn column : getColumnsWithoutPk()) {
/* 272 */       map.put(column.getKey(), column.initValue());
/*     */     }
/* 274 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessColumn getColumnByKey(String key) {
/* 279 */     for (BusinessColumn column : this.columns) {
/* 280 */       if (key.equals(column.getKey())) {
/* 281 */         return column;
/*     */       }
/*     */     } 
/* 284 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 289 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 294 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 299 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 304 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 309 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 314 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 319 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 324 */     this.updateBy = updateBy;
/*     */   }
/*     */   
/*     */   public String getDbpartition() {
/* 328 */     return this.dbpartition;
/*     */   }
/*     */   
/*     */   public void setDbpartition(String dbpartition) {
/* 332 */     this.dbpartition = dbpartition;
/*     */   }
/*     */   
/*     */   public String getTbpartition() {
/* 336 */     return this.tbpartition;
/*     */   }
/*     */   
/*     */   public void setTbpartition(String tbpartition) {
/* 340 */     this.tbpartition = tbpartition;
/*     */   }
/*     */   
/*     */   public String getDbpartitionType() {
/* 344 */     return this.dbpartitionType;
/*     */   }
/*     */   
/*     */   public void setDbpartitionType(String dbpartitionType) {
/* 348 */     this.dbpartitionType = dbpartitionType;
/*     */   }
/*     */   
/*     */   public String getTbpartitionType() {
/* 352 */     return this.tbpartitionType;
/*     */   }
/*     */   
/*     */   public void setTbpartitionType(String tbpartitionType) {
/* 356 */     this.tbpartitionType = tbpartitionType;
/*     */   }
/*     */   
/*     */   public String getTbpartitions() {
/* 360 */     return this.tbpartitions;
/*     */   }
/*     */   
/*     */   public void setTbpartitions(String tbpartitions) {
/* 364 */     this.tbpartitions = tbpartitions;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 368 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 372 */     this.orgId = orgId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusinessTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */