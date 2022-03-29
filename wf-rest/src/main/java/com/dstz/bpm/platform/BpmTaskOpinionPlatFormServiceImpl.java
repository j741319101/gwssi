package com.dstz.bpm.platform;

import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
import com.dstz.bpm.api.platform.IBpmTaskOpinionPlatFormService;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BpmTaskOpinionPlatFormServiceImpl implements IBpmTaskOpinionPlatFormService {
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   public IBpmTaskOpinion getBpmTaskOpinion(String id) {
      return (IBpmTaskOpinion)this.bpmTaskOpinionManager.get(id);
   }

   public List<BpmTaskOpinionVO> getInstanceOpinion(String instId, String taskId, String orgId, String status, Boolean extend) {
      return this.bpmTaskOpinionManager.getByInstsOpinion(instId, taskId, orgId, status, extend);
   }
}
