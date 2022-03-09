/*     */ package cn.gwssi.ecloudbpm.wf.core.model.overallview;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
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
/*     */ public class BpmOverallView
/*     */ {
/*     */   public static final String IMPORT_TYPE_OVERRIDE = "override";
/*     */   public static final String IMPORT_TYPE_EDIT = "edit";
/*     */   public static final String IMPORT_TYPE_NEW_VERSION = "newVersion";
/*     */   private BpmDefinition bpmDefinition;
/*  27 */   private String importType = "edit";
/*     */   
/*     */   private String defId;
/*     */   
/*     */   private Boolean isUpdateVersion;
/*     */   
/*     */   private JSONObject defSetting;
/*     */   
/*     */   private String bpmnXml;
/*     */   
/*     */   private String modelJson;
/*     */   
/*  39 */   private JSONArray permission = new JSONArray();
/*     */ 
/*     */   
/*     */   public String getImportType() {
/*  43 */     return this.importType;
/*     */   }
/*     */   
/*     */   public void setImportType(String importType) {
/*  47 */     this.importType = importType;
/*     */   }
/*     */   
/*     */   public Boolean getIsUpdateVersion() {
/*  51 */     return this.isUpdateVersion;
/*     */   }
/*     */   
/*     */   public void setIsUpdateVersion(Boolean isUpdateVersion) {
/*  55 */     this.isUpdateVersion = isUpdateVersion;
/*     */   }
/*     */   
/*     */   public JSONObject getDefSetting() {
/*  59 */     return this.defSetting;
/*     */   }
/*     */   
/*     */   public void setDefSetting(JSONObject defSetting) {
/*  63 */     this.defSetting = defSetting;
/*     */   }
/*     */   
/*     */   public BpmDefinition getBpmDefinition() {
/*  67 */     return this.bpmDefinition;
/*     */   }
/*     */   
/*     */   public void setBpmDefinition(BpmDefinition bpmDefinition) {
/*  71 */     this.bpmDefinition = bpmDefinition;
/*     */   }
/*     */   
/*     */   public String getDefId() {
/*  75 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/*  79 */     this.defId = defId;
/*     */   }
/*     */   
/*     */   public String getBpmnXml() {
/*  83 */     return this.bpmnXml;
/*     */   }
/*     */   
/*     */   public void setBpmnXml(String bpmnXml) {
/*  87 */     this.bpmnXml = bpmnXml;
/*     */   }
/*     */   
/*     */   public JSONArray getPermission() {
/*  91 */     return this.permission;
/*     */   }
/*     */   
/*     */   public void setPermission(JSONArray permission) {
/*  95 */     this.permission = permission;
/*     */   }
/*     */   
/*     */   public String getModelJson() {
/*  99 */     return this.modelJson;
/*     */   }
/*     */   
/*     */   public void setModelJson(String modelJson) {
/* 103 */     this.modelJson = modelJson;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/overallview/BpmOverallView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */