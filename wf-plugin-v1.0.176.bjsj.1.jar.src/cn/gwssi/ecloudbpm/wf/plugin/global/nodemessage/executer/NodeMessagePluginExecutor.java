/*     */ package com.dstz.bpm.plugin.global.nodemessage.executer;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.TaskSkipType;
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
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.UserDTO;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
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
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class NodeMessagePluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeMessagePluginDef> {
/*     */   @Resource
/*     */   IGroovyScriptEngine scriptEngine;
/*     */   @Resource
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkerEngine;
/*     */   @Autowired
/*     */   BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public Void execute(BpmExecutionPluginSession pluginSession, NodeMessagePluginDef pluginDef) {
/*  60 */     if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
/*  61 */       DefualtTaskActionCmd cmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
/*  62 */       if (TaskSkipType.NO_SKIP != cmd.isHasSkipThisTask()) {
/*  63 */         this.LOG.debug("【节点消息插件】跳过执行，因为当前节点自动跳过");
/*  64 */         return null;
/*     */       } 
/*     */     } 
/*  67 */     List<NodeMessage> messages = pluginDef.getNodeMessageList();
/*  68 */     for (NodeMessage nodeMessage : messages) {
/*     */       
/*  70 */       if (!supportCondition(pluginSession, nodeMessage)) {
/*     */         continue;
/*     */       }
/*  73 */       List<JmsDTO> JmsDto = getJmsMsgVo(nodeMessage, pluginSession);
/*  74 */       this.jmsProducer.sendToQueue(JmsDto);
/*  75 */       this.LOG.debug("【节点消息插件】发送消息成功！时机：{}，消息标题：{}", pluginSession.getEventType().getValue(), nodeMessage.getDesc());
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<JmsDTO> getJmsMsgVo(NodeMessage nodeMessage, BpmExecutionPluginSession session) {
/*     */     List<SysIdentity> userList;
/*     */     UserDTO userDTO;
/*  88 */     String[] msgType = nodeMessage.getMsgType().split(",");
/*     */     
/*  90 */     String htmlTemplate = nodeMessage.getHtmlTemplate();
/*  91 */     String textTemplate = nodeMessage.getTextTemplate();
/*  92 */     BpmInstance instance = (BpmInstance)session.getBpmInstance();
/*  93 */     String opinion = nodeMessage.getDesc();
/*  94 */     String content = "";
/*  95 */     DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)session.get("actionCmd");
/*  96 */     BpmTask task = (BpmTask)actionCmd.getBpmTask();
/*     */     
/*     */     try {
/*  99 */       if (StringUtil.isNotEmpty(htmlTemplate)) {
/* 100 */         htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*     */         
/* 102 */         content = this.freemarkerEngine.parseByString(htmlTemplate, session);
/*     */       } 
/* 104 */       if (StringUtil.isNotEmpty(textTemplate)) {
/* 105 */         content = this.freemarkerEngine.parseByString(textTemplate, session);
/*     */       }
/*     */     }
/* 108 */     catch (Exception e) {
/* 109 */       this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
/* 110 */       this.LOG.error("instId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[] { session.getBpmInstance().getId(), e.getMessage(), e });
/*     */       
/* 112 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (CollectionUtil.isEmpty(nodeMessage.getUserRules())) {
/* 117 */       BaseActionCmd cmd = (BaseActionCmd)BpmContext.getActionModel();
/* 118 */       userList = cmd.getBpmIdentity(cmd.getNodeId());
/*     */     } else {
/* 120 */       userList = getUser(session, nodeMessage.getUserRules());
/*     */     } 
/* 122 */     extractBpmIdentity(userList);
/* 123 */     if (CollectionUtil.isEmpty(userList)) {
/* 124 */       this.LOG.debug("【节点消息插件】没有需要发送的消息！原因：接收消息人员为空。 节点：{}，时机：{}，消息标题：{}", new Object[] { getActivitiId(session), session.getEventType().getValue(), nodeMessage.getDesc() });
/* 125 */       return Collections.emptyList();
/*     */     } 
/*     */ 
/*     */     
/* 129 */     Map<String, Object> extendVars = new HashMap<>();
/* 130 */     extendVars.put("detailId", task.getId());
/* 131 */     IUser user = ContextUtil.getCurrentUser();
/* 132 */     if (user == null) {
/*     */       
/* 134 */       userDTO = new UserDTO();
/* 135 */       userDTO.setFullname("系统");
/*     */     } 
/* 137 */     extendVars.put("statue", "call");
/* 138 */     if (StringUtils.equals((String)session.get("submitActionName"), "reject")) {
/* 139 */       extendVars.put("statue", "back");
/*     */     }
/* 141 */     extendVars.put("name", userDTO.getFullname());
/*     */     
/* 143 */     extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", new Object[] { userDTO.getFullname(), 
/* 144 */             DateUtil.formatDate(task.getCreateTime()), task.getSubject() }));
/* 145 */     extendVars.put("type", "待办");
/* 146 */     extendVars.put("head", "待办");
/*     */     
/* 148 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 149 */     for (String type : msgType) {
/* 150 */       NotifyMessage message = new NotifyMessage(opinion, content, (IUser)userDTO, userList);
/* 151 */       message.setTag("待办任务");
/* 152 */       message.setExtendVars(extendVars);
/* 153 */       jmsDto.add(new DefaultJmsDTO(type, (Serializable)message));
/*     */     } 
/* 155 */     return jmsDto;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<SysIdentity> getUser(BpmExecutionPluginSession pluginSession, List<UserAssignRule> ruleList) {
/* 160 */     BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession((BpmPluginSession)pluginSession);
/* 161 */     return UserAssignRuleCalc.calcUserAssign(calcSession, ruleList, Boolean.valueOf(false));
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
/*     */   private boolean supportCondition(BpmExecutionPluginSession session, NodeMessage nodeMessage) {
/* 174 */     if (StringUtil.isNotEmpty(nodeMessage.getEvent()) && 
/* 175 */       !nodeMessage.getEvent().equals(session.getEventType().getKey())) {
/* 176 */       return false;
/*     */     }
/*     */     
/* 179 */     if (StringUtil.isNotEmpty(nodeMessage.getNodeId()) && 
/* 180 */       !nodeMessage.getNodeId().equals(getActivitiId(session))) {
/* 181 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 185 */     if (StringUtil.isNotEmpty(nodeMessage.getCondition())) {
/* 186 */       Boolean support = Boolean.valueOf(this.scriptEngine.executeBoolean(nodeMessage.getCondition(), (Map)session));
/* 187 */       if (!support.booleanValue()) return false; 
/*     */     } 
/* 189 */     return true;
/*     */   }
/*     */   private void extractBpmIdentity(List<SysIdentity> identities) {
/* 192 */     if (identities == null) {
/*     */       return;
/*     */     }
/* 195 */     List<SysIdentity> results = new ArrayList<>();
/* 196 */     for (SysIdentity bpmIdentity : identities) {
/* 197 */       if ("user".equals(bpmIdentity.getType())) {
/* 198 */         results.add(bpmIdentity); continue;
/*     */       } 
/* 200 */       List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/* 201 */       for (IUser user : users) {
/* 202 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/* 206 */     identities.clear();
/* 207 */     identities.addAll(results);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/nodemessage/executer/NodeMessagePluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */