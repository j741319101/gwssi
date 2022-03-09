package com.dstz.bpm.api.platform;

import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.org.api.model.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface IBpmTaskPlatFormService {
  String doAction(FlowRequestParam paramFlowRequestParam, String paramString);
  
  Map<String, Object> handleNodeFreeSelectUser(FlowRequestParam paramFlowRequestParam, String paramString);
  
  PageResult<List<IBpmTask>> getTodoList(Integer paramInteger1, Integer paramInteger2, String paramString);
  
  PageResult<List<IBpmTask>> getMyTodoList(PageDTO paramPageDTO, String paramString);
  
  PageResult<List<IBpmTask>> getApproveList(PageDTO paramPageDTO, String paramString);
  
  PageResult<List<IBpmTask>> getApplyTaskList(PageDTO paramPageDTO, String paramString1, String paramString2);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/platform/IBpmTaskPlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */