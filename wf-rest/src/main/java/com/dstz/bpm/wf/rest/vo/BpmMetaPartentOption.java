package com.dstz.bpm.wf.rest.vo;

import java.util.List;

public class BpmMetaPartentOption {
   private String name;
   private String org;
   private String orgCode;
   private String roleName;
   private String roleCode;
   private String time;
   private String opinion;
   private String userSort;
   private List<BpmMetaOption> children;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getOrg() {
      return this.org;
   }

   public void setOrg(String org) {
      this.org = org;
   }

   public String getOrgCode() {
      return this.orgCode;
   }

   public void setOrgCode(String orgCode) {
      this.orgCode = orgCode;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getOpinion() {
      return this.opinion;
   }

   public void setOpinion(String opinion) {
      this.opinion = opinion;
   }

   public List<BpmMetaOption> getChildren() {
      return this.children;
   }

   public void setChildren(List<BpmMetaOption> children) {
      this.children = children;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public String getRoleCode() {
      return this.roleCode;
   }

   public void setRoleCode(String roleCode) {
      this.roleCode = roleCode;
   }

   public String getUserSort() {
      return this.userSort;
   }

   public void setUserSort(String userSort) {
      this.userSort = userSort;
   }
}
