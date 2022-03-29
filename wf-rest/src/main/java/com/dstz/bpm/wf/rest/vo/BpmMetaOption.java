package com.dstz.bpm.wf.rest.vo;

public class BpmMetaOption {
   private String name;
   private String org;
   private String orgCode;
   private String roleName;
   private String roleCode;
   private String userSort;
   private String time;
   private String opinion;

   public String getOrgCode() {
      return this.orgCode;
   }

   public void setOrgCode(String orgCode) {
      this.orgCode = orgCode;
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
}
