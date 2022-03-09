package cn.gwssi.ecloudframework.sys.api.platform;

import cn.gwssi.ecloudframework.sys.api.model.IDataDict;
import java.util.List;
import java.util.Map;

public interface ISysDataDictPlatFormService {
  List<? extends IDataDict> getByDictKey(String paramString, Boolean paramBoolean);
  
  Map<String, List<? extends IDataDict>> getByDictKeyList(Map<String, Boolean> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/platform/ISysDataDictPlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */