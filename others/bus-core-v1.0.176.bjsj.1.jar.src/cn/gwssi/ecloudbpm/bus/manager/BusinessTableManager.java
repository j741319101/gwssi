package cn.gwssi.ecloudbpm.bus.manager;

import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.db.tableoper.TableOperator;
import cn.gwssi.ecloudframework.base.manager.Manager;
import java.util.List;

public interface BusinessTableManager extends Manager<String, BusinessTable> {
  void save(BusinessTable paramBusinessTable);
  
  BusinessTable getByKey(String paramString);
  
  BusinessTable getFilledByKey(String paramString);
  
  TableOperator newTableOperator(BusinessTable paramBusinessTable);
  
  TableOperator newTableOperatorCheckExist(BusinessTable paramBusinessTable);
  
  List<BusinessTable> getEntities(String paramString);
  
  List<BusinessTable> queryByMetadata(QueryFilter paramQueryFilter);
  
  Integer countByTypeId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/BusinessTableManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */