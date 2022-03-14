package com.dstz.bpm.plugin.global.multinst.executer;

import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.handler.DefaultExtendTaskAction;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.plugin.global.multinst.context.MultInstPluginContext;
import com.dstz.bpm.plugin.global.multinst.def.MultInst;
import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
import com.dstz.base.api.exception.BusinessException;
import java.util.Iterator;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MultInstTaskAction extends DefaultExtendTaskAction {
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;

   public boolean isContainNode(String nodeId, BpmProcessDef bpmProcessDef, String appendType) {
      MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
      if (pluginContext != null) {
         MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
         Iterator var6 = pluginDef.getMultInsts().iterator();

         while(var6.hasNext()) {
            MultInst multInst = (MultInst)var6.next();
            if (StringUtils.equals(appendType, "start")) {
               if (StringUtils.equals(nodeId, multInst.getStartNodeKey())) {
                  return true;
               }
            } else if (StringUtils.equals(appendType, "end")) {
               if (StringUtils.equals(nodeId, multInst.getEndNodeKey())) {
                  return true;
               }
            } else if (StringUtils.equals(appendType, "both") && (StringUtils.equals(nodeId, multInst.getEndNodeKey()) || StringUtils.equals(nodeId, multInst.getStartNodeKey()))) {
               return true;
            }

            if (multInst.getContainNode().contains(nodeId)) {
               return true;
            }
         }
      }

      return false;
   }

   public void parseMultInstContainNode(BpmProcessDef bpmProcessDef) {
      MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
      if (pluginContext != null) {
         MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
         pluginDef.getMultInsts().forEach((multInst) -> {
            multInst.parseMultInstContainNode((DefaultBpmProcessDef)bpmProcessDef);
         });
      }

   }

   public void canFreeJump(IBpmTask bpmTask) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      String taskId = model.getTaskId();
      BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
      if (StringUtils.isNotEmpty(opinion.getTrace()) && !this.isContainNode(model.getDestination(), this.bpmProcessDefService.getBpmProcessDef(model.getBpmDefinition().getId()), "end")) {
         throw new BusinessException("多实例任务暂不适用跳转到多实例外的任务");
      }
   }

   public MultInst getMultInstConfig(String nodeId, BpmProcessDef bpmProcessDef, String appendType) {
      MultInstPluginContext pluginContext = (MultInstPluginContext)((DefaultBpmProcessDef)bpmProcessDef).getBpmPluginContext(MultInstPluginContext.class);
      if (pluginContext != null) {
         MultInstPluginDef pluginDef = (MultInstPluginDef)pluginContext.getBpmPluginDef();
         Iterator var6 = pluginDef.getMultInsts().iterator();

         while(var6.hasNext()) {
            MultInst multInst = (MultInst)var6.next();
            if (StringUtils.equals(appendType, "start")) {
               if (StringUtils.equals(nodeId, multInst.getStartNodeKey())) {
                  return multInst;
               }
            } else if (StringUtils.equals(appendType, "end")) {
               if (StringUtils.equals(nodeId, multInst.getEndNodeKey())) {
                  return multInst;
               }
            } else if (StringUtils.equals(appendType, "both") && (StringUtils.equals(nodeId, multInst.getEndNodeKey()) || StringUtils.equals(nodeId, multInst.getStartNodeKey()))) {
               return multInst;
            }

            if (multInst.getContainNode().contains(nodeId)) {
               return multInst;
            }
         }
      }

      return null;
   }
}
