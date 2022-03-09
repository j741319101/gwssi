/*     */ package cn.gwssi.ecloudbpm.wf.plugin.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
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
/*     */ public class BpmCarbonCopyRecord
/*     */   extends BaseModel
/*     */ {
/*     */   private static final long serialVersionUID = 3978044694562910321L;
/*     */   private String instId;
/*     */   private String taskId;
/*     */   private String nodeId;
/*     */   private String nodeName;
/*     */   private String formType;
/*     */   private String event;
/*     */   private String triggerUserId;
/*     */   private String triggerUserName;
/*     */   private String subject;
/*     */   private String content;
/*     */   private Integer rev;
/*     */   
/*     */   public String getInstId() {
/*  72 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  76 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/*  80 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  84 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/*  88 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/*  92 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getNodeName() {
/*  96 */     return this.nodeName;
/*     */   }
/*     */   
/*     */   public void setNodeName(String nodeName) {
/* 100 */     this.nodeName = nodeName;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/* 104 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/* 108 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public String getEvent() {
/* 112 */     return this.event;
/*     */   }
/*     */   
/*     */   public void setEvent(String event) {
/* 116 */     this.event = event;
/*     */   }
/*     */   
/*     */   public String getTriggerUserId() {
/* 120 */     return this.triggerUserId;
/*     */   }
/*     */   
/*     */   public void setTriggerUserId(String triggerUserId) {
/* 124 */     this.triggerUserId = triggerUserId;
/*     */   }
/*     */   
/*     */   public String getTriggerUserName() {
/* 128 */     return this.triggerUserName;
/*     */   }
/*     */   
/*     */   public void setTriggerUserName(String triggerUserName) {
/* 132 */     this.triggerUserName = triggerUserName;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 136 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 140 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 144 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/* 148 */     this.content = content;
/*     */   }
/*     */   
/*     */   public Integer getRev() {
/* 152 */     return this.rev;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 156 */     this.rev = rev;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/BpmCarbonCopyRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */