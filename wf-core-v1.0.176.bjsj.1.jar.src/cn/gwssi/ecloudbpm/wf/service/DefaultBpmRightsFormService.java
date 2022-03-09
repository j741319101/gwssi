/*    */ package cn.gwssi.ecloudbpm.wf.service;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.constant.BusinessPermissionObjType;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
/*    */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessPermissionService;
/*    */ import cn.gwssi.ecloudbpm.form.api.model.FormCategory;
/*    */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.data.result.BpmFlowData;
/*    */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.form.BpmForm;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.form.DefaultForm;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmRightsFormService;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component("defaultBpmFormService")
/*    */ public class DefaultBpmRightsFormService
/*    */   implements BpmRightsFormService
/*    */ {
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   @Resource
/*    */   IBusinessPermissionService businessPermissionService;
/*    */   
/*    */   public IBusinessPermission getInstanceFormPermission(BpmFlowData flowData, String nodeId, FormType formType, boolean isReadOnly) {
/*    */     String str;
/* 37 */     IBusinessPermission permision = null;
/* 38 */     BpmForm form = null;
/* 39 */     boolean isMobile = (FormType.MOBILE == formType);
/* 40 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
/*    */ 
/*    */ 
/*    */     
/* 44 */     if (StringUtil.isEmpty(nodeId)) {
/* 45 */       form = isMobile ? processDef.getInstMobileForm() : processDef.getInstForm();
/* 46 */       nodeId = "instance";
/* 47 */       str = "实例";
/*    */     } else {
/* 49 */       BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
/* 50 */       form = isMobile ? nodeDef.getMobileForm() : nodeDef.getForm();
/* 51 */       str = nodeDef.getName();
/*    */     } 
/*    */ 
/*    */     
/* 55 */     if (form == null || form.isFormEmpty()) {
/* 56 */       form = isMobile ? processDef.getGlobalMobileForm() : processDef.getGlobalForm();
/* 57 */       nodeId = "global";
/* 58 */       str = "全局";
/*    */     } 
/*    */     
/* 61 */     if (form == null || form.isFormEmpty()) {
/* 62 */       throw new BusinessException(String.format("请配置流程“%s”的“%s”表单", new Object[] { processDef.getName(), str }), BpmStatusCode.FLOW_FORM_LOSE);
/*    */     }
/*    */ 
/*    */     
/* 66 */     if (FormCategory.INNER.equals(form.getType())) {
/* 67 */       Integer defId = null;
/* 68 */       permision = this.businessPermissionService.getByObjTypeAndObjVal(flowData.getDefId(), BusinessPermissionObjType.FLOW
/* 69 */           .getKey(), processDef.getDefKey() + "-" + nodeId, processDef.getDataModelKeys(), true);
/* 70 */       flowData.setPermission(permision.getPermission(isReadOnly));
/* 71 */       flowData.setTablePermission(permision.getTablePermission(isReadOnly));
/*    */     } 
/*    */     
/* 74 */     DefaultForm bpmForm = new DefaultForm();
/* 75 */     bpmForm.setFormHandler(form.getFormHandler());
/* 76 */     bpmForm.setFormHtml(form.getFormHtml());
/* 77 */     bpmForm.setFormValue(form.getFormValue());
/* 78 */     bpmForm.setName(form.getName());
/* 79 */     bpmForm.setType(form.getType());
/* 80 */     flowData.setForm((BpmForm)bpmForm);
/*    */     
/* 82 */     return permision;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IBusinessPermission getNodeSavePermission(String defId, String defKey, String nodeId, Set<String> bocodes) {
/* 88 */     String boCodes = null;
/* 89 */     String[] ss = null;
/* 90 */     if (CollectionUtil.isNotEmpty(bocodes)) {
/* 91 */       boCodes = StringUtil.join(bocodes);
/*    */     }
/*    */     
/* 94 */     return this.businessPermissionService.getByObjTypeAndObjVal(defId, BusinessPermissionObjType.FLOW
/* 95 */         .getKey(), defKey + "-" + nodeId, boCodes, true);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/service/DefaultBpmRightsFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */