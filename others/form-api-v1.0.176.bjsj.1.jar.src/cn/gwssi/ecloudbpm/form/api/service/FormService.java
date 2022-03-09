package cn.gwssi.ecloudbpm.form.api.service;

import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
import java.util.Set;

public interface FormService {
  IFormDef getByFormKey(String paramString);
  
  IFormDef getByFormId(String paramString);
  
  String getFormExportXml(Set<String> paramSet);
  
  void importForm(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/service/FormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */