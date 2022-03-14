package com.dstz.bpm.plugin.node.sign.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.action.button.ButtonChecker;
import com.dstz.bpm.api.model.nodedef.Button;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("SIGNButtonChecker")
public class SignButtonChecker implements ButtonChecker {
   @Resource(
      name = "taskSignOpposeActionHandler"
   )
   TaskSignOpposeActionHandler taskSignOpposeActionHandler;
   @Resource(
      name = "taskSignAgreeActionHandler"
   )
   TaskSignAgreeActionHandler taskSignAgreeActionHandler;
   @Resource
   AddSignActionHandler addSignActionHandler;
   private final String supportsType;

   public SignButtonChecker() {
      this.supportsType = String.format("%s,%s,%s,%s,%s,%s,%s", ActionType.AGREE.getKey(), ActionType.OPPOSE.getKey(), ActionType.REJECT.getKey(), ActionType.LOCK.getKey(), ActionType.UNLOCK.getKey(), ActionType.RECALL.getKey(), ActionType.RECALL.getKey());
   }

   public boolean isSupport(Button btn) {
      return !this.supportsType.contains(btn.getAlias());
   }

   public void specialBtnHandler(List<Button> btn) {
      ActionType actionType = this.taskSignOpposeActionHandler.getActionType();
      Button signOppose = new Button(actionType.getName(), actionType.getKey(), this.taskSignOpposeActionHandler.getDefaultGroovyScript(), this.taskSignOpposeActionHandler.getConfigPage());
      ActionType agreeAction = this.taskSignAgreeActionHandler.getActionType();
      Button signAgree = new Button(agreeAction.getName(), agreeAction.getKey(), this.taskSignAgreeActionHandler.getDefaultGroovyScript(), this.taskSignAgreeActionHandler.getConfigPage());
      ActionType addSignAction = this.addSignActionHandler.getActionType();
      if (!StringUtils.contains("addSign", addSignAction.getKey())) {
         Button addSign = new Button(addSignAction.getName(), addSignAction.getKey(), this.addSignActionHandler.getDefaultGroovyScript(), this.addSignActionHandler.getConfigPage());
         btn.add(addSign);
      }

      btn.add(0, signOppose);
      btn.add(0, signAgree);
   }
}
