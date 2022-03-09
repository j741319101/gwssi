/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.model.permission.BusObjPermission;
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusinessPermission
/*     */   extends BaseModel
/*     */   implements IBusinessPermission
/*     */ {
/*     */   private String objType;
/*     */   private String objVal;
/*     */   private String busObjMapJson;
/*     */   private String boKey;
/*     */   private String defId;
/*     */   
/*     */   public String getDefId() {
/*  59 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/*  63 */     this.defId = defId;
/*     */   }
/*     */   
/*  66 */   private Map<String, BusObjPermission> busObjMap = new HashMap<>();
/*     */   
/*     */   private JSONObject tablePermission;
/*     */   
/*     */   public String getObjType() {
/*  71 */     return this.objType;
/*     */   }
/*     */   
/*     */   public void setObjType(String objType) {
/*  75 */     this.objType = objType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getObjVal() {
/*  80 */     return this.objVal;
/*     */   }
/*     */   
/*     */   public void setObjVal(String objVal) {
/*  84 */     this.objVal = objVal;
/*     */   }
/*     */   
/*     */   public String getBusObjMapJson() {
/*  88 */     return this.busObjMapJson;
/*     */   }
/*     */   
/*     */   public void setBusObjMapJson(String busObjMapJson) {
/*  92 */     this.busObjMapJson = busObjMapJson;
/*  93 */     if (StringUtil.isEmpty(busObjMapJson)) {
/*  94 */       this.busObjMap = null;
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     this.busObjMap = new HashMap<>();
/*  99 */     Map<String, Object> map = (Map<String, Object>)JSONObject.parseObject(busObjMapJson, Map.class);
/* 100 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 101 */       this.busObjMap.put(entry.getKey(), JSONObject.parseObject(entry.getValue().toString(), BusObjPermission.class));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, BusObjPermission> getBusObjMap() {
/* 107 */     return this.busObjMap;
/*     */   }
/*     */   
/*     */   public void setBusObjMap(Map<String, BusObjPermission> busObjMap) {
/* 111 */     this.busObjMap = busObjMap;
/* 112 */     this.busObjMapJson = JsonUtil.toJSONString(busObjMap);
/*     */   }
/*     */   
/*     */   public String getBoKey() {
/* 116 */     return this.boKey;
/*     */   }
/*     */   
/*     */   public void setBoKey(String boKey) {
/* 120 */     this.boKey = boKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusObjPermission getBusObj(String boKey) {
/* 125 */     return this.busObjMap.get(boKey);
/*     */   }
/*     */   
/*     */   public JSONObject getTablePermission(boolean isReadonly) {
/* 129 */     handlePermission(Boolean.valueOf(isReadonly));
/* 130 */     return this.tablePermission;
/*     */   }
/*     */   
/*     */   public JSONObject getPermission(boolean isReadonly) {
/* 134 */     handlePermission(Boolean.valueOf(isReadonly));
/* 135 */     return this.permission;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private JSONObject permission = null;
/*     */   
/*     */   private synchronized void handlePermission(Boolean isReadonly) {
/* 145 */     if (this.permission != null)
/*     */       return; 
/* 147 */     this.tablePermission = new JSONObject();
/* 148 */     this.permission = new JSONObject();
/*     */     
/* 150 */     for (Map.Entry<String, ? extends IBusObjPermission> entry : getBusObjMap().entrySet()) {
/* 151 */       IBusObjPermission busObjPermission = entry.getValue();
/* 152 */       busObjPermission.handlePermission(this.tablePermission, this.permission, isReadonly);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cleanBusObjMap() {
/* 157 */     this.busObjMap = null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusinessPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */