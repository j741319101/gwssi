/*     */ package com.dstz.bpm.plugin.global.carboncopy.executor;
/*     */ 
/*     */ import com.dstz.base.core.id.IdUtil;
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
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class CarbonCopyPluginExecutor
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, CarbonCopyPluginDef>
/*     */ {
/*  50 */   private static final Logger logger = LoggerFactory.getLogger(CarbonCopyPluginExecutor.class);
/*     */   
/*     */   @Autowired
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
/*  66 */     ccAndRecord(pluginSession, pluginDef);
/*  67 */     return null;
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
/*  80 */     boolean isTask = pluginSession instanceof DefaultBpmTaskPluginSession;
/*     */ 
/*     */     
/*  83 */     IBpmTask bpmTask = null;
/*  84 */     if (isTask) {
/*  85 */       bpmTask = ((DefaultBpmTaskPluginSession)pluginSession).getBpmTask();
/*  86 */       nodeId = bpmTask.getNodeId();
/*  87 */       nodeName = bpmTask.getName();
/*     */     } else {
/*  89 */       ExecutionEntity execution = (ExecutionEntity)pluginSession.getVariableScope();
/*  90 */       nodeId = execution.getActivityId();
/*  91 */       nodeName = execution.getCurrentActivityName();
/*     */     } 
/*     */ 
/*     */     
/*  95 */     List<BpmCarbonCopy> bpmCarbonCopyList = (List<BpmCarbonCopy>)pluginDef.getNodeEventCarbonCopyMap().getOrDefault(CarbonCopyPluginDef.getMapKey(nodeId, pluginSession.getEventType().getKey()), Collections.emptyList());
/*  96 */     if (bpmCarbonCopyList.isEmpty()) {
/*  97 */       logger.trace("{} {} no carbon copy configuration", nodeId, pluginSession.getEventType().getKey());
/*     */       return;
/*     */     } 
/* 100 */     Date currentTime = new Date();
/* 101 */     IUser currentUser = ContextUtil.getCurrentUser();
/* 102 */     IBpmInstance bpmInstance = pluginSession.getBpmInstance();
/* 103 */     BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)pluginSession);
/* 104 */     List<BpmCarbonCopyRecord> bpmCarbonCopyRecordList = new ArrayList<>();
/* 105 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/* 106 */     List<JmsDTO> jmsDtoList = new ArrayList<>();
/* 107 */     for (BpmCarbonCopy bpmCarbonCopy : bpmCarbonCopyList) {
/*     */       
/* 109 */       boolean matcher = (StringUtils.equals(nodeId, bpmCarbonCopy.getNodeId()) && StringUtils.equals(bpmCarbonCopy.getEvent(), pluginSession.getEventType().getKey()));
/* 110 */       List<SysIdentity> sysIdentities = Collections.emptyList();
/* 111 */       if (matcher) {
/* 112 */         sysIdentities = CollectionUtils.isEmpty(bpmCarbonCopy.getUserRules()) ? Collections.<SysIdentity>emptyList() : UserAssignRuleCalc.calcUserAssign(calcSession, bpmCarbonCopy.getUserRules(), Boolean.valueOf(true));
/*     */       }
/* 114 */       if (CollectionUtils.isNotEmpty(sysIdentities)) {
/* 115 */         String htmlContent = StringUtils.isEmpty(bpmCarbonCopy.getHtmlTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getHtmlTemplate(), pluginSession);
/* 116 */         String textContent = StringUtils.isEmpty(bpmCarbonCopy.getTextTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getTextTemplate(), pluginSession);
/* 117 */         BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/* 118 */         bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*     */         
/* 120 */         bpmCarbonCopyRecord.setInstId(bpmInstance.getId());
/* 121 */         bpmCarbonCopyRecord.setFormType(bpmCarbonCopy.getFormType());
/* 122 */         if (isTask) {
/* 123 */           bpmCarbonCopyRecord.setTaskId(bpmTask.getTaskId());
/*     */         } else {
/*     */           
/* 126 */           bpmCarbonCopyRecord.setFormType("instance");
/*     */         } 
/* 128 */         bpmCarbonCopyRecord.setNodeId(nodeId);
/* 129 */         bpmCarbonCopyRecord.setNodeName(nodeName);
/*     */         
/* 131 */         bpmCarbonCopyRecord.setEvent(bpmCarbonCopy.getEvent());
/* 132 */         bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/* 133 */         bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/* 134 */         bpmCarbonCopyRecord.setSubject(bpmInstance.getSubject());
/* 135 */         bpmCarbonCopyRecord.setContent(getContent(bpmCarbonCopy.getMsgType(), htmlContent, textContent));
/* 136 */         bpmCarbonCopyRecord.setCreateTime(currentTime);
/* 137 */         bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/* 138 */         bpmCarbonCopyRecord.setUpdateTime(currentTime);
/* 139 */         bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/* 140 */         bpmCarbonCopyRecord.setVersion(0);
/* 141 */         bpmCarbonCopyRecord.setDelete(Boolean.FALSE.booleanValue());
/* 142 */         bpmCarbonCopyRecordList.add(bpmCarbonCopyRecord);
/* 143 */         addReceiveUserList(bpmCarbonCopyReceiveList, sysIdentities, bpmCarbonCopyRecord);
/* 144 */         NotifyMessage notifyMessage = new NotifyMessage(bpmCarbonCopy.getDesc(), htmlContent, currentUser, sysIdentities);
/* 145 */         notifyMessage.setTextContent(textContent);
/* 146 */         jmsDtoList.add(new DefaultJmsDTO(bpmCarbonCopy.getMsgType(), (Serializable)notifyMessage));
/*     */       } 
/*     */     } 
/* 149 */     if (CollectionUtils.isNotEmpty(bpmCarbonCopyRecordList)) {
/* 150 */       this.bpmCarbonCopyRecordManager.createList(bpmCarbonCopyRecordList);
/* 151 */       this.bpmCarbonCopyReceiveManager.createList(bpmCarbonCopyReceiveList);
/*     */     } 
/*     */ 
/*     */     
/* 155 */     if (CollectionUtils.isNotEmpty(jmsDtoList));
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
/*     */   private void addReceiveUserList(List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList, List<SysIdentity> sysIdentities, BpmCarbonCopyRecord bpmCarbonCopyRecord) {
/* 169 */     for (SysIdentity sysIdentity : sysIdentities) {
/* 170 */       BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 171 */       bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/* 172 */       bpmCarbonCopyReceive.setReceiveUserId(sysIdentity.getId());
/* 173 */       bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/* 174 */       bpmCarbonCopyReceive.setId(IdUtil.getSuid());
/* 175 */       bpmCarbonCopyReceive.setCreateTime(new Date());
/* 176 */       bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/* 177 */       bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/* 178 */       bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/* 179 */       bpmCarbonCopyReceive.setVersion(0);
/* 180 */       bpmCarbonCopyReceive.setDelete(Boolean.FALSE.booleanValue());
/* 181 */       bpmCarbonCopyReceiveList.add(bpmCarbonCopyReceive);
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
/* 195 */     if (StringUtils.isNotEmpty(htmlContent) && StringUtils.isNotEmpty(textContent))
/* 196 */     { switch (msgType)
/*     */       { case "email":
/* 198 */           content = htmlContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 207 */           return content; }  content = textContent; } else { content = StringUtils.defaultString(textContent, htmlContent); }  return content;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/carboncopy/executor/CarbonCopyPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */