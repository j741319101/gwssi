package cn.gwssi.ecloudframework.sys.core.manager;

import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.core.model.SysTree;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface SysTreeManager extends Manager<String, SysTree> {
  SysTree getByKey(String paramString);
  
  void removeContainNode(String paramString);
  
  void importData(XSSFWorkbook paramXSSFWorkbook, Map<String, Integer> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/SysTreeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */