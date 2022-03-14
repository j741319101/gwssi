package cn.gwssi.ecloudbpm.bus.dao;

import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BusinessColumnDao extends BaseDao<String, BusinessColumn> {
  void removeByTableId(String paramString);
  
  List<BusinessColumn> getByTableId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/dao/BusinessColumnDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */