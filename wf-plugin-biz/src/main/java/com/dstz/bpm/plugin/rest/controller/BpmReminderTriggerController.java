package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.plugin.core.manager.BpmReminderTriggerManager;
import com.dstz.bpm.plugin.core.model.BpmReminderTrigger;
import com.dstz.base.rest.BaseController;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/bpmReminderTrigger"})
public class BpmReminderTriggerController extends BaseController<BpmReminderTrigger> {
   @Resource
   BpmReminderTriggerManager bpmReminderTriggerManager;

   protected String getModelDesc() {
      return "流程催办触发";
   }
}
