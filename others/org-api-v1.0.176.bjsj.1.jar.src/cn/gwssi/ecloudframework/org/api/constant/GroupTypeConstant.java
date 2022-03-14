/*    */ package com.dstz.org.api.constant;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GroupTypeConstant
/*    */ {
/* 15 */   ORG("org", "组织", new String[] { "bpm", "org" }),
/* 16 */   POST("post", "岗位", new String[] { "bpm" }),
/* 17 */   ROLE("role", "角色", new String[] { "bpm" });
/*    */   private String key;
/*    */   private String label;
/*    */   private String[] tags;
/*    */   
/*    */   GroupTypeConstant(String key, String label) {
/* 23 */     this.key = key;
/* 24 */     this.label = label;
/*    */   }
/*    */   
/*    */   GroupTypeConstant(String key, String label, String[] tags) {
/* 28 */     this.key = key;
/* 29 */     this.label = label;
/* 30 */     this.tags = tags;
/*    */   }
/*    */   
/*    */   public String key() {
/* 34 */     return this.key;
/*    */   }
/*    */   
/*    */   public String label() {
/* 38 */     return this.label;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static GroupTypeConstant fromStr(String key) {
/* 44 */     for (GroupTypeConstant e : values()) {
/* 45 */       if (key.equals(e.key)) {
/* 46 */         return e;
/*    */       }
/*    */     } 
/*    */     
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   public static GroupTypeConstant[] getValuesByTags(String tag) {
/* 54 */     List<GroupTypeConstant> groupTypeConstants = new ArrayList<>();
/* 55 */     for (GroupTypeConstant e : values()) {
/* 56 */       if (e.tags != null) {
/* 57 */         for (String enumTag : e.tags) {
/* 58 */           if (enumTag.equals(tag)) {
/* 59 */             groupTypeConstants.add(e);
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/* 65 */     return groupTypeConstants.<GroupTypeConstant>toArray(new GroupTypeConstant[0]);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/constant/GroupTypeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */