package com.dstz.bpm.plugin.core.model;

import com.dstz.base.core.model.BaseModel;

public class BpmSubmitDataLog extends BaseModel {
   protected String taskId;
   protected String instId;
   protected String data;
   protected String destination;
   protected String extendConf;

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public String getInstId() {
      return this.instId;
   }

   public void setData(String data) {
      this.data = data;
   }

   public String getData() {
      return this.data;
   }

   public void setDestination(String destination) {
      this.destination = destination;
   }

   public String getDestination() {
      return this.destination;
   }

   public void setExtendConf(String extendConf) {
      this.extendConf = extendConf;
   }

   public String getExtendConf() {
      return this.extendConf;
   }
}
