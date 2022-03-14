package com.dstz.org.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.org.core.model.Post;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface PostDao extends BaseDao<String, Post> {
  Post getByCode(@Param("code") String paramString1, @Param("excludeId") String paramString2);
  
  int updateByPrimaryKeySelective(Post paramPost);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/dao/PostDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */