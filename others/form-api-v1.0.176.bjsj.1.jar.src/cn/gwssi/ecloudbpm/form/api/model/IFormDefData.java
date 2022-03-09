package cn.gwssi.ecloudbpm.form.api.model;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

public interface IFormDefData extends Serializable {
  JSONObject getData();
  
  JSONObject getInitData();
  
  JSONObject getPermission();
  
  JSONObject getTablePermission();
  
  String getHtml();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-api/v1.0.176.bjsj.1/form-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/api/model/IFormDefData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */