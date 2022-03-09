/*     */ package cn.gwssi.ecloudframework.org.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.core.dao.PostDao;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.OrgRelationManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.PostManager;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Post;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service
/*     */ public class PostManagerImpl
/*     */   extends BaseManager<String, Post>
/*     */   implements PostManager
/*     */ {
/*     */   @Resource
/*     */   PostDao postDao;
/*     */   @Autowired
/*     */   OrgRelationManager orgRelationMananger;
/*     */   
/*     */   public void create(Post entity) {
/*  47 */     if (this.postDao.getByCode(entity.getCode(), null) != null) {
/*  48 */       throw new BusinessMessage("岗位编码“" + entity.getCode() + "”已存在，请修改！");
/*     */     }
/*  50 */     entity.setId(IdUtil.getSuid());
/*  51 */     super.create((Serializable)entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Post entity) {
/*  61 */     if (this.postDao.getByCode(entity.getCode(), entity.getId()) != null) {
/*  62 */       throw new BusinessMessage("岗位编码“" + entity.getCode() + "”已存在，请修改！");
/*     */     }
/*  64 */     super.update((Serializable)entity);
/*     */   }
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
/*     */   public Post getByAlias(String alias) {
/*  77 */     return this.postDao.getByCode(alias, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String id) {
/*  89 */     this.orgRelationMananger.removeCheck(id);
/*  90 */     super.remove(id);
/*     */   }
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
/*     */   public List<Post> getByUserId(String userId) {
/* 103 */     if (StringUtil.isEmpty(userId)) {
/* 104 */       return Collections.emptyList();
/*     */     }
/* 106 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 107 */     Map<String, Object> params = new HashMap<>();
/* 108 */     params.put("userId", userId);
/* 109 */     defaultQueryFilter.addParams(params);
/* 110 */     return this.postDao.query((QueryFilter)defaultQueryFilter);
/*     */   }
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
/*     */   public Post getMasterByUserId(String userId) {
/* 123 */     if (StringUtil.isEmpty(userId)) {
/* 124 */       return null;
/*     */     }
/* 126 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 127 */     Map<String, Object> params = new HashMap<>();
/* 128 */     params.put("userId", userId);
/* 129 */     params.put("isMaster", "1");
/* 130 */     defaultQueryFilter.addFilter("trelation.is_master_", Integer.valueOf(1), QueryOP.EQUAL);
/* 131 */     defaultQueryFilter.addParams(params);
/* 132 */     List<Post> lstPost = this.postDao.query((QueryFilter)defaultQueryFilter);
/* 133 */     if (null != lstPost && lstPost.size() > 0) {
/* 134 */       return lstPost.get(0);
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/PostManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */