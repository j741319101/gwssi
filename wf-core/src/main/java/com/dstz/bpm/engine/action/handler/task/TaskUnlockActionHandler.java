/*    */
package com.dstz.bpm.engine.action.handler.task;
/*    */
/*    */
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.TaskStatus;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.core.model.BpmTask;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
@Component
/*    */ public class TaskUnlockActionHandler
        extends AbstractTaskActionHandler<DefualtTaskActionCmd>
        {
    public ActionType getActionType() {
        /* 19 */
        return ActionType.UNLOCK;
    }
    @Resource
    BpmTaskManager bpmTaskMananger;
    public void execute(DefualtTaskActionCmd model) {
        /* 24 */
        prepareActionDatas(model);
        /* 25 */
        checkFlowIsValid((BaseActionCmd) model);
        /* 27 */
        BpmTask task = (BpmTask) model.getBpmTask();
        /* 28 */
        if (!task.getStatus().equals(TaskStatus.LOCK.getKey())) {
            /* 29 */
            throw new BusinessMessage("该任务并非锁定状态,或已经被解锁，解锁失败");
        }
        /* 32 */
        this.bpmTaskMananger.unLockTask(task.getId());
    }
    protected void doActionAfter(DefualtTaskActionCmd actionModel) {
    }
    protected void doActionBefore(DefualtTaskActionCmd actionModel) {
    }
    public int getSn() {
        /* 47 */
        return 6;
    }
    public Boolean isDefault() {
        /* 52 */
        return Boolean.valueOf(false);
    }
    public String getDefaultGroovyScript() {
        /* 57 */
        return "return task.getStatus().equals(\"LOCK\");";
    }
    public String getConfigPage() {
        /* 62 */
        return "";
    }
    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(false);//todo 暂时去掉
    }
}
 