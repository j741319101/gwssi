/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormDefData;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
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
/*     */ @ApiModel(description = "表单定义的页面数据类")
/*     */ public class FormDefData
/*     */   implements IFormDefData
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   @ApiModelProperty("表单的业务数据 JSON")
/*     */   private JSONObject data;
/*     */   @ApiModelProperty("表单的初始化数据")
/*     */   private JSONObject initData;
/*     */   @ApiModelProperty("表单字段的权限")
/*     */   private JSONObject permission;
/*     */   @ApiModelProperty("表单的表权限")
/*     */   private JSONObject tablePermission;
/*     */   @ApiModelProperty("表单的HTML 源码")
/*     */   private String html;
/*     */   
/*     */   public JSONObject getData() {
/* 104 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(JSONObject data) {
/* 108 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getInitData() {
/* 113 */     return this.initData;
/*     */   }
/*     */   
/*     */   public void setInitData(JSONObject initData) {
/* 117 */     this.initData = initData;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getPermission() {
/* 122 */     return this.permission;
/*     */   }
/*     */   
/*     */   public void setPermission(JSONObject permission) {
/* 126 */     this.permission = permission;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getTablePermission() {
/* 131 */     return this.tablePermission;
/*     */   }
/*     */   
/*     */   public void setTablePermission(JSONObject tablePermission) {
/* 135 */     this.tablePermission = tablePermission;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHtml() {
/* 140 */     return this.html;
/*     */   }
/*     */   
/*     */   public void setHtml(String html) {
/* 144 */     this.html = html;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormDefData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */