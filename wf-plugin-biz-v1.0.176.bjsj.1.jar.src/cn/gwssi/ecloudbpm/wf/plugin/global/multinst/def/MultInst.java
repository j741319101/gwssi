/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
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
/*     */   private String script;
/*     */   
/*     */   public String getStartNodeKey() {
/*  47 */     return this.startNodeKey;
/*     */   }
/*     */   
/*     */   public void setStartNodeKey(String startNodeKey) {
/*  51 */     this.startNodeKey = startNodeKey;
/*     */   }
/*     */   
/*     */   public String getEndNodeKey() {
/*  55 */     return this.endNodeKey;
/*     */   }
/*     */   
/*     */   public void setEndNodeKey(String endNodeKey) {
/*  59 */     this.endNodeKey = endNodeKey;
/*     */   }
/*     */   
/*     */   public String getRecoveryStrategy() {
/*  63 */     return this.recoveryStrategy;
/*     */   }
/*     */   
/*     */   public void setRecoveryStrategy(String recoveryStrategy) {
/*  67 */     this.recoveryStrategy = recoveryStrategy;
/*     */   }
/*     */   
/*     */   public void parseMultInstContainNode(DefaultBpmProcessDef processDef) {
/*  71 */     BpmNodeDef startNode = processDef.getBpmnNodeDef(this.startNodeKey);
/*     */     
/*  73 */     Set<String> existNode = new HashSet<>();
/*  74 */     Set<String> containNode = new HashSet<>();
/*  75 */     List<BpmNodeDef> nextNodeDefs = startNode.getOutcomeNodes();
/*  76 */     nextNodeDefs.forEach(node -> isEndNode(node, existNode, containNode));
/*     */ 
/*     */     
/*  79 */     setContainNode(containNode);
/*     */   }
/*     */   
/*     */   private boolean isEndNode(BpmNodeDef nextNodeDef, Set<String> existNode, Set<String> containNode) {
/*  83 */     boolean lastIsEndNode = true;
/*  84 */     if (StringUtils.equals(nextNodeDef.getNodeId(), this.endNodeKey)) {
/*  85 */       return true;
/*     */     }
/*  87 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/*  88 */     for (BpmNodeDef bpmNodeDef : nodeDefs) {
/*  89 */       if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
/*  90 */         return false;
/*     */       }
/*  92 */       existNode.add(nextNodeDef.getNodeId());
/*  93 */       if (!isEndNode(bpmNodeDef, existNode, containNode)) {
/*  94 */         lastIsEndNode = false;
/*     */       }
/*     */     } 
/*  97 */     if (lastIsEndNode) {
/*  98 */       containNode.add(nextNodeDef.getNodeId());
/*     */     } else {
/* 100 */       remove(nextNodeDef, containNode);
/*     */     } 
/*     */     
/* 103 */     return lastIsEndNode;
/*     */   }
/*     */   
/*     */   private void remove(BpmNodeDef nextNodeDef, Set<String> containNode) {
/* 107 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/* 108 */     nodeDefs.forEach(bpmNodeDef -> {
/*     */           if (containNode.contains(bpmNodeDef.getNodeId())) {
/*     */             remove(bpmNodeDef, containNode);
/*     */             containNode.remove(bpmNodeDef.getNodeId());
/*     */           } 
/*     */         });
/* 114 */     containNode.remove(nextNodeDef.getNodeId());
/*     */   }
/*     */   
/*     */   public Set<String> getContainNode() {
/* 118 */     return this.containNode;
/*     */   }
/*     */   
/*     */   public void setContainNode(Set<String> containNode) {
/* 122 */     this.containNode = containNode;
/*     */   }
/*     */   
/*     */   public String getScript() {
/* 126 */     return this.script;
/*     */   }
/*     */   
/*     */   public void setScript(String script) {
/* 130 */     this.script = script;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/multinst/def/MultInst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */