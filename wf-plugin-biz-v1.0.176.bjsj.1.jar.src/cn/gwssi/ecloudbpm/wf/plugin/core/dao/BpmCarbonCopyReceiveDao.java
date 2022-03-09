package cn.gwssi.ecloudbpm.wf.plugin.core.dao;

import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
import cn.gwssi.ecloudbpm.wf.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.dao.BaseDao;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface BpmCarbonCopyReceiveDao extends BaseDao<String, BpmCarbonCopyReceive> {
  int createList(@Param("records") List<BpmCarbonCopyReceive> paramList);
  
  int updateRead(@Param("record") BpmCarbonCopyReceive paramBpmCarbonCopyReceive, @Param("primaryKeys") Set<String> paramSet);
  
  int updateReadByUser(@Param("userId") String paramString);
  
  List<BpmUserReceiveCarbonCopyRecordVO> listUserReceiveList(QueryFilter paramQueryFilter);
  
  List<BpmCarbonCopyReceive> query2(QueryFilter paramQueryFilter);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/dao/BpmCarbonCopyReceiveDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */