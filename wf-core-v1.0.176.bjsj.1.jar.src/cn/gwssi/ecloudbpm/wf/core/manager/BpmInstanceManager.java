package com.dstz.bpm.core.manager;

import com.dstz.bpm.api.model.def.IBpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskApprove;
import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
import com.dstz.bpm.core.vo.BpmInstanceVO;
import com.dstz.bpm.core.vo.BpmTaskApproveVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.ibatis.annotations.Param;

public interface BpmInstanceManager extends Manager<String, BpmInstance> {
  boolean isSuspendByInstId(String paramString);
  
  List<BpmInstance> getApplyList(String paramString, QueryFilter paramQueryFilter);
  
  List<BpmTaskApprove> getApproveHistoryList(String paramString, QueryFilter paramQueryFilter);
  
  List<BpmTaskApproveVO> getApproveInstHistoryList(QueryFilter paramQueryFilter);
  
  BpmInstance getTopInstance(BpmInstance paramBpmInstance);
  
  BpmInstance genInstanceByDefinition(IBpmDefinition paramIBpmDefinition);
  
  List<BpmInstance> getByPId(String paramString);
  
  BpmInstance getByActInstId(String paramString);
  
  void delete(String paramString);
  
  List<BpmInstance> getByParentId(String paramString);
  
  void toForbidden(String paramString, boolean paramBoolean);
  
  List<BpmInstance> listParentAndSubById(String paramString);
  
  List<Map> getInstNum();
  
  int updateStatus(String paramString1, String paramString2);
  
  Map getInstIdByBusId(@Param("busId") String paramString);
  
  BpmInstance createInstanceByExecution(ExecutionEntity paramExecutionEntity);
  
  List<BpmTypeTreeCountVO> getApproveHistoryListTypeCount(String paramString, QueryFilter paramQueryFilter);
  
  List<BpmTypeTreeCountVO> applyTaskListTypeCount(String paramString, QueryFilter paramQueryFilter);
  
  List<BpmInstanceVO> listInstTaskJson(QueryFilter paramQueryFilter);
  
  void listInstTaskJsonToExcel(QueryFilter paramQueryFilter, OutputStream paramOutputStream) throws Exception;
  
  Map getInstanceStatusStatis(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmInstanceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */