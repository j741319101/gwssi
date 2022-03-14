/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class HandleTaskCreateEvent
/*     */ {
/*     */   @Resource
/*     */   private DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   private IGroovyScriptEngine scriptEngine;
/*     */   @Resource
/*     */   private HandelRejectAction handelRejectAction;
/*     */   @Resource
/*     */   private DynamicInstTaskAction dynamicInstTaskActions;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   @Resource
/*     */   private SuperviseTaskExecuter superviseTaskExecuter;
/*     */   
/*     */   public void postTaskCreateEvent(DefaultBpmTaskPluginSession pluginSession) {
/*  61 */     this.superviseTaskExecuter.createTask(pluginSession, "dynamic");
/*     */   }
/*     */   
/*     */   public void taskCreateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
/*  65 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/*  66 */     BpmExecutionStack prevBpmExecutionStack = BpmContext.getThreadDynamictaskStack(task.getNodeId());
/*  67 */     if (this.superviseTaskExecuter.getIsNeedSupervise() && prevBpmExecutionStack != null) {
/*  68 */       this.dynamicInstTaskActions.setTaskThreadLocal(task);
/*     */       return;
/*     */     } 
/*  71 */     DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  72 */     BpmTask prepareTask = this.dynamicInstTaskActions.getTaskThreadLocal();
/*  73 */     boolean isEnabled = pluginDef.getIsEnabled().booleanValue();
/*     */     
/*  75 */     if (StringUtils.equals("追加", taskActionCmd.getDoActionName())) {
/*  76 */       isEnabled = false;
/*     */     }
/*  78 */     BpmNodeDef instBpmNodeDef = this.dynamicInstTaskActions.getDynamicInstNodeDef(pluginSession);
/*  79 */     boolean isInstDy = false;
/*  80 */     if (!isEnabled) {
/*  81 */       isInstDy = this.dynamicInstTaskActions.isBeginDynamicInstTask(instBpmNodeDef, pluginSession, false);
/*  82 */       isEnabled = isInstDy;
/*     */     } 
/*     */     
/*  85 */     if (!isEnabled) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/*  90 */     String parentInstId = bpmInstance.getParentInstId();
/*  91 */     String superNodeId = bpmInstance.getSuperNodeId();
/*  92 */     if (isInstDy && instBpmNodeDef.getType() != NodeType.CALLACTIVITY) {
/*  93 */       BpmNodeDef subProcessNodeDef = instBpmNodeDef.getParentBpmNodeDef();
/*  94 */       parentInstId = bpmInstance.getId();
/*  95 */       superNodeId = subProcessNodeDef.getNodeId();
/*     */     } 
/*     */     
/*  98 */     ActionCmd cmd = BpmContext.submitActionModel();
/*  99 */     if (BpmContext.submitActionModel().getActionName().equals(ActionType.REJECT.getKey())) {
/* 100 */       if (isInstDy) {
/*     */         return;
/*     */       }
/* 103 */       DefualtTaskActionCmd submitActionCmd = (DefualtTaskActionCmd)cmd;
/*     */       
/* 105 */       if (!submitActionCmd.getBpmTask().getNodeId().equals(task.getNodeId())) {
/* 106 */         this.handelRejectAction.handelBack2Tasks(task, taskActionCmd);
/*     */         return;
/*     */       } 
/*     */     } 
/* 110 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*     */     
/* 112 */     if (isInstDy) {
/* 113 */       defaultQueryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
/* 114 */       defaultQueryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
/*     */     } else {
/* 116 */       defaultQueryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
/* 117 */       defaultQueryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
/*     */     } 
/* 119 */     defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/* 120 */     if (prepareTask != null) {
/* 121 */       defaultQueryFilter.addFilter("task_id_", prepareTask.getId(), QueryOP.EQUAL);
/*     */     }
/* 123 */     List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/* 124 */     DynamicTask dynamicTask = (dynamicTasks.size() == 0) ? null : dynamicTasks.get(0);
/*     */     
/* 126 */     boolean isParallel = pluginDef.getIsParallel().booleanValue();
/* 127 */     if (dynamicTask == null) {
/* 128 */       dynamicTask = genDynamicTask(pluginDef, taskActionCmd, task, pluginSession);
/*     */     }
/* 130 */     if (isInstDy) {
/* 131 */       isParallel = DynamicTaskPluginExecuter.isParallel(instBpmNodeDef);
/* 132 */       dynamicTask.setNodeId(superNodeId);
/* 133 */       dynamicTask.setInstId(parentInstId);
/*     */     } 
/* 135 */     dynamicTask.setIsParallel(isParallel);
/* 136 */     if (prepareTask != null) {
/* 137 */       dynamicTask.setTaskId(prepareTask.getId());
/*     */     }
/* 139 */     if (isParallel) {
/* 140 */       String nodeId = task.getNodeId();
/* 141 */       if (isInstDy && instBpmNodeDef instanceof com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef) {
/* 142 */         nodeId = superNodeId;
/*     */       }
/* 144 */       Integer currentIndex = DynamicInstTaskAction.popDynamictaskIndex(nodeId);
/* 145 */       if (currentIndex == null) {
/* 146 */         throw new BusinessMessage("动态任务生成错误");
/*     */       }
/* 148 */       if (currentIndex.intValue() == 0 && StringUtils.isEmpty(dynamicTask.getId())) {
/* 149 */         this.dynamicTaskManager.create(dynamicTask);
/*     */       }
/* 151 */       List<SysIdentity> sysIdentities = new ArrayList<>();
/* 152 */       sysIdentities.addAll(DynamicInstTaskAction.popDynamictaskIdentities(nodeId));
/* 153 */       taskActionCmd.setBpmIdentity(task.getNodeId(), sysIdentities);
/*     */     } else {
/* 155 */       if (StringUtil.isEmpty(dynamicTask.getId())) {
/* 156 */         this.dynamicTaskManager.create(dynamicTask);
/*     */       }
/* 158 */       DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
/* 159 */       taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private DynamicTask genDynamicTask(DynamicTaskPluginDef pluginDef, DefualtTaskActionCmd taskActionCmd, BpmTask task, DefaultBpmTaskPluginSession pluginSession) {
/* 165 */     if ("interface".equals(pluginDef.getDynamicType()) && StringUtil.isNotEmpty(pluginDef.getInterfaceName())) {
/*     */       try {
/* 167 */         List<DynamicTaskIdentitys> dynamicTaskIdentitys = (List<DynamicTaskIdentitys>)this.scriptEngine.executeObject(pluginDef.getInterfaceName(), (Map)pluginSession);
/* 168 */         if (CollectionUtil.isEmpty(dynamicTaskIdentitys)) {
/* 169 */           throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
/*     */         }
/* 171 */         DynamicTask dt = new DynamicTask((IBpmTask)task);
/* 172 */         dt.setAmmount(Integer.valueOf(dynamicTaskIdentitys.size()));
/* 173 */         dt.setIsParallel(pluginDef.getIsParallel().booleanValue());
/* 174 */         dt.setIdentityNode(JSON.toJSONString(dynamicTaskIdentitys));
/* 175 */         return dt;
/* 176 */       } catch (Exception e) {
/* 177 */         throw new BusinessException("动态任务生成失败，自定义动态任务接口执行失败：" + e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */     
/* 181 */     List<SysIdentity> identitys = new ArrayList<>();
/* 182 */     List<List<SysIdentity>> identityLis = taskActionCmd.getDynamicBpmIdentity(task.getNodeId());
/* 183 */     DynamicTask dynamicTask = new DynamicTask((IBpmTask)task);
/* 184 */     dynamicTask.setIsParallel(pluginDef.getIsParallel().booleanValue());
/* 185 */     if (CollectionUtils.isNotEmpty(identityLis)) {
/* 186 */       identityLis.forEach(identityLi -> identitys.addAll(identityLi));
/*     */ 
/*     */       
/* 189 */       dynamicTask.setAmmount(Integer.valueOf(identityLis.size()));
/* 190 */       List<DynamicTaskIdentitys> identityNodes = new ArrayList<>();
/* 191 */       for (int i = 0; i < identityLis.size(); i++) {
/* 192 */         List<SysIdentity> list = new ArrayList<>(1);
/* 193 */         list.addAll(identityLis.get(i));
/* 194 */         identityNodes.add(new DynamicTaskIdentitys(String.format("%s-%d", new Object[] { task.getName(), Integer.valueOf(i + 1) }), list));
/*     */       } 
/* 196 */       dynamicTask.setIdentityNode(JSON.toJSONString(identityNodes));
/* 197 */       return dynamicTask;
/*     */     } 
/* 199 */     List<SysIdentity> sysIdentities = taskActionCmd.getBpmIdentity(task.getNodeId());
/* 200 */     if (CollectionUtil.isNotEmpty(sysIdentities)) {
/* 201 */       identitys.addAll(sysIdentities);
/*     */     }
/*     */     
/* 204 */     if (CollectionUtil.isEmpty(identitys)) {
/* 205 */       throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
/*     */     }
/* 207 */     return new DynamicTask((IBpmTask)task, identitys, pluginDef.getIsParallel().booleanValue());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/executer/HandleTaskCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */