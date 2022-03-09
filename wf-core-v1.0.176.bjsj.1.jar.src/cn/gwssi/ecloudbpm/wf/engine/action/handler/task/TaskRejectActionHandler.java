/*     */ package cn.gwssi.ecloudbpm.wf.engine.action.handler.task;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowRequestParam;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.IExtendTaskAction;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.WorkFlowException;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.NodeProperties;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.helper.NewThreadActionUtil;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component("taskRejectActionHandler")
/*     */ public class TaskRejectActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*  59 */   private static Logger log = LoggerFactory.getLogger(TaskRejectActionHandler.class);
/*     */   
/*     */   @Resource
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   @Resource
/*     */   BpmTaskOpinionManager taskOpinionManager;
/*     */   
/*     */   @Resource
/*     */   BpmProcessDefService processDefService;
/*     */   @Resource
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public void doActionBefore(DefualtTaskActionCmd actionModel) {
/*  73 */     taskComplatePrePluginExecute(actionModel);
/*     */     
/*  75 */     NodeProperties nodeProperties = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties();
/*     */     
/*  77 */     BpmTask rejectTask = getPreDestination(actionModel, nodeProperties);
/*     */ 
/*     */     
/*  80 */     if ("history".equals(nodeProperties.getBackUserMode())) {
/*  81 */       setHistoryApprover(actionModel, rejectTask.getNodeId());
/*     */     }
/*     */     
/*  84 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/*  85 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
/*  86 */     List<IExtendTaskAction> taskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/*  87 */     for (IExtendTaskAction taskAction : taskActions) {
/*  88 */       if (taskAction.isContainNode(task.getNodeId(), bpmProcessDef, "end")) {
/*  89 */         throw new BusinessException("当前节点为多实例任务节点不支持驳回", BpmStatusCode.CANNOT_BACK_NODE);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     for (IExtendTaskAction taskAction : taskActions) {
/*  94 */       if (taskAction.isContainNode(rejectTask.getNodeId(), bpmProcessDef, "none")) {
/*  95 */         throw new BusinessException("目标节点为多实例任务节点不支持驳回", BpmStatusCode.CANNOT_BACK_NODE);
/*     */       }
/*     */     } 
/*     */     
/*  99 */     for (IExtendTaskAction taskAction : taskActions) {
/* 100 */       if (!taskAction.canReject((IBpmTask)task, (IBpmTask)rejectTask)) {
/* 101 */         throw new BusinessException("动态多实例任务节点暂不支持驳回", BpmStatusCode.CANNOT_BACK_NODE);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     task.setPriority(Integer.valueOf(task.getPriority().intValue() + 1));
/* 109 */     actionModel.setDestination(rejectTask.getNodeId());
/* 110 */     log.info("任务【{}-{}】将驳回至节点{}", new Object[] { task.getName(), task.getId(), rejectTask.getNodeId() });
/*     */ 
/*     */     
/* 113 */     handleInclusiveTask(rejectTask.getNodeId(), nodeProperties, actionModel);
/*     */   }
/*     */   
/*     */   private void setHistoryApprover(DefualtTaskActionCmd actionModel, String destinationNode) {
/*     */     DefaultIdentity defaultIdentity;
/* 118 */     List bpmIdentity = actionModel.getBpmIdentity(destinationNode);
/* 119 */     if (CollectionUtil.isNotEmpty(bpmIdentity)) {
/*     */       return;
/*     */     }
/* 122 */     SysIdentity identitys = null;
/*     */     
/* 124 */     List<BpmTaskOpinion> taskOpinions = this.taskOpinionManager.getByInstAndNode(actionModel.getInstanceId(), destinationNode);
/* 125 */     for (BpmTaskOpinion opinion : taskOpinions) {
/* 126 */       if (StringUtil.isNotEmpty(opinion.getApprover())) {
/* 127 */         defaultIdentity = new DefaultIdentity(opinion.getApprover(), opinion.getApproverName(), "user", opinion.getTaskOrgId());
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (defaultIdentity != null) {
/* 132 */       List<SysIdentity> list = new ArrayList<>();
/* 133 */       list.add(defaultIdentity);
/* 134 */       actionModel.setBpmIdentity(destinationNode, list);
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
/*     */   private void handleInclusiveTask(String destinationNode, NodeProperties nodeProperties, DefualtTaskActionCmd actionModel) {
/* 149 */     if ("back".equals(nodeProperties.getBackMode())) {
/*     */       return;
/*     */     }
/*     */     
/* 153 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(actionModel.getDefId());
/* 154 */     JSONArray multInst = null;
/* 155 */     if (bpmProcessDef.getJson().getJSONObject("flow").getJSONObject("plugins").containsKey("multInst")) {
/* 156 */       multInst = bpmProcessDef.getJson().getJSONObject("flow").getJSONObject("plugins").getJSONArray("multInst");
/*     */     }
/*     */     
/* 159 */     BpmNodeDef destNodeDef = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), destinationNode);
/* 160 */     Map<String, Integer> nodeDepthMap = new HashMap<>();
/* 161 */     int dept = 0;
/* 162 */     handleNodeDepthMap(destNodeDef, nodeDepthMap, dept, multInst);
/*     */     
/* 164 */     BpmTask task = (BpmTask)actionModel.getBpmTask();
/*     */     
/* 166 */     int deptGap = ((Integer)nodeDepthMap.getOrDefault(task.getNodeId(), Integer.valueOf(0))).intValue() - ((Integer)nodeDepthMap.get(destinationNode)).intValue();
/*     */     
/* 168 */     if (deptGap > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 175 */       List<BpmTask> otherTasks = (List<BpmTask>)this.bpmTaskManager.getByParam(task.getActInstId(), null).stream().filter(t -> task.getId().equals(t.getId()) ? false : ((((Integer)nodeDepthMap.getOrDefault(t.getNodeId(), (V)Integer.valueOf(0))).intValue() > ((Integer)nodeDepthMap.get(destinationNode)).intValue()))).collect(Collectors.toList());
/*     */       
/* 177 */       if (otherTasks.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 181 */       JSONObject extendsConfig = actionModel.getExtendConf();
/* 182 */       if (extendsConfig == null || !extendsConfig.containsKey("confirmRecycle") || !extendsConfig.getBoolean("confirmRecycle").booleanValue()) {
/* 183 */         List<String> strs = new ArrayList<>();
/* 184 */         for (BpmTask t : otherTasks) {
/* 185 */           strs.add("【" + t.getName() + "】候选人：" + t.getAssigneeNames());
/*     */         }
/*     */         
/* 188 */         throw new BusinessMessage("驳回节点【" + destNodeDef.getName() + "】需要回收以下任务：<br/>" + StrUtil.join("<br/>", new Object[] { strs }), BpmStatusCode.BPM_MULT_INST_CONFIRMR_ECYCLE);
/*     */       } 
/*     */ 
/*     */       
/* 192 */       for (BpmTask t : otherTasks) {
/* 193 */         this.bpmTaskManager.recycleTask(t.getId(), OpinionStatus.REJECT, "驳回回收同步任务");
/* 194 */         FlowRequestParam param = new FlowRequestParam(t.getId(), ActionType.AGREE.getKey(), new JSONObject(), "驳回回收同步任务");
/* 195 */         DefualtTaskActionCmd newFlowCmd = new DefualtTaskActionCmd(param);
/* 196 */         newFlowCmd.setIgnoreAuthentication(Boolean.valueOf(true));
/* 197 */         newFlowCmd.setDestinations(new String[0]);
/* 198 */         NewThreadActionUtil.newThreadDoAction((ActionCmd)newFlowCmd, ContextUtil.getCurrentUser());
/*     */       } 
/*     */ 
/*     */       
/* 202 */       List<BpmTaskOpinion> desOpinions = this.taskOpinionManager.getByInstAndNode(actionModel.getInstanceId(), destinationNode);
/* 203 */       if (CollUtil.isNotEmpty(desOpinions)) {
/* 204 */         BpmContext.pushMulInstOpTrace(((BpmTaskOpinion)desOpinions.get(0)).getTrace());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleNodeDepthMap(BpmNodeDef node, Map<String, Integer> nodeDepthMap, int dept, JSONArray multInst) {
/* 210 */     if (dept == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     if (nodeDepthMap.containsKey(node.getNodeId())) {
/*     */       return;
/*     */     }
/* 217 */     nodeDepthMap.put(node.getNodeId(), Integer.valueOf(dept));
/* 218 */     for (BpmNodeDef n : node.getOutcomeNodes()) {
/* 219 */       if (Arrays.<NodeType>asList(new NodeType[] { NodeType.PARALLELGATEWAY, NodeType.INCLUSIVEGATEWAY }).contains(n.getType())) {
/* 220 */         if (n.getOutcomeNodes().size() == 1) {
/* 221 */           handleNodeDepthMap(n, nodeDepthMap, dept - 1, multInst); continue;
/*     */         } 
/* 223 */         handleNodeDepthMap(n, nodeDepthMap, dept + 1, multInst);
/*     */         continue;
/*     */       } 
/* 226 */       int type = 0;
/* 227 */       for (int i = 0; i < multInst.size(); i++) {
/* 228 */         JSONObject jo = multInst.getJSONObject(i);
/* 229 */         if (jo.getString("startNodeKey").equals(node.getNodeId())) {
/* 230 */           type = 1;
/*     */         }
/* 232 */         if (jo.getString("endNodeKey").equals(n.getNodeId())) {
/* 233 */           type = -1;
/*     */         }
/*     */       } 
/*     */       
/* 237 */       handleNodeDepthMap(n, nodeDepthMap, dept + type, multInst);
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
/*     */   protected BpmTask getPreDestination(DefualtTaskActionCmd actionModel, NodeProperties nodeProperties) {
/* 256 */     String destination = null;
/* 257 */     if (StringUtil.isNotEmpty(actionModel.getDestination())) {
/* 258 */       destination = actionModel.getDestination();
/*     */     }
/*     */     
/* 261 */     if (nodeProperties != null && nodeProperties.isFreeBack() && 
/* 262 */       StringUtil.isEmpty(destination)) {
/* 263 */       throw new BusinessException("退回任意环节需选择环节名称");
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (nodeProperties != null && StringUtil.isNotEmpty(nodeProperties.getBackNode()) && !nodeProperties.isFreeBack()) {
/* 268 */       destination = nodeProperties.getBackNode();
/*     */     }
/*     */     
/* 271 */     if (StringUtil.isEmpty(destination)) {
/*     */       
/* 273 */       BpmNodeDef nodeDef = this.processDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId());
/* 274 */       if (nodeDef.getIncomeNodes().size() == 1 && ((BpmNodeDef)nodeDef.getIncomeNodes().get(0)).getType() == NodeType.USERTASK) {
/* 275 */         destination = ((BpmNodeDef)nodeDef.getIncomeNodes().get(0)).getNodeId();
/*     */       }
/*     */     } 
/*     */     
/* 279 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 280 */     defaultQueryFilter.addFilter("level", "1", QueryOP.GREAT);
/* 281 */     defaultQueryFilter.addParamsFilter("taskId", actionModel.getTaskId());
/* 282 */     defaultQueryFilter.addParamsFilter("prior", "BACK");
/* 283 */     defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
/* 284 */     if (!StringUtils.isEmpty(destination)) {
/* 285 */       defaultQueryFilter.addFilter("node_id_", destination, QueryOP.EQUAL);
/*     */     }
/* 287 */     defaultQueryFilter.addFieldSort("level", "asc");
/* 288 */     defaultQueryFilter.addFieldSort("start_time_", "asc");
/* 289 */     List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter)defaultQueryFilter);
/* 290 */     if (CollectionUtil.isEmpty(bpmTaskStacks)) {
/* 291 */       throw new BusinessException("未查到执行记录，检查执行记录和流程定义节点定义");
/*     */     }
/* 293 */     BpmTaskStack bpmTaskStack = bpmTaskStacks.get(0);
/* 294 */     BpmTask bpmTask = new BpmTask();
/* 295 */     bpmTask.setId(bpmTaskStack.getTaskId());
/* 296 */     bpmTask.setNodeId(bpmTaskStack.getNodeId());
/* 297 */     bpmTask.setInstId(bpmTaskStack.getInstId());
/* 298 */     return bpmTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BpmExecutionStack getPreTaskStack(List<BpmTaskStack> stackList, String id) {
/* 309 */     String parentId = id;
/* 310 */     for (int i = stackList.size() - 1; i > -1; i--) {
/* 311 */       BpmTaskStack stack = stackList.get(i);
/*     */       
/* 313 */       if (stack.getId().equals(parentId)) {
/* 314 */         parentId = stack.getParentId();
/* 315 */         if ("userTask".equals(stack.getNodeType())) {
/* 316 */           return (BpmExecutionStack)stack;
/*     */         }
/*     */       } 
/*     */     } 
/* 320 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doActionAfter(DefualtTaskActionCmd actionModel) {
/* 326 */     NodeProperties nodeProperties = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties();
/*     */     
/* 328 */     if ("back".equals(nodeProperties.getBackMode())) {
/* 329 */       List<BpmTask> tasks = this.taskManager.getByInstIdNodeId(actionModel.getInstanceId(), actionModel.getDestination());
/* 330 */       if (CollectionUtil.isEmpty(tasks)) {
/* 331 */         throw new WorkFlowException(String.format("任务[%s]返回节点[%s]标记失败，待标记任务查找不到", new Object[] { actionModel.getDestination(), actionModel.getNodeId() }), BpmStatusCode.NO_BACK_TARGET);
/*     */       }
/* 333 */       boolean hasUpdated = false;
/* 334 */       for (BpmTask task : tasks) {
/* 335 */         if (StringUtil.isEmpty(task.getBackNode()) && StringUtil.isNotEmpty(task.getActInstId()) && StringUtil.isNotEmpty(task.getTaskId())) {
/* 336 */           if (hasUpdated) {
/* 337 */             throw new WorkFlowException("任务返回节点标记失败，期望查找一条，但是出现多条", BpmStatusCode.NO_BACK_TARGET);
/*     */           }
/*     */           
/* 340 */           task.setBackNode(actionModel.getNodeId());
/* 341 */           this.taskManager.update(task);
/* 342 */           hasUpdated = true;
/*     */         } 
/*     */       } 
/* 345 */       if (!hasUpdated) {
/* 346 */         throw new WorkFlowException("任务返回节点标记失败，待标记任务查找不到", BpmStatusCode.NO_BACK_TARGET);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void taskComplatePrePluginExecute(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 357 */     return ActionType.REJECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 362 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 368 */     return "/bpm/task/taskOpinionDialog.html";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/task/TaskRejectActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */