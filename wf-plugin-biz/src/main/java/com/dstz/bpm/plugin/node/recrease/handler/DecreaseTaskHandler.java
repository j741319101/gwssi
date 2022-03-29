package com.dstz.bpm.plugin.node.recrease.handler;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.plugin.node.dynamictask.context.DynamicTaskPluginContext;
import com.dstz.bpm.plugin.node.dynamictask.def.DynamicTaskPluginDef;
import com.dstz.bpm.plugin.node.sign.context.SignTaskPluginContext;
import com.dstz.bpm.plugin.node.sign.def.SignTaskPluginDef;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DecreaseTaskHandler implements BuiltinActionHandler<DefualtTaskActionCmd> {
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmProcessDefService bpmProcessDefService;
   @Resource
   private RecreaseSignTaskExecuter recreaseSignTaskExecuter;
   @Resource
   private RecreaseDynamicTaskExecuter recreaseDynamicTaskExecuter;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;
   @Resource
   private RecreaseTaskAction recreaseTaskAction;

   @Transactional
   public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
      if (bpmInstance != null && bpmTask != null && !StringUtils.isEmpty(bpmInstance.getId()) && !StringUtils.isEmpty(bpmTask.getId())) {
         JSONObject result = this.recreaseTaskAction.getExistAliveTask(bpmInstance, bpmTask);
         return result.getBoolean("existAliveTask");
      } else {
         return false;
      }
   }

   public void execute(DefualtTaskActionCmd model) {
      JSONObject extendConf = model.getExtendConf();
      JSONObject dynamicNodes = extendConf.getJSONObject("dynamicNodes");
      String taskId = model.getTaskId();
      String instId = model.getInstanceId();
      BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(instId);
      model.setBpmInstance(bpmInstance);
      if (dynamicNodes != null && !dynamicNodes.isEmpty()) {
         Iterator var7 = dynamicNodes.keySet().iterator();

         while(var7.hasNext()) {
            String dynamicNodeId = (String)var7.next();
            DefualtTaskActionCmd execCmd = new DefualtTaskActionCmd();
            CopyOptions copyOptions = new CopyOptions();
            copyOptions.setIgnoreError(true);
            BeanUtil.copyProperties(model, execCmd, copyOptions);
            BpmContext.cleanTread();
            String callBackId = dynamicNodes.getString(dynamicNodeId);
            String nodeId = dynamicNodeId;
            if (StringUtils.indexOf(dynamicNodeId, "&") > -1) {
               String[] pgIds = dynamicNodeId.split("&");
               nodeId = pgIds[0];
            }

            extendConf.put("callBackId", callBackId);
            BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmInstance.getDefId(), nodeId);
            BpmNodeDef parentNodeDef = bpmNodeDef.getParentBpmNodeDef();
            SignTaskPluginDef signTaskPluginDef = (SignTaskPluginDef)((SignTaskPluginContext)bpmNodeDef.getPluginContext(SignTaskPluginContext.class)).getBpmPluginDef();
            DynamicTaskPluginDef dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)bpmNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
            if (parentNodeDef != null && parentNodeDef instanceof SubProcessNodeDef) {
               dynamicTaskPluginDef = (DynamicTaskPluginDef)((DynamicTaskPluginContext)parentNodeDef.getPluginContext(DynamicTaskPluginContext.class)).getBpmPluginDef();
            }

            if (signTaskPluginDef.isSignMultiTask()) {
               QueryFilter queryFilter = new DefaultQueryFilter(true);
               queryFilter.addFilter("level", 3, QueryOP.EQUAL);
               queryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
               queryFilter.addParamsFilter("taskId", taskId);
               queryFilter.addParamsFilter("prior", "FORWARD");
               queryFilter.addFieldSort("level", "asc");
               List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration(queryFilter);
               execCmd.setTaskId(((BpmTaskStack)bpmTaskStacks.get(0)).getTaskId());
               this.recreaseSignTaskExecuter.decrease(execCmd);
            } else if (dynamicTaskPluginDef.getEnabled()) {
               this.recreaseDynamicTaskExecuter.decrease(execCmd);
            }
         }
      }

   }

   public ActionType getActionType() {
      return ActionType.DECREASEDYNAMIC;
   }

   public int getSn() {
      return 1;
   }

   public Boolean isSupport(BpmNodeDef nodeDef) {
      return false;
   }

   public Boolean isDefault() {
      return true;
   }

   public String getConfigPage() {
      return null;
   }

   public String getDefaultGroovyScript() {
      return null;
   }

   public String getDefaultBeforeScript() {
      return null;
   }
}
