/*     */ package com.dstz.org.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.core.dao.GroupDao;
/*     */ import com.dstz.org.core.dao.UserDao;
/*     */ import com.dstz.org.core.manager.GroupManager;
/*     */ import com.dstz.org.core.manager.OrgRelationManager;
/*     */ import com.dstz.org.core.model.Group;
/*     */ import com.dstz.org.core.model.User;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class GroupManagerImpl
/*     */   extends BaseManager<String, Group>
/*     */   implements GroupManager
/*     */ {
/*  45 */   protected final Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   GroupDao groupDao;
/*     */   
/*     */   @Resource
/*     */   UserDao userDao;
/*     */   @Autowired
/*     */   OrgRelationManager orgRelationMananger;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   public Group getByCode(String code) {
/*  58 */     return this.groupDao.getByCode(code, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String id) {
/*  63 */     this.orgRelationMananger.removeCheck(id);
/*  64 */     Group group = (Group)this.groupDao.get(id);
/*  65 */     List<Group> childList = this.groupDao.getChildByPath(group.getPath() + "%");
/*     */ 
/*     */     
/*  68 */     childList.forEach(g -> {
/*     */           this.orgRelationMananger.removeCheck(g.getId());
/*     */           
/*     */           super.remove(g.getId());
/*     */         });
/*  73 */     super.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Group get(String entityId) {
/*  78 */     Group group = (Group)super.get(entityId);
/*  79 */     return group;
/*     */   }
/*     */ 
/*     */   
/*     */   public void create(Group entity) {
/*  84 */     if (this.groupDao.getByCode(entity.getCode(), null) != null) {
/*  85 */       throw new BusinessMessage("组织编码“" + entity.getCode() + "”已存在，请修改！");
/*     */     }
/*     */     
/*  88 */     if (null == entity.getSn()) {
/*  89 */       entity.setSn(Integer.valueOf(0));
/*     */     }
/*  91 */     entity.setId(IdUtil.getSuid());
/*  92 */     setPath(entity);
/*     */     
/*  94 */     super.create((Serializable)entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Group entity) {
/*  99 */     if (this.groupDao.getByCode(entity.getCode(), entity.getId()) != null) {
/* 100 */       throw new BusinessMessage("组织编码“" + entity.getCode() + "”已存在，请修改！");
/*     */     }
/* 102 */     setPath(entity);
/* 103 */     super.update((Serializable)entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setPath(Group entity) {
/* 112 */     entity.setPath(entity.getId());
/* 113 */     if (StringUtil.isNotZeroEmpty(entity.getParentId())) {
/* 114 */       Group parent = (Group)this.groupDao.get(entity.getParentId());
/* 115 */       if (parent != null && parent.getPath() != null) {
/* 116 */         entity.setPath(parent.getPath().concat(".").concat(entity.getId()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Group> getByUserId(String userId) {
/* 123 */     return this.groupDao.getByUserId(userId);
/*     */   }
/*     */   
/*     */   public List<Group> getByUserAccount(String account) {
/* 127 */     User user = this.userDao.getByAccount(account);
/* 128 */     return this.groupDao.getByUserId(user.getId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Group getMainGroup(String userId) {
/* 134 */     List<Group> groups = this.groupDao.getByUserId(userId);
/* 135 */     if (CollectionUtil.isEmpty(groups)) {
/* 136 */       return null;
/*     */     }
/* 138 */     if (null != userId && userId.equals(this.iCurrentContext.getCurrentUserId())) {
/*     */       
/* 140 */       ICache<IGroup> iCache = (ICache<IGroup>)AppUtil.getBean(ICache.class);
/* 141 */       String userKey = "current_org".concat(userId);
/*     */       
/* 143 */       IGroup currentGroup = (IGroup)iCache.getByKey(userKey);
/* 144 */       if (null != currentGroup) {
/* 145 */         return (Group)BeanCopierUtils.transformBean(currentGroup, Group.class);
/*     */       }
/*     */     } 
/* 148 */     return groups.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll() {
/* 153 */     this.groupDao.removeAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void chageOrder(List<Group> groups) {
/* 163 */     groups.forEach(group -> {
/*     */           if (StringUtils.isEmpty(group.getId())) {
/*     */             throw new BusinessMessage("ID不能为空");
/*     */           }
/*     */           this.groupDao.chageOrder(group);
/*     */         });
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
/*     */ 
/*     */   
/*     */   public List<Group> queryAllGroup() {
/* 183 */     String groupId = ContextUtil.getCurrentGroupId();
/* 184 */     Group group = getParentGroup(groupId);
/*     */     
/* 186 */     List<Group> groupList = Lists.newArrayList();
/* 187 */     List<Group> groupChilds = Lists.newArrayList();
/* 188 */     groupChilds.add(group);
/* 189 */     return queryChildGroup(groupList, groupChilds);
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
/*     */   private List<Group> queryChildGroup(List<Group> groupList, List<Group> childGroups) {
/* 202 */     for (Group group : childGroups) {
/* 203 */       groupList.add(group);
/* 204 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 205 */       defaultQueryFilter.addFilter("torg.parent_id_", group.getId(), QueryOP.EQUAL);
/* 206 */       List<Group> groups = this.groupDao.query((QueryFilter)defaultQueryFilter);
/* 207 */       if (!groups.isEmpty()) {
/* 208 */         queryChildGroup(groupList, groups);
/*     */       }
/*     */     } 
/* 211 */     return groupList;
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
/*     */   private Group getParentGroup(String groupId) {
/* 223 */     Group group = (Group)this.groupDao.get(groupId);
/*     */     
/* 225 */     if ("0".equals(group.getParentId())) {
/* 226 */       return group;
/*     */     }
/* 228 */     return getParentGroup(group.getParentId());
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
/*     */   public List<Group> getCurrentManagerOrgIds(String userId) {
/* 241 */     List<Group> groupList = this.groupDao.query();
/* 242 */     return groupList;
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
/*     */   
/*     */   public String findOrgId(Map<String, String> mapOrg, String mainOrgPath) {
/* 256 */     String orgId = mapOrg.get(mainOrgPath);
/* 257 */     if (StringUtils.isEmpty(orgId)) {
/* 258 */       String[] arrOrgName = mainOrgPath.split(">");
/*     */       
/* 260 */       List<Map<String, String>> lstData = new ArrayList<>();
/*     */ 
/*     */       
/* 263 */       for (String orgName : arrOrgName) {
/* 264 */         DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 265 */         defaultQueryFilter.addFilter("name_", orgName, QueryOP.EQUAL);
/* 266 */         List<Group> lstGroup = query((QueryFilter)defaultQueryFilter);
/* 267 */         if (lstGroup.size() > 0) {
/* 268 */           Map<String, String> temp = new HashMap<>();
/* 269 */           for (Group group : lstGroup) {
/* 270 */             temp.put(group.getId(), group.getParentId());
/*     */           }
/* 272 */           lstData.add(temp);
/*     */         } else {
/* 274 */           throw new BusinessMessage("机构：" + orgName + "不存在");
/*     */         } 
/*     */       } 
/*     */       
/* 278 */       label43: for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)((Map)lstData.get(lstData.size() - 1)).entrySet()) {
/* 279 */         String key = entry.getKey();
/* 280 */         String parentId = entry.getValue();
/* 281 */         if (lstData.size() == 1 && "0".equals(parentId)) {
/* 282 */           orgId = key;
/*     */           break;
/*     */         } 
/* 285 */         for (int i = lstData.size() - 2; i >= 0; i--) {
/* 286 */           Map<String, String> temp = lstData.get(i);
/* 287 */           String parentIdTemp = temp.get(parentId);
/* 288 */           if (i == 0 && "0".equals(parentIdTemp)) {
/* 289 */             orgId = key;
/*     */             break label43;
/*     */           } 
/* 292 */           if (StringUtils.isNotEmpty(parentIdTemp)) {
/* 293 */             parentId = parentIdTemp;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 298 */       if (StringUtils.isEmpty(orgId)) {
/* 299 */         throw new BusinessMessage("机构：" + mainOrgPath + "不存在");
/*     */       }
/* 301 */       mapOrg.put(mainOrgPath, orgId);
/*     */     } 
/* 303 */     return orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateByPrimaryKeySelective(Group record) {
/* 308 */     return this.groupDao.updateByPrimaryKeySelective(record);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/GroupManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */