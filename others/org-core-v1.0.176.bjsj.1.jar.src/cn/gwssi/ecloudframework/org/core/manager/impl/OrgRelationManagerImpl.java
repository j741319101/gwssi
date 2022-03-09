/*     */ package cn.gwssi.ecloudframework.org.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.constant.RelationTypeConstant;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.core.dao.GroupDao;
/*     */ import cn.gwssi.ecloudframework.org.core.dao.OrgRelationDao;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.OrgRelationManager;
/*     */ import cn.gwssi.ecloudframework.org.core.model.OrgRelation;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.github.pagehelper.Page;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class OrgRelationManagerImpl
/*     */   extends BaseManager<String, OrgRelation>
/*     */   implements OrgRelationManager
/*     */ {
/*  39 */   protected final Logger log = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   OrgRelationDao orgRelationDao;
/*     */   @Resource
/*     */   GroupDao groupDao;
/*     */   
/*     */   public List<OrgRelation> getPostByUserId(String userId) {
/*  47 */     return this.orgRelationDao.getUserRelation(userId, RelationTypeConstant.POST_USER.getKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<OrgRelation> getUserRelation(String userId, String relationType) {
/*  52 */     return this.orgRelationDao.getUserRelation(userId, relationType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByUserId(String userId, List<String> relationTypes) {
/*  57 */     ContextUtil.clearCurrentUser();
/*  58 */     this.orgRelationDao.removeByUserId(userId, relationTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRelationByGroupId(String groupType, String groupId) {
/*  63 */     this.orgRelationDao.removeRelationByGroupId(groupType, groupId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateUserGroupRelationIsMaster(String groupId, String userId, String groupType) {
/*  68 */     RelationTypeConstant relationType = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
/*  69 */     if (StringUtils.isEmpty(groupId)) {
/*  70 */       throw new BusinessMessage("组织id不能为空");
/*     */     }
/*  72 */     if (StringUtils.isEmpty(userId)) {
/*  73 */       throw new BusinessMessage("用户id不能为空");
/*     */     }
/*  75 */     if (null == relationType) {
/*  76 */       throw new BusinessMessage("组织类型不正确");
/*     */     }
/*  78 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  79 */     defaultQueryFilter.addFilter("relation.type_", relationType.getKey(), QueryOP.EQUAL);
/*  80 */     defaultQueryFilter.addFilter("relation.user_id_", userId, QueryOP.EQUAL);
/*  81 */     defaultQueryFilter.addFilter("relation.group_id_", groupId, QueryOP.EQUAL);
/*  82 */     List<OrgRelation> lstOrgRelation = query((QueryFilter)defaultQueryFilter);
/*  83 */     if (lstOrgRelation.size() > 0) {
/*  84 */       OrgRelation orgRelation = lstOrgRelation.get(0);
/*  85 */       orgRelation.setIsMaster(Integer.valueOf(1));
/*  86 */       this.orgRelationDao.update(orgRelation);
/*  87 */       for (int i = 1; i < lstOrgRelation.size(); i++) {
/*  88 */         this.orgRelationDao.remove(((OrgRelation)lstOrgRelation.get(i)).getId());
/*     */       }
/*     */     } else {
/*  91 */       OrgRelation orgRelation = new OrgRelation();
/*  92 */       orgRelation.setIsMaster(Integer.valueOf(1));
/*  93 */       orgRelation.setType(relationType.getKey());
/*  94 */       orgRelation.setUserId(userId);
/*  95 */       orgRelation.setGroupId(groupId);
/*  96 */       this.orgRelationDao.create(orgRelation);
/*     */     } 
/*  98 */     defaultQueryFilter = new DefaultQueryFilter(true);
/*  99 */     defaultQueryFilter.addFilter("relation.type_", relationType.getKey(), QueryOP.EQUAL);
/* 100 */     defaultQueryFilter.addFilter("relation.user_id_", userId, QueryOP.EQUAL);
/* 101 */     defaultQueryFilter.addFilter("relation.is_master_", "1", QueryOP.EQUAL);
/* 102 */     defaultQueryFilter.addFilter("relation.group_id_", groupId, QueryOP.NOT_EQUAL);
/* 103 */     lstOrgRelation = query((QueryFilter)defaultQueryFilter);
/* 104 */     if (lstOrgRelation.size() > 0) {
/* 105 */       for (OrgRelation orgRelation : lstOrgRelation) {
/* 106 */         this.orgRelationDao.remove(orgRelation.getId());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeStatus(String id, int status) {
/* 113 */     OrgRelation relation = (OrgRelation)this.orgRelationDao.get(id);
/* 114 */     if (relation == null) {
/*     */       return;
/*     */     }
/*     */     
/* 118 */     relation.setStatus(Integer.valueOf(status));
/* 119 */     this.orgRelationDao.update(relation);
/*     */     
/* 121 */     String userId = relation.getUserId();
/* 122 */     if (StringUtil.isEmpty(userId)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 127 */     ICache<IGroup> iCache = (ICache<IGroup>)AppUtil.getBean(ICache.class);
/* 128 */     String userKey = "current_org" + relation.getUserId();
/* 129 */     if (iCache != null) {
/* 130 */       iCache.delByKey(userKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveUserGroupRelation(String groupId, String[] roleIds, String[] userIds) {
/* 136 */     for (String userId : userIds) {
/* 137 */       if (!StringUtil.isEmpty(userId)) {
/*     */ 
/*     */         
/* 140 */         OrgRelation orgRelation = new OrgRelation(groupId, userId, RelationTypeConstant.GROUP_USER.getKey());
/* 141 */         if (ArrayUtil.isNotEmpty((Object[])roleIds)) {
/* 142 */           for (String roleId : roleIds) {
/* 143 */             orgRelation.setRoleId(roleId);
/* 144 */             orgRelation.setId(null);
/* 145 */             orgRelation.setType(RelationTypeConstant.POST_USER.getKey());
/*     */             
/* 147 */             if (!checkRelationIsExist(orgRelation)) {
/* 148 */               create(orgRelation);
/*     */             } else {
/* 150 */               this.log.warn("关系重复添加，已跳过  {}", JSON.toJSONString(orgRelation));
/*     */             }
/*     */           
/*     */           }
/*     */         
/* 155 */         } else if (!checkRelationIsExist(orgRelation)) {
/* 156 */           create(orgRelation);
/*     */         } else {
/* 158 */           this.log.warn("关系重复添加，已跳过  {}", JSON.toJSONString(orgRelation));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkRelationIsExist(OrgRelation orgRelation) {
/* 165 */     int count = this.orgRelationDao.getCountByRelation(orgRelation);
/* 166 */     return (count > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int saveRoleUsers(String roleId, String[] userIds) {
/* 171 */     List<String> relationTypes = new ArrayList<>();
/* 172 */     List<OrgRelation> lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, "", "", roleId);
/* 173 */     Map<String, OrgRelation> mapOrgRelation = new HashMap<>();
/* 174 */     lstOrgRelation.forEach(orgRelation -> (OrgRelation)mapOrgRelation.put(orgRelation.getUserId() + "-" + orgRelation.getRoleId(), orgRelation));
/* 175 */     int i = 0;
/* 176 */     if (null != userIds)
/* 177 */       for (String userId : userIds) {
/* 178 */         OrgRelation orgRelation = new OrgRelation(roleId, userId, RelationTypeConstant.USER_ROLE.getKey());
/* 179 */         if (checkRelationIsExist(orgRelation)) {
/* 180 */           mapOrgRelation.remove(orgRelation.getUserId() + "-" + orgRelation.getRoleId());
/*     */         } else {
/*     */           
/* 183 */           i++;
/* 184 */           create(orgRelation);
/*     */         } 
/*     */       }  
/* 187 */     mapOrgRelation.values().forEach(orgRelation -> this.orgRelationDao.remove(orgRelation.getId()));
/*     */     
/* 189 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<OrgRelation> getUserRole(String userId) {
/* 194 */     return this.orgRelationDao.getUserRole(userId);
/*     */   }
/*     */ 
/*     */   
/*     */   public OrgRelation getPost(String id) {
/* 199 */     return this.orgRelationDao.getPost(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCheck(String groupId) {
/* 208 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*     */     
/* 210 */     if (StringUtil.isNotEmpty(groupId)) {
/* 211 */       defaultQueryFilter.addFilter("relation.group_id_", groupId, QueryOP.EQUAL);
/*     */     }
/*     */     
/* 214 */     Page<OrgRelation> relationList = (Page<OrgRelation>)query((QueryFilter)defaultQueryFilter);
/* 215 */     if (relationList.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 220 */     StringBuilder sb = new StringBuilder("请先移除以下关系：<br>");
/* 221 */     for (OrgRelation relation : relationList) {
/* 222 */       getRelationNotes(relation, sb);
/*     */     }
/* 224 */     if (relationList.getTotal() > 10L) {
/* 225 */       sb.append("......<br>");
/*     */     }
/*     */     
/* 228 */     sb.append(" 共[").append(relationList.getTotal()).append("]条，待移除关系");
/*     */     
/* 230 */     throw new BusinessMessage(sb.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private void getRelationNotes(OrgRelation relation, StringBuilder sb) {
/* 235 */     if (relation.getType().equals(RelationTypeConstant.POST_USER.getKey())) {
/* 236 */       sb.append("岗位 [").append(relation.getPostName()).append("] 下用户：").append(relation.getUserName());
/* 237 */     } else if (relation.getType().equals(RelationTypeConstant.GROUP_USER.getKey())) {
/* 238 */       sb.append("组织 [").append(relation.getGroupName()).append("] 下用户：").append(relation.getUserName());
/* 239 */     } else if (relation.getType().equals(RelationTypeConstant.USER_ROLE.getKey())) {
/* 240 */       sb.append("角色 [").append(relation.getRoleName()).append("] 下用户：").append(relation.getUserName());
/*     */     } 
/* 242 */     sb.append("<br>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllRelation(String relationType) {
/* 252 */     this.orgRelationDao.removeAllRelation(relationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void modifyUserOrg(List<OrgRelation> relations) {
/* 263 */     for (OrgRelation relation : relations) {
/* 264 */       List<String> relationTypes = new ArrayList<>();
/* 265 */       relationTypes.add(relation.getType());
/* 266 */       List<OrgRelation> lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, relation.getUserId(), relation.getOldGroupId(), relation.getRoleId());
/* 267 */       OrgRelation relationTemp = lstOrgRelation.stream().filter(orgRelation -> (1 == orgRelation.getIsMaster().intValue())).findFirst().orElse(null);
/*     */       
/* 269 */       if (null != relationTemp) {
/* 270 */         relation.setIsMaster(Integer.valueOf(1));
/*     */       }
/* 272 */       this.orgRelationDao.deleteRelationByUserIdAndType(relation.getUserId(), relation.getType(), relation.getOldGroupId());
/* 273 */       lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, relation.getUserId(), relation.getGroupId(), relation.getRoleId());
/*     */       
/* 275 */       if (lstOrgRelation.isEmpty()) {
/* 276 */         create(relation); continue;
/*     */       } 
/* 278 */       if (null != relationTemp) {
/*     */         
/* 280 */         relationTemp = lstOrgRelation.get(0);
/* 281 */         relationTemp.setIsMaster(Integer.valueOf(1));
/* 282 */         this.orgRelationDao.updateByPrimaryKeySelective(relationTemp);
/*     */       } 
/*     */     } 
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
/*     */   public void modifyAllUserGroup(String path, String oldGroupId, String newGroupId, String type) {
/* 299 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 300 */     if (StringUtils.isNotEmpty(path)) {
/* 301 */       defaultQueryFilter.addFilter("tgroup.path_", path, QueryOP.RIGHT_LIKE);
/*     */     } else {
/* 303 */       defaultQueryFilter.addFilter("relation.group_id_", oldGroupId, QueryOP.EQUAL);
/*     */     } 
/* 305 */     defaultQueryFilter.addFilter("relation.type_", type, QueryOP.IN);
/* 306 */     List<OrgRelation> lstOrgRelation = this.orgRelationDao.query((QueryFilter)defaultQueryFilter);
/* 307 */     for (OrgRelation orgRelation : lstOrgRelation) {
/*     */       
/* 309 */       orgRelation.setGroupId(newGroupId);
/* 310 */       List<String> relationTypes = new ArrayList<>();
/* 311 */       relationTypes.add(orgRelation.getType());
/* 312 */       List<OrgRelation> temp = this.orgRelationDao.getRelationsByParam(relationTypes, orgRelation.getUserId(), orgRelation.getGroupId(), orgRelation.getRoleId());
/* 313 */       if (temp == null || temp.isEmpty()) {
/* 314 */         this.orgRelationDao.updateGroupId(orgRelation); continue;
/*     */       } 
/* 316 */       this.orgRelationDao.remove(orgRelation.getId());
/* 317 */       if (1 == orgRelation.getIsMaster().intValue()) {
/* 318 */         OrgRelation orgRelationTemp = temp.get(0);
/* 319 */         orgRelationTemp.setIsMaster(Integer.valueOf(1));
/* 320 */         this.orgRelationDao.updateByPrimaryKeySelective(orgRelationTemp);
/*     */       } 
/*     */     } 
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
/*     */   public void updateGroupIdByUserId(List<OrgRelation> relations, String relationType) {
/* 334 */     relations.forEach(relation -> {
/*     */           relation.setType(relationType);
/*     */           this.orgRelationDao.updateGroupIdByUserId(relation);
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
/*     */   public void batchAdd(List<OrgRelation> relations) {
/* 349 */     relations.forEach(orgRelation -> {
/*     */           if (!checkRelationIsExist(orgRelation)) {
/*     */             create(orgRelation);
/*     */           }
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
/*     */   public void update(OrgRelation entity) {
/* 365 */     this.orgRelationDao.updateByPrimaryKeySelective(entity);
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
/*     */   public void batchRemove(List<OrgRelation> relations) {
/* 377 */     relations.forEach(orgRelation -> {
/*     */           if (StringUtils.isNotEmpty(orgRelation.getId())) {
/*     */             this.orgRelationDao.remove(orgRelation.getId());
/*     */           } else {
/*     */             this.orgRelationDao.deleteRelationByUserIdAndType(orgRelation.getUserId(), orgRelation.getType(), orgRelation.getGroupId());
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(OrgRelation entity) {
/* 393 */     if (StringUtils.isEmpty(entity.getGroupId())) {
/* 394 */       throw new BusinessMessage("添加组关系 组id不能为空");
/*     */     }
/* 396 */     if (StringUtils.isEmpty(entity.getUserId())) {
/* 397 */       throw new BusinessMessage("添加组关系 用户id不能为空");
/*     */     }
/* 399 */     this.orgRelationDao.create(entity);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/OrgRelationManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */