package com.dstz.bpm.plugin.dto;

import java.io.Serializable;

public class BpmUserAgencyConfigTabDTO extends PageDTO implements Serializable {
   private static final long serialVersionUID = 1712345490619029539L;
   private String tab;
   private String configUserId;
   private String name;

   public String getTab() {
      return this.tab;
   }

   public void setTab(String tab) {
      this.tab = tab;
   }

   public String getConfigUserId() {
      return this.configUserId;
   }

   public void setConfigUserId(String configUserId) {
      this.configUserId = configUserId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
