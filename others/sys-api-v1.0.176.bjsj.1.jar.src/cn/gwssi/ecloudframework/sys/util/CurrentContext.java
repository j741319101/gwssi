/*    */ package com.dstz.sys.util;
/*    */ 
/*    */ import com.dstz.base.api.context.ICurrentContext;
/*    */ import com.dstz.org.api.model.IGroup;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class CurrentContext
/*    */   implements ICurrentContext
/*    */ {
/*    */   public String getCurrentUserId() {
/* 18 */     return ContextUtil.getCurrentUserId();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCurrentUserName() {
/* 23 */     IUser user = ContextUtil.getCurrentUser();
/* 24 */     return (user != null) ? user.getFullname() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCurrentGroupId() {
/* 30 */     return ContextUtil.getCurrentGroupId();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCurrentGroupName() {
/* 36 */     IGroup group = ContextUtil.getCurrentGroup();
/* 37 */     return (group != null) ? group.getGroupName() : null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/CurrentContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */