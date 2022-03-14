package com.dstz.bpm.plugin.node.sign.handler;

import com.dstz.bpm.api.constant.ActionType;
import org.springframework.stereotype.Component;

@Component("taskSignOpposeActionHandler")
public class TaskSignOpposeActionHandler extends TaskSignAgreeActionHandler {
   public ActionType getActionType() {
      return ActionType.SIGNOPPOSE;
   }

   public int getSn() {
      return 2;
   }
}
