package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.WorkbenchPanel;
import com.alibaba.fastjson.JSON;
import java.util.List;
import java.util.Map;

public interface WorkbenchPanelManager extends Manager<String, WorkbenchPanel> {
  List<WorkbenchPanel> getByUserId(String paramString);
  
  JSON getPanelData(Map<String, String> paramMap);
  
  JSON getDataByInterFace(QueryFilter paramQueryFilter, String paramString);
  
  List<WorkbenchPanel> getMyUsablePanels(QueryFilter paramQueryFilter);
  
  List<WorkbenchPanel> getBylayoutKey(String paramString);
  
  JSON getTestData();
  
  JSON getPieData();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/WorkbenchPanelManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */