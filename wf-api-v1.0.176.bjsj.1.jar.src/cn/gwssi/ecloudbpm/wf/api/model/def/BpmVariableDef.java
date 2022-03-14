package com.dstz.bpm.api.model.def;

import java.io.Serializable;

public interface BpmVariableDef extends Serializable {
  public static final String VAR_TYPE_STRING = "string";
  
  public static final String VAR_TYPE_INT = "int";
  
  public static final String VAR_TYPE_FLOAT = "float";
  
  public static final String VAR_TYPE_DOUBLE = "double";
  
  public static final String VAR_TYPE_LONG = "long";
  
  public static final String VAR_TYPE_DATE = "date";
  
  public static final String VAR_TYPE_JSON = "json";
  
  public static final String VAR_TYPE_XML = "xml";
  
  public static final String VAR_TYPE_BYTES = "bytes";
  
  String getName();
  
  void setName(String paramString);
  
  String getNodeId();
  
  void setNodeId(String paramString);
  
  String getKey();
  
  void setKey(String paramString);
  
  String getDataType();
  
  void setDataType(String paramString);
  
  Object getDefaultVal();
  
  void setDefaultVal(Object paramObject);
  
  boolean isRequired();
  
  void setRequired(boolean paramBoolean);
  
  boolean getIsRequired();
  
  void setIsRequired(boolean paramBoolean);
  
  String getDescription();
  
  void setDescription(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/BpmVariableDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */