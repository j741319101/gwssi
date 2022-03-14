package cn.gwssi.ecloudbpm.bus.dao;

import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BusinessTableDao extends BaseDao<String, BusinessTable> {
  List<BusinessTable> getEntities(@Param("groupId") String paramString);
  
  List<BusinessTable> queryByMetadata(QueryFilter paramQueryFilter);
  
  Integer countByTypeId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/dao/BusinessTableDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */