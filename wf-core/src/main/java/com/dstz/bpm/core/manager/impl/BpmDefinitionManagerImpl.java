/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ import com.dstz.bus.manager.BusinessPermissionManager;
/*     */ import com.dstz.bus.model.BusinessPermission;
/*     */ import com.dstz.bpm.act.cache.ActivitiDefCache;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.engine.event.BpmDefinitionUpdateEvent;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.eximport.DefinitionExpImport;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmDefinitionDuplicateDTO;
/*     */ import com.dstz.bpm.core.util.ProcessDefValidate;
/*     */ import com.dstz.bpm.core.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.bpmExpImport.BpmExpImport;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.BpmUserDTO;
/*     */ import com.dstz.sys.api.constant.EnvironmentConstant;
/*     */ import com.dstz.sys.api.service.SysAuthorizationService;
/*     */ import com.dstz.sys.core.manager.SysAuthorizationManager;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.map.MapBuilder;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */
import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.bpmn.converter.BpmnXMLConverter;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.editor.language.json.converter.BpmnJsonConverter;
/*     */ import org.activiti.engine.ProcessEngineConfiguration;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
/*     */ import org.activiti.engine.impl.bpmn.parser.BpmnParse;
/*     */ import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
/*     */ import org.activiti.engine.impl.context.Context;
/*     */ import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
/*     */ import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
/*     */ import org.activiti.engine.repository.Deployment;
/*     */ import org.activiti.engine.repository.Model;
/*     */ import org.activiti.engine.repository.ProcessDefinition;
/*     */ import org.apache.batik.transcoder.TranscoderInput;
/*     */ import org.apache.batik.transcoder.TranscoderOutput;
/*     */ import org.apache.batik.transcoder.image.PNGTranscoder;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ @Service("bpmDefinitionManager")
/*     */ public class BpmDefinitionManagerImpl extends BaseManager<String, BpmDefinition> implements BpmDefinitionManager {
/*  90 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionDao bpmDefinitionDao;
/*     */   
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   @Resource
/*     */   RepositoryService repositoryService;
/*     */   
/*     */   @Resource
/*     */   ProcessEngineConfiguration processEngineConfiguration;
/*     */   
/*     */   @Resource
/*     */   SysAuthorizationService sysAuthorizationService;
/*     */   @Resource
/*     */ BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   BusinessPermissionManager businessPermissionManager;
/*     */   @Resource
/*     */ ProcessDefValidate processDefValidate;
/*     */   @Resource
/*     */   SysConnectRecordService sysConnectRecordService;
/*     */   @Resource
/*     */   UserService userService;
/*     */   @Resource
/*     */   SysAuthorizationManager sysAuthorizationManager;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   public void create(BpmDefinition bpmDefinition) {
/* 122 */     if (StringUtil.isNotEmpty(bpmDefinition.getId())) {
/* 123 */       update(bpmDefinition);
/*     */       
/*     */       return;
/*     */     } 
/* 127 */     List<BpmDefinition> defList = this.bpmDefinitionDao.getByKey(bpmDefinition.getKey());
/* 128 */     if (CollectionUtil.isNotEmpty(defList)) {
/* 129 */       throw new BusinessMessage("流程定义Key重复：" + bpmDefinition.getKey());
/*     */     }
/*     */     
/* 132 */     bpmDefinition.setIsMain("Y");
/* 133 */     bpmDefinition.setStatus("draft");
/* 134 */     bpmDefinition.setVersion(Integer.valueOf(1));
/*     */     
/* 136 */     String defId = IdUtil.getSuid();
/* 137 */     bpmDefinition.setId(defId);
/* 138 */     bpmDefinition.setMainDefId(defId);
/*     */     
/* 140 */     String modelId = createActModel(bpmDefinition);
/*     */     
/* 142 */     bpmDefinition.setActModelId(modelId);
/* 143 */     this.bpmDefinitionDao.create(bpmDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createActModel(BpmDefinition bpmDefinition) {
/*     */     try {
/* 150 */       ObjectMapper objectMapper = new ObjectMapper();
/* 151 */       ObjectNode editorNode = objectMapper.createObjectNode();
/* 152 */       editorNode.put("id", "canvas");
/* 153 */       editorNode.put("resourceId", "canvas");
/* 154 */       ObjectNode stencilSetNode = objectMapper.createObjectNode();
/* 155 */       stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
/* 156 */       editorNode.set("stencilset", (JsonNode)stencilSetNode);
/* 157 */       Model modelData = this.repositoryService.newModel();
/*     */       
/* 159 */       ObjectNode modelObjectNode = objectMapper.createObjectNode();
/* 160 */       modelObjectNode.put("name", bpmDefinition.getName());
/* 161 */       modelObjectNode.put("revision", 1);
/* 162 */       modelObjectNode.put("key", bpmDefinition.getKey());
/*     */       
/* 164 */       modelObjectNode.put("description", bpmDefinition.getDesc());
/* 165 */       modelData.setMetaInfo(modelObjectNode.toString());
/* 166 */       modelData.setName(bpmDefinition.getName());
/* 167 */       modelData.setKey(bpmDefinition.getKey());
/*     */       
/* 169 */       this.repositoryService.saveModel(modelData);
/*     */       
/* 171 */       this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
/* 172 */       return modelData.getId();
/* 173 */     } catch (UnsupportedEncodingException e) {
/* 174 */       throw new RuntimeException("创建activiti流程定义失败！", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Map> getDefNumByTree() {
/* 181 */     return this.bpmDefinitionDao.getDefNumByTree();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmDefinition updateBpmnModel(Model model, Map<String, String> values) throws Exception {
/* 191 */     String bpmDefSettingJSON = values.get("bpmDefSetting");
/*     */     
/* 193 */     BpmDefinition bpmDef = getDefByActModelId(model.getId());
/*     */     
/* 195 */     if (StringUtils.isNotEmpty(bpmDef.getId())) {
/* 196 */       BpmDefinition bpmDefinition = get(bpmDef.getId());
/* 197 */       String lockBy = bpmDefinition.getLockBy();
/* 198 */       String currentUserId = this.iCurrentContext.getCurrentUserId();
/* 199 */       if (StringUtils.isNotEmpty(lockBy) && 
/* 200 */         !currentUserId.equals(lockBy)) {
/* 201 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 202 */         IUser user = this.userService.getUserById(lockBy);
/* 203 */         throw new BusinessException("保存失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinition.getLockTime()) + "]锁定");
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     bpmDef.setName(values.get("name"));
/* 208 */     bpmDef.setDesc(values.get("description"));
/* 209 */     bpmDef.setRev(Integer.valueOf(values.get("rev")));
/* 210 */     bpmDef.setDefSetting(bpmDefSettingJSON);
/*     */ 
/*     */     
/* 213 */     byte[] jsonXml = ((String)values.get("json_xml")).getBytes("utf-8");
/* 214 */     ObjectNode modelNode = (ObjectNode)(new ObjectMapper()).readTree(jsonXml);
/* 215 */     BpmnModel bpmnModel = (new BpmnJsonConverter()).convertToBpmnModel((JsonNode)modelNode);
/* 216 */     if (CollectionUtil.isEmpty(bpmnModel.getProcesses())) {
/* 217 */       throw new BusinessMessage("请绘制流程图后再保存！");
/*     */     }
/*     */ 
/*     */     
/* 221 */     if (!bpmnModel.getProcesses().isEmpty()) {
/* 222 */       ((Process)bpmnModel.getProcesses().get(0)).setName(bpmDef.getName());
/* 223 */       ((Process)bpmnModel.getProcesses().get(0)).setDocumentation(bpmDef.getDesc());
/*     */       
/* 225 */       ((Process)bpmnModel.getProcesses().get(0)).setId(bpmDef.getKey());
/*     */     } 
/*     */     
/* 228 */     byte[] bpmnBytes = (new BpmnXMLConverter()).convertToXML(bpmnModel);
/* 229 */     String oldDefId = bpmDef.getId();
/* 230 */     boolean publish = Boolean.parseBoolean(values.get("publish"));
/* 231 */     if (StringUtil.isEmpty(bpmDef.getActDefId()) || publish) {
/* 232 */       bpmDef = deploy(bpmDef, model, values, bpmnBytes);
/*     */     } else {
/* 234 */       saveModelInfo(model, values);
/* 235 */       updateProcessDef(bpmDef, model, bpmnBytes);
/*     */     } 
/*     */     
/* 238 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(bpmDef.getId());
/* 239 */     Set<String> nodeIds = new HashSet<>();
/* 240 */     nodeIds.add(bpmDef.getKey() + "-global");
/* 241 */     for (BpmNodeDef nodeDef : processDef.getBpmnNodeDefs()) {
/* 242 */       nodeIds.add(bpmDef.getKey() + "-" + nodeDef.getNodeId());
/* 243 */       if (nodeDef instanceof SubProcessNodeDef) {
/* 244 */         BpmDefinition finalBpmDef = bpmDef;
/* 245 */         ((SubProcessNodeDef)nodeDef).getChildBpmProcessDef().getBpmnNodeDefs().forEach(subNodeDef -> nodeIds.add(finalBpmDef.getKey() + "-" + subNodeDef.getNodeId()));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 250 */     this.businessPermissionManager.removeNotInBpmNode(bpmDef.getId(), bpmDef.getKey(), nodeIds);
/*     */ 
/*     */     
/* 253 */     if (!StringUtils.equals(oldDefId, bpmDef.getId())) {
/*     */       
/* 255 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 256 */       defaultQueryFilter.addFilter("def_id_", oldDefId, QueryOP.EQUAL);
/* 257 */       List<BusinessPermission> businessPermissions = this.businessPermissionManager.query((QueryFilter)defaultQueryFilter);
/* 258 */       String newDefId = bpmDef.getId();
/* 259 */       businessPermissions.forEach(businessPermission -> {
/*     */             businessPermission.setId(IdUtil.getSuid());
/*     */             businessPermission.setDefId(newDefId);
/*     */             this.businessPermissionManager.create(businessPermission);
/*     */           });
/*     */     } else {
/* 265 */       oldDefId = null;
/*     */     } 
/* 267 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 268 */     for (IExtendTaskAction iExtendTaskAction : extendTaskActions) {
/* 269 */       iExtendTaskAction.doSomethingWhenSaveDef((BpmProcessDef)processDef, oldDefId);
/*     */     }
/* 271 */     return bpmDef;
/*     */   }
/*     */   
/*     */   private void saveModelInfo(Model model, Map<String, String> values) {
/* 275 */     ByteArrayOutputStream outStream = new ByteArrayOutputStream();
/*     */     try {
/* 277 */       byte[] jsonXml = ((String)values.get("json_xml")).getBytes("utf-8");
/* 278 */       byte[] svg_xml = null;
/* 279 */       if (values.containsKey("pngByte")) {
/* 280 */         svg_xml = ((String)values.get("pngByte")).getBytes("utf-8");
/*     */       } else {
/* 282 */         InputStream svgStream = new ByteArrayInputStream(((String)values.get("svg_xml")).getBytes("utf-8"));
/* 283 */         TranscoderInput input = new TranscoderInput(svgStream);
/* 284 */         PNGTranscoder transcoder = new PNGTranscoder();
/*     */         
/* 286 */         TranscoderOutput output = new TranscoderOutput(outStream);
/*     */         
/* 288 */         transcoder.transcode(input, output);
/* 289 */         svg_xml = outStream.toByteArray();
/* 290 */         outStream.close();
/*     */       } 
/*     */       
/* 293 */       this.repositoryService.saveModel(model);
/* 294 */       this.repositoryService.addModelEditorSourceExtra(model.getId(), svg_xml);
/* 295 */       this.repositoryService.addModelEditorSource(model.getId(), jsonXml);
/* 296 */     } catch (Exception e) {
/* 297 */       throw new BusinessException("保存Model信息失败！", e);
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
/*     */   private void updateProcessDef(BpmDefinition definition, Model model, byte[] bpmnBytes) throws JsonProcessingException, IOException {
/* 312 */     ProcessDefinition bpmnProcessDef = this.repositoryService.getProcessDefinition(definition.getActDefId());
/*     */     
/* 314 */     ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl)this.processEngineConfiguration;
/* 315 */     Context.setProcessEngineConfiguration(conf);
/*     */     
/* 317 */     DeploymentManager deploymentManager = conf.getDeploymentManager();
/* 318 */     BpmnDeployer deployer = deploymentManager.getDeployers().get(0);
/*     */     
/* 320 */     DeploymentEntity deploy = this.repositoryService.createDeploymentQuery().deploymentId(definition.getActDeployId()).list().get(0);
/* 321 */     ByteArrayInputStream inputStream = new ByteArrayInputStream(bpmnBytes);
/*     */ 
/*     */ 
/*     */     
/* 325 */     BpmnParse bpmnParse = deployer.getBpmnParser().createParse().sourceInputStream(inputStream).setSourceSystemId(model.getKey() + ".bpmn20.xml").deployment(deploy).name(model.getKey() + ".bpmn20.xml");
/*     */     
/* 327 */     bpmnParse.execute();
/* 328 */     BpmnModel bpmnModel = bpmnParse.getBpmnModel();
/*     */     
/* 330 */     deploymentManager.getBpmnModelCache().add(bpmnProcessDef.getId(), bpmnModel);
/* 331 */     byte[] diagramBytes = IoUtil.readInputStream(this.processEngineConfiguration
/* 332 */         .getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", this.processEngineConfiguration
/* 333 */           .getActivityFontName(), this.processEngineConfiguration.getLabelFontName(), this.processEngineConfiguration
/* 334 */           .getAnnotationFontName(), this.processEngineConfiguration
/* 335 */           .getClassLoader()), null);
/*     */ 
/*     */     
/* 338 */     this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model.getKey() + ".bpmn20.xml", bpmnBytes);
/*     */     
/* 340 */     this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model
/* 341 */         .getKey() + "." + bpmnProcessDef.getKey() + ".png", diagramBytes);
/*     */ 
/*     */     
/* 344 */     setDefinitionProp(definition);
/*     */     
/* 346 */     update(definition);
/* 347 */     ActivitiDefCache.clearByDefId(definition.getActDefId());
/* 348 */     this.bpmProcessDefService.clean(definition.getId());
/* 349 */     this.bpmProcessDefService.getBpmProcessDef(definition.getId());
/* 350 */     publishEvent(definition);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setDefinitionProp(BpmDefinition bpmDef) {
/* 355 */     DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.initBpmProcessDef((IBpmDefinition)bpmDef);
/*     */ 
/*     */     
/* 358 */     if ("deploy".equals(bpmDef.getStatus()) && "deploy"
/* 359 */       .equals(def.getExtProperties().getStatus()) && 
/* 360 */       !AppUtil.getCtxEnvironment().contains(EnvironmentConstant.PROD.key())) {
/* 361 */       throw new BusinessMessage("除了生产环境外，已发布状态的流程禁止修改！");
/*     */     }
/*     */ 
/*     */     
/* 365 */     bpmDef.setStatus(def.getExtProperties().getStatus());
/* 366 */     bpmDef.setSupportMobile(def.getExtProperties().getSupportMobile());
/* 367 */     this.processDefValidate.validate((BpmProcessDef)def);
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
/*     */   private BpmDefinition deploy(BpmDefinition definition, Model preModel, Map<String, String> values, byte[] bpmnModelXml) throws UnsupportedEncodingException {
/* 380 */     String processName = definition.getKey() + ".bpmn20.xml";
/*     */     
/* 382 */     Deployment deployment = this.repositoryService.createDeployment().name(definition.getKey()).addString(processName, new String(bpmnModelXml, "utf-8")).deploy();
/*     */ 
/*     */     
/* 385 */     ProcessDefinition proDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
/* 386 */     if (proDefinition == null) {
/* 387 */       throw new RuntimeException("error   ");
/*     */     }
/*     */ 
/*     */     
/* 391 */     if (StringUtil.isEmpty(definition.getActDefId())) {
/* 392 */       definition.setActDefId(proDefinition.getId());
/* 393 */       definition.setActDeployId(deployment.getId());
/* 394 */       update(definition);
/* 395 */       saveModelInfo(preModel, values);
/* 396 */       return definition;
/*     */     } 
/*     */     
/* 399 */     String modelId = createActModel(definition);
/* 400 */     Model model = this.repositoryService.getModel(modelId);
/* 401 */     model.setDeploymentId(deployment.getId());
/* 402 */     this.repositoryService.saveModel(model);
/* 403 */     saveModelInfo(model, values);
/*     */ 
/*     */     
/* 406 */     String newDefId = IdUtil.getSuid();
/* 407 */     BpmDefinition main = this.bpmDefinitionDao.getMainByDefKey(definition.getKey());
/* 408 */     main.setIsMain("N");
/* 409 */     main.setMainDefId(newDefId);
/* 410 */     update(main);
/*     */     
/* 412 */     int maxVersion = this.bpmDefinitionDao.getFlowMaxVersion(definition.getKey());
/*     */ 
/*     */     
/* 415 */     String oldDefId = definition.getId();
/* 416 */     definition.setId(newDefId);
/* 417 */     definition.setIsMain("Y");
/* 418 */     definition.setRev(Integer.valueOf(0));
/* 419 */     definition.setMainDefId(newDefId);
/* 420 */     definition.setVersion(Integer.valueOf(maxVersion + 1));
/* 421 */     definition.setCreateBy(ContextUtil.getCurrentUser().getUserId());
/* 422 */     definition.setCreateTime(new Date());
/*     */     
/* 424 */     definition.setActDefId(proDefinition.getId());
/* 425 */     definition.setActDeployId(deployment.getId());
/* 426 */     definition.setActModelId(model.getId());
/* 427 */     this.bpmDefinitionDao.create(definition);
/*     */     
/* 429 */     this.bpmDefinitionDao.updateForMainVersion(newDefId, definition.getKey(), newDefId);
/* 430 */     return definition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmDefinition getDefByActModelId(String actModelId) {
/* 436 */     List<BpmDefinition> list = this.bpmDefinitionDao.getDefByActModelId(actModelId);
/* 437 */     if (CollectionUtil.isEmpty(list)) {
/* 438 */       throw new BusinessException("getDefByActModelId 查找失败modelId：" + actModelId);
/*     */     }
/* 440 */     if (list.size() > 1) {
/* 441 */       this.LOG.warn("getDefByActModelId 查找多条 modelId：" + actModelId);
/*     */     }
/*     */     
/* 444 */     for (BpmDefinition def : list) {
/* 445 */       if ("Y".equals(def.getIsMain())) {
/* 446 */         return def;
/*     */       }
/*     */     } 
/*     */     
/* 450 */     return list.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void publishEvent(BpmDefinition def) {
/* 455 */     List<BpmDefinition> defList = this.bpmDefinitionDao.getByKey(def.getKey());
/* 456 */     for (BpmDefinition defEntity : defList) {
/* 457 */       AppUtil.publishEvent((ApplicationEvent)new BpmDefinitionUpdateEvent((IBpmDefinition)defEntity));
/*     */     }
/* 459 */     AppUtil.publishEvent((ApplicationEvent)new BpmDefinitionUpdateEvent((IBpmDefinition)def));
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmDefinition getDefinitionByActDefId(String actDefId) {
/* 464 */     return this.bpmDefinitionDao.getByActDefId(actDefId);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmDefinition getByKey(String flowKey) {
/* 469 */     return this.bpmDefinitionDao.getMainByDefKey(flowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmDefinitionVO> getMyDefinitionList(String userId, QueryFilter queryFilter) {
/* 474 */     Map map = this.sysAuthorizationService.getUserRightsSql(RightsObjectConstants.FLOW, userId, "bpm_definition.key_");
/* 475 */     queryFilter.addParams(map);
/* 476 */     queryFilter.addFilter("status_", "forbidden", QueryOP.NOT_EQUAL);
/* 477 */     List<BpmDefinitionVO> lstBpmDefinitionTemp = this.bpmDefinitionDao.getMyDefinitionList(queryFilter);
/* 478 */     List<BpmDefinitionVO> lstBpmDefinition = new ArrayList<>();
/*     */     
/* 480 */     List<BpmUserDTO> lstOrg = this.userService.getUserOrgInfos(userId);
/* 481 */     if (null != lstBpmDefinitionTemp && null != lstOrg && lstBpmDefinitionTemp.size() > 0 && lstOrg.size() > 0) {
/* 482 */       for (BpmDefinitionVO bpmDefinitionVO : lstBpmDefinitionTemp) {
/* 483 */         List<BpmUserDTO> lstOrgTemp = new ArrayList<>();
/* 484 */         lstOrg.forEach(org -> {
/*     */               DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*     */               
/*     */               defaultQueryFilter.addFilter("rights_object_", "FLOW", QueryOP.EQUAL);
/*     */               defaultQueryFilter.addFilter("rights_target_", bpmDefinitionVO.getKey(), QueryOP.EQUAL);
/*     */               defaultQueryFilter.addFilter("rights_type_", "org", QueryOP.EQUAL);
/*     */               List temp = this.sysAuthorizationManager.query((QueryFilter)defaultQueryFilter);
/*     */               if (null != temp && temp.size() > 0) {
/*     */                 defaultQueryFilter = new DefaultQueryFilter();
/*     */                 defaultQueryFilter.addFilter("rights_object_", "FLOW", QueryOP.EQUAL);
/*     */                 defaultQueryFilter.addFilter("rights_target_", bpmDefinitionVO.getKey(), QueryOP.EQUAL);
/*     */                 defaultQueryFilter.addFilter("rights_permission_code_", org.getOrgId() + "-org", QueryOP.EQUAL);
/*     */                 temp = this.sysAuthorizationManager.query((QueryFilter)defaultQueryFilter);
/*     */                 if (null != temp && temp.size() > 0) {
/*     */                   lstOrgTemp.add(org);
/*     */                 }
/*     */               } else {
/*     */                 lstOrgTemp.add(org);
/*     */               } 
/*     */             });
/* 504 */         if (lstOrgTemp.size() > 1) {
/* 505 */           handleOrgName(lstOrgTemp);
/*     */         }
/* 507 */         lstOrgTemp.forEach(org -> {
/*     */               BpmDefinitionVO temp = (BpmDefinitionVO)BeanCopierUtils.transformBean(bpmDefinitionVO, BpmDefinitionVO.class);
/*     */               
/*     */               temp.setOrgId(org.getOrgId());
/*     */               if (lstOrgTemp.size() > 1) {
/*     */                 temp.setOrgName(org.getOrgName());
/*     */               }
/*     */               lstBpmDefinition.add(temp);
/*     */             });
/*     */       } 
/*     */     }
/* 518 */     return lstBpmDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleOrgName(List<BpmUserDTO> lstOrg) {
/* 527 */     HashSet<String> names = new HashSet<>();
/* 528 */     HashSet<String> namesTemp = new HashSet<>();
/* 529 */     lstOrg.forEach(org -> {
/*     */           if (names.contains(org.getOrgName())) {
/*     */             namesTemp.add(org.getOrgName());
/*     */           }
/*     */           names.add(org.getOrgName());
/*     */         });
/* 535 */     if (namesTemp.size() > 0) {
/* 536 */       lstOrg.forEach(org -> {
/*     */             if (namesTemp.contains(org.getOrgName()) && "3".equals(org.getOrgType())) {
/*     */               org.setOrgName(org.getParentOrgName() + "/" + org.getOrgName());
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String entityId) {
/* 546 */     BpmDefinition definition = (BpmDefinition)this.bpmDefinitionDao.get(entityId);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 551 */     if (isNotEmptyInstance(definition.getId())) {
/* 552 */       throw new BusinessMessage("该流程定义下存在流程实例，请勿删除！<br> 请清除数据后再来删除");
/*     */     }
/*     */     
/* 555 */     this.businessPermissionManager.removeByBpmDefKey(definition.getId(), "flow", definition.getKey());
/* 556 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 557 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(definition.getId());
/* 558 */     extendTaskActions.forEach(iExtendTaskAction -> iExtendTaskAction.doSomethingWhenDeleteDef(bpmProcessDef));
/* 559 */     AppUtil.publishEvent((ApplicationEvent)new BpmDefinitionUpdateEvent((IBpmDefinition)definition));
/* 560 */     this.bpmDefinitionDao.remove(definition.getId());
/* 561 */     if (StringUtil.isNotEmpty(definition.getActDeployId())) {
/* 562 */       this.repositoryService.deleteDeployment(definition.getActDeployId());
/*     */     }
/* 564 */     this.sysConnectRecordService.removeBySourceId(definition.getId(), null);
/* 565 */     if (StringUtil.isNotEmpty(definition.getActModelId())) {
/* 566 */       this.repositoryService.deleteModel(definition.getActModelId());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isNotEmptyInstance(String defId) {
/* 571 */     DefaultQueryFilter query = new DefaultQueryFilter();
/* 572 */     query.addFilter("def_id_", defId, QueryOP.EQUAL);
/* 573 */     List list = this.bpmInstanceManager.query((QueryFilter)query);
/* 574 */     return CollectionUtil.isNotEmpty(list);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(BpmDefinition entity) {
/* 579 */     entity.setUpdateTime(new Date());
/* 580 */     int updateRows = this.bpmDefinitionDao.update(entity).intValue();
/* 581 */     AppUtil.publishEvent((ApplicationEvent)new BpmDefinitionUpdateEvent((IBpmDefinition)entity));
/*     */     
/* 583 */     if (updateRows == 0) {
/* 584 */       throw new RuntimeException("流程定义更新失败，当前版本并非最新版本！已经刷新当前服务器缓存，请刷新页面重新修改再提交。id:" + entity.getId() + " reversion:" + entity
/* 585 */           .getRev());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefinition2Main(String definitionId) {
/* 591 */     BpmDefinition def = get(definitionId);
/* 592 */     def.setIsMain("Y");
/* 593 */     BpmDefinition oldDpmDefinition = getByKey(def.getKey());
/* 594 */     update(def);
/* 595 */     this.bpmDefinitionDao.updateForMainVersion(definitionId, def.getKey(), def.getId());
/* 596 */     BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(def.getId());
/* 597 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 598 */     extendTaskActions.forEach(iExtendTaskAction -> iExtendTaskAction.doSomethingWhenSaveDef(bpmProcessDef, oldDpmDefinition.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearBpmnModelCache(String actDefId) {
/* 603 */     ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl)this.processEngineConfiguration;
/* 604 */     Context.setProcessEngineConfiguration(conf);
/*     */     
/* 606 */     DeploymentManager deploymentManager = conf.getDeploymentManager();
/* 607 */     if (StringUtil.isNotEmpty(actDefId)) {
/* 608 */       deploymentManager.getBpmnModelCache().remove(actDefId);
/*     */     } else {
/* 610 */       deploymentManager.getBpmnModelCache().clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream bpmExport(String defIds) {
/* 617 */     String filePath = "/tmp/export";
/* 618 */     FileUtil.deleteFiles(filePath);
/* 619 */     FileUtil.deleteFiles(filePath + ".zip");
/* 620 */     List<BpmExpImport> expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);
/*     */     try {
/* 622 */       for (BpmExpImport expImport : expImports) {
/* 623 */         expImport.bpmExport(defIds, filePath);
/*     */       }
/* 625 */     } catch (Exception e) {
/* 626 */       throw new BusinessException("导出出错", e);
/*     */     } 
/* 628 */     InputStream in = null;
/*     */     try {
/* 630 */       FileUtil.toZipDir(filePath, filePath + ".zip", true);
/* 631 */       in = new FileInputStream(filePath + ".zip");
/* 632 */     } catch (Exception e) {
/* 633 */       e.printStackTrace();
/*     */     } 
/* 635 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bpmImport(File file) {
/* 640 */     String filePath = "/tmp/export";
/* 641 */     FileUtil.deleteFiles(filePath);
/* 642 */     FileUtil.deleteFiles(filePath + ".zip");
/*     */     try {
/* 644 */       FileUtil.unZipFiles(file, filePath);
/* 645 */     } catch (Exception e) {
/* 646 */       throw new BusinessException(e);
/*     */     } 
/* 648 */     List<BpmExpImport> expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);
/* 649 */     for (BpmExpImport expImport : expImports) {
/*     */       try {
/* 651 */         expImport.bpmImport(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1));
/* 652 */       } catch (Exception e) {
/* 653 */         throw new BusinessException("导入出错", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String checkImport(File file) {
/* 660 */     String filePath = "/tmp/export";
/* 661 */     FileUtil.deleteFiles(filePath);
/* 662 */     FileUtil.deleteFiles(filePath + ".zip");
/*     */     try {
/* 664 */       FileUtil.unZipFiles(file, filePath);
/* 665 */     } catch (Exception e) {
/* 666 */       throw new BusinessException(e);
/*     */     } 
/* 668 */     StringBuffer sb = new StringBuffer();
/* 669 */     List<BpmExpImport> expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);
/* 670 */     for (BpmExpImport expImport : expImports) {
/*     */       try {
/* 672 */         sb.append(expImport.checkImport(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1))).append("\n");
/* 673 */       } catch (Exception e) {
/* 674 */         throw new BusinessException("检查出错", e);
/*     */       } 
/*     */     } 
/* 677 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   @Transactional(rollbackFor = {Exception.class})
/*     */   public BpmDefinition duplicate(BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO) {
/* 683 */     if (this.bpmDefinitionDao.countByKey(bpmDefinitionDuplicateDTO.getNewKey())) {
/* 684 */       throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getNewKey() + "】已存在");
/*     */     }
/* 686 */     List<BpmDefinition> originBpmDefinitionList = this.bpmDefinitionDao.getByKey(bpmDefinitionDuplicateDTO.getOriginKey());
/* 687 */     if (CollectionUtil.isEmpty(originBpmDefinitionList)) {
/* 688 */       throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getOriginKey() + "】不存在");
/*     */     }
/* 690 */     BpmDefinition originBpmDefinition = null;
/* 691 */     for (BpmDefinition bpmDefinition : originBpmDefinitionList) {
/* 692 */       if (StringUtils.equals(bpmDefinition.getIsMain(), "Y")) {
/* 693 */         originBpmDefinition = bpmDefinition;
/*     */         break;
/*     */       } 
/*     */     } 
/* 697 */     if (originBpmDefinition == null) {
/* 698 */       throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getOriginKey() + "】主版本不存在");
/*     */     }
/* 700 */     BpmnModel bpmnModel = this.repositoryService.getBpmnModel(originBpmDefinition.getActDefId());
/* 701 */     bpmnModel.getMainProcess().setId(bpmDefinitionDuplicateDTO.getNewKey());
/* 702 */     bpmnModel.getMainProcess().setName(bpmDefinitionDuplicateDTO.getNewName());
/* 703 */     String bpmxXml = new String((new BpmnXMLConverter()).convertToXML(bpmnModel, StandardCharsets.UTF_8.displayName()), StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 708 */     Deployment deployment = this.repositoryService.createDeployment().name(bpmDefinitionDuplicateDTO.getNewKey()).addString(StringUtils.join((Object[])new String[] { bpmDefinitionDuplicateDTO.getNewKey(), ".bpmn20.xml" }, ), bpmxXml).deploy();
/* 709 */     ProcessDefinition processDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
/* 710 */     Model model = duplicateActModel(bpmDefinitionDuplicateDTO, originBpmDefinition, deployment);
/*     */     
/* 712 */     BpmDefinition newBpmDefinition = new BpmDefinition();
/* 713 */     newBpmDefinition.setId(IdUtil.getSuid());
/* 714 */     newBpmDefinition.setName(bpmDefinitionDuplicateDTO.getNewName());
/* 715 */     newBpmDefinition.setKey(bpmDefinitionDuplicateDTO.getNewKey());
/* 716 */     newBpmDefinition.setDesc(bpmDefinitionDuplicateDTO.getNewDesc());
/* 717 */     newBpmDefinition.setTypeId(bpmDefinitionDuplicateDTO.getNewTypeId());
/* 718 */     newBpmDefinition.setStatus(originBpmDefinition.getStatus());
/* 719 */     newBpmDefinition.setActDefId(processDefinition.getId());
/* 720 */     newBpmDefinition.setActModelId(model.getId());
/* 721 */     newBpmDefinition.setActDeployId(deployment.getId());
/* 722 */     newBpmDefinition.setVersion(NumberUtils.INTEGER_ONE);
/* 723 */     newBpmDefinition.setIsMain("Y");
/* 724 */     newBpmDefinition.setCreateBy(ContextUtil.getCurrentUserId());
/* 725 */     newBpmDefinition.setCreateTime(new Date());
/* 726 */     newBpmDefinition.setCreateOrgId(ContextUtil.getCurrentGroupId());
/* 727 */     newBpmDefinition.setUpdateBy(newBpmDefinition.getCreateBy());
/* 728 */     newBpmDefinition.setUpdateTime(newBpmDefinition.getCreateTime());
/* 729 */     newBpmDefinition.setSupportMobile(originBpmDefinition.getSupportMobile());
/* 730 */     newBpmDefinition.setRev(NumberUtils.INTEGER_ZERO);
/* 731 */     JSONObject defSetting = JSON.parseObject(originBpmDefinition.getDefSetting());
/* 732 */     defSetting.put("bpmDefinition", newBpmDefinition);
/* 733 */     newBpmDefinition.setDefSetting(defSetting.toJSONString());
/* 734 */     this.bpmDefinitionDao.create(newBpmDefinition);
/* 735 */     BpmProcessDef newBpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(newBpmDefinition.getId());
/* 736 */     String oldDefId = originBpmDefinition.getId();
/*     */ 
/*     */     
/* 739 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 740 */     defaultQueryFilter.addFilter("def_id_", oldDefId, QueryOP.EQUAL);
/* 741 */     List<BusinessPermission> businessPermissions = this.businessPermissionManager.query((QueryFilter)defaultQueryFilter);
/* 742 */     businessPermissions.forEach(businessPermission -> {
/*     */           businessPermission.setId(IdUtil.getSuid());
/*     */           businessPermission.setDefId(newBpmDefinition.getId());
/*     */           this.businessPermissionManager.create(businessPermission);
/*     */         });
/* 747 */     AppUtil.getImplInstanceArray(IExtendTaskAction.class).forEach(action -> action.doSomethingWhenSaveDef(newBpmProcessDef, oldDefId));
/*     */     
/* 749 */     return newBpmDefinition;
/*     */   }
/*     */ 
/*     */   
/*     */   private Model duplicateActModel(BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO, BpmDefinition originBpmDefinition, Deployment deployment) {
/* 754 */     Model model = this.repositoryService.newModel();
/* 755 */     model.setKey(bpmDefinitionDuplicateDTO.getNewKey());
/* 756 */     model.setName(bpmDefinitionDuplicateDTO.getNewName());
/* 757 */     model.setDeploymentId(deployment.getId());
/* 758 */     model.setMetaInfo(JSON.toJSONString(
/* 759 */           MapBuilder.create(new LinkedHashMap<>())
/* 760 */           .put("key", bpmDefinitionDuplicateDTO.getNewKey())
/* 761 */           .put("name", bpmDefinitionDuplicateDTO.getNewName())
/* 762 */           .put("description", bpmDefinitionDuplicateDTO.getNewDesc())
/* 763 */           .put("revision", "1")
/* 764 */           .build()));
/*     */     
/* 766 */     this.repositoryService.saveModel(model);
/* 767 */     this.repositoryService.addModelEditorSource(model.getId(), this.repositoryService.getModelEditorSource(originBpmDefinition.getActModelId()));
/* 768 */     this.repositoryService.addModelEditorSourceExtra(model.getId(), this.repositoryService.getModelEditorSourceExtra(originBpmDefinition.getActModelId()));
/* 769 */     return model;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream definitionExport(String defIds) {
/* 774 */     String filePath = "/tmp/export";
/* 775 */     FileUtil.deleteFiles(filePath);
/* 776 */     FileUtil.deleteFiles(filePath + ".zip");
/* 777 */     List<DefinitionExpImport> expImports = AppUtil.getImplInstanceArray(DefinitionExpImport.class);
/*     */     try {
/* 779 */       for (BpmExpImport expImport : expImports) {
/* 780 */         expImport.bpmExport(defIds, filePath);
/*     */       }
/* 782 */     } catch (Exception e) {
/* 783 */       throw new BusinessException("导出出错", e);
/*     */     } 
/* 785 */     InputStream in = null;
/*     */     try {
/* 787 */       FileUtil.toZipDir(filePath, filePath + ".zip", true);
/* 788 */       in = new FileInputStream(filePath + ".zip");
/* 789 */     } catch (Exception e) {
/* 790 */       e.printStackTrace();
/*     */     } 
/* 792 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   public String definitionImport(File file, boolean notPublish) {
/* 797 */     String filePath = "/tmp/export";
/* 798 */     FileUtil.deleteFiles(filePath);
/* 799 */     FileUtil.deleteFiles(filePath + ".zip");
/*     */     try {
/* 801 */       FileUtil.unZipFiles(file, filePath);
/* 802 */     } catch (Exception e) {
/* 803 */       throw new BusinessException(e);
/*     */     } 
/* 805 */     List<DefinitionExpImport> expImports = AppUtil.getImplInstanceArray(DefinitionExpImport.class);
/* 806 */     StringBuilder sMsg = new StringBuilder("开始导入\n");
/* 807 */     for (DefinitionExpImport expImport : expImports) {
/*     */       try {
/* 809 */         sMsg.append(expImport.bpmImportWithLog(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1), notPublish));
/* 810 */       } catch (Exception e) {
/* 811 */         throw new BusinessException("导入出错", e);
/*     */       } 
/*     */     } 
/* 814 */     sMsg.append("结束导入\n");
/* 815 */     return sMsg.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmDefinition> query(QueryFilter queryFilter) {
/* 821 */     List<BpmDefinition> lst = this.dao.query(queryFilter);
/* 822 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmDefinition get(String entityId) {
/* 827 */     BpmDefinition temp = (BpmDefinition)this.dao.get(entityId);
/* 828 */     return temp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void lock(String id) {
/* 833 */     String userId = this.iCurrentContext.getCurrentUserId();
/* 834 */     if (StringUtils.isNotEmpty(userId)) {
/* 835 */       BpmDefinition bpmDefinitionBase = (BpmDefinition)this.bpmDefinitionDao.get(id);
/* 836 */       if (StringUtils.isEmpty(bpmDefinitionBase.getLockBy())) {
/* 837 */         BpmDefinition bpmDefinition = new BpmDefinition();
/* 838 */         bpmDefinition.setId(id);
/* 839 */         bpmDefinition.setLockBy(userId);
/* 840 */         bpmDefinition.setLockTime(new Date());
/* 841 */         this.bpmDefinitionDao.lock(bpmDefinition);
/*     */       } else {
/* 843 */         if (userId.equals(bpmDefinitionBase.getLockBy())) {
/* 844 */           throw new BusinessMessage("已被您锁定,请勿重复操作");
/*     */         }
/* 846 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 847 */         IUser user = this.userService.getUserById(bpmDefinitionBase.getLockBy());
/* 848 */         throw new BusinessMessage("锁定失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinitionBase.getLockTime()) + "]锁定");
/*     */       } 
/*     */     } else {
/*     */       
/* 852 */       throw new BusinessMessage("当前用户信息丢失");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unlock(String id) {
/* 858 */     String userId = this.iCurrentContext.getCurrentUserId();
/* 859 */     if (StringUtils.isNotEmpty(userId)) {
/* 860 */       BpmDefinition bpmDefinitionBase = (BpmDefinition)this.bpmDefinitionDao.get(id);
/* 861 */       if (StringUtils.isEmpty(bpmDefinitionBase.getLockBy())) {
/* 862 */         throw new BusinessMessage("解锁失败,当前流程并没有上锁");
/*     */       }
/* 864 */       if (userId.equals(bpmDefinitionBase.getLockBy()) || this.iCurrentContext.isAdmin(this.iCurrentContext.getCurrentUser())) {
/* 865 */         BpmDefinition bpmDefinition = new BpmDefinition();
/* 866 */         bpmDefinition.setId(id);
/* 867 */         bpmDefinition.setLockBy("");
/* 868 */         this.bpmDefinitionDao.unlock(bpmDefinition);
/*     */       } else {
/* 870 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 871 */         IUser user = this.userService.getUserById(bpmDefinitionBase.getLockBy());
/* 872 */         throw new BusinessMessage("解锁失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinitionBase.getLockTime()) + "]锁定");
/*     */       } 
/*     */     } else {
/*     */       
/* 876 */       throw new BusinessMessage("当前用户信息丢失");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmDefinitionManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */