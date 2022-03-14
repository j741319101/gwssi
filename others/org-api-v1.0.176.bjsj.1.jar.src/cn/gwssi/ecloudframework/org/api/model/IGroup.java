/*     */ package com.dstz.org.api.model;
/*     */ 
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.io.Serializable;
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
/*     */ @ApiModel(description = "组定义，含org,role,post 组类型详见 GroupTypeConstant.java")
/*     */ public interface IGroup
/*     */   extends Serializable
/*     */ {
/*     */   @ApiModelProperty("组ID")
/*     */   String getGroupId();
/*     */   
/*     */   @ApiModelProperty("组名字")
/*     */   String getGroupName();
/*     */   
/*     */   @ApiModelProperty("组CODE")
/*     */   String getGroupCode();
/*     */   
/*     */   @ApiModelProperty("组类型：org,role,post")
/*     */   String getGroupType();
/*     */   
/*     */   @ApiModelProperty("组级别")
/*     */   Integer getGroupLevel();
/*     */   
/*     */   @ApiModelProperty("树形组 parentId")
/*     */   String getParentId();
/*     */   
/*     */   @ApiModelProperty("排序sn")
/*     */   Integer getSn();
/*     */   
/*     */   @ApiModelProperty("路径")
/*     */   String getPath();
/*     */   
/*     */   @ApiModelProperty("用户人数")
/*     */   Integer getUserNum();
/*     */   
/*     */   @ApiModelProperty("简称")
/*     */   String getSimple();
/*     */   
/*     */   @ApiModelProperty("父级名称")
/*     */   default String getParentName() {
/* 106 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/IGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */