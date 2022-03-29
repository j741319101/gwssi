package com.dstz.bpm.wf.rest.vo;

import java.io.Serializable;

public class UserTaskNodeVO implements Serializable {
   private static final long serialVersionUID = -587907919367773210L;
   private String nodeId;
   private String nodeName;

   public UserTaskNodeVO() {
   }

   public UserTaskNodeVO(String nodeId, String nodeName) {
      this.nodeId = nodeId;
      this.nodeName = nodeName;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public String toString() {
      return "UserTaskNodeVO{nodeId='" + this.nodeId + '\'' + ", nodeName='" + this.nodeName + '\'' + '}';
   }
}
