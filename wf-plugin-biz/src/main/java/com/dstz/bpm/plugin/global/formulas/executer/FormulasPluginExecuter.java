package com.dstz.bpm.plugin.global.formulas.executer;

import com.dstz.bpm.api.constant.EventType;
import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import com.dstz.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import com.dstz.bpm.plugin.global.formulas.def.FormulasPluginDef;
import com.dstz.bpm.plugin.global.formulas.def.OprFormula;
import cn.hutool.core.collection.CollectionUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class FormulasPluginExecuter
        extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, FormulasPluginDef> {
//    private final ThreadLocal<List<IDataFlowSql>> listThreadLocal = new ThreadLocal<>();

//    @Resource
//    private IDataFlowService dataFlowService;

    public Void execute(DefaultBpmTaskPluginSession pluginSession, FormulasPluginDef pluginDef) {
        //todo 公式相关暂时注掉
        //        ActionCmd actionCmd = BpmContext.getActionModel();
//        List<OprFormula> oprFormulas = pluginDef.getOprFormulas();
//        Set<String> ids = new HashSet<>();
//        oprFormulas.forEach(oprFormula -> {
//            List<String> actions = oprFormula.getAction();
//
//            if (CollectionUtil.isNotEmpty(actions) && CollectionUtil.contains(actions, actionCmd.getActionName())) {
//                ids.add(oprFormula.getId());
//            }
//        });
//        if (EventType.PRE_SAVE_BUS_EVENT == pluginSession.getEventType()) {
//            this.listThreadLocal.remove();
//            if (CollectionUtil.isEmpty(ids)) {
//                return null;
//            }
//            this.listThreadLocal.set(this.dataFlowService.analysis(actionCmd.getBusData(), ids));
//        } else if (EventType.TASK_POST_CREATE_EVENT == pluginSession.getEventType()) {
//            List<IDataFlowSql> dataFlowSqls = this.listThreadLocal.get();
//            if (CollectionUtil.isNotEmpty(dataFlowSqls)) {
//                this.dataFlowService.execute(dataFlowSqls);
//            }
//            this.listThreadLocal.remove();
//        }
        return null;
    }
}


