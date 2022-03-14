package com.dstz.bpm.api.engine.plugin.context;

import com.alibaba.fastjson.JSON;

public interface PluginParse<T extends com.dstz.bpm.api.engine.plugin.def.BpmPluginDef> {
  T parse(JSON paramJSON);
  
  T parse(String paramString);
  
  JSON getJson();
  
  String getType();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/context/PluginParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */