/*    */ package cn.gwssi.ecloudframework.sys.permission.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.api.permission.IPermissionCalculator;
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
/*    */ @Service
/*    */ public class NonePermissionCalculator
/*    */   implements IPermissionCalculator
/*    */ {
/*    */   public String getTitle() {
/* 21 */     return "无";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 26 */     return "none";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean haveRights(JSONObject json) {
/* 31 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/NonePermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */