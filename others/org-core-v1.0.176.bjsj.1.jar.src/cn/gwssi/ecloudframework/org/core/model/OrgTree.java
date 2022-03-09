/*     */ package cn.gwssi.ecloudframework.org.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.Tree;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class OrgTree
/*     */   extends Group
/*     */   implements Tree<OrgTree>
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   public static final String ICON_COMORG = "/styles/theme/default/images/icons/u_darkblue/u_zzgl_darkblue.png";
/*     */   protected String icon;
/*     */   protected boolean nocheck = false;
/*     */   protected boolean chkDisabled = false;
/*     */   protected boolean click = true;
/*  27 */   protected String title = "";
/*     */   
/*     */   protected List<OrgTree> children;
/*     */ 
/*     */   
/*     */   public OrgTree() {}
/*     */   
/*     */   public OrgTree(String name, String id, String parentId, String icon) {
/*  35 */     setName(name);
/*  36 */     this.parentId = parentId;
/*  37 */     this.id = id;
/*  38 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<OrgTree> GroupList2TreeList(List<Group> groupList, String icon) {
/*  46 */     if (groupList == null || groupList.size() == 0) {
/*  47 */       return Collections.emptyList();
/*     */     }
/*     */     
/*  50 */     List<OrgTree> groupTreeList = new ArrayList<>();
/*  51 */     for (Group group : groupList) {
/*  52 */       OrgTree grouptree = new OrgTree(group);
/*  53 */       grouptree.setIcon(icon);
/*  54 */       groupTreeList.add(grouptree);
/*     */     } 
/*  56 */     return groupTreeList;
/*     */   }
/*     */   
/*     */   public OrgTree(Group group) {
/*  60 */     this.id = group.getId();
/*  61 */     this.name = group.name;
/*  62 */     this.code = group.code;
/*  63 */     this.sn = group.sn;
/*  64 */     this.parentId = group.parentId;
/*  65 */     this.path = group.path;
/*  66 */     this.type = group.type;
/*  67 */     this.desc = group.desc;
/*  68 */     if (!this.name.contains("style=")) {
/*  69 */       this.title = this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  75 */     this.name = name;
/*     */     
/*  77 */     if ("".equals(this.title) && !this.name.contains("style=")) {
/*  78 */       this.title = name;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTitle() {
/*  84 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/*  88 */     this.title = title;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*  93 */     return this.icon;
/*     */   }
/*     */   
/*     */   public void setIcon(String icon) {
/*  97 */     this.icon = icon;
/*     */   }
/*     */   
/*     */   public boolean isNocheck() {
/* 101 */     return this.nocheck;
/*     */   }
/*     */   
/*     */   public void setNocheck(boolean nocheck) {
/* 105 */     this.nocheck = nocheck;
/*     */   }
/*     */   
/*     */   public boolean isChkDisabled() {
/* 109 */     return this.chkDisabled;
/*     */   }
/*     */   
/*     */   public boolean isClick() {
/* 113 */     return this.click;
/*     */   }
/*     */   
/*     */   public void setClick(boolean click) {
/* 117 */     this.click = click;
/*     */   }
/*     */   
/*     */   public void setChkDisabled(boolean chkDisabled) {
/* 121 */     this.chkDisabled = chkDisabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<OrgTree> getChildren() {
/* 126 */     return this.children;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChildren(List<OrgTree> list) {
/* 131 */     this.children = list;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/model/OrgTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */