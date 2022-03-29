package com.dstz.bpm.plugin.node.recrease.executer;

import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.plugin.node.recrease.handler.RecreaseTaskAction;
import com.dstz.base.api.exception.BusinessException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SuperviseTaskAction extends DefaultExtendTaskAction {
   @Resource
   private RecreaseTaskAction recreaseTaskAction;
   @Resource
   private BpmInstanceManager bpmInstanceManager;

   public void canFreeJump(IBpmTask bpmTask) {
      BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(bpmTask.getInstId());
      JSONObject result = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
      Boolean can = result.getBoolean("existAliveTask");
      if (can) {
         throw new BusinessException("监管任务跳转，有受监管任务未完成");
      } else {
         JSONObject nodeTasks = result.getJSONObject("tasks");
         if (nodeTasks != null) {
            Iterator var6 = nodeTasks.keySet().iterator();

            while(true) {
               JSONArray tasks;
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  String nodeId = (String)var6.next();
                  tasks = nodeTasks.getJSONObject(nodeId).getJSONArray("tasks");
               } while(tasks == null);

               Iterator var9 = tasks.iterator();

               while(var9.hasNext()) {
                  Object task = var9.next();
                  String approver = ((JSONObject)task).getString("approver");
                  if (StringUtils.isEmpty(approver)) {
                     throw new BusinessException("监管任务跳转，有受监管任务未完成");
                  }
               }
            }
         }
      }
   }
}
