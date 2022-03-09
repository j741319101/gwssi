/*     */ package cn.gwssi.ecloudframework.sys.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ 
/*     */ public class SerialNo extends BaseModel {
/*     */   protected String name;
/*     */   protected String alias;
/*     */   protected String regulation;
/*     */   protected Short genType;
/*     */   protected Integer noLength;
/*     */   protected String curDate;
/*     */   protected Integer initValue;
/*  13 */   protected Integer curValue = Integer.valueOf(0);
/*     */ 
/*     */   
/*     */   protected Short step;
/*     */ 
/*     */   
/*  19 */   protected Integer newCurValue = Integer.valueOf(0);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   protected String curIdenValue = "";
/*     */ 
/*     */ 
/*     */   
/*     */   protected String typeId;
/*     */ 
/*     */   
/*     */   protected String typeName;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  36 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  45 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  49 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlias() {
/*  58 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setRegulation(String regulation) {
/*  62 */     this.regulation = regulation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRegulation() {
/*  71 */     return this.regulation;
/*     */   }
/*     */   
/*     */   public void setGenType(Short genType) {
/*  75 */     this.genType = genType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Short getGenType() {
/*  84 */     return this.genType;
/*     */   }
/*     */   
/*     */   public void setNoLength(Integer noLength) {
/*  88 */     this.noLength = noLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getNoLength() {
/*  97 */     return this.noLength;
/*     */   }
/*     */   
/*     */   public void setCurDate(String curDate) {
/* 101 */     this.curDate = curDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurDate() {
/* 110 */     return this.curDate;
/*     */   }
/*     */   
/*     */   public void setInitValue(Integer initValue) {
/* 114 */     this.initValue = initValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getInitValue() {
/* 123 */     return this.initValue;
/*     */   }
/*     */   
/*     */   public void setCurValue(Integer curValue) {
/* 127 */     this.curValue = curValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCurValue() {
/* 136 */     if (this.curValue == null) {
/* 137 */       return Integer.valueOf(0);
/*     */     }
/* 139 */     return this.curValue;
/*     */   }
/*     */   
/*     */   public void setStep(Short step) {
/* 143 */     this.step = step;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Short getStep() {
/* 152 */     return this.step;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getNewCurValue() {
/* 157 */     return this.newCurValue;
/*     */   }
/*     */   
/*     */   public void setNewCurValue(Integer newCurValue) {
/* 161 */     this.newCurValue = newCurValue;
/*     */   }
/*     */   
/*     */   public String getCurIdenValue() {
/* 165 */     return this.curIdenValue;
/*     */   }
/*     */   
/*     */   public void setCurIdenValue(String curIdenValue) {
/* 169 */     this.curIdenValue = curIdenValue;
/*     */   }
/*     */   
/*     */   public String getTypeId() {
/* 173 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 177 */     this.typeId = typeId;
/*     */   }
/*     */   
/*     */   public String getTypeName() {
/* 181 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public void setTypeName(String typeName) {
/* 185 */     this.typeName = typeName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SerialNo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */