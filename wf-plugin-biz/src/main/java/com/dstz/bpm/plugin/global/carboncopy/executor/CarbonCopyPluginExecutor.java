package com.dstz.bpm.plugin.global.carboncopy.executor;

import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.bpm.plugin.global.carboncopy.def.BpmCarbonCopy;
import com.dstz.bpm.plugin.global.carboncopy.def.CarbonCopyPluginDef;
import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import com.dstz.base.core.id.IdUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.freemark.IFreemarkerEngine;
import com.dstz.sys.api.jms.model.DefaultJmsDTO;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.date.DateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarbonCopyPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, CarbonCopyPluginDef> {
   private static final Logger logger = LoggerFactory.getLogger(CarbonCopyPluginExecutor.class);
   @Resource
   private JmsProducer jmsProducer;
   @Autowired
   private IFreemarkerEngine freemarkerEngine;
   @Autowired
   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
   @Autowired
   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;

   public Void execute(BpmExecutionPluginSession pluginSession, CarbonCopyPluginDef pluginDef) {
      this.ccAndRecord(pluginSession, pluginDef);
      return null;
   }

   private void ccAndRecord(BpmExecutionPluginSession pluginSession, CarbonCopyPluginDef pluginDef) {
      boolean isTask = pluginSession instanceof DefaultBpmTaskPluginSession;
      IBpmTask bpmTask = null;
      String nodeId;
      String nodeName;
      if (isTask) {
         bpmTask = ((DefaultBpmTaskPluginSession)pluginSession).getBpmTask();
         nodeId = bpmTask.getNodeId();
         nodeName = bpmTask.getName();
      } else {
         ExecutionEntity execution = (ExecutionEntity)pluginSession.getVariableScope();
         nodeId = execution.getActivityId();
         nodeName = execution.getCurrentActivityName();
      }

      List<BpmCarbonCopy> bpmCarbonCopyList = (List)pluginDef.getNodeEventCarbonCopyMap().getOrDefault(CarbonCopyPluginDef.getMapKey(nodeId, pluginSession.getEventType().getKey()), Collections.emptyList());
      if (bpmCarbonCopyList.isEmpty()) {
         logger.trace("{} {} no carbon copy configuration", nodeId, pluginSession.getEventType().getKey());
      } else {
         Date currentTime = new Date();
         IUser currentUser = ContextUtil.getCurrentUser();
         IBpmInstance bpmInstance = pluginSession.getBpmInstance();
         BpmUserCalcPluginSession calcSession = BpmPluginSessionFactory.buildBpmUserCalcPluginSession(pluginSession);
         List<BpmCarbonCopyRecord> bpmCarbonCopyRecordList = new ArrayList();
         List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList();
         List<JmsDTO> jmsDtoList = new ArrayList();
         Iterator var15 = bpmCarbonCopyList.iterator();

         while(var15.hasNext()) {
            BpmCarbonCopy bpmCarbonCopy = (BpmCarbonCopy)var15.next();
            boolean matcher = StringUtils.equals(nodeId, bpmCarbonCopy.getNodeId()) && StringUtils.equals(bpmCarbonCopy.getEvent(), pluginSession.getEventType().getKey());
            List<SysIdentity> sysIdentities = Collections.emptyList();
            if (matcher) {
               sysIdentities = CollectionUtils.isEmpty(bpmCarbonCopy.getUserRules()) ? Collections.emptyList() : UserAssignRuleCalc.calcUserAssign(calcSession, bpmCarbonCopy.getUserRules(), true);
            }

            if (CollectionUtils.isNotEmpty(sysIdentities)) {
               String htmlContent = StringUtils.isEmpty(bpmCarbonCopy.getHtmlTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getHtmlTemplate(), pluginSession);
               String textContent = StringUtils.isEmpty(bpmCarbonCopy.getTextTemplate()) ? null : this.freemarkerEngine.parseByString(bpmCarbonCopy.getTextTemplate(), pluginSession);
               BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
               bpmCarbonCopyRecord.setId(IdUtil.getSuid());
               bpmCarbonCopyRecord.setInstId(bpmInstance.getId());
               bpmCarbonCopyRecord.setFormType(bpmCarbonCopy.getFormType());
               if (isTask) {
                  bpmCarbonCopyRecord.setTaskId(bpmTask.getTaskId());
               } else {
                  bpmCarbonCopyRecord.setFormType("instance");
               }

               bpmCarbonCopyRecord.setNodeId(nodeId);
               bpmCarbonCopyRecord.setNodeName(nodeName);
               bpmCarbonCopyRecord.setEvent(bpmCarbonCopy.getEvent());
               bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
               bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
               bpmCarbonCopyRecord.setSubject(bpmInstance.getSubject());
               bpmCarbonCopyRecord.setContent(this.getContent(bpmCarbonCopy.getMsgType(), htmlContent, textContent));
               bpmCarbonCopyRecord.setCreateTime(currentTime);
               bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
               bpmCarbonCopyRecord.setUpdateTime(currentTime);
               bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
               bpmCarbonCopyRecord.setVersion(0);
               bpmCarbonCopyRecord.setDelete(Boolean.FALSE);
               bpmCarbonCopyRecordList.add(bpmCarbonCopyRecord);
               this.addReceiveUserList(bpmCarbonCopyReceiveList, sysIdentities, bpmCarbonCopyRecord);
               NotifyMessage notifyMessage = new NotifyMessage(bpmCarbonCopy.getDesc(), htmlContent, currentUser, sysIdentities);
               notifyMessage.setTag("待阅任务");
               Map<String, Object> extendVars = new HashMap();
               extendVars.put("detailId", bpmInstance.getId());
               extendVars.put("statue", "carbon");
               extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
               extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", ContextUtil.getCurrentUser().getFullname(), DateUtil.formatDate(new Date()), bpmInstance.getSubject()));
               extendVars.put("type", "待办");
               extendVars.put("head", "待阅");
               notifyMessage.setExtendVars(extendVars);
               notifyMessage.setTextContent(textContent);
               jmsDtoList.add(new DefaultJmsDTO(bpmCarbonCopy.getMsgType(), notifyMessage));
            }
         }

         if (CollectionUtils.isNotEmpty(bpmCarbonCopyRecordList)) {
            this.bpmCarbonCopyRecordManager.createList(bpmCarbonCopyRecordList);
            this.bpmCarbonCopyReceiveManager.createList(bpmCarbonCopyReceiveList);
         }

         if (CollectionUtils.isNotEmpty(jmsDtoList)) {
            this.jmsProducer.sendToQueue(jmsDtoList);
         }

      }
   }

   private void addReceiveUserList(List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList, List<SysIdentity> sysIdentities, BpmCarbonCopyRecord bpmCarbonCopyRecord) {
      Iterator var4 = sysIdentities.iterator();

      while(var4.hasNext()) {
         SysIdentity sysIdentity = (SysIdentity)var4.next();
         BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
         bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
         bpmCarbonCopyReceive.setReceiveUserId(sysIdentity.getId());
         bpmCarbonCopyReceive.setReceiveUserName(sysIdentity.getName());
         bpmCarbonCopyReceive.setRead(Boolean.FALSE);
         bpmCarbonCopyReceive.setId(IdUtil.getSuid());
         bpmCarbonCopyReceive.setCreateTime(new Date());
         bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
         bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
         bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
         bpmCarbonCopyReceive.setVersion(0);
         bpmCarbonCopyReceive.setDelete(Boolean.FALSE);
         bpmCarbonCopyReceiveList.add(bpmCarbonCopyReceive);
      }

   }

   private String getContent(String msgType, String htmlContent, String textContent) {
      String content;
      if (StringUtils.isNotEmpty(htmlContent) && StringUtils.isNotEmpty(textContent)) {
         byte var6 = -1;
         switch(msgType.hashCode()) {
         case 96619420:
            if (msgType.equals("email")) {
               var6 = 0;
            }
         default:
            switch(var6) {
            case 0:
               content = htmlContent;
               break;
            default:
               content = textContent;
            }
         }
      } else {
         content = StringUtils.defaultString(textContent, htmlContent);
      }

      return content;
   }
}
