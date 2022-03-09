/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.Tree;
/*     */ import cn.gwssi.ecloudframework.sys.api.constant.SysTreeTypeConstants;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.ISysTreeNode;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ApiModel(description = "分类下流程任务数量VO")
/*     */ public class BpmTypeTreeCountVO
/*     */   implements Tree<BpmTypeTreeCountVO>
/*     */ {
/*     */   public static final String TYPE_FLOW = "flow";
/*     */   public static final String TYPE_TREE = "tree";
/*     */   @ApiModelProperty("TreeID/DefKey")
/*     */   private String Id;
/*     */   @ApiModelProperty("ParentId")
/*     */   private String parentId;
/*     */   @ApiModelProperty("类型：flow:流程，tree:分类")
/*  26 */   private String type = "flow";
/*     */ 
/*     */   
/*     */   @ApiModelProperty("数量")
/*     */   private Integer count;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("名字，分类名、流程名")
/*     */   private String name;
/*     */ 
/*     */   
/*     */   private String key;
/*     */ 
/*     */   
/*     */   private boolean expand = false;
/*     */   
/*     */   @ApiModelProperty("子节点")
/*     */   private List<BpmTypeTreeCountVO> children;
/*     */ 
/*     */   
/*     */   public BpmTypeTreeCountVO() {}
/*     */ 
/*     */   
/*     */   public BpmTypeTreeCountVO(ISysTreeNode treeNode) {
/*  50 */     this.Id = treeNode.getId();
/*  51 */     this.name = treeNode.getName();
/*  52 */     this.type = "tree";
/*  53 */     this.parentId = treeNode.getParentId();
/*  54 */     this.key = String.format("sysTree.%s.%s", new Object[] { SysTreeTypeConstants.FLOW.key(), treeNode.getKey() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  59 */     return this.Id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  63 */     this.Id = id;
/*     */   }
/*     */   
/*     */   public String getParentId() {
/*  67 */     return this.parentId;
/*     */   }
/*     */   
/*     */   public void setParentId(String parentId) {
/*  71 */     this.parentId = parentId;
/*     */   }
/*     */   
/*     */   public boolean isExpand() {
/*  75 */     return this.expand;
/*     */   }
/*     */   
/*     */   public void setExpand(boolean expand) {
/*  79 */     this.expand = expand;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/*  84 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  88 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Integer getCount() {
/*  92 */     return this.count;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  96 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 100 */     this.key = key;
/*     */   }
/*     */   
/*     */   public void setCount(Integer count) {
/* 104 */     this.count = count;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 108 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getCountName() {
/* 112 */     if (this.count == null || this.count.intValue() < 1) {
/* 113 */       return this.name;
/*     */     }
/*     */     
/* 116 */     return String.format("%s(%s)", new Object[] { this.name, this.count.toString() });
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 120 */     this.name = name;
/*     */   }
/*     */   
/*     */   public List<BpmTypeTreeCountVO> getChildren() {
/* 124 */     return this.children;
/*     */   }
/*     */   
/*     */   public void setChildren(List<BpmTypeTreeCountVO> children) {
/* 128 */     this.children = children;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmTypeTreeCountVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */