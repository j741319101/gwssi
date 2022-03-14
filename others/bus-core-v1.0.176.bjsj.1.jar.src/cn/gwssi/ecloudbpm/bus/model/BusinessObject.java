/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.RightsType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusColumnPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusTablePermission;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.base.core.util.JsonUtil;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusinessObject
/*     */   extends BaseModel
/*     */   implements IBusinessObject
/*     */ {
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   @NotEmpty
/*     */   private String relationJson;
/*     */   private String groupId;
/*     */   private String groupName;
/*     */   @NotEmpty
/*     */   private String persistenceType;
/*     */   private String perTypeConfig;
/*     */   private String diagramJson;
/*     */   private BusTableRel relation;
/*     */   private BusObjPermission permission;
/*     */   private boolean saveIndex;
/*     */   protected String orgId;
/*     */   
/*     */   public String getKey() {
/*  87 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  91 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  96 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 100 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 105 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 109 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public String getRelationJson() {
/* 113 */     return this.relationJson;
/*     */   }
/*     */   
/*     */   public void setRelationJson(String relationJson) {
/* 117 */     this.relationJson = relationJson;
/* 118 */     this.relation = (BusTableRel)JsonUtil.parseObject(relationJson, BusTableRel.class);
/*     */   }
/*     */   
/*     */   public String getDiagramJson() {
/* 122 */     return this.diagramJson;
/*     */   }
/*     */   
/*     */   public void setDiagramJson(String diagramJson) {
/* 126 */     this.diagramJson = diagramJson;
/*     */   }
/*     */   
/*     */   public String getGroupId() {
/* 130 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/* 134 */     this.groupId = groupId;
/*     */   }
/*     */   
/*     */   public String getGroupName() {
/* 138 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 142 */     this.groupName = groupName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPersistenceType() {
/* 147 */     return this.persistenceType;
/*     */   }
/*     */   
/*     */   public void setPersistenceType(String persistenceType) {
/* 151 */     this.persistenceType = persistenceType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPerTypeConfig() {
/* 156 */     return this.perTypeConfig;
/*     */   }
/*     */   
/*     */   public void setPerTypeConfig(String perTypeConfig) {
/* 160 */     this.perTypeConfig = perTypeConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusTableRel getRelation() {
/* 165 */     return this.relation;
/*     */   }
/*     */   
/*     */   public void setRelation(BusTableRel relation) {
/* 169 */     this.relation = relation;
/* 170 */     this.relationJson = JsonUtil.toJSONString(relation);
/*     */   }
/*     */ 
/*     */   
/*     */   public BusObjPermission getPermission() {
/* 175 */     return this.permission;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPermission(IBusObjPermission permission) {
/* 180 */     this.permission = (BusObjPermission)permission;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean haveTableDbEditRights(String tableKey) {
/* 185 */     return haveTableDbRights(true, tableKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean haveTableDbReadRights(String tableKey) {
/* 190 */     return haveTableDbRights(false, tableKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean haveColumnDbEditRights(String tableKey, String columnKey) {
/* 195 */     return haveColumnDbRights(true, tableKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean haveColumnDbReadRights(String tableKey, String columnKey) {
/* 200 */     return haveColumnDbRights(false, tableKey, columnKey);
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
/*     */   
/*     */   private boolean haveColumnDbRights(boolean isEdit, String tableKey, String columnKey) {
/* 215 */     RightsType rightsType = null;
/* 216 */     if (this.permission != null) {
/* 217 */       BusTablePermission tablePermission = (BusTablePermission)this.permission.getTableMap().get(tableKey);
/* 218 */       if (tablePermission != null) {
/* 219 */         BusColumnPermission columnPermission = (BusColumnPermission)tablePermission.getColumnMap().get(columnKey);
/* 220 */         if (columnPermission != null) {
/* 221 */           rightsType = RightsType.getByKey(columnPermission.getResult());
/*     */         }
/*     */       } 
/*     */     } 
/* 225 */     if (rightsType == null) {
/* 226 */       rightsType = RightsType.getDefalut();
/*     */     }
/* 228 */     if (isEdit) {
/* 229 */       return rightsType.isDbEditable();
/*     */     }
/* 231 */     return rightsType.isDbReadable();
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
/*     */   private boolean haveTableDbRights(boolean isEdit, String tableKey) {
/* 245 */     if (this.permission != null) {
/* 246 */       BusTablePermission tablePermission = (BusTablePermission)this.permission.getTableMap().get(tableKey);
/* 247 */       if (tablePermission != null) {
/* 248 */         for (BusinessColumn column : this.relation.find(tableKey).getTable().getColumnsWithoutPk()) {
/* 249 */           if (isEdit && haveColumnDbEditRights(tableKey, column.getKey())) {
/* 250 */             return true;
/*     */           }
/* 252 */           if (!isEdit && haveColumnDbReadRights(tableKey, column.getKey())) {
/* 253 */             return true;
/*     */           }
/*     */         } 
/*     */         
/* 257 */         if (isEdit) {
/* 258 */           return !RightsType.READ.getKey().equals(tablePermission.getResult());
/*     */         }
/* 260 */         return false;
/*     */       } 
/*     */     } 
/* 263 */     if (isEdit) {
/* 264 */       return RightsType.getDefalut().isDbEditable();
/*     */     }
/* 266 */     return RightsType.getDefalut().isDbReadable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> calDataSourceKeys() {
/* 272 */     Set<String> keys = new HashSet<>();
/* 273 */     for (BusTableRel rel : this.relation.list()) {
/* 274 */       keys.add(rel.getTable().getDsKey());
/*     */     }
/* 276 */     return keys;
/*     */   }
/*     */   
/*     */   public void setPermission(BusObjPermission permission) {
/* 280 */     this.permission = permission;
/*     */   }
/*     */   
/*     */   public boolean isSaveIndex() {
/* 284 */     return this.saveIndex;
/*     */   }
/*     */   
/*     */   public void setSaveIndex(boolean saveIndex) {
/* 288 */     this.saveIndex = saveIndex;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 292 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 296 */     this.orgId = orgId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusinessObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */