package cn.gwssi.ecloudbpm.bus.dao;

import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
import com.dstz.base.dao.BaseDao;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BusinessPermissionDao extends BaseDao<String, BusinessPermission> {
  BusinessPermission getByObjTypeAndObjVal(@Param("defId") String paramString1, @Param("objType") String paramString2, @Param("objVal") String paramString3);
  
  int removeByBpmDefKey(@Param("defId") String paramString1, @Param("objType") String paramString2, @Param("boKey") String paramString3);
  
  int removeNotInBpmNode(@Param("defId") String paramString1, @Param("boKey") String paramString2, @Param("nodeIds") Set<String> paramSet);
  
  int removeByDefId(@Param("defId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/dao/BusinessPermissionDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */