package cn.gwssi.ecloudbpm.form.dao;

import cn.gwssi.ecloudbpm.form.model.FormCustomConfHistory;
import com.dstz.base.dao.BaseDao;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface FormCustomConfHistoryDao extends BaseDao<String, FormCustomConfHistory> {
  void updateByPrimaryKeySelective(FormCustomConfHistory paramFormCustomConfHistory);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/dao/FormCustomConfHistoryDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */