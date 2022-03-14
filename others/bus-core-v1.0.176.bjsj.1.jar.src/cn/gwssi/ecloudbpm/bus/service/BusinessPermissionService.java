/*     */ package cn.gwssi.ecloudbpm.bus.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.RightsType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessPermissionService;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessPermissionManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.AbstractPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusColumnPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusTablePermission;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import com.dstz.sys.api.permission.PermissionCalculatorFactory;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class BusinessPermissionService
/*     */   implements IBusinessPermissionService
/*     */ {
/*     */   @Autowired
/*     */   BusinessPermissionManager businessPermissionManager;
/*     */   @Autowired
/*     */   BusinessObjectManager businessObjectManager;
/*     */   
/*     */   public BusinessPermission getByObjTypeAndObjVal(String defId, String objType, String objVal, String defalutBoKeys, boolean calculate) {
/*  34 */     BusinessPermission businessPermission = null;
/*  35 */     if (StringUtil.isNotEmpty(defalutBoKeys)) {
/*  36 */       businessPermission = this.businessPermissionManager.getByObjTypeAndObjVal(defId, objType, objVal, defalutBoKeys);
/*     */     } else {
/*  38 */       businessPermission = this.businessPermissionManager.getByObjTypeAndObjVal(defId, objType, objVal);
/*     */     } 
/*  40 */     if (businessPermission == null) {
/*  41 */       return new BusinessPermission();
/*     */     }
/*     */     
/*  44 */     if (calculate) {
/*  45 */       calculateResult(businessPermission);
/*     */     }
/*  47 */     return businessPermission;
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateResult(BusinessPermission businessPermission) {
/*  52 */     for (Map.Entry<String, BusObjPermission> entry : (Iterable<Map.Entry<String, BusObjPermission>>)businessPermission.getBusObjMap().entrySet()) {
/*  53 */       BusObjPermission busObjPermission = entry.getValue();
/*  54 */       calculateResult((AbstractPermission)busObjPermission);
/*  55 */       for (Map.Entry<String, BusTablePermission> etry : (Iterable<Map.Entry<String, BusTablePermission>>)busObjPermission.getTableMap().entrySet()) {
/*  56 */         BusTablePermission busTablePermission = etry.getValue();
/*     */         
/*  58 */         if (CollectionUtil.isEmpty(busTablePermission.getRights())) {
/*  59 */           busTablePermission.setResult(busObjPermission.getResult());
/*     */         } else {
/*  61 */           calculateResult((AbstractPermission)busTablePermission);
/*     */         } 
/*     */         
/*  64 */         for (Map.Entry<String, BusColumnPermission> ery : (Iterable<Map.Entry<String, BusColumnPermission>>)busTablePermission.getColumnMap().entrySet()) {
/*  65 */           BusColumnPermission busColumnPermission = ery.getValue();
/*     */           
/*  67 */           if (MapUtil.isEmpty(busColumnPermission.getRights())) {
/*  68 */             busColumnPermission.setResult(busTablePermission.getResult()); continue;
/*     */           } 
/*  70 */           calculateResult((AbstractPermission)busColumnPermission);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateResult(AbstractPermission permission) {
/*  86 */     for (RightsType rightsType : RightsType.values()) {
/*  87 */       JSONArray jsonArray = (JSONArray)permission.getRights().get(rightsType.getKey());
/*  88 */       boolean b = PermissionCalculatorFactory.haveRights(jsonArray);
/*  89 */       if (b) {
/*  90 */         permission.setResult(rightsType.getKey());
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  95 */     if (StringUtil.isEmpty(permission.getResult())) {
/*  96 */       permission.setResult(RightsType.values()[(RightsType.values()).length - 1].getKey());
/*     */     }
/*     */ 
/*     */     
/* 100 */     ThreadMapUtil.remove("cn.gwssi.ecloudbpm.sys.permission.impl.GroupPermission");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/BusinessPermissionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */