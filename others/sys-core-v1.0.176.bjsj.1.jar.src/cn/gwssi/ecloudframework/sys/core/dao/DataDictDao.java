package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.core.model.DataDict;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataDictDao extends BaseDao<String, DataDict> {
  List<DataDict> getDictNodeList(@Param("dictKey") String paramString, @Param("hasRoot") Boolean paramBoolean);
  
  Integer isExistDict(@Param("key") String paramString1, @Param("id") String paramString2);
  
  Integer isExistNode(@Param("dictKey") String paramString1, @Param("key") String paramString2, @Param("id") String paramString3);
  
  Integer countDictByTypeId(@Param("typeId") String paramString);
  
  void removeAll();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/DataDictDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */