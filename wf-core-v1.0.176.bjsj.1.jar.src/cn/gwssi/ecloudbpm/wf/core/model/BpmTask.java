/*     */ package com.dstz.bpm.core.model;
/*     */ 
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.base.api.model.IBaseModel;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Date;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*     */ @ApiModel(description = "流程任务信息")
/*     */ public class BpmTask
/*     */   implements IBaseModel, IBpmTask
/*     */ {
/*     */   @ApiModelProperty("任务ID")
/*     */   protected String id;
/*     */   @ApiModelProperty("任务名")
/*     */   protected String name;
/*     */   @ApiModelProperty("任务标题")
/*     */   protected String subject;
/*     */   @ApiModelProperty("流程实例ID")
/*     */   protected String instId;
/*     */   @ApiModelProperty("任务原生TaskID")
/*     */   protected String taskId;
/*     */   @ApiModelProperty("任务节点ID")
/*     */   protected String nodeId;
/*     */   @ApiModelProperty("流程定义ID")
/*     */   protected String defId;
/*     */   @ApiModelProperty("候选人ID，若为0 则为多人")
/*     */   protected String assigneeId;
/*     */   @ApiModelProperty("候选人Name")
/*     */   protected String assigneeNames;
/*     */   @ApiModelProperty("任务状态")
/*     */   protected String status;
/*     */   @ApiModelProperty("任务优先级，默认50")
/*     */   protected Integer priority;
/*     */   protected Date dueTime;
/*     */   @ApiModelProperty("任务类型")
/*     */   protected String taskType;
/*     */   @ApiModelProperty("父任务ID 会签，分发等情况会用到")
/*     */   protected String parentId;
/*     */   @ApiModelProperty("原生实例ID")
/*     */   protected String actInstId;
/*     */   @ApiModelProperty("原生ExecutionId")
/*     */   protected String actExecutionId;
/*     */   protected String typeId;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   @ApiModelProperty("任务是否支持移动端")
/*     */   protected Integer supportMobile;
/*     */   @ApiModelProperty("驳回后返回至节点")
/*     */   protected String backNode;
/*     */   private String defKey;
/*     */   
/*     */   public String getTaskType() {
/* 123 */     return this.taskType;
/*     */   }
/*     */   
/*     */   public void setTaskType(String taskType) {
/* 127 */     this.taskType = taskType;
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
/*     */   public void setId(String id) {
/* 152 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 160 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 164 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 172 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 176 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 184 */     return this.subject;
/*     */   }
/*     */   
/*     */   public String getAssigneeNames() {
/* 188 */     return this.assigneeNames;
/*     */   }
/*     */   
/*     */   public void setAssigneeNames(String assigneeNames) {
/* 192 */     this.assigneeNames = assigneeNames;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 196 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 204 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 208 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/* 216 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 220 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 228 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/* 232 */     this.defId = defId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefId() {
/* 240 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setAssigneeId(String assigneeId) {
/* 244 */     this.assigneeId = assigneeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAssigneeId() {
/* 252 */     return this.assigneeId;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 256 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 264 */     return this.status;
/*     */   }
/*     */   
/*     */   public String getBackNode() {
/* 268 */     return this.backNode;
/*     */   }
/*     */   
/*     */   public void setBackNode(String backNode) {
/* 272 */     this.backNode = backNode;
/*     */   }
/*     */   
/*     */   public void setPriority(Integer priority) {
/* 276 */     this.priority = priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPriority() {
/* 284 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setDueTime(Date dueTime) {
/* 288 */     this.dueTime = dueTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDueTime() {
/* 296 */     return this.dueTime;
/*     */   }
/*     */   
/*     */   public void setParentId(String parentId) {
/* 300 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 308 */     return this.parentId;
/*     */   }
/*     */   
/*     */   public void setActInstId(String actInstId) {
/* 312 */     this.actInstId = actInstId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActInstId() {
/* 320 */     return this.actInstId;
/*     */   }
/*     */   
/*     */   public void setActExecutionId(String actExecutionId) {
/* 324 */     this.actExecutionId = actExecutionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 329 */     this.typeId = typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeId() {
/* 337 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 341 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 349 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 353 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 361 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setSupportMobile(Integer supportMobile) {
/* 365 */     this.supportMobile = supportMobile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSupportMobile() {
/* 373 */     return this.supportMobile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 379 */     return (new ToStringBuilder(this))
/* 380 */       .append("id", this.id)
/* 381 */       .append("name", this.name)
/* 382 */       .append("subject", this.subject)
/* 383 */       .append("instId", this.instId)
/* 384 */       .append("taskId", this.taskId)
/* 385 */       .append("nodeId", this.nodeId)
/* 386 */       .append("defId", this.defId)
/* 387 */       .append("assigneeId", this.assigneeId)
/* 388 */       .append("status", this.status)
/* 389 */       .append("priority", this.priority)
/* 390 */       .append("dueTime", this.dueTime)
/* 391 */       .append("taskType", this.taskType)
/* 392 */       .append("parentId", this.parentId)
/* 393 */       .append("actInstId", this.actInstId)
/* 394 */       .append("actExecutionId", this.actExecutionId)
/* 395 */       .append("typeId", this.typeId)
/* 396 */       .append("createTime", this.createTime)
/* 397 */       .append("createBy", this.createBy)
/* 398 */       .append("supportMobile", this.supportMobile)
/* 399 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getActExecutionId() {
/* 404 */     return this.actExecutionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 409 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updatetime) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 419 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {}
/*     */ 
/*     */   
/*     */   public String getDefKey() {
/* 428 */     return this.defKey;
/*     */   }
/*     */   
/*     */   public void setDefKey(String defKey) {
/* 432 */     this.defKey = defKey;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */