package com.dstz.bpm.platform;

import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
import com.dstz.bpm.api.engine.data.result.BpmFlowData;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.platform.IBpmFlowDataAccessorPlatFormSerice;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import javax.annotation.Resource;

import com.dstz.form.api.model.FormType;
import org.springframework.stereotype.Service;

@Service
public class BpmFlowDataAccessorPlatFormSericeImpl implements IBpmFlowDataAccessorPlatFormSerice {
   @Resource
   private BpmFlowDataAccessor bpmFlowDataAccessor;
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;

   public BpmFlowData getStartFlowData(String defId, String flowKey, String instId, String taskId, String formType, Boolean readonly) {
      if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
         BpmDefinition def = this.bpmDefinitionManager.getByKey(flowKey);
         if (def == null) {
            throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
         }

         defId = def.getId();
      }

      return this.bpmFlowDataAccessor.getStartFlowData(defId, instId, taskId, FormType.fromValue(formType), readonly);
   }

   public BpmFlowData getFlowTaskData(String taskId, String taskOrgId, String formType) {
      return this.bpmFlowDataAccessor.getFlowTaskData(taskId, taskOrgId, FormType.fromValue(formType));
   }
}
