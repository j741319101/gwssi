package com.dstz.bpm.plugin.global.carboncopy.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.jms.model.DefaultJmsDTO;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.jms.producer.JmsProducer;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarbonCopyActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
   @Autowired
   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
   @Autowired
   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
   @Resource
   JmsProducer jmsProducer;
   @Autowired
   private UserService userService;

   public ActionType getActionType() {
      return ActionType.CARBONCOPY;
   }

   public int getSn() {
      return 1;
   }

   public String getConfigPage() {
      return "/bpm/task/carbonCopyActionDialog.html";
   }

   protected void doActionBefore(DefualtTaskActionCmd actionModel) {
   }

   public void doAction(DefualtTaskActionCmd actionModel) {
      IUser currentUser = ContextUtil.getCurrentUser();
      Date currentTime = new Date();
      BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
      bpmCarbonCopyRecord.setId(IdUtil.getSuid());
      bpmCarbonCopyRecord.setInstId(actionModel.getInstanceId());
      bpmCarbonCopyRecord.setTaskId(actionModel.getTaskId());
      bpmCarbonCopyRecord.setNodeId(actionModel.getNodeId());
      if (actionModel.getBpmTask() != null) {
         bpmCarbonCopyRecord.setNodeName(actionModel.getBpmTask().getName());
      }

      bpmCarbonCopyRecord.setFormType("instance");
      bpmCarbonCopyRecord.setEvent("manMade");
      bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
      bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
      bpmCarbonCopyRecord.setSubject(actionModel.getBpmInstance().getSubject());
      bpmCarbonCopyRecord.setContent(actionModel.getOpinion());
      bpmCarbonCopyRecord.setCreateTime(currentTime);
      bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
      bpmCarbonCopyRecord.setUpdateTime(currentTime);
      bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
      bpmCarbonCopyRecord.setVersion(0);
      bpmCarbonCopyRecord.setDelete(Boolean.FALSE);
      this.bpmCarbonCopyRecordManager.create(bpmCarbonCopyRecord);
      JSONArray users = actionModel.getExtendConf().getJSONArray("users");
      new ArrayList();
      List<JmsDTO> jmsDto = new ArrayList();
      List<SysIdentity> identities = new ArrayList();
      BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
      bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
      bpmCarbonCopyReceive.setRead(Boolean.FALSE);
      bpmCarbonCopyReceive.setId(IdUtil.getSuid());
      bpmCarbonCopyReceive.setCreateTime(new Date());
      bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
      bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
      bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
      bpmCarbonCopyReceive.setVersion(0);
      bpmCarbonCopyReceive.setDelete(Boolean.FALSE);
      users.forEach((obj) -> {
         JSONObject user = (JSONObject)obj;
         String type = user.getString("type");
         if (StringUtil.isEmpty(type)) {
            type = "user";
         }

         if (StringUtils.equals("user", type)) {
            bpmCarbonCopyReceive.setId(IdUtil.getSuid());
            identities.add(new DefaultIdentity(user.getString("id"), "", "user", ""));
            bpmCarbonCopyReceive.setReceiveUserId(user.getString("id"));
            bpmCarbonCopyReceive.setReceiveUserName(user.getString("name"));
            this.bpmCarbonCopyReceiveManager.create(bpmCarbonCopyReceive);
         } else {
            List<IUser> groupUsers =(List<IUser>) this.userService.getUserListByGroup(type, user.getString("id"));
            if (CollectionUtil.isNotEmpty(groupUsers)) {
               groupUsers.forEach((gu) -> {
                  bpmCarbonCopyReceive.setId(IdUtil.getSuid());
                  identities.add(new DefaultIdentity(gu.getUserId(), "", "user", ""));
                  bpmCarbonCopyReceive.setReceiveUserId(gu.getUserId());
                  bpmCarbonCopyReceive.setReceiveUserName(user.getString("name"));
                  this.bpmCarbonCopyReceiveManager.create(bpmCarbonCopyReceive);
               });
            }
         }

      });
      NotifyMessage message = new NotifyMessage(actionModel.getOpinion(), "", ContextUtil.getCurrentUser(), identities);
      message.setTag("待阅任务");
      Map<String, Object> extendVars = new HashMap();
      extendVars.put("detailId", actionModel.getInstanceId());
      extendVars.put("statue", "carbon");
      extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
      extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", ContextUtil.getCurrentUser().getFullname(), DateUtil.formatDate(new Date()), actionModel.getBpmTask().getSubject()));
      extendVars.put("type", "待办");
      extendVars.put("head", "待阅");
      message.setExtendVars(extendVars);
      jmsDto.add(new DefaultJmsDTO("inner", message));
      this.jmsProducer.sendToQueue(jmsDto);
   }

   protected void doActionAfter(DefualtTaskActionCmd actionModel) {
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      NodeType nodeType = nodeDef.getType();
//      return nodeType == NodeType.USERTASK;
      return false;//todo 暂时去掉按钮
   }
}
