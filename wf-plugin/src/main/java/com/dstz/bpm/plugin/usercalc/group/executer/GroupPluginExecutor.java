package com.dstz.bpm.plugin.usercalc.group.executer;

import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.usercalc.group.def.GroupPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.service.GroupService;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class GroupPluginExecutor extends AbstractUserCalcPlugin<GroupPluginDef> {
   @Resource
   GroupService userGroupService;

   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, GroupPluginDef def) {
      if (StringUtil.isEmpty(def.getGroupKey())) {
         throw new BusinessException("流程定义候选人配置错误：" + def.getGroupName());
      } else {
         String groupType = def.getType();
         List<SysIdentity> identityList = new ArrayList();
         String[] var5 = def.getGroupKey().split(",");
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String key = var5[var7];
            if (!StringUtil.isEmpty(key)) {
               IGroup group = this.userGroupService.getByCode(groupType, key);
               if (group != null) {
                  DefaultIdentity identity = new DefaultIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType(), group.getGroupId());// todo orgId -d
//                  DefaultIdentity identity = new DefaultIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType());
                  if (group.getSn() != null) {
                     identity.setSn(group.getSn());
                  }

                  identityList.add(identity);
               }
            }
         }

         return identityList;
      }
   }
}
