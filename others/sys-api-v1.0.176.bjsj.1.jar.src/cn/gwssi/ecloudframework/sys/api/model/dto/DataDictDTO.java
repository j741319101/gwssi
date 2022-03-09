/*     */ package cn.gwssi.ecloudframework.sys.api.model.dto;
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
/*     */ public class DataDictDTO
/*     */ {
/*     */   protected String parentId;
/*     */   protected String key;
/*     */   protected String name;
/*     */   protected String dictKey;
/*     */   protected String typeId;
/*  32 */   protected Integer sn = Integer.valueOf(0);
/*     */ 
/*     */ 
/*     */   
/*     */   protected String dictType;
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected String deleteFlag = "0";
/*     */   
/*     */   public void setParentId(String parentId) {
/*  43 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/*  51 */     return this.parentId;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  55 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  63 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  67 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setDictKey(String dictKey) {
/*  79 */     this.dictKey = dictKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDictKey() {
/*  87 */     return this.dictKey;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/*  91 */     this.typeId = typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeId() {
/*  99 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setSn(Integer sn) {
/* 103 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/* 111 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setDictType(String dictType) {
/* 115 */     this.dictType = dictType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDictType() {
/* 123 */     return this.dictType;
/*     */   }
/*     */   
/*     */   public void setDeleteFlag(String deleteFlag) {
/* 127 */     this.deleteFlag = deleteFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeleteFlag() {
/* 135 */     return this.deleteFlag;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/dto/DataDictDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */