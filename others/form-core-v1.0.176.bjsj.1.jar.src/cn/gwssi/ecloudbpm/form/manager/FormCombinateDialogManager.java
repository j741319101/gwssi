package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormCombinateDialog;
import com.dstz.base.manager.Manager;

public interface FormCombinateDialogManager extends Manager<String, FormCombinateDialog> {
  FormCombinateDialog getByAlias(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormCombinateDialogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */