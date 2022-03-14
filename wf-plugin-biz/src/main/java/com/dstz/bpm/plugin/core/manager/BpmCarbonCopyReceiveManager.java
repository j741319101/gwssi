package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.util.List;
import java.util.Set;

public interface BpmCarbonCopyReceiveManager extends Manager<String, BpmCarbonCopyReceive> {
   int createList(List<BpmCarbonCopyReceive> var1);

   int updateRead(BpmCarbonCopyReceive var1, Set<String> var2);

   int updateReadByUser(String var1);

   List<BpmUserReceiveCarbonCopyRecordVO> listUserReceive(QueryFilter var1);

   void removeByInstId(String var1);

   List<BpmCarbonCopyReceive> query2(QueryFilter var1);

   List<BpmCarbonCopyReceive> getByParam(String var1, String var2, String var3);
}
