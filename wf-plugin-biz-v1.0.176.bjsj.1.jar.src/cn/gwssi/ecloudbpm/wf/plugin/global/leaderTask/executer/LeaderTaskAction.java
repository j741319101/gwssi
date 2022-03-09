/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.executer;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.DefaultExtendTaskAction;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderOptionLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmLeaderTaskLogManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmLeaderTaskLog;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.context.LeaderTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.LeaderService;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import dm.jdbc.util.StringUtil;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class LeaderTaskAction
/*     */   extends DefaultExtendTaskAction
/*     */ {
/*     */   @Autowired
/*     */   private BpmLeaderTaskLogManager bpmLeaderTaskLogManager;
/*     */   @Autowired
/*     */   private BpmLeaderOptionLogManager bpmLeaderOptionLogManager;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   public void turnLeaderTask(IBpmTask bpmTask) {
/*  49 */     String nodeId = bpmTask.getNodeId();
/*  50 */     String defId = bpmTask.getDefId();
/*  51 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/*  52 */     LeaderTaskPluginContext leaderTaskPlaginContext = (LeaderTaskPluginContext)bpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
/*  53 */     if (leaderTaskPlaginContext == null || !((LeaderTaskPluginDef)leaderTaskPlaginContext.getBpmPluginDef()).isSignLeaderTask()) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     BpmTask task = (BpmTask)bpmTask;
/*  58 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/*  59 */     if (!StringUtil.equals("0", task.getAssigneeId()) && leaderService.isLeaderByLeaderId(task.getAssigneeId())) {
/*     */       
/*  61 */       BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/*  62 */       taskLog.setTaskId(task.getId());
/*  63 */       taskLog.setInstId(task.getInstId());
/*  64 */       taskLog.setLeaderId(task.getAssigneeId());
/*  65 */       taskLog.setLeaderName(task.getAssigneeNames());
/*  66 */       taskLog.setType("1");
/*  67 */       this.bpmLeaderTaskLogManager.create(taskLog);
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
/*     */ 
/*     */   
/*     */   public boolean carbonCopyLeaderTask(String receiverId, String receId, IBpmTask bpmTask) {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean carbonInstCopyLeaderTask(String receiverId, String receId, IBpmInstance bpmInstance) {
/* 104 */     LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 105 */     BpmInstance inst = (BpmInstance)bpmInstance;
/*     */     
/* 107 */     if (leaderService.isLeaderByLeaderId(receiverId)) {
/* 108 */       IUser secretary = ContextUtil.getCurrentUser();
/* 109 */       IUser leader = leaderService.getUserBySecretaryId(secretary.getUserId());
/*     */       
/* 111 */       if (!StringUtils.equals(receiverId, leader.getUserId())) {
/*     */         
/* 113 */         BpmLeaderTaskLog taskLog = new BpmLeaderTaskLog();
/* 114 */         taskLog.setInstId(inst.getId());
/* 115 */         taskLog.setLeaderId(leader.getUserId());
/* 116 */         taskLog.setLeaderName(leader.getFullname());
/* 117 */         taskLog.setType("2");
/* 118 */         this.bpmLeaderTaskLogManager.create(taskLog);
/* 119 */         return true;
/*     */       } 
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   public String getLeaderUserRights() {
/* 126 */     List<LeaderService> leaderServices = AppUtil.getImplInstanceArray(LeaderService.class);
/* 127 */     if (CollectionUtil.isEmpty(leaderServices)) {
/* 128 */       return "";
/*     */     }
/* 130 */     LeaderService leaderService = leaderServices.get(0);
/*     */     
/* 132 */     IUser leader = leaderService.getUserBySecretaryId(ContextUtil.getCurrentUserId());
/* 133 */     if (leader != null) {
/* 134 */       return String.format("%s-%s", new Object[] { leader.getUserId(), "user" });
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteDataByInstId(String instId) {
/* 141 */     this.bpmLeaderOptionLogManager.removeByInstId(instId);
/* 142 */     this.bpmLeaderTaskLogManager.removeByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addQueryDodoTaskParams(QueryFilter queryFilter, String taskTableAlias) {
/* 147 */     if (StringUtil.isEmpty(taskTableAlias)) {
/* 148 */       taskTableAlias = "task";
/*     */     }
/* 150 */     Map<String, Object> params = queryFilter.getParams();
/* 151 */     String defaultWhere = params.containsKey("defaultWhere") ? params.get("defaultWhere").toString() : "";
/* 152 */     if (StringUtil.isNotEmpty(defaultWhere)) {
/* 153 */       defaultWhere = defaultWhere + " and ";
/*     */     }
/*     */ 
/*     */     
/* 157 */     defaultWhere = defaultWhere + " not exists (select 1 from bpm_leader_task_log leaderILog where leaderILog.task_id_ = " + taskTableAlias + ".id_ and leaderILog.leader_Id_ = '" + ContextUtil.getCurrentUserId() + "' ) ";
/* 158 */     queryFilter.addParamsFilter("defaultWhere", defaultWhere);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addQueryDoReadTaskParams(QueryFilter queryFilter, String carbonTableAlias) {
/* 163 */     if (StringUtil.isEmpty(carbonTableAlias)) {
/* 164 */       carbonTableAlias = "carbon";
/*     */     }
/* 166 */     Map<String, Object> params = queryFilter.getParams();
/* 167 */     String defaultWhere = params.containsKey("defaultWhere") ? params.get("defaultWhere").toString() : "";
/* 168 */     if (StringUtil.isNotEmpty(defaultWhere)) {
/* 169 */       defaultWhere = defaultWhere + " and ";
/*     */     }
/*     */ 
/*     */     
/* 173 */     defaultWhere = defaultWhere + " not exists (select 1 from bpm_leader_task_log leaderILog where leaderILog.receive_id_  = " + carbonTableAlias + ".id_ and leaderILog.leader_Id_ = '" + ContextUtil.getCurrentUserId() + "' ) ";
/* 174 */     queryFilter.addParamsFilter("defaultWhere", defaultWhere);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLeaderTask(String taskId) {
/* 179 */     return (this.bpmLeaderTaskLogManager.getByTaskId(taskId) != null);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/leaderTask/executer/LeaderTaskAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */