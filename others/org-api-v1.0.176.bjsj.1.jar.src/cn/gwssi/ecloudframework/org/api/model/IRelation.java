package cn.gwssi.ecloudframework.org.api.model;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

@ApiModel(description = "关系定义")
public interface IRelation extends Serializable {
  String getGroupId();
  
  String getUserId();
  
  Integer getIsMaster();
  
  String getType();
  
  String getHasChild();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/IRelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */