package cn.gwssi.ecloudbpm.bus.api.model;

import java.util.List;

public interface IBusTableRel {
  List<? extends IBusTableRel> getChildren();
  
  String getTableKey();
  
  String getTableComment();
  
  String getType();
  
  List<? extends IBusTableRelFk> getFks();
  
  List<? extends IBusTableRel> list();
  
  IBusinessTable getTable();
  
  IBusTableRel find(String paramString);
  
  IBusTableRel getParent();
  
  IBusinessObject getBusObj();
  
  List<IBusTableRel> getChildren(String paramString);
  
  List<? extends IBusDataFlowRel> getDataFlowLinks();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/model/IBusTableRel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */