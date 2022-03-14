/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.dao.BpmTaskDao;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.core.vo.BpmTaskVO;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service("bpmTaskManager")
/*     */ public class BpmTaskManagerImpl
/*     */   extends BaseManager<String, BpmTask>
/*     */   implements BpmTaskManager
/*     */ {
/*     */   @Resource
/*     */   BpmTaskDao bpmTaskDao;
/*     */   @Resource
/*     */   TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   @Resource
/*     */   BpmInstanceManager instanceManager;
/*     */   @Resource
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   
/*     */   public List<BpmTask> getByInstIdNodeId(String instId, String nodeId) {
/*  58 */     return this.bpmTaskDao.getByInstIdNodeId(instId, nodeId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTask> getSignTaskBySignSourceId(String taskId) {
/*  63 */     return this.bpmTaskDao.getSignTaskBySignSourceId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTask> getByInstId(String instId) {
/*  68 */     return this.bpmTaskDao.getByInstIdNodeId(instId, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/*  73 */     this.bpmTaskDao.removeByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskVO> getTodoList(String userId, QueryFilter queryFilter) {
/*  78 */     Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
/*  79 */     queryFilter.addParamsFilter("userRights", userRights);
/*  80 */     queryFilter.addParamsFilter("userId", userId);
/*  81 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/*  82 */     for (IExtendTaskAction extendTaskAction : extendTaskActions) {
/*  83 */       extendTaskAction.addQueryDodoTaskParams(queryFilter, "task");
/*     */     }
/*  85 */     return this.bpmTaskDao.getTodoList(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List getTodoList(QueryFilter queryFilter) {
/*  90 */     String userId = ContextUtil.getCurrentUserId();
/*  91 */     String type = (String)queryFilter.getParams().get("type");
/*  92 */     String title = (String)queryFilter.getParams().get("subject");
/*     */     
/*  94 */     if (StringUtil.isNotEmpty(title)) {
/*  95 */       queryFilter.addFilter("subject_", title, QueryOP.LIKE);
/*     */     }
/*     */     
/*  98 */     if ("done".equals(type)) {
/*  99 */       return this.instanceManager.getApproveHistoryList(userId, queryFilter);
/*     */     }
/*     */     
/* 102 */     Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
/* 103 */     queryFilter.addParamsFilter("userRights", userRights);
/* 104 */     queryFilter.addParamsFilter("userId", userId);
/*     */     
/* 106 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 107 */     for (IExtendTaskAction extendTaskAction : extendTaskActions) {
/* 108 */       extendTaskAction.addQueryDodoTaskParams(queryFilter, "task");
/*     */     }
/*     */     
/* 111 */     return this.bpmTaskDao.getTodoList(queryFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assigneeTask(String taskId, List<SysIdentity> sysIdentities) {
/* 118 */     BpmTask task = (BpmTask)get(taskId);
/* 119 */     if (task == null) {
/* 120 */       throw new BusinessException("任务可能已经被处理，请刷新。", BpmStatusCode.TASK_NOT_FOUND);
/*     */     }
/* 122 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/* 123 */       throw new BusinessException("候选人为空");
/*     */     }
/* 125 */     if (sysIdentities.size() > 1) {
/* 126 */       task.setAssigneeId("0");
/* 127 */       StringBuffer stringBuffer = new StringBuffer();
/* 128 */       sysIdentities.forEach(sysIdentity -> sb.append(sysIdentity.getName()).append(","));
/*     */ 
/*     */       
/* 131 */       task.setAssigneeNames(stringBuffer.substring(0, stringBuffer.length() - 1));
/*     */     } else {
/* 133 */       SysIdentity sysIdentity = sysIdentities.get(0);
/* 134 */       task.setAssigneeId(sysIdentity.getId());
/* 135 */       task.setAssigneeNames(sysIdentity.getName());
/*     */     } 
/*     */     
/* 138 */     task.setStatus(TaskStatus.DESIGNATE.getKey());
/* 139 */     update((Serializable)task);
/*     */     
/* 141 */     this.taskIdentityLinkManager.removeByTaskId(task.getId());
/* 142 */     this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, sysIdentities);
/*     */     
/* 144 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 145 */     StringBuffer sb = new StringBuffer();
/* 146 */     sysIdentities.forEach(sysIdentity -> sb.append("user").append("-").append(sysIdentity.getName()).append("-").append(sysIdentity.getId()).append("-").append(sysIdentity.getOrgId()).append(","));
/*     */ 
/*     */ 
/*     */     
/* 150 */     taskOpinion.setAssignInfo(sb.toString());
/* 151 */     this.bpmTaskOpinionManager.update(taskOpinion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unLockTask(String taskId) {
/* 156 */     BpmTask task = (BpmTask)get(taskId);
/*     */     
/* 158 */     List<TaskIdentityLink> identitys = this.taskIdentityLinkManager.getByTaskId(task.getId());
/* 159 */     if (CollectionUtil.isEmpty(identitys)) {
/* 160 */       throw new BusinessMessage("该任务并非多候选人状态，无效的操作！");
/*     */     }
/*     */     
/* 163 */     StringBuilder nameSb = new StringBuilder();
/* 164 */     for (TaskIdentityLink identity : identitys) {
/* 165 */       if (nameSb.length() > 0) {
/* 166 */         nameSb.append(";");
/*     */       }
/* 168 */       nameSb.append(identity.getIdentityName());
/*     */     } 
/*     */     
/* 171 */     task.setAssigneeId("0");
/* 172 */     task.setAssigneeNames(nameSb.toString());
/* 173 */     task.setStatus(TaskStatus.NORMAL.getKey());
/* 174 */     update((Serializable)task);
/*     */     
/* 176 */     StringBuilder assignInfo = new StringBuilder();
/* 177 */     if (CollectionUtil.isNotEmpty(identitys)) {
/* 178 */       for (TaskIdentityLink identity : identitys) {
/* 179 */         assignInfo.append(identity.getType()).append("-").append(identity.getIdentityName())
/* 180 */           .append("-").append(identity.getIdentity())
/* 181 */           .append("-").append(identity.getOrgId()).append(",");
/*     */       }
/*     */     }
/*     */     
/* 185 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
/* 186 */     taskOpinion.setAssignInfo(assignInfo.toString());
/* 187 */     this.bpmTaskOpinionManager.update(taskOpinion);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SysIdentity> getAssignUserById(BpmTask task) {
/* 193 */     List<SysIdentity> identityList = new ArrayList<>();
/* 194 */     List<TaskIdentityLink> identitys = this.taskIdentityLinkManager.getByTaskId(task.getId());
/* 195 */     for (TaskIdentityLink link : identitys) {
/* 196 */       identityList.add(new DefaultIdentity(link.getIdentity(), link.getIdentityName(), link.getType(), link.getOrgId()));
/*     */     }
/*     */     
/* 199 */     return identityList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTask> getByParentId(String parentId) {
/* 204 */     return this.bpmTaskDao.getByParentId(parentId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTypeTreeCountVO> todoTaskListTypeCount(String userId, QueryFilter queryFilter) {
/* 209 */     queryFilter.setPage(null);
/* 210 */     Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
/* 211 */     queryFilter.addParamsFilter("userRights", userRights);
/* 212 */     queryFilter.addParamsFilter("userId", ContextUtil.getCurrentUserId());
/*     */     
/* 214 */     return this.bpmTaskDao.getTodoListTypeCount(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> selectTaskIdByInstId(String instId) {
/* 219 */     return this.bpmTaskDao.selectTaskIdByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map getMyTaskNum() {
/* 224 */     String userId = ContextUtil.getCurrentUserId();
/* 225 */     Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
/* 226 */     return this.bpmTaskDao.getMyTaskNum(userId, userRights);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map> getUnionOrder() {
/* 231 */     return this.bpmTaskDao.getUnionOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTask> getByParam(String actInstId, String actExecutionId) {
/* 236 */     Map<String, Object> map = new HashMap<>();
/* 237 */     map.put("actInstId", actInstId);
/* 238 */     map.put("actExecutionId", actExecutionId);
/* 239 */     return this.bpmTaskDao.getByParam(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recycleTask(String taskId, OpinionStatus opinionStatus, String opinionStr) {
/* 244 */     BpmTask task = (BpmTask)get(taskId);
/* 245 */     String sourceTaskId = taskId;
/* 246 */     if (Arrays.<TaskType>asList(new TaskType[] { TaskType.ADD_SIGN, TaskType.SIGN, TaskType.ADDDO }).contains(TaskType.fromKey(task.getTaskType()))) {
/* 247 */       sourceTaskId = task.getParentId();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     List<BpmTask> subTasks = (List<BpmTask>)getByParentId(sourceTaskId).stream().filter(t -> Arrays.<TaskType>asList(new TaskType[] { TaskType.ADD_SIGN, TaskType.SIGN, TaskType.ADDDO }).contains(TaskType.fromKey(t.getTaskType()))).collect(Collectors.toList());
/*     */     
/* 257 */     for (BpmTask t : subTasks) {
/* 258 */       this.taskIdentityLinkManager.removeByTaskId(t.getId());
/* 259 */       remove(t.getId());
/*     */     } 
/*     */     
/* 262 */     for (BpmTask t : subTasks) {
/* 263 */       BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(t.getId());
/* 264 */       if (OpinionStatus.fromKey(opinion.getStatus()) != OpinionStatus.AWAITING_CHECK) {
/*     */         continue;
/*     */       }
/* 267 */       opinion.setApproveTime(new Date((new Date()).getTime()));
/* 268 */       opinion.setOpinion(opinionStr);
/* 269 */       opinion.setStatus(opinionStatus.getKey());
/* 270 */       this.bpmTaskOpinionManager.update(opinion);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskVO> getTaskLinksInfo(QueryFilter queryFilter) {
/* 276 */     return this.bpmTaskDao.getTaskLinksInfo(queryFilter);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmTaskManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */