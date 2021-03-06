/*    */ package cn.gwssi.ecloudbpm.activiti.rest.diagram.services;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*    */ @RestController
/*    */ public class ProcessInstanceDiagramLayoutResource
/*    */   extends BaseProcessDefinitionDiagramLayoutResource
/*    */ {
/*    */   @RequestMapping(value = {"/process-instance/{processInstanceId}/diagram-layout"}, method = {RequestMethod.GET}, produces = {"application/json"})
/*    */   public ObjectNode getDiagram(@PathVariable String processInstanceId) {
/* 27 */     return getDiagramNode(processInstanceId, null);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/activiti/rest/diagram/services/ProcessInstanceDiagramLayoutResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */