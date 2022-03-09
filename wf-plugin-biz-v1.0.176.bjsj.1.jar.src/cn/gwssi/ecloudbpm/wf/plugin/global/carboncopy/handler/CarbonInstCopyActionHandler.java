/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.handler.AbsActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
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
/*     */   @Resource
/*     */   JmsProducer jmsProducer;
/*     */   @Autowired
/*     */   private UserService userService;
/*     */   
/*     */   protected void doAction(DefaultInstanceActionCmd actionModel) {
/*  63 */     IUser currentUser = ContextUtil.getCurrentUser();
/*  64 */     Date currentTime = new Date();
/*  65 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = new BpmCarbonCopyRecord();
/*  66 */     bpmCarbonCopyRecord.setId(IdUtil.getSuid());
/*  67 */     bpmCarbonCopyRecord.setInstId(actionModel.getInstanceId());
/*  68 */     bpmCarbonCopyRecord.setNodeId(actionModel.getNodeId());
/*  69 */     bpmCarbonCopyRecord.setFormType("instance");
/*  70 */     bpmCarbonCopyRecord.setEvent("manMade");
/*  71 */     bpmCarbonCopyRecord.setTriggerUserId(currentUser.getUserId());
/*  72 */     bpmCarbonCopyRecord.setTriggerUserName(currentUser.getFullname());
/*  73 */     bpmCarbonCopyRecord.setSubject(actionModel.getBpmInstance().getSubject());
/*  74 */     bpmCarbonCopyRecord.setContent(actionModel.getOpinion());
/*  75 */     bpmCarbonCopyRecord.setCreateTime(currentTime);
/*  76 */     bpmCarbonCopyRecord.setCreateBy(currentUser.getUserId());
/*  77 */     bpmCarbonCopyRecord.setUpdateTime(currentTime);
/*  78 */     bpmCarbonCopyRecord.setUpdateBy(currentUser.getUserId());
/*  79 */     bpmCarbonCopyRecord.setVersion(Integer.valueOf(0));
/*  80 */     bpmCarbonCopyRecord.setDelete(Boolean.FALSE);
/*  81 */     this.bpmCarbonCopyRecordManager.create(bpmCarbonCopyRecord);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     JSONArray users = actionModel.getExtendConf().getJSONArray("users");
/*  87 */     List<BpmCarbonCopyReceive> bpmCarbonCopyReceiveList = new ArrayList<>();
/*  88 */     List<JmsDTO> jmsDto = new ArrayList<>();
/*  89 */     List<SysIdentity> identities = new ArrayList<>();
/*  90 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/*  91 */     bpmCarbonCopyReceive.setCcRecordId(bpmCarbonCopyRecord.getId());
/*  92 */     bpmCarbonCopyReceive.setRead(Boolean.FALSE);
/*  93 */     bpmCarbonCopyReceive.setCreateTime(new Date());
/*  94 */     bpmCarbonCopyReceive.setCreateBy(bpmCarbonCopyRecord.getCreateBy());
/*  95 */     bpmCarbonCopyReceive.setUpdateTime(bpmCarbonCopyRecord.getUpdateTime());
/*  96 */     bpmCarbonCopyReceive.setUpdateBy(bpmCarbonCopyRecord.getUpdateBy());
/*  97 */     bpmCarbonCopyReceive.setVersion(Integer.valueOf(0));
/*  98 */     bpmCarbonCopyReceive.setDelete(Boolean.FALSE);
/*  99 */     users.forEach(obj -> {
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
/* 124 */     NotifyMessage message = new NotifyMessage(actionModel.getOpinion(), "", ContextUtil.getCurrentUser(), identities);
/* 125 */     message.setTag("待阅任务");
/* 126 */     Map<String, Object> extendVars = new HashMap<>();
/* 127 */     extendVars.put("detailId", actionModel.getInstanceId());
/* 128 */     extendVars.put("statue", "carbon");
/* 129 */     extendVars.put("name", ContextUtil.getCurrentUser().getFullname());
/* 130 */     extendVars.put("title", String.format("%s于%s发起了待阅任务《%s》", new Object[] { ContextUtil.getCurrentUser().getFullname(), 
/* 131 */             DateUtil.formatDate(new Date()), actionModel.getSubject() }));
/* 132 */     extendVars.put("type", "待办");
/* 133 */     extendVars.put("head", "待阅");
/* 134 */     message.setExtendVars(extendVars);
/* 135 */     jmsDto.add(new DefaultJmsDTO("inner", (Serializable)message));
/* 136 */     this.jmsProducer.sendToQueue(jmsDto);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean prepareActionDatas(DefaultInstanceActionCmd intanceCmdData) {
/* 141 */     IBpmInstance instance = (IBpmInstance)this.bpmInstanceManager.get(intanceCmdData.getInstanceId());
/* 142 */     if (instance == null) {
/* 143 */       throw new BusinessException(BpmStatusCode.INST_NOT_FOUND);
/*     */     }
/* 145 */     intanceCmdData.setBpmInstance(instance);
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionBefore(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doActionAfter(DefaultInstanceActionCmd actionModel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/* 162 */     return ActionType.CARBONINSTCOPY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/* 167 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 172 */     return Boolean.valueOf((nodeDef == null || nodeDef.getNodeId() == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 177 */     return "/bpm/task/carbonCopyActionDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 182 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/handler/CarbonInstCopyActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */