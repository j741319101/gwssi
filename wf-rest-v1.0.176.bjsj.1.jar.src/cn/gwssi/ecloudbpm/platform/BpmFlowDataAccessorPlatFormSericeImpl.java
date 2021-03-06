/*    */ package cn.gwssi.ecloudbpm.platform;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*    */ import com.dstz.bpm.api.engine.data.BpmFlowDataAccessor;
/*    */ import com.dstz.bpm.api.engine.data.result.BpmFlowData;
/*    */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*    */ import com.dstz.bpm.api.platform.IBpmFlowDataAccessorPlatFormSerice;
/*    */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*    */ import com.dstz.bpm.core.model.BpmDefinition;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BpmFlowDataAccessorPlatFormSericeImpl
/*    */   implements IBpmFlowDataAccessorPlatFormSerice
/*    */ {
/*    */   @Resource
/*    */   private BpmFlowDataAccessor bpmFlowDataAccessor;
/*    */   @Resource
/*    */   private BpmDefinitionManager bpmDefinitionManager;
/*    */   
/*    */   public BpmFlowData getStartFlowData(String defId, String flowKey, String instId, String taskId, String formType, Boolean readonly) {
/* 25 */     if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
/* 26 */       BpmDefinition def = this.bpmDefinitionManager.getByKey(flowKey);
/* 27 */       if (def == null) {
/* 28 */         throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
/*    */       }
/* 30 */       defId = def.getId();
/*    */     } 
/* 32 */     return this.bpmFlowDataAccessor.getStartFlowData(defId, instId, taskId, FormType.fromValue(formType), readonly);
/*    */   }
/*    */ 
/*    */   
/*    */   public BpmFlowData getFlowTaskData(String taskId, String taskOrgId, String formType) {
/* 37 */     return this.bpmFlowDataAccessor.getFlowTaskData(taskId, taskOrgId, FormType.fromValue(formType));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmFlowDataAccessorPlatFormSericeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */