/*    */ package com.dstz.sys.api.permission;
/*    */ 
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class PermissionCalculatorFactory
/*    */ {
/*    */   private static Map<String, IPermissionCalculator> permissionMap;
/*    */   
/*    */   private static Map<String, IPermissionCalculator> permissionMap() {
/* 33 */     if (permissionMap == null) {
/* 34 */       permissionMap = new HashMap<>();
/* 35 */       for (Map.Entry<String, IPermissionCalculator> entry : (Iterable<Map.Entry<String, IPermissionCalculator>>)AppUtil.getImplInstance(IPermissionCalculator.class).entrySet()) {
/* 36 */         permissionMap.put(((IPermissionCalculator)entry.getValue()).getType(), entry.getValue());
/*    */       }
/*    */     } 
/* 39 */     return permissionMap;
/*    */   }
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
/*    */   public static boolean haveRights(JSONArray jsonArray) {
/* 52 */     if (jsonArray == null) {
/* 53 */       return false;
/*    */     }
/* 55 */     for (Object obj : jsonArray) {
/* 56 */       JSONObject json = (JSONObject)obj;
/* 57 */       IPermissionCalculator permission = permissionMap().get(json.getString("type"));
/* 58 */       if (permission == null) {
/* 59 */         throw new BusinessException("权限类型[" + json.getString("type") + "]找不到处理器");
/*    */       }
/* 61 */       if (permission.haveRights(json)) {
/* 62 */         return true;
/*    */       }
/*    */     } 
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/permission/PermissionCalculatorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */