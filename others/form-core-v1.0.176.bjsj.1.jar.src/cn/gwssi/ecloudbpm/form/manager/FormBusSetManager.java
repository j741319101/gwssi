package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormBusSet;
import cn.gwssi.ecloudframework.base.manager.Manager;

public interface FormBusSetManager extends Manager<String, FormBusSet> {
  FormBusSet getByFormKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormBusSetManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */