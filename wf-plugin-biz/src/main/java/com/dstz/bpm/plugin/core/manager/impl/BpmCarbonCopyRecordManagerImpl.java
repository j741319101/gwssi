package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmCarbonCopyRecordDao;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.manager.impl.BaseManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bpmCarbonCopyRecordManager")
public class BpmCarbonCopyRecordManagerImpl extends BaseManager<String, BpmCarbonCopyRecord> implements BpmCarbonCopyRecordManager {
   @Autowired
   private BpmCarbonCopyRecordDao bpmCarbonCopyRecordDao;

   public int createList(List<BpmCarbonCopyRecord> records) {
      return this.bpmCarbonCopyRecordDao.createList(records);
   }

   public void removeByInstId(String instId) {
      this.bpmCarbonCopyRecordDao.removeByInstId(instId);
   }
}
