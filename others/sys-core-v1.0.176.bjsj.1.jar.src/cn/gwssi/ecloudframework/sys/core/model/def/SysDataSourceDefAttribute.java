/*    */ package com.dstz.sys.core.model.def;
/*    */ 
/*    */ import com.dstz.sys.api.model.ISysDataSourceDefAttribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SysDataSourceDefAttribute
/*    */   implements ISysDataSourceDefAttribute
/*    */ {
/*    */   private String name;
/*    */   private String comment;
/*    */   private String type;
/*    */   private boolean required;
/*    */   private String defaultValue;
/*    */   private String value;
/*    */   
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 51 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getComment() {
/* 55 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(String comment) {
/* 59 */     this.comment = comment;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 63 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 67 */     this.type = type;
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 71 */     return this.required;
/*    */   }
/*    */   
/*    */   public void setRequired(boolean required) {
/* 75 */     this.required = required;
/*    */   }
/*    */   
/*    */   public String getDefaultValue() {
/* 79 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public void setDefaultValue(String defaultValue) {
/* 83 */     this.defaultValue = defaultValue;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 87 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 91 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/def/SysDataSourceDefAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */