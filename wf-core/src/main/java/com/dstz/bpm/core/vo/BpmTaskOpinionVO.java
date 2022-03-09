/*     */ package com.dstz.bpm.core.vo;
/*     */ 
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class BpmTaskOpinionVO
/*     */   extends BpmTaskOpinion implements Comparable<BpmTaskOpinionVO> {
/*     */   private String parentTaskId;
/*     */   private String parentNodeId;
/*     */   private String orgName;
/*     */   private Integer orgSn;
/*     */   private String taskType;
/*     */   private String dynamicTaskId;
/*     */   private String defId;
/*     */   private String parentDefId;
/*  17 */   private List<BpmTaskOpinionVO> bpmTaskOpinionVOS = new ArrayList<>();
/*     */   
/*     */   public String getParentTaskId() {
/*  20 */     return this.parentTaskId;
/*     */   }
/*     */   
/*     */   public void setParentTaskId(String parentTaskId) {
/*  24 */     this.parentTaskId = parentTaskId;
/*     */   }
/*     */   
/*     */   public String getParentNodeId() {
/*  28 */     return this.parentNodeId;
/*     */   }
/*     */   
/*     */   public void setParentNodeId(String parentNodeId) {
/*  32 */     this.parentNodeId = parentNodeId;
/*     */   }
/*     */   
/*     */   public String getOrgName() {
/*  36 */     return this.orgName;
/*     */   }
/*     */   
/*     */   public void setOrgName(String orgName) {
/*  40 */     this.orgName = orgName;
/*     */   }
/*     */   
/*     */   public Integer getOrgSn() {
/*  44 */     return this.orgSn;
/*     */   }
/*     */   
/*     */   public void setOrgSn(Integer orgSn) {
/*  48 */     this.orgSn = orgSn;
/*     */   }
/*     */   
/*     */   public String getTaskType() {
/*  52 */     return this.taskType;
/*     */   }
/*     */   
/*     */   public void setTaskType(String taskType) {
/*  56 */     this.taskType = taskType;
/*     */   }
/*     */   
/*     */   public String getDynamicTaskId() {
/*  60 */     return this.dynamicTaskId;
/*     */   }
/*     */   
/*     */   public void setDynamicTaskId(String dynamicTaskId) {
/*  64 */     this.dynamicTaskId = dynamicTaskId;
/*     */   }
/*     */   
/*     */   public List<BpmTaskOpinionVO> getBpmTaskOpinionVOS() {
/*  68 */     return this.bpmTaskOpinionVOS;
/*     */   }
/*     */   
/*     */   public void setBpmTaskOpinionVOS(List<BpmTaskOpinionVO> bpmTaskOpinionVOS) {
/*  72 */     this.bpmTaskOpinionVOS = bpmTaskOpinionVOS;
/*     */   }
/*     */   public void addBpmTaskOpinionVO(BpmTaskOpinionVO bpmTaskOpinionVO) {
/*  75 */     this.bpmTaskOpinionVOS.add(bpmTaskOpinionVO);
/*     */   }
/*     */   public int compareTo(BpmTaskOpinionVO opinion) {
/*  78 */     if (this.orgSn == null || opinion.getOrgSn() == null) {
/*  79 */       if (this.createTime.getTime() > opinion.createTime.getTime())
/*  80 */         return 1; 
/*  81 */       if (this.createTime.getTime() == opinion.createTime.getTime()) {
/*  82 */         return 0;
/*     */       }
/*  84 */       return -1;
/*     */     } 
/*  86 */     if (this.orgSn.intValue() > opinion.getOrgSn().intValue())
/*  87 */       return 1; 
/*  88 */     if (this.orgSn == opinion.getOrgSn()) {
/*  89 */       return 0;
/*     */     }
/*  91 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefId() {
/*  96 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/* 100 */     this.defId = defId;
/*     */   }
/*     */   
/*     */   public String getParentDefId() {
/* 104 */     return this.parentDefId;
/*     */   }
/*     */   
/*     */   public void setParentDefId(String parentDefId) {
/* 108 */     this.parentDefId = parentDefId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/vo/BpmTaskOpinionVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */