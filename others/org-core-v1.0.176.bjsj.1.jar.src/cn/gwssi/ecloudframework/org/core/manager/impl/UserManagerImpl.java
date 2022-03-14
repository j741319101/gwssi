/*     */ package com.dstz.org.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.constant.EventEnum;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.event.CommonEvent;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.sm3.SM3Util;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.constant.RelationTypeConstant;
/*     */ import com.dstz.org.api.constant.UserTypeConstant;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.core.dao.UserDao;
/*     */ import com.dstz.org.core.manager.GroupManager;
/*     */ import com.dstz.org.core.manager.OrgRelationManager;
/*     */ import com.dstz.org.core.manager.UserManager;
/*     */ import com.dstz.org.core.model.OrgRelation;
/*     */ import com.dstz.org.core.model.User;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class UserManagerImpl
/*     */   extends BaseManager<String, User>
/*     */   implements UserManager
/*     */ {
/*     */   public static final String LOGIN_USER_CACHE_KEY = "ecloudframework:loginUser:";
/*     */   @Resource
/*     */   UserDao userDao;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationMananger;
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */   @Autowired
/*     */   ICache icache;
/*     */   @Autowired
/*     */   ICurrentContext currentContext;
/*     */   
/*     */   public User getByAccount(String account) {
/*  53 */     User user = this.userDao.getByAccount(account);
/*  54 */     if (user != null) {
/*  55 */       user.setOrgRelationList(this.orgRelationMananger.getUserRelation(user.getUserId(), null));
/*     */     }
/*     */     
/*  58 */     return user;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUserExist(User user) {
/*  63 */     return (this.userDao.isUserExist(user).intValue() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<User> getUserListByRelation(String relId, String type) {
/*  68 */     return this.userDao.getUserListByRelation(relId, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User get(String entityId) {
/*  76 */     User user = (User)super.get(entityId);
/*  77 */     if (user != null) {
/*  78 */       user.setOrgRelationList(this.orgRelationMananger.getUserRelation(entityId, null));
/*     */     }
/*     */     
/*  81 */     return user;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String entityId) {
/*  86 */     this.orgRelationMananger.removeByUserId(entityId, null);
/*  87 */     super.remove(entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveUserInfo(User user) {
/*  92 */     List<OrgRelation> orgRelationList = user.getOrgRelationList();
/*  93 */     if (StringUtil.isEmpty(user.getId())) {
/*  94 */       String account = user.getAccount();
/*  95 */       String email = user.getEmail();
/*  96 */       String phone = user.getMobile();
/*  97 */       if (StringUtils.isNotEmpty(account) && null != getByAccount(account)) {
/*  98 */         throw new BusinessMessage(String.format("账号:%s已存在", new Object[] { account }));
/*     */       }
/*     */       
/* 101 */       if (StringUtil.isEmpty(user.getPassword())) {
/* 102 */         user.setPassword("111111");
/*     */       }
/* 104 */       user.setPassword(SM3Util.SM3EncodePws(user.getPassword()));
/* 105 */       user.setStatus(Integer.valueOf(0));
/* 106 */       user.setActiveStatus(Integer.valueOf(0));
/*     */       
/* 108 */       if (StringUtils.isEmpty(user.getType())) {
/* 109 */         user.setType(UserTypeConstant.NORMAL.key());
/*     */       }
/* 111 */       if (UserTypeConstant.MANAGER.key().equals(user.getType())) {
/* 112 */         user.setStatus(Integer.valueOf(1));
/* 113 */         user.setActiveStatus(Integer.valueOf(1));
/*     */       } 
/*     */       
/* 116 */       if (null == user.getSn()) {
/* 117 */         user.setSn(Integer.valueOf(0));
/*     */       }
/* 119 */       create(user);
/*     */     } else {
/*     */       
/* 122 */       user.setAccount(null);
/* 123 */       if (StringUtil.isNotEmpty(user.getPassword())) {
/* 124 */         user.setPassword(SM3Util.SM3EncodePws(user.getPassword()));
/*     */       }
/* 126 */       user.setUpdateBy(this.currentContext.getCurrentUserId());
/* 127 */       user.setUpdateTime(new Date());
/* 128 */       updateByPrimaryKeySelective(user);
/* 129 */       if (!CollectionUtil.isEmpty(orgRelationList)) {
/*     */         
/* 131 */         List<String> relationTypes = new ArrayList<>();
/* 132 */         relationTypes.add(RelationTypeConstant.GROUP_USER.getKey());
/* 133 */         this.orgRelationMananger.removeByUserId(user.getId(), relationTypes);
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (!CollectionUtil.isEmpty(orgRelationList)) {
/* 138 */       orgRelationList.forEach(rel -> {
/*     */             if (RelationTypeConstant.GROUP_USER.getKey().equals(rel.getType())) {
/*     */               rel.setUserId(user.getId());
/*     */               this.orgRelationMananger.create(rel);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(User entity) {
/* 150 */     AppUtil.publishEvent((ApplicationEvent)new CommonEvent(EventEnum.ADD_USER));
/* 151 */     this.dao.create(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<User> getUserListByGroupPath(String path) {
/* 156 */     return this.userDao.getUserListByGroupPath(path + "%");
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeOutSystemUser() {
/* 161 */     this.userDao.removeOutSystemUser();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public User getByOpneid(String openid) {
/* 167 */     return this.userDao.getByOpenid(openid);
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateByPrimaryKeySelective(User record) {
/* 172 */     User entity = get(record.getId());
/* 173 */     clearUserCache(entity);
/* 174 */     return this.userDao.updateByPrimaryKeySelective(record);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIdByAccount(String account) {
/* 179 */     return this.userDao.getIdByAccount(account);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(User entity) {
/* 185 */     super.update((Serializable)entity);
/* 186 */     clearUserCache(entity);
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
/*     */   public Integer getAllEnableUserNum() {
/* 198 */     return this.userDao.getAllEnableUserNum();
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
/*     */   public List<User> getUsersByOrgPath(String orgPath) {
/* 211 */     if (StringUtils.isNotEmpty(orgPath)) {
/* 212 */       orgPath = orgPath.concat("%");
/*     */     }
/* 214 */     return this.userDao.getUsersByOrgPath(orgPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearUserCache(User entity) {
/* 221 */     this.icache.delByKey("ecloudframework:loginUser:".concat(entity.getAccount()));
/* 222 */     this.icache.delByKey("current_org".concat(entity.getId()));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/UserManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */