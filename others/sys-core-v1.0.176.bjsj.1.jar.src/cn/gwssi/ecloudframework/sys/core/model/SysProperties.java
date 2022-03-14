/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.encrypt.EncryptUtil;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.sys.api.constant.EnvironmentConstant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class SysProperties
/*     */   extends BaseModel
/*     */ {
/*     */   protected String name;
/*     */   protected String alias;
/*     */   protected String group;
/*     */   protected String value;
/*  41 */   protected List<String> categorys = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected int encrypt = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected String description = "";
/*     */   
/*  54 */   protected String environment = EnvironmentConstant.DEV.key();
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/*  58 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  67 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  71 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  80 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  84 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlias() {
/*  93 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setGroup(String group) {
/*  97 */     this.group = group;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroup() {
/* 106 */     return this.group;
/*     */   }
/*     */   
/*     */   public void setValue(String val) throws Exception {
/* 110 */     this.value = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 119 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValByEncrypt() throws Exception {
/* 128 */     if (this.encrypt == 1) {
/* 129 */       this.value = EncryptUtil.encrypt(this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealVal() {
/* 139 */     if (this.encrypt == 1) {
/* 140 */       return EncryptUtil.decrypt(this.value);
/*     */     }
/* 142 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getCategorys() {
/* 147 */     return this.categorys;
/*     */   }
/*     */   
/*     */   public void setCategorys(List<String> categorys) {
/* 151 */     this.categorys = categorys;
/*     */   }
/*     */   
/*     */   public int getEncrypt() {
/* 155 */     return this.encrypt;
/*     */   }
/*     */   
/*     */   public void setEncrypt(int encrypt) {
/* 159 */     this.encrypt = encrypt;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 163 */     return this.description;
/*     */   }
/*     */   
/*     */   public String getEnvironment() {
/* 167 */     return this.environment;
/*     */   }
/*     */   
/*     */   public void setEnvironment(String environment) {
/* 171 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 175 */     this.description = description;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */