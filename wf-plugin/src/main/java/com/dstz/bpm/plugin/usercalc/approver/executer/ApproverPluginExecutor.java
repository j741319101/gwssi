package com.dstz.bpm.plugin.usercalc.approver.executer;

import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApproverPluginExecutor extends AbstractUserCalcPlugin<ApproverPluginDef> {
   @Resource
   BpmTaskOpinionManager taskOpinionManager;

   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ApproverPluginDef pluginDef) {
      List<SysIdentity> bpmIdentities = new ArrayList();
      List<BpmTaskOpinion> taskOpinionList = this.taskOpinionManager.getByInstId(pluginSession.getBpmTask().getInstId());
      Iterator var5 = taskOpinionList.iterator();

      while(var5.hasNext()) {
         IBpmTaskOpinion taskOpinion = (IBpmTaskOpinion)var5.next();
         if (!StringUtil.isEmpty(taskOpinion.getApprover())) {
            SysIdentity bpmIdentity = new DefaultIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), "user", taskOpinion.getTaskOrgId());
            bpmIdentities.add(bpmIdentity);
         }
      }

      return bpmIdentities;
   }
}
