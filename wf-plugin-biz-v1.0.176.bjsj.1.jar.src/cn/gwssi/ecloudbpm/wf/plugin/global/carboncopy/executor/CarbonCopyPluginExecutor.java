/*     */ package com.dstz.bpm.plugin.global.carboncopy.executor;
/*     */ 
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import com.dstz.bpm.plugin.global.carboncopy.def.BpmCarbonCopy;
/*     */ import com.dstz.bpm.plugin.global.carboncopy.def.CarbonCopyPluginDef;
/*     */ import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class CarbonCopyPluginExecutor
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, CarbonCopyPluginDef>
/*     */ {
/*  49 */   private static final Logger logger = LoggerFactory.getLogger(CarbonCopyPluginExecutor.class);
/*     */   
/*     */   @Resource
/*     */   private JmsProducer jmsProducer;
/*     */   
/*     */   @Autowired
/*     */   private IFreemarkerEngine freemarkerEngine;
/*     */   
/*     */   @Autowired
/*     */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*     */   
/*     */   @Autowired
/*     */   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*     */ 
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, CarbonCopyPluginDef pluginDef) {
/*  65 */     ccAndRecord(pluginSession, pluginDef);
/*  66 */     return null;
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
/*     */   private void ccAndRecord(BpmExecutionPluginSession pluginSession, CarbonCopyPluginDef pluginDef) {
/*     */     String nodeId, nodeName;
/*  79 */     boolean isTask = pluginSession instanceof DefaultBpmTaskPluginSession;
/*     */ 
/*     */     
/*  82 */     IBpmTask bpmTask = null;
/*  83 */     if (isTask) {
/*  84 */       bpmTask = ((DefaultBpmTaskPluginSession)pluginSession).getBpmTask();
/*  85 */       nodeId = bpmTask.getNodeId();
/*  86 */       nodeName = bpmTask.getName();
/*     */     } else {
/*  88 */       ExecutionEntity execution = (ExecutionEntity)pluginSession.getVariableScope();
/*  89 */       nodeId = execution.getActivityId();
/*  90 */       nodeName = execution.getCurrentActivityName();
/*     */     } 
/*     */ 
/*     */     
/*  94 */     List<BpmCarbonCopy> bpmCarbonCopyList = (List<BpmCarbonCopy>)pluginDef.getNodeEventCarbonCopyMap().getOrDefault(CarbonCopyPluginDef.getMapKey(nodeId, pluginSession.getEventType().getKey()), Collections.emptyList());
/*  95 */     if (bpmCarbonCopyList.isEmpty()) {
/*  96 */       logger.trace("{} {} no carbon copy configuration", nodeId, pluginSession.getEventType().getKey());
/*     */       return;
/*     */     } 
/*  99 */     Date currentTime = new Date();
/* 100 */     IUser currentUser = ContextUtil.getCurrentUser();
/* 101 */     IBpmInstance bpmInstance = pluginSession.getBpmInstance();
/* 102 */     BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)pluginSession);
/* 103 */     List<BpmCarbonCopyRecord> bpmCarbonCopyRecordList = new ArrayList<>();
/* 104 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/* 105 */     List<JmsDTO> jmsDtoList = new ArrayList<>();
/* 106 */     for (BpmCarbonCopy bpmCarbonCopy : bpmCarbonCopyList) {
/*     */       
/* 108 */       boolean matcher = (StringUtils.equals(nodeId, bpmCarbonCopy.getNodeId()) && StringUtils.equals(bpmCarbonCopy.getEvent(), pluginSession.getEventType().getKey()));
/* 109 */       List<SysIdentity> sysIdentities = Collections.emptyList();
/* 110 */       if (matcher) {
/* 111 */         sysIdentities = CollectionUtils.isEmpty(bpmCarbonCopy.getUserRules()) ? Collections.<SysIdentity>emptyList() : UserAssignRuleCalc.calcUserAssign(calcSession, bpmCarbonCopy.getUserRules(), Boolean.valueOf(true));
/*     */       }
/* 113 */       if (CollectionUtils.isNotEmpty(sysIdentities)) {
/* 114 */         String htmlContent = StringUtils.isEmpty(bpmCarbonCopy.getHtmlTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getHtmlTemplate(), pluginSession);
/* 115 */         String textContent = StringUtils.isEmpty(bpmCarbonCopy.getTextTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getTextTemplate(), pluginSession);
/* 116 */         BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/* 117 */         bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*     */         
/* 119 */         bpmCarbonCopyRecord.setInstId(bpmInstance.getId());
/* 120 */         bpmCarbonCopyRecord.setFormType(bpmCarbonCopy.getFormType());
/* 121 */         if (isTask) {
/* 122 */           bpmCarbonCopyRecord.setTaskId(bpmTask.getTaskId());
/*     */         } else {
/*     */           
/* 125 */           bpmCarbonCopyRecord.setFormType("instance");
/*     */         } 
/* 127 */         bpmCarbonCopyRecord.setNodeId(nodeId);
/* 128 */         bpmCarbonCopyRecord.setNodeName(nodeName);
/*     */         
/* 130 */         bpmCarbonCopyRecord.setEvent(bpmCarbonCopy.getEvent());
/* 131 */         bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/* 132 */         bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/* 133 */         bpmCarbonCopyRecord.setSubject(bpmInstance.getSubject());
/* 134 */         bpmCarbonCopyRecord.setContent(getContent(bpmCarbonCopy.getMsgType(), htmlContent, textContent));
/* 135 */         bpmCarbonCopyRecord.setCreateTime(currentTime);
/* 136 */         bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/* 137 */         bpmCarbonCopyRecord.setUpdateTime(currentTime);
/* 138 */         bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/* 139 */         bpmCarbonCopyRecord.setVersion(Integer.valueOf(0));
/* 140 */         bpmCarbonCopyRecord.setDelete(Boolean.FALSE);
/* 141 */         bpmCarbonCopyRecordList.add(bpmCarbonCopyRecord);
/* 142 */         addReceiveUserList(bpmCarbonCopyReceiveList, sysIdentities, bpmCarbonCopyRecord);
/* 143 */         NotifyMessage notifyMessage = new NotifyMessage(bpmCarbonCopy.getDesc(), htmlContent, currentUser, sysIdentities);
/* 144 */         notifyMessage.setTag("待阅任务");
/* 145 */         Map<String, Object> extendVars = new HashMap<>();
/* 146 */         extendVars.put("detailId", bpmInstance.getId());
/* 147 */         extendVars.put("statue", "carbon");
/* 148 */         extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 149 */         extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 150 */                 DateUtil.formatDate(new Date()), bpmInstance.getSubject() }));
/* 151 */         extendVars.put("type", "待办");
/* 152 */         extendVars.put("head", "待阅");
/* 153 */         notifyMessage.setExtendVars(extendVars);
/* 154 */         notifyMessage.setTextContent(textContent);
/* 155 */         jmsDtoList.add(new DefaultJmsDTO(bpmCarbonCopy.getMsgType(), (Serializable)notifyMessage));
/*     */       } 
/*     */     } 
/* 158 */     if (CollectionUtils.isNotEmpty(bpmCarbonCopyRecordList)) {
/* 159 */       this.bpmCarbonCopyRecordManager.createList(bpmCarbonCopyRecordList);
/* 160 */       this.bpmCarbonCopyReceiveManager.createList(bpmCarbonCopyReceiveList);
/*     */     } 
/*     */ 
/*     */     
/* 164 */     if (CollectionUtils.isNotEmpty(jmsDtoList)) {
/* 165 */       this.jmsProducer.sendToQueue(jmsDtoList);
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
/*     */   private void addReceiveUserList(List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList, List<SysIdentity> sysIdentities, BpmCarbonCopyRecord bpmCarbonCopyRecord) {
/* 177 */     for (SysIdentity sysIdentity : sysIdentities) {
/* 178 */       BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 179 */       bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/* 180 */       bpmCarbonCopyReceive.setReceiveUserId(sysIdentity.getId());
/* 181 */       bpmCarbonCopyReceive.setReceiveUserName(sysIdentity.getName());
/* 182 */       bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/* 183 */       bpmCarbonCopyReceive.setId(IdUtil.getSuid());
/* 184 */       bpmCarbonCopyReceive.setCreateTime(new Date());
/* 185 */       bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/* 186 */       bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/* 187 */       bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/* 188 */       bpmCarbonCopyReceive.setVersion(Integer.valueOf(0));
/* 189 */       bpmCarbonCopyReceive.setDelete(Boolean.FALSE);
/* 190 */       bpmCarbonCopyReceiveList.add(bpmCarbonCopyReceive);
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
/*     */   private String getContent(String msgType, String htmlContent, String textContent) {
/*     */     String content;
/* 204 */     if (StringUtils.isNotEmpty(htmlContent) && StringUtils.isNotEmpty(textContent))
/* 205 */     { switch (msgType)
/*     */       { case "email":
/* 207 */           content = htmlContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 216 */           return content; }  content = textContent; } else { content = StringUtils.defaultString(textContent, htmlContent); }  return content;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/executor/CarbonCopyPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */