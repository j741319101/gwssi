/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*     */ public class FormBusSet
/*     */   implements IDModel
/*     */ {
/*     */   @XmlAttribute(name = "id")
/*     */   protected String id;
/*     */   @XmlAttribute(name = "formKey")
/*     */   protected String formKey;
/*     */   @XmlElement(name = "jsPreScript")
/*     */   protected String jsPreScript;
/*     */   @XmlElement(name = "jsAfterScript")
/*     */   protected String jsAfterScript;
/*     */   @XmlElement(name = "preScript")
/*     */   protected String preScript;
/*     */   @XmlElement(name = "afterScript")
/*     */   protected String afterScript;
/*     */   @XmlElement(name = "showScript")
/*     */   protected String showScript;
/*     */   @XmlAttribute(name = "isTreeList")
/*     */   protected Short isTreeList;
/*     */   @XmlElement(name = "treeConf")
/*     */   protected String treeConf;
/*     */   
/*     */   public void setId(String id) {
/*  74 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  83 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setFormKey(String formKey) {
/*  87 */     this.formKey = formKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormKey() {
/*  96 */     return this.formKey;
/*     */   }
/*     */   
/*     */   public void setJsPreScript(String jsPreScript) {
/* 100 */     this.jsPreScript = jsPreScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJsPreScript() {
/* 109 */     return this.jsPreScript;
/*     */   }
/*     */   
/*     */   public void setJsAfterScript(String jsAfterScript) {
/* 113 */     this.jsAfterScript = jsAfterScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJsAfterScript() {
/* 122 */     return this.jsAfterScript;
/*     */   }
/*     */   
/*     */   public void setPreScript(String preScript) {
/* 126 */     this.preScript = preScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreScript() {
/* 135 */     return this.preScript;
/*     */   }
/*     */   
/*     */   public void setAfterScript(String afterScript) {
/* 139 */     this.afterScript = afterScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAfterScript() {
/* 148 */     return this.afterScript;
/*     */   }
/*     */   
/*     */   public String getShowScript() {
/* 152 */     return this.showScript;
/*     */   }
/*     */   
/*     */   public void setShowScript(String showScript) {
/* 156 */     this.showScript = showScript;
/*     */   }
/*     */   
/*     */   public void setIsTreeList(Short isTreeList) {
/* 160 */     this.isTreeList = isTreeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Short getIsTreeList() {
/* 169 */     return this.isTreeList;
/*     */   }
/*     */   
/*     */   public void setTreeConf(String treeConf) {
/* 173 */     this.treeConf = treeConf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTreeConf() {
/* 182 */     return this.treeConf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 189 */     return (new ToStringBuilder(this))
/* 190 */       .append("id", this.id)
/* 191 */       .append("formKey", this.formKey)
/* 192 */       .append("jsPreScript", this.jsPreScript)
/* 193 */       .append("jsAfterScript", this.jsAfterScript)
/* 194 */       .append("preScript", this.preScript)
/* 195 */       .append("afterScript", this.afterScript)
/* 196 */       .append("isTreeList", this.isTreeList)
/* 197 */       .append("treeConf", this.treeConf)
/* 198 */       .toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormBusSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */