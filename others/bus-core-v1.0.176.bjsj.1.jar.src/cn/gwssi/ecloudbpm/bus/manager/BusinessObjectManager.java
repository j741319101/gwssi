package cn.gwssi.ecloudbpm.bus.manager;

import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
import com.dstz.base.manager.Manager;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

public interface BusinessObjectManager extends Manager<String, BusinessObject> {
  BusinessObject getByKey(String paramString);
  
  List<JSONObject> boTreeData(String paramString);
  
  BusinessObject getFilledByKey(String paramString);
  
  void updateOverallArrangementByCode(String paramString1, String paramString2);
  
  String getOverallArrangementByCode(String paramString);
  
  void afterSave(BusinessObject paramBusinessObject);
  
  List<BusinessObject> listJsonByKey(String paramString1, String paramString2);
  
  void updateDiagramJson(String paramString1, String paramString2);
  
  Integer countByTypeId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/BusinessObjectManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */