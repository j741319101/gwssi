package com.dstz.bpm.engine.data.handle;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
import cn.gwssi.ecloudbpm.bus.api.model.IBusinessPermission;
import com.dstz.bpm.core.model.BpmInstance;
import java.util.Map;

public interface IBpmBusDataHandle {
  Map<String, IBusinessData> getInstanceData(IBusinessPermission paramIBusinessPermission, BpmInstance paramBpmInstance);
  
  Map<String, IBusinessData> getInstanceBusData(String paramString, IBusinessPermission paramIBusinessPermission);
  
  Map<String, IBusinessData> getInitData(IBusinessPermission paramIBusinessPermission, String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/data/handle/IBpmBusDataHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */