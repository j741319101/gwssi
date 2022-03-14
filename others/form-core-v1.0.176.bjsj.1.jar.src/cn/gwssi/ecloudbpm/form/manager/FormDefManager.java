package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormDef;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import com.alibaba.fastjson.JSONArray;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FormDefManager extends Manager<String, FormDef> {
  FormDef getByKey(String paramString);
  
  void saveBackupHtml(FormDef paramFormDef);
  
  String getBackupHtml(FormDef paramFormDef);
  
  String generateFormHtml(String paramString1, JSONArray paramJSONArray, String paramString2);
  
  List<Map> queryWithBo(QueryFilter paramQueryFilter);
  
  List<Map> getFormNumByTree();
  
  int updateFormDef(FormDef paramFormDef);
  
  void updateByPrimaryKeySelective(FormDef paramFormDef);
  
  FormDef getWithoutHtml(String paramString);
  
  InputStream definitionExport(String paramString);
  
  String definitionImport(File paramFile);
  
  void lock(String paramString);
  
  void unlock(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormDefManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */