package com.dstz.bpm.plugin.core.dao;

import com.dstz.bpm.plugin.core.model.BpmLeaderOptionLog;
import com.dstz.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface BpmLeaderOptionLogDao extends BaseDao<String, BpmLeaderOptionLog> {
   void removeByInstId(@Param("instId") String var1);
}
