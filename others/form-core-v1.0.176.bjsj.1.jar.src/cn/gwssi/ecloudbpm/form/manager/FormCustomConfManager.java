package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormCustomConf;
import cn.gwssi.ecloudframework.base.manager.Manager;

public interface FormCustomConfManager extends Manager<String, FormCustomConf> {
  FormCustomConf getByFormKey(String paramString);
  
  String save(FormCustomConf paramFormCustomConf);
  
  void updateByPrimaryKeySelective(FormCustomConf paramFormCustomConf);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormCustomConfManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */