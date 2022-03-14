package com.dstz.bpm.plugin.usercalc.util;

import com.dstz.bpm.api.engine.constant.LogicType;
import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import com.dstz.base.core.util.AppUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.model.DefaultIdentity;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.dstz.sys.api.model.SysIdentity;
import org.apache.commons.lang3.StringUtils;

public class AtomicUserCalcPreview {
   public static List<SysIdentity> calcUserPreview(List<Object[]> sysIdentitieAndLogic) {
      UserService userService = (UserService)AppUtil.getImplInstanceArray(UserService.class).get(0);
      List<SysIdentity> sysIdentities = new ArrayList();
      sysIdentitieAndLogic.forEach((identitieAndLogic) -> {
         SysIdentity sysIdentity = (SysIdentity)identitieAndLogic[0];
         String logicType = (String)identitieAndLogic[1];
         if (StringUtils.equals(sysIdentity.getType(), "user")) {
            List<SysIdentity> sysIdentities1 = new ArrayList();
            sysIdentities1.add(sysIdentity);
            UserAssignRuleCalc.calc(sysIdentities, sysIdentities1, LogicType.fromKey(logicType));
         } else {
            List<IUser> users =(List<IUser>) userService.getUserListByGroup(sysIdentity.getType(), sysIdentity.getId());
            List<SysIdentity> identities = new ArrayList();
            if (CollectionUtil.isNotEmpty(users)) {
               users.forEach((user) -> {
                  identities.add(new DefaultIdentity(user));
               });
            }

            UserAssignRuleCalc.calc(sysIdentities, identities, LogicType.fromKey(logicType));
         }

      });
      return sysIdentities;
   }
}
