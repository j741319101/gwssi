/*    */ package com.dstz.org.api.model.dto;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class QueryConfDTO
/*    */   implements Serializable
/*    */ {
/*    */   String name;
/*    */   String type;
/*    */   String value;
/*    */   
/*    */   public QueryConfDTO(String name, String type, String value) {
/* 37 */     this.name = name;
/* 38 */     this.type = type;
/* 39 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 47 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 51 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 55 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 59 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 63 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/QueryConfDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */