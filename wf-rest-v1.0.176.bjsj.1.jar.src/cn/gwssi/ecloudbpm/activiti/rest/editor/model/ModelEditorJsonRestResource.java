/*     */ package cn.gwssi.ecloudbpm.activiti.rest.editor.model;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.rest.ControllerTools;
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
/*     */ @RestController
/*     */ public class ModelEditorJsonRestResource
/*     */   extends ControllerTools
/*     */ {
/*  52 */   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
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
/*     */   @OperateLog
/*     */   @RequestMapping(value = {"/model/{modelId}/json"}, method = {RequestMethod.GET})
/*     */   @ResponseBody
/*     */   public JSONObject getEditorJson(@PathVariable String modelId) {
/*  67 */     JSONObject json = null;
/*  68 */     BpmContext.cleanTread();
/*     */     
/*  70 */     Model model = this.repositoryService.getModel(modelId);
/*  71 */     if (model != null) {
/*     */       try {
/*  73 */         if (StringUtils.isNotEmpty(model.getMetaInfo())) {
/*  74 */           json = JSONObject.parseObject(model.getMetaInfo());
/*     */         } else {
/*  76 */           json = new JSONObject();
/*  77 */           json.put("name", model.getName());
/*     */         } 
/*  79 */         json.put("modelId", model.getId());
/*  80 */         String editorJson = new String(this.repositoryService.getModelEditorSource(model.getId()), "utf-8");
/*     */         
/*  82 */         JSONObject editorModelJson = JSONObject.parseObject(editorJson);
/*  83 */         BpmDefinition def = this.bpmDefinitionManager.getDefByActModelId(modelId);
/*     */         
/*  85 */         JSONObject bpmDefSetting = new JSONObject();
/*  86 */         if (StringUtil.isNotEmpty(def.getActDefId())) {
/*  87 */           DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
/*  88 */           bpmDefSetting = processDef.getJson();
/*     */         } 
/*     */         
/*  91 */         def.setDefSetting(null);
/*  92 */         bpmDefSetting.put("bpmDefinition", def);
/*  93 */         json.put("bpmDefSetting", bpmDefSetting);
/*     */         
/*  95 */         if (!editorModelJson.containsKey("properties")) {
/*  96 */           JSONObject initJson = new JSONObject();
/*  97 */           initJson.put("process_id", model.getKey());
/*  98 */           initJson.put("name", model.getName());
/*  99 */           editorModelJson.put("properties", initJson);
/*     */         } 
/*     */         
/* 102 */         json.put("model", editorModelJson);
/*     */       }
/* 104 */       catch (Exception e) {
/* 105 */         LOGGER.error("Error creating model JSON", e);
/* 106 */         throw new ActivitiException("Error creating model JSON", e);
/*     */       } 
/*     */     }
/* 109 */     return json;
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
/*     */   @OperateLog
/*     */   @RequestMapping(value = {"/definition/export"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResponseEntity<byte[]> definitionExport(String defIds) throws Exception {
/* 135 */     HttpHeaders headers = new HttpHeaders();
/* 136 */     headers.setContentDispositionFormData("attachment", "filename=bpm.zip");
/* 137 */     headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 138 */     return new ResponseEntity(IOUtils.toByteArray(this.bpmDefinitionManager.definitionExport(defIds)), (MultiValueMap)headers, HttpStatus.OK);
/*     */   }
/*     */   
/*     */   @OperateLog
/*     */   @RequestMapping(value = {"/definition/import"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResultMsg definitionImport(MultipartFile file, @RequestParam(name = "notPublish", required = false) boolean notPublish) throws Exception {
/* 145 */     String sMsg = this.bpmDefinitionManager.definitionImport(FileUtil.multipartFileToFile(file), notPublish);
/*     */     
/* 147 */     ((ICache)AppUtil.getBean(ICache.class)).clearAll();
/* 148 */     this.bpmDefinitionManager.clearBpmnModelCache(null);
/* 149 */     return ResultMsg.SUCCESS("导入日志：\n" + sMsg);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/model/checkImport"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResultMsg<String> bpmCheckImport(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {
/* 155 */     return ResultMsg.SUCCESS(this.bpmDefinitionManager.checkImport(FileUtil.multipartFileToFile(file)));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/model/ModelEditorJsonRestResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */