package com.dstz.bpm.plugin.global.carboncopy.executor;

import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarbonCopyTaskAction extends DefaultExtendTaskAction {
   @Autowired
   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
   @Autowired
   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;

   public void deleteDataByInstId(String instId) {
      this.bpmCarbonCopyReceiveManager.removeByInstId(instId);
      this.bpmCarbonCopyRecordManager.removeByInstId(instId);
   }
}
