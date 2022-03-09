/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.task.AbstractTaskActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.DefaultJmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.JmsDTO;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.model.msg.NotifyMessage;
/*     */ import cn.gwssi.ecloudframework.sys.api.jms.producer.JmsProducer;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
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
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */   @Resource
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   private UserService userService;
/*     */   
/*     */   public ActionType getActionType() {
/*  57 */     return ActionType.CARBONCOPY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/*  62 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/*  67 */     return "/bpm/task/carbonCopyActionDialog.html";
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
/*  80 */     IUser currentUser = ContextUtil.getCurrentUser();
/*  81 */     Date currentTime = new Date();
/*  82 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/*  83 */     bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*  84 */     bpmCarbonCopyRecord.setInstId(actionModel.getInstanceId());
/*  85 */     bpmCarbonCopyRecord.setTaskId(actionModel.getTaskId());
/*  86 */     bpmCarbonCopyRecord.setNodeId(actionModel.getNodeId());
/*  87 */     if (actionModel.getBpmTask() != null) {
/*  88 */       bpmCarbonCopyRecord.setNodeName(actionModel.getBpmTask().getName());
/*     */     }
/*  90 */     bpmCarbonCopyRecord.setFormType("instance");
/*  91 */     bpmCarbonCopyRecord.setEvent("manMade");
/*  92 */     bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/*  93 */     bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/*  94 */     bpmCarbonCopyRecord.setSubject(actionModel.getBpmInstance().getSubject());
/*  95 */     bpmCarbonCopyRecord.setContent(actionModel.getOpinion());
/*  96 */     bpmCarbonCopyRecord.setCreateTime(currentTime);
/*  97 */     bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/*  98 */     bpmCarbonCopyRecord.setUpdateTime(currentTime);
/*  99 */     bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/* 100 */     bpmCarbonCopyRecord.setVersion(Integer.valueOf(0));
/* 101 */     bpmCarbonCopyRecord.setDelete(Boolean.FALSE);
/* 102 */     this.bpmCarbonCopyRecordManager.create(bpmCarbonCopyRecord);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     JSONArray users = actionModel.getExtendConf().getJSONArray("users");
/* 108 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/* 109 */     List<JmsDTO> jmsDto = new ArrayList<>();
/* 110 */     List<SysIdentity> identities = new ArrayList<>();
/* 111 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 112 */     bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/*     */     
/* 114 */     bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/* 115 */     bpmCarbonCopyReceive.setId(IdUtil.getSuid());
/* 116 */     bpmCarbonCopyReceive.setCreateTime(new Date());
/* 117 */     bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/* 118 */     bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/* 119 */     bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/* 120 */     bpmCarbonCopyReceive.setVersion(Integer.valueOf(0));
/* 121 */     bpmCarbonCopyReceive.setDelete(Boolean.FALSE);
/* 122 */     users.forEach(obj -> {
/*     */           JSONObject user = (JSONObject)obj;
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
/*     */             identities.add(new DefaultIdentity(user.getString("id"), "", "user", ""));
/*     */             
/*     */             bpmCarbonCopyReceive.setReceiveUserId(user.getString("id"));
/*     */             
/*     */             bpmCarbonCopyReceive.setReceiveUserName(user.getString("name"));
/*     */             this.bpmCarbonCopyReceiveManager.create(bpmCarbonCopyReceive);
/*     */           } else {
/*     */             List<IUser> groupUsers = this.userService.getUserListByGroup(type, user.getString("id"));
/*     */             if (CollectionUtil.isNotEmpty(groupUsers)) {
/*     */               groupUsers.forEach(());
/*     */             }
/*     */           } 
/*     */         });
/* 147 */     NotifyMessage message = new NotifyMessage(actionModel.getOpinion(), "", ContextUtil.getCurrentUser(), identities);
/* 148 */     message.setTag("待阅任务");
/* 149 */     Map<String, Object> extendVars = new HashMap<>();
/* 150 */     extendVars.put("detailId", actionModel.getInstanceId());
/* 151 */     extendVars.put("statue", "carbon");
/* 152 */     extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 153 */     extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 154 */             DateUtil.formatDate(new Date()), actionModel.getBpmTask().getSubject() }));
/* 155 */     extendVars.put("type", "待办");
/* 156 */     extendVars.put("head", "待阅");
/* 157 */     message.setExtendVars(extendVars);
/* 158 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 159 */     this.jmsProducer.sendToQueue(jmsDto);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefualtTaskActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 169 */     NodeType nodeType = nodeDef.getType();
/* 170 */     return Boolean.valueOf((nodeType == NodeType.USERTASK));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/handler/CarbonCopyActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */