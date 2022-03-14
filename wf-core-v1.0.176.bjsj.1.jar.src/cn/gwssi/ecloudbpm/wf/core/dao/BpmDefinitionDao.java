package com.dstz.bpm.core.dao;

import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.vo.BpmDefinitionVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.dao.BaseDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmDefinitionDao extends BaseDao<String, BpmDefinition> {
  BpmDefinition getMainByDefKey(String paramString);
  
  void updateActResourceEntity(@Param("deploymentId") String paramString1, @Param("resName") String paramString2, @Param("bpmnBytes") byte[] paramArrayOfbyte);
  
  List<BpmDefinition> getByKey(String paramString);
  
  List<BpmDefinition> getDefByActModelId(String paramString);
  
  BpmDefinition getByActDefId(String paramString);
  
  void updateToMain(String paramString);
  
  List<BpmDefinitionVO> getMyDefinitionList(QueryFilter paramQueryFilter);
  
  void updateForMainVersion(@Param("mainDefId") String paramString1, @Param("key") String paramString2, @Param("newDefId") String paramString3);
  
  List<Map> getDefNumByTree();
  
  int getFlowMaxVersion(String paramString);
  
  boolean countByKey(String paramString);
  
  void lock(BpmDefinition paramBpmDefinition);
  
  void unlock(BpmDefinition paramBpmDefinition);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/dao/BpmDefinitionDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */