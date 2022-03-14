package com.dstz.bpm.plugin.global.multinst.def;

import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

public class MultInst implements Serializable {
   private static final long serialVersionUID = -1120163905633604828L;
   public static final String RECOVERYSTRATEGY_COMPLETED = "completed";
   public static final String RECOVERYSTRATEGY_AUTONOMOUSLY = "autonomously";
   @NotEmpty
   private String startNodeKey;
   @NotEmpty
   private String endNodeKey;
   @NotEmpty
   private String recoveryStrategy;
   private Set<String> containNode;
   private String script;

   public String getStartNodeKey() {
      return this.startNodeKey;
   }

   public void setStartNodeKey(String startNodeKey) {
      this.startNodeKey = startNodeKey;
   }

   public String getEndNodeKey() {
      return this.endNodeKey;
   }

   public void setEndNodeKey(String endNodeKey) {
      this.endNodeKey = endNodeKey;
   }

   public String getRecoveryStrategy() {
      return this.recoveryStrategy;
   }

   public void setRecoveryStrategy(String recoveryStrategy) {
      this.recoveryStrategy = recoveryStrategy;
   }

   public void parseMultInstContainNode(DefaultBpmProcessDef processDef) {
      BpmNodeDef startNode = processDef.getBpmnNodeDef(this.startNodeKey);
      Set<String> existNode = new HashSet();
      Set<String> containNode = new HashSet();
      List<BpmNodeDef> nextNodeDefs = startNode.getOutcomeNodes();
      nextNodeDefs.forEach((node) -> {
         this.isEndNode(node, existNode, containNode);
      });
      this.setContainNode(containNode);
   }

   private boolean isEndNode(BpmNodeDef nextNodeDef, Set<String> existNode, Set<String> containNode) {
      boolean lastIsEndNode = true;
      if (StringUtils.equals(nextNodeDef.getNodeId(), this.endNodeKey)) {
         return true;
      } else {
         List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
         Iterator var6 = nodeDefs.iterator();

         while(var6.hasNext()) {
            BpmNodeDef bpmNodeDef = (BpmNodeDef)var6.next();
            if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
               return false;
            }

            existNode.add(nextNodeDef.getNodeId());
            if (!this.isEndNode(bpmNodeDef, existNode, containNode)) {
               lastIsEndNode = false;
            }
         }

         if (lastIsEndNode) {
            containNode.add(nextNodeDef.getNodeId());
         } else {
            this.remove(nextNodeDef, containNode);
         }

         return lastIsEndNode;
      }
   }

   private void remove(BpmNodeDef nextNodeDef, Set<String> containNode) {
      List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
      nodeDefs.forEach((bpmNodeDef) -> {
         if (containNode.contains(bpmNodeDef.getNodeId())) {
            this.remove(bpmNodeDef, containNode);
            containNode.remove(bpmNodeDef.getNodeId());
         }

      });
      containNode.remove(nextNodeDef.getNodeId());
   }

   public Set<String> getContainNode() {
      return this.containNode;
   }

   public void setContainNode(Set<String> containNode) {
      this.containNode = containNode;
   }

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }
}
