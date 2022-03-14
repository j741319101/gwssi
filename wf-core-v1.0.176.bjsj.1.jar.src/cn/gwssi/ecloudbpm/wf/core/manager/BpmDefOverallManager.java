package com.dstz.bpm.core.manager;

import com.dstz.bpm.core.model.overallview.BpmOverallView;
import java.util.List;
import java.util.Map;

public interface BpmDefOverallManager {
  BpmOverallView getBpmOverallView(String paramString);
  
  void saveBpmOverallView(BpmOverallView paramBpmOverallView);
  
  Map<String, List<BpmOverallView>> importPreview(String paramString) throws Exception;
  
  void importSave(List<BpmOverallView> paramList);
  
  Map<String, String> exportBpmDefinitions(String... paramVarArgs) throws Exception;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmDefOverallManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */