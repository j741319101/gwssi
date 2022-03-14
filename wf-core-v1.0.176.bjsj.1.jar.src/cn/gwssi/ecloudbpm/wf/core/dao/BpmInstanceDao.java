package com.dstz.bpm.core.dao;

import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
import com.dstz.bpm.core.vo.BpmInstanceVO;
import com.dstz.bpm.core.vo.BpmTaskApproveVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmInstanceDao extends BaseDao<String, BpmInstance> {
  List<BpmInstance> getApplyList(QueryFilter paramQueryFilter);
  
  List<BpmTaskApprove> getApproveHistoryList(QueryFilter paramQueryFilter);
  
  List<BpmTaskApproveVO> getApproveInstHistoryList(QueryFilter paramQueryFilter);
  
  List<BpmInstance> getByPId(@Param("parentInstId") String paramString);
  
  BpmInstance getByActInstId(@Param("actInstId") String paramString);
  
  List<Map> getInstNum();
  
  int updateStatus(@Param("ids") List paramList, @Param("status") String paramString1, @Param("userId") String paramString2);
  
  Map getInstIdByBusId(@Param("busId") String paramString);
  
  List<BpmTypeTreeCountVO> getApproveHistoryListTypeCount(QueryFilter paramQueryFilter);
  
  List<BpmTypeTreeCountVO> getApplyListTypeCount(QueryFilter paramQueryFilter);
  
  List<BpmInstanceVO> listInstTaskJson(QueryFilter paramQueryFilter);
  
  Map getInstanceStatusStatis(@Param("instId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmInstanceDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */