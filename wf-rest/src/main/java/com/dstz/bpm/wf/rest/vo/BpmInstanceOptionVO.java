package com.dstz.bpm.wf.rest.vo;

import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
import java.util.ArrayList;
import java.util.List;

public class BpmInstanceOptionVO extends BpmInstanceOption {
   private String parentTaskId;
   private String parentNodeId;
   private String orgName;
   private Integer orgSn;
   private String taskType;
   private String dynamicTaskId;
   private String defId;
   private String parentDefId;
   private List<BpmTaskOpinionVO> bpmTaskOpinionVOS = new ArrayList();

   public String getParentTaskId() {
      return this.parentTaskId;
   }

   public void setParentTaskId(String parentTaskId) {
      this.parentTaskId = parentTaskId;
   }

   public String getParentNodeId() {
      return this.parentNodeId;
   }

   public void setParentNodeId(String parentNodeId) {
      this.parentNodeId = parentNodeId;
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

   public String getTaskType() {
      return this.taskType;
   }

   public void setTaskType(String taskType) {
      this.taskType = taskType;
   }

   public String getDynamicTaskId() {
      return this.dynamicTaskId;
   }

   public void setDynamicTaskId(String dynamicTaskId) {
      this.dynamicTaskId = dynamicTaskId;
   }

   public String getDefId() {
      return this.defId;
   }

   public void setDefId(String defId) {
      this.defId = defId;
   }

   public String getParentDefId() {
      return this.parentDefId;
   }

   public void setParentDefId(String parentDefId) {
      this.parentDefId = parentDefId;
   }

   public List<BpmTaskOpinionVO> getBpmTaskOpinionVOS() {
      return this.bpmTaskOpinionVOS;
   }

   public void setBpmTaskOpinionVOS(List<BpmTaskOpinionVO> bpmTaskOpinionVOS) {
      this.bpmTaskOpinionVOS = bpmTaskOpinionVOS;
   }
}
