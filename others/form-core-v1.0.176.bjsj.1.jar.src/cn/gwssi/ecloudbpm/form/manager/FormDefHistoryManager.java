package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormDefHistory;
import com.dstz.base.manager.Manager;

public interface FormDefHistoryManager extends Manager<String, FormDefHistory> {
  FormDefHistory getWithoutHtml(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormDefHistoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */