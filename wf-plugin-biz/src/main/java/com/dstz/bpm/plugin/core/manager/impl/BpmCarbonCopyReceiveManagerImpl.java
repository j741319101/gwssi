package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmCarbonCopyReceiveDao;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.Page;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmCarbonCopyReceiveManager")
public class BpmCarbonCopyReceiveManagerImpl extends BaseManager<String, BpmCarbonCopyReceive> implements BpmCarbonCopyReceiveManager {
   @Resource
   private BpmCarbonCopyReceiveDao bpmCarbonCopyReceiveDao;

   public int createList(List<BpmCarbonCopyReceive> records) {
      return this.bpmCarbonCopyReceiveDao.createList(records);
   }

   public int updateRead(BpmCarbonCopyReceive record, Set<String> primaryKeys) {
      record.setRead(Boolean.TRUE);
      record.setUpdateTime(new Date());
      return this.bpmCarbonCopyReceiveDao.updateRead(record, primaryKeys);
   }

   public int updateReadByUser(String userId) {
      return this.bpmCarbonCopyReceiveDao.updateReadByUser(userId);
   }

   public List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter queryFilter) {
      return this.bpmCarbonCopyReceiveDao.listUserReceiveList(queryFilter);
   }

   public void removeByInstId(String instId) {
      QueryFilter queryFilter = new DefaultQueryFilter();
      queryFilter.addFilter("b.inst_id", instId, QueryOP.EQUAL);
      this.bpmCarbonCopyReceiveDao.listUserReceiveList(queryFilter).forEach((bpmUserReceive) -> {
         this.bpmCarbonCopyReceiveDao.remove(bpmUserReceive.getId());
      });
   }

   public List<BpmCarbonCopyReceive> query2(QueryFilter queryFilter) {
      return this.bpmCarbonCopyReceiveDao.query2(queryFilter);
   }

   public List<BpmCarbonCopyReceive> getByParam(String instId, String receiveUserId, String nodeId) {
      QueryFilter filter = new DefaultQueryFilter();
      filter.setPage((Page)null);
      if (StringUtil.isNotEmpty(receiveUserId)) {
         filter.addFilter("a.receive_user_id", receiveUserId, QueryOP.EQUAL);
      }

      if (StringUtil.isNotEmpty(instId)) {
         filter.addFilter("b.inst_id", instId, QueryOP.EQUAL);
      }

      if (StringUtil.isNotEmpty(nodeId)) {
         filter.addFilter("b.node_id", nodeId, QueryOP.EQUAL);
      }

      return this.query2(filter);
   }
}
