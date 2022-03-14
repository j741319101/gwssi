package cn.gwssi.ecloudbpm.form.dao;

import cn.gwssi.ecloudbpm.form.model.FormBusSet;
import com.dstz.base.dao.BaseDao;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface FormBusSetDao extends BaseDao<String, FormBusSet> {
  FormBusSet getByFormKey(String paramString);
  
  Integer isExist(FormBusSet paramFormBusSet);
  
  void removeByFormKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/dao/FormBusSetDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */