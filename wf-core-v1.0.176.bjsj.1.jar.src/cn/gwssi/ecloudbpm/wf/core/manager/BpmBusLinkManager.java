package cn.gwssi.ecloudbpm.wf.core.manager;

import cn.gwssi.ecloudbpm.wf.core.model.BpmBusLink;
import cn.gwssi.ecloudframework.base.manager.Manager;
import java.util.List;

public interface BpmBusLinkManager extends Manager<String, BpmBusLink> {
  List<BpmBusLink> getByInstanceId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmBusLinkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */