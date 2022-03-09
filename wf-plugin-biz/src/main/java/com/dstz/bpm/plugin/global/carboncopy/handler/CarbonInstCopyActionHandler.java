/*     */ package com.dstz.bpm.plugin.global.carboncopy.handler;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.TaskStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.action.handler.AbsActionHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class CarbonInstCopyActionHandler
/*     */   extends AbsActionHandler<DefaultInstanceActionCmd>
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
/*     */   protected void doAction(DefaultInstanceActionCmd actionModel) {
/*  71 */     IUser currentUser = ContextUtil.getCurrentUser();
/*  72 */     Date currentTime = new Date();
/*  73 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/*  74 */     bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*  75 */     bpmCarbonCopyRecord.setInstId(actionModel.getInstanceId());
/*  76 */     bpmCarbonCopyRecord.setNodeId(actionModel.getNodeId());
/*  77 */     bpmCarbonCopyRecord.setFormType("instance");
/*  78 */     bpmCarbonCopyRecord.setEvent("manMade");
/*  79 */     bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/*  80 */     bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/*  81 */     bpmCarbonCopyRecord.setSubject(actionModel.getBpmInstance().getSubject());
/*  82 */     bpmCarbonCopyRecord.setContent(actionModel.getOpinion());
/*  83 */     bpmCarbonCopyRecord.setCreateTime(currentTime);
/*  84 */     bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/*  85 */     bpmCarbonCopyRecord.setUpdateTime(currentTime);
/*  86 */     bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/*  87 */     bpmCarbonCopyRecord.setVersion(0);
/*  88 */     bpmCarbonCopyRecord.setDelete(Boolean.FALSE.booleanValue());
/*  89 */     this.bpmCarbonCopyRecordManager.create(bpmCarbonCopyRecord);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     JSONArray users = actionModel.getExtendConf().getJSONArray("users");
/*  95 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/*  96 */     List<JmsDTO> jmsDto = new ArrayList<>();
/*  97 */     List<SysIdentity> identities = new ArrayList<>();
/*  98 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/*  99 */     bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/* 100 */     bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/* 101 */     bpmCarbonCopyReceive.setCreateTime(new Date());
/* 102 */     bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/* 103 */     bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/* 104 */     bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/* 105 */     bpmCarbonCopyReceive.setVersion(0);
/* 106 */     bpmCarbonCopyReceive.setDelete(Boolean.FALSE.booleanValue());
/* 107 */     users.forEach(obj -> {
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
/*     */             if (handleLeaderCarbon(user.getString("id"), bpmCarbonCopyReceive.getId(), actionModel.getBpmInstance())) {
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
/* 140 */     NotifyMessage message = new NotifyMessage(actionModel.getOpinion(), "", ContextUtil.getCurrentUser(), identities);
/* 141 */     message.setTag("待阅任务");
/* 142 */     Map<String, Object> extendVars = new HashMap<>();
/* 143 */     extendVars.put("detailId", actionModel.getInstanceId());
/* 144 */     extendVars.put("statue", "carbon");
/* 145 */     extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 146 */     extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 147 */             DateUtil.formatDate(new Date()), actionModel.getSubject() }));
/* 148 */     extendVars.put("type", "待办");
/* 149 */     extendVars.put("head", "待阅");
/* 150 */     message.setExtendVars(extendVars);
/* 151 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 152 */     this.jmsProducer.sendToQueue(jmsDto);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareActionDatas(DefaultInstanceActionCmd intanceCmdData) {
/* 157 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceManager.get(intanceCmdData.getInstanceId());
/* 158 */     if (instance == null) {
/* 159 */       throw new BusinessException(BpmStatusCode.INST_NOT_FOUND);
/*     */     }
/* 161 */     intanceCmdData.setBpmInstance(instance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleLeaderCarbon(String receiverId, String receId, IBpmInstance instance) {
/* 176 */     Map<String, IExtendTaskAction> leaderTaskAction = AppUtil.getImplInstance(IExtendTaskAction.class);
/* 177 */     boolean isLeader = false;
/* 178 */     for (IExtendTaskAction extendTaskAction : leaderTaskAction.values()) {
/* 179 */       isLeader = extendTaskAction.carbonInstCopyLeaderTask(receiverId, receId, instance);
/*     */     }
/* 181 */     return isLeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 186 */     return ActionType.CARBONINSTCOPY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 191 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 196 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 201 */     return "/bpm/task/carbonCopyActionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 206 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/global/carboncopy/handler/CarbonInstCopyActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */