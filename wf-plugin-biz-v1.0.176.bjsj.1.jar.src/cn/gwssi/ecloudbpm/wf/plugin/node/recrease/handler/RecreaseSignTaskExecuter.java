/*     */ package com.dstz.bpm.plugin.node.recrease.handler;
/*     */ 
/*     */ import com.dstz.bpm.act.service.ActTaskService;
/*     */ import com.dstz.bpm.act.util.ActivitiUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.TaskCommand;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.core.model.TaskIdentityLink;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.task.TaskRejectActionHandler;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.context.LeaderTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.sign.handler.TaskSignAgreeActionHandler;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.UserDTO;
/*     */ import com.dstz.org.api.service.LeaderService;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class RecreaseSignTaskExecuter
/*     */ {
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private UserService userService;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private TaskIdentityLinkManager taskIdentityLinkManager;
/*     */   
/*     */   public void increase(DefualtTaskActionCmd model) {
/*  70 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
/*  71 */     JSONObject userInfo = model.getExtendConf();
/*  72 */     JSONArray users = userInfo.getJSONArray("users");
/*  73 */     String signId = model.getTaskId();
/*  74 */     for (int i = 0; i < users.size(); i++) {
/*  75 */       JSONObject user = users.getJSONObject(i);
/*  76 */       BpmTask task = (BpmTask)this.bpmTaskManager.get(model.getTaskId());
/*  77 */       task.setId(IdUtil.getSuid());
/*  78 */       task.setTaskType(TaskType.SIGN.getKey());
/*  79 */       task.setStatus("NORMAL");
/*  80 */       task.setParentId(signId);
/*  81 */       String clazzStr = user.getString("clazz");
/*  82 */       Class<SysIdentity> clazz = SysIdentity.class;
/*  83 */       if (StringUtils.isNotEmpty(clazzStr)) {
/*  84 */         ServiceLoader<SysIdentity> loader = ServiceLoader.load(SysIdentity.class);
/*  85 */         for (SysIdentity sysIdentity : loader) {
/*  86 */           if (StringUtils.equals(clazzStr, sysIdentity.getClass().getSimpleName())) {
/*  87 */             clazz = (Class)sysIdentity.getClass();
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*  92 */       SysIdentity bpmInentity = (SysIdentity)JSON.toJavaObject((JSON)user, clazz);
/*  93 */       task.setAssigneeId(bpmInentity.getId());
/*  94 */       IUser addUser = this.userService.getUserById(bpmInentity.getId());
/*  95 */       if (addUser == null) {
/*  96 */         throw new BusinessException(user.getString("name") + "用户丢失");
/*     */       }
/*  98 */       bpmInentity.setName(addUser.getFullname());
/*  99 */       task.setAssigneeNames(addUser.getFullname());
/*     */       
/* 101 */       List<SysIdentity> identityList = new ArrayList<>();
/* 102 */       identityList.add(bpmInentity);
/* 103 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(model.getInstanceId());
/* 104 */       this.bpmTaskOpinionManager.createOpinion((IBpmTask)task, (IBpmInstance)bpmInstance, identityList, model.getOpinion(), model.getActionName(), model
/* 105 */           .getFormId(), signId, null, null);
/* 106 */       this.taskIdentityLinkManager.createIdentityLink((IBpmTask)task, identityList);
/* 107 */       this.bpmTaskManager.create(task);
/* 108 */       BpmContext.setActionModel((ActionCmd)model);
/* 109 */       model.setBpmTask((IBpmTask)task);
/* 110 */       this.taskCommand.execute(EventType.TASK_POST_CREATE_EVENT, (TaskActionCmd)model);
/* 111 */       BpmContext.removeActionModel();
/*     */     } 
/* 113 */     bpmTaskOpinion.setCreateTime(new Date((new Date()).getTime() + 1000L));
/* 114 */     this.bpmTaskOpinionManager.update(bpmTaskOpinion); } @Resource private BpmProcessDefService bpmProcessDefService; @Resource
/*     */   private TaskCommand taskCommand; @Resource
/*     */   private TaskSignAgreeActionHandler taskSignAgreeActionHandler; @Resource(name = "taskRejectActionHandler")
/*     */   private TaskRejectActionHandler taskRejectActionHandler; @Resource
/* 118 */   protected ActTaskService actTaskService; public void decrease(DefualtTaskActionCmd model) { JSONObject extendConf = model.getExtendConf();
/* 119 */     String callBackId = (extendConf == null) ? "" : extendConf.getString("callBackId");
/* 120 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(model.getTaskId());
/* 121 */     String orgId = "";
/* 122 */     if (bpmTaskOpinion.getApproveTime() == null) {
/*     */       
/* 124 */       TaskIdentityLink taskIdentityLink = (TaskIdentityLink)this.taskIdentityLinkManager.get(model.getTaskLinkId());
/* 125 */       if (taskIdentityLink != null) {
/* 126 */         orgId = taskIdentityLink.getOrgId();
/*     */       }
/*     */     } else {
/* 129 */       orgId = bpmTaskOpinion.getTaskOrgId();
/*     */     } 
/* 131 */     if (StringUtils.isEmpty(orgId)) {
/* 132 */       throw new BusinessException("当前机构为空，请检查TaskLinkId");
/*     */     }
/* 134 */     model.setApproveOrgId(orgId);
/* 135 */     BpmTask mainTask = null;
/* 136 */     if (StringUtils.isNotEmpty(callBackId)) {
/* 137 */       for (String opinionId : callBackId.split(",")) {
/* 138 */         UserDTO userDTO; BpmTaskOpinion decreaseTaskOpinion = (BpmTaskOpinion)this.bpmTaskOpinionManager.get(opinionId);
/* 139 */         if (decreaseTaskOpinion == null || decreaseTaskOpinion.getApproveTime() != null) {
/* 140 */           throw new BusinessException("任务已处理 : " + opinionId);
/*     */         }
/*     */ 
/*     */         
/* 144 */         IUser user = ContextUtil.getCurrentUser();
/* 145 */         BpmTask signTask = (BpmTask)this.bpmTaskManager.get(decreaseTaskOpinion.getTaskId());
/*     */         
/* 147 */         BpmNodeDef dBpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(signTask.getDefId(), signTask.getNodeId());
/* 148 */         decreaseTaskOpinion.setStatus(OpinionStatus.DECREASEDYNAMIC.getKey());
/* 149 */         decreaseTaskOpinion.setApproveTime(new Date());
/* 150 */         decreaseTaskOpinion.setDurMs(Long.valueOf(decreaseTaskOpinion.getApproveTime().getTime() - decreaseTaskOpinion.getCreateTime().getTime()));
/* 151 */         LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)dBpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
/* 152 */         if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
/* 153 */           LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 154 */           IUser secretary = leaderService.getUserByLeaderId(signTask.getAssigneeId());
/*     */           
/* 156 */           if (secretary != null && StringUtils.equals(secretary.getUserId(), user.getUserId())) {
/* 157 */             userDTO = new UserDTO();
/* 158 */             userDTO.setId(signTask.getAssigneeId());
/* 159 */             userDTO.setFullname(signTask.getAssigneeNames());
/* 160 */             userDTO.setSn(Integer.valueOf(0));
/*     */           } 
/*     */         } 
/* 163 */         decreaseTaskOpinion.setApprover(userDTO.getUserId());
/* 164 */         decreaseTaskOpinion.setOpinion(model.getOpinion());
/* 165 */         decreaseTaskOpinion.setApproverName(userDTO.getFullname());
/* 166 */         decreaseTaskOpinion.setTaskOrgId(model.getApproveOrgId());
/* 167 */         this.bpmTaskOpinionManager.update(decreaseTaskOpinion);
/*     */ 
/*     */         
/* 170 */         if (mainTask == null) {
/* 171 */           mainTask = (BpmTask)this.bpmTaskManager.get(decreaseTaskOpinion.getSignId());
/*     */         }
/*     */         
/* 174 */         this.bpmTaskManager.remove(decreaseTaskOpinion.getTaskId());
/*     */         
/* 176 */         this.taskIdentityLinkManager.removeByTaskId(decreaseTaskOpinion.getTaskId());
/*     */         
/* 178 */         model.setBpmTask((IBpmTask)signTask);
/* 179 */         model.setTaskId(signTask.getId());
/* 180 */         BpmContext.setActionModel((ActionCmd)model);
/* 181 */         this.taskCommand.execute(EventType.TASK_COMPLETE_EVENT, (TaskActionCmd)model);
/* 182 */         this.taskCommand.execute(EventType.TASK_POST_COMPLETE_EVENT, (TaskActionCmd)model);
/* 183 */         BpmContext.removeActionModel();
/*     */       } 
/*     */     } else {
/* 186 */       throw new BusinessException("减签操作 任务id不能为空");
/*     */     } 
/*     */     
/* 189 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(mainTask.getDefId(), mainTask.getNodeId());
/* 190 */     List<BpmTask> subTasks = this.bpmTaskManager.getByParentId(mainTask.getId());
/*     */     
/* 192 */     StringBuilder mainApproverName = new StringBuilder();
/* 193 */     SignTaskPluginDef pluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
/* 194 */     boolean hanldeMainTask = this.taskSignAgreeActionHandler.isNeedHandleMainTask(model, pluginDef, subTasks, mainTask, mainApproverName, 0);
/* 195 */     if (hanldeMainTask) {
/*     */       
/* 197 */       this.bpmTaskManager.recycleTask(mainTask.getId(), OpinionStatus.SIGN_RECYCLE, "满足条件自动回收该会签任务");
/* 198 */       model.setBpmTask((IBpmTask)mainTask);
/* 199 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(mainTask.getInstId());
/* 200 */       model.setBpmInstance((IBpmInstance)bpmInstance);
/* 201 */       BpmContext.setActionModel((ActionCmd)model);
/* 202 */       String[] nodeIds = new String[0];
/* 203 */       if (bpmTaskOpinion.getApproveTime() != null) {
/* 204 */         nodeIds = new String[] { bpmTaskOpinion.getTaskKey() };
/*     */       }
/* 206 */       Map<String, Object> activityMap = ActivitiUtil.skipPrepare(bpmInstance.getActDefId(), bpmNodeDef.getNodeId(), nodeIds);
/*     */       
/*     */       try {
/* 209 */         if (ActionType.SIGNOPPOSE.getKey().equals(model.getActionName()) && ActionType.REJECT.getKey().equals(pluginDef.getOpposedAction())) {
/* 210 */           this.taskRejectActionHandler.doActionBefore(model);
/* 211 */           this.taskRejectActionHandler.doAction(model);
/* 212 */           this.taskRejectActionHandler.doActionAfter(model);
/*     */         } else {
/* 214 */           this.actTaskService.completeTask(mainTask.getTaskId(), model.getActionVariables());
/*     */         } 
/* 216 */       } catch (Exception e) {
/* 217 */         throw new BusinessException(e);
/*     */       } finally {
/* 219 */         ActivitiUtil.restoreActivity(activityMap);
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/handler/RecreaseSignTaskExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */