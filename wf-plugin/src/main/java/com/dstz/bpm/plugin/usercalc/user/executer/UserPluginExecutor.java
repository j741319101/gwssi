package com.dstz.bpm.plugin.usercalc.user.executer;

import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import com.dstz.bpm.plugin.usercalc.user.def.UserPluginDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.GroupDTO;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserPluginExecutor extends AbstractUserCalcPlugin<UserPluginDef> {
   @Resource
   BpmTaskOpinionManager taskOpinionManager;
   @Resource
   private UserService userService;
   @Resource
   BpmProcessDefService bpmProcessDefService;

   @Resource
   GroupService groupService;

   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, UserPluginDef def) {
      List<SysIdentity> list = new ArrayList();
      String source = def.getSource();
      String actionName = (String)pluginSession.get("actionName");
      if (StringUtil.isNotEmpty(actionName) && StringUtils.equals(actionName, "start") && StringUtils.equals(source, "start")) {
         source = "currentUser";
      }

      if ("start".equals(source)) {
         IBpmInstance instance = pluginSession.getBpmInstance();
         BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
         String startNodeId = processDef.getStartEvent().getNodeId();
         List<BpmTaskOpinion> opinions = this.taskOpinionManager.getByInstAndNode(instance.getId(), startNodeId);
         if (CollectionUtil.isEmpty(opinions)) {
            if (StringUtil.isEmpty(instance.getCreateBy())) {
               throw new BusinessException("流程数据异常，无法获取发起人！", BpmStatusCode.USER_CALC_ERROR);
            }

            SysIdentity bpmIdentity = new DefaultIdentity(instance.getCreateBy(), instance.getCreator(), "user", instance.getCreateOrgId());
            list.add(bpmIdentity);
         } else {
            BpmTaskOpinion firstNode = (BpmTaskOpinion)opinions.get(0);
            SysIdentity bpmIdentity = new DefaultIdentity(firstNode.getApprover(), firstNode.getApproverName(), "user", firstNode.getTaskOrgId());
            list.add(bpmIdentity);
         }
      }

      if ("currentUser".equals(source)) {
         SysIdentity bpmIdentity = new DefaultIdentity(ContextUtil.getCurrentUser());
         bpmIdentity.setOrgId(((BaseActionCmd)BpmContext.getActionModel()).getApproveOrgId());
         list.add(bpmIdentity);
      } else if ("spec".equals(source)) {
         String userKeys = def.getAccount();
         String[] aryInfo = userKeys.split(",");
         String[] var18 = aryInfo;
         int var19 = aryInfo.length;

         for(int var21 = 0; var21 < var19; ++var21) {
            String info = var18[var21];
            String[] userInfo = info.split("-");
            IUser user = this.userService.getUserByAccount(userInfo[0]);
            if (user == null) {
               throw new BusinessException(userInfo[0] + "用户丢失", BpmStatusCode.USER_CALC_ERROR);
            }

            SysIdentity bpmIdentity = new DefaultIdentity(user);
            if(StringUtils.isEmpty(userInfo[1])){ // todo 获取用户机构id 先测试
                GroupDTO group = (GroupDTO)groupService.getMainGroup(user.getUserId());
                if (group != null){
                    bpmIdentity.setOrgId(group.getGroupId());
                }else {
                    bpmIdentity.setOrgId("1");
                }
            }else {
                bpmIdentity.setOrgId(userInfo[1]);
            }
            list.add(bpmIdentity);
         }
      }

      return list;
   }
}
