package com.dstz.bpm.api.platform;

import com.dstz.bpm.api.model.def.IBpmDefinition;
import com.dstz.base.db.model.page.PageResult;
import java.util.List;

public interface IBpmDefinitionPlatFormService {
  PageResult<List<IBpmDefinition>> listJson(Integer paramInteger1, Integer paramInteger2, String paramString1, String paramString2);
  
  PageResult<List<IBpmDefinition>> listMyJson(Integer paramInteger1, Integer paramInteger2, String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/platform/IBpmDefinitionPlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */