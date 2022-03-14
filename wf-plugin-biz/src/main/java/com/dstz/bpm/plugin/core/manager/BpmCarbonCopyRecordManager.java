package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.base.manager.Manager;
import java.util.List;

public interface BpmCarbonCopyRecordManager extends Manager<String, BpmCarbonCopyRecord> {
   int createList(List<BpmCarbonCopyRecord> var1);

   void removeByInstId(String var1);
}
