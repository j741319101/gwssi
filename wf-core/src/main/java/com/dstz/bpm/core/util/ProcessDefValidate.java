/*     */ package com.dstz.bpm.core.util;
/*     */ 
/*     */ import com.dstz.form.api.model.FormCategory;
/*     */ import com.dstz.form.api.model.IFormDef;
/*     */ import com.dstz.form.api.service.FormService;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*     */ import com.dstz.bpm.api.engine.plugin.context.UserQueryPluginContext;
/*     */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import com.dstz.sys.api.model.SysConnectRecordDTO;
/*     */ import com.dstz.sys.api.service.SysConnectRecordService;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service
/*     */ public class ProcessDefValidate
/*     */ {
/*     */   public static final String MSG_TYPE_WARN = "warning";
/*     */   public static final String MSG_TYPE_ERROR = "danger";
/*     */   public static final String MSG_TYPE_INFO = "info";
/*     */   public static final String CONNECTiON_RECORD_FORM = "BPM-DEF-FORM";
/*     */   public static final String CONNECTiON_RECORD_SUB_DEF = "BPM-DEF-DUBDEF";
/*     */   @Resource
/*     */   private FormService formService;
/*     */   @Resource
/*     */   private SysConnectRecordService sysConnectRecordService;
/*     */   
/*     */   public void validate(BpmProcessDef processDef) {
/*  53 */     ThreadMapUtil.remove();
/*     */     
/*  55 */     this.sysConnectRecordService.removeBySourceId(processDef.getProcessDefinitionId(), null);
/*     */     
/*  57 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)processDef;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     validateForm(def);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     validateNodeUser(def);
/*     */ 
/*     */     
/*  72 */     validateMobileForm(def);
/*     */ 
/*     */     
/*  75 */     def.getBpmnNodeDefs().stream().filter(node -> (node.getType() == NodeType.EXCLUSIVEGATEWAY || node.getType() == NodeType.INCLUSIVEGATEWAY)).forEach(currentNode -> {
/*     */           if (currentNode.getOutcomeNodes().size() > 1) {
/*     */             currentNode.getOutcomeNodes().forEach(());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateCallActiviti(DefaultBpmProcessDef def) {
/*  88 */     def.getBpmnNodeDefs().stream().filter(node -> (node.getType() == NodeType.CALLACTIVITY)).forEach(currentNode -> {
/*     */           CallActivityNodeDef node = (CallActivityNodeDef)currentNode;
/*     */           if (StringUtil.isEmpty(node.getFlowKey())) {
/*     */             putMsgToThread("danger", String.format("请配置外部子流程节点 [%s] 的关联子流程 ！", new Object[] { currentNode.getName() }));
/*     */           } else {
/*     */             SysConnectRecordDTO record = new SysConnectRecordDTO();
/*     */             record.setId(IdUtil.getSuid());
/*     */             record.setType("BPM-DEF-DUBDEF");
/*     */             record.setSourceId(def.getProcessDefinitionId());
/*     */             record.setTargetId(node.getFlowKey());
/*     */             record.setNotice(String.format("当前流程正在以外部子流程的形式被流程 “%s” [%s] 引用，请先移除关联", new Object[] { def.getName(), def.getDefKey() }));
/*     */             this.sysConnectRecordService.save(record);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateNodeUser(DefaultBpmProcessDef def) {
/* 108 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 110 */     def.getBpmnNodeDefs().forEach(nodeDef -> {
/*     */           if (nodeDef.getType() == NodeType.USERTASK) {
/*     */             validateOneNodeUser(nodeDef, sb);
/*     */           } else if (nodeDef.getType() == NodeType.SUBPROCESS) {
/*     */             SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)nodeDef;
/*     */ 
/*     */             
/*     */             BpmProcessDef processDef = subProcessNodeDef.getChildBpmProcessDef();
/*     */ 
/*     */             
/*     */             if (processDef != null) {
/*     */               List<BpmNodeDef> subBpmNodeDefs = processDef.getBpmnNodeDefs();
/*     */               
/*     */               subBpmNodeDefs.forEach(());
/*     */             } 
/*     */           } 
/*     */         });
/*     */     
/* 128 */     if (sb.length() > 0) {
/* 129 */       sb.insert(0, "请配置以下节点候选人：<br>");
/* 130 */       putMsgToThread("danger", sb.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateOneNodeUser(BpmNodeDef nodeDef, StringBuilder sb) {
/* 135 */     boolean hasUserAssignPlgin = false;
/*     */     
/* 137 */     for (BpmPluginContext ctx : nodeDef.getBpmPluginContexts()) {
/* 138 */       if (ctx instanceof UserQueryPluginContext && !((UserQueryPluginContext)ctx).isEmpty()) {
/* 139 */         hasUserAssignPlgin = true;
/*     */       }
/*     */     } 
/*     */     
/* 143 */     if (!hasUserAssignPlgin) {
/* 144 */       if (sb.length() > 1) {
/* 145 */         sb.append("，");
/*     */       }
/* 147 */       sb.append("[").append(nodeDef.getName()).append("]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateForm(DefaultBpmProcessDef processDef) {
/* 154 */     BpmForm globalForm = processDef.getGlobalForm();
/*     */     
/* 156 */     List<BpmDataModel> boDefs = processDef.getDataModelList();
/*     */     
/* 158 */     checkForm(boDefs, globalForm, "全局表单");
/*     */     
/* 160 */     boolean noOneForm = true;
/* 161 */     Set<String> formKeys = new HashSet<>();
/*     */     
/* 163 */     if (globalForm != null && !globalForm.isFormEmpty()) {
/* 164 */       noOneForm = false;
/* 165 */       if (globalForm.getType() == FormCategory.INNER) {
/* 166 */         formKeys.add(globalForm.getFormValue());
/*     */       }
/*     */     } 
/*     */     
/* 170 */     for (BpmNodeDef nodeDef : processDef.getBpmnNodeDefs()) {
/* 171 */       BpmForm nodeForm = nodeDef.getForm();
/* 172 */       checkForm(boDefs, nodeForm, nodeDef.getName());
/* 173 */       if (nodeForm != null && !nodeForm.isFormEmpty()) {
/* 174 */         noOneForm = false;
/* 175 */         if (nodeForm.getType() == FormCategory.INNER) {
/* 176 */           formKeys.add(nodeForm.getFormValue());
/*     */         }
/*     */       } 
/*     */     } 
/* 180 */     if (noOneForm) {
/* 181 */       putMsgToThread("danger", "请配置流程表单 ！！！");
/*     */     }
/*     */ 
/*     */     
/* 185 */     saveFormConnects(formKeys, processDef);
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveFormConnects(Set<String> formKeys, DefaultBpmProcessDef processDef) {
/* 190 */     if (formKeys.isEmpty())
/* 191 */       return;  List<SysConnectRecordDTO> connectRecords = new ArrayList<>();
/*     */     
/* 193 */     for (String key : formKeys) {
/* 194 */       SysConnectRecordDTO record = new SysConnectRecordDTO();
/* 195 */       record.setId(IdUtil.getSuid());
/* 196 */       record.setType("BPM-DEF-FORM");
/* 197 */       record.setSourceId(processDef.getProcessDefinitionId());
/* 198 */       record.setTargetId(key);
/* 199 */       record.setNotice(String.format("流程 “%s” [%s] 正在使用当前表单", new Object[] { processDef.getName(), processDef.getDefKey() }));
/* 200 */       connectRecords.add(record);
/*     */     } 
/*     */     
/* 203 */     this.sysConnectRecordService.save(connectRecords);
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateMobileForm(DefaultBpmProcessDef processDef) {
/* 208 */     BpmForm globalForm = processDef.getGlobalMobileForm();
/* 209 */     List<BpmDataModel> boDefs = processDef.getDataModelList();
/*     */     
/* 211 */     checkForm(boDefs, globalForm, "移动端全局表单");
/*     */     
/* 213 */     boolean noOneForm = true;
/* 214 */     Set<String> formKeys = new HashSet<>();
/*     */     
/* 216 */     if (globalForm != null && !globalForm.isFormEmpty()) {
/* 217 */       noOneForm = false;
/* 218 */       if (globalForm.getType() == FormCategory.INNER) {
/* 219 */         formKeys.add(globalForm.getFormValue());
/*     */       }
/*     */     } 
/*     */     
/* 223 */     for (BpmNodeDef nodeDef : processDef.getBpmnNodeDefs()) {
/* 224 */       BpmForm mobileNodeForm = nodeDef.getMobileForm();
/* 225 */       if (mobileNodeForm != null && !mobileNodeForm.isFormEmpty()) {
/* 226 */         noOneForm = false;
/* 227 */         if (mobileNodeForm.getType() == FormCategory.INNER) {
/* 228 */           formKeys.add(mobileNodeForm.getFormValue());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 234 */     if (processDef.getExtProperties().getSupportMobile().intValue() == 0 && !noOneForm) {
/* 235 */       putMsgToThread("warning", "流程配置尚未开启 ”支持移动端“，移动端表单的配置无效，请注意");
/*     */     }
/*     */     
/* 238 */     if (processDef.getExtProperties().getSupportMobile().intValue() != 0 && noOneForm) {
/* 239 */       putMsgToThread("danger", "请配置移动端表单！！！");
/*     */     }
/*     */     
/* 242 */     saveFormConnects(formKeys, processDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void putMsgToThread(String type, String msg) {
/* 249 */     List<String> msgList = (List<String>)ThreadMapUtil.getOrDefault(type, new ArrayList());
/* 250 */     msgList.add(msg);
/* 251 */     ThreadMapUtil.put(type, msgList);
/*     */   }
/*     */   
/*     */   private void checkForm(List<BpmDataModel> boDefs, BpmForm form, String nodeName) {
/* 255 */     if (form == null || form.isFormEmpty())
/*     */       return; 
/* 257 */     if (FormCategory.FRAME == form.getType()) {
/* 258 */       if (StringUtil.isNotEmpty(form.getFormValue()) && StringUtil.isEmpty(form.getFormHandler())) {
/* 259 */         putMsgToThread("info", nodeName + " URL表单未配置表单处理器，请确认是否不需要持久化页面数据？");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 264 */     IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
/* 265 */     if (formDef == null) {
/* 266 */       throw new BusinessMessage(String.format("在线表单 [%s] 已经被移除！请重新配置[%s]的表单！！!", new Object[] { form.getName(), nodeName }));
/*     */     }
/*     */     
/* 269 */     boolean hasBo = false;
/* 270 */     for (BpmDataModel dm : boDefs) {
/* 271 */       if (dm.getCode().equals(formDef.getBoKey())) {
/* 272 */         hasBo = true;
/*     */       }
/*     */     } 
/*     */     
/* 276 */     if (!hasBo)
/* 277 */       throw new BusinessMessage(String.format("在线表单 [%s] 对应的业务对象已经被移除！请重新配置[%s]的表单！！!", new Object[] { form.getName(), nodeName })); 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/util/ProcessDefValidate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */