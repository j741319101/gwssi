/*    */ package com.dstz.sys.core.model;
/*    */ 
/*    */ import com.dstz.base.core.model.BaseModel;
/*    */ import com.dstz.base.core.util.JsonUtil;
/*    */ import com.dstz.sys.core.model.def.SysDataSourceDefAttribute;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SysDataSourceDef
/*    */   extends BaseModel
/*    */ {
/*    */   @NotEmpty
/*    */   private String name;
/*    */   @NotEmpty
/*    */   private String classPath;
/*    */   @NotEmpty
/*    */   private String attributesJson;
/*    */   @NotNull
/*    */   @Valid
/*    */   private List<SysDataSourceDefAttribute> attributes;
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 52 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getClassPath() {
/* 56 */     return this.classPath;
/*    */   }
/*    */   
/*    */   public void setClassPath(String classPath) {
/* 60 */     this.classPath = classPath;
/*    */   }
/*    */   
/*    */   public String getAttributesJson() {
/* 64 */     return this.attributesJson;
/*    */   }
/*    */   
/*    */   public void setAttributesJson(String attributesJson) {
/* 68 */     this.attributesJson = attributesJson;
/* 69 */     this.attributes = JsonUtil.parseArray(attributesJson, SysDataSourceDefAttribute.class);
/*    */   }
/*    */   
/*    */   public List<SysDataSourceDefAttribute> getAttributes() {
/* 73 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public void setAttributes(List<SysDataSourceDefAttribute> attributes) {
/* 77 */     this.attributes = attributes;
/* 78 */     this.attributesJson = JsonUtil.toJSONString(attributes);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysDataSourceDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */