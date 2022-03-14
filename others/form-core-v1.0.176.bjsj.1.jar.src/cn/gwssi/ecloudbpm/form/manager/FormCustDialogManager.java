package cn.gwssi.ecloudbpm.form.manager;

import cn.gwssi.ecloudbpm.form.model.FormCustDialog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.db.model.table.Column;
import com.dstz.base.db.model.table.Table;
import com.dstz.base.manager.Manager;
import java.util.List;
import java.util.Map;

public interface FormCustDialogManager extends Manager<String, FormCustDialog> {
  FormCustDialog getByKey(String paramString);
  
  Map<String, String> searchObjName(FormCustDialog paramFormCustDialog);
  
  Table<Column> getTable(FormCustDialog paramFormCustDialog);
  
  List<?> data(FormCustDialog paramFormCustDialog, QueryFilter paramQueryFilter);
  
  boolean existsByKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/FormCustDialogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */