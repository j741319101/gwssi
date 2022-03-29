package com.dstz.bpm.platform;

import com.dstz.bpm.api.constant.CarbonCopyStatus;
import com.dstz.bpm.api.platform.IBpmCarbonCopyPlatFormService;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class BpmCarbonCopyPlatFormServiceImpl implements IBpmCarbonCopyPlatFormService {
   @Resource
   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;

   public String updateRead(String id) {
      Set<String> ids = (Set)Arrays.stream(StringUtils.split(id, ",")).collect(Collectors.toSet());
      BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
      bpmCarbonCopyReceive.setStatus(CarbonCopyStatus.READ.getKey());
      bpmCarbonCopyReceive.setUpdateTime(new Date());
      this.bpmCarbonCopyReceiveManager.updateRead(bpmCarbonCopyReceive, ids);
      return "更新成功";
   }

   public String updateReadUser(String userId) {
      this.bpmCarbonCopyReceiveManager.updateReadByUser(userId);
      return "更新成功";
   }
}
