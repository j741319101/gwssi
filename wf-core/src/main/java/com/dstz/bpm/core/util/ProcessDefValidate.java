//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dstz.bpm.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Resource;

import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
import com.dstz.bpm.api.engine.plugin.context.UserQueryPluginContext;
import com.dstz.bpm.api.model.def.BpmDataModel;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.form.BpmForm;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.form.api.model.FormCategory;
import com.dstz.form.api.model.IFormDef;
import com.dstz.form.api.service.FormService;
import com.dstz.sys.api.model.SysConnectRecordDTO;
import com.dstz.sys.api.service.SysConnectRecordService;
import org.springframework.stereotype.Service;

@Service
public class ProcessDefValidate {
    public static final String MSG_TYPE_WARN = "warning";
    public static final String MSG_TYPE_ERROR = "danger";
    public static final String MSG_TYPE_INFO = "info";
    public static final String CONNECTiON_RECORD_FORM = "BPM-DEF-FORM";
    public static final String CONNECTiON_RECORD_SUB_DEF = "BPM-DEF-DUBDEF";
    @Resource
    private FormService formService;
    @Resource
    private SysConnectRecordService sysConnectRecordService;

    public ProcessDefValidate() {
    }

    public void validate(BpmProcessDef processDef) {
        ThreadMapUtil.remove();
        this.sysConnectRecordService.removeBySourceId(processDef.getProcessDefinitionId(), (String)null);
        DefaultBpmProcessDef def = (DefaultBpmProcessDef)processDef;
        this.validateForm(def);
        this.validateNodeUser(def);
        this.validateMobileForm(def);
        def.getBpmnNodeDefs().stream().filter((node) -> {
            return node.getType() == NodeType.EXCLUSIVEGATEWAY || node.getType() == NodeType.INCLUSIVEGATEWAY;
        }).forEach((currentNode) -> {
            if (currentNode.getOutcomeNodes().size() > 1) {
                currentNode.getOutcomeNodes().forEach((outNode) -> {
                    GateWayBpmNodeDef node = (GateWayBpmNodeDef)currentNode;
                    if (!node.getOutGoingConditions().containsKey(outNode.getNodeId())) {
                        this.putMsgToThread("danger", String.format("请配置 [%s] --> [%s] 节点的分支条件！", node.getName(), outNode.getName()));
                    }

                });
            }

        });
    }

    private void validateCallActiviti(DefaultBpmProcessDef def) {
        def.getBpmnNodeDefs().stream().filter((node) -> {
            return node.getType() == NodeType.CALLACTIVITY;
        }).forEach((currentNode) -> {
            CallActivityNodeDef node = (CallActivityNodeDef)currentNode;
            if (StringUtil.isEmpty(node.getFlowKey())) {
                this.putMsgToThread("danger", String.format("请配置外部子流程节点 [%s] 的关联子流程 ！", currentNode.getName()));
            } else {
                SysConnectRecordDTO record = new SysConnectRecordDTO();
                record.setId(IdUtil.getSuid());
                record.setType("BPM-DEF-DUBDEF");
                record.setSourceId(def.getProcessDefinitionId());
                record.setTargetId(node.getFlowKey());
                record.setNotice(String.format("当前流程正在以外部子流程的形式被流程 “%s” [%s] 引用，请先移除关联", def.getName(), def.getDefKey()));
                this.sysConnectRecordService.save(record);
            }

        });
    }

    private void validateNodeUser(DefaultBpmProcessDef def) {
        StringBuilder sb = new StringBuilder();
        def.getBpmnNodeDefs().forEach((nodeDef) -> {
            if (nodeDef.getType() == NodeType.USERTASK) {
                this.validateOneNodeUser(nodeDef, sb);
            } else if (nodeDef.getType() == NodeType.SUBPROCESS) {
                SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)nodeDef;
                BpmProcessDef processDef = subProcessNodeDef.getChildBpmProcessDef();
                if (processDef != null) {
                    List<BpmNodeDef> subBpmNodeDefs = processDef.getBpmnNodeDefs();
                    subBpmNodeDefs.forEach((subNodes) -> {
                        if (subNodes.getType() == NodeType.USERTASK) {
                            this.validateOneNodeUser(subNodes, sb);
                        }

                    });
                }
            }

        });
        if (sb.length() > 0) {
            sb.insert(0, "请配置以下节点候选人：<br>");
            this.putMsgToThread("danger", sb.toString());
        }

    }

    private void validateOneNodeUser(BpmNodeDef nodeDef, StringBuilder sb) {
        boolean hasUserAssignPlgin = false;
        Iterator var4 = nodeDef.getBpmPluginContexts().iterator();

        while(var4.hasNext()) {
            BpmPluginContext ctx = (BpmPluginContext)var4.next();
            if (ctx instanceof UserQueryPluginContext && !((UserQueryPluginContext)ctx).isEmpty()) {
                hasUserAssignPlgin = true;
            }
        }

        if (!hasUserAssignPlgin) {
            if (sb.length() > 1) {
                sb.append("，");
            }

            sb.append("[").append(nodeDef.getName()).append("]");
        }

    }

    private void validateForm(DefaultBpmProcessDef processDef) {
        BpmForm globalForm = processDef.getGlobalForm();
        List<BpmDataModel> boDefs = processDef.getDataModelList();
        this.checkForm(boDefs, globalForm, "全局表单");
        boolean noOneForm = true;
        Set<String> formKeys = new HashSet();
        if (globalForm != null && !globalForm.isFormEmpty()) {
            noOneForm = false;
            if (globalForm.getType() == FormCategory.INNER) {
                formKeys.add(globalForm.getFormValue());
            }
        }

        Iterator var6 = processDef.getBpmnNodeDefs().iterator();

        while(var6.hasNext()) {
            BpmNodeDef nodeDef = (BpmNodeDef)var6.next();
            BpmForm nodeForm = nodeDef.getForm();
            this.checkForm(boDefs, nodeForm, nodeDef.getName());
            if (nodeForm != null && !nodeForm.isFormEmpty()) {
                noOneForm = false;
                if (nodeForm.getType() == FormCategory.INNER) {
                    formKeys.add(nodeForm.getFormValue());
                }
            }
        }

        if (noOneForm) {
            this.putMsgToThread("danger", "请配置流程表单 ！！！");
        }

        this.saveFormConnects(formKeys, processDef);
    }

    private void saveFormConnects(Set<String> formKeys, DefaultBpmProcessDef processDef) {
        if (!formKeys.isEmpty()) {
            List<SysConnectRecordDTO> connectRecords = new ArrayList();
            Iterator var4 = formKeys.iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                SysConnectRecordDTO record = new SysConnectRecordDTO();
                record.setId(IdUtil.getSuid());
                record.setType("BPM-DEF-FORM");
                record.setSourceId(processDef.getProcessDefinitionId());
                record.setTargetId(key);
                record.setNotice(String.format("流程 “%s” [%s] 正在使用当前表单", processDef.getName(), processDef.getDefKey()));
                connectRecords.add(record);
            }

            this.sysConnectRecordService.save(connectRecords);
        }
    }

    private void validateMobileForm(DefaultBpmProcessDef processDef) {
        BpmForm globalForm = processDef.getGlobalMobileForm();
        List<BpmDataModel> boDefs = processDef.getDataModelList();
        this.checkForm(boDefs, globalForm, "移动端全局表单");
        boolean noOneForm = true;
        Set<String> formKeys = new HashSet();
        if (globalForm != null && !globalForm.isFormEmpty()) {
            noOneForm = false;
            if (globalForm.getType() == FormCategory.INNER) {
                formKeys.add(globalForm.getFormValue());
            }
        }

        Iterator var6 = processDef.getBpmnNodeDefs().iterator();

        while(var6.hasNext()) {
            BpmNodeDef nodeDef = (BpmNodeDef)var6.next();
            BpmForm mobileNodeForm = nodeDef.getMobileForm();
            if (mobileNodeForm != null && !mobileNodeForm.isFormEmpty()) {
                noOneForm = false;
                if (mobileNodeForm.getType() == FormCategory.INNER) {
                    formKeys.add(mobileNodeForm.getFormValue());
                }
            }
        }

        if (processDef.getExtProperties().getSupportMobile() == 0 && !noOneForm) {
            this.putMsgToThread("warning", "流程配置尚未开启 ”支持移动端“，移动端表单的配置无效，请注意");
        }

        if (processDef.getExtProperties().getSupportMobile() != 0 && noOneForm) {
            this.putMsgToThread("danger", "请配置移动端表单！！！");
        }

        this.saveFormConnects(formKeys, processDef);
    }

    private void putMsgToThread(String type, String msg) {
        List<String> msgList = (List)ThreadMapUtil.getOrDefault(type, new ArrayList());
        msgList.add(msg);
        ThreadMapUtil.put(type, msgList);
    }

    private void checkForm(List<BpmDataModel> boDefs, BpmForm form, String nodeName) {
        if (form != null && !form.isFormEmpty()) {
            if (FormCategory.FRAME == form.getType()) {
                if (StringUtil.isNotEmpty(form.getFormValue()) && StringUtil.isEmpty(form.getFormHandler())) {
                    this.putMsgToThread("info", nodeName + " URL表单未配置表单处理器，请确认是否不需要持久化页面数据？");
                }

            } else {
                IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
                if (formDef == null) {
                    throw new BusinessMessage(String.format("在线表单 [%s] 已经被移除！请重新配置[%s]的表单！！!", form.getName(), nodeName));
                } else {
                    boolean hasBo = false;
                    Iterator var6 = boDefs.iterator();

                    while(var6.hasNext()) {
                        BpmDataModel dm = (BpmDataModel)var6.next();
                        if (dm.getCode().equals(formDef.getBoKey())) {
                            hasBo = true;
                        }
                    }

                    if (!hasBo) {
                        throw new BusinessMessage(String.format("在线表单 [%s] 对应的业务对象已经被移除！请重新配置[%s]的表单！！!", form.getName(), nodeName));
                    }
                }
            }
        }
    }
}
