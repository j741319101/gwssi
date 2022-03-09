/*    */ package com.dstz.bpm.api.model.def;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BpmDataModel
/*    */   implements Serializable
/*    */ {
/* 13 */   private String name = "";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   private String code = "";
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 25 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 29 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setCode(String code) {
/* 33 */     this.code = code;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/BpmDataModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */