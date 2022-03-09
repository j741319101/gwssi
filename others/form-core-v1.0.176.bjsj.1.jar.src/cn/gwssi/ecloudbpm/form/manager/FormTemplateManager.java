package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormTemplate;
import cn.gwssi.ecloudframework.base.manager.Manager;
import com.alibaba.fastjson.JSONArray;

public interface FormTemplateManager extends Manager<String, FormTemplate> {
  FormTemplate getByKey(String paramString);
  
  void initAllTemplate();
  
  void init();
  
  boolean isExist(String paramString);
  
  void backUpTemplate(String paramString);
  
  JSONArray templateData(String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormTemplateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */