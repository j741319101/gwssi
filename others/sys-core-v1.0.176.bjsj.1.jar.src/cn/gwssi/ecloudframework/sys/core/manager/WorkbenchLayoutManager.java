package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.WorkbenchLayout;
import java.util.List;

public interface WorkbenchLayoutManager extends Manager<String, WorkbenchLayout> {
  List<WorkbenchLayout> getByUserId(String paramString);
  
  void savePanelLayout(List<WorkbenchLayout> paramList, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/WorkbenchLayoutManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */