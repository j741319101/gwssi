/*     */ package cn.gwssi.ecloudbpm.wf.api.model.def;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class NodeProperties
/*     */   implements Serializable
/*     */ {
/*     */   public static final String BACK_MODEL_NORMAL = "normal";
/*     */   public static final String BACK_MODEL_BACK = "back";
/*     */   public static final String BACK_USER_MODEL_HISTORY = "history";
/*     */   public static final String BACK_USER_MODEL_NORMAL = "normal";
/*     */   private static final long serialVersionUID = -3157546646728816168L;
/*  31 */   private String nodeId = "";
/*     */   
/*  33 */   private String jumpType = "";
/*     */   
/*     */   private boolean allowExecutorEmpty = true;
/*     */   
/*  37 */   private String backMode = "normal";
/*     */   
/*  39 */   private String backNode = "";
/*     */   
/*  41 */   private String backUserMode = "history";
/*     */   
/*     */   private boolean freeBack = false;
/*     */   
/*  45 */   private String freeSelectUser = "no";
/*     */ 
/*     */   
/*     */   private boolean freeSelectNode = false;
/*     */   
/*  50 */   private String officialDocumentBtnPermission = "";
/*     */   
/*  52 */   private String officialDocumentPermission = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean allowRevocation;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String allowRevocationPreNodeId;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean requiredOpinion;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean allowRecall;
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected String allowRecallPreNodeId = "";
/*     */ 
/*     */ 
/*     */   
/*     */   protected String labels;
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected Boolean allowSysIdentityMultiple = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected List<String> showTaskSysIdentityRule = Arrays.asList(new String[] { "user", "org" });
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected String javaScript = "";
/*     */ 
/*     */   
/*     */   protected Boolean allowBatchActions;
/*     */ 
/*     */   
/*     */   protected String reminderScript;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 101 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 105 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getJumpType() {
/* 109 */     return this.jumpType;
/*     */   }
/*     */   
/*     */   public void setJumpType(String jumpType) {
/* 113 */     this.jumpType = jumpType;
/*     */   }
/*     */   
/*     */   public boolean isAllowExecutorEmpty() {
/* 117 */     return this.allowExecutorEmpty;
/*     */   }
/*     */   
/*     */   public void setAllowExecutorEmpty(boolean allowExecutorEmpty) {
/* 121 */     this.allowExecutorEmpty = allowExecutorEmpty;
/*     */   }
/*     */   
/*     */   public String getBackMode() {
/* 125 */     return this.backMode;
/*     */   }
/*     */   
/*     */   public void setBackMode(String backMode) {
/* 129 */     this.backMode = backMode;
/*     */   }
/*     */   
/*     */   public String getBackNode() {
/* 133 */     return this.backNode;
/*     */   }
/*     */   
/*     */   public void setBackNode(String backNode) {
/* 137 */     this.backNode = backNode;
/*     */   }
/*     */   
/*     */   public String getBackUserMode() {
/* 141 */     return this.backUserMode;
/*     */   }
/*     */   
/*     */   public boolean isFreeSelectNode() {
/* 145 */     return this.freeSelectNode;
/*     */   }
/*     */   
/*     */   public void setFreeSelectNode(boolean freeSelectNode) {
/* 149 */     this.freeSelectNode = freeSelectNode;
/*     */   }
/*     */   
/*     */   public void setBackUserMode(String backUserMode) {
/* 153 */     this.backUserMode = backUserMode;
/*     */   }
/*     */   
/*     */   public String getFreeSelectUser() {
/* 157 */     return this.freeSelectUser;
/*     */   }
/*     */   
/*     */   public void setFreeSelectUser(String freeSelectUser) {
/* 161 */     this.freeSelectUser = freeSelectUser;
/*     */   }
/*     */   
/*     */   public String getOfficialDocumentBtnPermission() {
/* 165 */     return this.officialDocumentBtnPermission;
/*     */   }
/*     */   
/*     */   public void setOfficialDocumentBtnPermission(String officialDocumentBtnPermission) {
/* 169 */     this.officialDocumentBtnPermission = officialDocumentBtnPermission;
/*     */   }
/*     */   
/*     */   public boolean isFreeBack() {
/* 173 */     return this.freeBack;
/*     */   }
/*     */   
/*     */   public void setFreeBack(boolean freeBack) {
/* 177 */     this.freeBack = freeBack;
/*     */   }
/*     */   
/*     */   public Boolean getAllowRevocation() {
/* 181 */     return this.allowRevocation;
/*     */   }
/*     */   
/*     */   public void setAllowRevocation(Boolean allowRevocation) {
/* 185 */     this.allowRevocation = allowRevocation;
/*     */   }
/*     */   
/*     */   public String getAllowRevocationPreNodeId() {
/* 189 */     return this.allowRevocationPreNodeId;
/*     */   }
/*     */   
/*     */   public void setAllowRevocationPreNodeId(String allowRevocationPreNodeId) {
/* 193 */     this.allowRevocationPreNodeId = allowRevocationPreNodeId;
/*     */   }
/*     */   
/*     */   public Boolean getRequiredOpinion() {
/* 197 */     return this.requiredOpinion;
/*     */   }
/*     */   
/*     */   public void setRequiredOpinion(Boolean requiredOpinion) {
/* 201 */     this.requiredOpinion = requiredOpinion;
/*     */   }
/*     */   
/*     */   public Boolean getAllowRecall() {
/* 205 */     return this.allowRecall;
/*     */   }
/*     */   
/*     */   public void setAllowRecall(Boolean allowRecall) {
/* 209 */     this.allowRecall = allowRecall;
/*     */   }
/*     */   
/*     */   public String getAllowRecallPreNodeId() {
/* 213 */     return this.allowRecallPreNodeId;
/*     */   }
/*     */   
/*     */   public void setAllowRecallPreNodeId(String allowRecallPreNodeId) {
/* 217 */     this.allowRecallPreNodeId = allowRecallPreNodeId;
/*     */   }
/*     */   
/*     */   public String getOfficialDocumentPermission() {
/* 221 */     return this.officialDocumentPermission;
/*     */   }
/*     */   
/*     */   public void setOfficialDocumentPermission(String officialDocumentPermission) {
/* 225 */     this.officialDocumentPermission = officialDocumentPermission;
/*     */   }
/*     */   
/*     */   public String getLabels() {
/* 229 */     return this.labels;
/*     */   }
/*     */   
/*     */   public void setLabels(String labels) {
/* 233 */     this.labels = labels;
/*     */   }
/*     */   
/*     */   public List<String> getShowTaskSysIdentityRule() {
/* 237 */     return this.showTaskSysIdentityRule;
/*     */   }
/*     */   
/*     */   public void setShowTaskSysIdentityRule(List<String> showTaskSysIdentityRule) {
/* 241 */     this.showTaskSysIdentityRule = showTaskSysIdentityRule;
/*     */   }
/*     */   
/*     */   public String getJavaScript() {
/* 245 */     return this.javaScript;
/*     */   }
/*     */   
/*     */   public void setJavaScript(String javaScript) {
/* 249 */     this.javaScript = javaScript;
/*     */   }
/*     */   
/*     */   public Boolean getAllowSysIdentityMultiple() {
/* 253 */     return this.allowSysIdentityMultiple;
/*     */   }
/*     */   
/*     */   public void setAllowSysIdentityMultiple(Boolean allowSysIdentityMultiple) {
/* 257 */     this.allowSysIdentityMultiple = allowSysIdentityMultiple;
/*     */   }
/*     */   
/*     */   public Boolean getAllowBatchActions() {
/* 261 */     return this.allowBatchActions;
/*     */   }
/*     */   
/*     */   public void setAllowBatchActions(Boolean allowBatchActions) {
/* 265 */     this.allowBatchActions = allowBatchActions;
/*     */   }
/*     */   
/*     */   public String getReminderScript() {
/* 269 */     return this.reminderScript;
/*     */   }
/*     */   
/*     */   public void setReminderScript(String reminderScript) {
/* 273 */     this.reminderScript = reminderScript;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/NodeProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */