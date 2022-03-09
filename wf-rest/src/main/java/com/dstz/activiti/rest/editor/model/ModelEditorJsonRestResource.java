/*     */ package com.dstz.activiti.rest.editor.model;
/*     */ 
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.activiti.engine.ActivitiException;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.repository.Model;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
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
/*     */ @RestController
/*     */ public class ModelEditorJsonRestResource
/*     */ {
/*  58 */   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
/*     */   
/*     */   @Autowired
/*     */   private RepositoryService repositoryService;
/*     */   
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   
/*     */   @RequestMapping(value = {"/model/{modelId}/json"}, method = {RequestMethod.GET})
/*     */   @ResponseBody
/*     */   public JSONObject getEditorJson(@PathVariable String modelId) {
/*  72 */     JSONObject json = null;
/*  73 */     BpmContext.cleanTread();
/*     */     
/*  75 */     Model model = this.repositoryService.getModel(modelId);
/*  76 */     if (model != null) {
/*     */       try {
/*  78 */         if (StringUtils.isNotEmpty(model.getMetaInfo())) {
/*  79 */           json = JSONObject.parseObject(model.getMetaInfo());
/*     */         } else {
/*  81 */           json = new JSONObject();
/*  82 */           json.put("name", model.getName());
/*     */         } 
/*  84 */         json.put("modelId", model.getId());
/*  85 */         String editorJson = new String(this.repositoryService.getModelEditorSource(model.getId()), "utf-8");
/*     */         
/*  87 */         JSONObject editorModelJson = JSONObject.parseObject(editorJson);
/*  88 */         BpmDefinition def = this.bpmDefinitionManager.getDefByActModelId(modelId);
/*     */         
/*  90 */         JSONObject bpmDefSetting = new JSONObject();
/*  91 */         if (StringUtil.isNotEmpty(def.getActDefId())) {
/*  92 */           DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
/*  93 */           bpmDefSetting = processDef.getJson();
/*     */         } 
/*     */         
/*  96 */         def.setDefSetting(null);
/*  97 */         bpmDefSetting.put("bpmDefinition", def);
/*  98 */         json.put("bpmDefSetting", bpmDefSetting);
/*     */         
/* 100 */         if (!editorModelJson.containsKey("properties")) {
/* 101 */           JSONObject initJson = new JSONObject();
/* 102 */           initJson.put("process_id", model.getKey());
/* 103 */           initJson.put("name", model.getName());
/* 104 */           editorModelJson.put("properties", initJson);
/*     */         } 
/*     */         
/* 107 */         json.put("model", editorModelJson);
/*     */       }
/* 109 */       catch (Exception e) {
/* 110 */         LOGGER.error("Error creating model JSON", e);
/* 111 */         throw new ActivitiException("Error creating model JSON", e);
/*     */       } 
/*     */     }
/* 114 */     return json;
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/model/export"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResponseEntity<byte[]> export(String defIds) throws Exception {
/* 120 */     HttpHeaders headers = new HttpHeaders();
/* 121 */     headers.setContentDispositionFormData("attachment", "filename=bpm.zip");
/* 122 */     headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 123 */     return new ResponseEntity(IOUtils.toByteArray(this.bpmDefinitionManager.bpmExport(defIds)), (MultiValueMap)headers, HttpStatus.OK);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/model/import"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResultMsg bpmImport(MultipartFile file) throws Exception {
/* 129 */     this.bpmDefinitionManager.bpmImport(FileUtil.multipartFileToFile(file));
/*     */     
/* 131 */     ((ICache)AppUtil.getBean(ICache.class)).clearAll();
/* 132 */     this.bpmDefinitionManager.clearBpmnModelCache(null);
/* 133 */     return ResultMsg.SUCCESS("导入成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/model/checkImport"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResultMsg<String> bpmCheckImport(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {
/* 139 */     return ResultMsg.SUCCESS(this.bpmDefinitionManager.checkImport(FileUtil.multipartFileToFile(file)));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/model/ModelEditorJsonRestResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */