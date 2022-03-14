/*     */ package com.dstz.bpm.rest.vo;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmOtherOptionVO
/*     */   extends BpmOtherOption
/*     */ {
/*     */   private String defId;
/*     */   private String taskType;
/*     */   private String taskId;
/*     */   private String opinion;
/*     */   private String instId;
/*     */   private String orgName;
/*     */   private Integer orgSn;
/*  39 */   private List<BpmOtherOptionVO> bpmTaskOpinionVOS = new ArrayList<>(0);
/*     */ 
/*     */   
/*     */   public String getDefId() {
/*  43 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/*  47 */     this.defId = defId;
/*     */   }
/*     */   
/*     */   public String getTaskType() {
/*  51 */     return this.taskType;
/*     */   }
/*     */   
/*     */   public void setTaskType(String taskType) {
/*  55 */     this.taskType = taskType;
/*     */   }
/*     */   
/*     */   public List<BpmOtherOptionVO> getBpmTaskOpinionVOS() {
/*  59 */     return this.bpmTaskOpinionVOS;
/*     */   }
/*     */   
/*     */   public void setBpmTaskOpinionVOS(List<BpmOtherOptionVO> bpmTaskOpinionVOS) {
/*  63 */     this.bpmTaskOpinionVOS = bpmTaskOpinionVOS;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/*  67 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/*  71 */     this.taskId = taskId;
/*     */   }
/*     */   
/*     */   public String getOpinion() {
/*  75 */     return this.opinion;
/*     */   }
/*     */   
/*     */   public void setOpinion(String opinion) {
/*  79 */     this.opinion = opinion;
/*     */   }
/*     */   
/*     */   public String getInstId() {
/*  83 */     return this.instId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  87 */     this.instId = instId;
/*     */   }
/*     */   
/*     */   public String getOrgName() {
/*  91 */     return this.orgName;
/*     */   }
/*     */   
/*     */   public void setOrgName(String orgName) {
/*  95 */     this.orgName = orgName;
/*     */   }
/*     */   
/*     */   public Integer getOrgSn() {
/*  99 */     return this.orgSn;
/*     */   }
/*     */   
/*     */   public void setOrgSn(Integer orgSn) {
/* 103 */     this.orgSn = orgSn;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/BpmOtherOptionVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */