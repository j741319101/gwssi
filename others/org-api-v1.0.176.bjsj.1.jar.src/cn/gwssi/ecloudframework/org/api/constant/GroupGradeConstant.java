/*    */ package com.dstz.org.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GroupGradeConstant
/*    */ {
/* 10 */   GROUP(0, "集团"),
/* 11 */   COMPANY(1, "公司"),
/* 12 */   DEPARTMENT(3, "部门");
/*    */   
/*    */   private int key;
/*    */   
/*    */   private String label;
/*    */   
/*    */   GroupGradeConstant(int key, String label) {
/* 19 */     this.key = key;
/* 20 */     this.label = label;
/*    */   }
/*    */   
/*    */   public int key() {
/* 24 */     return this.key;
/*    */   }
/*    */   
/*    */   public String label() {
/* 28 */     return this.label;
/*    */   }
/*    */   
/*    */   public static GroupGradeConstant getByLabel(String label) {
/* 32 */     for (GroupGradeConstant entity : values()) {
/* 33 */       if (entity.label.equals(label)) {
/* 34 */         return entity;
/*    */       }
/*    */     } 
/* 37 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/constant/GroupGradeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */