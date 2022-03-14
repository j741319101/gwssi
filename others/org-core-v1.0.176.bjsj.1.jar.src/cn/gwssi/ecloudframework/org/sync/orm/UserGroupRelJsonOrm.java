/*    */ package com.dstz.org.sync.orm;
/*    */ 
/*    */ import com.alibaba.fastjson.annotation.JSONField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserGroupRelJsonOrm
/*    */ {
/*    */   protected String id;
/*    */   protected String groupId;
/*    */   protected String userId;
/* 14 */   protected String type = "groupUser";
/*    */ 
/*    */   
/*    */   public String getGroupId() {
/* 18 */     return this.groupId;
/*    */   }
/*    */   
/*    */   @JSONField(name = "orgId")
/*    */   public void setGroupId(String groupId) {
/* 23 */     this.groupId = groupId;
/*    */   }
/*    */   
/*    */   public String getUserId() {
/* 27 */     return this.userId;
/*    */   }
/*    */   
/*    */   @JSONField(name = "userId")
/*    */   public void setUserId(String userId) {
/* 32 */     this.userId = userId;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 36 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 40 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 44 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 48 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/sync/orm/UserGroupRelJsonOrm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */