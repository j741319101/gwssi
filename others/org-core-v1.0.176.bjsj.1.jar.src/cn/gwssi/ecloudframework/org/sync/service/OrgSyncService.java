/*     */ package com.dstz.org.sync.service;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.RestTemplateUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.core.dao.GroupDao;
/*     */ import com.dstz.org.core.manager.GroupManager;
/*     */ import com.dstz.org.core.manager.OrgRelationManager;
/*     */ import com.dstz.org.core.manager.UserManager;
/*     */ import com.dstz.org.core.model.Group;
/*     */ import com.dstz.org.core.model.OrgRelation;
/*     */ import com.dstz.org.core.model.User;
/*     */ import com.dstz.org.sync.orm.GroupJsonOrm;
/*     */ import com.dstz.org.sync.orm.UserGroupRelJsonOrm;
/*     */ import com.dstz.org.sync.orm.UserJsonOrm;
/*     */ import com.dstz.sys.util.SysPropertyUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ @Service
/*     */ public class OrgSyncService
/*     */ {
/*  31 */   private static Logger LOG = LoggerFactory.getLogger(RestTemplateUtil.class);
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private GroupManager groupMananger;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   GroupDao groupDao;
/*     */   
/*     */   @Resource
/*     */   private UserManager userMananger;
/*     */   
/*     */   @Resource
/*     */   private OrgRelationManager relationManager;
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public void fullSyncOrg() {
/*  50 */     LOG.info("=============== 开始同步组织用户！===============");
/*     */     
/*  52 */     syncFullUser();
/*     */ 
/*     */     
/*  55 */     syncFullOrg();
/*     */ 
/*     */     
/*  58 */     syncFullRelation();
/*     */     
/*  60 */     LOG.info("============同步组织用户成功！===============");
/*     */   }
/*     */ 
/*     */   
/*     */   private void syncFullUser() {
/*  65 */     List<UserJsonOrm> userList = getRemoteData("user_full_sync_url", UserJsonOrm.class, "users");
/*     */ 
/*     */     
/*  68 */     this.userMananger.removeOutSystemUser();
/*     */ 
/*     */     
/*  71 */     userList.forEach(userOrm -> {
/*     */           User user = (User)BeanCopierUtils.transformBean(userOrm, User.class);
/*     */           this.userMananger.create(user);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void syncFullOrg() {
/*  79 */     List<GroupJsonOrm> orgList = getRemoteData("group_full_sync_url", GroupJsonOrm.class, "orgList");
/*     */ 
/*     */     
/*  82 */     this.groupMananger.removeAll();
/*     */ 
/*     */     
/*  85 */     orgList.forEach(group -> {
/*     */           Group g = (Group)BeanCopierUtils.transformBean(group, Group.class);
/*     */           this.groupDao.create(g);
/*     */         });
/*     */   }
/*     */   
/*     */   private void syncFullRelation() {
/*  92 */     List<UserGroupRelJsonOrm> relationList = getRemoteData("relation_full_sync_url", UserGroupRelJsonOrm.class, "relation");
/*     */ 
/*     */     
/*  95 */     this.relationManager.removeAllRelation("groupUser");
/*     */ 
/*     */     
/*  98 */     relationList.forEach(rel -> {
/*     */           OrgRelation relation = (OrgRelation)BeanCopierUtils.transformBean(rel, OrgRelation.class);
/*     */           this.relationManager.create(relation);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> List<T> getRemoteData(String urlPropertyAlias, Class<T> dataType, String jsonKey) {
/* 107 */     String url = SysPropertyUtil.getByAlias(urlPropertyAlias);
/* 108 */     if (StringUtil.isEmpty(url)) {
/* 109 */       LOG.warn("同步失败，同步URL未配置：" + urlPropertyAlias);
/* 110 */       throw new BusinessException("同步失败");
/*     */     } 
/*     */ 
/*     */     
/* 114 */     JSONObject json = null;
/*     */     
/* 116 */     if (!json.containsKey("success") || !json.containsKey("data")) {
/* 117 */       LOG.warn("同步失败：" + json);
/* 118 */       throw new BusinessException("同步失败");
/*     */     } 
/*     */     
/* 121 */     JSONArray jsonArray = json.getJSONObject("data").getJSONArray(jsonKey);
/* 122 */     List<T> list = JSON.parseArray(jsonArray.toJSONString(), dataType);
/*     */ 
/*     */     
/* 125 */     return list;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/sync/service/OrgSyncService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */