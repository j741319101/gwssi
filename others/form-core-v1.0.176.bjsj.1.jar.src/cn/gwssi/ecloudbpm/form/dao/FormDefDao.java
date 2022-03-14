package cn.gwssi.ecloudbpm.form.dao;

import cn.gwssi.ecloudbpm.form.model.FormDef;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface FormDefDao extends BaseDao<String, FormDef> {
  FormDef getByKey(String paramString);
  
  List<Map> queryWithBo(QueryFilter paramQueryFilter);
  
  List<Map> getFormNumByTree();
  
  int updateFormDef(FormDef paramFormDef);
  
  void updateByPrimaryKeySelective(FormDef paramFormDef);
  
  FormDef getWithoutHtml(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/dao/FormDefDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */