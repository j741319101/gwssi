/*    */ package com.dstz.bpm.rest.vo;
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
/*    */ public class DocumentEnclosureDto
/*    */   implements Serializable
/*    */ {
/*    */   private String enclosureId;
/*    */   private String enclosureName;
/*    */   private Integer enclosureSize;
/*    */   private String path;
/*    */   private String e_flag;
/*    */   
/*    */   public String getEnclosureId() {
/* 34 */     return this.enclosureId;
/*    */   }
/*    */   
/*    */   public void setEnclosureId(String enclosureId) {
/* 38 */     this.enclosureId = enclosureId;
/*    */   }
/*    */   
/*    */   public String getEnclosureName() {
/* 42 */     return this.enclosureName;
/*    */   }
/*    */   
/*    */   public void setEnclosureName(String enclosureName) {
/* 46 */     this.enclosureName = enclosureName;
/*    */   }
/*    */   
/*    */   public Integer getEnclosureSize() {
/* 50 */     return this.enclosureSize;
/*    */   }
/*    */   
/*    */   public void setEnclosureSize(Integer enclosureSize) {
/* 54 */     this.enclosureSize = enclosureSize;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 58 */     return this.path;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 62 */     this.path = path;
/*    */   }
/*    */   
/*    */   public String getE_flag() {
/* 66 */     return this.e_flag;
/*    */   }
/*    */   
/*    */   public void setE_flag(String e_flag) {
/* 70 */     this.e_flag = e_flag;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/DocumentEnclosureDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */