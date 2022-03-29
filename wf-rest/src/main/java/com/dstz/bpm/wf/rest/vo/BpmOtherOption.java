package com.dstz.bpm.wf.rest.vo;

import java.util.List;

public class BpmOtherOption {
   private String nodeName;
   private String nodeId;
   private String time;
   private List<BpmMetaPartentOption> children;

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public List<BpmMetaPartentOption> getChildren() {
      return this.children;
   }

   public void setChildren(List<BpmMetaPartentOption> children) {
      this.children = children;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }
}
