package com.dstz.bpm.plugin.global.carboncopy.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.CarbonCopyStatus;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.core.util.RequestContext;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollUtil;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("carbonCopyReviewActionHandler")
public class CarbonCopyReviewActionHandler implements BuiltinActionHandler<BaseActionCmd> {
   private static final Logger logger = LoggerFactory.getLogger(CarbonCopyReviewActionHandler.class);
   @Autowired
   BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
   @Autowired
   BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
   @Autowired
   BpmTaskOpinionManager bpmTaskOpinionManager;
   @Autowired
   BpmInstanceManager bpmInstanceManager;

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public void execute(BaseActionCmd model) {
      String carbonId = model.getExtendConf().getString("carbonId");
      BpmCarbonCopyReceive receive = (BpmCarbonCopyReceive)this.bpmCarbonCopyReceiveManager.get(carbonId);
      BpmCarbonCopyRecord record = (BpmCarbonCopyRecord)this.bpmCarbonCopyRecordManager.get(receive.getCcRecordId());
      BpmInstance instance = (BpmInstance)this.bpmInstanceManager.get(record.getInstId());
      BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(carbonId);
      BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(record.getTaskId());
      if (opinion == null) {
         opinion = new BpmTaskOpinion();
      }

      opinion.setInstId(record.getInstId());
      opinion.setTaskId(carbonId);
      opinion.setTaskKey(record.getNodeId());
      opinion.setTaskName(record.getNodeName());
      opinion.setApprover(ContextUtil.getCurrentUserId());
      opinion.setApproverName(ContextUtil.getCurrentUserName());
      opinion.setApproveTime(new Date());
      opinion.setStatus(OpinionStatus.CARBON_COPY.getKey());
      opinion.setDurMs(0L);
      opinion.setOpinion(model.getOpinion());
      opinion.setActExecutionId(instance.getActInstId());
      if (taskOpinion != null) {
         opinion.setTrace(taskOpinion.getTrace());
         opinion.setActExecutionId(taskOpinion.getActExecutionId());
      }

      if (StringUtil.isEmpty(opinion.getId())) {
         this.bpmTaskOpinionManager.create(opinion);
         receive.setStatus(CarbonCopyStatus.REVIEWED.getKey());
         this.bpmCarbonCopyReceiveManager.update(receive);
      } else {
         this.bpmTaskOpinionManager.update(opinion);
      }

   }

   public ActionType getActionType() {
      return ActionType.CARBONCOPYREVIEW;
   }

   public int getSn() {
      return 7;
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      return Boolean.FALSE;
   }

   public Boolean isDefault() {
      return Boolean.TRUE;
   }

   public String getConfigPage() {
      return "/bpm/task/carbonCopyReviewDialog.html";
   }

   public String getDefaultGroovyScript() {
      return "";
   }

   public String getDefaultBeforeScript() {
      return "";
   }

   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
      if (bpmInstance == null) {
         return false;
      } else if (!readOnly) {
         return false;
      } else {
         String carbonId = RequestUtil.getString(RequestContext.getHttpServletRequest(), "carbonId");
         if (StringUtils.isEmpty(carbonId)) {
            return false;
         } else {
            BpmCarbonCopyReceive bpmCarbonCopyReceive = (BpmCarbonCopyReceive)this.bpmCarbonCopyReceiveManager.get(carbonId);
            if (bpmCarbonCopyReceive != null && CarbonCopyStatus.REVIEWED.getKey().equals(bpmCarbonCopyReceive.getStatus())) {
               return false;
            } else {
               List<BpmCarbonCopyReceive> receives = this.bpmCarbonCopyReceiveManager.getByParam(bpmInstance.getId(), ContextUtil.getCurrentUserId(), bpmNodeDef.getNodeId());
               return CollUtil.isNotEmpty(receives);
            }
         }
      }
   }
}
