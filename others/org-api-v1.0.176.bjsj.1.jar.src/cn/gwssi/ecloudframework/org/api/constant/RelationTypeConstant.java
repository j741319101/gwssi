/*    */ package cn.gwssi.ecloudframework.org.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RelationTypeConstant
/*    */ {
/* 13 */   GROUP_USER("groupUser", "机构用户"),
/* 14 */   USER_ROLE("userRole", "角色用户"),
/* 15 */   POST_USER("postUser", "岗位用户"),
/* 16 */   GROUP_MANAGER("groupManager", "机构管理员");
/*    */   
/*    */   private String key;
/*    */   
/*    */   private String label;
/*    */   
/*    */   RelationTypeConstant(String key, String label) {
/* 23 */     setKey(key);
/* 24 */     this.label = label;
/*    */   }
/*    */ 
/*    */   
/*    */   public String label() {
/* 29 */     return this.label;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 33 */     return this.label;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLabel(String label) {
/* 38 */     this.label = label;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 43 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setKey(String key) {
/* 48 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RelationTypeConstant getUserRelationTypeByGroupType(String groupType) {
/* 59 */     GroupTypeConstant type = GroupTypeConstant.fromStr(groupType);
/* 60 */     if (null != type) {
/* 61 */       switch (type) {
/*    */         case ORG:
/* 63 */           return GROUP_USER;
/*    */         case POST:
/* 65 */           return POST_USER;
/*    */         case ROLE:
/* 67 */           return USER_ROLE;
/*    */       } 
/*    */ 
/*    */     
/*    */     }
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/constant/RelationTypeConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */