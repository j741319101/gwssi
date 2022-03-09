/*     */ package com.dstz.bpm.plugin.global.multinst.executer;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ToStringUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.OpinionStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.helper.NewThreadActionUtil;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.global.multinst.def.MultInst;
/*     */ import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
/*     */ import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class MultInstPluginExecuter
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, MultInstPluginDef>
/*     */ {
/*     */   @Autowired
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Autowired
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   private static final String recycleOpinion = "回收未完成的分发任务";
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession session, MultInstPluginDef pluginDef) {
/*  62 */     if (!(session instanceof DefaultBpmTaskPluginSession) || 
/*  63 */       StringUtils.equals(String.valueOf(session.get("submitActionName")), ActionType.REJECT.getKey()) || 
/*  64 */       StringUtils.equals(String.valueOf(session.get("submitActionName")), ActionType.REJECT2START.getKey())) {
/*  65 */       return null;
/*     */     }
/*  67 */     DefaultBpmTaskPluginSession pluginSession = (DefaultBpmTaskPluginSession)session;
/*     */     
/*  69 */     if (pluginSession.getEventType() == EventType.TASK_PRE_COMPLETE_EVENT) {
/*  70 */       startMulInst(pluginSession, pluginDef);
/*  71 */       endMulInst(pluginSession, pluginDef);
/*     */       
/*  73 */       checkIsAllComplated(pluginSession, pluginDef);
/*     */     } 
/*     */     
/*  76 */     if (pluginSession.getEventType() == EventType.TASK_CREATE_EVENT) {
/*  77 */       setIdentity(pluginSession, pluginDef);
/*     */     }
/*     */     
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startMulInst(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/*  91 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  92 */     boolean isStartNode = false;
/*  93 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/*  94 */     MultInst currentMultInst = null;
/*  95 */     for (MultInst mi : pluginDef.getMultInsts()) {
/*  96 */       if (bpmTask.getNodeId().equals(mi.getStartNodeKey())) {
/*  97 */         isStartNode = true;
/*  98 */         currentMultInst = mi;
/*     */       } 
/*     */     } 
/* 101 */     if (!isStartNode) {
/*     */       return;
/*     */     }
/* 104 */     BpmNodeDef nextNode = null;
/* 105 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 106 */     List<String> destinations = new ArrayList<>();
/* 107 */     String[] destinationNodes = model.getDestinations();
/*     */     
/* 109 */     if (destinationNodes != null) {
/* 110 */       if (destinationNodes.length > 1) {
/* 111 */         throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置");
/*     */       }
/* 113 */       nextNode = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), destinationNodes[0]);
/*     */     } 
/*     */     
/* 116 */     if (nextNode == null) {
/* 117 */       if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/* 118 */         throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置流程定义");
/*     */       }
/* 120 */       nextNode = bpmNodeDef.getOutcomeNodes().get(0);
/*     */     } 
/*     */     
/* 123 */     Set<String> existNode = new HashSet<>();
/* 124 */     existNode.add(bpmTask.getNodeId());
/*     */     
/* 126 */     if (!isEndNode(nextNode, currentMultInst, existNode)) {
/*     */       return;
/*     */     }
/* 129 */     List<SysIdentity> identities = model.getBpmIdentity(nextNode.getNodeId());
/* 130 */     if (CollectionUtil.isEmpty(identities)) {
/* 131 */       identities = UserCalcPreview.calcNodeUsers(nextNode, model);
/*     */     }
/* 133 */     if (identities.isEmpty()) {
/* 134 */       throw new BusinessException("任务【" + nextNode.getName() + "】预计算候选人为空，无法生成多实例任务");
/*     */     }
/* 136 */     if (identities.size() == 1) {
/* 137 */       this.LOG.debug("节点【{}】为分发节点，候选人为一个不需开启多实例", bpmTask.getName());
/*     */       return;
/*     */     } 
/* 140 */     this.LOG.debug("节点【{}】为分发节点，将会产生【{}】个分发任务", bpmTask.getName(), Integer.valueOf(identities.size()));
/*     */     
/* 142 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/* 143 */     for (int i = 0; i < identities.size(); i++) {
/* 144 */       SysIdentity id = identities.get(i);
/* 145 */       destinations.add(nextNode.getNodeId());
/* 146 */       BpmContext.pushMulInstIdentities(id);
/* 147 */       if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
/* 148 */         BpmContext.pushMulInstOpTrace("t-" + i);
/*     */       } else {
/* 150 */         BpmContext.pushMulInstOpTrace(bpmTaskOpinion.getTrace() + "-" + i);
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     model.setDestinations(destinations.<String>toArray(new String[destinations.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setIdentity(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 164 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 165 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 166 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 167 */     if (bpmNodeDef.getIncomeNodes().size() != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 171 */     boolean isMulInstTask = false;
/* 172 */     MultInst currentMultInst = null;
/* 173 */     List<BpmNodeDef> preNodes = bpmNodeDef.getIncomeTaskNodes();
/* 174 */     for (MultInst mi : pluginDef.getMultInsts()) {
/* 175 */       for (BpmNodeDef nodeDef : preNodes) {
/* 176 */         if (mi.getStartNodeKey().equals(nodeDef.getNodeId())) {
/* 177 */           isMulInstTask = true;
/* 178 */           currentMultInst = mi;
/*     */         } 
/*     */       } 
/*     */     } 
/* 182 */     if (!isMulInstTask) {
/*     */       return;
/*     */     }
/*     */     
/* 186 */     Set<String> existNode = new HashSet<>();
/* 187 */     if (!isEndNode(bpmNodeDef, currentMultInst, existNode)) {
/*     */       return;
/*     */     }
/* 190 */     SysIdentity identity = BpmContext.popMulInstIdentities();
/*     */     
/* 192 */     if (identity == null) {
/*     */       return;
/*     */     }
/* 195 */     this.LOG.debug("任务【ID：{}】为分发任务，将从分发候选人的栈中取出候选人：{}", bpmTask.getId(), ToStringUtil.toString(identity));
/* 196 */     model.setBpmIdentity(model.getBpmTask().getNodeId(), CollUtil.newArrayList((Object[])new SysIdentity[] { identity }));
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
/*     */   private void endMulInst(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 210 */     String currentRecoveryStrategy = "completed";
/* 211 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 212 */     boolean isEndNode = false;
/* 213 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/*     */     
/* 215 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/*     */     
/* 217 */     String[] destinationNodes = model.getDestinations();
/* 218 */     String destination = null;
/* 219 */     if (destinationNodes != null) {
/* 220 */       for (MultInst mi : pluginDef.getMultInsts()) {
/* 221 */         if (StringUtils.equals(destinationNodes[0], mi.getEndNodeKey())) {
/* 222 */           isEndNode = true;
/* 223 */           destination = destinationNodes[0];
/*     */         } 
/*     */       } 
/*     */     } else {
/* 227 */       if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/*     */         return;
/*     */       }
/* 230 */       BpmNodeDef nextNode = bpmNodeDef.getOutcomeNodes().get(0);
/* 231 */       destination = nextNode.getNodeId();
/* 232 */       for (MultInst mi : pluginDef.getMultInsts()) {
/* 233 */         if (mi.getEndNodeKey().equals(nextNode.getNodeId())) {
/* 234 */           isEndNode = true;
/* 235 */           currentRecoveryStrategy = mi.getRecoveryStrategy();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     if (!isEndNode) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 245 */     if (!BpmContext.isMulInstOpTraceEmpty()) {
/* 246 */       BpmContext.clearMulInstOpTrace();
/* 247 */       model.setDestination(destination);
/*     */     } 
/* 249 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 250 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
/*     */     
/* 252 */     if (StringUtil.isEmpty(opinion.getTrace())) {
/* 253 */       for (BpmTaskOpinion o : opinions) {
/*     */         
/* 255 */         if (o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && o.getTaskKey().equals(destination)) {
/* 256 */           model.setDestinations(new String[0]);
/*     */         }
/*     */       } 
/*     */       return;
/*     */     } 
/* 261 */     boolean b = false;
/* 262 */     for (BpmTaskOpinion o : opinions) {
/* 263 */       if (o.getId().equals(opinion.getId())) {
/*     */         continue;
/*     */       }
/* 266 */       if (!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey())) {
/*     */         continue;
/*     */       }
/* 269 */       if (o.getTrace() == null || o.getTrace().length() != opinion.getTrace().length()) {
/*     */         continue;
/*     */       }
/* 272 */       if (o.getTrace().startsWith(opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-")))) {
/* 273 */         b = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 279 */     if ("autonomously".equals(currentRecoveryStrategy)) {
/*     */       
/* 281 */       if (checkIsFirstComplated(opinions, opinion, destination)) {
/*     */         
/* 283 */         if ("回收未完成的分发任务".equals(model.getOpinion())) {
/* 284 */           model.setDestinations(new String[0]);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 289 */         if (StringUtil.isEmpty(opinion.getTrace())) {
/*     */           return;
/*     */         }
/*     */         
/* 293 */         String trace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
/* 294 */         BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
/*     */         return;
/*     */       } 
/* 297 */     } else if (!b) {
/* 298 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/* 299 */       if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
/*     */         return;
/*     */       }
/*     */       
/* 303 */       String trace = bpmTaskOpinion.getTrace().substring(0, bpmTaskOpinion.getTrace().lastIndexOf("-"));
/* 304 */       BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
/*     */       
/*     */       return;
/*     */     } 
/* 308 */     model.setDestinations(new String[0]);
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
/*     */   private boolean checkIsFirstComplated(List<BpmTaskOpinion> opinions, BpmTaskOpinion opinion, String nextNode) {
/* 320 */     String parentTrace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
/* 321 */     for (BpmTaskOpinion o : opinions) {
/*     */       
/* 323 */       if (o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && (
/*     */         
/* 325 */         StringUtil.isEmpty(o.getTrace()) || parentTrace.equals(o.getTrace())) && nextNode
/*     */         
/* 327 */         .equals(o.getTaskKey())) {
/* 328 */         return false;
/*     */       }
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 335 */     System.out.println("t-1-2-3".substring(0, "t-1-2-3".lastIndexOf("-")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEndNode(BpmNodeDef nextNodeDef, MultInst currentMultInst, Set<String> existNode) {
/* 345 */     boolean lastIsEndNode = true;
/* 346 */     if (StringUtils.equals(nextNodeDef.getNodeId(), currentMultInst.getEndNodeKey())) {
/* 347 */       return true;
/*     */     }
/* 349 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/* 350 */     for (BpmNodeDef bpmNodeDef : nodeDefs) {
/* 351 */       if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
/* 352 */         return false;
/*     */       }
/* 354 */       existNode.add(nextNodeDef.getNodeId());
/* 355 */       if (!isEndNode(bpmNodeDef, currentMultInst, existNode)) {
/* 356 */         lastIsEndNode = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 361 */     return lastIsEndNode;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkIsAllComplated(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 366 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 367 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 368 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 369 */     if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 373 */     Boolean isAutonomously = Boolean.valueOf(false);
/*     */ 
/*     */     
/* 376 */     for (MultInst mi : pluginDef.getMultInsts()) {
/* 377 */       if (mi.getEndNodeKey().equals(bpmTask.getNodeId()) && "autonomously".equals(mi.getRecoveryStrategy())) {
/* 378 */         isAutonomously = Boolean.valueOf(true);
/*     */       }
/*     */     } 
/* 381 */     if (!isAutonomously.booleanValue())
/*     */       return; 
/* 383 */     BpmTaskOpinion currentOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 384 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
/*     */     
/* 386 */     List<BpmTaskOpinion> hasNotComplated = new ArrayList<>();
/*     */     
/* 388 */     StringBuilder hasNotComplatedTask = new StringBuilder();
/* 389 */     for (BpmTaskOpinion o : opinions) {
/*     */       
/* 391 */       if (!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) || StringUtil.isEmpty(o.getTrace())) {
/*     */         continue;
/*     */       }
/*     */       
/* 395 */       if (currentOpinion.getId().equals(o.getId())) {
/*     */         continue;
/*     */       }
/*     */       
/* 399 */       if (StringUtil.isEmpty(currentOpinion.getTrace()) || o.getTrace().startsWith(currentOpinion.getTrace())) {
/* 400 */         hasNotComplated.add(o);
/* 401 */         BpmTask task = (BpmTask)this.bpmTaskManager.get(o.getTaskId());
/* 402 */         hasNotComplatedTask.append("<br/>【").append(o.getTaskName()).append("】候选人:").append(task.getAssigneeNames());
/*     */       } 
/*     */     } 
/*     */     
/* 406 */     if (hasNotComplated.isEmpty())
/*     */       return; 
/* 408 */     JSONObject extendsConfig = model.getExtendConf();
/*     */     
/* 410 */     if (extendsConfig == null || !extendsConfig.containsKey("confirmRecycle") || !extendsConfig.getBoolean("confirmRecycle").booleanValue()) {
/* 411 */       hasNotComplatedTask.insert(0, "请确认，是否要回收以下未完成的分发任务:");
/* 412 */       throw new BusinessMessage(hasNotComplatedTask.toString(), BpmStatusCode.BPM_MULT_INST_CONFIRMR_ECYCLE);
/*     */     } 
/*     */     
/* 415 */     recycleTasks(hasNotComplated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recycleTasks(List<BpmTaskOpinion> hasNotComplated) {
/* 424 */     for (BpmTaskOpinion o : hasNotComplated) {
/* 425 */       FlowRequestParam param = new FlowRequestParam(o.getTaskId(), ActionType.AGREE.getKey(), new JSONObject(), "回收未完成的分发任务");
/* 426 */       DefualtTaskActionCmd newFlowCmd = new DefualtTaskActionCmd(param);
/* 427 */       newFlowCmd.setIgnoreAuthentication(Boolean.valueOf(true));
/* 428 */       NewThreadActionUtil.newThreadDoAction((ActionCmd)newFlowCmd, ContextUtil.getCurrentUser());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/multinst/executer/MultInstPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */