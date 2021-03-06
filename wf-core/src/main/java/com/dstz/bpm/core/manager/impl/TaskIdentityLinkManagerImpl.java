/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.dao.TaskIdentityLinkDao;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */
import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("taskIdentityLinkManager")
/*     */ public class TaskIdentityLinkManagerImpl
/*     */   extends BaseManager<String, TaskIdentityLink>
/*     */   implements TaskIdentityLinkManager
/*     */ {
/*     */   @Resource
/*     */ TaskIdentityLinkDao taskIdentityLinkDao;
/*     */   @Resource
/*     */   GroupService groupService;
/*     */   
/*     */   public void removeByTaskId(String taskId) {
/*  42 */     if (this.taskIdentityLinkDao.queryTaskIdentityCount(taskId) == 0)
/*  43 */       return;  this.taskIdentityLinkDao.removeByTaskId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/*  48 */     if (this.taskIdentityLinkDao.queryInstanceIdentityCount(instId) == 0)
/*  49 */       return;  this.taskIdentityLinkDao.removeByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bulkCreate(List<TaskIdentityLink> list) {
/*  54 */     list.forEach(taskIdentityLink -> this.taskIdentityLinkDao.create(taskIdentityLink));
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean checkUserOperatorPermission(String userId, String instanceId, String taskId) {
/*  59 */     if (StringUtil.isEmpty(instanceId) && StringUtil.isEmpty(taskId)) {
/*  60 */       throw new BusinessException("??????????????????????????????????????????id", BpmStatusCode.PARAM_ILLEGAL);
/*     */     }
/*     */     
/*  63 */     Set<String> rights = getUserRights(userId);
/*     */     
/*  65 */     Map<String, IExtendTaskAction> leaderTaskAction = AppUtil.getImplInstance(IExtendTaskAction.class);
/*  66 */     for (IExtendTaskAction extendTaskAction : leaderTaskAction.values()) {
/*  67 */       String leaderRight = extendTaskAction.getLeaderUserRights();
/*  68 */       if (!StringUtil.isEmpty(leaderRight)) {
/*  69 */         rights.add(leaderRight);
/*     */       }
/*     */     } 
/*  72 */     return Boolean.valueOf((this.taskIdentityLinkDao.checkUserOperatorPermission(rights, taskId, instanceId) > 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getUserRights(String userId) {
/*  82 */     List<? extends IGroup>  list = this.groupService.getGroupsByUserId(userId);
/*     */     
/*  84 */     Set<String> rights = new HashSet<>();
/*  85 */     rights.add(String.format("%s-%s", new Object[] { userId, "user" }));
/*     */     
/*  87 */     if (CollectionUtil.isEmpty(list)) return rights;
/*     */     
/*  89 */     for (IGroup group : list) {
/*  90 */       rights.add(String.format("%s-%s", new Object[] { group.getGroupId(), group.getGroupType() }));
/*     */     } 
/*  92 */     return rights;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createIdentityLink(IBpmTask bpmTask, List<SysIdentity> identitys) {
/*  98 */     List<TaskIdentityLink> identityLinks = new ArrayList<>();
/*     */     
/* 100 */     for (SysIdentity identity : identitys) {
/* 101 */       TaskIdentityLink link = new TaskIdentityLink();
/* 102 */       link.setId(IdUtil.getSuid());
/* 103 */       link.setIdentity(identity.getId());
/* 104 */       link.setIdentityName(identity.getName());
/* 105 */       link.setType(identity.getType());
/* 106 */       link.setPermissionCode(identity.getId() + "-" + identity.getType());
/* 107 */       link.setTaskId(bpmTask.getId());
/* 108 */       link.setInstId(bpmTask.getInstId());
/* 109 */       link.setTaskType(bpmTask.getTaskType());
///* 110 */       link.setOrgId(identity.getOrgId());
/* 111 */       identityLinks.add(link);
/*     */     } 
/*     */     
/* 114 */     bulkCreate(identityLinks);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TaskIdentityLink> getByTaskId(String taskId) {
/* 119 */     return this.taskIdentityLinkDao.getByTaskId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public TaskIdentityLink getByTaskIdAndUserId(String taskId, String userId) {
/* 124 */     return this.taskIdentityLinkDao.getByTaskIdAndUserId(taskId, userId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int updateCheckState(QueryFilter queryFilter) {
/* 130 */     queryFilter.addFilter("identity_", ContextUtil.getCurrentUserId(), QueryOP.IN);
/* 131 */     return this.taskIdentityLinkDao.updateCheckState(queryFilter);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/TaskIdentityLinkManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */