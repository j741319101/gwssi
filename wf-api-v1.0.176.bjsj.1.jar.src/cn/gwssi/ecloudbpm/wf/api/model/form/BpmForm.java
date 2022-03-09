package cn.gwssi.ecloudbpm.wf.api.model.form;

import cn.gwssi.ecloudbpm.form.api.model.FormCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "流程表单信息DTO")
public interface BpmForm extends Serializable {
  @ApiModelProperty("表单名称描述")
  String getName();
  
  void setName(String paramString);
  
  @ApiModelProperty("表单类型")
  FormCategory getType();
  
  void setType(FormCategory paramFormCategory);
  
  @ApiModelProperty("Inner表单Key，iframe的URL值")
  String getFormValue();
  
  void setFormValue(String paramString);
  
  @ApiModelProperty(hidden = true)
  boolean isFormEmpty();
  
  @ApiModelProperty("Url 表单处理器")
  String getFormHandler();
  
  void setFormHandler(String paramString);
  
  @ApiModelProperty("Inner 表单HTML 内容")
  String getFormHtml();
  
  void setFormHtml(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/form/BpmForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */