
package com.dstz.bpm.engine.action.handler.instance;


import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bpm.api.constant.ActionType;
import com.dstz.bpm.api.constant.InstanceStatus;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
import com.dstz.bpm.api.model.def.IBpmDefinition;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.test.BpmSystemTestService;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.bpm.engine.action.handler.AbsActionHandler;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
public class InstanceSaveActionHandler
        extends AbsActionHandler<DefaultInstanceActionCmd> {

    @Resource
    private BpmSystemTestService bpmSystemTestService;

    protected void doAction(DefaultInstanceActionCmd model) {
        BpmInstance instance = (BpmInstance) model.getBpmInstance();
        instance.setStatus(InstanceStatus.STATUS_DRAFT.getKey());
    }

    protected void doActionBefore(DefaultInstanceActionCmd actionModel) {
    }
    protected boolean prepareActionDatas(DefaultInstanceActionCmd data) {
        data.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(data.getDefId()));
        getInstance(data);
        parserBusinessData(data);
        boolean isStop = handelFormInit((BaseActionCmd) data, this.bpmProcessDefService.getStartEvent(data.getDefId()));
        return isStop;
    }

    protected void doActionAfter(DefaultInstanceActionCmd actionModel) {
        persistenceInstance(actionModel);
    }

    protected void persistenceInstance(DefaultInstanceActionCmd actionModel) {
        BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
        if (instance.hasCreate().booleanValue()) {
            handleInstanceSubject(actionModel);
            this.bpmInstanceManager.update(instance);
        } else {
            handleInstanceSubject(actionModel);
            this.bpmSystemTestService.saveBpmInstance((IBpmInstance) instance);
            this.bpmInstanceManager.create(instance);
        }
    }

    protected void getInstance(DefaultInstanceActionCmd intanceCmdData) {
        String instId = intanceCmdData.getInstanceId();
        BpmInstance instance = null;
        if (StringUtil.isNotEmpty(instId)) {
            instance = (BpmInstance) this.bpmInstanceManager.get(instId);
            if (StringUtil.isNotEmpty(instance.getActInstId())) {
                throw new BusinessException("草稿已经启动，请勿多次启动该草稿！");
            }
        }
        if (instance == null) {
            IBpmDefinition bpmDefinition = intanceCmdData.getBpmDefinition();
            instance = this.bpmInstanceManager.genInstanceByDefinition(bpmDefinition);
        }
        if (Objects.isNull(instance.getCreateBy()) || Objects.isNull(instance.getCreator())){
            instance.setCreateBy(ContextUtil.getCurrentUserId());
            instance.setCreator(ContextUtil.getCurrentUserAccount());
        }

            instance.setCreateOrgId(intanceCmdData.getApproveOrgId());
        intanceCmdData.setBpmInstance((IBpmInstance) instance);
    }


    private void handleInstanceSubject(DefaultInstanceActionCmd data) {
        BpmInstance instance = (BpmInstance) data.getBpmInstance();
        DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
        String subjectRule = processDef.getExtProperties().getSubjectRule();
        if (StringUtil.isEmpty(subjectRule))
            return;
        Map<String, Object> ruleVariables = new HashMap<>();
        ruleVariables.put("title", processDef.getName());
        IUser user = ContextUtil.getCurrentUser();
        if (user != null) {
            ruleVariables.put("startorName", user.getFullname());

        } else {
            ruleVariables.put("startorName", "系统");
        }
        ruleVariables.put("startDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        ruleVariables.put("startTime", DateUtil.now());
        ruleVariables.putAll(data.getVariables());

        Map<String, IBusinessData> boMap = data.getBizDataMap();
        if (CollectionUtil.isNotEmpty(boMap)) {
            Set<String> bocodes = boMap.keySet();
            for (String bocode : bocodes) {
                IBusinessData bizData = boMap.get(bocode);
                Map<String, Object> dataMap = bizData.getData();
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    ruleVariables.put(bocode + "." + (String) entry.getKey(), entry.getValue());
                }
            }
        }
        String subject = getTitleByVariables(subjectRule, ruleVariables);
        instance.setSubject(subject);
        this.LOG.debug("更新流程标题:{}", subject);
    }

    private String getTitleByVariables(String subject, Map<String, Object> variables) {
        if (StringUtils.isEmpty(subject))
             return "";
        Pattern regex = Pattern.compile("\\{(.*?)\\}", 98);
        Matcher matcher = regex.matcher(subject);
        while (matcher.find()) {
            String tag = matcher.group(0);
            String rule = matcher.group(1);
            String[] aryRule = rule.split(":");
            String name = "";
            if (aryRule.length == 1) {
                name = rule;
            } else {
                name = aryRule[1];
            }
            if (variables.containsKey(name)) {
                Object obj = variables.get(name);
                if (obj != null) {
                    try {
                        subject = subject.replace(tag, obj.toString());
                    } catch (Exception e) {
                        subject = subject.replace(tag, "");
                    }
                    continue;
                }
                subject = subject.replace(tag, "");
                continue;
            }
            subject = subject.replace(tag, "");
        }
        return subject;
    }

    public ActionType getActionType() {
        return ActionType.DRAFT;
    }

    public int getSn() {
        return 2;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        if (nodeDef.getType() == NodeType.START) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return null;
    }
    public String getDefaultGroovyScript() {
        return "";
    }

}

