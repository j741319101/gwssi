package cn.gwssi.ecloudbpm.bus.dao;

import cn.gwssi.ecloudbpm.bus.model.BusColumnCtrl;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BusColumnCtrlDao extends BaseDao<String, BusColumnCtrl> {
  void removeByTableId(String paramString);
  
  BusColumnCtrl getByColumnId(String paramString);
  
  List<BusColumnCtrl> selectByTableId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/dao/BusColumnCtrlDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */