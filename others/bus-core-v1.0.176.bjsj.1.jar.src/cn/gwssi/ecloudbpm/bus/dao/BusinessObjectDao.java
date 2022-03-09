package cn.gwssi.ecloudbpm.bus.dao;

import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BusinessObjectDao extends BaseDao<String, BusinessObject> {
  String getOverallArrangementByCode(String paramString);
  
  void updateOverallArrangementByCode(@Param("boCode") String paramString1, @Param("overallArrangement") String paramString2);
  
  List<BusinessObject> listJsonByKey(@Param("tableKey") String paramString1, @Param("tableGroupKey") String paramString2);
  
  void updateDiagramJson(@Param("id") String paramString1, @Param("diagramJson") String paramString2);
  
  Integer countByTypeId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/dao/BusinessObjectDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */