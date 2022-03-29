package com.dstz.bpm.activiti.rest.editor.model;

import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.FileUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.rest.ControllerTools;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ModelEditorJsonRestResource extends ControllerTools {
   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
   @Autowired
   private RepositoryService repositoryService;
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;
   @Autowired
   private BpmProcessDefService bpmProcessDefService;

   @OperateLog
   @RequestMapping(
      value = {"/model/{modelId}/json"},
      method = {RequestMethod.GET}
   )
   @ResponseBody
   public JSONObject getEditorJson(@PathVariable String modelId) {
      JSONObject json = null;
      BpmContext.cleanTread();
      Model model = this.repositoryService.getModel(modelId);
      if (model != null) {
         try {
            if (StringUtils.isNotEmpty(model.getMetaInfo())) {
               json = JSONObject.parseObject(model.getMetaInfo());
            } else {
               json = new JSONObject();
               json.put("name", model.getName());
            }

            json.put("modelId", model.getId());
            String editorJson = new String(this.repositoryService.getModelEditorSource(model.getId()), "utf-8");
            JSONObject editorModelJson = JSONObject.parseObject(editorJson);
            BpmDefinition def = this.bpmDefinitionManager.getDefByActModelId(modelId);
            JSONObject bpmDefSetting = new JSONObject();
            if (StringUtil.isNotEmpty(def.getActDefId())) {
               DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
               bpmDefSetting = processDef.getJson();
            }

            def.setDefSetting((String)null);
            bpmDefSetting.put("bpmDefinition", def);
            json.put("bpmDefSetting", bpmDefSetting);
            if (!editorModelJson.containsKey("properties")) {
               JSONObject initJson = new JSONObject();
               initJson.put("process_id", model.getKey());
               initJson.put("name", model.getName());
               editorModelJson.put("properties", initJson);
            }

            json.put("model", editorModelJson);
         } catch (Exception var9) {
            LOGGER.error("Error creating model JSON", var9);
            throw new ActivitiException("Error creating model JSON", var9);
         }
      }

      return json;
   }

   @OperateLog
   @RequestMapping(
      value = {"/definition/export"},
      method = {RequestMethod.GET, RequestMethod.POST}
   )
   @ResponseBody
   public ResponseEntity<byte[]> definitionExport(String defIds) throws Exception {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentDispositionFormData("attachment", "filename=bpm.zip");
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      return new ResponseEntity(IOUtils.toByteArray(this.bpmDefinitionManager.definitionExport(defIds)), headers, HttpStatus.OK);
   }

   @OperateLog
   @RequestMapping(
      value = {"/definition/import"},
      method = {RequestMethod.GET, RequestMethod.POST}
   )
   @ResponseBody
   public ResultMsg definitionImport(MultipartFile file, @RequestParam(name = "notPublish",required = false) boolean notPublish) throws Exception {
      String sMsg = this.bpmDefinitionManager.definitionImport(FileUtil.multipartFileToFile(file), notPublish);
      ((ICache)AppUtil.getBean(ICache.class)).clearAll();
      this.bpmDefinitionManager.clearBpmnModelCache((String)null);
      return ResultMsg.SUCCESS("导入日志：\n" + sMsg);
   }

   @RequestMapping(
      value = {"/model/checkImport"},
      method = {RequestMethod.GET, RequestMethod.POST}
   )
   @ResponseBody
   public ResultMsg<String> bpmCheckImport(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {
      return ResultMsg.SUCCESS(this.bpmDefinitionManager.checkImport(FileUtil.multipartFileToFile(file)));
   }
}
