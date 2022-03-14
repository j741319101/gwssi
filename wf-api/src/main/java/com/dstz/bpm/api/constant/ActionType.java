package com.dstz.bpm.api.constant;

import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.util.DynamicEnumUtil;
import com.dstz.base.api.exception.BusinessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum ActionType {
    DRAFT("draft", "保存草稿", "instanceSaveActionHandler"),
    START("start", "启动", "instanceStartActionHandler"),
    AGREE("agree", "同意", "taskAgreeActionHandler"),
    SIGNAGREE("signAgree", "同意", "taskSignAgreeActionHandler"),
    SAVE("save", "保存", "taskSaveActionHandler"),
    OPPOSE("oppose", "反对", "taskOpposeActionHandler"),
    SIGNOPPOSE("signOppose", "否决", "taskSignOpposeActionHandler"),
    REJECT("reject", "驳回", "taskRejectActionHandler"),
    REJECT2START("reject2Start", "驳回发起人", "taskReject2StartActionHandler"),
    RECOVER("recover", "撤销", "instanceRecoverActionHandler"),
    INCREASEDYNAMIC("increaseDynamic", "追加", "increaseTaskHandler"),
    DECREASEDYNAMIC("decreaseDynamic", "取回", "decreaseTaskHandler"),
    DISPENDSE("dispense", "分发", "null"),
    TASKOPINION("taskOpinion", "审批历史", "instanceTaskOpinionActionHandler"),
    FLOWIMAGE("flowImage", "流程图", "instanceImageActionHandler"),
    PRINT("print", "打印", "instancePrintActionHandler"),
    MANUALEND("manualEnd", "人工终止", "instanceManualEndActionHandler"),
    INSTANCE_END("instanceEnd", "超管终止", "instanceEndActionHandler"),
    TASK_FREE_JUMP("taskFreeJump", "超管跳转", "taskFreeJumpActionHandler"),
    INSTANCE_RESTART("instanceRestart", "重启实例", "instanceRestartActionHandler"),
    LOCK("lock", "锁定", "taskLockActionHandler"),
    UNLOCK("unlock", "解锁", "taskUnlockActionHandler"),
    TURN("turn", "转办", "taskTurnActionHandler"),
    REMINDER("reminder", "催办", "instanceReminderActionHandler"),
    RECALL("recall", "撤回", "instanceRecallActionHandler"),
    ADDSIGN("addSign", "加签", "addSignActionHandler"),
    ADDDO("addDo", "加办", "addDoActionHandler"),
    ADDDOAGREE("addDoAgree", "处理加办", "addDoAgreeActionHandler"),
    CREATE("create", "创建时", "null"),
    END("end", "流程结束", "null"),
    CLOSE("closeWindow", "返回", "null"),
    TASKCANCELLED("taskCancelled", "任务取消", "null"),
    CARBONCOPY("carbonCopy", "抄送", "carbonCopyActionHandler"),
    CARBONINSTCOPY("carbonInstCopy", "流程抄送", "carbonInstCopyActionHandler"),
    CARBONCOPYREVIEW("carbonCopyReview", "抄送审阅", "carbonCopyReviewActionHandler"),
    LEADERSAVE("leaderSave", "返回秘书", "taskLeaderSaveActionHandler"),
    SENDLEADER("sendLeader", "呈送领导", "taskSendLeaderActionHandle");

    private String key = "";
    private String name = "";
    private String beanId = "";
    private static final List<ActionType> actionTypes = new ArrayList();

    private ActionType(String key, String name, String beanId) {
        this.key = key;
        this.name = name;
        this.beanId = beanId;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public String getBeanId() {
        return this.beanId;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String toString() {
        return this.key;
    }

    public static ActionType addEnum(String enumName, String key, String name, String beanId) {
        ActionType actionType = (ActionType)DynamicEnumUtil.addEnum(ActionType.class, enumName, new Class[]{String.class, String.class, String.class}, new Object[]{key, name, beanId});
        return actionType;
    }

    public static ActionType fromKey(String key) {
        Iterator var1 = actionTypes.iterator();

        ActionType c;
        do {
            if (!var1.hasNext()) {
                ActionType[] var5 = values();
                int var6 = var5.length;

                for(int var3 = 0; var3 < var6; ++var3) {
                      c = var5[var3];
                    if (c.getKey().equalsIgnoreCase(key)) {
                        return c;
                    }
                }

                throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
            }

            c = (ActionType)var1.next();
        } while(!c.getKey().equalsIgnoreCase(key));

        return c;
    }
}
