/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ 
/*     */ import com.dstz.bpm.core.dao.BpmDefinitionDao;
/*     */ import com.dstz.bpm.core.manager.BpmDefOverallManager;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.overallview.BpmOverallView;
/*     */ import com.dstz.bpm.core.model.overallview.OverallViewExport;
/*     */ import com.dstz.bpm.core.model.overallview.OverallViewImportXml;
/*     */ import com.dstz.bpm.core.util.XmlCovertUtil;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMsgUtil;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.io.IoUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */
import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.repository.Deployment;
/*     */ import org.activiti.engine.repository.Model;
/*     */ import org.activiti.engine.repository.ProcessDefinition;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class

BpmDefOverallManagerImpl
/*     */   implements BpmDefOverallManager
/*     */ {
/*  39 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionManager;
/*     */   @Autowired
/*     */   RepositoryService repositoryService;
/*     */   @Resource
/*     */   BpmDefinitionDao bpmDefinitionDao;
/*     */   
/*     */   public BpmOverallView getBpmOverallView(String defId) {
/*  49 */     BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/*     */     
/*  51 */     BpmOverallView overallView = new BpmOverallView();
/*  52 */     overallView.setDefId(def.getId());
/*  53 */     overallView.setBpmDefinition(def);
/*     */     
/*  55 */     overallView.setDefSetting(JSON.parseObject(def.getDefSetting()));
/*  56 */     return overallView;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveBpmOverallView(BpmOverallView overAllView) {
/*  61 */     BpmDefinition def = overAllView.getBpmDefinition();
/*  62 */     def.setDefSetting(overAllView.getDefSetting().toJSONString());
/*  63 */     this.bpmDefinitionManager.update(def);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<BpmOverallView>> importPreview(String flowXml) throws Exception {
/*  68 */     OverallViewImportXml voerallViewExport = (OverallViewImportXml) XmlCovertUtil.covert2Object(flowXml, new Class[] { OverallViewImportXml.class });
/*     */     
/*  70 */     Map<String, List<BpmOverallView>> map = new HashMap<>();
/*     */     
/*  72 */     List<OverallViewExport> list = voerallViewExport.getBpmXmlList();
/*  73 */     for (OverallViewExport overallViewExport : list) {
/*  74 */       List<BpmOverallView> listAllView = new ArrayList<>();
/*  75 */       BpmOverallView overallView = new BpmOverallView();
/*  76 */       BpmDefinition definition = overallViewExport.getBpmDefinition();
/*     */       
/*  78 */       overallView.setIsUpdateVersion(Boolean.valueOf(true));
/*  79 */       overallView.setBpmDefinition(definition);
/*  80 */       overallView.setPermission(overallViewExport.getPermission());
/*  81 */       overallView.setBpmnXml(overallViewExport.getBpmnXml());
/*  82 */       overallView.setDefSetting(JSON.parseObject(definition.getDefSetting()));
/*  83 */       overallView.setModelJson(overallViewExport.getModelEditorJson());
/*  84 */       listAllView.add(overallView);
/*     */       
/*  86 */       BpmDefinition def = this.bpmDefinitionManager.getByKey(overallViewExport.getBpmDefinition().getKey());
/*  87 */       if (def != null) {
/*  88 */         BpmOverallView oldOverallView = getBpmOverallView(def.getId());
/*  89 */         listAllView.add(oldOverallView);
/*     */       } 
/*     */       
/*  92 */       map.put(overallViewExport.getBpmDefinition().getName(), listAllView);
/*     */     } 
/*     */     
/*  95 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void importSave(List<BpmOverallView> overAllViewList) {
/* 100 */     ThreadMsgUtil.addMsg("流程导入开始,共：" + overAllViewList.size() + "个流程需要导入");
/* 101 */     for (BpmOverallView overAllView : overAllViewList) {
/*     */       
/* 103 */       if (!checkOverAllView(overAllView))
/*     */         continue; 
/* 105 */       BpmDefinition newDefinition = overAllView.getBpmDefinition();
/* 106 */       BpmDefinition existDefinition = this.bpmDefinitionManager.getByKey(newDefinition.getKey());
/*     */ 
/*     */       
/*     */       try {
/* 110 */         if (existDefinition == null) {
/* 111 */           createOrUpVersionDefinition(overAllView, null);
/* 112 */           ThreadMsgUtil.addMsg(String.format("流程:“%s” key:【%s】创建导入成功！", new Object[] { newDefinition.getName(), newDefinition.getKey() }));
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 117 */         if (overAllView.getIsUpdateVersion().booleanValue()) {
/* 118 */           createOrUpVersionDefinition(overAllView, existDefinition);
/* 119 */           ThreadMsgUtil.addMsg(String.format("对流程“%s”key:【%s】进行版本升级成功！", new Object[] { newDefinition.getName(), newDefinition.getKey() }));
/*     */           
/*     */           continue;
/*     */         } 
/* 123 */         if (StringUtil.isNotEmpty(existDefinition.getId())) {
/* 124 */           copyXmlDefinition(existDefinition, newDefinition);
/* 125 */           existDefinition.setDefSetting(overAllView.getDefSetting().toJSONString());
/*     */         } 
/* 127 */         ProcessDefinition bpmnProcessDef = this.repositoryService.getProcessDefinition(existDefinition.getActDefId());
/*     */         
/* 129 */         this.repositoryService.addModelEditorSource(existDefinition.getActModelId(), overAllView.getModelJson().getBytes("utf-8"));
/* 130 */         this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), existDefinition.getKey() + ".bpmn20.xml", overAllView.getBpmnXml().getBytes("utf-8"));
/*     */         
/* 132 */         this.bpmDefinitionManager.update(existDefinition);
/* 133 */         ThreadMsgUtil.addMsg(String.format("对流程“%s”key:【%s】进行更新成功！", new Object[] { newDefinition.getName(), existDefinition.getKey() }));
/*     */       }
/* 135 */       catch (UnsupportedEncodingException e) {
/* 136 */         this.LOG.error("流程导入异常，utf-8 字符流获取失败！ 不支持的字符集", e);
/* 137 */         ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BPMN XML 转流失败！", new Object[] { newDefinition.getName(), newDefinition.getKey() }));
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */   private void createOrUpVersionDefinition(BpmOverallView overAllView, BpmDefinition existDefinition) throws UnsupportedEncodingException {
/* 153 */     BpmDefinition bpmDefinition = overAllView.getBpmDefinition();
/*     */     
/* 155 */     String processName = bpmDefinition.getKey() + ".bpmn20.xml";
/*     */ 
/*     */ 
/*     */     
/* 159 */     Deployment deployment = this.repositoryService.createDeployment().name(bpmDefinition.getKey()).addString(processName, overAllView.getBpmnXml()).deploy();
/* 160 */     ProcessDefinition proDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
/*     */ 
/*     */     
/* 163 */     String modelId = this.bpmDefinitionManager.createActModel(bpmDefinition);
/* 164 */     Model model = this.repositoryService.getModel(modelId);
/* 165 */     model.setDeploymentId(deployment.getId());
/* 166 */     this.repositoryService.saveModel(model);
/* 167 */     this.repositoryService.addModelEditorSource(modelId, overAllView.getModelJson().getBytes("utf-8"));
/* 168 */     int version = 0;
/*     */     
/* 170 */     String newDefId = IdUtil.getSuid();
/* 171 */     if (existDefinition != null) {
/* 172 */       existDefinition.setIsMain("N");
/* 173 */       existDefinition.setMainDefId(newDefId);
/* 174 */       this.bpmDefinitionManager.update(existDefinition);
/* 175 */       version = existDefinition.getVersion().intValue() + 1;
/*     */     } 
/*     */ 
/*     */     
/* 179 */     bpmDefinition.setId(newDefId);
/* 180 */     bpmDefinition.setIsMain("Y");
/* 181 */     bpmDefinition.setRev(Integer.valueOf(0));
/* 182 */     bpmDefinition.setVersion(Integer.valueOf(version));
/* 183 */     bpmDefinition.setCreateBy(ContextUtil.getCurrentUser().getUserId());
/* 184 */     bpmDefinition.setCreateTime(new Date());
/* 185 */     bpmDefinition.setDefSetting(overAllView.getDefSetting().toJSONString());
/*     */     
/* 187 */     bpmDefinition.setActDefId(proDefinition.getId());
/* 188 */     bpmDefinition.setActDeployId(deployment.getId());
/* 189 */     bpmDefinition.setActModelId(modelId);
/* 190 */     this.bpmDefinitionDao.create(bpmDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkOverAllView(BpmOverallView overAllView) {
/* 195 */     BpmDefinition newDefinition = overAllView.getBpmDefinition();
/* 196 */     if (StringUtil.isEmpty(newDefinition.getKey())) {
/* 197 */       ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BpmDefinition KEY 不能为空 ！", new Object[] { newDefinition.getName(), newDefinition.getKey() }));
/* 198 */       return false;
/*     */     } 
/*     */     
/* 201 */     if (StringUtil.isEmpty(overAllView.getBpmnXml())) {
/* 202 */       ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BPMN XML 为空 ！", new Object[] { newDefinition.getName(), newDefinition.getKey() }));
/* 203 */       return false;
/*     */     } 
/*     */     
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyXmlDefinition(BpmDefinition existDefinition, BpmDefinition newDefinition) {
/* 211 */     existDefinition.setName(newDefinition.getName());
/* 212 */     existDefinition.setDesc(newDefinition.getDesc());
/* 213 */     existDefinition.setTypeId(newDefinition.getTypeId());
/* 214 */     existDefinition.setDefSetting(newDefinition.getDefSetting());
/* 215 */     existDefinition.setSupportMobile(newDefinition.getSupportMobile());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> exportBpmDefinitions(String... defIds) throws Exception {
/* 220 */     Map<String, String> exportFiles = new HashMap<>();
/*     */     
/* 222 */     OverallViewImportXml overallViewImportXml = new OverallViewImportXml();
/* 223 */     for (String defId : defIds) {
/* 224 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 225 */       if (def == null || StringUtil.isEmpty(def.getActDefId())) {
/* 226 */         this.LOG.info("defId : {} 非可用流程，已经跳过导出！", defId);
/*     */       }
/*     */       else {
/*     */         
/* 230 */         OverallViewExport importXml = new OverallViewExport();
/* 231 */         importXml.setBpmDefinition(def);
/*     */         
/* 233 */         InputStream inputStream = this.repositoryService.getResourceAsStream(def.getActDeployId(), def.getKey() + ".bpmn20.xml");
/* 234 */         importXml.setBpmnXml(IoUtil.read(inputStream, "utf-8"));
/*     */         
/* 236 */         String modelEditorJson = new String(this.repositoryService.getModelEditorSource(def.getActModelId()), "utf-8");
/* 237 */         importXml.setModelEditorJson(modelEditorJson);
/*     */ 
/*     */         
/* 240 */         overallViewImportXml.addOverallViewExport(importXml);
/*     */       } 
/* 242 */     }  String xml = XmlCovertUtil.covert2Xml(overallViewImportXml);
/*     */     
/* 244 */     exportFiles.put("ecloudbpm-flow.xml", xml);
/* 245 */     return exportFiles;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmDefOverallManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */