package com.dstz.bpm.plugin.impl;

import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.service.BpmPluginService;
import com.dstz.bpm.plugin.global.taskskip.context.TaskSkipPluginContext;
import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
import com.dstz.base.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BpmPluginServiceImpl implements BpmPluginService {
   @Autowired
   BpmProcessDefService bpmProcessDefService;

   public Boolean isSkipFirstNode(String defId) {
      DefaultBpmProcessDef def = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
      BpmPluginContext context = def.getBpmPluginContext(TaskSkipPluginContext.class);
      if (context == null) {
         return false;
      } else {
         TaskSkipPluginDef pluginDef = (TaskSkipPluginDef)context.getBpmPluginDef();
         if (pluginDef != null && !StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
            return pluginDef.getSkipTypeArr().indexOf(TaskSkipType.FIRSTNODE_SKIP.getKey()) != -1 ? true : false;
         } else {
            return false;
         }
      }
   }
}
