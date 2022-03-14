/*     */
package com.dstz.bpm.engine.action.handler.instance;
/*     */
/*     */

import com.dstz.base.api.constant.IStatusCode;
import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.*;


@Component("instanceRestartActionHandler")
public class InstanceRestartActionHandler
        implements BuiltinActionHandler<DefaultInstanceActionCmd> {
    @Autowired
    private BpmInstanceManager bpmInstanceManager;
    @Autowired
    private ActInstanceService actInstanceService;
    @Autowired
    private BpmDefinitionManager bpmDefinitionManager;
    @Resource
    private IBpmBusDataHandle bpmBusDataHandle;
    @Resource
    private BpmTaskStackManager bpmTaskStackManager;

    public boolean isDisplay(boolean readOnly, BpmNodeDef bpmNodeDef, IBpmInstance bpmInstance, IBpmTask bpmTask) {
        if (bpmInstance == null || !ContextUtil.currentUserIsAdmin()) {
            return false;
        }
        if (bpmInstance != null) {
            return false;
        }
        return canRestart(bpmInstance);
    }

    private boolean canRestart(IBpmInstance bpmInstance) {
        return StringUtils.equalsAny(bpmInstance.getStatus(), new CharSequence[]{InstanceStatus.STATUS_END
                .getKey(), InstanceStatus.STATUS_MANUAL_END
                .getKey()});
    }


    public void execute(DefaultInstanceActionCmd model) {
        if (!ContextUtil.currentUserIsAdmin()) {
            throw new BusinessMessage("操作受限，您没有该操作权限", (IStatusCode) BpmStatusCode.NO_PERMISSION);
        }
        BpmInstance bpmInstance = (BpmInstance) this.bpmInstanceManager.get(model.getInstanceId());
        if (bpmInstance == null)
            throw new BusinessMessage("操作流程实例不存在");
        if (!canRestart((IBpmInstance) bpmInstance)) {
            throw new BusinessMessage("操作流程实例状态已在运行中");
        }

        String destination = model.getDestination();
        bpmInstance.setActInstId(null);
        model.setBizDataMap(this.bpmBusDataHandle.getInstanceData(null, bpmInstance));
        model.setBpmInstance((IBpmInstance) bpmInstance);
        model.setBpmDefinition((IBpmDefinition) this.bpmDefinitionManager.get(bpmInstance.getDefId()));

        if (isNotEmpty(bpmInstance.getParentInstId())) {

            DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
            defaultQueryFilter.addFilter("inst_id_", bpmInstance.getId(), QueryOP.EQUAL);
            defaultQueryFilter.addFilter("node_type_", "endNoneEvent", QueryOP.EQUAL);
            defaultQueryFilter.addFieldSort("id_", "desc");
            List<BpmTaskStack> bpmTaskStacks = this.bpmTaskStackManager.query((QueryFilter) defaultQueryFilter);
            if (CollectionUtil.isEmpty(bpmTaskStacks)) {
                throw new BusinessException("重启失败，查询执行栈： instID："+ bpmInstance.getId());
            }
            BpmTaskStack bpmTaskStack = bpmTaskStacks.get(0);
            bpmTaskStack.setNodeId(bpmInstance.getSuperNodeId());
            model.setExecutionStack((BpmExecutionStack) bpmTaskStack);
            BpmContext.setThreadDynamictaskStack(bpmTaskStack.getNodeId(), (BpmExecutionStack) bpmTaskStack);
        } try {
            String actInstId;
            BpmContext.setActionModel((ActionCmd) model);

            if (isEmpty(destination)) {
                actInstId = this.actInstanceService.startProcessInstance(bpmInstance.getActDefId(), bpmInstance.getBizKey(), model.getActionVariables());
            } else {
                actInstId = this.actInstanceService.startProcessInstance((IBpmInstance) bpmInstance, model.getActionVariables(), new String[]{destination});
            }
            bpmInstance.setActInstId(actInstId);
            bpmInstance.setStatus(InstanceStatus.STATUS_RUNNING.getKey());
            bpmInstance.setDuration(null);
            bpmInstance.setEndTime(null);
            this.bpmInstanceManager.update(bpmInstance);
        } finally {
            BpmContext.cleanTread();
        }
    }


    public ActionType getActionType() {
        return ActionType.INSTANCE_RESTART;
    }


    public int getSn() {
        return 9;
    }


    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.FALSE;
    }


    public Boolean isDefault() {
        return Boolean.TRUE;
    }


    public String getConfigPage() {
        return "/bpm/instance/instanceRestartOpinionDialog.html";
    }


    public String getDefaultGroovyScript() {
        return null;
    }


    public String getDefaultBeforeScript() {
        return null;
    }
}