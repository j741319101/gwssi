/*    */ package cn.gwssi.ecloudbpm.activiti.rest.editor.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*    */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*    */ import cn.gwssi.ecloudframework.base.api.aop.annotion.OperateLog;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*    */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*    */ import cn.gwssi.ecloudframework.base.rest.util.RequestUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
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
/*    */ @RestController
/*    */ public class ModelSaveRestResource
/*    */   implements ModelDataJsonConstants
/*    */ {
/* 49 */   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);
/*    */   
/*    */   @Autowired
/*    */   private BpmDefinitionManager bpmDefinitionManager;
/* 53 */   private ObjectMapper objectMapper = new ObjectMapper();
/*    */   
/*    */   @Resource
/*    */   RepositoryService repositoryService;
/*    */   
/*    */   @Resource
/*    */   ICurrentContext iCurrentContext;
/*    */   
/*    */   @Resource
/*    */   UserService userService;
/*    */   
/*    */   @OperateLog
/*    */   @RequestMapping(value = {"/model/{modelId}/save"}, method = {RequestMethod.PUT, RequestMethod.POST})
/*    */   @ResponseStatus(HttpStatus.OK)
/*    */   @CatchErr
/*    */   public ResultMsg<Map> saveModel(HttpServletResponse response, HttpServletRequest request, @PathVariable String modelId, @RequestBody(required = false) MultiValueMap<String, String> values) throws Exception {
/* 69 */     Map<String, String> params = null;
/*    */     
/* 71 */     if (values != null) {
/* 72 */       params = new HashMap<>();
/* 73 */       for (String key : values.keySet()) {
/* 74 */         params.put(key, values.getFirst(key));
/*    */       }
/*    */     } else {
/* 77 */       params = RequestUtil.getParameterValueMap(request, false);
/*    */     } 
/*    */ 
/*    */     
/* 81 */     Model model = this.repositoryService.getModel(modelId);
/* 82 */     ObjectNode modelJson = (ObjectNode)this.objectMapper.readTree(model.getMetaInfo());
/* 83 */     modelJson.put("name", params.get("name"));
/* 84 */     modelJson.put("description", params.get("description"));
/*    */     
/*    */     try {
/* 87 */       this.bpmDefinitionManager.updateBpmnModel(model, params);
/* 88 */     } catch (XMLException e) {
/* 89 */       throw new BusinessException("流程图解析失败！不合法的流程图：" + e.getMessage(), e);
/*    */     } 
/* 91 */     ResultMsg<Map> r = new ResultMsg(ThreadMapUtil.getMap());
/* 92 */     ThreadMapUtil.remove();
/* 93 */     return r;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/model/ModelSaveRestResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */