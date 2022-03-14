/*     */ package com.dstz.sys.core.model;
/*     */ 
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.sys.api.model.ISysTreeNode;
/*     */ import java.util.List;
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
/*     */ public class SysTreeNode
/*     */   extends BaseModel
/*     */   implements ISysTreeNode
/*     */ {
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   @NotEmpty
/*     */   private String icon;
/*     */   @NotEmpty
/*     */   private String treeId;
/*     */   private String parentId;
/*     */   private String path;
/*     */   private int sn;
/*     */   String appName;
/*     */   int count;
/*     */   private List<SysTreeNode> children;
/*     */   
/*     */   public String getIcon() {
/*  73 */     return this.icon;
/*     */   }
/*     */   
/*     */   public void setIcon(String icon) {
/*  77 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  82 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  86 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  95 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 100 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 104 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 109 */     return this.parentId;
/*     */   }
/*     */   
/*     */   public void setParentId(String parentId) {
/* 113 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 118 */     return this.path;
/*     */   }
/*     */   
/*     */   public void setPath(String path) {
/* 122 */     this.path = path;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 127 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(int sn) {
/* 131 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeId() {
/* 136 */     return this.treeId;
/*     */   }
/*     */   
/*     */   public void setTreeId(String treeId) {
/* 140 */     this.treeId = treeId;
/*     */   }
/*     */   
/*     */   public String getAppName() {
/* 144 */     return this.appName;
/*     */   }
/*     */   
/*     */   public void setAppName(String appName) {
/* 148 */     this.appName = appName;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 152 */     return this.count;
/*     */   }
/*     */   
/*     */   public void setCount(int count) {
/* 156 */     this.count = count;
/*     */   }
/*     */   
/*     */   public List<SysTreeNode> getChildren() {
/* 160 */     return this.children;
/*     */   }
/*     */   
/*     */   public void setChildren(List<SysTreeNode> children) {
/* 164 */     this.children = children;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/SysTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */