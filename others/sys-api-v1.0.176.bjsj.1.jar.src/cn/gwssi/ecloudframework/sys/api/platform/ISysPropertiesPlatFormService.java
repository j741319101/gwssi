package cn.gwssi.ecloudframework.sys.api.platform;

import java.util.Map;

public interface ISysPropertiesPlatFormService {
  String getByAlias(String paramString);
  
  Map<String, Map<String, String>> reloadProperty();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/platform/ISysPropertiesPlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */