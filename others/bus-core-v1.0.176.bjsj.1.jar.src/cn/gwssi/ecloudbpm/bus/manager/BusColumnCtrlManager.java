package cn.gwssi.ecloudbpm.bus.manager;

import cn.gwssi.ecloudbpm.bus.model.BusColumnCtrl;
import cn.gwssi.ecloudframework.base.manager.Manager;

public interface BusColumnCtrlManager extends Manager<String, BusColumnCtrl> {
  void removeByTableId(String paramString);
  
  BusColumnCtrl getByColumnId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/BusColumnCtrlManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */