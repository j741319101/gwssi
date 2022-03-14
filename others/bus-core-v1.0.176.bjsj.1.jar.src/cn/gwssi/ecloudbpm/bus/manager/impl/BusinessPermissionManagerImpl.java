/*     */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.RightsType;
/*     */ import cn.gwssi.ecloudbpm.bus.dao.BusinessPermissionDao;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessPermissionManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.AbstractPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusColumnPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusTablePermission;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("busObjPermissionManager")
/*     */ public class BusinessPermissionManagerImpl
/*     */   extends BaseManager<String, BusinessPermission>
/*     */   implements BusinessPermissionManager
/*     */ {
/*     */   @Resource
/*     */   BusinessPermissionDao busObjPermissionDao;
/*     */   @Autowired
/*     */   BusinessObjectManager businessObjectManager;
/*     */   
/*     */   public BusinessPermission getByObjTypeAndObjVal(String defId, String objType, String objVal) {
/*  43 */     return this.busObjPermissionDao.getByObjTypeAndObjVal(defId, objType, objVal);
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessPermission getByObjTypeAndObjVal(String defId, String objType, String objVal, String defaultBoKeys) {
/*  48 */     BusinessPermission oldPermission = getByObjTypeAndObjVal(defId, objType, objVal);
/*  49 */     if (oldPermission == null) {
/*  50 */       if (!objVal.endsWith("-global")) {
/*  51 */         oldPermission = getByObjTypeAndObjVal(defId, objType, objVal.split("-")[0] + "-global");
/*     */       }
/*  53 */       if (oldPermission == null) {
/*  54 */         oldPermission = new BusinessPermission();
/*     */       }
/*     */     } 
/*     */     
/*  58 */     BusinessPermission businessPermission = new BusinessPermission();
/*  59 */     businessPermission.setObjType(objType);
/*  60 */     businessPermission.setObjVal(objVal);
/*  61 */     for (String boKey : defaultBoKeys.split(",")) {
/*     */       
/*  63 */       BusinessObject bo = this.businessObjectManager.getFilledByKey(boKey);
/*  64 */       if (bo == null) {
/*  65 */         throw new BusinessException(boKey + " 业务对象丢失！");
/*     */       }
/*     */       
/*  68 */       BusObjPermission busObjPermission = oldPermission.getBusObj(boKey);
/*  69 */       if (busObjPermission == null) {
/*  70 */         busObjPermission = new BusObjPermission();
/*  71 */         busObjPermission.setKey(boKey);
/*  72 */         busObjPermission.setName(bo.getName());
/*  73 */         hanldeDefaultRightsField((AbstractPermission)busObjPermission);
/*     */       } 
/*  75 */       businessPermission.getBusObjMap().put(boKey, busObjPermission);
/*     */ 
/*     */       
/*  78 */       for (BusTableRel rel : bo.getRelation().list()) {
/*  79 */         BusTablePermission busTablePermission = (BusTablePermission)busObjPermission.getTableMap().get(rel.getTableKey());
/*  80 */         if (busTablePermission == null) {
/*  81 */           busTablePermission = new BusTablePermission();
/*  82 */           busTablePermission.setKey(rel.getTableKey());
/*  83 */           busTablePermission.setComment(rel.getTableComment());
/*     */         } 
/*  85 */         busObjPermission.getTableMap().put(rel.getTableKey(), busTablePermission);
/*     */ 
/*     */         
/*  88 */         for (BusinessColumn column : rel.getTable().getColumnsWithoutPk()) {
/*  89 */           BusColumnPermission busColumnPermission = (BusColumnPermission)busTablePermission.getColumnMap().get(column.getKey());
/*  90 */           if (busColumnPermission == null) {
/*  91 */             busColumnPermission = new BusColumnPermission();
/*  92 */             busColumnPermission.setKey(column.getKey());
/*  93 */             busColumnPermission.setComment(column.getComment());
/*     */           } 
/*  95 */           busTablePermission.getColumnMap().put(column.getKey(), busColumnPermission);
/*     */         } 
/*     */ 
/*     */         
/*  99 */         Iterator<Map.Entry<String, BusColumnPermission>> iterator = busTablePermission.getColumnMap().entrySet().iterator();
/* 100 */         while (iterator.hasNext()) {
/* 101 */           Map.Entry<String, BusColumnPermission> entry = iterator.next();
/* 102 */           if (rel.getTable().getColumnByKey(entry.getKey()) == null) {
/* 103 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 109 */       Iterator<Map.Entry<String, BusTablePermission>> it = busObjPermission.getTableMap().entrySet().iterator();
/* 110 */       while (it.hasNext()) {
/* 111 */         Map.Entry<String, BusTablePermission> entry = it.next();
/* 112 */         if (bo.getRelation().find(entry.getKey()) == null) {
/* 113 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     return businessPermission;
/*     */   }
/*     */ 
/*     */   
/*     */   public int removeByBpmDefKey(String defId, String objType, String boKey) {
/* 123 */     return this.busObjPermissionDao.removeByBpmDefKey(defId, objType, boKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public int removeNotInBpmNode(String defId, String obKey, Set<String> nodeIds) {
/* 128 */     return this.busObjPermissionDao.removeNotInBpmNode(defId, obKey, nodeIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public int removeByDefId(String defId) {
/* 133 */     return this.busObjPermissionDao.removeByDefId(defId);
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
/*     */   private void hanldeDefaultRightsField(AbstractPermission permission) {
/* 145 */     JSONArray jsonArray = new JSONArray();
/* 146 */     JSONObject json = new JSONObject();
/* 147 */     json.put("type", "everyone");
/* 148 */     json.put("desc", "所有人");
/* 149 */     jsonArray.add(json);
/* 150 */     permission.getRights().put(RightsType.getDefalut().getKey(), jsonArray);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusinessPermissionManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */