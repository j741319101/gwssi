/*     */ package com.dstz.bpm.plugin.node.dynamictask.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
/*     */ import com.dstz.bpm.plugin.core.model.DynamicTask;
/*     */ import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class HandleTaskCreateEvent
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskManager dynamicTaskManager;
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   @Resource
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Resource
/*     */   HandelRejectAction handelRejectAction;
/*     */   @Resource
/*     */   DynamicInstTaskAction dynamicInstTaskActions;
/*     */   
/*     */   public void taskCreateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
/*  49 */     DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  50 */     BpmTask task = (BpmTask)pluginSession.getBpmTask();
/*  51 */     boolean isEnabled = pluginDef.getIsEnabled().booleanValue();
/*  52 */     BpmNodeDef instBpmNodeDef = this.dynamicInstTaskActions.getDynamicInstNodeDef(pluginSession);
/*  53 */     if (this.dynamicInstTaskActions.isDynamicInstTask(instBpmNodeDef) && 
/*  54 */       !StringUtils.equals(pluginSession.get("submitActionName").toString(), ActionType.AGREE.getKey()) && 
/*  55 */       !StringUtils.equals(pluginSession.get("submitActionName").toString(), ActionType.OPPOSE.getKey()) && 
/*  56 */       !StringUtils.equals(pluginSession.get("submitActionName").toString(), ActionType.SIGNOPPOSE.getKey()) && 
/*  57 */       !StringUtils.equals(pluginSession.get("submitActionName").toString(), ActionType.SIGNAGREE.getKey()) && 
/*  58 */       !StringUtils.equals(pluginSession.get("submitActionName").toString(), ActionType.TASK_FREE_JUMP.getKey())) {
/*  59 */       throw new BusinessException(String.format("动态多实例任务暂不适用%s操作", new Object[] { pluginSession.get("submitActionDesc") }));
/*     */     }
/*     */     
/*  62 */     boolean isInstDy = false;
/*  63 */     if (!isEnabled) {
/*  64 */       isInstDy = this.dynamicInstTaskActions.isBeginDynamicInstTask(instBpmNodeDef, pluginSession);
/*  65 */       isEnabled = isInstDy;
/*     */     } 
/*     */     
/*  68 */     if (!isEnabled) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     BpmInstance bpmInstance = (BpmInstance)pluginSession.getBpmInstance();
/*  73 */     String parentInstId = bpmInstance.getParentInstId();
/*  74 */     String superNodeId = bpmInstance.getSuperNodeId();
/*  75 */     if (isInstDy && instBpmNodeDef.getType() != NodeType.CALLACTIVITY) {
/*  76 */       BpmNodeDef subProcessNodeDef = instBpmNodeDef.getParentBpmNodeDef();
/*  77 */       parentInstId = bpmInstance.getId();
/*  78 */       superNodeId = subProcessNodeDef.getNodeId();
/*     */     } 
/*     */     
/*  81 */     ActionCmd cmd = BpmContext.submitActionModel();
/*  82 */     if (BpmContext.submitActionModel().getActionName().equals(ActionType.REJECT.getKey())) {
/*  83 */       if (isInstDy) {
/*     */         return;
/*     */       }
/*  86 */       DefualtTaskActionCmd submitActionCmd = (DefualtTaskActionCmd)cmd;
/*     */       
/*  88 */       if (!submitActionCmd.getBpmTask().getNodeId().equals(task.getNodeId())) {
/*  89 */         this.handelRejectAction.handelBack2Tasks(task, taskActionCmd);
/*     */         return;
/*     */       } 
/*     */     } 
/*  93 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*     */     
/*  95 */     if (isInstDy) {
/*  96 */       defaultQueryFilter.addFilter("inst_id_", parentInstId, QueryOP.EQUAL);
/*  97 */       defaultQueryFilter.addFilter("node_id_", superNodeId, QueryOP.EQUAL);
/*     */     } else {
/*  99 */       defaultQueryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
/* 100 */       defaultQueryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
/*     */     } 
/* 102 */     defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/* 103 */     List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query((QueryFilter)defaultQueryFilter);
/* 104 */     DynamicTask dynamicTask = (dynamicTasks.size() == 0) ? null : dynamicTasks.get(0);
/*     */     
/* 106 */     boolean isParallel = pluginDef.getIsParallel().booleanValue();
/* 107 */     if (dynamicTask == null) {
/* 108 */       dynamicTask = genDynamicTask(pluginDef, taskActionCmd, task, pluginSession);
/*     */     }
/* 110 */     if (isInstDy) {
/* 111 */       isParallel = DynamicTaskPluginExecuter.isParallel(instBpmNodeDef);
/* 112 */       dynamicTask.setNodeId(superNodeId);
/* 113 */       dynamicTask.setInstId(parentInstId);
/*     */     } 
/* 115 */     dynamicTask.setIsParallel(isParallel);
/* 116 */     if (isParallel) {
/* 117 */       Integer currentIndex = BpmContext.popDynamictaskIndex();
/* 118 */       if (currentIndex.intValue() == 0) {
/* 119 */         this.dynamicTaskManager.create(dynamicTask);
/*     */       }
/* 121 */       this.bpmTaskManager.update(task);
/* 122 */       List<SysIdentity> sysIdentities = new ArrayList<>();
/* 123 */       sysIdentities.add(BpmContext.popDynamictaskIdentities());
/* 124 */       taskActionCmd.setBpmIdentity(task.getNodeId(), sysIdentities);
/*     */     } else {
/* 126 */       if (StringUtil.isEmpty(dynamicTask.getId())) {
/* 127 */         this.dynamicTaskManager.create(dynamicTask);
/*     */       }
/* 129 */       DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
/*     */ 
/*     */ 
/*     */       
/* 133 */       taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private DynamicTask genDynamicTask(DynamicTaskPluginDef pluginDef, DefualtTaskActionCmd taskActionCmd, BpmTask task, DefaultBpmTaskPluginSession pluginSession) {
/* 139 */     if ("interface".equals(pluginDef.getDynamicType()) && StringUtil.isNotEmpty(pluginDef.getInterfaceName())) {
/*     */       try {
/* 141 */         List<DynamicTaskIdentitys> dynamicTaskIdentitys = (List<DynamicTaskIdentitys>)this.scriptEngine.executeObject(pluginDef.getInterfaceName(), (Map)pluginSession);
/* 142 */         if (CollectionUtil.isEmpty(dynamicTaskIdentitys)) {
/* 143 */           throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
/*     */         }
/* 145 */         DynamicTask dt = new DynamicTask((IBpmTask)task);
/* 146 */         dt.setAmmount(Integer.valueOf(dynamicTaskIdentitys.size()));
/* 147 */         dt.setIsParallel(pluginDef.getIsParallel().booleanValue());
/* 148 */         dt.setIdentityNode(JSON.toJSONString(dynamicTaskIdentitys));
/* 149 */         return dt;
/* 150 */       } catch (Exception e) {
/* 151 */         throw new BusinessException("动态任务生成失败，自定义动态任务接口执行失败：" + e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */     
/* 155 */     List<SysIdentity> identitys = taskActionCmd.getBpmIdentity(task.getNodeId());
/* 156 */     if (CollectionUtil.isEmpty(identitys)) {
/* 157 */       throw new BusinessException("动态任务生成失败，节点候选人不能为空！节点：" + task.getName());
/*     */     }
/* 159 */     return new DynamicTask((IBpmTask)task, identitys, pluginDef.getIsParallel().booleanValue());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/executer/HandleTaskCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */