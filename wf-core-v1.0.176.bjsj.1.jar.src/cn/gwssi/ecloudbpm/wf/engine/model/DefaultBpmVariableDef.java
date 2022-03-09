/*     */ package cn.gwssi.ecloudbpm.wf.engine.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmVariableDef;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultBpmVariableDef
/*     */   implements BpmVariableDef
/*     */ {
/*  13 */   private String nodeId = "";
/*     */   
/*  15 */   private String name = "";
/*     */   
/*  17 */   private String key = "";
/*     */   
/*  19 */   private String dataType = "string";
/*     */   
/*  21 */   private Object defaultVal = "";
/*     */   
/*     */   private boolean isRequired = false;
/*     */   
/*  25 */   private String description = "";
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
/*     */   public static Object getValue(String dataType, String value) {
/*  40 */     if ("double".equals(dataType))
/*  41 */       return new Double(value); 
/*  42 */     if ("float".equals(dataType))
/*  43 */       return new Float(value); 
/*  44 */     if ("int".equals(dataType)) {
/*  45 */       if (value == null || StringUtil.isEmpty(value)) {
/*  46 */         return Integer.valueOf(0);
/*     */       }
/*  48 */       return new Integer(value);
/*  49 */     }  if ("date".equals(dataType)) {
/*  50 */       return DateUtil.parse(value);
/*     */     }
/*     */     
/*  53 */     return value;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  61 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  65 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String varKey) {
/*  69 */     this.key = varKey;
/*     */   }
/*     */   
/*     */   public String getDataType() {
/*  73 */     return this.dataType;
/*     */   }
/*     */   
/*     */   public void setDataType(String dataType) {
/*  77 */     this.dataType = dataType;
/*     */   }
/*     */   
/*     */   public Object getDefaultVal() {
/*  81 */     return this.defaultVal;
/*     */   }
/*     */   
/*     */   public void setDefaultVal(Object defaultVal) {
/*  85 */     this.defaultVal = defaultVal;
/*     */   }
/*     */   
/*     */   public void setDefaultVal(String defaulVal2) {
/*  89 */     this.defaultVal = getValue(this.dataType, defaulVal2);
/*     */   }
/*     */   
/*     */   public boolean getIsRequired() {
/*  93 */     return this.isRequired;
/*     */   }
/*     */   
/*     */   public void setIsRequired(boolean isRequired) {
/*  97 */     this.isRequired = isRequired;
/*     */   }
/*     */   public boolean isRequired() {
/* 100 */     return this.isRequired;
/*     */   }
/*     */   
/*     */   public void setRequired(boolean isRequired) {
/* 104 */     this.isRequired = isRequired;
/*     */   }
/*     */   public String getDescription() {
/* 107 */     return (this.description == null) ? "" : this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 111 */     this.description = description;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 116 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 121 */     this.nodeId = nodeId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/model/DefaultBpmVariableDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */