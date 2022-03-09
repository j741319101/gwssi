package cn.gwssi.ecloudbpm.bus.api.service;

import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowConf;
import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowSql;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Set;

public interface IDataFlowService {
  List<IDataFlowSql> analysis(JSONObject paramJSONObject, Set<String> paramSet);
  
  List<IDataFlowSql> analysis(JSONObject paramJSONObject, IDataFlowConf paramIDataFlowConf);
  
  void execute(List<IDataFlowSql> paramList);
  
  Object saveData(JSONObject paramJSONObject, IDataFlowConf paramIDataFlowConf);
  
  void removeData(IBusinessObject paramIBusinessObject, String paramString, IDataFlowConf paramIDataFlowConf);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/service/IDataFlowService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */