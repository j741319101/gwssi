package com.dstz.bpm.core.dao;

import com.dstz.bpm.core.model.BpmBusLink;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmBusLinkDao extends BaseDao<String, BpmBusLink> {
  List<BpmBusLink> getByInstanceId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmBusLinkDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */