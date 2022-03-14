package com.dstz.bpm.plugin.global.taskskip.executer;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.TaskSkipType;
import com.dstz.bpm.api.engine.action.cmd.TaskActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TaskSkipPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, TaskSkipPluginDef> {
   @Resource
   IGroovyScriptEngine scriptEngine;
   @Resource
   BpmProcessDefService processDefService;

   public Void execute(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      if (model.isHasSkipThisTask() == TaskSkipType.NO_SKIP) {
         TaskSkipType isSkip = this.getSkipResult(pluginSession, pluginDef);
         model.setHasSkipThisTask(isSkip);
      }

      return null;
   }

   private TaskSkipType getSkipResult(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
      TaskActionCmd model = (TaskActionCmd)BpmContext.getActionModel();
      TaskSkipType skipResult = TaskSkipType.NO_SKIP;
      if (StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
         return skipResult;
      } else {
         String[] skip = pluginDef.getSkipTypeArr().split(",");
         String[] var6 = skip;
         int var7 = skip.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String typeStr = var6[var8];
            TaskSkipType type = TaskSkipType.fromKey(typeStr);
            switch(type) {
            case ALL_SKIP:
               skipResult = TaskSkipType.ALL_SKIP;
               break;
            case SCRIPT_SKIP:
               if (!StringUtils.isEmpty(pluginDef.getScript())) {
                  boolean isSkip = this.scriptEngine.executeBoolean(pluginDef.getScript(), pluginSession);
                  if (isSkip) {
                     skipResult = TaskSkipType.SCRIPT_SKIP;
                  }
               }
               break;
            case SAME_USER_SKIP:
               List<SysIdentity> identityList = model.getBpmIdentity(model.getBpmTask().getNodeId());
               if (CollectionUtil.isNotEmpty(identityList)) {
                  String userId = ContextUtil.getCurrentUserId();
                  SysIdentity identity = (SysIdentity)identityList.get(0);
                  if (identity.getId().equals(userId)) {
                     skipResult = TaskSkipType.SAME_USER_SKIP;
                  }
               }
               break;
            case USER_EMPTY_SKIP:
               List<SysIdentity> identityList1 = model.getBpmIdentity(model.getBpmTask().getNodeId());
               if (CollectionUtil.isEmpty(identityList1)) {
                  skipResult = TaskSkipType.USER_EMPTY_SKIP;
               }
               break;
            case FIRSTNODE_SKIP:
               if (ActionType.START.getKey().equals(BpmContext.submitActionModel().getActionName())) {
                  List<BpmNodeDef> list = this.processDefService.getStartNodes(model.getBpmTask().getDefId());
                  Iterator var14 = list.iterator();

                  while(var14.hasNext()) {
                     BpmNodeDef def = (BpmNodeDef)var14.next();
                     if (def.getNodeId().equals(model.getBpmTask().getNodeId())) {
                        skipResult = TaskSkipType.FIRSTNODE_SKIP;
                        break;
                     }
                  }
               }
            }

            if (skipResult != TaskSkipType.NO_SKIP) {
               this.LOG.info("{}节点【{}】设置了【{}】，将跳过当前任务", new Object[]{model.getBpmTask().getId(), model.getBpmTask().getName(), skipResult.getValue()});
               return skipResult;
            }
         }

         return skipResult;
      }
   }
}
