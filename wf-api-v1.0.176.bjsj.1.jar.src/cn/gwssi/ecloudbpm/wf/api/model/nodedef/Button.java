/*     */ package cn.gwssi.ecloudbpm.wf.api.model.nodedef;
/*     */ 
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ApiModel(description = "流程节点按钮定义")
/*     */ public class Button
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public Button() {}
/*     */   
/*     */   public Button(String name, String alias) {
/*  21 */     this.name = name;
/*  22 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */   
/*     */   public Button(String name, String alias, String groovyScript, String configPage) {
/*  27 */     this.name = name;
/*  28 */     this.alias = alias;
/*  29 */     this.groovyScript = groovyScript;
/*  30 */     this.configPage = configPage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮名字")
/*  36 */   protected String name = "";
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮标识")
/*  41 */   protected String alias = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮提交前置js脚本")
/*  47 */   protected String beforeScript = "";
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮提交后置js脚本")
/*  52 */   protected String afterScript = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮后台权限 groovy脚本")
/*  58 */   protected String groovyScript = "";
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("按钮对应控制器后端处理器更多配置信息页")
/*  63 */   protected String configPage = "";
/*     */ 
/*     */   
/*     */   public String getName() {
/*  67 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  71 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getAlias() {
/*  75 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  79 */     this.alias = alias;
/*     */   }
/*     */   
/*     */   public String getBeforeScript() {
/*  83 */     return this.beforeScript;
/*     */   }
/*     */   
/*     */   public void setBeforeScript(String beforeScript) {
/*  87 */     this.beforeScript = beforeScript;
/*     */   }
/*     */   
/*     */   public String getAfterScript() {
/*  91 */     return this.afterScript;
/*     */   }
/*     */   
/*     */   public void setAfterScript(String afterScript) {
/*  95 */     this.afterScript = afterScript;
/*     */   }
/*     */   
/*     */   public String getGroovyScript() {
/*  99 */     return this.groovyScript;
/*     */   }
/*     */   
/*     */   public void setGroovyScript(String groovyScript) {
/* 103 */     this.groovyScript = groovyScript;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 108 */     return this.configPage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConfigPage(String configPage) {
/* 113 */     this.configPage = configPage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 118 */     if (this == o) {
/* 119 */       return true;
/*     */     }
/* 121 */     if (o == null || getClass() != o.getClass()) {
/* 122 */       return false;
/*     */     }
/* 124 */     Button button = (Button)o;
/* 125 */     return Objects.equals(this.alias, button.alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     return Objects.hash(new Object[] { this.alias });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return "[name=" + this.name + ", alias=" + this.alias + "]";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */