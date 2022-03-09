package cn.gwssi.ecloudbpm.wf.core.dao;

import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskStackDao extends BaseDao<String, BpmTaskStack> {
  BpmTaskStack getByTaskId(String paramString);
  
  void removeByInstanceId(String paramString);
  
  List<BpmTaskStack> getByInstanceId(String paramString);
  
  List<BpmTaskStack> getTaskStackByIteration(QueryFilter paramQueryFilter);
  
  List<BpmTaskStack> getTaskStackByIterationMysql(@Param("taskId") String paramString1, @Param("prior") String paramString2, @Param("whereSql") String paramString3, @Param("orderSql") String paramString4);
  
  List<BpmTaskStack> getTaskStackByIterationHighGo(@Param("taskId") String paramString1, @Param("prior") String paramString2, @Param("whereSql") String paramString3, @Param("orderSql") String paramString4);
  
  void updateStackEndByInstId(@Param("instId") String paramString, @Param("endTime") Date paramDate);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmTaskStackDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */