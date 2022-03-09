package cn.gwssi.ecloudbpm.wf.api.model.def;

import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
import java.util.List;

public interface BpmProcessDef extends BpmDef {
  String getDefKey();
  
  String getName();
  
  String getProcessDefinitionId();
  
  List<BpmNodeDef> getBpmnNodeDefs();
  
  BpmProcessDef getParentProcessDef();
  
  BpmNodeDef getStartEvent();
  
  List<BpmNodeDef> getStartNodes();
  
  List<BpmNodeDef> getEndEvents();
  
  BpmNodeDef getBpmnNodeDef(String paramString);
  
  BpmPluginContext getBpmPluginContext(Class<?> paramClass);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/BpmProcessDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */