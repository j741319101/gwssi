package cn.gwssi.ecloudbpm.wf.core.dao;

import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskOpinionVO;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskOpinionDao extends BaseDao<String, BpmTaskOpinion> {
  BpmTaskOpinion getByTaskId(String paramString);
  
  List<BpmTaskOpinion> getByInstAndNode(@Param("instId") String paramString1, @Param("nodeId") String paramString2, @Param("signId") String paramString3, @Param("actExecutionId") String paramString4);
  
  List<BpmTaskOpinionVO> getByInstsOpinion(QueryFilter paramQueryFilter);
  
  List<BpmTaskOpinion> getByInstAndNodeVersion(@Param("instId") String paramString1, @Param("nodeId") String paramString2);
  
  void removeByInstId(@Param("instId") String paramString);
  
  void removeByTaskId(@Param("taskId") String paramString);
  
  List<BpmTaskOpinion> selectByTaskIds(@Param("taskIds") Collection<String> paramCollection);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmTaskOpinionDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */