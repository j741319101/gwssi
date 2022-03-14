package com.dstz.bpm.plugin.node.sign.executer;

import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.api.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SignTaskPluginAction extends DefaultExtendTaskAction {
   public void canFreeJump(IBpmTask bpmTask) {
      if (StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN.getKey()) || StringUtils.equals(bpmTask.getTaskType(), TaskType.SIGN_SOURCE.getKey())) {
         throw new BusinessException("会签节点暂不适用跳转");
      }
   }

   public boolean isSignTask(BpmNodeDef bpmNodeDef) {
      SignTaskPluginContext signTaskPluginContext = (SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class);
      return signTaskPluginContext != null && ((SignTaskPluginDef)signTaskPluginContext.getBpmPluginDef()).isSignMultiTask();
   }
}
