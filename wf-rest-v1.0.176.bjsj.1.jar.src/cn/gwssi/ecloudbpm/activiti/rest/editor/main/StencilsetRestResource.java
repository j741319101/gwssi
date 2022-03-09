/*    */ package cn.gwssi.ecloudbpm.activiti.rest.editor.main;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.io.InputStream;
/*    */ import org.activiti.engine.ActivitiException;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMethod;
/*    */ import org.springframework.web.bind.annotation.ResponseBody;
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
/*    */ 
/*    */ @RestController
/*    */ public class StencilsetRestResource
/*    */ {
/*    */   @RequestMapping(value = {"/editor/stencilset"}, method = {RequestMethod.GET})
/*    */   @ResponseBody
/*    */   public JSONObject getStencilset() {
/* 34 */     InputStream stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
/*    */     try {
/* 36 */       JSONObject json = JSONObject.parseObject(IOUtils.toString(stencilsetStream));
/* 37 */       return json;
/* 38 */     } catch (Exception e) {
/* 39 */       throw new ActivitiException("Error while loading stencil set", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/main/StencilsetRestResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */