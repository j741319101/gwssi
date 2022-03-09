/*     */ package cn.gwssi.ecloudbpm.wf.plugin.node.recrease.executer;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.TaskType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.TaskIdentityLink;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.context.LeaderTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.leaderTask.def.LeaderTaskPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.context.DynamicTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.context.SignTaskPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.node.sign.def.SignTaskPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.UserDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.service.LeaderService;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class SuperviseTaskExecuter
/*     */ {
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*  51 */   private ThreadLocal<Boolean> execForce = new ThreadLocal<>();
/*     */   
/*     */   public void createTask(DefaultBpmTaskPluginSession pluginSession, String type) {
/*  54 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/*  55 */     BpmExecutionStack prevBpmExecutionStack = BpmContext.getThreadDynamictaskStack(task.getNodeId());
/*  56 */     if (!StringUtils.equals(task.getTaskType(), TaskType.SUPERVISE.getKey()) && getIsNeedSupervise()) {
/*  57 */       task.setTaskType(TaskType.SUPERVISE.getKey());
/*  58 */       this.bpmTaskManager.update(task);
/*  59 */       setIsNeedSupervise(Boolean.valueOf(false));
/*     */       
/*  61 */       if (prevBpmExecutionStack != null && StringUtils.equals("dynamic", type)) {
/*  62 */         BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(task.getId());
/*  63 */         Map<String, BpmExecutionStack> map = BpmContext.getAllThreadDynamictaskStack();
/*  64 */         if (CollectionUtil.isNotEmpty(map)) {
/*  65 */           map.forEach((key, stack) -> stack.setId(bpmExecutionStack.getId()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> preTaskComplete(DefaultBpmTaskPluginSession pluginSession) {
/*  74 */     BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
/*     */     
/*  76 */     if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SUPERVISE.getKey())) {
/*  77 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  78 */       defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/*  79 */       defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/*  80 */       defaultQueryFilter.addFilter("end_time", null, QueryOP.IS_NULL);
/*  81 */       defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/*  82 */       defaultQueryFilter.addFilter("level", Integer.valueOf(1), QueryOP.GREAT);
/*  83 */       defaultQueryFilter.addFilter("level", Integer.valueOf(5), QueryOP.LESS);
/*  84 */       List aliveStack = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/*  85 */       if (CollectionUtil.isNotEmpty(aliveStack)) {
/*  86 */         Boolean exec = this.execForce.get();
/*  87 */         if (exec == null || !exec.booleanValue()) {
/*  88 */           throw new BusinessException("需监管的任务还未完成");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/*  94 */     BaseActionCmd model = (BaseActionCmd)BpmContext.getActionModel();
/*     */     
/*  96 */     String[] destinations = model.getDestinations();
/*     */     
/*  98 */     List<String> typeSupervise = new ArrayList<>();
/*  99 */     if (destinations == null) {
/* 100 */       return typeSupervise;
/*     */     }
/* 102 */     for (String destination : destinations) {
/* 103 */       BpmNodeDef superviseNode = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), destination);
/* 104 */       BpmNodeDef subProcessNodeDef = superviseNode.getParentBpmNodeDef();
/* 105 */       SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)superviseNode.getPluginContext(SignTaskPluginContext.class);
/* 106 */       SignTaskPluginDef signTaskPluginDef = null;
/* 107 */       if (signTaskPluginContext != null) {
/* 108 */         signTaskPluginDef = (SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef();
/*     */       }
/* 110 */       DynamicTaskPluginContext dynamicTaskPluginContext = (DynamicTaskPluginContext)superviseNode.getPluginContext(DynamicTaskPluginContext.class);
/* 111 */       DynamicTaskPluginDef dynamicTaskPluginDef = null;
/* 112 */       if (subProcessNodeDef != null && subProcessNodeDef instanceof cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef) {
/* 113 */         dynamicTaskPluginContext = (DynamicTaskPluginContext)subProcessNodeDef.getPluginContext(DynamicTaskPluginContext.class);
/*     */       }
/* 115 */       if (dynamicTaskPluginContext != null) {
/* 116 */         dynamicTaskPluginDef = (DynamicTaskPluginDef)dynamicTaskPluginContext.getBpmPluginDef();
/*     */       }
/* 118 */       if ((signTaskPluginDef != null && signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) || (dynamicTaskPluginDef != null && dynamicTaskPluginDef
/* 119 */         .getEnabled().booleanValue() && dynamicTaskPluginDef.isNeedSupervise())) {
/* 120 */         UserDTO userDTO; if (subProcessNodeDef == null) {
/* 121 */           if (CollectionUtil.isNotEmpty(superviseNode.getOutcomeNodes())) {
/* 122 */             throw new BusinessException("受监管的任务不能有后续任务");
/*     */           }
/* 124 */         } else if (dynamicTaskPluginDef.getEnabled().booleanValue()) {
/* 125 */           if (CollectionUtil.isNotEmpty(subProcessNodeDef.getOutcomeNodes())) {
/* 126 */             throw new BusinessException("受监管的任务不能有后续任务");
/*     */           }
/*     */         }
/* 129 */         else if (CollectionUtil.isNotEmpty(superviseNode.getOutcomeNodes())) {
/* 130 */           throw new BusinessException("受监管的任务不能有后续任务");
/*     */         } 
/*     */         
/* 133 */         if (signTaskPluginDef.isSignMultiTask() && signTaskPluginDef.isNeedSupervise()) {
/* 134 */           typeSupervise.add("sign");
/*     */         }
/* 136 */         if (dynamicTaskPluginDef.getEnabled().booleanValue() && dynamicTaskPluginDef.isNeedSupervise()) {
/* 137 */           typeSupervise.add("dynamic");
/*     */         }
/* 139 */         IUser user = ContextUtil.getCurrentUser();
/* 140 */         if (!StringUtils.equals(bpmTask.getAssigneeId(), "0") && 
/* 141 */           !StringUtils.equals(user.getUserId(), bpmTask.getAssigneeId())) {
/* 142 */           LeaderService leaderService = AppUtil.getImplInstanceArray(LeaderService.class).get(0);
/* 143 */           LeaderTaskPluginContext leaderTaskPluginContext = (LeaderTaskPluginContext)bpmNodeDef.getPluginContext(LeaderTaskPluginContext.class);
/* 144 */           if (((LeaderTaskPluginDef)leaderTaskPluginContext.getBpmPluginDef()).isSignLeaderTask()) {
/* 145 */             IUser secretary = leaderService.getUserByLeaderId(bpmTask.getAssigneeId());
/*     */             
/* 147 */             if (secretary != null && StringUtils.equals(secretary.getUserId(), user.getUserId())) {
/* 148 */               userDTO = new UserDTO();
/* 149 */               userDTO.setId(bpmTask.getAssigneeId());
/* 150 */               userDTO.setFullname(bpmTask.getAssigneeNames());
/* 151 */               userDTO.setSn(Integer.valueOf(0));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 156 */         TaskIdentityLink taskIdentityLink = pluginSession.getTaskIdentityLink();
/*     */         
/* 158 */         String orgId = model.getApproveOrgId();
/* 159 */         if (taskIdentityLink != null) {
/* 160 */           orgId = taskIdentityLink.getOrgId();
/*     */         }
/* 162 */         model.setBpmIdentity(bpmTask.getNodeId(), 
/* 163 */             Arrays.asList(new DefaultIdentity[] { new DefaultIdentity(userDTO.getUserId(), userDTO.getFullname(), "user", orgId) }));
/* 164 */         setIsNeedSupervise(Boolean.valueOf(true));
/*     */       } 
/*     */     } 
/* 167 */     return typeSupervise;
/*     */   }
/*     */   
/*     */   public boolean taskComplete(DefaultBpmTaskPluginSession pluginSession) {
/* 171 */     BpmTask bpmTask = (BpmTask)pluginSession.getBpmTask();
/*     */     
/* 173 */     if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SUPERVISE.getKey())) {
/* 174 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 175 */       defaultQueryFilter.addParamsFilter("taskId", bpmTask.getId());
/* 176 */       defaultQueryFilter.addParamsFilter("prior", "FORWARD");
/* 177 */       defaultQueryFilter.addFilter("end_time", null, QueryOP.IS_NULL);
/* 178 */       defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/* 179 */       defaultQueryFilter.addFilter("level", Integer.valueOf(1), QueryOP.GREAT);
/* 180 */       defaultQueryFilter.addFilter("level", Integer.valueOf(5), QueryOP.LESS);
/* 181 */       List aliveStack = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 182 */       if (CollectionUtil.isNotEmpty(aliveStack)) {
/* 183 */         throw new BusinessException("需监管的任务还未完成");
/*     */       }
/* 185 */       return true;
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */   
/* 190 */   private final ThreadLocal<Boolean> isNeedSupervise = new ThreadLocal<>();
/*     */   
/*     */   public void setIsNeedSupervise(Boolean need) {
/* 193 */     this.isNeedSupervise.set(need);
/*     */   }
/*     */   
/*     */   public boolean getIsNeedSupervise() {
/* 197 */     Boolean b = this.isNeedSupervise.get();
/* 198 */     return (b == null) ? false : b.booleanValue();
/*     */   }
/*     */   
/*     */   public void clearSupervise() {
/* 202 */     this.isNeedSupervise.remove();
/*     */   }
/*     */   
/*     */   public Boolean getExecForce() {
/* 206 */     return this.execForce.get();
/*     */   }
/*     */   
/*     */   public void setExecForce(Boolean exec) {
/* 210 */     this.execForce.set(exec);
/*     */   }
/*     */   
/*     */   public void clearExecForce() {
/* 214 */     this.execForce.remove();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/recrease/executer/SuperviseTaskExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */