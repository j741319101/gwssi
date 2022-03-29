package com.dstz.bpm.core.manager.impl;
import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.constant.TaskType;
import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.dao.BpmTaskDao;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.core.vo.BpmTaskVO;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
@Service("bpmTaskManager")
public class BpmTaskManagerImpl  extends BaseManager<String, BpmTask> implements BpmTaskManager {
    @Resource
    BpmTaskDao bpmTaskDao;
    @Resource
    TaskIdentityLinkManager taskIdentityLinkManager;
    @Resource
    BpmInstanceManager instanceManager;
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;
    public List<BpmTask> getByInstIdNodeId(String instId, String nodeId) {
        return this.bpmTaskDao.getByInstIdNodeId(instId, nodeId);
    }
    public List<BpmTask> getSignTaskBySignSourceId(String taskId) {
        return this.bpmTaskDao.getSignTaskBySignSourceId(taskId);
    }
    public List<BpmTask> getByInstId(String instId) {
        return this.bpmTaskDao.getByInstIdNodeId(instId, null);
    }
    public void removeByInstId(String instId) {
        this.bpmTaskDao.removeByInstId(instId);
    }
    public List<BpmTaskVO> getTodoList(String userId, QueryFilter queryFilter) {
        Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
        queryFilter.addParamsFilter("userRights", userRights);
        queryFilter.addParamsFilter("userId", userId);
        List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        for (IExtendTaskAction extendTaskAction : extendTaskActions) {
            extendTaskAction.addQueryDodoTaskParams(queryFilter, "task");
        }
        return this.bpmTaskDao.getTodoList(queryFilter);
    }
    public List getTodoList(QueryFilter queryFilter) {
        String userId = ContextUtil.getCurrentUserId();
        String type = (String) queryFilter.getParams().get("type");
        String title = (String) queryFilter.getParams().get("subject");
        if (StringUtil.isNotEmpty(title)) {
            queryFilter.addFilter("subject_", title, QueryOP.LIKE);
        }
        if ("done".equals(type)) {
            return this.instanceManager.getApproveHistoryList(userId, queryFilter);
        }
        Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
        queryFilter.addParamsFilter("userRights", userRights);
        queryFilter.addParamsFilter("userId", userId);
        List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        for (IExtendTaskAction extendTaskAction : extendTaskActions) {
            extendTaskAction.addQueryDodoTaskParams(queryFilter, "task");
        }
        return this.bpmTaskDao.getTodoList(queryFilter);
    }
    public void assigneeTask(String taskId, List<SysIdentity> sysIdentities) {
        BpmTask task = (BpmTask) get(taskId);
        if (task == null) {
            throw new BusinessException("任务可能已经被处理，请刷新。", BpmStatusCode.TASK_NOT_FOUND);
        }
        if (CollectionUtil.isEmpty(sysIdentities)) {
            throw new BusinessException("候选人为空");
        }
        if (sysIdentities.size() > 1) {
            task.setAssigneeId("0");
            StringBuffer stringBuffer = new StringBuffer();
            sysIdentities.forEach(sysIdentity -> stringBuffer.append(sysIdentity.getName()).append(","));
            task.setAssigneeNames(stringBuffer.substring(0, stringBuffer.length() - 1));
        } else {
            SysIdentity sysIdentity = sysIdentities.get(0);
            task.setAssigneeId(sysIdentity.getId());
            task.setAssigneeNames(sysIdentity.getName());
        }
        task.setStatus(TaskStatus.DESIGNATE.getKey());
        update(task);
        this.taskIdentityLinkManager.removeByTaskId(task.getId());
        this.taskIdentityLinkManager.createIdentityLink((IBpmTask) task, sysIdentities);
        BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
        StringBuffer sb = new StringBuffer();
        sysIdentities.forEach(sysIdentity -> sb.append("user").append("-").append(sysIdentity.getName()).append("-").append(sysIdentity.getId()).append("-").append(sysIdentity.getOrgId()).append(","));//todo 添加org-id
        taskOpinion.setAssignInfo(sb.toString());
        this.bpmTaskOpinionManager.update(taskOpinion);
    }
    public void unLockTask(String taskId) {
        BpmTask task = (BpmTask) get(taskId);
        List<TaskIdentityLink> identitys = this.taskIdentityLinkManager.getByTaskId(task.getId());
        if (CollectionUtil.isEmpty(identitys)) {
            throw new BusinessMessage("该任务并非多候选人状态，无效的操作！");
        }
        StringBuilder nameSb = new StringBuilder();
        for (TaskIdentityLink identity : identitys) {
            if (nameSb.length() > 0) {
                nameSb.append(";");
            }
            nameSb.append(identity.getIdentityName());
        }
        task.setAssigneeId("0");
        task.setAssigneeNames(nameSb.toString());
        task.setStatus(TaskStatus.NORMAL.getKey());
        update(task);
        StringBuilder assignInfo = new StringBuilder();
        if (CollectionUtil.isNotEmpty(identitys)) {
            for (TaskIdentityLink identity : identitys) {
                assignInfo.append(identity.getType()).append("-").append(identity.getIdentityName())
                        .append("-").append(identity.getIdentity())
                        .append("-").append(identity.getOrgId()).append(",");
            }
        }
        BpmTaskOpinion taskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskId);
        taskOpinion.setAssignInfo(assignInfo.toString());
        this.bpmTaskOpinionManager.update(taskOpinion);
    }
    public List<SysIdentity> getAssignUserById(BpmTask task) {
        List<SysIdentity> identityList = new ArrayList<>();
        List<TaskIdentityLink> identitys = this.taskIdentityLinkManager.getByTaskId(task.getId());
        for (TaskIdentityLink link : identitys) {
            identityList.add(new DefaultIdentity(link.getIdentity(), link.getIdentityName(), link.getType()));
        }
        return identityList;
    }
    public List<BpmTask> getByParentId(String parentId) {
        return this.bpmTaskDao.getByParentId(parentId);
    }
    public List<BpmTypeTreeCountVO> todoTaskListTypeCount(String userId, QueryFilter queryFilter) {
        queryFilter.setPage(null);
        Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
        queryFilter.addParamsFilter("userRights", userRights);
        queryFilter.addParamsFilter("userId", ContextUtil.getCurrentUserId());
        return this.bpmTaskDao.getTodoListTypeCount(queryFilter);
    }
    public Set<String> selectTaskIdByInstId(String instId) {
        return this.bpmTaskDao.selectTaskIdByInstId(instId);
    }
    public Map getMyTaskNum() {
        String userId = ContextUtil.getCurrentUserId();
        Set<String> userRights = this.taskIdentityLinkManager.getUserRights(userId);
        return this.bpmTaskDao.getMyTaskNum(userId, userRights);
    }
    public List<Map> getUnionOrder() {
        return this.bpmTaskDao.getUnionOrder();
    }
    public List<BpmTask> getByParam(String actInstId, String actExecutionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("actInstId", actInstId);
        map.put("actExecutionId", actExecutionId);
        return this.bpmTaskDao.getByParam(map);
    }
    public void recycleTask(String taskId, OpinionStatus opinionStatus, String opinionStr) {
        BpmTask task = (BpmTask) get(taskId);
        String sourceTaskId = taskId;
        if (Arrays.<TaskType>asList(new TaskType[]{TaskType.ADD_SIGN, TaskType.SIGN, TaskType.ADDDO}).contains(TaskType.fromKey(task.getTaskType()))) {
            sourceTaskId = task.getParentId();
        }
        List<BpmTask> subTasks = (List<BpmTask>) getByParentId(sourceTaskId).stream().filter(t -> Arrays.<TaskType>asList(new TaskType[]{TaskType.ADD_SIGN, TaskType.SIGN, TaskType.ADDDO}).contains(TaskType.fromKey(t.getTaskType()))).collect(Collectors.toList());
        for (BpmTask t : subTasks) {
            this.taskIdentityLinkManager.removeByTaskId(t.getId());
            remove(t.getId());
        }
        for (BpmTask t : subTasks) {
            BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(t.getId());
            if (OpinionStatus.fromKey(opinion.getStatus()) != OpinionStatus.AWAITING_CHECK) {
                continue;
            }
            opinion.setApproveTime(new Date((new Date()).getTime()));
            opinion.setOpinion(opinionStr);
            opinion.setStatus(opinionStatus.getKey());
            this.bpmTaskOpinionManager.update(opinion);
        }
    }
    public List<BpmTaskVO> getTaskLinksInfo(QueryFilter queryFilter) {
        return this.bpmTaskDao.getTaskLinksInfo(queryFilter);
    }
}
