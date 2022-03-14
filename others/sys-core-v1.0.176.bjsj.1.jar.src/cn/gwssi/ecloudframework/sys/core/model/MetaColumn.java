/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import java.io.Serializable;
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
/*     */ public class MetaColumn
/*     */   extends BaseModel
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String groupId;
/*     */   private String name;
/*     */   private String key;
/*     */   private String type;
/*     */   private Integer length;
/*     */   private Integer decimal;
/*     */   private Integer enabled;
/*     */   private String defaultValue;
/*     */   private String inputValid;
/*     */   private String comment;
/*     */   private String displayControl;
/*     */   private String prompt;
/*     */   private String groupName;
/*     */   
/*     */   public String getGroupId() {
/*  65 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/*  69 */     this.groupId = groupId;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  77 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  81 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  85 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  89 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  93 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Integer getLength() {
/*  97 */     return this.length;
/*     */   }
/*     */   
/*     */   public void setLength(Integer length) {
/* 101 */     this.length = length;
/*     */   }
/*     */   
/*     */   public Integer getDecimal() {
/* 105 */     return this.decimal;
/*     */   }
/*     */   
/*     */   public void setDecimal(Integer decimal) {
/* 109 */     this.decimal = decimal;
/*     */   }
/*     */   
/*     */   public Integer getEnabled() {
/* 113 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(Integer enabled) {
/* 117 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public String getDefaultValue() {
/* 121 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public void setDefaultValue(String defaultValue) {
/* 125 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public String getInputValid() {
/* 129 */     return this.inputValid;
/*     */   }
/*     */   
/*     */   public void setInputValid(String inputValid) {
/* 133 */     this.inputValid = inputValid;
/*     */   }
/*     */   
/*     */   public String getComment() {
/* 137 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(String comment) {
/* 141 */     this.comment = comment;
/*     */   }
/*     */   
/*     */   public String getDisplayControl() {
/* 145 */     return this.displayControl;
/*     */   }
/*     */   
/*     */   public void setDisplayControl(String displayControl) {
/* 149 */     this.displayControl = displayControl;
/*     */   }
/*     */   
/*     */   public String getPrompt() {
/* 153 */     return this.prompt;
/*     */   }
/*     */   
/*     */   public void setPrompt(String prompt) {
/* 157 */     this.prompt = prompt;
/*     */   }
/*     */   
/*     */   public String getGroupName() {
/* 161 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 165 */     this.groupName = groupName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/MetaColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */