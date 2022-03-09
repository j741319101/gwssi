/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.handler;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.CarbonCopyStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.BuiltinActionHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import cn.gwssi.ecloudframework.base.core.util.RequestContext;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.rest.util.RequestUtil;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component("carbonCopyReviewActionHandler")
/*     */ public class CarbonCopyReviewActionHandler
/*     */   implements BuiltinActionHandler<BaseActionCmd>
/*     */ {
/*  45 */   private static final Logger logger = LoggerFactory.getLogger(CarbonCopyReviewActionHandler.class);
/*     */   
/*     */   @Autowired
/*     */   BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*     */   @Autowired
/*     */   BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*     */   @Autowired
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Transactional(rollbackFor = {Exception.class})
/*     */   public void execute(BaseActionCmd model) {
/*  58 */     String carbonId = model.getExtendConf().getString("carbonId");
/*  59 */     BpmCarbonCopyReceive receive = (BpmCarbonCopyReceive)this.bpmCarbonCopyReceiveManager.get(carbonId);
/*  60 */     BpmCarbonCopyRecord record = (BpmCarbonCopyRecord)this.bpmCarbonCopyRecordManager.get(receive.getCcRecordId());
/*  61 */     BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(record.getInstId());
/*  62 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(carbonId);
/*  63 */     BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(record.getTaskId());
/*  64 */     if (opinion == null) {
/*  65 */       opinion = new BpmTaskOpinion();
/*     */     }
/*  67 */     opinion.setInstId(record.getInstId());
/*  68 */     opinion.setTaskId(carbonId);
/*  69 */     opinion.setTaskKey(record.getNodeId());
/*  70 */     opinion.setTaskName(record.getNodeName());
/*  71 */     opinion.setApprover(ContextUtil.getCurrentUserId());
/*  72 */     opinion.setApproverName(ContextUtil.getCurrentUserName());
/*  73 */     opinion.setApproveTime(new Date());
/*  74 */     opinion.setStatus(OpinionStatus.CARBON_COPY.getKey());
/*  75 */     opinion.setDurMs(Long.valueOf(0L));
/*  76 */     opinion.setOpinion(model.getOpinion());
/*  77 */     opinion.setActExecutionId(instance.getActInstId());
/*  78 */     if (taskOpinion != null) {
/*  79 */       opinion.setTrace(taskOpinion.getTrace());
/*  80 */       opinion.setActExecutionId(taskOpinion.getActExecutionId());
/*     */     } 
/*  82 */     if (StringUtil.isEmpty(opinion.getId())) {
/*  83 */       this.bpmTaskOpinionManager.create(opinion);
/*  84 */       receive.setStatus(CarbonCopyStatus.REVIEWED.getKey());
/*  85 */       this.bpmCarbonCopyReceiveManager.update(receive);
/*     */     } else {
/*  87 */       this.bpmTaskOpinionManager.update(opinion);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionType getActionType() {
/*  93 */     return ActionType.CARBONCOPYREVIEW;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSn() {
/*  98 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isSupport(BpmNodeDef nodeDef) {
/* 103 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isDefault() {
/* 108 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigPage() {
/* 113 */     return "/bpm/task/carbonCopyReviewDialog.html";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultGroovyScript() {
/* 118 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultBeforeScript() {
/* 123 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
/* 128 */     if (bpmInstance == null) {
/* 129 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 133 */     if (!readOnly) {
/* 134 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 138 */     String carbonId = RequestUtil.getString(RequestContext.getHttpServletRequest(), "carbonId");
/* 139 */     if (StringUtils.isEmpty(carbonId)) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = (BpmCarbonCopyReceive)this.bpmCarbonCopyReceiveManager.get(carbonId);
/* 144 */     if (bpmCarbonCopyReceive != null && CarbonCopyStatus.REVIEWED.getKey().equals(bpmCarbonCopyReceive.getStatus())) {
/* 145 */       return false;
/*     */     }
/*     */     
/* 148 */     List<BpmCarbonCopyReceive> receives = this.bpmCarbonCopyReceiveManager.getByParam(bpmInstance.getId(), ContextUtil.getCurrentUserId(), bpmNodeDef.getNodeId());
/* 149 */     return CollUtil.isNotEmpty(receives);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/handler/CarbonCopyReviewActionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */