package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class HandelRejectAction {
   @Resource
   DynamicTaskManager dynamicTaskManager;
   @Resource
   BpmTaskManager bpmTaskManager;

   public void handelReject(DefualtTaskActionCmd taskActionCmd, DynamicTask dynamicTask) {
      String destination = taskActionCmd.getDestination();
      if (!StringUtil.isNotEmpty(destination)) {
         if (StringUtil.isEmpty(destination)) {
            dynamicTask.setStatus("completed");
            this.dynamicTaskManager.update(dynamicTask);
         } else if (dynamicTask.getCurrentIndex() >= 1) {
            dynamicTask.setCurrentIndex(dynamicTask.getCurrentIndex() - 1);
            this.dynamicTaskManager.update(dynamicTask);
            taskActionCmd.setDestination(dynamicTask.getNodeId());
         }
      } else if (destination.indexOf("$$") == -1) {
         dynamicTask.setCurrentIndex(0);
         dynamicTask.setStatus("completed");
         this.dynamicTaskManager.update(dynamicTask);
      } else {
         String taskName = destination.substring(destination.indexOf("$$") + 2);
         List<DynamicTaskIdentitys> taskIdentitys = dynamicTask.loadAllTaskIdentitys();

         for(int i = 0; i < taskIdentitys.size(); ++i) {
            DynamicTaskIdentitys taskIdentity = (DynamicTaskIdentitys)taskIdentitys.get(i);
            if (taskIdentity.getTaskName().equals(taskName)) {
               dynamicTask.setCurrentIndex(i);
               break;
            }
         }

         this.dynamicTaskManager.update(dynamicTask);
      }
   }

   public void handelBack2Tasks(BpmTask task, DefualtTaskActionCmd taskActionCmd) {
      DynamicTask dynamicTask = this.dynamicTaskManager.getByStatus(task.getNodeId(), task.getActExecutionId(), "completed");
      if (dynamicTask == null) {
         throw new BusinessException("动态任务配置丢失，请联系管理员，节点：" + task.getName());
      } else {
         ActionCmd submitActionCmd = BpmContext.submitActionModel();
         String destination = submitActionCmd.getDestination();
         if (StringUtil.isNotEmpty(destination) && destination.indexOf("$$") != -1) {
            String taskName = destination.substring(destination.indexOf("$$") + 2);
            List<DynamicTaskIdentitys> taskIdentitys = dynamicTask.loadAllTaskIdentitys();

            for(int i = 0; i < taskIdentitys.size(); ++i) {
               DynamicTaskIdentitys taskIdentity = (DynamicTaskIdentitys)taskIdentitys.get(i);
               if (taskIdentity.getTaskName().equals(taskName)) {
                  dynamicTask.setCurrentIndex(i);
                  break;
               }
            }

            dynamicTask.setStatus("runtime");
            this.dynamicTaskManager.update(dynamicTask);
            taskActionCmd.setBpmIdentity(task.getNodeId(), dynamicTask.loadCurrentTaskIdentitys().getNodeIdentitys());
            task.setName(taskName);
            this.bpmTaskManager.update(task);
         } else {
            DynamicTaskIdentitys taskIdentitys = dynamicTask.loadCurrentTaskIdentitys();
            taskActionCmd.setBpmIdentity(task.getNodeId(), taskIdentitys.getNodeIdentitys());
            dynamicTask.setStatus("runtime");
            this.dynamicTaskManager.update(dynamicTask);
            task.setName(taskIdentitys.getTaskName());
            this.bpmTaskManager.update(task);
         }
      }
   }
}
