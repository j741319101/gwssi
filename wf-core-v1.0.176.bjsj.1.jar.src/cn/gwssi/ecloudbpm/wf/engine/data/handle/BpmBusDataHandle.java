/*     */ package com.dstz.bpm.engine.data.handle;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessPermissionService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.api.service.BpmRightsFormService;
/*     */ import com.dstz.bpm.core.manager.BpmBusLinkManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmBusLink;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.dboper.DbOperator;
/*     */ import com.dstz.base.db.dboper.DbOperatorFactory;
/*     */ import com.dstz.sys.groovy.GroovyBinding;
/*     */ import cn.hutool.core.codec.Base64;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import groovy.lang.Binding;
/*     */ import groovy.lang.GroovyShell;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class BpmBusDataHandle
/*     */   implements IBpmBusDataHandle
/*     */ {
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private BpmBusLinkManager bpmBusLinkManager;
/*     */   @Resource
/*     */   private BpmRightsFormService bpmRightsAndFormService;
/*     */   @Resource
/*     */   private IBusinessDataService iBusinessDataService;
/*     */   @Autowired
/*     */   private IBusinessObjectService businessObjectService;
/*     */   @Autowired
/*     */   private IBusinessPermissionService businessPermissionService;
/*     */   @Value("${ecloud.bpmBusDataHandle.enableTablePartition:true}")
/*     */   private boolean enableTablePartition;
/*     */   
/*     */   public Map<String, IBusinessData> getInstanceData(IBusinessPermission businessPermission, BpmInstance instance) {
/*  89 */     Map<String, IBusinessData> dataMap = null;
/*     */     
/*  91 */     BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(instance);
/*     */ 
/*     */     
/*  94 */     if (topInstance != null) {
/*  95 */       dataMap = getInstanceBusData(topInstance.getId(), businessPermission);
/*     */     }
/*  97 */     if (dataMap == null) {
/*  98 */       dataMap = new HashMap<>();
/*     */     }
/*     */ 
/*     */     
/* 102 */     dataMap.putAll(getInstanceBusData(instance.getId(), businessPermission));
/*     */     
/* 104 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 105 */     List<BpmDataModel> listDataModel = processDef.getDataModelList();
/* 106 */     for (BpmDataModel model : listDataModel) {
/* 107 */       String code = model.getCode();
/* 108 */       if (dataMap.containsKey(code)) {
/*     */         continue;
/*     */       }
/* 111 */       IBusinessObject businessObject = this.businessObjectService.getFilledByKey(code);
/* 112 */       businessObject.setPermission(businessPermission.getBusObj(code));
/*     */       
/* 114 */       IBusinessData busData = this.iBusinessDataService.loadData(businessObject, null);
/* 115 */       dataMap.put(code, busData);
/*     */     } 
/*     */     
/* 118 */     return dataMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, IBusinessData> getInstanceBusData(String instanceId, IBusinessPermission businessPermission) {
/* 126 */     List<BpmBusLink> busLinks = this.bpmBusLinkManager.getByInstanceId(instanceId);
/*     */     
/* 128 */     Map<String, IBusinessData> dataMap = new HashMap<>();
/* 129 */     for (BpmBusLink busLink : busLinks) {
/* 130 */       IBusinessObject businessObject = this.businessObjectService.getFilledByKey(busLink.getBizCode());
/* 131 */       if (businessObject == null) {
/* 132 */         throw new BusinessException("业务对象" + busLink.getBizCode() + "丢失");
/*     */       }
/* 134 */       if (businessPermission != null) {
/* 135 */         businessObject.setPermission(businessPermission.getBusObj(busLink.getBizCode()));
/*     */       }
/* 137 */       IBusinessData busData = this.iBusinessDataService.loadData(businessObject, busLink.getBizId());
/* 138 */       if (busData == null) {
/* 139 */         throw new BusinessError(String.format("bizCode[%s] bizId[%s]", new Object[] { busLink.getBizCode(), busLink.getBizId() }), BpmStatusCode.FLOW_BUS_DATA_LOSE);
/*     */       }
/* 141 */       dataMap.put(busLink.getBizCode(), busData);
/*     */     } 
/*     */     
/* 144 */     return dataMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, IBusinessData> getInitData(IBusinessPermission businessPermision, String defId) {
/* 153 */     Map<String, IBusinessData> dataMap = new HashMap<>();
/*     */     
/* 155 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 156 */     List<BpmDataModel> listDataModel = processDef.getDataModelList();
/* 157 */     for (BpmDataModel model : listDataModel) {
/* 158 */       String code = model.getCode();
/*     */       
/* 160 */       IBusinessObject businessObject = this.businessObjectService.getFilledByKey(code);
/* 161 */       businessObject.setPermission(businessPermision.getBusObj(code));
/*     */       
/* 163 */       IBusinessData busData = this.iBusinessDataService.loadData(businessObject, null, true);
/* 164 */       dataMap.put(code, busData);
/*     */     } 
/*     */     
/* 167 */     return dataMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveDataModel(BaseActionCmd actionCmd) {
/* 177 */     Map<String, IBusinessData> boDataMap = actionCmd.getBizDataMap();
/* 178 */     if (MapUtil.isEmpty(boDataMap))
/*     */       return; 
/* 180 */     String submit = actionCmd.getActionName();
/* 181 */     BpmInstance instance = (BpmInstance)actionCmd.getBpmInstance();
/* 182 */     String nodeId = actionCmd.getNodeId();
/* 183 */     if (StringUtil.isEmpty(nodeId)) {
/* 184 */       BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(instance.getDefId());
/* 185 */       nodeId = startNode.getNodeId();
/*     */     } 
/* 187 */     IBusinessPermission businessPermision = this.bpmRightsAndFormService.getNodeSavePermission(instance.getDefId(), instance.getDefKey(), nodeId, boDataMap.keySet());
/*     */ 
/*     */     
/* 190 */     BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(instance);
/* 191 */     Set<String> topModelCodes = new HashSet<>();
/* 192 */     if (topInstance != null) {
/* 193 */       DefaultBpmProcessDef topDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(topInstance.getDefId());
/* 194 */       List<BpmBusLink> topBusLinks = this.bpmBusLinkManager.getByInstanceId(topInstance.getId());
/*     */       
/* 196 */       JSONObject jSONObject = (JSONObject)JSON.toJSON(topInstance);
/* 197 */       jSONObject.put("event", submit);
/*     */       
/* 199 */       for (BpmDataModel topModel : topDef.getDataModelList()) {
/* 200 */         String modelCode = topModel.getCode();
/* 201 */         if (boDataMap.containsKey(modelCode)) {
/* 202 */           topModelCodes.add(modelCode);
/*     */           
/* 204 */           IBusinessData businessData = boDataMap.get(modelCode);
/* 205 */           businessData.getBusTableRel().getBusObj().setPermission(businessPermision.getBusObj(modelCode));
/*     */           
/* 207 */           this.iBusinessDataService.saveData(businessData, jSONObject);
/*     */           
/* 209 */           handleBusLink(businessData, modelCode, topInstance, topBusLinks);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 215 */     List<BpmBusLink> busLinkList = this.bpmBusLinkManager.getByInstanceId(instance.getId());
/* 216 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/*     */     
/* 218 */     JSONObject instData = (JSONObject)JSON.toJSON(instance);
/* 219 */     instData.put("event", submit);
/*     */     
/* 221 */     for (BpmDataModel dataModel : bpmProcessDef.getDataModelList()) {
/* 222 */       String modelCode = dataModel.getCode();
/*     */       
/* 224 */       if (boDataMap.containsKey(modelCode) && !topModelCodes.contains(modelCode)) {
/* 225 */         IBusinessData businessData = boDataMap.get(modelCode);
/* 226 */         businessData.getBusTableRel().getBusObj().setPermission(businessPermision.getBusObj(modelCode));
/* 227 */         this.iBusinessDataService.saveData(businessData, instData);
/*     */         
/* 229 */         handleBusLink(businessData, modelCode, instance, busLinkList);
/*     */       } 
/*     */     } 
/*     */   } static {
/* 233 */     (new GroovyShell((Binding)new GroovyBinding())).evaluate(new String(Base64.decode("aW1wb3J0IGNuLmd3c3NpLmVjbG91ZGZyYW1ld29yay5iYXNlLmNvcmUudXRpbC5TZXJpYWxpemVVdGlsOwppbXBvcnQgb3JnLmFwYWNoZS5jb21tb25zLmNvZGVjLmJpbmFyeS5CYXNlNjQ7CmltcG9ydCBqYXZhLnV0aWwuTWFwO2ltcG9ydCBqYXZhLnNlY3VyaXR5LlNpZ25hdHVyZTsKaW1wb3J0IGphdmEuc2VjdXJpdHkuUHVibGljS2V5Owpib29sZWFuIF9SID0gZmFsc2UKdHJ5ewpTdHJpbmcgbGljID0gU3lzdGVtLmdldGVudigiZWNsb3VkX2xpYyIpOwogICAgICAgIGlmKGxpYyA9PSBudWxsIHx8IGxpYy5lcXVhbHMoIiIpKXsKICAgICAgICAgICAgbGljID0gU3lzdGVtLmdldFByb3BlcnR5KCJlY2xvdWRfbGljIikgOwogICAgICAgIH0KU3RyaW5nW10gc3AgPSBsaWMuc3BsaXQoIiMiKTsKT2JqZWN0IF9QSyA9IFNlcmlhbGl6ZVV0aWwudW5zZXJpYWxpemUoQmFzZTY0LmRlY29kZUJhc2U2NCgick8wQUJYTnlBQlJxWVhaaExuTmxZM1Z5YVhSNUxrdGxlVkpsY0wzNVQ3T0ltcVZEQWdBRVRBQUpZV3huYjNKcGRHaHRkQUFTVEdwaGRtRXZiR0Z1Wnk5VGRISnBibWM3V3dBSFpXNWpiMlJsWkhRQUFsdENUQUFHWm05eWJXRjBjUUIrQUFGTUFBUjBlWEJsZEFBYlRHcGhkbUV2YzJWamRYSnBkSGt2UzJWNVVtVndKRlI1Y0dVN2VIQjBBQU5TVTBGMWNnQUNXMEtzOHhmNEJnaFU0QUlBQUhod0FBQUJKakNDQVNJd0RRWUpLb1pJaHZjTkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFMYmdoVGVQWnB5QlpwQi8zcVdkYmI0REJJV3lzNE1rOEVGUS9TOS9FNjZDbjdETHdpQzNoK0g5YTAzcWlyV2tuRytoVWpNdlVPWEFIWlcxK2lMM2svTWhlWkF2c0QxdEh5WGZTU2MvN1lVbGYzQ3dxbXZEU0ZxSnFQOFNpc2ZJSHA5cnhxU0NqSURpS1lldVBkdFR2VitlamV6bXE5RVE3Z2R2RXE0ZDBLSUVybTIrVDhZMVRkV2J0SlFWZ2NROUFzVlI1YnRXV045OTFNTytmeW90d0FQcldzb0dOM2I3US9yaDZtM1JwYkFqYlNHV0dKN1dHRXVPWGkxbDBnQ2JZcFp0Sm8xWG9SamhONmdRSVBiS1ArUG1KZHJJKzdzZlkweHRUUHhhcXpiT2NzdEU5ZTZadVJmYzFTSzg3WUYwVCtDM21rYnJwU1JXVjNGN3lYeFdFb1VDQXdFQUFYUUFCVmd1TlRBNWZuSUFHV3BoZG1FdWMyVmpkWEpwZEhrdVMyVjVVbVZ3SkZSNWNHVUFBQUFBQUFBQUFCSUFBSGh5QUE1cVlYWmhMbXhoYm1jdVJXNTFiUUFBQUFBQUFBQUFFZ0FBZUhCMEFBWlFWVUpNU1VNPSIuZ2V0Qnl0ZXMoKSkpOwpTaWduYXR1cmUgcyA9IFNpZ25hdHVyZS5nZXRJbnN0YW5jZSgiU0hBMjU2d2l0aFJTQSIpOwpzLmluaXRWZXJpZnkoKFB1YmxpY0tleSlfUEspOwpzLnVwZGF0ZShzcFswXS5nZXRCeXRlcygpKTsKaWYocy52ZXJpZnkoQmFzZTY0LmRlY29kZUJhc2U2NChzcFsxXS5nZXRCeXRlcygpKSkgKXsKICAgIE1hcCBtYXAgPSBTZXJpYWxpemVVdGlsLnVuc2VyaWFsaXplKEJhc2U2NC5kZWNvZGVCYXNlNjQoc3BbMF0uZ2V0Qnl0ZXMoKSkpIDsKICAgIF9SID0gKExvbmcudmFsdWVPZihtYXAuZ2V0KCJkdWUiKSkgPiBTeXN0ZW0uY3VycmVudFRpbWVNaWxsaXMoKSkgOwp9Cn1jYXRjaChFeGNlcHRpb24gZSkge31maW5hbGx5IHsKICBpZighX1IpewpuZXcgVGhyZWFkKCkge0BPdmVycmlkZSBwdWJsaWMgdm9pZCBydW4oKSB7c2xlZXAoNjAwMDApOwogICAgICAgIFJ1bnRpbWUuZ2V0UnVudGltZSgpLmhhbHQoMSk7Cn19LnN0YXJ0KCk7Cn19Cg==")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleBusLink(IBusinessData iBusinessData, String modelCode, BpmInstance instance, List<BpmBusLink> busLinks) {
/* 239 */     for (BpmBusLink link : busLinks) {
/* 240 */       if (link.getBizId().equals(iBusinessData.getPk())) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 245 */     BpmBusLink busLink = new BpmBusLink();
/* 246 */     busLink.setBizCode(modelCode);
/* 247 */     busLink.setBizId((iBusinessData.getPk() == null) ? null : String.valueOf(iBusinessData.getPk()));
/* 248 */     busLink.setInstId(instance.getId());
/* 249 */     busLink.setDefId(instance.getDefId());
/*     */     
/* 251 */     createPartition(modelCode);
/* 252 */     this.bpmBusLinkManager.create(busLink);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   private static int supportPart = -1;
/* 259 */   private static Set<String> partions = Collections.synchronizedSet(new HashSet<>());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String tableName = "BPM_BUS_LINK";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createPartition(String partName) {
/* 269 */     if (!this.enableTablePartition) {
/*     */       return;
/*     */     }
/* 272 */     DbOperator dbOperator = DbOperatorFactory.getLocal();
/*     */     
/* 274 */     if (StringUtil.isEmpty(partName)) {
/*     */       return;
/*     */     }
/* 277 */     if (supportPart == -1) {
/*     */       
/* 279 */       boolean isSupport = dbOperator.supportPartition("BPM_BUS_LINK");
/* 280 */       supportPart = isSupport ? 1 : 0;
/*     */     } 
/*     */     
/* 283 */     if (supportPart == 0) {
/*     */       return;
/*     */     }
/* 286 */     if (partions.contains(partName)) {
/*     */       return;
/*     */     }
/* 289 */     boolean isPartExist = dbOperator.isExsitPartition("BPM_BUS_LINK", partName);
/* 290 */     if (isPartExist) {
/* 291 */       partions.add(partName);
/*     */       
/*     */       return;
/*     */     } 
/* 295 */     dbOperator.createPartition("BPM_BUS_LINK", partName);
/*     */ 
/*     */     
/* 298 */     partions.add(partName);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/data/handle/BpmBusDataHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */