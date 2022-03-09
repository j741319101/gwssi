/*     */ package com.dstz.bpm.plugin.global.nodemessage.executer;
/*     */ 
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
/*     */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmPluginSession;
/*     */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*     */ import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessage;
/*     */ import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
/*     */ import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.UserDTO;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class NodeMessagePluginExecutor
/*     */   extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeMessagePluginDef>
/*     */ {
/*     */   @Resource
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Resource
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkerEngine;
/*     */   @Autowired
/*     */   BpmTaskManager bpmTaskManager;
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, NodeMessagePluginDef pluginDef) {
/*  56 */     List<NodeMessage> messages = pluginDef.getNodeMessageList();
/*  57 */     for (NodeMessage nodeMessage : messages) {
/*     */       
/*  59 */       if (!supportCondition(pluginSession, nodeMessage)) {
/*     */         continue;
/*     */       }
/*  62 */       List<JmsDTO> JmsDto = getJmsMsgVo(nodeMessage, pluginSession);
/*  63 */       this.jmsProducer.sendToQueue(JmsDto);
/*  64 */       this.LOG.debug("【节点消息插件】发送消息成功！时机：{}，消息标题：{}", pluginSession.getEventType().getValue(), nodeMessage.getDesc());
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<JmsDTO> getJmsMsgVo(NodeMessage nodeMessage, BpmExecutionPluginSession session) {
/*     */     List<SysIdentity> userList;
/*     */     UserDTO userDTO;
/*  76 */     String[] msgType = nodeMessage.getMsgType().split(",");
/*     */     
/*  78 */     String htmlTemplate = nodeMessage.getHtmlTemplate();
/*  79 */     String textTemplate = nodeMessage.getTextTemplate();
/*  80 */     BpmInstance instance = (BpmInstance)session.getBpmInstance();
/*  81 */     String opinion = nodeMessage.getDesc();
/*  82 */     String content = "";
/*  83 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)session.get("actionCmd");
/*  84 */     BpmTask task = (BpmTask)actionCmd.getBpmTask();
/*     */     try {
/*  86 */       if (StringUtil.isNotEmpty(htmlTemplate)) {
/*  87 */         htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*     */         
/*  89 */         content = this.freemarkerEngine.parseByString(htmlTemplate, session);
/*     */       } 
/*  91 */       if (StringUtil.isNotEmpty(textTemplate)) {
/*  92 */         content = this.freemarkerEngine.parseByString(textTemplate, session);
/*     */       }
/*     */     }
/*  95 */     catch (Exception e) {
/*  96 */       this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
/*  97 */       this.LOG.error("instId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[] { session.getBpmInstance().getId(), e.getMessage(), e });
/*     */       
/*  99 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 103 */     if (CollectionUtil.isEmpty(nodeMessage.getUserRules())) {
/* 104 */       BaseActionCmd cmd = (BaseActionCmd)BpmContext.getActionModel();
/* 105 */       userList = cmd.getBpmIdentity(cmd.getNodeId());
/*     */     } else {
/* 107 */       userList = getUser(session, nodeMessage.getUserRules());
/*     */     } 
/*     */     
/* 110 */     if (CollectionUtil.isEmpty(userList)) {
/* 111 */       this.LOG.debug("【节点消息插件】没有需要发送的消息！原因：接收消息人员为空。 节点：{}，时机：{}，消息标题：{}", new Object[] { getActivitiId(session), session.getEventType().getValue(), nodeMessage.getDesc() });
/* 112 */       return Collections.emptyList();
/*     */     } 
/*     */     
/* 115 */     Map<String, Object> extendVars = new HashMap<>();
/* 116 */     extendVars.put("detailId", task.getId());
/* 117 */     IUser user = ContextUtil.getCurrentUser();
/* 118 */     if (user == null) {
/* 119 */       userDTO = new UserDTO();
/* 120 */       userDTO.setFullname("系统");
/*     */     } 
/* 122 */     extendVars.put("name", userDTO.getFullname());
/* 123 */     extendVars.put("statue", "call");
/* 124 */     extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", new Object[] { userDTO.getFullname(), 
/* 125 */             DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 126 */     extendVars.put("type", "待办");
/* 127 */     extendVars.put("head", "待办");
/*     */     
/* 129 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 130 */     for (String type : msgType) {
/* 131 */       NotifyMessage message = new NotifyMessage(opinion, content, (IUser)userDTO, userList);
/* 132 */       message.setTag("待办任务");
/* 133 */       message.setExtendVars(extendVars);
/* 134 */       jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */     } 
/* 136 */     return jmsDto;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<SysIdentity> getUser(BpmExecutionPluginSession pluginSession, List<UserAssignRule> ruleList) {
/* 141 */     BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)pluginSession);
/* 142 */     return UserAssignRuleCalc.calcUserAssign(calcSession, ruleList, Boolean.valueOf(false));
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
/*     */   private boolean supportCondition(BpmExecutionPluginSession session, NodeMessage nodeMessage) {
/* 154 */     if (StringUtil.isNotEmpty(nodeMessage.getEvent()) && 
/* 155 */       !nodeMessage.getEvent().equals(session.getEventType().getKey())) {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     if (StringUtil.isNotEmpty(nodeMessage.getNodeId()) && 
/* 160 */       !nodeMessage.getNodeId().equals(getActivitiId(session))) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     if (StringUtil.isNotEmpty(nodeMessage.getCondition())) {
/* 165 */       Boolean support = Boolean.valueOf(this.scriptEngine.executeBoolean(nodeMessage.getCondition(), (Map)session));
/* 166 */       if (!support.booleanValue()) return false; 
/*     */     } 
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/nodemessage/executer/NodeMessagePluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */