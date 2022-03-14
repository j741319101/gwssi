/*     */ package com.dstz.bpm.engine.action.handler.instance;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.test.BpmSystemTestService;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.AbsActionHandler;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class InstanceSaveActionHandler
/*     */   extends AbsActionHandler<DefaultInstanceActionCmd>
/*     */ {
/*     */   @Resource
/*     */   private BpmSystemTestService bpmSystemTestService;
/*     */   
/*     */   protected void doAction(DefaultInstanceActionCmd model) {
/*  50 */     BpmInstance instance = (BpmInstance)model.getBpmInstance();
/*  51 */     instance.setStatus(InstanceStatus.STATUS_DRAFT.getKey());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean prepareActionDatas(DefaultInstanceActionCmd data) {
/*  62 */     data.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(data.getDefId()));
/*     */     
/*  64 */     getInstance(data);
/*     */     
/*  66 */     parserBusinessData((BaseActionCmd)data);
/*     */     
/*  68 */     boolean isStop = handelFormInit((BaseActionCmd)data, this.bpmProcessDefService.getStartEvent(data.getDefId()));
/*  69 */     return isStop;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefaultInstanceActionCmd actionModel) {
/*  75 */     persistenceInstance(actionModel);
/*     */   }
/*     */   
/*     */   protected void persistenceInstance(DefaultInstanceActionCmd actionModel) {
/*  79 */     BpmInstance instance = (BpmInstance)actionModel.getBpmInstance();
/*     */     
/*  81 */     if (instance.hasCreate().booleanValue()) {
/*  82 */       handleInstanceSubject(actionModel);
/*  83 */       this.bpmInstanceManager.update(instance);
/*     */     } else {
/*  85 */       handleInstanceSubject(actionModel);
/*  86 */       this.bpmSystemTestService.saveBpmInstance((IBpmInstance)instance);
/*  87 */       this.bpmInstanceManager.create(instance);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void getInstance(DefaultInstanceActionCmd intanceCmdData) {
/*  92 */     String instId = intanceCmdData.getInstanceId();
/*     */     
/*  94 */     BpmInstance instance = null;
/*  95 */     if (StringUtil.isNotEmpty(instId)) {
/*  96 */       instance = (BpmInstance)this.bpmInstanceManager.get(instId);
/*  97 */       if (StringUtil.isNotEmpty(instance.getActInstId())) {
/*  98 */         throw new BusinessException("草稿已经启动，请勿多次启动该草稿！");
/*     */       }
/*     */     } 
/*     */     
/* 102 */     if (instance == null) {
/* 103 */       IBpmDefinition bpmDefinition = intanceCmdData.getBpmDefinition();
/* 104 */       instance = this.bpmInstanceManager.genInstanceByDefinition(bpmDefinition);
/*     */     } 
/* 106 */     instance.setCreateOrgId(intanceCmdData.getApproveOrgId());
/* 107 */     intanceCmdData.setBpmInstance((IBpmInstance)instance);
/*     */   }
/*     */   private void handleInstanceSubject(DefaultInstanceActionCmd data) {
/* 110 */     BpmInstance instance = (BpmInstance)data.getBpmInstance();
/* 111 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 112 */     String subjectRule = processDef.getExtProperties().getSubjectRule();
/*     */     
/* 114 */     if (StringUtil.isEmpty(subjectRule))
/*     */       return; 
/* 116 */     Map<String, Object> ruleVariables = new HashMap<>();
/* 117 */     ruleVariables.put("title", processDef.getName());
/* 118 */     IUser user = ContextUtil.getCurrentUser();
/* 119 */     if (user != null) {
/* 120 */       ruleVariables.put("startorName", user.getFullname());
/*     */     } else {
/* 122 */       ruleVariables.put("startorName", "系统");
/*     */     } 
/* 124 */     ruleVariables.put("startDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
/* 125 */     ruleVariables.put("startTime", DateUtil.now());
/* 126 */     ruleVariables.putAll(data.getVariables());
/*     */ 
/*     */     
/* 129 */     Map<String, IBusinessData> boMap = data.getBizDataMap();
/* 130 */     if (CollectionUtil.isNotEmpty(boMap)) {
/* 131 */       Set<String> bocodes = boMap.keySet();
/* 132 */       for (String bocode : bocodes) {
/* 133 */         IBusinessData bizData = boMap.get(bocode);
/*     */         
/* 135 */         Map<String, Object> dataMap = bizData.getData();
/* 136 */         for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
/* 137 */           ruleVariables.put(bocode + "." + (String)entry.getKey(), entry.getValue());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     String subject = getTitleByVariables(subjectRule, ruleVariables);
/*     */     
/* 144 */     instance.setSubject(subject);
/* 145 */     this.LOG.debug("更新流程标题:{}", subject);
/*     */   }
/*     */   private String getTitleByVariables(String subject, Map<String, Object> variables) {
/* 148 */     if (StringUtils.isEmpty(subject))
/* 149 */       return ""; 
/* 150 */     Pattern regex = Pattern.compile("\\{(.*?)\\}", 98);
/* 151 */     Matcher matcher = regex.matcher(subject);
/* 152 */     while (matcher.find()) {
/* 153 */       String tag = matcher.group(0);
/* 154 */       String rule = matcher.group(1);
/* 155 */       String[] aryRule = rule.split(":");
/* 156 */       String name = "";
/* 157 */       if (aryRule.length == 1) {
/* 158 */         name = rule;
/*     */       } else {
/* 160 */         name = aryRule[1];
/*     */       } 
/* 162 */       if (variables.containsKey(name)) {
/* 163 */         Object obj = variables.get(name);
/* 164 */         if (obj != null) {
/*     */           try {
/* 166 */             subject = subject.replace(tag, obj.toString());
/* 167 */           } catch (Exception e) {
/* 168 */             subject = subject.replace(tag, "");
/*     */           }  continue;
/*     */         } 
/* 171 */         subject = subject.replace(tag, "");
/*     */         continue;
/*     */       } 
/* 174 */       subject = subject.replace(tag, "");
/*     */     } 
/*     */     
/* 177 */     return subject;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 182 */     return ActionType.DRAFT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 188 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 194 */     if (nodeDef.getType() == NodeType.START) {
/* 195 */       return Boolean.valueOf(true);
/*     */     }
/* 197 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 209 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/handler/instance/InstanceSaveActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */