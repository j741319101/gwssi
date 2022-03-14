package com.dstz.bpm.api.service;

import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.def.IBpmDefinition;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import java.util.List;

public interface BpmProcessDefService {
  IBpmDefinition getDefinitionById(String paramString);
  
  IBpmDefinition getDefinitionByKey(String paramString);
  
  IBpmDefinition getDefinitionByActDefId(String paramString);
  
  BpmNodeDef getBpmNodeDef(String paramString1, String paramString2);
  
  BpmProcessDef getBpmProcessDef(String paramString);
  
  List<BpmNodeDef> getNodeDefs(String paramString);
  
  List<BpmNodeDef> getNodesByType(String paramString, NodeType paramNodeType);
  
  List<BpmNodeDef> getAllNodeDef(String paramString);
  
  void clean(String paramString);
  
  BpmNodeDef getStartEvent(String paramString);
  
  List<BpmNodeDef> getEndEvents(String paramString);
  
  List<BpmNodeDef> getStartNodes(String paramString);
  
  boolean isStartNode(String paramString1, String paramString2);
  
  boolean validNodeDefType(String paramString1, String paramString2, NodeType paramNodeType);
  
  boolean isContainCallActivity(String paramString);
  
  List<BpmNodeDef> getSignUserNode(String paramString);
  
  BpmProcessDef initBpmProcessDef(IBpmDefinition paramIBpmDefinition);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/service/BpmProcessDefService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */