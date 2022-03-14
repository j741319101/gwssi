package com.dstz.bpm.api.platform;

import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
import java.util.List;

public interface IBpmTaskOpinionPlatFormService {
  IBpmTaskOpinion getBpmTaskOpinion(String paramString);
  
  List<? extends IBpmTaskOpinion> getInstanceOpinion(String paramString1, String paramString2, String paramString3, String paramString4, Boolean paramBoolean);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/platform/IBpmTaskOpinionPlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */