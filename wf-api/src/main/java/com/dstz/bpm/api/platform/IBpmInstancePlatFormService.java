package com.dstz.bpm.api.platform;

import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.task.IBpmTaskApprove;
import com.dstz.base.db.model.page.PageResult;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IBpmInstancePlatFormService {
  IBpmInstance getBpmInstance(String paramString);
  
  List<? extends IBpmInstance> getBpmInstanceByParentId(String paramString);
  
  ResponseEntity<byte[]> flowImage(String paramString1, String paramString2);
  
  JSONObject getInstanceOpinionStruct(String paramString);
  
  JSONObject getFlowImageInfo(String paramString1, String paramString2, String paramString3);
  
  String doAction(FlowRequestParam paramFlowRequestParam, String paramString);
  
  PageResult<List<IBpmTaskApprove>> getApproveHistoryList(String paramString1, String paramString2, String paramString3, Integer paramInteger1, Integer paramInteger2, String paramString4, String paramString5, String paramString6, String paramString7);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/platform/IBpmInstancePlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */