package com.dstz.bpm.activiti.rest.diagram.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessDefinitionDiagramLayoutResource extends BaseProcessDefinitionDiagramLayoutResource {
   @RequestMapping(
      value = {"/process-definition/{processDefinitionId}/diagram-layout"},
      method = {RequestMethod.GET},
      produces = {"application/json"}
   )
   public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
      return this.getDiagramNode((String)null, processDefinitionId);
   }
}
