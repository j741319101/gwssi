package com.dstz.bpm.plugin.usercalc.samenode.executer;

import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.model.task.IBpmTaskOpinion;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SameNodePluginExecutor extends AbstractUserCalcPlugin<SameNodePluginDef> {
   @Resource
   BpmTaskOpinionManager taskOpinionManager;

   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, SameNodePluginDef sameNodeDef) {
      List<SysIdentity> bpmIdentities = new ArrayList();
      IBpmTask task = pluginSession.getBpmTask();
      if (task == null) {
         return Collections.emptyList();
      } else {
         List<BpmTaskOpinion> taskOpinionList = this.taskOpinionManager.getByInstAndNode(pluginSession.getBpmTask().getInstId(), sameNodeDef.getNodeId());
         if (CollectionUtil.isEmpty(taskOpinionList)) {
            return bpmIdentities;
         } else {
            IBpmTaskOpinion taskOpinion = null;

            for(int i = taskOpinionList.size() - 1; i > -1; --i) {
               if (StringUtil.isNotEmpty(((BpmTaskOpinion)taskOpinionList.get(i)).getApprover()) && !StringUtils.equals(((BpmTaskOpinion)taskOpinionList.get(i)).getStatus(), OpinionStatus.RECALL.getKey())) {
                  taskOpinion = (IBpmTaskOpinion)taskOpinionList.get(i);
                  break;
               }
            }

            if (taskOpinion != null) {
//               SysIdentity bpmIdentity = new DefaultIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), "user", taskOpinion.getTaskOrgId());//todo orgId -d
               SysIdentity bpmIdentity = new DefaultIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), "user");
               bpmIdentities.add(bpmIdentity);
            }

            return bpmIdentities;
         }
      }
   }

   public boolean supportPreView() {
      return false;
   }
}
