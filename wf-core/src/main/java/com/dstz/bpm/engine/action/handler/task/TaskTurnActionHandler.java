package com.dstz.bpm.engine.action.handler.task;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.TaskIdentityLink;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.AppUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.bean.BeanUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
@Component
public class TaskTurnActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;
    public ActionType getActionType() {
        return ActionType.TURN;
    }
    public void execute(DefualtTaskActionCmd model) {
        prepareActionDatas(model);
        checkFlowIsValid((BaseActionCmd) model);
        BpmTask task = (BpmTask) model.getBpmTask();
        List<SysIdentity> userSetting = model.getBpmIdentity(task.getNodeId());
        if (BeanUtil.isEmpty(userSetting)) {
            throw new BusinessException(BpmStatusCode.NO_ASSIGN_USER);
        }
        IUser user = ContextUtil.getCurrentUser();
        TaskIdentityLink taskIdentityLink = model.getTaskIdentityLink();
        if (StringUtils.equals(task.getAssigneeId(), "0") &&
                StringUtils.indexOf(task.getAssigneeNames(), "user") != -1) {
            TaskIdentityLink identityLink = this.taskIdentityLinkManager.getByTaskIdAndUserId(task.getId(), user.getUserId());
            identityLink.setIdentity(((SysIdentity) userSetting.get(0)).getId());
            identityLink.setIdentityName(((SysIdentity) userSetting.get(0)).getName());
            identityLink.setPermissionCode(String.format("%s-user", new Object[]{((SysIdentity) userSetting.get(0)).getId()}));
            identityLink.setOrgId(((SysIdentity) userSetting.get(0)).getOrgId());
            this.taskIdentityLinkManager.update(identityLink);
            task.setAssigneeNames(task.getAssigneeNames().replace(
                    String.format("user-%s-%s", new Object[]{user.getFullname(), user.getUserId()
                    }), String.format("user-%s-%s", new Object[]{((SysIdentity) userSetting.get(0)).getName(), ((SysIdentity) userSetting.get(0)).getId()})));
        } else {
            task.setAssigneeId(((SysIdentity) userSetting.get(0)).getId());
            task.setAssigneeNames(((SysIdentity) userSetting.get(0)).getName());
            this.taskIdentityLinkManager.removeByTaskId(task.getId());
            List<SysIdentity> identitys = new ArrayList<>();
            DefaultIdentity defaultIdentity = new DefaultIdentity();
            defaultIdentity.setId(((SysIdentity) userSetting.get(0)).getId());
            defaultIdentity.setName(((SysIdentity) userSetting.get(0)).getName());
            defaultIdentity.setType("user");
            identitys.add(defaultIdentity);
            this.taskIdentityLinkManager.createIdentityLink((IBpmTask) task, identitys);
        }
        task.setStatus(TaskStatus.TURN.getKey());
        this.taskManager.update(task);
        handelTaskOpinion(task, taskIdentityLink, model.getOpinion());
        Map<String, IExtendTaskAction> leaderTaskAction = AppUtil.getImplInstance(IExtendTaskAction.class);
        leaderTaskAction.forEach((key, action) -> action.turnLeaderTask((IBpmTask) task));
    }
    private void handelTaskOpinion(BpmTask task, TaskIdentityLink taskIdentityLink, String taskOpinion) {
        BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
        String opinionId = opinion.getId();
        String assignInfo = opinion.getAssignInfo();
        opinion.setId(IdUtil.getSuid());
        StringBuilder sb = new StringBuilder();
        sb.append("user").append("-").append(task.getAssigneeNames()).append("-")
                .append(task.getAssigneeId()).append("-").append(taskIdentityLink.getOrgId()).append(",");
        opinion.setAssignInfo(sb.toString());
        opinion.setTaskOrgId(taskIdentityLink.getOrgId());
        this.bpmTaskOpinionManager.create(opinion);
        opinion.setId(opinionId);
        opinion.setAssignInfo(assignInfo);
        opinion.setTaskId("-");
        opinion.setOpinion(taskOpinion);
        opinion.setStatus(ActionType.TURN.getKey());
        opinion.setApproveTime(new Date());
        opinion.setApprover(ContextUtil.getCurrentUserId());
        opinion.setApproverName(ContextUtil.getCurrentUser().getFullname());
        opinion.setDurMs(Long.valueOf(opinion.getApproveTime().getTime() - opinion.getCreateTime().getTime()));
        this.bpmTaskOpinionManager.update(opinion);
    }
    protected void doActionAfter(DefualtTaskActionCmd actionModel) {
    }
    protected void doActionBefore(DefualtTaskActionCmd actionModel) {
    }
    public int getSn() {
        return 6;
    }
    public Boolean isDefault() {
        //return Boolean.valueOf(true);//todo 暂时去掉按钮
        return Boolean.valueOf(false);
    }
    public Boolean isSupport(BpmNodeDef paramBpmNodeDef) {
        return Boolean.valueOf(false);
    }
    ;
    public String getConfigPage() {
        return "/bpm/task/taskTrunActionDialog.html";
    }
}
 