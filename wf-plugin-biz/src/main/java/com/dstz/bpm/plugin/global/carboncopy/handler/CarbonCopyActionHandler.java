/*     */ package com.dstz.bpm.plugin.global.carboncopy.handler;
/*     */ 
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.api.jms.model.DefaultJmsDTO;
/*     */ import com.dstz.sys.api.jms.model.JmsDTO;
/*     */ import com.dstz.sys.api.jms.model.msg.NotifyMessage;
/*     */ import com.dstz.sys.api.jms.producer.JmsProducer;
/*     */ import com.dstz.sys.api.model.DefaultIdentity;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class CarbonCopyActionHandler
/*     */   extends AbstractTaskActionHandler<DefualtTaskActionCmd>
/*     */ {
/*     */   @Autowired
/*     */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*     */   @Autowired
/*     */   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*     */   @Autowired
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   private UserService userService;
/*     */   
/*     */   public ActionType getActionType() {
/*  63 */     return ActionType.CARBONCOPY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/*  68 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/*  73 */     return "/bpm/task/carbonCopyActionDialog.html";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAction(DefualtTaskActionCmd actionModel) {
/*  86 */     IUser currentUser = ContextUtil.getCurrentUser();
/*  87 */     Date currentTime = new Date();
/*  88 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/*  89 */     bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*  90 */     bpmCarbonCopyRecord.setInstId(actionModel.getInstanceId());
/*  91 */     bpmCarbonCopyRecord.setTaskId(actionModel.getTaskId());
/*  92 */     bpmCarbonCopyRecord.setNodeId(actionModel.getNodeId());
/*  93 */     if (actionModel.getBpmTask() != null) {
/*  94 */       bpmCarbonCopyRecord.setNodeName(actionModel.getBpmTask().getName());
/*     */     }
/*  96 */     bpmCarbonCopyRecord.setFormType("instance");
/*  97 */     bpmCarbonCopyRecord.setEvent("manMade");
/*  98 */     bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/*  99 */     bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/* 100 */     bpmCarbonCopyRecord.setSubject(actionModel.getBpmInstance().getSubject());
/* 101 */     bpmCarbonCopyRecord.setContent(actionModel.getOpinion());
/* 102 */     bpmCarbonCopyRecord.setCreateTime(currentTime);
/* 103 */     bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/* 104 */     bpmCarbonCopyRecord.setUpdateTime(currentTime);
/* 105 */     bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/* 106 */     bpmCarbonCopyRecord.setVersion(0);
/* 107 */     bpmCarbonCopyRecord.setDelete(Boolean.FALSE.booleanValue());
/* 108 */     this.bpmCarbonCopyRecordManager.create(bpmCarbonCopyRecord);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     JSONArray users = actionModel.getExtendConf().getJSONArray("users");
/* 114 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/* 115 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 116 */     List<SysIdentity> identities = new ArrayList<>();
/* 117 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 118 */     bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/*     */     
/* 120 */     bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/* 121 */     bpmCarbonCopyReceive.setId(IdUtil.getSuid());
/* 122 */     bpmCarbonCopyReceive.setCreateTime(new Date());
/* 123 */     bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/* 124 */     bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/* 125 */     bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/* 126 */     bpmCarbonCopyReceive.setVersion(0);
/* 127 */     bpmCarbonCopyReceive.setDelete(Boolean.FALSE.booleanValue());
/* 128 */     users.forEach(obj -> {
/*     */           JSONObject user = (JSONObject)obj;
/*     */ 
/*     */           
/*     */           String type = user.getString("type");
/*     */           
/*     */           if (StringUtil.isEmpty(type)) {
/*     */             type = "user";
/*     */           }
/*     */           
/*     */           if (StringUtils.equals("user", type)) {
/*     */             bpmCarbonCopyReceive.setId(IdUtil.getSuid());
/*     */             
/*     */             identities.add(new DefaultIdentity(user.getString("id"), "", "user"));
/*     */             
/*     */             bpmCarbonCopyReceive.setReceiveUserId(user.getString("id"));
/*     */             
/*     */             this.bpmCarbonCopyReceiveManager.create(bpmCarbonCopyReceive);
/*     */             
/*     */             if (handleLeaderCarbon(user.getString("id"), bpmCarbonCopyReceive.getId(), actionModel.getBpmTask())) {
/*     */               bpmCarbonCopyReceive.setType(TaskStatus.LEADER.getKey());
/*     */               
/*     */               this.bpmCarbonCopyReceiveManager.update(bpmCarbonCopyReceive);
/*     */             } 
/*     */           } else {
/*     */             List<IUser> groupUsers = this.userService.getUserListByGroup(type, user.getString("id"));
/*     */             
/*     */             if (CollectionUtil.isNotEmpty(groupUsers)) {
/*     */               groupUsers.forEach(());
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 161 */     NotifyMessage message = new NotifyMessage(actionModel.getOpinion(), "", ContextUtil.getCurrentUser(), identities);
/* 162 */     message.setTag("待阅任务");
/* 163 */     Map<String, Object> extendVars = new HashMap<>();
/* 164 */     extendVars.put("detailId", actionModel.getInstanceId());
/* 165 */     extendVars.put("statue", "carbon");
/* 166 */     extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 167 */     extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 168 */             DateUtil.formatDate(new Date()), actionModel.getBpmTask().getSubject() }));
/* 169 */     extendVars.put("type", "待办");
/* 170 */     extendVars.put("head", "待阅");
/* 171 */     message.setExtendVars(extendVars);
/* 172 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 173 */     this.jmsProducer.sendToQueue(jmsDto);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 183 */     NodeType nodeType = nodeDef.getType();
/* 184 */     return Boolean.valueOf((nodeType == NodeType.USERTASK));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean handleLeaderCarbon(String receiverId, String receId, IBpmTask task) {
/* 189 */     Map<String, IExtendTaskAction> leaderTaskAction = AppUtil.getImplInstance(IExtendTaskAction.class);
/* 190 */     boolean isLeader = false;
/* 191 */     for (IExtendTaskAction extendTaskAction : leaderTaskAction.values()) {
/* 192 */       isLeader = extendTaskAction.carbonCopyLeaderTask(receiverId, receId, task);
/*     */     }
/* 194 */     return isLeader;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/carboncopy/handler/CarbonCopyActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */