package cn.gwssi.ecloudbpm.wf.api.model.nodedef;

import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
import cn.gwssi.ecloudbpm.wf.api.model.def.NodeProperties;
import cn.gwssi.ecloudbpm.wf.api.model.form.BpmForm;
import java.io.Serializable;
import java.util.List;

public interface BpmNodeDef extends Serializable, BpmDef {
  String getNodeId();
  
  void setNodeId(String paramString);
  
  String getName();
  
  void setName(String paramString);
  
  NodeType getType();
  
  void setType(NodeType paramNodeType);
  
  List<BpmNodeDef> getIncomeNodes();
  
  List<BpmNodeDef> getIncomeTaskNodes();
  
  List<BpmNodeDef> getOutcomeNodes();
  
  List<BpmNodeDef> getOutcomeTaskNodes();
  
  List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean paramBoolean);
  
  List<BpmPluginContext> getBpmPluginContexts();
  
  BpmProcessDef getBpmProcessDef();
  
  String getAttribute(String paramString);
  
  void addIncomeNode(BpmNodeDef paramBpmNodeDef);
  
  void addOutcomeNode(BpmNodeDef paramBpmNodeDef);
  
  BpmNodeDef getParentBpmNodeDef();
  
  String getRealPath();
  
  BpmProcessDef getRootProcessDef();
  
  BpmForm getForm();
  
  BpmForm getMobileForm();
  
  <T> T getPluginContext(Class<T> paramClass);
  
  NodeProperties getNodeProperties();
  
  List<Button> getButtons();
  
  List<BpmNodeDef> getOutcomeTaskNodesIncludeCall();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/BpmNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */