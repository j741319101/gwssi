/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SysTree
/*     */   extends BaseModel
/*     */ {
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   private String description;
/*     */   private boolean system;
/*     */   private boolean multiSelect;
/*     */   private boolean leafStore;
/*     */   private boolean iconShow;
/*     */   private boolean enableEdit;
/*     */   private boolean drag;
/*     */   private List<SysTreeNode> nodes;
/*     */   private String typeId;
/*     */   private String typeName;
/*     */   
/*     */   public String getKey() {
/*  83 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  87 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  95 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  99 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 103 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public boolean isSystem() {
/* 107 */     return this.system;
/*     */   }
/*     */   
/*     */   public void setSystem(boolean system) {
/* 111 */     this.system = system;
/*     */   }
/*     */   
/*     */   public List<SysTreeNode> getNodes() {
/* 115 */     return this.nodes;
/*     */   }
/*     */   
/*     */   public void setNodes(List<SysTreeNode> nodes) {
/* 119 */     this.nodes = nodes;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 123 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 127 */     this.description = description;
/*     */   }
/*     */   
/*     */   public boolean isMultiSelect() {
/* 131 */     return this.multiSelect;
/*     */   }
/*     */   
/*     */   public void setMultiSelect(boolean multiSelect) {
/* 135 */     this.multiSelect = multiSelect;
/*     */   }
/*     */   
/*     */   public boolean isLeafStore() {
/* 139 */     return this.leafStore;
/*     */   }
/*     */   
/*     */   public void setLeafStore(boolean leafStore) {
/* 143 */     this.leafStore = leafStore;
/*     */   }
/*     */   
/*     */   public boolean isIconShow() {
/* 147 */     return this.iconShow;
/*     */   }
/*     */   
/*     */   public void setIconShow(boolean iconShow) {
/* 151 */     this.iconShow = iconShow;
/*     */   }
/*     */   
/*     */   public boolean isEnableEdit() {
/* 155 */     return this.enableEdit;
/*     */   }
/*     */   
/*     */   public void setEnableEdit(boolean enableEdit) {
/* 159 */     this.enableEdit = enableEdit;
/*     */   }
/*     */   
/*     */   public boolean isDrag() {
/* 163 */     return this.drag;
/*     */   }
/*     */   
/*     */   public void setDrag(boolean drag) {
/* 167 */     this.drag = drag;
/*     */   }
/*     */   
/*     */   public String getTypeId() {
/* 171 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 175 */     this.typeId = typeId;
/*     */   }
/*     */   
/*     */   public String getTypeName() {
/* 179 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public void setTypeName(String typeName) {
/* 183 */     this.typeName = typeName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */