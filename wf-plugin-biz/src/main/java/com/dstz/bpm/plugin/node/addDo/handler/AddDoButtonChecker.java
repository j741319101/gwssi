package com.dstz.bpm.plugin.node.addDo.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.action.button.ButtonChecker;
import com.dstz.bpm.api.model.nodedef.Button;
import cn.hutool.core.collection.CollectionUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ADD_DOButtonChecker")
public class AddDoButtonChecker implements ButtonChecker {
   private final List<String> supportsTypes;
   @Autowired
   private AddDoAgreeActionHandler addDoAgreeActionHandler;

   public AddDoButtonChecker() {
      this.supportsTypes = CollectionUtil.newArrayList(new String[]{ActionType.TASKOPINION.getKey(), ActionType.FLOWIMAGE.getKey(), ActionType.SAVE.getKey()});
   }

   public boolean isSupport(Button btn) {
      return this.supportsTypes.contains(btn.getAlias());
   }

   public void specialBtnHandler(List<Button> btn) {
      ActionType actionType = this.addDoAgreeActionHandler.getActionType();
      if (!StringUtils.contains("addSign", actionType.getKey())) {
         Button addDoAgree = new Button(actionType.getName(), actionType.getKey(), this.addDoAgreeActionHandler.getDefaultGroovyScript(), this.addDoAgreeActionHandler.getConfigPage());
         btn.add(0, addDoAgree);
      }

   }
}
