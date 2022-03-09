/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.multinst.executer;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowRequestParam;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.helper.NewThreadActionUtil;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInst;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.global.multinst.def.MultInstPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.util.UserCalcPreview;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ToStringUtil;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
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
/*     */   @Autowired
/*     */   private IGroovyScriptEngine groovyScriptEngine;
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
/*  80 */     if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
/*  81 */       executeScript(pluginSession, pluginDef);
/*     */     }
/*  83 */     return null;
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
/*  94 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  95 */     boolean isStartNode = false;
/*  96 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/*  97 */     MultInst currentMultInst = null;
/*  98 */     for (MultInst mi : pluginDef.getMultInsts()) {
/*  99 */       if (bpmTask.getNodeId().equals(mi.getStartNodeKey())) {
/* 100 */         isStartNode = true;
/* 101 */         currentMultInst = mi;
/*     */       } 
/*     */     } 
/* 104 */     if (!isStartNode) {
/*     */       return;
/*     */     }
/* 107 */     BpmNodeDef nextNode = null;
/* 108 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 109 */     List<String> destinations = new ArrayList<>();
/* 110 */     String[] destinationNodes = model.getDestinations();
/*     */     
/* 112 */     if (destinationNodes != null) {
/* 113 */       if (destinationNodes.length > 1) {
/* 114 */         throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置");
/*     */       }
/* 116 */       nextNode = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), destinationNodes[0]);
/*     */     } 
/*     */     
/* 119 */     if (nextNode == null) {
/* 120 */       if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/* 121 */         throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置流程定义");
/*     */       }
/* 123 */       nextNode = bpmNodeDef.getOutcomeNodes().get(0);
/*     */     } 
/*     */     
/* 126 */     Set<String> existNode = new HashSet<>();
/* 127 */     existNode.add(bpmTask.getNodeId());
/*     */     
/* 129 */     if (!isEndNode(nextNode, currentMultInst, existNode)) {
/*     */       return;
/*     */     }
/* 132 */     List<SysIdentity> identities = model.getBpmIdentity(nextNode.getNodeId());
/* 133 */     if (CollectionUtil.isEmpty(identities)) {
/* 134 */       identities = UserCalcPreview.calcNodeUsers(nextNode, model);
/*     */     }
/* 136 */     if (identities.isEmpty()) {
/* 137 */       throw new BusinessException("任务【" + nextNode.getName() + "】预计算候选人为空，无法生成多实例任务");
/*     */     }
/* 139 */     if (identities.size() == 1) {
/* 140 */       this.LOG.debug("节点【{}】为分发节点，候选人为一个不需开启多实例", bpmTask.getName());
/*     */       return;
/*     */     } 
/* 143 */     this.LOG.debug("节点【{}】为分发节点，将会产生【{}】个分发任务", bpmTask.getName(), Integer.valueOf(identities.size()));
/*     */     
/* 145 */     BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/* 146 */     for (int i = 0; i < identities.size(); i++) {
/* 147 */       SysIdentity id = identities.get(i);
/* 148 */       destinations.add(nextNode.getNodeId());
/* 149 */       BpmContext.pushMulInstIdentities(id);
/* 150 */       if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
/* 151 */         BpmContext.pushMulInstOpTrace("t-" + i);
/*     */       } else {
/* 153 */         BpmContext.pushMulInstOpTrace(bpmTaskOpinion.getTrace() + "-" + i);
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     model.setDestinations(destinations.<String>toArray(new String[destinations.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setIdentity(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 167 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 168 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 169 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 170 */     if (bpmNodeDef.getIncomeNodes().size() != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 174 */     boolean isMulInstTask = false;
/* 175 */     MultInst currentMultInst = null;
/* 176 */     List<BpmNodeDef> preNodes = bpmNodeDef.getIncomeTaskNodes();
/* 177 */     for (MultInst mi : pluginDef.getMultInsts()) {
/* 178 */       for (BpmNodeDef nodeDef : preNodes) {
/* 179 */         if (mi.getStartNodeKey().equals(nodeDef.getNodeId())) {
/* 180 */           isMulInstTask = true;
/* 181 */           currentMultInst = mi;
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     if (!isMulInstTask) {
/*     */       return;
/*     */     }
/*     */     
/* 189 */     Set<String> existNode = new HashSet<>();
/* 190 */     if (!isEndNode(bpmNodeDef, currentMultInst, existNode)) {
/*     */       return;
/*     */     }
/* 193 */     SysIdentity identity = BpmContext.popMulInstIdentities();
/*     */     
/* 195 */     if (identity == null) {
/*     */       return;
/*     */     }
/* 198 */     this.LOG.debug("任务【ID：{}】为分发任务，将从分发候选人的栈中取出候选人：{}", bpmTask.getId(), ToStringUtil.toString(identity));
/* 199 */     model.setBpmIdentity(model.getBpmTask().getNodeId(), CollUtil.newArrayList((Object[])new SysIdentity[] { identity }));
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
/* 213 */     String currentRecoveryStrategy = "completed";
/* 214 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 215 */     boolean isEndNode = false;
/* 216 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/*     */     
/* 218 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/*     */     
/* 220 */     String[] destinationNodes = model.getDestinations();
/* 221 */     String destination = null;
/* 222 */     if (destinationNodes != null) {
/* 223 */       for (MultInst mi : pluginDef.getMultInsts()) {
/* 224 */         if (StringUtils.equals(destinationNodes[0], mi.getEndNodeKey())) {
/* 225 */           isEndNode = true;
/* 226 */           destination = destinationNodes[0];
/*     */         } 
/*     */       } 
/*     */     } else {
/* 230 */       if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/*     */         return;
/*     */       }
/* 233 */       BpmNodeDef nextNode = bpmNodeDef.getOutcomeNodes().get(0);
/* 234 */       destination = nextNode.getNodeId();
/* 235 */       for (MultInst mi : pluginDef.getMultInsts()) {
/* 236 */         if (mi.getEndNodeKey().equals(nextNode.getNodeId())) {
/* 237 */           isEndNode = true;
/* 238 */           currentRecoveryStrategy = mi.getRecoveryStrategy();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     if (!isEndNode) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 248 */     if (!BpmContext.isMulInstOpTraceEmpty()) {
/* 249 */       BpmContext.clearMulInstOpTrace();
/* 250 */       model.setDestination(destination);
/*     */     } 
/* 252 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 253 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
/*     */     
/* 255 */     if (StringUtil.isEmpty(opinion.getTrace())) {
/* 256 */       for (BpmTaskOpinion o : opinions) {
/*     */         
/* 258 */         if (o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && o.getTaskKey().equals(destination)) {
/* 259 */           model.setDestinations(new String[0]);
/*     */         }
/*     */       } 
/*     */       return;
/*     */     } 
/* 264 */     boolean b = false;
/* 265 */     for (BpmTaskOpinion o : opinions) {
/* 266 */       if (o.getId().equals(opinion.getId())) {
/*     */         continue;
/*     */       }
/* 269 */       if (!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey())) {
/*     */         continue;
/*     */       }
/* 272 */       if (o.getTrace() == null || o.getTrace().length() != opinion.getTrace().length()) {
/*     */         continue;
/*     */       }
/* 275 */       if ("所有会签用户".equals(o.getAssignInfo())) {
/*     */         continue;
/*     */       }
/* 278 */       if (o.getTrace().startsWith(opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-")))) {
/* 279 */         b = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     if ("autonomously".equals(currentRecoveryStrategy)) {
/*     */       
/* 287 */       if (checkIsFirstComplated(opinions, opinion, destination)) {
/*     */         
/* 289 */         if ("回收未完成的分发任务".equals(model.getOpinion())) {
/* 290 */           model.setDestinations(new String[0]);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 295 */         if (StringUtil.isEmpty(opinion.getTrace())) {
/*     */           return;
/*     */         }
/*     */         
/* 299 */         String trace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
/* 300 */         BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
/*     */         return;
/*     */       } 
/* 303 */     } else if (!b) {
/* 304 */       BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
/* 305 */       if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
/*     */         return;
/*     */       }
/*     */       
/* 309 */       String trace = bpmTaskOpinion.getTrace().substring(0, bpmTaskOpinion.getTrace().lastIndexOf("-"));
/* 310 */       BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
/*     */       
/*     */       return;
/*     */     } 
/* 314 */     model.setDestinations(new String[0]);
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
/* 326 */     String parentTrace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
/* 327 */     for (BpmTaskOpinion o : opinions) {
/*     */       
/* 329 */       if (o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && (
/*     */         
/* 331 */         StringUtil.isEmpty(o.getTrace()) || parentTrace.equals(o.getTrace())) && nextNode
/*     */         
/* 333 */         .equals(o.getTaskKey())) {
/* 334 */         return false;
/*     */       }
/*     */     } 
/* 337 */     return true;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 341 */     System.out.println("t-1-2-3".substring(0, "t-1-2-3".lastIndexOf("-")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEndNode(BpmNodeDef nextNodeDef, MultInst currentMultInst, Set<String> existNode) {
/* 351 */     boolean lastIsEndNode = true;
/* 352 */     if (StringUtils.equals(nextNodeDef.getNodeId(), currentMultInst.getEndNodeKey())) {
/* 353 */       return true;
/*     */     }
/* 355 */     List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
/* 356 */     for (BpmNodeDef bpmNodeDef : nodeDefs) {
/* 357 */       if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
/* 358 */         return false;
/*     */       }
/* 360 */       existNode.add(nextNodeDef.getNodeId());
/* 361 */       if (!isEndNode(bpmNodeDef, currentMultInst, existNode)) {
/* 362 */         lastIsEndNode = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 367 */     return lastIsEndNode;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkIsAllComplated(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 372 */     DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
/* 373 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 374 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 375 */     if (bpmNodeDef.getOutcomeNodes().size() != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 379 */     Boolean isAutonomously = Boolean.valueOf(false);
/*     */ 
/*     */     
/* 382 */     for (MultInst mi : pluginDef.getMultInsts()) {
/* 383 */       if (mi.getEndNodeKey().equals(bpmTask.getNodeId()) && "autonomously".equals(mi.getRecoveryStrategy())) {
/* 384 */         isAutonomously = Boolean.valueOf(true);
/*     */       }
/*     */     } 
/* 387 */     if (!isAutonomously.booleanValue())
/*     */       return; 
/* 389 */     BpmTaskOpinion currentOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
/* 390 */     List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
/*     */     
/* 392 */     List<BpmTaskOpinion> hasNotComplated = new ArrayList<>();
/*     */     
/* 394 */     StringBuilder hasNotComplatedTask = new StringBuilder();
/* 395 */     for (BpmTaskOpinion o : opinions) {
/*     */       
/* 397 */       if (!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) || StringUtil.isEmpty(o.getTrace())) {
/*     */         continue;
/*     */       }
/*     */       
/* 401 */       if (currentOpinion.getId().equals(o.getId())) {
/*     */         continue;
/*     */       }
/*     */       
/* 405 */       if (StringUtil.isEmpty(currentOpinion.getTrace()) || o.getTrace().startsWith(currentOpinion.getTrace())) {
/* 406 */         hasNotComplated.add(o);
/* 407 */         BpmTask task = (BpmTask)this.bpmTaskManager.get(o.getTaskId());
/* 408 */         hasNotComplatedTask.append("<br/>【").append(o.getTaskName()).append("】候选人:").append(task.getAssigneeNames());
/*     */       } 
/*     */     } 
/*     */     
/* 412 */     if (hasNotComplated.isEmpty())
/*     */       return; 
/* 414 */     JSONObject extendsConfig = model.getExtendConf();
/*     */     
/* 416 */     if (extendsConfig == null || !extendsConfig.containsKey("confirmRecycle") || !extendsConfig.getBoolean("confirmRecycle").booleanValue()) {
/* 417 */       hasNotComplatedTask.insert(0, "请确认，是否要回收以下未完成的分发任务:");
/* 418 */       throw new BusinessMessage(hasNotComplatedTask.toString(), BpmStatusCode.BPM_MULT_INST_CONFIRMR_ECYCLE);
/*     */     } 
/*     */     
/* 421 */     recycleTasks(hasNotComplated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recycleTasks(List<BpmTaskOpinion> hasNotComplated) {
/* 430 */     for (BpmTaskOpinion o : hasNotComplated) {
/* 431 */       FlowRequestParam param = new FlowRequestParam(o.getTaskId(), ActionType.AGREE.getKey(), new JSONObject(), "回收未完成的分发任务");
/* 432 */       DefualtTaskActionCmd newFlowCmd = new DefualtTaskActionCmd(param);
/* 433 */       newFlowCmd.setIgnoreAuthentication(Boolean.valueOf(true));
/* 434 */       newFlowCmd.setDestinations(new String[0]);
/* 435 */       NewThreadActionUtil.newThreadDoAction((ActionCmd)newFlowCmd, ContextUtil.getCurrentUser());
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
/*     */   private void executeScript(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
/* 449 */     IBpmTask bpmTask = pluginSession.getBpmTask();
/* 450 */     BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
/* 451 */     if (bpmNodeDef.getIncomeNodes().size() != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 455 */     List<BpmNodeDef> preNodes = bpmNodeDef.getIncomeTaskNodes();
/* 456 */     for (MultInst mi : pluginDef.getMultInsts()) {
/* 457 */       for (BpmNodeDef nodeDef : preNodes) {
/* 458 */         if (mi.getStartNodeKey().equals(nodeDef.getNodeId()) && StringUtil.isNotEmpty(mi.getScript()))
/* 459 */           this.groovyScriptEngine.execute(mi.getScript()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/multinst/executer/MultInstPluginExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */