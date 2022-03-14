package com.dstz.bpm.plugin.node.dynamictask.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.recrease.executer.SuperviseTaskExecuter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DynamicTaskPluginExecuter extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, DynamicTaskPluginDef> {
   @Resource
   private HandleTaskCreateEvent handleTaskCreateEvent;
   @Resource
   private HandlePrevDynamicTaskCreate handlePrevDynamicTaskCreate;
   @Resource
   private HandleTaskComplateEvent handleTaskComplateEvent;
   @Resource
   private DynamicInstTaskAction dynamicInstTaskAction;
   @Resource
   private SuperviseTaskExecuter superviseTaskExecuter;

   public Void execute(DefaultBpmTaskPluginSession pluginSession, DynamicTaskPluginDef pluginDef) {
      EventType eventType = pluginSession.getEventType();
      this.dynamicInstTaskAction.checkEvent(pluginSession);
      if (eventType == EventType.TASK_CREATE_EVENT) {
         this.handleTaskCreateEvent.taskCreateEvent(pluginDef, pluginSession);
      } else if (eventType == EventType.TASK_POST_CREATE_EVENT) {
         this.handleTaskCreateEvent.postTaskCreateEvent(pluginSession);
      } else if (eventType == EventType.TASK_PRE_COMPLETE_EVENT) {
         this.superviseTaskExecuter.clearSupervise();
         this.dynamicInstTaskAction.clearTaskThreadLocal();
         this.dynamicInstTaskAction.clearIdentities();
         this.superviseTaskExecuter.setIsNeedSupervise(false);
         this.handlePrevDynamicTaskCreate.prevDynamicTaskCreate(pluginSession);
         this.handleTaskComplateEvent.taskComplateEvent(pluginDef, pluginSession);
      }

      return null;
   }

   public static boolean isParallel(BpmNodeDef bpmNodeDef) {
      AtomicReference<Boolean> isParallel = new AtomicReference(false);
      if (bpmNodeDef == null) {
         return false;
      } else {
         bpmNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
            if (bpmPluginContext instanceof DynamicTaskPluginContext) {
               isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
            }

         });
         if (!(Boolean)isParallel.get()) {
            BpmNodeDef subProcessNodeDef = bpmNodeDef.getParentBpmNodeDef();
            if (subProcessNodeDef != null && subProcessNodeDef instanceof SubProcessNodeDef) {
               subProcessNodeDef.getBpmPluginContexts().forEach((bpmPluginContext) -> {
                  if (bpmPluginContext instanceof DynamicTaskPluginContext) {
                     isParallel.set(((DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmPluginContext).getBpmPluginDef()).getIsParallel());
                  }

               });
            }
         }

         return (Boolean)isParallel.get();
      }
   }
}
