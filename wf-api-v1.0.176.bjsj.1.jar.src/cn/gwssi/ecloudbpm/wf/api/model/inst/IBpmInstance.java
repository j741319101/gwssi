/*   */ package cn.gwssi.ecloudbpm.wf.api.model.inst;
/*   */ import io.swagger.annotations.ApiModel;
/*   */ import io.swagger.annotations.ApiModelProperty;
/*   */ import java.util.Date;
/*   */ 
/*   */ @ApiModel(description = "流程实例")
/*   */ public interface IBpmInstance {
/* 8 */   public static final Short INSTANCE_FORBIDDEN = Short.valueOf((short)1);
/* 9 */   public static final Short INSTANCE_NO_FORBIDDEN = Short.valueOf((short)0);
/*   */   
/*   */   @ApiModelProperty("流程实例ID")
/*   */   String getId();
/*   */   
/*   */   @ApiModelProperty("流程标题")
/*   */   String getSubject();
/*   */   
/*   */   @ApiModelProperty("流程定义ID")
/*   */   String getDefId();
/*   */   
/*   */   @ApiModelProperty("原生流程定义ID")
/*   */   String getActDefId();
/*   */   
/*   */   @ApiModelProperty("流程定义KEY")
/*   */   String getDefKey();
/*   */   
/*   */   @ApiModelProperty("流程定义名字")
/*   */   String getDefName();
/*   */   
/*   */   @ApiModelProperty("URL 表单业务主键")
/*   */   String getBizKey();
/*   */   
/*   */   @ApiModelProperty("流程实例状态")
/*   */   String getStatus();
/*   */   
/*   */   @ApiModelProperty("流程终止时间")
/*   */   Date getEndTime();
/*   */   
/*   */   @ApiModelProperty("流程持续时间")
/*   */   Long getDuration();
/*   */   
/*   */   @ApiModelProperty("所属分类")
/*   */   String getTypeId();
/*   */   
/*   */   @ApiModelProperty("原生实例ID")
/*   */   String getActInstId();
/*   */   
/*   */   String getCreateBy();
/*   */   
/*   */   @ApiModelProperty("流程实例创建人，也是发起人")
/*   */   String getCreator();
/*   */   
/*   */   Date getCreateTime();
/*   */   
/*   */   @ApiModelProperty("发起人当时的组织ID")
/*   */   String getCreateOrgId();
/*   */   
/*   */   String getUpdateBy();
/*   */   
/*   */   Date getUpdateTime();
/*   */   
/*   */   @ApiModelProperty("是否正式数据，流程定义草稿状态发起的流程均为非正式数据")
/*   */   String getIsFormmal();
/*   */   
/*   */   @ApiModelProperty("父实例ID")
/*   */   String getParentInstId();
/*   */   
/*   */   @ApiModelProperty("是否被禁止")
/*   */   Short getIsForbidden();
/*   */   
/*   */   @ApiModelProperty(hidden = true)
/*   */   String getDataMode();
/*   */   
/*   */   @ApiModelProperty("是否支持移动端")
/*   */   Integer getSupportMobile();
/*   */   
/*   */   @ApiModelProperty("外部子流程父流程定义节点ID")
/*   */   String getSuperNodeId();
/*   */   
/*   */   @ApiModelProperty(hidden = true)
/*   */   Boolean hasCreate();
/*   */   
/*   */   void setHasCreate(Boolean paramBoolean);
/*   */   
/*   */   void setTestData(Boolean paramBoolean);
/*   */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/inst/IBpmInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */