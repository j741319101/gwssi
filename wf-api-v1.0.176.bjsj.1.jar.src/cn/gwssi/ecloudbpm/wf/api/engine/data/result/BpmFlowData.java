/*     */ package com.dstz.bpm.api.engine.data.result;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.List;
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
/*     */ @ApiModel("流程数据")
/*     */ public class BpmFlowData
/*     */   implements FlowData
/*     */ {
/*     */   @ApiModelProperty("流程定义ID")
/*     */   protected String defId;
/*     */   @ApiModelProperty("流程定义名字")
/*     */   protected String defName;
/*     */   @ApiModelProperty("流程当前节点表单")
/*     */   protected BpmForm Form;
/*     */   @ApiModelProperty("流程自定义表单业务数据")
/*     */   protected JSONObject data;
/*     */   @ApiModelProperty("流程自定义表单 权限")
/*     */   protected JSONObject permission;
/*     */   @ApiModelProperty("流程自定义表单 表权限")
/*     */   protected JSONObject tablePermission;
/*     */   @ApiModelProperty("流程自定义表单 初始化数据，可用于子表数据复制赋值")
/*     */   protected JSONObject initData;
/*     */   @ApiModelProperty("流程 当前节点按钮信息")
/*     */   protected List<Button> buttonList;
/*     */   protected String labels;
/*     */   @ApiModelProperty("流程属性配置")
/*  42 */   protected BpmFlowConfigSpecified configSpecified = new BpmFlowConfigSpecified();
/*     */ 
/*     */   
/*     */   transient Map<String, IBusinessData> dataMap;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("流程全局变量")
/*     */   protected Map<String, Object> variables;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefId() {
/*  54 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/*  58 */     this.defId = defId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmForm getForm() {
/*  66 */     return this.Form;
/*     */   }
/*     */   
/*     */   public void setForm(BpmForm Form) {
/*  70 */     this.Form = Form;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getPermission() {
/*  75 */     return this.permission;
/*     */   }
/*     */   
/*     */   public JSONObject getTablePermission() {
/*  79 */     return this.tablePermission;
/*     */   }
/*     */   
/*     */   public void setTablePermission(JSONObject tablePermission) {
/*  83 */     this.tablePermission = tablePermission;
/*     */   }
/*     */   
/*     */   public void setPermission(JSONObject permission) {
/*  87 */     this.permission = permission;
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
/*     */   
/*     */   public JSONObject getData() {
/* 101 */     return this.data;
/*     */   }
/*     */   
/*     */   public String getDefName() {
/* 105 */     return this.defName;
/*     */   }
/*     */   
/*     */   public void setDefName(String defName) {
/* 109 */     this.defName = defName;
/*     */   }
/*     */   
/*     */   public void setData(JSONObject dataModel) {
/* 113 */     this.data = dataModel;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Button> getButtonList() {
/* 118 */     return this.buttonList;
/*     */   }
/*     */   
/*     */   public Map<String, IBusinessData> getDataMap() {
/* 122 */     return this.dataMap;
/*     */   }
/*     */   
/*     */   public void setDataMap(Map<String, IBusinessData> dataMap) {
/* 126 */     this.dataMap = dataMap;
/*     */   }
/*     */   
/*     */   public void setButtonList(List<Button> list) {
/* 130 */     this.buttonList = list;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getInitData() {
/* 135 */     return this.initData;
/*     */   }
/*     */   
/*     */   public void setInitData(JSONObject initData) {
/* 139 */     this.initData = initData;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getVariables() {
/* 143 */     return this.variables;
/*     */   }
/*     */   
/*     */   public void setVariables(Map<String, Object> variables) {
/* 147 */     this.variables = variables;
/*     */   }
/*     */   
/*     */   public BpmFlowConfigSpecified getConfigSpecified() {
/* 151 */     return this.configSpecified;
/*     */   }
/*     */   
/*     */   public void setConfigSpecified(BpmFlowConfigSpecified configSpecified) {
/* 155 */     this.configSpecified = configSpecified;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabels() {
/* 160 */     return this.labels;
/*     */   }
/*     */   
/*     */   public void setLabels(String labels) {
/* 164 */     this.labels = labels;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/BpmFlowData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */