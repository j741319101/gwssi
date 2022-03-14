/*    */ package com.dstz.sys.permission.impl;
/*    */ 
/*    */ import com.dstz.sys.api.permission.IPermissionCalculator;
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
/*    */ 
/*    */ @Service
/*    */ public class EveryonePermissionCalculator
/*    */   implements IPermissionCalculator
/*    */ {
/*    */   public String getType() {
/* 23 */     return "everyone";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 28 */     return "所有人";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean haveRights(JSONObject json) {
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/EveryonePermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */