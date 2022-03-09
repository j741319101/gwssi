/*     */ package cn.gwssi.ecloudframework.sys.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.ISysDataSource;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.def.SysDataSourceDefAttribute;
/*     */ import java.util.List;
/*     */ import javax.validation.Valid;
/*     */ import javax.validation.constraints.NotNull;
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
/*     */ public class SysDataSource
/*     */   extends BaseModel
/*     */   implements ISysDataSource
/*     */ {
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   @NotEmpty
/*     */   private String dbType;
/*     */   @NotEmpty
/*     */   private String classPath;
/*     */   @NotEmpty
/*     */   private String attributesJson;
/*     */   @NotNull
/*     */   @Valid
/*     */   private List<SysDataSourceDefAttribute> attributes;
/*     */   
/*     */   public String getKey() {
/*  66 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  70 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  79 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDesc() {
/*  84 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/*  88 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDbType() {
/*  93 */     return this.dbType;
/*     */   }
/*     */   
/*     */   public void setDbType(String dbType) {
/*  97 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public String getClassPath() {
/* 101 */     return this.classPath;
/*     */   }
/*     */   
/*     */   public void setClassPath(String classPath) {
/* 105 */     this.classPath = classPath;
/*     */   }
/*     */   
/*     */   public String getAttributesJson() {
/* 109 */     return this.attributesJson;
/*     */   }
/*     */   
/*     */   public void setAttributesJson(String attributesJson) {
/* 113 */     this.attributesJson = attributesJson;
/* 114 */     this.attributes = JsonUtil.parseArray(attributesJson, SysDataSourceDefAttribute.class);
/*     */   }
/*     */   
/*     */   public List<SysDataSourceDefAttribute> getAttributes() {
/* 118 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public void setAttributes(List<SysDataSourceDefAttribute> attributes) {
/* 122 */     this.attributes = attributes;
/* 123 */     this.attributesJson = JsonUtil.toJSONString(attributes);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */