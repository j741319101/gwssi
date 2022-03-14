package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.DataDict;
import com.alibaba.fastjson.JSONArray;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface DataDictManager extends Manager<String, DataDict> {
  List<DataDict> getDictNodeList(String paramString, Boolean paramBoolean);
  
  JSONArray getDictTree();
  
  Integer countDictByTypeId(String paramString);
  
  void importData(XSSFWorkbook paramXSSFWorkbook, Map<String, Integer> paramMap);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/DataDictManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */