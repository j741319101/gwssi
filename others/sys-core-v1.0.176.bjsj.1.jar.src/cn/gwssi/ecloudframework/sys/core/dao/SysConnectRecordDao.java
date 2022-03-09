package cn.gwssi.ecloudframework.sys.core.dao;

import cn.gwssi.ecloudframework.base.dao.BaseDao;
import cn.gwssi.ecloudframework.sys.core.model.SysConnectRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysConnectRecordDao extends BaseDao<String, SysConnectRecord> {
  void removeBySourceId(@Param("sourceId") String paramString1, @Param("type") String paramString2);
  
  void bulkCreate(List<SysConnectRecord> paramList);
  
  List<SysConnectRecord> getByTargetId(@Param("targetId") String paramString1, @Param("type") String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/SysConnectRecordDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */