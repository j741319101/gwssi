package cn.gwssi.ecloudbpm.bus.manager;

import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BusinessColumnManager extends Manager<String, BusinessColumn> {
  void removeByTableId(String paramString);
  
  List<BusinessColumn> getByTableId(String paramString);
  
  List<BusinessColumn> getByTableKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/BusinessColumnManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */