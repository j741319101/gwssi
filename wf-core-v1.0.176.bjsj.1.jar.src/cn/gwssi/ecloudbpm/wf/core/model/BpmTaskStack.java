/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
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
/*     */ public class BpmTaskStack
/*     */   implements IDModel, BpmExecutionStack
/*     */ {
/*     */   protected String id;
/*     */   protected String taskId;
/*     */   protected String instId;
/*     */   protected String parentId;
/*     */   protected String nodeId;
/*     */   protected String nodeName;
/*     */   protected Date startTime;
/*     */   protected Date endTime;
/*     */   protected Short isMulitiTask;
/*     */   protected String nodeType;
/*     */   protected String actionName;
/*     */   protected String trace;
/*     */   private Integer level;
/*     */   
/*     */   public void setId(String id) {
/*  81 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  89 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  97 */     this.taskId = taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskId() {
/* 105 */     return this.taskId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInstId(String instId) {
/* 113 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 121 */     return this.instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentId(String parentId) {
/* 129 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 137 */     return this.parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 145 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 153 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 161 */     this.nodeName = nodeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeName() {
/* 169 */     return this.nodeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(Date startTime) {
/* 177 */     this.startTime = startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getStartTime() {
/* 185 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 193 */     this.endTime = endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getEndTime() {
/* 201 */     return this.endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsMulitiTask(Short isMulitiTask) {
/* 209 */     this.isMulitiTask = isMulitiTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Short getIsMulitiTask() {
/* 217 */     return this.isMulitiTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActionName() {
/* 225 */     return this.actionName;
/*     */   }
/*     */   
/*     */   public void setActionName(String actionName) {
/* 229 */     this.actionName = actionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 236 */     return (new ToStringBuilder(this))
/* 237 */       .append("id", this.id)
/* 238 */       .append("taskId", this.taskId)
/* 239 */       .append("instId", this.instId)
/* 240 */       .append("parentId", this.parentId)
/* 241 */       .append("nodeId", this.nodeId)
/* 242 */       .append("nodeName", this.nodeName)
/* 243 */       .append("startTime", this.startTime)
/* 244 */       .append("endTime", this.endTime)
/* 245 */       .append("isMulitiTask", this.isMulitiTask)
/* 246 */       .append("nodeTYpe", this.nodeType)
/* 247 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNodeType(String nodeType) {
/* 252 */     this.nodeType = nodeType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeType() {
/* 258 */     return this.nodeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActionName(ActionCmd actionModel) {
/* 267 */     if (actionModel != null) {
/* 268 */       setActionName(actionModel.getActionName());
/*     */     }
/*     */     
/* 271 */     if ((ActionType.CREATE.getKey().equals(getActionName()) || ActionType.START.getKey().equals(getActionName())) && 
/* 272 */       this.nodeType != null && "sequenceFlow,inclusiveGateway,exclusiveGateway,parallelGateway,serviceTask,".indexOf(this.nodeType) != -1) {
/* 273 */       setActionName(ActionType.AGREE.getKey());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTrace() {
/* 279 */     return this.trace;
/*     */   }
/*     */   
/*     */   public void setTrace(String trace) {
/* 283 */     this.trace = trace;
/*     */   }
/*     */   
/*     */   public Integer getLevel() {
/* 287 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Integer level) {
/* 291 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 296 */     if (this == o) return true; 
/* 297 */     if (o == null || getClass() != o.getClass()) return false; 
/* 298 */     BpmTaskStack that = (BpmTaskStack)o;
/* 299 */     return Objects.equals(this.id, that.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 304 */     return Objects.hash(new Object[] { this.id });
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTaskStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */