package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class HandleTaskComplateEvent {
   @Resource
   DynamicTaskManager dynamicTaskManager;
   @Resource
   HandelRejectAction handelRejectAction;
   @Resource
   DynamicInstTaskAction dynamicInstTaskAction;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;

   public void taskComplateEvent(DynamicTaskPluginDef pluginDef, DefaultBpmTaskPluginSession pluginSession) {
      IBpmTask task = pluginSession.getBpmTask();
      this.superviseTaskExecuter.taskComplete(pluginSession);
      boolean isEnabled = pluginDef.getIsEnabled();
      BpmNodeDef bpmNodeDef = this.dynamicInstTaskAction.getDynamicInstNodeDef(pluginSession);
      DefualtTaskActionCmd taskActionCmd = (DefualtTaskActionCmd)BpmContext.getActionModel();
      boolean isInstDy = false;
      if (!isEnabled) {
         isInstDy = this.dynamicInstTaskAction.isEndDynamicInstTask(bpmNodeDef, pluginSession);
         isEnabled = isInstDy;
      }

      if (isEnabled) {
         if (!isInstDy) {
            QueryFilter queryFilter = new DefaultQueryFilter();
            queryFilter.addFilter("inst_id_", task.getInstId(), QueryOP.EQUAL);
            queryFilter.addFilter("node_id_", task.getNodeId(), QueryOP.EQUAL);
            queryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
            List<DynamicTask> dynamicTasks = this.dynamicTaskManager.query(queryFilter);
            if (dynamicTasks == null || dynamicTasks.size() == 0) {
               return;
            }

            DynamicTask dynamicTask = (DynamicTask)dynamicTasks.get(0);
            if (taskActionCmd.getActionType() == ActionType.REJECT) {
               this.handelRejectAction.handelReject(taskActionCmd, dynamicTask);
               return;
            }

            dynamicTask.setCurrentIndex(dynamicTask.getCurrentIndex() + 1);
            if (dynamicTask.getCurrentIndex() >= dynamicTask.getAmmount()) {
               dynamicTask.setStatus("completed");
               this.dynamicTaskManager.update(dynamicTask);
               return;
            }

            if (pluginDef.getIsParallel()) {
               taskActionCmd.setDestinations(new String[0]);
            } else {
               taskActionCmd.setDestination(task.getNodeId());
            }

            this.dynamicTaskManager.update(dynamicTask);
         } else {
            this.dynamicInstTaskAction.dynamicInstTaskComplate(pluginSession, bpmNodeDef);
         }

      }
   }
}
