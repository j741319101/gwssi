package com.dstz.bpm.activiti.rest.editor.main;

import com.alibaba.fastjson.JSONObject;
import java.io.InputStream;
import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StencilsetRestResource {
   @RequestMapping(
      value = {"/editor/stencilset"},
      method = {RequestMethod.GET}
   )
   @ResponseBody
   public JSONObject getStencilset() {
      InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");

      try {
         JSONObject json = JSONObject.parseObject(IOUtils.toString(stencilsetStream));
         return json;
      } catch (Exception var3) {
         throw new ActivitiException("Error while loading stencil set", var3);
      }
   }
}
