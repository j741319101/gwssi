/*     */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.EventType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ScriptType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmVariableDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.test.BpmSystemTestService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class InstanceStartEventListener
/*     */   extends AbstractInstanceListener
/*     */ {
/*     */   @Resource
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionMananger;
/*     */   @Resource
/*     */   private BpmSystemTestService bpmSystemTestService;
/*     */   
/*     */   public EventType getBeforeTriggerEventType() {
/*  64 */     return EventType.START_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public EventType getAfterTriggerEventType() {
/*  69 */     return EventType.START_POST_EVENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforePluginExecute(InstanceActionCmd instanceActionModel) {
/*  74 */     this.LOG.debug("流程实例【{}】执行启动过程 instanceID:[{}]", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
/*     */     
/*  76 */     Map<String, Object> actionVariables = instanceActionModel.getActionVariables();
/*  77 */     if (CollectionUtil.isEmpty(actionVariables)) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     for (String key : actionVariables.keySet()) {
/*  82 */       instanceActionModel.addVariable(key, actionVariables.get(key));
/*     */     }
/*  84 */     this.LOG.debug("设置流程变量【{}】", actionVariables.keySet().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerExecute(InstanceActionCmd instanceActionModel) {
/*  90 */     this.bpmTaskOpinionManager.createOpinionByInstance(instanceActionModel, true);
/*     */     
/*  92 */     handleInstanceSubject((DefaultInstanceActionCmd)instanceActionModel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPluginExecute(InstanceActionCmd instanceActionModel) {
/*  97 */     this.LOG.debug("流程实例【{}】完成创建过程   instanceID：{}", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   protected ScriptType getScriptType() {
/* 102 */     return ScriptType.START;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InstanceActionCmd getInstanceActionModel(ExecutionEntity executionEntity) {
/* 108 */     if (StringUtil.isNotEmpty(executionEntity.getActivityId()) && (executionEntity
/* 109 */       .getActivityId().startsWith("StartSignal") || executionEntity
/* 110 */       .getActivityId().startsWith("StartTimer"))) {
/* 111 */       initActionModelByActivitiEvent(executionEntity);
/*     */     }
/*     */     
/* 114 */     ActionCmd actionCmd = BpmContext.getActionModel();
/* 115 */     handlerSubProcess(executionEntity, actionCmd);
/*     */     
/* 117 */     DefaultInstanceActionCmd actionModel = (DefaultInstanceActionCmd)BpmContext.getActionModel();
/* 118 */     actionModel.setExecutionEntity(executionEntity);
/* 119 */     actionModel.setApproveOrgId(actionModel.getApproveOrgId());
/* 120 */     BpmInstance instance = (BpmInstance)actionModel.getBpmInstance();
/* 121 */     if (StringUtil.isEmpty(instance.getActInstId())) {
/* 122 */       instance.setActDefId(executionEntity.getProcessDefinitionId());
/* 123 */       instance.setActInstId(executionEntity.getProcessInstanceId());
/*     */     } 
/*     */     
/* 126 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 127 */     List<BpmVariableDef> bpmVariableDefs = bpmProcessDef.getVariableList();
/* 128 */     Map<String, Object> variables = new HashMap<>();
/* 129 */     for (BpmVariableDef bpmVariableDef : bpmVariableDefs) {
/* 130 */       variables.put(bpmVariableDef.getKey(), bpmVariableDef.getDefaultVal());
/*     */     }
/* 132 */     actionModel.setActionVariables(variables);
/* 133 */     return (InstanceActionCmd)actionModel;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleInstanceSubject(DefaultInstanceActionCmd data) {
/* 138 */     BpmInstance instance = (BpmInstance)data.getBpmInstance();
/*     */     
/* 140 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 141 */     String subjectRule = processDef.getExtProperties().getSubjectRule();
/*     */     
/* 143 */     if (StringUtil.isEmpty(subjectRule))
/*     */       return; 
/* 145 */     Map<String, Object> ruleVariables = new HashMap<>();
/* 146 */     ruleVariables.put("title", processDef.getName());
/* 147 */     IUser user = ContextUtil.getCurrentUser();
/* 148 */     if (user != null) {
/* 149 */       ruleVariables.put("startorName", user.getFullname());
/*     */     } else {
/* 151 */       ruleVariables.put("startorName", "系统");
/*     */     } 
/* 153 */     ruleVariables.put("startDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
/* 154 */     ruleVariables.put("startTime", DateUtil.now());
/* 155 */     ruleVariables.putAll(data.getVariables());
/*     */ 
/*     */     
/* 158 */     Map<String, IBusinessData> boMap = data.getBizDataMap();
/* 159 */     if (CollectionUtil.isNotEmpty(boMap)) {
/* 160 */       Set<String> bocodes = boMap.keySet();
/* 161 */       for (String bocode : bocodes) {
/* 162 */         IBusinessData bizData = boMap.get(bocode);
/*     */         
/* 164 */         Map<String, Object> dataMap = bizData.getData();
/* 165 */         for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
/* 166 */           ruleVariables.put(bocode + "." + (String)entry.getKey(), entry.getValue());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     String subject = getTitleByVariables(subjectRule, ruleVariables);
/*     */     
/* 173 */     instance.setSubject(subject);
/* 174 */     this.LOG.debug("更新流程标题:{}", subject);
/*     */   }
/*     */   
/*     */   private String getTitleByVariables(String subject, Map<String, Object> variables) {
/* 178 */     if (StringUtils.isEmpty(subject))
/* 179 */       return ""; 
/* 180 */     Pattern regex = Pattern.compile("\\{(.*?)\\}", 98);
/* 181 */     Matcher matcher = regex.matcher(subject);
/* 182 */     while (matcher.find()) {
/* 183 */       String tag = matcher.group(0);
/* 184 */       String rule = matcher.group(1);
/* 185 */       String[] aryRule = rule.split(":");
/* 186 */       String name = "";
/* 187 */       if (aryRule.length == 1) {
/* 188 */         name = rule;
/*     */       } else {
/* 190 */         name = aryRule[1];
/*     */       } 
/* 192 */       if (variables.containsKey(name)) {
/* 193 */         Object obj = variables.get(name);
/* 194 */         if (obj != null) {
/*     */           try {
/* 196 */             subject = subject.replace(tag, obj.toString());
/* 197 */           } catch (Exception e) {
/* 198 */             subject = subject.replace(tag, "");
/*     */           }  continue;
/*     */         } 
/* 201 */         subject = subject.replace(tag, "");
/*     */         continue;
/*     */       } 
/* 204 */       subject = subject.replace(tag, "");
/*     */     } 
/*     */     
/* 207 */     return subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handlerSubProcess(ExecutionEntity excutionEntity, ActionCmd preAction) {
/*     */     BaseActionCmd baseActionCmd;
/* 217 */     String preActionDefKey = preAction.getBpmInstance().getDefKey();
/*     */     
/* 219 */     JSONArray startSubProcessKey = null;
/* 220 */     JSONObject extendConf = ((BaseActionCmd)preAction).getExtendConf();
/* 221 */     if (extendConf != null) {
/* 222 */       startSubProcessKey = extendConf.getJSONArray("StartSubProcess");
/*     */     }
/* 224 */     if (preAction instanceof InstanceActionCmd) {
/* 225 */       if (!excutionEntity.getProcessDefinitionKey().equals(preActionDefKey)) {
/* 226 */         if (startSubProcessKey == null || !startSubProcessKey.contains(excutionEntity.getProcessDefinitionKey())) {
/* 227 */           throw new BusinessException("流程启动失败，错误的线程数据！", BpmStatusCode.ACTIONCMD_ERROR);
/*     */         }
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 234 */     ExecutionEntity callActivityNode = excutionEntity.getSuperExecution();
/*     */     
/* 236 */     if (preAction instanceof cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd && (
/* 237 */       callActivityNode == null || !preAction.getBpmInstance().getActInstId().equals(callActivityNode.getProcessInstanceId()))) {
/* 238 */       baseActionCmd = BpmContext.getActionModel(callActivityNode.getProcessInstanceId());
/* 239 */       if (baseActionCmd == null) {
/* 240 */         throw new BusinessException(BpmStatusCode.ACTIONCMD_ERROR);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     BpmDefinition subDefinition = this.bpmDefinitionMananger.getByKey(excutionEntity.getProcessDefinitionKey());
/* 250 */     BpmInstance subInstance = this.bpmInstanceManager.genInstanceByDefinition((IBpmDefinition)subDefinition);
/*     */     
/* 252 */     subInstance.setActInstId(excutionEntity.getProcessInstanceId());
/* 253 */     if (!baseActionCmd.getBpmInstance().getActInstId().equals(callActivityNode.getProcessInstanceId())) {
/* 254 */       subInstance.setParentInstId(baseActionCmd.getBpmInstance().getParentInstId());
/*     */     } else {
/* 256 */       subInstance.setParentInstId(baseActionCmd.getBpmInstance().getId());
/*     */     } 
/* 258 */     subInstance.setSuperNodeId(callActivityNode.getActivityId());
/*     */     
/* 260 */     DefaultInstanceActionCmd startAction = new DefaultInstanceActionCmd();
/* 261 */     startAction.setBpmDefinition((IBpmDefinition)subDefinition);
/* 262 */     startAction.setBpmInstance((IBpmInstance)subInstance);
/* 263 */     startAction.setExecutionEntity(excutionEntity);
/* 264 */     startAction.setBizDataMap(baseActionCmd.getBizDataMap());
/* 265 */     startAction.setActionName(ActionType.START.getKey());
/* 266 */     startAction.setBpmIdentities(baseActionCmd.getBpmIdentities());
/* 267 */     startAction.setDynamicBpmIdentity(baseActionCmd.getDynamicBpmIdentity());
/* 268 */     startAction.setDynamicSubmitTaskName(baseActionCmd.getDynamicSubmitTaskName());
/* 269 */     startAction.setExecutionStack(baseActionCmd.getExecutionStack());
/* 270 */     startAction.setApproveOrgId(baseActionCmd.getApproveOrgId());
/*     */     
/* 272 */     BpmContext.setActionModel((ActionCmd)startAction);
/*     */ 
/*     */     
/* 275 */     handleInstanceSubject(startAction);
/* 276 */     subInstance.setCreateOrgId(startAction.getApproveOrgId());
/* 277 */     this.bpmSystemTestService.saveBpmInstance((IBpmInstance)subInstance);
/* 278 */     this.bpmInstanceManager.create(subInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initActionModelByActivitiEvent(ExecutionEntity executionEntity) {
/* 283 */     if (BpmContext.getActionModel() != null) {
/* 284 */       IBpmInstance iBpmInstance = BpmContext.getActionModel().getBpmInstance();
/* 285 */       if ((iBpmInstance != null && 
/* 286 */         StringUtils.equals(iBpmInstance.getActInstId(), executionEntity.getProcessInstanceId())) || 
/* 287 */         StringUtils.equals(BpmContext.getActionModel().getActionName(), ActionType.START.getKey())) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 294 */     BpmInstance instance = this.bpmInstanceManager.createInstanceByExecution(executionEntity);
/*     */     
/* 296 */     DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd();
/*     */     
/* 298 */     instanceCmd.setActionName(ActionType.START.getKey());
/*     */     
/* 300 */     instanceCmd.setBpmInstance((IBpmInstance)instance);
/* 301 */     instanceCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(instance.getDefId()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     instanceCmd.setOpinion("流程事件启动");
/*     */ 
/*     */ 
/*     */     
/* 310 */     BpmContext.setActionModel((ActionCmd)instanceCmd);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/InstanceStartEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */