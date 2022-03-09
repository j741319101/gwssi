package cn.gwssi.ecloudbpm.wf.api.engine.plugin.def;

import cn.gwssi.ecloudbpm.wf.api.constant.ExtractType;
import cn.gwssi.ecloudbpm.wf.api.engine.constant.LogicType;

public interface BpmUserCalcPluginDef extends BpmPluginDef {
  ExtractType getExtract();
  
  void setExtract(ExtractType paramExtractType);
  
  LogicType getLogicCal();
  
  void setLogicCal(LogicType paramLogicType);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/def/BpmUserCalcPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */