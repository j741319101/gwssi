
package com.dstz.bpm.engine.action.handler.task;

import com.dstz.base.api.constant.IStatusCode;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.exception.WorkFlowException;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.def.NodeProperties;
import com.dstz.bpm.api.model.inst.BpmExecutionStack;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.bpm.engine.action.helper.NewThreadActionUtil;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("taskRejectActionHandler")
public class TaskRejectActionHandler
        extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    private static Logger log = LoggerFactory.getLogger(TaskRejectActionHandler.class);

    @Resource
    BpmTaskStackManager bpmTaskStackManager;

    @Resource
    BpmTaskOpinionManager taskOpinionManager;

    @Resource
    BpmProcessDefService processDefService;
    @Resource
    BpmTaskManager bpmTaskManager;

    public void doActionBefore(DefualtTaskActionCmd actionModel) {
        taskComplatePrePluginExecute(actionModel);

        NodeProperties nodeProperties = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties();

        BpmTask rejectTask = getPreDestination(actionModel, nodeProperties);


        if ("history".equals(nodeProperties.getBackUserMode())) {
            setHistoryApprover(actionModel, rejectTask.getNodeId());
        }

        BpmTask task = (BpmTask) actionModel.getBpmTask();
        BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(task.getDefId());
        List<IExtendTaskAction> taskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        for (IExtendTaskAction taskAction : taskActions) {
            if (taskAction.isContainNode(task.getNodeId(), bpmProcessDef, "end")) {
                throw new BusinessException("当前节点为多实例任务节点不支持驳回", (IStatusCode) BpmStatusCode.CANNOT_BACK_NODE);
            }
        }

        for (IExtendTaskAction taskAction : taskActions) {
            if (taskAction.isContainNode(rejectTask.getNodeId(), bpmProcessDef, "none")) {
                throw new BusinessException("目标节点为多实例任务节点不支持驳回", (IStatusCode) BpmStatusCode.CANNOT_BACK_NODE);
            }
        }

        for (IExtendTaskAction taskAction : taskActions) {
            if (!taskAction.canReject((IBpmTask) task, (IBpmTask) rejectTask)) {
                throw new BusinessException("动态多实例任务节点暂不支持驳回", (IStatusCode) BpmStatusCode.CANNOT_BACK_NODE);
            }
        }


        task.setPriority(Integer.valueOf(task.getPriority().intValue() + 1));
        actionModel.setDestination(rejectTask.getNodeId());
        log.info("任务【{}-{}】将驳回至节点{}", new Object[]{task.getName(), task.getId(), rejectTask.getNodeId()});


        handleInclusiveTask(rejectTask.getNodeId(), nodeProperties, actionModel);
    }


    private void setHistoryApprover(DefualtTaskActionCmd actionModel, String destinationNode) {
        List<SysIdentity> bpmIdentity = actionModel.getBpmIdentity(destinationNode);
        if (CollectionUtil.isNotEmpty(bpmIdentity)) {
            return;
        }
        DefaultIdentity object = null;

        List<BpmTaskOpinion> taskOpinions = this.taskOpinionManager.getByInstAndNode(actionModel.getInstanceId(), destinationNode);
        for (BpmTaskOpinion opinion : taskOpinions) {
            if (StringUtil.isNotEmpty(opinion.getApprover())) {
                object = new DefaultIdentity(opinion.getApprover(), opinion.getApproverName(), "user", opinion.getTaskOrgId());
            }
        }

        if (object != null) {
            List<SysIdentity> list = new ArrayList<>();
            list.add(object);
            actionModel.setBpmIdentity(destinationNode, list);
        }
    }


    private void handleInclusiveTask(String destinationNode, NodeProperties nodeProperties, DefualtTaskActionCmd actionModel) {
        if ("back".equals(nodeProperties.getBackMode())) {
            return;
        }

        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(actionModel.getDefId());
        JSONArray multInst = null;
        if (bpmProcessDef.getJson().getJSONObject("flow").getJSONObject("plugins").containsKey("multInst")) {
            multInst = bpmProcessDef.getJson().getJSONObject("flow").getJSONObject("plugins").getJSONArray("multInst");
        }

        BpmNodeDef destNodeDef = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), destinationNode);
        Map<String, Integer> nodeDepthMap = new HashMap<>();
        int dept = 0;
        handleNodeDepthMap(destNodeDef, nodeDepthMap, dept, multInst);

        BpmTask task = (BpmTask) actionModel.getBpmTask();

        int deptGap = ((Integer) nodeDepthMap.getOrDefault(task.getNodeId(), Integer.valueOf(0))).intValue() - ((Integer) nodeDepthMap.get(destinationNode)).intValue();

        if (deptGap > 0) {


            List<BpmTask> otherTasks = (List<BpmTask>) this.bpmTaskManager.getByParam(task.getActInstId(), null).stream().filter(t -> {
                if (task.getId().equals(t.getId())) return false;
                return (((Integer) nodeDepthMap.getOrDefault(t.getNodeId(), Integer.valueOf(0))).intValue() > ((Integer) nodeDepthMap.get(destinationNode)).intValue());
            }).collect(Collectors.toList());

            if (otherTasks.isEmpty()) {
                return;
            }

            JSONObject extendsConfig = actionModel.getExtendConf();
            if (extendsConfig == null || !extendsConfig.containsKey("confirmRecycle") || !extendsConfig.getBoolean("confirmRecycle").booleanValue()) {
                List<String> strs = new ArrayList<>();
                for (BpmTask t : otherTasks) {
                    strs.add("【" + t.getName() + "】候选人：" + t.getAssigneeNames());
                }

                throw new BusinessMessage("驳回节点【" + destNodeDef.getName() + "】需要回收以下任务：<br/>" + StrUtil.join("<br/>", new Object[]{strs}), (IStatusCode) BpmStatusCode.BPM_MULT_INST_CONFIRMR_ECYCLE);
            }


            for (BpmTask t : otherTasks) {
                this.bpmTaskManager.recycleTask(t.getId(), OpinionStatus.REJECT, "驳回回收同步任务");
                FlowRequestParam param = new FlowRequestParam(t.getId(), ActionType.AGREE.getKey(), new JSONObject(), "驳回回收同步任务");
                DefualtTaskActionCmd newFlowCmd = new DefualtTaskActionCmd(param);
                newFlowCmd.setIgnoreAuthentication(Boolean.valueOf(true));
                newFlowCmd.setDestinations(new String[0]);
                NewThreadActionUtil.newThreadDoAction((ActionCmd) newFlowCmd, ContextUtil.getCurrentUser());
            }


            List<BpmTaskOpinion> desOpinions = this.taskOpinionManager.getByInstAndNode(actionModel.getInstanceId(), destinationNode);
            if (CollUtil.isNotEmpty(desOpinions)) {
                BpmContext.pushMulInstOpTrace(((BpmTaskOpinion) desOpinions.get(0)).getTrace());
            }
        }
    }

    private void handleNodeDepthMap(BpmNodeDef node, Map<String, Integer> nodeDepthMap, int dept, JSONArray multInst) {
        if (dept == -1) {
            return;
        }

        if (nodeDepthMap.containsKey(node.getNodeId())) {
            return;
        }
        nodeDepthMap.put(node.getNodeId(), Integer.valueOf(dept));
        for (BpmNodeDef n : node.getOutcomeNodes()) {
            if (Arrays.asList(new NodeType[]{NodeType.PARALLELGATEWAY, NodeType.INCLUSIVEGATEWAY}).contains(n.getType())) {
                if (n.getOutcomeNodes().size() == 1) {
                    handleNodeDepthMap(n, nodeDepthMap, dept - 1, multInst);
                    continue;
                }
                handleNodeDepthMap(n, nodeDepthMap, dept + 1, multInst);
                continue;
            }
            int type = 0;
            for (int i = 0; i < multInst.size(); i++) {
                JSONObject jo = multInst.getJSONObject(i);
                if (jo.getString("startNodeKey").equals(node.getNodeId())) {
                    type = 1;
                }
                if (jo.getString("endNodeKey").equals(n.getNodeId())) {
                    type = -1;
                }
            }

            handleNodeDepthMap(n, nodeDepthMap, dept + type, multInst);
        }
    }


    protected BpmTask getPreDestination(DefualtTaskActionCmd actionModel, NodeProperties nodeProperties) {
        String destination = null;
        if (StringUtil.isNotEmpty(actionModel.getDestination())) {
            destination = actionModel.getDestination();
        }

        if (nodeProperties != null && nodeProperties.isFreeBack() &&
                StringUtil.isEmpty(destination)) {
            throw new BusinessException("退回任意环节需选择环节名称");
        }


        if (nodeProperties != null && StringUtil.isNotEmpty(nodeProperties.getBackNode()) && !nodeProperties.isFreeBack()) {
            destination = nodeProperties.getBackNode();
        }

        if (StringUtil.isEmpty(destination)) {

            BpmNodeDef nodeDef = this.processDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId());
            if (nodeDef.getIncomeNodes().size() == 1 && ((BpmNodeDef) nodeDef.getIncomeNodes().get(0)).getType() == NodeType.USERTASK) {
                destination = ((BpmNodeDef) nodeDef.getIncomeNodes().get(0)).getNodeId();
            }
        }

        DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
        defaultQueryFilter.addFilter("level", "1", QueryOP.GREAT);
        defaultQueryFilter.addParamsFilter("taskId", actionModel.getTaskId());
        defaultQueryFilter.addParamsFilter("prior", "BACK");
        defaultQueryFilter.addFilter("node_type_", "userTask", QueryOP.EQUAL);
        if (!StringUtils.isEmpty(destination)) {
            defaultQueryFilter.addFilter("node_id_", destination, QueryOP.EQUAL);
        }
        defaultQueryFilter.addFieldSort("level", "asc");
        defaultQueryFilter.addFieldSort("start_time_", "asc");
        List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.getTaskStackByIteration((QueryFilter) defaultQueryFilter);
        if (CollectionUtil.isEmpty(bpmTaskStacks)) {
            throw new BusinessException("未查到执行记录，检查执行记录和流程定义节点定义");
        }
        BpmTaskStack bpmTaskStack = bpmTaskStacks.get(0);
        BpmTask bpmTask = new BpmTask();
        bpmTask.setId(bpmTaskStack.getTaskId());
        bpmTask.setNodeId(bpmTaskStack.getNodeId());
        bpmTask.setInstId(bpmTaskStack.getInstId());
        return bpmTask;
    }


    BpmExecutionStack getPreTaskStack(List<BpmTaskStack> stackList, String id) {
        String parentId = id;
        for (int i = stackList.size() - 1; i > -1; i--) {
            BpmTaskStack stack = stackList.get(i);

            if (stack.getId().equals(parentId)) {
                parentId = stack.getParentId();
                if ("userTask".equals(stack.getNodeType())) {
                    return (BpmExecutionStack) stack;
                }
            }
        }
        return null;
    }


    public void doActionAfter(DefualtTaskActionCmd actionModel) {
        NodeProperties nodeProperties = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties();

        if ("back".equals(nodeProperties.getBackMode())) {
            List<BpmTask> tasks = this.taskManager.getByInstIdNodeId(actionModel.getInstanceId(), actionModel.getDestination());
            if (CollectionUtil.isEmpty(tasks)) {
                throw new WorkFlowException(String.format("任务[%s]返回节点[%s]标记失败，待标记任务查找不到", new Object[]{actionModel.getDestination(), actionModel.getNodeId()}), (IStatusCode) BpmStatusCode.NO_BACK_TARGET);
            }
            boolean hasUpdated = false;
            for (BpmTask task : tasks) {
                if (StringUtil.isEmpty(task.getBackNode()) && StringUtil.isNotEmpty(task.getActInstId()) && StringUtil.isNotEmpty(task.getTaskId())) {
                    if (hasUpdated) {
                        throw new WorkFlowException("任务返回节点标记失败，期望查找一条，但是出现多条", (IStatusCode) BpmStatusCode.NO_BACK_TARGET);
                    }

                    task.setBackNode(actionModel.getNodeId());
                    this.taskManager.update(task);
                    hasUpdated = true;
                }
            }
            if (!hasUpdated) {
                throw new WorkFlowException("任务返回节点标记失败，待标记任务查找不到", (IStatusCode) BpmStatusCode.NO_BACK_TARGET);
            }
        }
    }


    protected void taskComplatePrePluginExecute(DefualtTaskActionCmd actionModel) {
    }


    public ActionType getActionType() {
        return ActionType.REJECT;
    }


    public int getSn() {
        return 3;
    }


    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }
}
