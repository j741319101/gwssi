package cn.gwssi.ecloudbpm.bus.manager;

import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
import com.dstz.base.manager.Manager;
import java.util.Set;

public interface BusinessPermissionManager extends Manager<String, BusinessPermission> {
  BusinessPermission getByObjTypeAndObjVal(String paramString1, String paramString2, String paramString3);
  
  BusinessPermission getByObjTypeAndObjVal(String paramString1, String paramString2, String paramString3, String paramString4);
  
  int removeByBpmDefKey(String paramString1, String paramString2, String paramString3);
  
  int removeNotInBpmNode(String paramString1, String paramString2, Set<String> paramSet);
  
  int removeByDefId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/BusinessPermissionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */