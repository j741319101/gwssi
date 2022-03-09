/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTaskOpinion;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IBaseModel;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
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
/*     */ @ApiModel(description = "流程任务审批记录")
/*     */ public class BpmTaskOpinion
/*     */   implements IBaseModel, IBpmTaskOpinion
/*     */ {
/*     */   protected String id;
/*     */   @ApiModelProperty("流程实例ID")
/*     */   protected String instId;
/*     */   @ApiModelProperty("父实例ID，用于子流程")
/*     */   protected String supInstId;
/*     */   @ApiModelProperty("任务ID")
/*     */   protected String taskId;
/*     */   @ApiModelProperty("任务节点ID")
/*     */   protected String taskKey;
/*     */   @ApiModelProperty("任务名称")
/*     */   protected String taskName;
/*     */   @ApiModelProperty(hidden = true)
/*     */   protected String trace;
/*     */   @ApiModelProperty(hidden = true)
/*     */   protected String signId;
/*     */   @ApiModelProperty("任务候选人情况")
/*     */   private String assignInfo;
/*     */   @ApiModelProperty("任务审批人ID")
/*     */   protected String approver;
/*     */   @ApiModelProperty("任务审批人名字")
/*     */   protected String approverName;
/*     */   @ApiModelProperty("任务处理意见")
/*     */   protected String opinion;
/*     */   @ApiModelProperty("状态")
/*     */   protected String status;
/*     */   @ApiModelProperty(hidden = true)
/*     */   protected String formId;
/*     */   protected String createBy;
/*     */   protected Date createTime;
/*     */   @ApiModelProperty("审批时间")
/*     */   protected Date approveTime;
/*     */   @ApiModelProperty("任务处理耗时")
/*     */   protected Long durMs;
/*     */   private String version;
/*     */   @ApiModelProperty("审批堆栈")
/*     */   protected String actExecutionId;
/*     */   @ApiModelProperty("任务所属组织")
/*     */   protected String taskOrgId;
/*     */   
/*     */   public String getVersion() {
/* 137 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(String version) {
/* 141 */     this.version = version;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 145 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 153 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/* 157 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 165 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setSupInstId(String supInstId) {
/* 169 */     this.supInstId = supInstId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSupInstId() {
/* 177 */     return this.supInstId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 181 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/* 189 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskKey(String taskKey) {
/* 193 */     this.taskKey = taskKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskKey() {
/* 201 */     return this.taskKey;
/*     */   }
/*     */   
/*     */   public void setTaskName(String taskName) {
/* 205 */     this.taskName = taskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskName() {
/* 213 */     return this.taskName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApprover(String approver) {
/* 219 */     this.approver = approver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApprover() {
/* 227 */     return this.approver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApproverName(String approverName) {
/* 232 */     this.approverName = approverName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApproverName() {
/* 240 */     return this.approverName;
/*     */   }
/*     */   
/*     */   public void setOpinion(String opinion) {
/* 244 */     this.opinion = opinion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOpinion() {
/* 252 */     return this.opinion;
/*     */   }
/*     */   
/*     */   public Date getApproveTime() {
/* 256 */     return this.approveTime;
/*     */   }
/*     */   
/*     */   public void setApproveTime(Date approveTime) {
/* 260 */     this.approveTime = approveTime;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 264 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 272 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setFormId(String formId) {
/* 276 */     this.formId = formId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormId() {
/* 284 */     return this.formId;
/*     */   }
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 288 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 296 */     return this.createBy;
/*     */   }
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 300 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 308 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setDurMs(Long durMs) {
/* 312 */     this.durMs = durMs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getDurMs() {
/* 320 */     return this.durMs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return (new ToStringBuilder(this))
/* 327 */       .append("id", this.id)
/* 328 */       .append("instId", this.instId)
/* 329 */       .append("supInstId", this.supInstId)
/* 330 */       .append("taskId", this.taskId)
/* 331 */       .append("taskKey", this.taskKey)
/* 332 */       .append("taskName", this.taskName)
/* 333 */       .append("signId", this.signId)
/* 334 */       .append("approver", this.approver)
/* 335 */       .append("approverName", this.approverName)
/* 336 */       .append("opinion", this.opinion)
/* 337 */       .append("status", this.status)
/* 338 */       .append("formId", this.formId)
/* 339 */       .append("createBy", this.createBy)
/* 340 */       .append("createTime", this.createTime)
/* 341 */       .append("approveTime", this.approveTime)
/* 342 */       .append("durMs", this.durMs)
/* 343 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updatetime) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 374 */     if (this == o) return true; 
/* 375 */     if (o == null || getClass() != o.getClass()) return false; 
/* 376 */     BpmTaskOpinion that = (BpmTaskOpinion)o;
/* 377 */     return Objects.equals(this.id, that.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 382 */     return Objects.hash(new Object[] { this.id });
/*     */   }
/*     */   
/*     */   public String getTrace() {
/* 386 */     return this.trace;
/*     */   }
/*     */   
/*     */   public void setTrace(String trace) {
/* 390 */     this.trace = trace;
/*     */   }
/*     */   
/*     */   public String getSignId() {
/* 394 */     return this.signId;
/*     */   }
/*     */   
/*     */   public void setSignId(String signId) {
/* 398 */     this.signId = signId;
/*     */   }
/*     */   
/*     */   public String getAssignInfo() {
/* 402 */     return this.assignInfo;
/*     */   }
/*     */   
/*     */   public void setAssignInfo(String assignInfo) {
/* 406 */     this.assignInfo = assignInfo;
/*     */   }
/*     */   
/*     */   public String getActExecutionId() {
/* 410 */     return this.actExecutionId;
/*     */   }
/*     */   
/*     */   public void setActExecutionId(String actExecutionId) {
/* 414 */     this.actExecutionId = actExecutionId;
/*     */   }
/*     */   
/*     */   public String getTaskOrgId() {
/* 418 */     return this.taskOrgId;
/*     */   }
/*     */   
/*     */   public void setTaskOrgId(String taskOrgId) {
/* 422 */     this.taskOrgId = taskOrgId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTaskOpinion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */