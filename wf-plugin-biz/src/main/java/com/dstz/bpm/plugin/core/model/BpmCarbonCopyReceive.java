package com.dstz.bpm.plugin.core.model;

import com.dstz.bpm.api.constant.CarbonCopyStatus;
import com.dstz.base.core.model.BaseModel;

public class BpmCarbonCopyReceive extends BaseModel {
   private static final long serialVersionUID = -3089259622456880564L;
   private String ccRecordId;
   private String receiveUserId;
   private String receiveUserName;
   private String status;
   private Boolean read;
   private Integer rev;
   private String type;

   public BpmCarbonCopyReceive() {
      this.status = CarbonCopyStatus.UNREAD.getKey();
      this.type = "";
   }

   public void setCcRecordId(String ccRecordId) {
      this.ccRecordId = ccRecordId;
   }

   public String getCcRecordId() {
      return this.ccRecordId;
   }

   public void setReceiveUserId(String receiveUserId) {
      this.receiveUserId = receiveUserId;
   }

   public String getReceiveUserName() {
      return this.receiveUserName;
   }

   public void setReceiveUserName(String receiveUserName) {
      this.receiveUserName = receiveUserName;
   }

   public String getReceiveUserId() {
      return this.receiveUserId;
   }

   public void setRead(Boolean read) {
      this.read = read;
   }

   public Boolean getRead() {
      return this.read;
   }

   public void setRev(Integer rev) {
      this.rev = rev;
   }

   public Integer getRev() {
      return this.rev;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
