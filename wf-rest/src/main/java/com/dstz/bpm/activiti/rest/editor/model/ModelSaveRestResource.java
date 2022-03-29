package com.dstz.bpm.activiti.rest.editor.model;

import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.aop.annotion.OperateLog;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelSaveRestResource implements ModelDataJsonConstants {
   protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);
   @Autowired
   private BpmDefinitionManager bpmDefinitionManager;
   private ObjectMapper objectMapper = new ObjectMapper();
   @Resource
   RepositoryService repositoryService;
   @Resource
   ICurrentContext iCurrentContext;
   @Resource
   UserService userService;

   @OperateLog
   @RequestMapping(
      value = {"/model/{modelId}/save"},
      method = {RequestMethod.PUT, RequestMethod.POST}
   )
   @ResponseStatus(HttpStatus.OK)
   @CatchErr
   public ResultMsg<Map> saveModel(HttpServletResponse response, HttpServletRequest request, @PathVariable String modelId, @RequestBody(required = false) MultiValueMap<String, String> values) throws Exception {
      Map<String, String> params = null;
      if (values != null) {
         params = new HashMap();
         Iterator var6 = values.keySet().iterator();

         while(var6.hasNext()) {
            String key = (String)var6.next();
            ((Map)params).put(key, values.getFirst(key));
         }
      } else {
         params = RequestUtil.getParameterValueMap(request, false);
      }

      Model model = this.repositoryService.getModel(modelId);
      ObjectNode modelJson = (ObjectNode)this.objectMapper.readTree(model.getMetaInfo());
      modelJson.put("name", (String)((Map)params).get("name"));
      modelJson.put("description", (String)((Map)params).get("description"));

      try {
         this.bpmDefinitionManager.updateBpmnModel(model, (Map)params);
      } catch (XMLException var9) {
         throw new BusinessException("流程图解析失败！不合法的流程图：" + var9.getMessage(), var9);
      }

      ResultMsg<Map> r = new ResultMsg(ThreadMapUtil.getMap());
      ThreadMapUtil.remove();
      return r;
   }
}
