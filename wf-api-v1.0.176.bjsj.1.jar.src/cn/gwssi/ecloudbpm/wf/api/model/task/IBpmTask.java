package cn.gwssi.ecloudbpm.wf.api.model.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel
public interface IBpmTask {
  public static final short STATUS_SUSPEND = 1;
  
  public static final short STATUS_NO_SUSPEND = 0;
  
  @ApiModelProperty("任务ID")
  String getId();
  
  @ApiModelProperty("任务名")
  String getName();
  
  @ApiModelProperty("任务标题")
  String getSubject();
  
  @ApiModelProperty("任务原生TaskID")
  String getTaskId();
  
  @ApiModelProperty("原生ExecutionId")
  String getActExecutionId();
  
  @ApiModelProperty("任务节点ID")
  String getNodeId();
  
  @ApiModelProperty("流程实例ID")
  String getInstId();
  
  @ApiModelProperty("流程定义ID")
  String getDefId();
  
  @ApiModelProperty("候选人ID，若为0 则为多人")
  String getAssigneeId();
  
  @ApiModelProperty("任务状态")
  String getStatus();
  
  @ApiModelProperty("任务优先级，默认50")
  Integer getPriority();
  
  Date getCreateTime();
  
  Date getDueTime();
  
  @ApiModelProperty("任务类型")
  String getTaskType();
  
  @ApiModelProperty("父任务ID 会签，分发等情况会用到")
  String getParentId();
  
  @ApiModelProperty("原生实例ID")
  String getActInstId();
  
  void setAssigneeId(String paramString);
  
  void setAssigneeNames(String paramString);
  
  @ApiModelProperty("候选人Name")
  String getAssigneeNames();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/task/IBpmTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */