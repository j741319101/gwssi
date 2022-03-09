/*     */ package cn.gwssi.ecloudbpm.wf.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.dao.BpmTaskOpinionDao;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskOpinionVO;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ @Service("bpmTaskOpinionManager")
/*     */ public class BpmTaskOpinionManagerImpl
/*     */   extends BaseManager<String, BpmTaskOpinion>
/*     */   implements BpmTaskOpinionManager
/*     */ {
/*     */   @Resource
/*     */   BpmTaskOpinionDao bpmTaskOpinionDao;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private IGroovyScriptEngine groovyScriptEngine;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private GroupService groupService;
/*     */   
/*     */   public BpmTaskOpinion getByTaskId(String taskId) {
/*  78 */     return this.bpmTaskOpinionDao.getByTaskId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createOpinionByInstance(InstanceActionCmd actionModel, boolean isCreateEvent) {
/*  83 */     IBpmInstance bpmInstance = actionModel.getBpmInstance();
/*  84 */     String formIdentity = actionModel.getFormId();
/*  85 */     DefaultInstanceActionCmd actionCmd = (DefaultInstanceActionCmd)actionModel;
/*  86 */     ExecutionEntity entity = actionCmd.getExecutionEntity();
/*  87 */     entity.getActivityId();
/*     */     
/*  89 */     BpmTaskOpinion bpmTaskOpinion = new BpmTaskOpinion();
/*  90 */     bpmTaskOpinion.setCreateTime(new Date());
/*  91 */     bpmTaskOpinion.setApproveTime(new Date());
/*  92 */     bpmTaskOpinion.setUpdateTime(new Date());
/*  93 */     bpmTaskOpinion.setDurMs(Long.valueOf(0L));
/*  94 */     bpmTaskOpinion.setVersion(BpmContext.getOptionVersion());
/*  95 */     bpmTaskOpinion.setInstId(bpmInstance.getId());
/*  96 */     bpmTaskOpinion.setSupInstId(bpmInstance.getParentInstId());
/*     */     
/*  98 */     if (!isCreateEvent) {
/*  99 */       bpmTaskOpinion.setOpinion("流程结束");
/*     */     } else {
/* 101 */       bpmTaskOpinion.setOpinion(actionCmd.getOpinion());
/*     */     } 
/* 103 */     bpmTaskOpinion.setTaskOrgId(actionCmd.getApproveOrgId());
/* 104 */     bpmTaskOpinion.setStatus(isCreateEvent ? "start" : "end");
/* 105 */     bpmTaskOpinion.setTaskId("0");
/* 106 */     if (entity.getActivityId() == null && isCreateEvent) {
/* 107 */       bpmTaskOpinion.setTaskName("开始节点");
/* 108 */       BpmNodeDef startNodeDef = this.bpmProcessDefService.getStartEvent(bpmInstance.getDefId());
/* 109 */       bpmTaskOpinion.setTaskKey(startNodeDef.getNodeId());
/* 110 */       bpmTaskOpinion.setTaskName(startNodeDef.getName());
/*     */     } else {
/* 112 */       bpmTaskOpinion.setTaskKey(entity.getActivityId());
/* 113 */       bpmTaskOpinion.setTaskName(entity.getCurrentActivityName());
/*     */     } 
/* 115 */     bpmTaskOpinion.setFormId(formIdentity);
/*     */     
/* 117 */     IUser user = ContextUtil.getCurrentUser();
/* 118 */     if (user != null) {
/* 119 */       bpmTaskOpinion.setApprover(user.getUserId());
/* 120 */       bpmTaskOpinion.setApproverName(user.getFullname());
/* 121 */       bpmTaskOpinion.setCreateBy(user.getUserId());
/* 122 */       bpmTaskOpinion.setUpdateBy(user.getUserId());
/*     */     } 
/*     */     
/* 125 */     create((Serializable)bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createOpinionByTask(TaskActionCmd taskActionModel) {
/* 130 */     List<SysIdentity> taskIdentitys = taskActionModel.getBpmIdentity(taskActionModel.getNodeId());
/* 131 */     createOpinion(taskActionModel.getBpmTask(), taskActionModel.getBpmInstance(), taskIdentitys, taskActionModel.getOpinion(), taskActionModel.getActionName(), taskActionModel.getFormId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void createOpinion(IBpmTask task, IBpmInstance bpmInstance, List<SysIdentity> taskIdentitys, String opinion, String actionName, String formId) {
/* 136 */     createOpinion(task, bpmInstance, taskIdentitys, opinion, actionName, formId, (String)null, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createOpinion(IBpmTask task, IBpmInstance bpmInstance, List<SysIdentity> taskIdentitys, String opinion, String actionName, String formId, String signId, String trace) {
/* 141 */     createOpinion(task, bpmInstance, taskIdentitys, opinion, actionName, formId, signId, trace, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createOpinion(IBpmTask task, IBpmInstance bpmInstance, List<SysIdentity> taskIdentitys, String opinion, String actionName, String formId, String signId, String trace, String version) {
/* 146 */     BpmTaskOpinion bpmTaskOpinion = new BpmTaskOpinion();
/* 147 */     bpmTaskOpinion.setCreateTime(new Date());
/* 148 */     bpmTaskOpinion.setUpdateTime(new Date());
/* 149 */     bpmTaskOpinion.setDurMs(Long.valueOf(0L));
/*     */     
/* 151 */     bpmTaskOpinion.setInstId(bpmInstance.getId());
/* 152 */     bpmTaskOpinion.setSupInstId(bpmInstance.getParentInstId());
/*     */     
/* 154 */     bpmTaskOpinion.setOpinion(opinion);
/* 155 */     bpmTaskOpinion.setStatus(OpinionStatus.getByActionName(actionName).getKey());
/* 156 */     bpmTaskOpinion.setTaskId(task.getId());
/* 157 */     bpmTaskOpinion.setTaskKey(task.getNodeId());
/* 158 */     bpmTaskOpinion.setTaskName(task.getName());
/* 159 */     bpmTaskOpinion.setFormId(formId);
/*     */     
/* 161 */     bpmTaskOpinion.setActExecutionId(task.getActExecutionId());
/* 162 */     IUser user = ContextUtil.getCurrentUser();
/* 163 */     if (user != null) {
/* 164 */       bpmTaskOpinion.setCreateBy(user.getUserId());
/* 165 */       bpmTaskOpinion.setUpdateBy(user.getUserId());
/*     */     } 
/*     */     
/* 168 */     StringBuilder assignInfo = new StringBuilder();
/* 169 */     if (CollectionUtil.isNotEmpty(taskIdentitys)) {
/* 170 */       for (SysIdentity identity : taskIdentitys) {
/* 171 */         assignInfo.append(identity.getType()).append("-")
/* 172 */           .append(identity.getName()).append("-")
/* 173 */           .append(identity.getId()).append("-")
/* 174 */           .append(identity.getOrgId()).append(",");
/*     */       }
/*     */     }
/*     */     
/* 178 */     bpmTaskOpinion.setAssignInfo(assignInfo.toString());
/*     */     
/* 180 */     bpmTaskOpinion.setSignId(signId);
/* 181 */     if (StringUtil.isEmpty(trace)) {
/* 182 */       trace = BpmContext.peekMulInstOpTrace();
/*     */     }
/* 184 */     bpmTaskOpinion.setTrace(trace);
/* 185 */     if (StringUtil.isEmpty(version)) {
/* 186 */       version = BpmContext.getOptionVersion();
/*     */     }
/* 188 */     bpmTaskOpinion.setVersion(version);
/*     */     
/* 190 */     create((Serializable)bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByInstAndNode(String instId, String nodeId) {
/* 195 */     return this.bpmTaskOpinionDao.getByInstAndNode(instId, nodeId, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByInstAndSignId(String instId, String signId) {
/* 200 */     return this.bpmTaskOpinionDao.getByInstAndNode(instId, null, signId, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByInstAndNodeVersion(String instId, String nodeId) {
/* 205 */     return this.bpmTaskOpinionDao.getByInstAndNodeVersion(instId, nodeId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByInst(String instId, String nodeId, String signId, String trace) {
/* 210 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionDao.getByInstAndNode(instId, nodeId, signId, null);
/* 211 */     if (StringUtil.isEmpty(trace)) {
/* 212 */       return opinions;
/*     */     }
/* 214 */     List<BpmTaskOpinion> ops = new ArrayList<>();
/* 215 */     opinions.forEach(op -> {
/*     */           if (StringUtil.isEmpty(op.getTrace()) || op.getTrace().startsWith(trace) || trace.startsWith(op.getTrace())) {
/*     */             ops.add(op);
/*     */           }
/*     */         });
/*     */     
/* 221 */     return ops;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinionVO> getByInstsOpinion(String instId, String taskId, String orgId, String status, Boolean extend) {
/* 226 */     DefaultQueryFilter defaultQueryFilter1 = new DefaultQueryFilter(true);
/* 227 */     if (StringUtil.isNotEmpty(taskId)) {
/* 228 */       BpmTaskOpinion opinion = this.bpmTaskOpinionDao.getByTaskId(taskId);
/* 229 */       if (StringUtil.isNotEmpty(opinion.getTrace())) {
/* 230 */         defaultQueryFilter1.addFilter("trace_", opinion.getTrace(), QueryOP.EQUAL);
/*     */       }
/*     */     } 
/* 233 */     if (StringUtils.isNotEmpty(status)) {
/* 234 */       if (StringUtils.equals(status, "awaiting_check")) {
/* 235 */         defaultQueryFilter1.addFilter("opinion.status_", status, QueryOP.EQUAL);
/*     */       } else {
/* 237 */         defaultQueryFilter1.addFilter("opinion.status_", "awaiting_check", QueryOP.NOT_IN);
/*     */       } 
/*     */     }
/* 240 */     List<String> instIds = new ArrayList<>();
/*     */     
/* 242 */     DefaultQueryFilter defaultQueryFilter2 = new DefaultQueryFilter(true);
/* 243 */     if (extend.booleanValue()) {
/* 244 */       List<BpmInstance> bpmInstances = this.bpmInstanceManager.listParentAndSubById(instId);
/* 245 */       bpmInstances.forEach(inst -> instIds.add(inst.getId()));
/* 246 */       if (CollectionUtils.isEmpty(instIds)) {
/* 247 */         return Collections.emptyList();
/*     */       }
/* 249 */       BpmInstance topInst = this.bpmInstanceManager.getTopInstance((BpmInstance)this.bpmInstanceManager.get(instId));
/* 250 */       if (topInst == null) {
/* 251 */         defaultQueryFilter2.addFilter("inst_id_", instId, QueryOP.EQUAL);
/*     */       } else {
/* 253 */         defaultQueryFilter2.addFilter("inst_id_", topInst.getId(), QueryOP.EQUAL);
/*     */       } 
/*     */     } else {
/* 256 */       instIds.add(instId);
/*     */     } 
/* 258 */     defaultQueryFilter1.addFilter("opinion.inst_id_", instIds, QueryOP.IN);
/*     */     
/* 260 */     if (CollectionUtil.isEmpty(instIds)) {
/* 261 */       return Collections.EMPTY_LIST;
/*     */     }
/*     */     
/* 264 */     List<BpmTaskOpinionVO> bpmTaskOpinionVOS = this.bpmTaskOpinionDao.getByInstsOpinion((QueryFilter)defaultQueryFilter1);
/* 265 */     if (!extend.booleanValue()) {
/* 266 */       return bpmTaskOpinionVOS;
/*     */     }
/* 268 */     defaultQueryFilter2.addFieldSort("id_", "asc");
/* 269 */     List<BpmTaskStack> topBpmTaskStacks = this.bpmTaskStackManager.query((QueryFilter)defaultQueryFilter2);
/* 270 */     String topTaskId = "";
/* 271 */     for (BpmTaskStack bpmTaskStack : topBpmTaskStacks) {
/* 272 */       if (StringUtil.isNotEmpty(bpmTaskStack.getTaskId())) {
/* 273 */         topTaskId = bpmTaskStack.getTaskId();
/*     */         break;
/*     */       } 
/*     */     } 
/* 277 */     DefaultQueryFilter defaultQueryFilter3 = new DefaultQueryFilter(true);
/* 278 */     defaultQueryFilter3.addParamsFilter("taskId", topTaskId);
/* 279 */     defaultQueryFilter3.addParamsFilter("prior", "FORWARD");
/* 280 */     defaultQueryFilter3.addFieldSort("level", "asc");
/* 281 */     defaultQueryFilter3.addFieldSort("id_", "asc");
/*     */     
/* 283 */     List<BpmTaskStack> allBpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter3);
/* 284 */     Map<String, List<BpmTaskStack>> instStacks = new HashMap<>();
/* 285 */     allBpmTaskStacks.forEach(stack -> {
/*     */           List<BpmTaskStack> bpmTaskStacks = (List<BpmTaskStack>)instStacks.get(stack.getInstId());
/*     */           
/*     */           if (bpmTaskStacks == null) {
/*     */             bpmTaskStacks = new ArrayList<>();
/*     */             instStacks.put(stack.getInstId(), bpmTaskStacks);
/*     */           } 
/*     */           if (!bpmTaskStacks.contains(stack)) {
/*     */             bpmTaskStacks.add(stack);
/*     */           }
/*     */         });
/* 296 */     List<BpmTaskOpinionVO> topBpmTaskOpinions = new ArrayList<>();
/*     */     
/* 298 */     Map<String, BpmTaskOpinionVO> callActivityDistTaskMap = new HashMap<>();
/*     */     
/* 300 */     Map<String, BpmTaskOpinionVO> callActivityExecutionMap = new HashMap<>();
/*     */     
/* 302 */     Map<String, String[]> callActivityTaskRelationKey = (Map)new HashMap<>();
/* 303 */     Map<String, IGroup> groupMap = new HashMap<>();
/* 304 */     String topInstId = null;
/* 305 */     for (BpmTaskOpinionVO bpmTaskOpinionVO : bpmTaskOpinionVOS) {
/* 306 */       if (StringUtils.isEmpty(bpmTaskOpinionVO.getParentNodeId())) {
/* 307 */         topBpmTaskOpinions.add(bpmTaskOpinionVO);
/* 308 */         topInstId = bpmTaskOpinionVO.getInstId();
/*     */         
/*     */         continue;
/*     */       } 
/* 312 */       String[] callActivityTaskRelation = callActivityTaskRelationKey.get(bpmTaskOpinionVO.getInstId());
/* 313 */       if (callActivityTaskRelation == null) {
/* 314 */         callActivityTaskRelation = new String[2];
/*     */         
/* 316 */         BpmTaskStack bpmTaskStack = getTaskStack(instStacks, (BpmTaskOpinion)bpmTaskOpinionVO);
/* 317 */         if (bpmTaskStack == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 322 */         if (StringUtils.equals("callActivity", bpmTaskStack.getNodeType())) {
/*     */           
/* 324 */           callActivityTaskRelation[0] = bpmTaskStack.getId();
/*     */           
/* 326 */           List<BpmTaskStack> parentStack = instStacks.get(bpmTaskStack.getInstId());
/* 327 */           BpmTaskStack distributeTaskStack = null;
/* 328 */           for (BpmTaskStack seqStack : parentStack) {
/* 329 */             if (StringUtils.equals(seqStack.getId(), bpmTaskStack.getParentId())) {
/* 330 */               for (BpmTaskStack disStack : parentStack) {
/* 331 */                 if (StringUtils.equals(seqStack.getParentId(), disStack.getId())) {
/* 332 */                   distributeTaskStack = disStack;
/*     */                 }
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 339 */           callActivityTaskRelation[1] = distributeTaskStack.getId();
/*     */           
/* 341 */           callActivityTaskRelationKey.put(bpmTaskOpinionVO.getInstId(), callActivityTaskRelation);
/*     */ 
/*     */           
/* 344 */           BpmTaskOpinionVO bpmTaskOpinionVO1 = new BpmTaskOpinionVO();
/* 345 */           bpmTaskOpinionVO1.setTaskId(callActivityTaskRelation[0]);
/* 346 */           bpmTaskOpinionVO1.setInstId(bpmTaskStack.getInstId());
/* 347 */           bpmTaskOpinionVO1.setTaskName(bpmTaskStack.getNodeName());
/* 348 */           bpmTaskOpinionVO1.setCreateTime(bpmTaskStack.getStartTime());
/* 349 */           bpmTaskOpinionVO1.setApproveTime(bpmTaskStack.getEndTime());
/* 350 */           bpmTaskOpinionVO1.setStatus(bpmTaskStack.getActionName());
/* 351 */           bpmTaskOpinionVO1.setDefId(bpmTaskOpinionVO.getParentDefId());
/* 352 */           bpmTaskOpinionVO1.setTaskKey(bpmTaskStack.getNodeId());
/* 353 */           bpmTaskOpinionVO1.setTaskType("CallActivity");
/* 354 */           bpmTaskOpinionVO1.setOpinion("agree");
/* 355 */           callActivityExecutionMap.put(callActivityTaskRelation[0], bpmTaskOpinionVO1);
/*     */           
/* 357 */           BpmTaskOpinionVO callActivityDistTask = callActivityDistTaskMap.get(callActivityTaskRelation[1]);
/* 358 */           if (callActivityDistTask == null) {
/* 359 */             callActivityDistTask = new BpmTaskOpinionVO();
/* 360 */             callActivityDistTask.setTaskId(callActivityTaskRelation[1]);
/* 361 */             callActivityDistTask.setInstId(bpmTaskStack.getInstId());
/* 362 */             callActivityDistTask.setTaskName(bpmTaskOpinionVO1.getTaskName());
/* 363 */             callActivityDistTask.setTaskKey(bpmTaskOpinionVO1.getTaskKey());
/* 364 */             callActivityDistTask.setCreateTime(bpmTaskStack.getStartTime());
/* 365 */             callActivityDistTask.setDefId(bpmTaskOpinionVO.getParentDefId());
/* 366 */             callActivityDistTask.setApproveTime(bpmTaskStack.getEndTime());
/* 367 */             callActivityDistTask.setTaskType("CallActivity");
/* 368 */             callActivityDistTask.setStatus("agree");
/* 369 */             callActivityDistTask.setOpinion("agree");
/* 370 */             if (StringUtils.equals(bpmTaskStack.getInstId(), topInstId)) {
/* 371 */               topBpmTaskOpinions.add(callActivityDistTask);
/*     */             } else {
/* 373 */               String[] parentCallActivityTaskRelation = callActivityTaskRelationKey.get(bpmTaskOpinionVO.getSupInstId());
/* 374 */               if (parentCallActivityTaskRelation != null) {
/* 375 */                 BpmTaskOpinionVO parentCallActivityExecution = callActivityExecutionMap.get(parentCallActivityTaskRelation[0]);
/* 376 */                 if (parentCallActivityExecution != null) {
/* 377 */                   parentCallActivityExecution.addBpmTaskOpinionVO(callActivityDistTask);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 382 */             callActivityDistTaskMap.put(callActivityTaskRelation[1], callActivityDistTask);
/*     */           } 
/* 384 */           callActivityDistTask.addBpmTaskOpinionVO(bpmTaskOpinionVO1);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 389 */       BpmTaskOpinionVO callActivityExecution = callActivityExecutionMap.get(callActivityTaskRelation[0]);
/* 390 */       callActivityExecution.addBpmTaskOpinionVO(bpmTaskOpinionVO);
/*     */     } 
/* 392 */     callActivityDistTaskMap.forEach((id, distTask) -> Collections.sort(distTask.getBpmTaskOpinionVOS()));
/* 393 */     return topBpmTaskOpinions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BpmTaskStack getTaskStack(Map<String, List<BpmTaskStack>> instStack, BpmTaskOpinion subBpmOpinion) {
/* 404 */     if (StringUtil.isNotEmpty(subBpmOpinion.getSupInstId())) {
/* 405 */       BpmTaskStack childrenStack = ((List<BpmTaskStack>)instStack.get(subBpmOpinion.getInstId())).get(0);
/* 406 */       List<BpmTaskStack> parentStacks = instStack.get(subBpmOpinion.getSupInstId());
/* 407 */       for (BpmTaskStack bpmTaskStack : parentStacks) {
/* 408 */         if (StringUtils.equals(bpmTaskStack.getId(), childrenStack.getParentId())) {
/* 409 */           return bpmTaskStack;
/*     */         }
/*     */       } 
/*     */     } else {
/* 413 */       List<BpmTaskStack> parentStacks = instStack.get(subBpmOpinion.getInstId());
/* 414 */       for (BpmTaskStack bpmTaskStack : parentStacks) {
/* 415 */         if (StringUtils.equals(bpmTaskStack.getTaskId(), subBpmOpinion.getTaskId())) {
/* 416 */           return bpmTaskStack;
/*     */         }
/*     */         
/* 419 */         if (StringUtil.isNotEmpty(subBpmOpinion.getTrace()) && 
/* 420 */           StringUtils.equals(bpmTaskStack.getTaskId(), subBpmOpinion.getTrace())) {
/* 421 */           return bpmTaskStack;
/*     */         }
/*     */ 
/*     */         
/* 425 */         if (StringUtils.equals("0", subBpmOpinion.getTaskId()) && 
/* 426 */           StringUtils.equals(bpmTaskStack.getNodeId(), subBpmOpinion.getTaskKey())) {
/* 427 */           return bpmTaskStack;
/*     */         }
/*     */       } 
/*     */     } 
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByInstId(String instId) {
/* 436 */     return this.bpmTaskOpinionDao.getByInstAndNode(instId, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/* 441 */     this.bpmTaskOpinionDao.removeByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByTaskId(String taskId) {
/* 446 */     this.bpmTaskOpinionDao.removeByTaskId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void commonUpdate(String taskId, OpinionStatus opinionStatus, String opinion) {
/* 451 */     BpmTaskOpinion bpmTaskOpinion = getByTaskId(taskId);
/* 452 */     bpmTaskOpinion.setStatus(opinionStatus.getKey());
/* 453 */     bpmTaskOpinion.setApproveTime(new Date());
/* 454 */     Long durMs = Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime());
/* 455 */     bpmTaskOpinion.setDurMs(Long.valueOf((durMs.longValue() > 0L) ? durMs.longValue() : 10L));
/* 456 */     bpmTaskOpinion.setOpinion(opinion);
/* 457 */     IUser user = ContextUtil.getCurrentUser();
/* 458 */     if (user != null) {
/* 459 */       bpmTaskOpinion.setApprover(user.getUserId());
/* 460 */       bpmTaskOpinion.setApproverName(user.getFullname());
/*     */     } 
/* 462 */     update((Serializable)bpmTaskOpinion);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> selectByTaskIds(Collection<String> taskIds) {
/* 467 */     if (taskIds == null || taskIds.isEmpty()) {
/* 468 */       return new ArrayList<>();
/*     */     }
/* 470 */     return this.bpmTaskOpinionDao.selectByTaskIds(taskIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskOpinion> getByParam(String instId, String nodeId, String signId, String actExecutionId) {
/* 475 */     return this.bpmTaskOpinionDao.getByInstAndNode(instId, nodeId, signId, actExecutionId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmTaskOpinionManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */