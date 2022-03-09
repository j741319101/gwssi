package cn.gwssi.ecloudbpm.wf.api.model.def;

import cn.gwssi.ecloudframework.base.api.model.IBaseModel;
import java.util.Date;

public interface IBpmDefinition extends IBaseModel {
  String getId();
  
  String getName();
  
  String getKey();
  
  String getDesc();
  
  String getTypeId();
  
  String getStatus();
  
  void setActDefId(String paramString);
  
  String getActDefId();
  
  void setActModelId(String paramString);
  
  String getActModelId();
  
  void setActDeployId(String paramString);
  
  String getActDeployId();
  
  void setVersion(Integer paramInteger);
  
  Integer getVersion();
  
  void setMainDefId(String paramString);
  
  String getMainDefId();
  
  void setIsMain(String paramString);
  
  String getIsMain();
  
  void setCreateBy(String paramString);
  
  String getCreateBy();
  
  void setCreateTime(Date paramDate);
  
  Date getCreateTime();
  
  void setCreateOrgId(String paramString);
  
  String getCreateOrgId();
  
  void setUpdateBy(String paramString);
  
  String getUpdateBy();
  
  void setUpdateTime(Date paramDate);
  
  Date getUpdateTime();
  
  Integer getSupportMobile();
  
  String getDefSetting();
  
  Integer getRev();
  
  String getContentId();
  
  String getContentDes();
  
  public static final class STATUS {
    public static final String DRAFT = "draft";
    
    public static final String DEPLOY = "deploy";
    
    public static final String FORBIDDEN = "forbidden";
  }
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/IBpmDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */