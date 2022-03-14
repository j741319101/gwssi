package com.dstz.bpm.plugin.global.datalog.executer;

import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.plugin.core.manager.BpmSubmitDataLogManager;
import com.dstz.bpm.plugin.core.model.BpmSubmitDataLog;
import com.dstz.bpm.plugin.global.datalog.def.DataLogPluginDef;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;

import com.dstz.bus.api.service.IBusinessDataService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class DataLogPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, DataLogPluginDef> {
   @Resource
   BpmProcessDefService processDefService;
   @Resource
   private ThreadPoolTaskExecutor threadPoolTaskExecutor;
   @Resource
   private IBusinessDataService businessDataService;
   @Resource
   BpmSubmitDataLogManager bpmSubmitDataLogManager;

   public Void execute(BpmExecutionPluginSession pluginSession, DataLogPluginDef pluginDef) {
      BaseActionCmd cmd = (BaseActionCmd)BpmContext.getActionModel();
      JSONObject businessData = cmd.getBusData();
      if (businessData != null && !businessData.isEmpty()) {
         String defId = pluginSession.getBpmInstance().getDefId();
         DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.processDefService.getBpmProcessDef(defId);
         if (!processDef.getExtProperties().isLogSubmitData()) {
            return null;
         } else {
            BpmSubmitDataLog submitDataLog = new BpmSubmitDataLog();
            submitDataLog.setData(businessData.toJSONString());
            submitDataLog.setDestination(cmd.getDestination());
            if (cmd.getExtendConf() != null) {
               submitDataLog.setExtendConf(cmd.getExtendConf().toJSONString());
            }

            if (cmd instanceof DefualtTaskActionCmd) {
               submitDataLog.setTaskId(((DefualtTaskActionCmd)cmd).getBpmTask().getId());
            }

            submitDataLog.setInstId(cmd.getInstanceId());
            this.threadPoolTaskExecutor.execute(() -> {
               this.bpmSubmitDataLogManager.create(submitDataLog);
            });
            this.LOG.debug("记录流程提交业务数据 ");
            return null;
         }
      } else {
         return null;
      }
   }
}
