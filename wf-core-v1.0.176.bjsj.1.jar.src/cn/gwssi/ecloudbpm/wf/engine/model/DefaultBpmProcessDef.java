/*     */ package com.dstz.bpm.engine.model;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*     */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*     */ import com.dstz.bpm.api.model.def.BpmDefProperties;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.def.BpmVariableDef;
/*     */ import com.dstz.bpm.api.model.def.NodeInit;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultBpmProcessDef
/*     */   implements BpmProcessDef
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  30 */   private String defKey = "";
/*  31 */   private String name = "";
/*  32 */   private String processDefinitionId = "";
/*     */   private List<BpmNodeDef> bpmnNodeDefs;
/*  34 */   private BpmProcessDef parentProcessDef = null;
/*     */   
/*  36 */   private List<BpmPluginContext> pluginContextList = new ArrayList<>();
/*     */   
/*  38 */   private List<BpmVariableDef> varList = new ArrayList<>();
/*     */   
/*  40 */   private List<BpmDataModel> dataModelList = new ArrayList<>();
/*     */   
/*  42 */   private List<Button> instanceBtnList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*  46 */   private BpmForm instForm = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private BpmForm instMobileForm = null;
/*     */ 
/*     */ 
/*     */   
/*  55 */   private BpmForm globalForm = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private BpmForm globalMobileForm = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private BpmDefProperties extPropertys = new BpmDefProperties();
/*     */   
/*  68 */   private List<NodeInit> nodeInitList = new ArrayList<>();
/*     */   
/*     */   private JSONObject processDefJson;
/*     */   
/*     */   public BpmProcessDef getParentProcessDef() {
/*  73 */     return this.parentProcessDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmDefProperties getExtProperties() {
/*  83 */     return this.extPropertys;
/*     */   }
/*     */   
/*     */   public void setExtProperties(BpmDefProperties extPropertys) {
/*  87 */     this.extPropertys = extPropertys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmPluginContext> getBpmPluginContexts() {
/*  96 */     return this.pluginContextList;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmPluginContext getBpmPluginContext(Class<?> clazz) {
/* 101 */     List<BpmPluginContext> Plugins = getBpmPluginContexts();
/* 102 */     if (CollectionUtil.isEmpty(Plugins)) return null;
/*     */     
/* 104 */     for (BpmPluginContext pulgin : Plugins) {
/* 105 */       if (pulgin.getClass().isAssignableFrom(clazz))
/* 106 */         return pulgin; 
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmVariableDef> getVariableList() {
/* 117 */     return this.varList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmForm getInstForm() {
/* 126 */     return this.instForm;
/*     */   }
/*     */   
/*     */   public BpmForm getInstMobileForm() {
/* 130 */     return this.instMobileForm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmForm getGlobalForm() {
/* 139 */     return this.globalForm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmForm getGlobalMobileForm() {
/* 147 */     return this.globalMobileForm;
/*     */   }
/*     */   
/*     */   public List<BpmPluginContext> getPluginContextList() {
/* 151 */     return this.pluginContextList;
/*     */   }
/*     */   
/*     */   public void setPluginContextList(List<BpmPluginContext> pluginContextList) {
/* 155 */     Collections.sort(pluginContextList);
/* 156 */     this.pluginContextList = pluginContextList;
/*     */   }
/*     */   
/*     */   public List<BpmVariableDef> getVarList() {
/* 160 */     return this.varList;
/*     */   }
/*     */   
/*     */   public void setVarList(List<BpmVariableDef> varList) {
/* 164 */     this.varList = varList;
/*     */   }
/*     */   
/*     */   public void setInstForm(BpmForm instForm) {
/* 168 */     this.instForm = instForm;
/*     */   }
/*     */   
/*     */   public void setInstMobileForm(BpmForm instMobileForm) {
/* 172 */     this.instMobileForm = instMobileForm;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 176 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 181 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setGlobalForm(BpmForm globalForm) {
/* 185 */     this.globalForm = globalForm;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGlobalMobileForm(BpmForm globalMobileForm) {
/* 190 */     this.globalMobileForm = globalMobileForm;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefKey() {
/* 196 */     return this.defKey;
/*     */   }
/*     */   
/*     */   public void setDefKey(String defKey) {
/* 200 */     this.defKey = defKey;
/*     */   }
/*     */   
/*     */   public void setProcessDefinitionId(String processDefinitionId) {
/* 204 */     this.processDefinitionId = processDefinitionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProcessDefinitionId() {
/* 209 */     return this.processDefinitionId;
/*     */   }
/*     */   
/*     */   public void setBpmnNodeDefs(List<BpmNodeDef> bpmnNodeDefs) {
/* 213 */     this.bpmnNodeDefs = bpmnNodeDefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getBpmnNodeDefs() {
/* 220 */     return this.bpmnNodeDefs;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmNodeDef getBpmnNodeDef(String nodeId) {
/* 225 */     for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
/* 226 */       if (nodeId.equals(nodeDef.getNodeId())) {
/* 227 */         return nodeDef;
/*     */       }
/*     */     } 
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmNodeDef getStartEvent() {
/* 235 */     for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
/* 236 */       if (nodeDef.getType().equals(NodeType.START)) {
/* 237 */         return nodeDef;
/*     */       }
/*     */     } 
/* 240 */     return null;
/*     */   }
/*     */   
/*     */   public List<NodeInit> getNodeInitList() {
/* 244 */     return this.nodeInitList;
/*     */   }
/*     */   
/*     */   public List<NodeInit> getNodeInitList(String nodeId) {
/* 248 */     List<NodeInit> initList = new ArrayList<>();
/* 249 */     for (NodeInit init : this.nodeInitList) {
/* 250 */       if (StringUtil.isNotEmpty(nodeId) && init.getNodeId().equals(nodeId)) {
/* 251 */         initList.add(init);
/*     */       }
/*     */     } 
/*     */     
/* 255 */     return initList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNodeInitList(List<NodeInit> nodeInitList) {
/* 260 */     this.nodeInitList = nodeInitList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getStartNodes() {
/* 266 */     BpmNodeDef startNode = getStartEvent();
/* 267 */     if (startNode == null) return null; 
/* 268 */     return startNode.getOutcomeNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getEndEvents() {
/* 273 */     List<BpmNodeDef> rtnList = new ArrayList<>();
/* 274 */     for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
/* 275 */       if (nodeDef.getType().equals(NodeType.END)) {
/* 276 */         rtnList.add(nodeDef);
/*     */       }
/*     */     } 
/* 279 */     return rtnList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmDataModel> getDataModelList() {
/* 284 */     return this.dataModelList;
/*     */   }
/*     */   public String getDataModelKeys() {
/* 287 */     List<String> keys = new ArrayList<>();
/* 288 */     for (BpmDataModel model : this.dataModelList) {
/* 289 */       keys.add(model.getCode());
/*     */     }
/* 291 */     return keys.isEmpty() ? null : StringUtil.join(keys);
/*     */   }
/*     */   
/*     */   public void setDataModelList(List<BpmDataModel> dataModelList) {
/* 295 */     this.dataModelList = dataModelList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParentProcessDef(DefaultBpmProcessDef processDef) {
/* 300 */     this.parentProcessDef = processDef;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJson(JSONObject bpmDefSetting) {
/* 306 */     this.processDefJson = bpmDefSetting;
/*     */   }
/*     */   
/*     */   public JSONObject getJson() {
/* 310 */     return this.processDefJson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Button> getInstanceBtnList() {
/* 318 */     if (CollectionUtil.isEmpty(this.instanceBtnList));
/*     */ 
/*     */     
/* 321 */     return this.instanceBtnList;
/*     */   }
/*     */   
/*     */   public void setInstanceBtnList(List<Button> instanceBtnList) {
/* 325 */     this.instanceBtnList = instanceBtnList;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/model/DefaultBpmProcessDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */