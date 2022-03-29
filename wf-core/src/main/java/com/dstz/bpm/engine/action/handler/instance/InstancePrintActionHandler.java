package com.dstz.bpm.engine.action.handler.instance;
import com.dstz.base.core.util.StringUtil;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.task.IBpmTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
@Component
public class InstancePrintActionHandler
        implements BuiltinActionHandler<BaseActionCmd> {
    public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
        return (bpmInstance != null && StringUtil.isNotEmpty(bpmInstance.getStatus()) &&
                !StringUtils.equals(InstanceStatus.STATUS_DRAFT.getKey(), bpmInstance.getStatus()) &&
                !StringUtils.equals(InstanceStatus.STATUS_DISCARD.getKey(), bpmInstance.getStatus()));
    }
    public void execute(BaseActionCmd model) {
    }
    public ActionType getActionType() {
        return ActionType.PRINT;
    }
    public int getSn() {
        return 7;
    }
    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(true);
    }
    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }
    public String getConfigPage() {
        return "";
    }
    public String getDefaultGroovyScript() {
        return "";
    }
    public String getDefaultBeforeScript() {
//        return "_BtnThis.printForm(); return false;"; todo 暂时更改打印返回值
        return "print(); return false;";
    }
}
