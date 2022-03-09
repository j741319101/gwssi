package cn.gwssi.ecloudbpm.wf.engine.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

interface BpmDefParser<D extends cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef> {
  Object parseDef(D paramD, String paramString);
  
  void parse(D paramD, JSONObject paramJSONObject);
  
  String getKey();
  
  Class getClazz();
  
  boolean isArray();
  
  String validate(Object paramObject);
  
  void setDefParam(D paramD, Object paramObject);
  
  void JSONAmend(D paramD, Object paramObject, JSON paramJSON);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/BpmDefParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */