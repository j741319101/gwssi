package com.dstz.bpm.wf.rest.vo;

import com.dstz.bpm.core.model.BpmDefinition;

public class BpmDefinitionVO extends BpmDefinition {
   protected String createUser;
   protected String updateUser;
   protected String lockUser;

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public String getLockUser() {
      return this.lockUser;
   }

   public void setLockUser(String lockUser) {
      this.lockUser = lockUser;
   }
}
