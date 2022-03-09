/*    */ package com.dstz.activiti.rest.editor.model;
/*    */ 
/*    */ import com.dstz.base.api.aop.annotion.CatchErr;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.core.util.ThreadMapUtil;
/*    */ import com.dstz.base.rest.util.RequestUtil;
/*    */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.activiti.bpmn.exceptions.XMLException;
/*    */ import org.activiti.editor.constants.ModelDataJsonConstants;
/*    */ import org.activiti.engine.RepositoryService;
/*    */ import org.activiti.engine.repository.Model;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMethod;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ public class ModelSaveRestResource
/*    */   implements ModelDataJsonConstants
/*    */ {
/* 51 */   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);
/*    */   
/*    */   @Autowired
/*    */   private BpmDefinitionManager bpmDefinitionManager;
/* 55 */   private ObjectMapper objectMapper = new ObjectMapper();
/*    */   
/*    */   @Resource
/*    */   RepositoryService repositoryService;
/*    */ 
/*    */   
/*    */   @RequestMapping(value = {"/model/{modelId}/save"}, method = {RequestMethod.PUT, RequestMethod.POST})
/*    */   @ResponseStatus(HttpStatus.OK)
/*    */   @CatchErr
/*    */   public ResultMsg<Map> saveModel(HttpServletResponse response, HttpServletRequest request, @PathVariable String modelId, @RequestBody(required = false) MultiValueMap<String, String> values) throws Exception {
/* 65 */     Map<String, String> params = null;
/*    */     
/* 67 */     if (values != null) {
/* 68 */       params = new HashMap<>();
/* 69 */       for (String key : values.keySet()) {
/* 70 */         params.put(key, values.getFirst(key));
/*    */       }
/*    */     } else {
/* 73 */       params = RequestUtil.getParameterValueMap(request, false);
/*    */     } 
/*    */ 
/*    */     
/* 77 */     Model model = this.repositoryService.getModel(modelId);
/* 78 */     ObjectNode modelJson = (ObjectNode)this.objectMapper.readTree(model.getMetaInfo());
/* 79 */     modelJson.put("name", params.get("name"));
/* 80 */     modelJson.put("description", params.get("description"));
/*    */     
/*    */     try {
/* 83 */       this.bpmDefinitionManager.updateBpmnModel(model, params);
/* 84 */     } catch (XMLException e) {
/* 85 */       throw new BusinessException("流程图解析失败！不合法的流程图：" + e.getMessage(), e);
/*    */     } 
/* 87 */     ResultMsg<Map> r = new ResultMsg(ThreadMapUtil.getMap());
/* 88 */     ThreadMapUtil.remove();
/* 89 */     return r;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/model/ModelSaveRestResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */