package cn.gwssi.ecloudbpm.form.api.model;

import java.util.List;

public interface IFormCustDialog {
  String getKey();
  
  String getName();
  
  String getDesc();
  
  String getStyle();
  
  String getDsKey();
  
  String getDsName();
  
  String getObjType();
  
  String getObjName();
  
  boolean isPage();
  
  int getPageSize();
  
  int getWidth();
  
  int getHeight();
  
  boolean isSystem();
  
  boolean isMultiple();
  
  String getTreeConfigJson();
  
  String getDisplayFieldsJson();
  
  String getConditionFieldsJson();
  
  String getReturnFieldsJson();
  
  String getSortFieldsJson();
  
  String getDataSource();
  
  IFormCustDialogTreeConfig getTreeConfig();
  
  List<? extends IFormCustDialogDisplayField> getDisplayFields();
  
  List<? extends IFormCustDialogConditionField> getConditionFields();
  
  List<? extends IFormCustDialogReturnField> getReturnFields();
  
  List<? extends IFormCustDialogSortField> getSortFields();
  
  IFormCustDialog getPrimaryTableConfig();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/model/IFormCustDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */