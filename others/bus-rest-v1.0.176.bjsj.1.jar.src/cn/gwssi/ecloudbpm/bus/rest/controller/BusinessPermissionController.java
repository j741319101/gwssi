/*    */ package cn.gwssi.ecloudbpm.bus.rest.controller;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessPermissionManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
/*    */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*    */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*    */ import cn.gwssi.ecloudframework.base.rest.BaseController;
/*    */ import cn.gwssi.ecloudframework.base.rest.util.RequestUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
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
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/bus/businessPermission/"})
/*    */ public class BusinessPermissionController
/*    */   extends BaseController<BusinessPermission>
/*    */ {
/*    */   @Resource
/*    */   BusinessObjectManager businessObjectManager;
/*    */   @Autowired
/*    */   BusinessPermissionManager businessPermissionManager;
/*    */   
/*    */   @RequestMapping({"getObject"})
/*    */   @CatchErr(write2response = true, value = "获取businessPermission异常")
/*    */   public ResultMsg<BusinessPermission> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 53 */     String objType = RequestUtil.getString(request, "objType");
/* 54 */     String objVal = RequestUtil.getString(request, "objVal");
/* 55 */     String defId = RequestUtil.getString(request, "defId");
/* 56 */     BusinessPermission businessPermission = this.businessPermissionManager.getByObjTypeAndObjVal(defId, objType, objVal);
/* 57 */     return getSuccessResult(businessPermission);
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
/*    */   
/*    */   @RequestMapping({"getBo"})
/*    */   @CatchErr(write2response = true, value = "获取boo异常")
/*    */   public ResultMsg<Map<String, BusinessObject>> getBo(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 73 */     String[] boKeys = RequestUtil.getStringAryByStr(request, "boKeys");
/*    */     
/* 75 */     Map<String, BusinessObject> boMap = new HashMap<>();
/* 76 */     for (String boKey : boKeys) {
/* 77 */       BusinessObject bo = this.businessObjectManager.getFilledByKey(boKey);
/* 78 */       boMap.put(boKey, bo);
/*    */     } 
/* 80 */     return getSuccessResult(boMap);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getModelDesc() {
/* 85 */     return "业务对象权限";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/rest/controller/BusinessPermissionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */