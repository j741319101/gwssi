package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface DynamicTaskDao extends BaseDao<String, DynamicTask> {
   void removeByInstId(@Param("instId") String var1);

   void updateEndByInstId(@Param("instId") String var1);
}
