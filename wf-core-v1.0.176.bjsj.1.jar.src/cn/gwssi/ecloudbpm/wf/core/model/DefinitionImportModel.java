/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysAuthorization;
/*     */ import java.io.Serializable;
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
/*     */ public class DefinitionImportModel
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   private String defTypeTreeKey;
/*     */   private String defTypeKey;
/*     */   private BpmDefinition def;
/*     */   String jsonXml;
/*     */   String pngByte;
/*     */   String bpmDefSetting;
/*     */   List<SysAuthorization> lstAuthorization;
/*     */   List<BusinessPermission> lstBusinessPermission;
/*     */   
/*     */   public String getDefTypeTreeKey() {
/*  50 */     return this.defTypeTreeKey;
/*     */   }
/*     */   
/*     */   public void setDefTypeTreeKey(String defTypeTreeKey) {
/*  54 */     this.defTypeTreeKey = defTypeTreeKey;
/*     */   }
/*     */   
/*     */   public String getDefTypeKey() {
/*  58 */     return this.defTypeKey;
/*     */   }
/*     */   
/*     */   public void setDefTypeKey(String defTypeKey) {
/*  62 */     this.defTypeKey = defTypeKey;
/*     */   }
/*     */   
/*     */   public BpmDefinition getDef() {
/*  66 */     return this.def;
/*     */   }
/*     */   
/*     */   public void setDef(BpmDefinition def) {
/*  70 */     this.def = def;
/*     */   }
/*     */   
/*     */   public String getJsonXml() {
/*  74 */     return this.jsonXml;
/*     */   }
/*     */   
/*     */   public void setJsonXml(String jsonXml) {
/*  78 */     this.jsonXml = jsonXml;
/*     */   }
/*     */   
/*     */   public String getPngByte() {
/*  82 */     return this.pngByte;
/*     */   }
/*     */   
/*     */   public void setPngByte(String pngByte) {
/*  86 */     this.pngByte = pngByte;
/*     */   }
/*     */   
/*     */   public String getBpmDefSetting() {
/*  90 */     return this.bpmDefSetting;
/*     */   }
/*     */   
/*     */   public void setBpmDefSetting(String bpmDefSetting) {
/*  94 */     this.bpmDefSetting = bpmDefSetting;
/*     */   }
/*     */   
/*     */   public List<SysAuthorization> getLstAuthorization() {
/*  98 */     return this.lstAuthorization;
/*     */   }
/*     */   
/*     */   public void setLstAuthorization(List<SysAuthorization> lstAuthorization) {
/* 102 */     this.lstAuthorization = lstAuthorization;
/*     */   }
/*     */   
/*     */   public List<BusinessPermission> getLstBusinessPermission() {
/* 106 */     return this.lstBusinessPermission;
/*     */   }
/*     */   
/*     */   public void setLstBusinessPermission(List<BusinessPermission> lstBusinessPermission) {
/* 110 */     this.lstBusinessPermission = lstBusinessPermission;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/DefinitionImportModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */