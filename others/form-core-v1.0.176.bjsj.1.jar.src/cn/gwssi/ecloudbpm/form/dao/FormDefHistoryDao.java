package cn.gwssi.ecloudbpm.form.dao;

import cn.gwssi.ecloudbpm.form.model.FormDefHistory;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface FormDefHistoryDao extends BaseDao<String, FormDefHistory> {
  void updateByPrimaryKeySelective(FormDefHistory paramFormDefHistory);
  
  FormDefHistory getWithoutHtml(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/dao/FormDefHistoryDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */