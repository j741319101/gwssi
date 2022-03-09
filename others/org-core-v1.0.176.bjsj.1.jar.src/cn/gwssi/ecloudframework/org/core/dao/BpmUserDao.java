package cn.gwssi.ecloudframework.org.core.dao;

import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.org.api.model.dto.BpmOrgDTO;
import cn.gwssi.ecloudframework.org.api.model.dto.BpmUserDTO;
import cn.gwssi.ecloudframework.org.core.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmUserDao {
  List<User> getUserListByOrgId(@Param("groupId") String paramString);
  
  List<User> getUserListByRelation(@Param("groupId") String paramString1, @Param("type") String paramString2);
  
  List<User> query(QueryFilter paramQueryFilter);
  
  List<BpmUserDTO> getUserOrgInfos(@Param("userIds") String[] paramArrayOfString);
  
  List<BpmOrgDTO> getOrgInfos(@Param("orgIds") String[] paramArrayOfString);
  
  List<User> getUserListByPostId(@Param("postId") String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/dao/BpmUserDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */