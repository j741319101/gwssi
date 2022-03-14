package com.dstz.bpm.plugin.global.nodemessage.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessage;
import com.dstz.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.UserDTO;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.freemark.IFreemarkerEngine;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.jms.model.DefaultJmsDTO;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeMessagePluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeMessagePluginDef> {
   @Resource
   IGroovyScriptEngine scriptEngine;
   @Resource
   JmsProducer jmsProducer;
   @Autowired
   IFreemarkerEngine freemarkerEngine;
   @Autowired
   BpmTaskManager bpmTaskManager;
   @Autowired
   UserService userService;

   public Void execute(BpmExecutionPluginSession pluginSession, NodeMessagePluginDef pluginDef) {
      if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
         DefualtTaskActionCmd cmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
         if (TaskSkipType.NO_SKIP != cmd.isHasSkipThisTask()) {
            this.LOG.debug("【节点消息插件】跳过执行，因为当前节点自动跳过");
            return null;
         }
      }

      List<NodeMessage> messages = pluginDef.getNodeMessageList();
      Iterator var4 = messages.iterator();

      while(var4.hasNext()) {
         NodeMessage nodeMessage = (NodeMessage)var4.next();
         if (this.supportCondition(pluginSession, nodeMessage)) {
            List<JmsDTO> JmsDto = this.getJmsMsgVo(nodeMessage, pluginSession);
            this.jmsProducer.sendToQueue(JmsDto);
            this.LOG.debug("【节点消息插件】发送消息成功！时机：{}，消息标题：{}", pluginSession.getEventType().getValue(), nodeMessage.getDesc());
         }
      }

      return null;
   }

   private List<JmsDTO> getJmsMsgVo(NodeMessage nodeMessage, BpmExecutionPluginSession session) {
      String[] msgType = nodeMessage.getMsgType().split(",");
      String htmlTemplate = nodeMessage.getHtmlTemplate();
      String textTemplate = nodeMessage.getTextTemplate();
      BpmInstance instance = (BpmInstance)session.getBpmInstance();
      String opinion = nodeMessage.getDesc();
      String content = "";
      DefualtTaskActionCmd actionCmd = (DefualtTaskActionCmd)session.get("actionCmd");
      BpmTask task = (BpmTask)actionCmd.getBpmTask();

      try {
         if (StringUtil.isNotEmpty(htmlTemplate)) {
            htmlTemplate = htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            content = this.freemarkerEngine.parseByString(htmlTemplate, session);
         }

         if (StringUtil.isNotEmpty(textTemplate)) {
            content = this.freemarkerEngine.parseByString(textTemplate, session);
         }
      } catch (Exception var20) {
         this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
         this.LOG.error("instId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[]{session.getBpmInstance().getId(), var20.getMessage(), var20});
         var20.printStackTrace();
      }

      List userList;
      if (CollectionUtil.isEmpty(nodeMessage.getUserRules())) {
         BaseActionCmd cmd = (BaseActionCmd)BpmContext.getActionModel();
         userList = cmd.getBpmIdentity(cmd.getNodeId());
      } else {
         userList = this.getUser(session, nodeMessage.getUserRules());
      }

      this.extractBpmIdentity(userList);
      if (CollectionUtil.isEmpty(userList)) {
         this.LOG.debug("【节点消息插件】没有需要发送的消息！原因：接收消息人员为空。 节点：{}，时机：{}，消息标题：{}", new Object[]{this.getActivitiId(session), session.getEventType().getValue(), nodeMessage.getDesc()});
         return Collections.emptyList();
      } else {
         Map<String, Object> extendVars = new HashMap();
         extendVars.put("detailId", task.getId());
         IUser user = ContextUtil.getCurrentUser();
         if (user == null) {
            user = new UserDTO();
            ((IUser)user).setFullname("系统");
         }

         extendVars.put("statue", "call");
         if (StringUtils.equals((String)session.get("submitActionName"), "reject")) {
            extendVars.put("statue", "back");
         }

         extendVars.put("name", ((IUser)user).getFullname());
         extendVars.put("title", String.format("%s于%s发起了待办任务《%s》", ((IUser)user).getFullname(), DateUtil.formatDate(task.getCreateTime()), task.getSubject()));
         extendVars.put("type", "待办");
         extendVars.put("head", "待办");
         List<JmsDTO> jmsDto = new ArrayList();
         String[] var15 = msgType;
         int var16 = msgType.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            String type = var15[var17];
            NotifyMessage message = new NotifyMessage(opinion, content, (IUser)user, userList);
            message.setTag("待办任务");
            message.setExtendVars(extendVars);
            jmsDto.add(new DefaultJmsDTO(type, message));
         }

         return jmsDto;
      }
   }

   private List<SysIdentity> getUser(BpmExecutionPluginSession pluginSession, List<UserAssignRule> ruleList) {
      BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession(pluginSession);
      return UserAssignRuleCalc.calcUserAssign(calcSession, ruleList, false);
   }

   private boolean supportCondition(BpmExecutionPluginSession session, NodeMessage nodeMessage) {
      if (StringUtil.isNotEmpty(nodeMessage.getEvent()) && !nodeMessage.getEvent().equals(session.getEventType().getKey())) {
         return false;
      } else if (StringUtil.isNotEmpty(nodeMessage.getNodeId()) && !nodeMessage.getNodeId().equals(this.getActivitiId(session))) {
         return false;
      } else {
         if (StringUtil.isNotEmpty(nodeMessage.getCondition())) {
            Boolean support = this.scriptEngine.executeBoolean(nodeMessage.getCondition(), session);
            if (!support) {
               return false;
            }
         }

         return true;
      }
   }

   private void extractBpmIdentity(List<SysIdentity> identities) {
      if (identities != null) {
         List<SysIdentity> results = new ArrayList();
         Iterator var3 = identities.iterator();

         while(true) {
            while(var3.hasNext()) {
               SysIdentity bpmIdentity = (SysIdentity)var3.next();
               if ("user".equals(bpmIdentity.getType())) {
                  results.add(bpmIdentity);
               } else {
                  List<IUser> users = (List<IUser> )this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
                  Iterator var6 = users.iterator();

                  while(var6.hasNext()) {
                     IUser user = (IUser)var6.next();
                     results.add(new DefaultIdentity(user));
                  }
               }
            }

            identities.clear();
            identities.addAll(results);
            return;
         }
      }
   }
}
