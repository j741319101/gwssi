package com.dstz.bpm.wf.rest.vo;

import java.util.ArrayList;
import java.util.List;

public class BpmOtherOptionVO extends BpmOtherOption {
   private String defId;
   private String taskType;
   private String taskId;
   private String opinion;
   private String instId;
   private String orgName;
   private Integer orgSn;
   private List<BpmOtherOptionVO> bpmTaskOpinionVOS = new ArrayList(0);

   public String getDefId() {
      return this.defId;
   }

   public void setDefId(String defId) {
      this.defId = defId;
   }

   public String getTaskType() {
      return this.taskType;
   }

   public void setTaskType(String taskType) {
      this.taskType = taskType;
   }

   public List<BpmOtherOptionVO> getBpmTaskOpinionVOS() {
      return this.bpmTaskOpinionVOS;
   }

   public void setBpmTaskOpinionVOS(List<BpmOtherOptionVO> bpmTaskOpinionVOS) {
      this.bpmTaskOpinionVOS = bpmTaskOpinionVOS;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getOpinion() {
      return this.opinion;
   }

   public void setOpinion(String opinion) {
      this.opinion = opinion;
   }

   public String getInstId() {
      return this.instId;
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public String getOrgName() {
      return this.orgName;
   }

   public void setOrgName(String orgName) {
      this.orgName = orgName;
   }

   public Integer getOrgSn() {
      return this.orgSn;
   }

   public void setOrgSn(Integer orgSn) {
      this.orgSn = orgSn;
   }
}
