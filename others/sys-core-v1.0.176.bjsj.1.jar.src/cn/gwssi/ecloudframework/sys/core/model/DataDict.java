/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.sys.api.model.IDataDict;
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
/*     */ public class DataDict
/*     */   extends BaseModel
/*     */   implements IDataDict
/*     */ {
/*     */   public static final String TYPE_DICT = "dict";
/*     */   public static final String TYPE_NODE = "node";
/*     */   protected String parentId;
/*     */   protected String key;
/*     */   protected String name;
/*     */   protected String dictKey;
/*     */   protected String typeId;
/*  39 */   protected Integer sn = Integer.valueOf(0);
/*     */ 
/*     */ 
/*     */   
/*     */   protected String dictType;
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected String deleteFlag = "0";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentId(String parentId) {
/*  54 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/*  62 */     return this.parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKey(String key) {
/*  69 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  77 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  84 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  92 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDictKey(String dictKey) {
/*  99 */     this.dictKey = dictKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDictKey() {
/* 107 */     return this.dictKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 114 */     this.typeId = typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeId() {
/* 122 */     return this.typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSn(Integer sn) {
/* 129 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 137 */     return this.sn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDictType(String dictType) {
/* 144 */     this.dictType = dictType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDictType() {
/* 152 */     return this.dictType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeleteFlag(String deleteFlag) {
/* 159 */     this.deleteFlag = deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeleteFlag() {
/* 167 */     return this.deleteFlag;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/DataDict.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */