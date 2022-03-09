package cn.gwssi.ecloudbpm.bus.api.service;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
import java.util.List;

public interface IBusinessColumnService {
  List<? extends IBusinessColumn> getByTableKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/service/IBusinessColumnService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */