/*     */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessTableService;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessColumnManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.form.generator.AbsFormElementGenerator;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormCustomConfHistoryManager;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormDefHistoryManager;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormDefManager;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormTemplateManager;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormCustomConf;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormCustomConfHistory;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormDef;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormDefHistory;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormImportModel;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormTemplate;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormGroup;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormOverallArrangement;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormTab;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.PropertyUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.freemark.IFreemarkerEngine;
/*     */ import com.dstz.sys.api.model.ISysTreeNode;
/*     */ import com.dstz.sys.api.service.ISysTreeNodeService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.io.FileUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.select.Elements;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ @Service("formDefManager")
/*     */ public class FormDefManagerImpl extends BaseManager<String, FormDef> implements FormDefManager {
/*  64 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   FormDefDao formDefDao;
/*     */   @Autowired
/*     */   ISysTreeNodeService sysTreeNodeService;
/*     */   @Autowired
/*     */   IBusinessObjectService businessObjectService;
/*     */   @Autowired
/*     */   IBusinessTableService businessTableService;
/*     */   @Autowired
/*     */   FormTemplateManager formTemplateManager;
/*     */   @Autowired
/*     */   IFreemarkerEngine freemarkEngine;
/*     */   @Autowired
/*     */   BusinessPermissionManager businessPermissionManager;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Autowired
/*     */   ISysTreeNodeService iSysTreeNodeService;
/*     */   @Autowired
/*     */   FormCustomConfManager formCustomConfManager;
/*     */   @Autowired
/*     */   FormDefHistoryManager formDefHistoryManager;
/*     */   @Autowired
/*     */   FormCustomConfHistoryManager formCustomConfHistoryManager;
/*     */   @Autowired
/*     */   BusinessColumnManager businessColumnManager;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public FormDef getByKey(String key) {
/*  96 */     FormDef form = this.formDefDao.getByKey(key);
/*     */     
/*  98 */     return form;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveBackupHtml(FormDef formDef) {
/* 103 */     String formDefPath = PropertyUtil.getFormDefBackupPath();
/* 104 */     if (StringUtil.isEmpty(formDefPath)) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     ISysTreeNode node = this.sysTreeNodeService.getById(formDef.getGroupId());
/* 109 */     String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
/* 110 */     FileUtil.writeUtf8String(formDef.getHtml(), fileName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBackupHtml(FormDef formDef) {
/* 115 */     String formDefPath = PropertyUtil.getFormDefBackupPath();
/* 116 */     if (StringUtil.isNotEmpty(formDefPath)) {
/* 117 */       ISysTreeNode node = this.sysTreeNodeService.getById(formDef.getGroupId());
/* 118 */       String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
/* 119 */       formDef.setHtml(FileUtil.readUtf8String(fileName));
/*     */     } 
/*     */     
/* 122 */     return formDef.getHtml();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateFormHtml(String boKey, JSONArray templateConfig, String fromType) {
/* 128 */     String boDesign = this.businessObjectService.getBoOverallArrangement(boKey);
/*     */     
/* 130 */     if (StringUtil.isNotEmpty(boDesign)) {
/*     */       
/* 132 */       if (FormType.PC_IVIEW.value().equals(fromType)) {
/* 133 */         return generateIviewOverallArrangementFormHtml(boDesign, boKey, templateConfig);
/*     */       }
/* 135 */       return generateVueOverallArrangementFormHtml(boDesign, boKey, templateConfig);
/*     */     } 
/*     */ 
/*     */     
/* 139 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(boKey);
/*     */     
/* 141 */     StringBuilder sb = new StringBuilder();
/* 142 */     for (Object object : templateConfig) {
/* 143 */       JSONObject jsonObject = (JSONObject)object;
/* 144 */       String templateKey = jsonObject.getString("templateKey");
/* 145 */       IBusTableRel relation = businessObject.getRelation().find(jsonObject.getString("tableKey"));
/* 146 */       FormTemplate template = this.formTemplateManager.getByKey(templateKey);
/* 147 */       if (template == null) {
/* 148 */         this.LOG.warn("表单模板{}不存在！", templateKey);
/*     */         continue;
/*     */       } 
/* 151 */       Map<String, Object> map = new HashMap<>();
/* 152 */       map.put("relation", relation);
/*     */ 
/*     */       
/* 155 */       for (AbsFormElementGenerator generator : AppUtil.getImplInstanceArray(AbsFormElementGenerator.class)) {
/* 156 */         map.put(generator.getGeneratorName(), generator);
/*     */       }
/*     */       
/* 159 */       String html = this.freemarkEngine.parseByString(template.getHtml(), map);
/*     */       
/* 161 */       sb.append(html);
/*     */     } 
/* 163 */     if (sb.length() > 0) {
/* 164 */       sb.insert(0, "<div class=\"ivu-form ivu-form-label-right\">");
/* 165 */       sb.append("</div>");
/*     */     } 
/*     */     
/* 168 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateIviewOverallArrangementFormHtml(String boDesign, String boKey, JSONArray templateConfig) {
/* 185 */     FormOverallArrangement formDesign = (FormOverallArrangement)JSON.toJavaObject((JSON)JSON.parseObject(boDesign), FormOverallArrangement.class);
/* 186 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(boKey);
/*     */ 
/*     */     
/* 189 */     StringBuffer html = new StringBuffer();
/* 190 */     if (CollectionUtil.isNotEmpty(formDesign.getTabList())) {
/* 191 */       html.append("<Tabs>");
/* 192 */       formDesign.getTabList().forEach(tab -> {
/*     */             html.append(" <tab-pane label=\"").append(tab.getComment()).append("\">");
/*     */ 
/*     */             
/*     */             html.append(generateGroupsHtml(tab.getGroupList(), businessObject, templateConfig));
/*     */ 
/*     */             
/*     */             html.append("</tab-pane> ");
/*     */           });
/*     */ 
/*     */       
/* 203 */       html.append("</Tabs>");
/*     */     } else {
/* 205 */       html.append(generateGroupsHtml(formDesign.getGroupList(), businessObject, templateConfig));
/*     */     } 
/*     */     
/* 208 */     if (html.length() > 0) {
/* 209 */       html.insert(0, "<div class=\"ivu-form ivu-form-label-right\"><script>\n    window.custFormComponentMixin ={\n        data: function () {\n            return {\"test\":\"helloWorld\"};\n        },\n        created:function(){\n            console.log(\"脚本将会混入自定义表单组件中...\");\n        },methods:{\n            testaaa:function(){alert(1)}\n        }\n    }\n</script>");
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
/*     */ 
/*     */       
/* 223 */       html.append("</div>");
/*     */     } 
/*     */     
/* 226 */     return html.toString();
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
/*     */   private String generateVueOverallArrangementFormHtml(String boDesign, String boKey, JSONArray templateConfig) {
/* 250 */     FormOverallArrangement formDesign = (FormOverallArrangement)JSON.toJavaObject((JSON)JSON.parseObject(boDesign), FormOverallArrangement.class);
/* 251 */     IBusinessObject businessObject = this.businessObjectService.getFilledByKey(boKey);
/*     */ 
/*     */     
/* 254 */     StringBuffer html = new StringBuffer();
/* 255 */     if (CollectionUtil.isNotEmpty(formDesign.getTabList())) {
/* 256 */       html.append("<ul class=\"nav nav-tabs\">");
/* 257 */       boolean isFirst = true;
/* 258 */       for (FormTab tab : formDesign.getTabList()) {
/* 259 */         html.append(
/* 260 */             String.format("<li %s ><a href=\"#%s\"  data-toggle=\"tab\" >%s</a></li>", new Object[] {
/* 261 */                 isFirst ? "class=\"active\"" : "", tab.getComment(), tab.getComment()
/*     */               }));
/* 263 */         isFirst = false;
/*     */       } 
/*     */ 
/*     */       
/* 267 */       html.append("</ul> ");
/*     */       
/* 269 */       isFirst = true;
/* 270 */       html.append("<div class=\"tab-content\">");
/* 271 */       for (FormTab tab : formDesign.getTabList()) {
/* 272 */         html.append("<div class=\"tab-pane ").append(isFirst ? "active" : "").append("\" id=\"").append(tab.getComment()).append("\">")
/* 273 */           .append(generateGroupsHtml(tab.getGroupList(), businessObject, templateConfig))
/* 274 */           .append("</div>");
/* 275 */         isFirst = false;
/*     */       } 
/* 277 */       html.append("</div>");
/*     */     } else {
/* 279 */       html.append(generateGroupsHtml(formDesign.getGroupList(), businessObject, templateConfig));
/*     */     } 
/*     */     
/* 282 */     if (html.length() > 0) {
/* 283 */       html.insert(0, "<div class=\"ivu-form ivu-form-label-right\"><script>\n    window.custFormComponentMixin ={\n        data: function () {\n            return {\"test\":\"helloWorld\"};\n        },\n        created:function(){\n            console.log(\"脚本将会混入自定义表单组件中...\");\n        },methods:{\n            testaaa:function(){alert(1)}\n        }\n    }\n</script>");
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
/*     */ 
/*     */ 
/*     */       
/* 298 */       html.append("</div>");
/*     */     } 
/*     */     
/* 301 */     return html.toString();
/*     */   }
/*     */   
/*     */   private String generateGroupsHtml(List<FormGroup> groupList, IBusinessObject businessObject, JSONArray templateConfig) {
/* 305 */     StringBuilder groupHtml = new StringBuilder();
/* 306 */     groupList.forEach(group -> {
/*     */           String tableKey = group.getKey();
/*     */           
/*     */           String templateKey = getTemplateKey(templateConfig, tableKey);
/*     */           
/*     */           IBusTableRel relation = businessObject.getRelation().find(tableKey);
/*     */           
/*     */           FormTemplate template = this.formTemplateManager.getByKey(templateKey);
/*     */           
/*     */           Map<String, Object> map = new HashMap<>();
/*     */           
/*     */           map.put("relation", relation);
/*     */           
/*     */           group.setTableRelation(relation);
/*     */           
/*     */           map.put("group", group);
/*     */           for (AbsFormElementGenerator generator : AppUtil.getImplInstanceArray(AbsFormElementGenerator.class)) {
/*     */             map.put(generator.getGeneratorName(), generator);
/*     */           }
/*     */           this.LOG.debug("templateKey:{},tableKey:{}", templateKey, tableKey);
/*     */           String html = this.freemarkEngine.parseByString(template.getHtml(), map);
/*     */           groupHtml.append(html);
/*     */         });
/* 329 */     return groupHtml.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getTemplateKey(JSONArray templateConfig, String tableKey) {
/* 335 */     for (Object object : templateConfig) {
/* 336 */       JSONObject jsonObject = (JSONObject)object;
/* 337 */       if (jsonObject.getString("tableKey").equals(tableKey)) {
/* 338 */         return jsonObject.getString("templateKey");
/*     */       }
/*     */     } 
/*     */     
/* 342 */     throw new BusinessException("tableKey :" + tableKey + "尚未选择模板，请选择模板后生成表单");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map> queryWithBo(QueryFilter queryFilter) {
/* 347 */     List<Map> lst = this.formDefDao.queryWithBo(queryFilter);
/* 348 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map> getFormNumByTree() {
/* 353 */     return this.formDefDao.getFormNumByTree();
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateFormDef(FormDef formDef) {
/* 358 */     formDef.setUpdateBy(ContextUtil.getCurrentUserId());
/* 359 */     formDef.setUpdateTime(new Date());
/* 360 */     formDef.setVersion(Integer.valueOf(formDef.getVersion().intValue() + 1));
/* 361 */     return this.formDefDao.updateFormDef(formDef);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String id) {
/* 366 */     FormDef formDef = get(id);
/* 367 */     if (formDef != null)
/*     */     {
/* 369 */       this.businessPermissionManager.removeByBpmDefKey(null, "form", formDef.getKey());
/*     */     }
/* 371 */     this.formDefDao.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateByPrimaryKeySelective(FormDef formDef) {
/* 376 */     formDef.setUpdateBy(this.iCurrentContext.getCurrentUserId());
/* 377 */     this.formDefDao.updateByPrimaryKeySelective(formDef);
/*     */   }
/*     */ 
/*     */   
/*     */   public FormDef getWithoutHtml(String id) {
/* 382 */     FormDef form = this.formDefDao.getWithoutHtml(id);
/* 383 */     return form;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream definitionExport(String formIds) {
/* 388 */     String filePath = "/tmp/export";
/* 389 */     FileUtil.deleteFiles(filePath);
/* 390 */     FileUtil.deleteFiles(filePath + ".zip");
/*     */     try {
/* 392 */       formExport(formIds, filePath);
/* 393 */     } catch (Exception e) {
/* 394 */       throw new BusinessException("导出出错", e);
/*     */     } 
/* 396 */     InputStream in = null;
/*     */     try {
/* 398 */       FileUtil.toZipDir(filePath, filePath + ".zip", true);
/* 399 */       in = new FileInputStream(filePath + ".zip");
/* 400 */     } catch (Exception e) {
/* 401 */       e.printStackTrace();
/*     */     } 
/* 403 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   public String definitionImport(File file) {
/* 408 */     String filePath = "/tmp/export";
/* 409 */     FileUtil.deleteFiles(filePath);
/* 410 */     FileUtil.deleteFiles(filePath + ".zip");
/*     */     try {
/* 412 */       FileUtil.unZipFiles(file, filePath);
/* 413 */     } catch (Exception e) {
/* 414 */       throw new BusinessException(e);
/*     */     } 
/* 416 */     StringBuilder sMsg = new StringBuilder("开始导入\n");
/*     */     
/*     */     try {
/* 419 */       sMsg.append(formImportWithLog(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1)));
/* 420 */     } catch (Exception e) {
/* 421 */       throw new BusinessException("导入出错", e);
/*     */     } 
/* 423 */     sMsg.append("结束导入\n");
/* 424 */     return sMsg.toString();
/*     */   }
/*     */   
/*     */   public void formExport(String formIds, String filePath) throws Exception {
/* 428 */     if (StringUtils.isNotEmpty(formIds)) {
/* 429 */       File file = new File(filePath);
/* 430 */       if (!file.exists()) {
/* 431 */         file.mkdirs();
/*     */       }
/* 433 */       String[] arrFormId = formIds.split(",");
/* 434 */       for (String formId : arrFormId) {
/* 435 */         FormDef form = get(formId);
/* 436 */         if (null != form) {
/* 437 */           FormImportModel importModel = new FormImportModel();
/*     */           
/* 439 */           ISysTreeNode sysTreeNode = this.iSysTreeNodeService.getById(form.getGroupId());
/* 440 */           if (null != sysTreeNode) {
/* 441 */             importModel.setFormTypeTreeKey("bdfl");
/* 442 */             importModel.setFormTypeKey(sysTreeNode.getKey());
/*     */           } 
/* 444 */           FormDef mobileForm = getByKey(form.getKey() + "_mobile");
/* 445 */           if (null != mobileForm) {
/* 446 */             importModel.setMobileFormDef(mobileForm);
/*     */           }
/* 448 */           importModel.setFormDef(form);
/* 449 */           importModel.setFormCustomConf(this.formCustomConfManager.getByFormKey(form.getKey()));
/* 450 */           importModel.setMobileFormCustomConf(this.formCustomConfManager.getByFormKey(form.getKey() + "_mobile"));
/*     */           
/* 452 */           OutputStream outputStream = new FileOutputStream(filePath + "/" + form.getKey() + ".txt");
/* 453 */           outputStream.write(JSON.toJSONString(importModel, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 454 */           outputStream.close();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String formImportWithLog(String filePath) throws Exception {
/* 463 */     StringBuilder sMsg = new StringBuilder("");
/* 464 */     File fileDirectory = new File(filePath);
/* 465 */     if (fileDirectory.exists()) {
/* 466 */       File[] files = fileDirectory.listFiles();
/* 467 */       for (File file : files) {
/* 468 */         try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
/* 469 */           StringBuffer buffer = new StringBuffer();
/* 470 */           String line = "";
/* 471 */           while ((line = in.readLine()) != null) {
/* 472 */             buffer.append(line);
/*     */           }
/* 474 */           FormImportModel importModel = (FormImportModel)JSON.parseObject(buffer.toString(), FormImportModel.class);
/* 475 */           sMsg.append("开始导入表单[" + importModel.getFormDef().getKey() + "]\n");
/* 476 */           List<? extends ISysTreeNode> lstData = this.iSysTreeNodeService.getTreeNodesByType(importModel.getFormTypeTreeKey());
/* 477 */           AtomicReference<String> typeId = new AtomicReference<>("");
/* 478 */           AtomicReference<String> typeName = new AtomicReference<>("");
/* 479 */           if (null != lstData && lstData.size() > 0) {
/* 480 */             lstData.forEach(temp -> {
/*     */                   if (null != temp && temp.getKey().equals(importModel.getFormTypeKey())) {
/*     */                     typeId.set(temp.getId());
/*     */                     typeName.set(temp.getName());
/*     */                     return;
/*     */                   } 
/*     */                 });
/*     */           }
/* 488 */           syncHtml(importModel.getFormDef());
/* 489 */           syncHtml(importModel.getMobileFormDef());
/* 490 */           syncFormDef(importModel.getFormDef(), typeId.get(), typeName.get());
/* 491 */           syncFormDef(importModel.getMobileFormDef(), typeId.get(), typeName.get());
/* 492 */           FormCustomConf formCustomConf = importModel.getFormCustomConf();
/* 493 */           FormCustomConf mobileFormCustomConf = importModel.getMobileFormCustomConf();
/* 494 */           if (null != formCustomConf) {
/* 495 */             FormCustomConf formCustomConfOld = this.formCustomConfManager.getByFormKey(formCustomConf.getFormKey());
/* 496 */             if (null == formCustomConfOld) {
/* 497 */               formCustomConf.setId(null);
/* 498 */               this.formCustomConfManager.create(formCustomConf);
/*     */             } else {
/* 500 */               FormCustomConfHistory formCustomConfHistory = (FormCustomConfHistory)BeanCopierUtils.transformBean(formCustomConfOld, FormCustomConfHistory.class);
/* 501 */               formCustomConfHistory.setFormCustomConfId(formCustomConfOld.getId());
/* 502 */               this.formCustomConfHistoryManager.create(formCustomConfHistory);
/* 503 */               FormCustomConf formCustomConfTemp = formCustomConf;
/* 504 */               formCustomConfTemp.setId(formCustomConfOld.getId());
/* 505 */               this.formCustomConfManager.updateByPrimaryKeySelective(formCustomConfTemp);
/*     */             } 
/*     */           } 
/* 508 */           if (null != mobileFormCustomConf) {
/* 509 */             FormCustomConf mobileFormCustomConfOld = this.formCustomConfManager.getByFormKey(mobileFormCustomConf.getFormKey());
/* 510 */             if (null == mobileFormCustomConfOld) {
/* 511 */               mobileFormCustomConf.setId(null);
/* 512 */               this.formCustomConfManager.create(mobileFormCustomConf);
/*     */             } else {
/* 514 */               FormCustomConfHistory mobileFormCustomConfHistory = (FormCustomConfHistory)BeanCopierUtils.transformBean(mobileFormCustomConfOld, FormCustomConfHistory.class);
/* 515 */               mobileFormCustomConfHistory.setFormCustomConfId(mobileFormCustomConfOld.getId());
/* 516 */               this.formCustomConfHistoryManager.create(mobileFormCustomConfHistory);
/* 517 */               FormCustomConf mobileFormCustomConfTemp = mobileFormCustomConf;
/* 518 */               mobileFormCustomConfTemp.setId(mobileFormCustomConfOld.getId());
/* 519 */               this.formCustomConfManager.updateByPrimaryKeySelective(mobileFormCustomConfTemp);
/*     */             } 
/*     */           } 
/* 522 */           sMsg.append("结束导入表单[" + importModel.getFormDef().getKey() + "]\n");
/* 523 */         } catch (Exception e) {
/* 524 */           this.LOG.error("导入表单失败", e);
/* 525 */           sMsg.append("导入失败:表单[").append(file.getName()).append("]失败\n");
/*     */         } 
/*     */       } 
/*     */     } else {
/* 529 */       sMsg.append("导入失败：文件不存在").append(filePath).append("\n");
/*     */     } 
/* 531 */     return sMsg.toString();
/*     */   }
/*     */   
/*     */   private void syncFormDef(FormDef formDefTop, String typeId, String typeName) {
/* 535 */     FormDef formDef = getByKey(formDefTop.getKey());
/* 536 */     if (null == formDef) {
/* 537 */       formDef = formDefTop;
/* 538 */       formDef.setGroupId(typeId);
/* 539 */       formDef.setGroupName(typeName);
/* 540 */       create((Serializable)formDef);
/*     */     } else {
/* 542 */       FormDefHistory formDefHistory = (FormDefHistory)BeanCopierUtils.transformBean(formDef, FormDefHistory.class);
/* 543 */       formDefHistory.setId(null);
/* 544 */       formDefHistory.setFormDefId(formDef.getId());
/* 545 */       this.formDefHistoryManager.create(formDefHistory);
/* 546 */       FormDef formDefTemp = formDefTop;
/* 547 */       formDefTemp.setId(formDef.getId());
/* 548 */       if (StringUtils.isNotEmpty(typeId)) {
/* 549 */         formDefTemp.setGroupId(typeId);
/* 550 */         formDefTemp.setGroupName(typeName);
/*     */       } 
/* 552 */       formDefTemp.setKey(null);
/* 553 */       Integer version = formDefTemp.getVersion();
/* 554 */       if (null == version) {
/* 555 */         version = Integer.valueOf(0);
/*     */       }
/* 557 */       Integer integer1 = version, integer2 = version = Integer.valueOf(version.intValue() + 1);
/* 558 */       formDefTemp.setVersion(version);
/* 559 */       updateByPrimaryKeySelective(formDefTemp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void syncHtml(FormDef formDef) {
/* 570 */     String html = formDef.getHtml();
/* 571 */     Document doc = Jsoup.parse(html);
/* 572 */     Elements elements = doc.select("[column-id]");
/* 573 */     Map<String, String> mapColumnId = new HashMap<>();
/* 574 */     for (Element ele : elements) {
/* 575 */       String tableKey = ele.attr("table-key");
/* 576 */       String columnKey = ele.attr("column-key");
/* 577 */       if (StringUtils.isNotEmpty(tableKey) && StringUtils.isNotEmpty(columnKey)) {
/* 578 */         String columnId = mapColumnId.get(tableKey + "@" + columnKey);
/* 579 */         if (StringUtils.isNotEmpty(columnId)) {
/* 580 */           ele.attr("column-id", columnId); continue;
/*     */         } 
/* 582 */         List<BusinessColumn> lstColumn = this.businessColumnManager.getByTableKey(tableKey);
/* 583 */         if (null != lstColumn && lstColumn.size() > 0) {
/* 584 */           lstColumn.forEach(temp -> (String)mapColumnId.put(tableKey + "@" + temp.getKey(), temp.getId()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 590 */     formDef.setHtml(doc.outerHtml());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormDef> query(QueryFilter queryFilter) {
/* 595 */     List<FormDef> lst = this.dao.query(queryFilter);
/* 596 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormDef get(String entityId) {
/* 601 */     FormDef temp = (FormDef)this.dao.get(entityId);
/* 602 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void lock(String id) {
/* 608 */     String userId = this.iCurrentContext.getCurrentUserId();
/* 609 */     if (StringUtils.isNotEmpty(userId)) {
/* 610 */       FormDef formDefBase = (FormDef)this.formDefDao.get(id);
/* 611 */       if (StringUtils.isEmpty(formDefBase.getLockBy())) {
/* 612 */         FormDef formDef = new FormDef();
/* 613 */         formDef.setId(id);
/* 614 */         formDef.setLockBy(userId);
/* 615 */         formDef.setLockTime(new Date());
/* 616 */         this.formDefDao.updateByPrimaryKeySelective(formDef);
/*     */       } else {
/* 618 */         if (userId.equals(formDefBase.getLockBy())) {
/* 619 */           throw new BusinessMessage("已被您锁定,请勿重复操作");
/*     */         }
/* 621 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 622 */         IUser user = this.userService.getUserById(formDefBase.getLockBy());
/* 623 */         throw new BusinessMessage("锁定失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(formDefBase.getLockTime()) + "]锁定");
/*     */       } 
/*     */     } else {
/*     */       
/* 627 */       throw new BusinessMessage("当前用户信息丢失");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unlock(String id) {
/* 633 */     String userId = this.iCurrentContext.getCurrentUserId();
/* 634 */     if (StringUtils.isNotEmpty(userId)) {
/* 635 */       FormDef formDefBase = (FormDef)this.formDefDao.get(id);
/* 636 */       if (StringUtils.isEmpty(formDefBase.getLockBy())) {
/* 637 */         throw new BusinessMessage("解锁失败,当前表单并没有上锁");
/*     */       }
/* 639 */       if (userId.equals(formDefBase.getLockBy()) || this.iCurrentContext.isAdmin(this.iCurrentContext.getCurrentUser())) {
/* 640 */         FormDef formDef = new FormDef();
/* 641 */         formDef.setId(id);
/* 642 */         formDef.setLockBy("");
/* 643 */         this.formDefDao.updateByPrimaryKeySelective(formDef);
/*     */       } else {
/* 645 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 646 */         IUser user = this.userService.getUserById(formDefBase.getLockBy());
/* 647 */         throw new BusinessMessage("解锁失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(formDefBase.getLockTime()) + "]锁定");
/*     */       } 
/*     */     } else {
/*     */       
/* 651 */       throw new BusinessMessage("当前用户信息丢失");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormDefManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */