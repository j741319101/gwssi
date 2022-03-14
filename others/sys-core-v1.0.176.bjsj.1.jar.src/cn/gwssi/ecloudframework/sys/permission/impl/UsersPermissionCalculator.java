/*    */ package com.dstz.sys.permission.impl;
/*    */ 
/*    */ import com.dstz.sys.api.permission.IPermissionCalculator;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.stereotype.Service;
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
/*    */ @Service
/*    */ public class UsersPermissionCalculator
/*    */   implements IPermissionCalculator
/*    */ {
/*    */   public String getTitle() {
/* 23 */     return "用户";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 28 */     return "user";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean haveRights(JSONObject json) {
/* 33 */     for (String id : json.getString("id").split(",")) {
/* 34 */       if (id.equals(ContextUtil.getCurrentUserId())) {
/* 35 */         return true;
/*    */       }
/*    */     } 
/* 38 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/UsersPermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */