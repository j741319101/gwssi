/*     */ package com.dstz.bpm.plugin.global.multinst.def;
/*     */ 
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultInst
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1120163905633604828L;
/*     */   public static final String RECOVERYSTRATEGY_COMPLETED = "completed";
/*     */   public static final String RECOVERYSTRATEGY_AUTONOMOUSLY = "autonomously";
/*     */   @NotEmpty
/*     */   private String startNodeKey;
/*     */   @NotEmpty
/*     */   private String endNodeKey;
/*     */   @NotEmpty
/*     */   private String recoveryStrategy;
/*     */   private Set<String> containNode;
/*     */   
/*     */   public String getStartNodeKey() {
/*  43 */     return this.startNodeKey;
/*     */   }
/*     */   
/*     */   public void setStartNodeKey(String startNodeKey) {
/*  47 */     this.startNodeKey = startNodeKey;
/*     */   }
/*     */   
/*     */   public String getEndNodeKey() {
/*  51 */     return this.endNodeKey;
/*     */   }
/*     */   
/*     */   public void setEndNodeKey(String endNodeKey) {
/*  55 */     this.endNodeKey = endNodeKey;
/*     */   }
/*     */   
/*     */   public String getRecoveryStrategy() {
/*  59 */     return this.recoveryStrategy;
/*     */   }
/*     */   
/*     */   public void setRecoveryStrategy(String recoveryStrategy) {
/*  63 */     this.recoveryStrategy = recoveryStrategy;
/*     */   }
/*     */   
/*     */   public void parseMultInstContainNode(DefaultBpmProcessDef processDef) {
/*  67 */     BpmNodeDef startNode = processDef.getBpmnNodeDef(this.startNodeKey);
/*     */     
/*  69 */     Set<String> existNode = new HashSet<>();
/*  70 */     Set<String> containNode = new HashSet<>();
/*  71 */     List<BpmNodeDef> nextNodeDefs = startNode.getOutcomeNodes();
/*  72 */     nextNodeDefs.forEach(node -> isEndNode(node, existNode, containNode));
/*     */ 
/*     */     
/*  75 */     setContainNode(containNode);
/*     */   }
/*     */   
/*     */   private boolean isEndNode(BpmNodeDef nextNodeDef, Set<String> existNode, Set<String> containNode) {
/*  79 */     boolean lastIsEndNode = true;
/*  80 */     if (StringUtils.equals(nextNodeDef.getNodeId(), this.endNodeKey)) {
/*  81 */       return true;
/*     */     }
/*  83 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/*  84 */     for (BpmNodeDef bpmNodeDef : nodeDefs) {
/*  85 */       if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
/*  86 */         return false;
/*     */       }
/*  88 */       existNode.add(nextNodeDef.getNodeId());
/*  89 */       if (!isEndNode(bpmNodeDef, existNode, containNode)) {
/*  90 */         lastIsEndNode = false;
/*     */       }
/*     */     } 
/*  93 */     if (lastIsEndNode) {
/*  94 */       containNode.add(nextNodeDef.getNodeId());
/*     */     } else {
/*  96 */       remove(nextNodeDef, containNode);
/*     */     } 
/*     */     
/*  99 */     return lastIsEndNode;
/*     */   }
/*     */   
/*     */   private void remove(BpmNodeDef nextNodeDef, Set<String> containNode) {
/* 103 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/* 104 */     nodeDefs.forEach(bpmNodeDef -> {
/*     */           if (containNode.contains(bpmNodeDef.getNodeId())) {
/*     */             remove(bpmNodeDef, containNode);
/*     */             containNode.remove(bpmNodeDef.getNodeId());
/*     */           } 
/*     */         });
/* 110 */     containNode.remove(nextNodeDef.getNodeId());
/*     */   }
/*     */   
/*     */   public Set<String> getContainNode() {
/* 114 */     return this.containNode;
/*     */   }
/*     */   
/*     */   public void setContainNode(Set<String> containNode) {
/* 118 */     this.containNode = containNode;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/multinst/def/MultInst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */