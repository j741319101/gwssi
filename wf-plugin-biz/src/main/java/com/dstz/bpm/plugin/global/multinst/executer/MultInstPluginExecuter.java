package com.dstz.bpm.plugin.global.multinst.executer;

import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.helper.NewThreadActionUtil;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.BpmExecutionPluginSession;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.global.multinst.def.MultInst;
import com.dstz.bpm.plugin.global.multinst.def.MultInstPluginDef;
import com.dstz.bpm.plugin.usercalc.util.UserCalcPreview;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ToStringUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultInstPluginExecuter extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, MultInstPluginDef> {
   @Autowired
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Autowired
   private BpmProcessDefService bpmProcessDefService;
   @Autowired
   private BpmTaskManager bpmTaskManager;
   @Autowired
   private IGroovyScriptEngine groovyScriptEngine;
   private static final String recycleOpinion = "回收未完成的分发任务";

   public Void execute(BpmExecutionPluginSession session, MultInstPluginDef pluginDef) {
      if (session instanceof DefaultBpmTaskPluginSession && !StringUtils.equals(String.valueOf(session.get("submitActionName")), ActionType.REJECT.getKey()) && !StringUtils.equals(String.valueOf(session.get("submitActionName")), ActionType.REJECT2START.getKey())) {
         DefaultBpmTaskPluginSession pluginSession = (DefaultBpmTaskPluginSession)session;
         if (pluginSession.getEventType() == EventType.TASK_PRE_COMPLETE_EVENT) {
            this.startMulInst(pluginSession, pluginDef);
            this.endMulInst(pluginSession, pluginDef);
            this.checkIsAllComplated(pluginSession, pluginDef);
         }

         if (pluginSession.getEventType() == EventType.TASK_CREATE_EVENT) {
            this.setIdentity(pluginSession, pluginDef);
         }

         if (pluginSession.getEventType() == EventType.TASK_POST_CREATE_EVENT) {
            this.executeScript(pluginSession, pluginDef);
         }

         return null;
      } else {
         return null;
      }
   }

   private void startMulInst(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      boolean isStartNode = false;
      IBpmTask bpmTask = pluginSession.getBpmTask();
      MultInst currentMultInst = null;
      Iterator var7 = pluginDef.getMultInsts().iterator();

      while(var7.hasNext()) {
         MultInst mi = (MultInst)var7.next();
         if (bpmTask.getNodeId().equals(mi.getStartNodeKey())) {
            isStartNode = true;
            currentMultInst = mi;
         }
      }

      if (isStartNode) {
         BpmNodeDef nextNode = null;
         BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
         List<String> destinations = new ArrayList();
         String[] destinationNodes = model.getDestinations();
         if (destinationNodes != null) {
            if (destinationNodes.length > 1) {
               throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置");
            }

            nextNode = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), destinationNodes[0]);
         }

         if (nextNode == null) {
            if (bpmNodeDef.getOutcomeNodes().size() != 1) {
               throw new BusinessException("多实例任务后续任务分支只能为一个，请重新配置流程定义");
            }

            nextNode = (BpmNodeDef)bpmNodeDef.getOutcomeNodes().get(0);
         }

         Set<String> existNode = new HashSet();
         existNode.add(bpmTask.getNodeId());
         if (this.isEndNode(nextNode, currentMultInst, existNode)) {
            List<SysIdentity> identities = model.getBpmIdentity(nextNode.getNodeId());
            if (CollectionUtil.isEmpty(identities)) {
               identities = UserCalcPreview.calcNodeUsers(nextNode, model);
            }

            if (identities.isEmpty()) {
               throw new BusinessException("任务【" + nextNode.getName() + "】预计算候选人为空，无法生成多实例任务");
            } else if (identities.size() == 1) {
               this.LOG.debug("节点【{}】为分发节点，候选人为一个不需开启多实例", bpmTask.getName());
            } else {
               this.LOG.debug("节点【{}】为分发节点，将会产生【{}】个分发任务", bpmTask.getName(), identities.size());
               BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());

               for(int i = 0; i < identities.size(); ++i) {
                  SysIdentity id = (SysIdentity)identities.get(i);
                  destinations.add(nextNode.getNodeId());
                  BpmContext.pushMulInstIdentities(id);
                  if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
                     BpmContext.pushMulInstOpTrace("t-" + i);
                  } else {
                     BpmContext.pushMulInstOpTrace(bpmTaskOpinion.getTrace() + "-" + i);
                  }
               }

               model.setDestinations((String[])destinations.toArray(new String[destinations.size()]));
            }
         }
      }
   }

   private void setIdentity(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      IBpmTask bpmTask = pluginSession.getBpmTask();
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      if (bpmNodeDef.getIncomeNodes().size() == 1) {
         boolean isMulInstTask = false;
         MultInst currentMultInst = null;
         List<BpmNodeDef> preNodes = bpmNodeDef.getIncomeTaskNodes();
         Iterator var9 = pluginDef.getMultInsts().iterator();

         while(var9.hasNext()) {
            MultInst mi = (MultInst)var9.next();
            Iterator var11 = preNodes.iterator();

            while(var11.hasNext()) {
               BpmNodeDef nodeDef = (BpmNodeDef)var11.next();
               if (mi.getStartNodeKey().equals(nodeDef.getNodeId())) {
                  isMulInstTask = true;
                  currentMultInst = mi;
               }
            }
         }

         if (isMulInstTask) {
            Set<String> existNode = new HashSet();
            if (this.isEndNode(bpmNodeDef, currentMultInst, existNode)) {
               SysIdentity identity = BpmContext.popMulInstIdentities();
               if (identity != null) {
                  this.LOG.debug("任务【ID：{}】为分发任务，将从分发候选人的栈中取出候选人：{}", bpmTask.getId(), ToStringUtil.toString(identity));
                  model.setBpmIdentity(model.getBpmTask().getNodeId(), CollUtil.newArrayList(new SysIdentity[]{identity}));
               }
            }
         }
      }
   }

   private void endMulInst(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
      String currentRecoveryStrategy = "completed";
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      boolean isEndNode = false;
      IBpmTask bpmTask = pluginSession.getBpmTask();
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      String[] destinationNodes = model.getDestinations();
      String destination = null;
      if (destinationNodes != null) {
         Iterator var10 = pluginDef.getMultInsts().iterator();

         while(var10.hasNext()) {
            MultInst mi = (MultInst)var10.next();
            if (StringUtils.equals(destinationNodes[0], mi.getEndNodeKey())) {
               isEndNode = true;
               destination = destinationNodes[0];
            }
         }
      } else {
         if (bpmNodeDef.getOutcomeNodes().size() != 1) {
            return;
         }

         BpmNodeDef nextNode = (BpmNodeDef)bpmNodeDef.getOutcomeNodes().get(0);
         destination = nextNode.getNodeId();
         Iterator var17 = pluginDef.getMultInsts().iterator();

         while(var17.hasNext()) {
            MultInst mi = (MultInst)var17.next();
            if (mi.getEndNodeKey().equals(nextNode.getNodeId())) {
               isEndNode = true;
               currentRecoveryStrategy = mi.getRecoveryStrategy();
            }
         }
      }

      if (isEndNode) {
         if (!BpmContext.isMulInstOpTraceEmpty()) {
            BpmContext.clearMulInstOpTrace();
            model.setDestination(destination);
         }

         BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
         List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
         BpmTaskOpinion bpmTaskOpinion;
         if (StringUtil.isEmpty(opinion.getTrace())) {
            Iterator var20 = opinions.iterator();

            while(var20.hasNext()) {
               bpmTaskOpinion = (BpmTaskOpinion)var20.next();
               if (bpmTaskOpinion.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && bpmTaskOpinion.getTaskKey().equals(destination)) {
                  model.setDestinations(new String[0]);
               }
            }

         } else {
            boolean b = false;
            Iterator var13 = opinions.iterator();

            while(var13.hasNext()) {
               BpmTaskOpinion o = (BpmTaskOpinion)var13.next();
               if (!o.getId().equals(opinion.getId()) && o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()) && o.getTrace() != null && o.getTrace().length() == opinion.getTrace().length() && !"所有会签用户".equals(o.getAssignInfo()) && o.getTrace().startsWith(opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-")))) {
                  b = true;
                  break;
               }
            }

            if ("autonomously".equals(currentRecoveryStrategy)) {
               if (this.checkIsFirstComplated(opinions, opinion, destination)) {
                  if ("回收未完成的分发任务".equals(model.getOpinion())) {
                     model.setDestinations(new String[0]);
                     return;
                  }

                  if (StringUtil.isEmpty(opinion.getTrace())) {
                     return;
                  }

                  String trace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
                  BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
                  return;
               }
            } else if (!b) {
               bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getTaskId());
               if (StringUtil.isEmpty(bpmTaskOpinion.getTrace())) {
                  return;
               }

               String trace = bpmTaskOpinion.getTrace().substring(0, bpmTaskOpinion.getTrace().lastIndexOf("-"));
               BpmContext.pushMulInstOpTrace("t".equals(trace) ? "" : trace);
               return;
            }

            model.setDestinations(new String[0]);
         }
      }
   }

   private boolean checkIsFirstComplated(List<BpmTaskOpinion> opinions, BpmTaskOpinion opinion, String nextNode) {
      String parentTrace = opinion.getTrace().substring(0, opinion.getTrace().lastIndexOf("-"));
      Iterator var5 = opinions.iterator();

      BpmTaskOpinion o;
      do {
         do {
            do {
               if (!var5.hasNext()) {
                  return true;
               }

               o = (BpmTaskOpinion)var5.next();
            } while(!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()));
         } while(!StringUtil.isEmpty(o.getTrace()) && !parentTrace.equals(o.getTrace()));
      } while(!nextNode.equals(o.getTaskKey()));

      return false;
   }

   public static void main(String[] args) {
      System.out.println("t-1-2-3".substring(0, "t-1-2-3".lastIndexOf("-")));
   }

   private boolean isEndNode(BpmNodeDef nextNodeDef, MultInst currentMultInst, Set<String> existNode) {
      boolean lastIsEndNode = true;
      if (StringUtils.equals(nextNodeDef.getNodeId(), currentMultInst.getEndNodeKey())) {
         return true;
      } else {
         List<BpmNodeDef> nodeDefs = nextNodeDef.getOutcomeNodes();
         Iterator var6 = nodeDefs.iterator();

         while(var6.hasNext()) {
            BpmNodeDef bpmNodeDef = (BpmNodeDef)var6.next();
            if (existNode.contains(bpmNodeDef.getNodeId()) || StringUtils.equals(bpmNodeDef.getType().getKey(), "EndNoneEvent")) {
               return false;
            }

            existNode.add(nextNodeDef.getNodeId());
            if (!this.isEndNode(bpmNodeDef, currentMultInst, existNode)) {
               lastIsEndNode = false;
            }
         }

         return lastIsEndNode;
      }
   }

   private void checkIsAllComplated(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
      DefualtTaskActionCmd model = (DefualtTaskActionCmd)BpmContext.getActionModel();
      IBpmTask bpmTask = pluginSession.getBpmTask();
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      if (bpmNodeDef.getOutcomeNodes().size() == 1) {
         Boolean isAutonomously = false;
         Iterator var7 = pluginDef.getMultInsts().iterator();

         while(var7.hasNext()) {
            MultInst mi = (MultInst)var7.next();
            if (mi.getEndNodeKey().equals(bpmTask.getNodeId()) && "autonomously".equals(mi.getRecoveryStrategy())) {
               isAutonomously = true;
            }
         }

         if (isAutonomously) {
            BpmTaskOpinion currentOpinion = this.bpmTaskOpinionManager.getByTaskId(bpmTask.getId());
            List<BpmTaskOpinion> opinions = this.bpmTaskOpinionManager.getByInstId(bpmTask.getInstId());
            List<BpmTaskOpinion> hasNotComplated = new ArrayList();
            StringBuilder hasNotComplatedTask = new StringBuilder();
            Iterator var11 = opinions.iterator();

            while(true) {
               BpmTaskOpinion o;
               do {
                  do {
                     do {
                        do {
                           if (!var11.hasNext()) {
                              if (hasNotComplated.isEmpty()) {
                                 return;
                              }

                              JSONObject extendsConfig = model.getExtendConf();
                              if (extendsConfig != null && extendsConfig.containsKey("confirmRecycle") && extendsConfig.getBoolean("confirmRecycle")) {
                                 this.recycleTasks(hasNotComplated);
                                 return;
                              }

                              hasNotComplatedTask.insert(0, "请确认，是否要回收以下未完成的分发任务:");
                              throw new BusinessMessage(hasNotComplatedTask.toString(), BpmStatusCode.BPM_MULT_INST_CONFIRMR_ECYCLE);
                           }

                           o = (BpmTaskOpinion)var11.next();
                        } while(!o.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey()));
                     } while(StringUtil.isEmpty(o.getTrace()));
                  } while(currentOpinion.getId().equals(o.getId()));
               } while(!StringUtil.isEmpty(currentOpinion.getTrace()) && !o.getTrace().startsWith(currentOpinion.getTrace()));

               hasNotComplated.add(o);
               BpmTask task = (BpmTask)this.bpmTaskManager.get(o.getTaskId());
               hasNotComplatedTask.append("<br/>【").append(o.getTaskName()).append("】候选人:").append(task.getAssigneeNames());
            }
         }
      }
   }

   private void recycleTasks(List<BpmTaskOpinion> hasNotComplated) {
      Iterator var2 = hasNotComplated.iterator();

      while(var2.hasNext()) {
         BpmTaskOpinion o = (BpmTaskOpinion)var2.next();
         FlowRequestParam param = new FlowRequestParam(o.getTaskId(), ActionType.AGREE.getKey(), new JSONObject(), "回收未完成的分发任务");
         DefualtTaskActionCmd newFlowCmd = new DefualtTaskActionCmd(param);
         newFlowCmd.setIgnoreAuthentication(true);
         newFlowCmd.setDestinations(new String[0]);
         NewThreadActionUtil.newThreadDoAction(newFlowCmd, ContextUtil.getCurrentUser());
      }

   }

   private void executeScript(DefaultBpmTaskPluginSession pluginSession, MultInstPluginDef pluginDef) {
      IBpmTask bpmTask = pluginSession.getBpmTask();
      BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId());
      if (bpmNodeDef.getIncomeNodes().size() == 1) {
         List<BpmNodeDef> preNodes = bpmNodeDef.getIncomeTaskNodes();
         Iterator var6 = pluginDef.getMultInsts().iterator();

         while(var6.hasNext()) {
            MultInst mi = (MultInst)var6.next();
            Iterator var8 = preNodes.iterator();

            while(var8.hasNext()) {
               BpmNodeDef nodeDef = (BpmNodeDef)var8.next();
               if (mi.getStartNodeKey().equals(nodeDef.getNodeId()) && StringUtil.isNotEmpty(mi.getScript())) {
                  this.groovyScriptEngine.execute(mi.getScript());
               }
            }
         }

      }
   }
}
