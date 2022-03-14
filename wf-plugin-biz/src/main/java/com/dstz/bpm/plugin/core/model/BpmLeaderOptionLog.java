package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;

public class BpmLeaderOptionLog extends BaseModel {
   private String leaderId;
   private String leaderName;
   private String secretaryId;
   private String secretaryName;
   private String taskId;
   private String instId;
   private String option;
   private String type;

   public String getLeaderId() {
      return this.leaderId;
   }

   public String getLeaderName() {
      return this.leaderName;
   }

   public String getInstId() {
      return this.instId;
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public void setLeaderName(String leaderName) {
      this.leaderName = leaderName;
   }

   public String getSecretaryName() {
      return this.secretaryName;
   }

   public void setSecretaryName(String secretaryName) {
      this.secretaryName = secretaryName;
   }

   public void setLeaderId(String leaderId) {
      this.leaderId = leaderId;
   }

   public String getSecretaryId() {
      return this.secretaryId;
   }

   public void setSecretaryId(String secretaryId) {
      this.secretaryId = secretaryId;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getOption() {
      return this.option;
   }

   public void setOption(String option) {
      this.option = option;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }
}
