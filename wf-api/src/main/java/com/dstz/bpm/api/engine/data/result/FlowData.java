package com.dstz.bpm.api.engine.data.result;

import com.dstz.bpm.api.model.form.BpmForm;
import com.dstz.bpm.api.model.nodedef.Button;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "流程实例，流程任务数据信息")
public interface FlowData {
  @ApiModelProperty("流程定义ID")
  String getDefId();
  
  @ApiModelProperty("流程当前节点表单")
  BpmForm getForm();
  
  @ApiModelProperty("流程自定义表单业务数据")
  JSONObject getData();
  
  @ApiModelProperty("流程自定义表单 字段权限")
  JSONObject getPermission();
  
  @ApiModelProperty("流程自定义表单 表权限")
  JSONObject getTablePermission();
  
  @ApiModelProperty("流程自定义表单 初始化数据，可用于子表数据复制赋值")
  JSONObject getInitData();
  
  @ApiModelProperty("流程 当前节点按钮信息")
  List<Button> getButtonList();
  
  String getLabels();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/data/result/FlowData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */