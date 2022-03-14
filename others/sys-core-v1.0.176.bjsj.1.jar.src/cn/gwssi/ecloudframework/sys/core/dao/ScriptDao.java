package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.Script;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface ScriptDao extends BaseDao<String, Script> {
  List<String> getDistinctCategory();
  
  Integer isNameExists(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/dao/ScriptDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */