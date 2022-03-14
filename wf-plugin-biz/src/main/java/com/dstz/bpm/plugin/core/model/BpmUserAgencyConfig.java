package com.dstz.bpm.plugin.core.model;

import com.dstz.base.api.model.IBaseModel;
import java.util.Date;

public class BpmUserAgencyConfig implements IBaseModel {
   private static final long serialVersionUID = -5472201936456269888L;
   private String id;
   private Date startDatetime;
   private Date endDatetime;
   private String agencyFlowKey;
   private String agencyFlowName;
   private String configUserId;
   private String targetUserId;
   private String targetUserName;
   private Boolean enable = true;
   private String createBy;
   private Date createTime;
   private String createOrgId;
   private String updateBy;
   private Date updateTime;
   private String comment;
   private String configUserName;
   private Integer rev;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Date getStartDatetime() {
      return this.startDatetime;
   }

   public void setStartDatetime(Date startDatetime) {
      this.startDatetime = startDatetime;
   }

   public Date getEndDatetime() {
      return this.endDatetime;
   }

   public void setEndDatetime(Date endDatetime) {
      this.endDatetime = endDatetime;
   }

   public String getAgencyFlowKey() {
      return this.agencyFlowKey;
   }

   public void setAgencyFlowKey(String agencyFlowKey) {
      this.agencyFlowKey = agencyFlowKey;
   }

   public String getAgencyFlowName() {
      return this.agencyFlowName;
   }

   public void setAgencyFlowName(String agencyFlowName) {
      this.agencyFlowName = agencyFlowName;
   }

   public String getConfigUserId() {
      return this.configUserId;
   }

   public void setConfigUserId(String configUserId) {
      this.configUserId = configUserId;
   }

   public String getTargetUserId() {
      return this.targetUserId;
   }

   public void setTargetUserId(String targetUserId) {
      this.targetUserId = targetUserId;
   }

   public String getTargetUserName() {
      return this.targetUserName;
   }

   public void setTargetUserName(String targetUserName) {
      this.targetUserName = targetUserName;
   }

   public Boolean getEnable() {
      return this.enable;
   }

   public void setEnable(Boolean enable) {
      this.enable = enable;
   }

   public String getCreateBy() {
      return this.createBy;
   }

   public void setCreateBy(String createBy) {
      this.createBy = createBy;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getCreateOrgId() {
      return this.createOrgId;
   }

   public void setCreateOrgId(String createOrgId) {
      this.createOrgId = createOrgId;
   }

   public String getUpdateBy() {
      return this.updateBy;
   }

   public void setUpdateBy(String updateBy) {
      this.updateBy = updateBy;
   }

   public Date getUpdateTime() {
      return this.updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public Integer getRev() {
      return this.rev;
   }

   public void setRev(Integer rev) {
      this.rev = rev;
   }

   public String getConfigUserName() {
      return this.configUserName;
   }

   public void setConfigUserName(String configUserName) {
      this.configUserName = configUserName;
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
