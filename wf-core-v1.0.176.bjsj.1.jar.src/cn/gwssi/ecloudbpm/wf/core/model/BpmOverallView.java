/*    */ package cn.gwssi.ecloudbpm.wf.core.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BpmOverallView
/*    */ {
/*    */   public static final String IMPORT_TYPE_OVERRIDE = "override";
/*    */   public static final String IMPORT_TYPE_EDIT = "edit";
/*    */   public static final String IMPORT_TYPE_NEW_VERSION = "newVersion";
/*    */   private transient BpmDefinition bpmDefinition;
/* 28 */   private List<IBusinessPermission> formRights = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/* 32 */   private String importType = "edit";
/*    */   
/*    */   private String defId;
/*    */   
/*    */   private Boolean isUpdateVersion;
/*    */   
/*    */   private JSONObject defSetting;
/*    */   
/*    */   private String bpmnXml;
/*    */ 
/*    */   
/*    */   public List<IBusinessPermission> getFormRights() {
/* 44 */     return this.formRights;
/*    */   }
/*    */   
/*    */   public void setFormRights(List<IBusinessPermission> formRights) {
/* 48 */     this.formRights = formRights;
/*    */   }
/*    */   
/*    */   public String getImportType() {
/* 52 */     return this.importType;
/*    */   }
/*    */   
/*    */   public void setImportType(String importType) {
/* 56 */     this.importType = importType;
/*    */   }
/*    */   
/*    */   public Boolean getIsUpdateVersion() {
/* 60 */     return this.isUpdateVersion;
/*    */   }
/*    */   
/*    */   public void setIsUpdateVersion(Boolean isUpdateVersion) {
/* 64 */     this.isUpdateVersion = isUpdateVersion;
/*    */   }
/*    */   
/*    */   public JSONObject getDefSetting() {
/* 68 */     return this.defSetting;
/*    */   }
/*    */   
/*    */   public void setDefSetting(JSONObject defSetting) {
/* 72 */     this.defSetting = defSetting;
/*    */   }
/*    */   
/*    */   public BpmDefinition getBpmDefinition() {
/* 76 */     return this.bpmDefinition;
/*    */   }
/*    */   
/*    */   public void setBpmDefinition(BpmDefinition bpmDefinition) {
/* 80 */     this.bpmDefinition = bpmDefinition;
/*    */   }
/*    */   
/*    */   public String getDefId() {
/* 84 */     return this.defId;
/*    */   }
/*    */   
/*    */   public void setDefId(String defId) {
/* 88 */     this.defId = defId;
/*    */   }
/*    */   
/*    */   public String getBpmnXml() {
/* 92 */     return this.bpmnXml;
/*    */   }
/*    */   
/*    */   public void setBpmnXml(String bpmnXml) {
/* 96 */     this.bpmnXml = bpmnXml;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmOverallView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */