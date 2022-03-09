/*     */ package com.dstz.bpm.api.engine.action.cmd;
/*     */ 
/*     */ import com.dstz.form.api.model.FormCategory;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Map;
/*     */ import org.hibernate.validator.constraints.NotBlank;
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
/*     */ @ApiModel(description = "流程实例、流程任务 ActionCmd 请求参数，请参考FlowRequestParam.java 或者文档了解 ")
/*     */ public class FlowRequestParam
/*     */ {
/*     */   @ApiModelProperty("流程定义id，流程启动等场景必须")
/*     */   private String defId;
/*     */   @ApiModelProperty("流程定义key")
/*     */   private String defKey;
/*     */   @ApiModelProperty("流程实例id，流程草稿等场景")
/*     */   private String instanceId;
/*     */   @ApiModelProperty("流程任务id，流程任务处理时必须")
/*     */   private String taskId;
/*     */   @ApiModelProperty("流程任务候选人关系id，流程任务处理时必须")
/*     */   private String taskLinkId;
/*     */   @ApiModelProperty("启动流程时组织id")
/*     */   private String startOrgId;
/*     */   private String nodeId;
/*     */   @NotBlank
/*     */   @ApiModelProperty("action name 必须")
/*     */   private String action;
/*     */   private String actionName;
/*     */   @ApiModelProperty("前端节点人员设置")
/*     */   private JSONObject nodeUsers;
/*     */   @ApiModelProperty("流程业务数据，JSON格式：{boCodeA:{},boCodeB:{}}")
/*     */   private JSONObject data;
/*     */   @ApiModelProperty("流程业务主键。 URL表单，可以直接赋值调用rest接口启动流程")
/*     */   private String businessKey;
/*     */   @ApiModelProperty("表单类型")
/*  62 */   private String formType = FormCategory.INNER
/*  63 */     .value();
/*     */ 
/*     */   
/*     */   @ApiModelProperty("流程任务审批意见")
/*     */   private String opinion;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("目标节点")
/*     */   private String destination;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("特殊属性扩展配置：可以再 ActionCmd 中拿到此配置")
/*     */   private JSONObject extendConf;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("流程变量，启动或者处理任务可以设置ACT的流程变量")
/*     */   private Map<String, Object> variables;
/*     */ 
/*     */ 
/*     */   
/*     */   public FlowRequestParam(String taskId, String action, JSONObject data, String opinion) {
/*  84 */     this.taskId = taskId;
/*  85 */     this.action = action;
/*  86 */     this.data = data;
/*  87 */     this.opinion = opinion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlowRequestParam(String defId, String action, JSONObject data) {
/*  98 */     this.defId = defId;
/*  99 */     this.action = action;
/* 100 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public FlowRequestParam() {}
/*     */ 
/*     */   
/*     */   public String getDefId() {
/* 108 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/* 112 */     this.defId = defId;
/*     */   }
/*     */   
/*     */   public String getInstanceId() {
/* 116 */     return this.instanceId;
/*     */   }
/*     */   
/*     */   public void setInstanceId(String instanceId) {
/* 120 */     this.instanceId = instanceId;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 124 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 128 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getAction() {
/* 132 */     return this.action;
/*     */   }
/*     */   
/*     */   public void setAction(String action) {
/* 136 */     this.action = action;
/*     */   }
/*     */   
/*     */   public String getBusinessKey() {
/* 140 */     return this.businessKey;
/*     */   }
/*     */   
/*     */   public String getTaskLinkId() {
/* 144 */     return this.taskLinkId;
/*     */   }
/*     */   
/*     */   public void setTaskLinkId(String taskLinkId) {
/* 148 */     this.taskLinkId = taskLinkId;
/*     */   }
/*     */   
/*     */   public String getStartOrgId() {
/* 152 */     return this.startOrgId;
/*     */   }
/*     */   
/*     */   public void setStartOrgId(String startOrgId) {
/* 156 */     this.startOrgId = startOrgId;
/*     */   }
/*     */   
/*     */   public void setBusinessKey(String businessKey) {
/* 160 */     this.businessKey = businessKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getNodeUsers() {
/* 165 */     return this.nodeUsers;
/*     */   }
/*     */   
/*     */   public void setNodeUsers(JSONObject nodeUsers) {
/* 169 */     this.nodeUsers = nodeUsers;
/*     */   }
/*     */   
/*     */   public JSONObject getData() {
/* 173 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(JSONObject data) {
/* 177 */     this.data = data;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/* 181 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/* 185 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public String getOpinion() {
/* 189 */     return this.opinion;
/*     */   }
/*     */   
/*     */   public void setOpinion(String opinion) {
/* 193 */     this.opinion = opinion;
/*     */   }
/*     */   
/*     */   public String getDestination() {
/* 197 */     return this.destination;
/*     */   }
/*     */   
/*     */   public void setDestination(String destination) {
/* 201 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getExtendConf() {
/* 206 */     return this.extendConf;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExtendConf(JSONObject extendConf) {
/* 211 */     this.extendConf = extendConf;
/*     */   }
/*     */   
/*     */   public String getDefKey() {
/* 215 */     return this.defKey;
/*     */   }
/*     */   
/*     */   public void setDefKey(String defKey) {
/* 219 */     this.defKey = defKey;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getVariables() {
/* 223 */     return this.variables;
/*     */   }
/*     */   
/*     */   public void setVariables(Map<String, Object> variables) {
/* 227 */     this.variables = variables;
/*     */   }
/*     */   
/*     */   public String getActionName() {
/* 231 */     return this.actionName;
/*     */   }
/*     */   
/*     */   public void setActionName(String actionName) {
/* 235 */     this.actionName = actionName;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/* 239 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 243 */     this.nodeId = nodeId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/cmd/FlowRequestParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */