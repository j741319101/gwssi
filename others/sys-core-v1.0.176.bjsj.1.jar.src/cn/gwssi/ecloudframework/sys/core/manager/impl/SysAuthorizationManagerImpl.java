/*     */ package com.dstz.sys.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.dstz.sys.api.constant.RightsObjectConstants;
/*     */ import com.dstz.sys.core.dao.SysAuthorizationDao;
/*     */ import com.dstz.sys.core.manager.SysAuthorizationManager;
/*     */ import com.dstz.sys.core.model.SysAuthorization;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @Service("sysAuthorizationManager")
/*     */ public class SysAuthorizationManagerImpl
/*     */   extends BaseManager<String, SysAuthorization>
/*     */   implements SysAuthorizationManager
/*     */ {
/*     */   @Resource
/*     */   private SysAuthorizationDao sysAuthorizationDao;
/*     */   @Autowired(required = false)
/*     */   private GroupService userGroupService;
/*     */   
/*     */   public Set<String> getUserRights(String userId) {
/*  43 */     List<? extends IGroup> list = this.userGroupService.getGroupsByUserId(userId);
/*     */     
/*  45 */     Set<String> rights = new HashSet<>();
/*  46 */     rights.add(String.format("%s-%s", new Object[] { userId, "user" }));
/*  47 */     rights.add(String.format("%s-%s", new Object[] { "user", "all" }));
/*     */ 
/*     */     
/*  50 */     if (CollectionUtil.isEmpty(list)) return rights;
/*     */     
/*  52 */     for (IGroup group : list) {
/*  53 */       rights.add(String.format("%s-%s", new Object[] { group.getGroupId(), group.getGroupType() }));
/*     */     } 
/*     */     
/*  56 */     return rights;
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
/*     */   public Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey) {
/*  68 */     if (StringUtil.isEmpty(targetKey)) {
/*  69 */       targetKey = "id_";
/*     */     }
/*     */     
/*  72 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  74 */     Set<String> rights = getUserRights(userId);
/*     */     
/*  76 */     for (String r : rights) {
/*  77 */       if (sb.length() > 0) {
/*  78 */         sb.append(",");
/*     */       }
/*  80 */       sb.append("'").append(r).append("'");
/*     */     } 
/*  82 */     sb.insert(0, "inner join sys_authorization rights on " + targetKey + " = rights.rights_target_  and  rights.rights_object_ ='" + rightsObject.key() + "' and rights.rights_permission_code_ in ( ");
/*  83 */     sb.append(")");
/*     */     
/*  85 */     Map<String, Object> param = new HashMap<>();
/*  86 */     param.put("rightsSql", sb.toString());
/*     */     
/*  88 */     param.put("rights", rights);
/*     */     
/*  90 */     return param;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SysAuthorization> getByTarget(RightsObjectConstants rightsObject, String rightsTarget) {
/*  96 */     return this.sysAuthorizationDao.getByTarget(rightsObject.key(), rightsTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createAll(List<SysAuthorization> sysAuthorizationList, String targetId, String targetObject) {
/* 102 */     this.sysAuthorizationDao.deleteByTarget(targetObject, targetId);
/*     */     
/* 104 */     for (SysAuthorization authorization : sysAuthorizationList) {
/* 105 */       authorization.setRightsPermissionCode(String.format("%s-%s", new Object[] { authorization.getRightsIdentity(), authorization.getRightsType() }));
/* 106 */       if (StringUtil.isEmpty(authorization.getRightsObject())) {
/* 107 */         authorization.setRightsObject(targetObject);
/*     */       }
/* 109 */       authorization.setRightsCreateBy(ContextUtil.getCurrentUserId());
/* 110 */       authorization.setRightsCreateTime(new Date());
/*     */       
/* 112 */       this.sysAuthorizationDao.create(authorization);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysAuthorizationManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */