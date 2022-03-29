package com.dstz.bpm.plugin.node.recrease.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.action.button.ButtonChecker;
import com.dstz.bpm.api.model.nodedef.Button;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("SUPERVISEButtonChecker")
public class SuperviseTaskButtonChecker implements ButtonChecker {
   @Resource
   private IncreaseTaskHandler increaseTaskHandler;
   private final String supportsType;

   public SuperviseTaskButtonChecker() {
      this.supportsType = String.format("%s,%s,%s,%s,%s,%s", ActionType.ADDDO.getKey(), ActionType.ADDSIGN.getKey(), ActionType.REJECT.getKey(), ActionType.LOCK.getKey(), ActionType.UNLOCK.getKey(), ActionType.TURN.getKey());
   }

   public boolean isSupport(Button btn) {
      return !this.supportsType.contains(btn.getAlias());
   }

   public void specialBtnHandler(List<Button> btn) {
      ActionType actionType = this.increaseTaskHandler.getActionType();
      Button increase = new Button(actionType.getName(), actionType.getKey(), this.increaseTaskHandler.getDefaultGroovyScript(), this.increaseTaskHandler.getConfigPage());
      if (btn.contains(increase)) {
         Button agree = new Button("同意", "agree", "", "");
         btn.remove(agree);
         agree.setAlias("oppose");
         btn.remove(agree);
      }

   }
}
