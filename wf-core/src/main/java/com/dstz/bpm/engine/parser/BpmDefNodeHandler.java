package com.dstz.bpm.engine.parser;

import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.GateWayBpmNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.ServiceTaskNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import com.dstz.bpm.api.model.nodedef.impl.UserTaskNodeDef;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.core.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BpmDefNodeHandler {
    protected Logger LOG = LoggerFactory.getLogger(getClass());


    public void setProcessDefNodes(DefaultBpmProcessDef bpmProcessDef, Collection<FlowElement> collection) {
        setProcessDefNodes(null, collection, bpmProcessDef);
    }


    private void setProcessDefNodes(BpmNodeDef parentNodeDef, Collection<FlowElement> flowElementList, DefaultBpmProcessDef bpmProcessDef) {
        Map<String, FlowElement> nodeMap = getNodeList(flowElementList);

        List<SequenceFlow> seqList = getSequenceFlowList(flowElementList);

        Map<String, BpmNodeDef> nodeDefMap = getBpmNodeDef(nodeMap, parentNodeDef, bpmProcessDef);


        setRelateNodeDef(nodeDefMap, seqList);


        List<BpmNodeDef> nodeDefList = new ArrayList<>(nodeDefMap.values());


        bpmProcessDef.setBpmnNodeDefs(nodeDefList);

        for (BpmNodeDef nodeDef : nodeDefList) {
            BaseBpmNodeDef node = (BaseBpmNodeDef) nodeDef;
            node.setBpmProcessDef((BpmProcessDef) bpmProcessDef);
        }
    }


    private Map<String, BpmNodeDef> getBpmNodeDef(Map<String, FlowElement> nodeMap, BpmNodeDef parentNodeDef, DefaultBpmProcessDef bpmProcessDef) {
        Map<String, BpmNodeDef> map = new HashMap<>();
        Set<Map.Entry<String, FlowElement>> set = nodeMap.entrySet();
        for (Iterator<Map.Entry<String, FlowElement>> it = set.iterator(); it.hasNext(); ) {
            Map.Entry<String, FlowElement> ent = it.next();
            FlowElement flowEl = ent.getValue();

            BaseBpmNodeDef nodeDef = getNodeDef(parentNodeDef, flowEl, bpmProcessDef);

            map.put(ent.getKey(), nodeDef);
        }
        return map;
    }


    private void setRelateNodeDef(Map<String, BpmNodeDef> nodeDefMap, List<SequenceFlow> seqList) {
        for (SequenceFlow seq : seqList) {
            BpmNodeDef sourceDef = nodeDefMap.get(seq.getSourceRef());
            BpmNodeDef targetDef = nodeDefMap.get(seq.getTargetRef());

            if (sourceDef == null || targetDef == null) {
                this.LOG.warn("=====待完善的节点类型===={}=====", seq.toString());
                continue;
            }
            sourceDef.addOutcomeNode(targetDef);
            targetDef.addIncomeNode(sourceDef);

            if (sourceDef instanceof GateWayBpmNodeDef &&
                    StringUtil.isNotEmpty(seq.getConditionExpression())) {
                ((GateWayBpmNodeDef) sourceDef).getOutGoingConditions().put(targetDef.getNodeId(), seq.getConditionExpression());
            }
        }
    }


    private Map<String, FlowElement> getNodeList(Collection<FlowElement> flowElementList) {
        Map<String, FlowElement> map = new HashMap<>();
        for (FlowElement flowElement : flowElementList) {
            addNode(flowElement, map, (Class<? extends FlowElement>[]) this.aryNodeElement);
        }
        return map;
    }


    private Class<FlowElement>[] aryNodeElement = new Class[]{StartEvent.class, EndEvent.class, ParallelGateway.class, InclusiveGateway.class, ExclusiveGateway.class, UserTask.class, ServiceTask.class, CallActivity.class, SubProcess.class};


    private BaseBpmNodeDef getNodeDef(BpmNodeDef parentNodeDef, FlowElement flowElement, DefaultBpmProcessDef bpmProcessDef) {
        BaseBpmNodeDef nodeDef = null;
        if (flowElement instanceof Activity) {
            String multi = this.getNodeDefLoop((Activity)flowElement);
            if (flowElement instanceof UserTask) {
                if (multi == null) {
                    UserTaskNodeDef userTaskDef = new UserTaskNodeDef();
                    nodeDef = userTaskDef;
                    userTaskDef.setType(NodeType.USERTASK);
                }
            } else if (flowElement instanceof ServiceTask) {
                nodeDef = new ServiceTaskNodeDef();
                ((BaseBpmNodeDef)nodeDef).setType(NodeType.SERVICETASK);
            } else if (flowElement instanceof CallActivity) {
                CallActivityNodeDef callNodeDef = new CallActivityNodeDef();
                CallActivity call = (CallActivity)flowElement;
                String flowKey = call.getCalledElement();
                callNodeDef.setType(NodeType.CALLACTIVITY);
                callNodeDef.setFlowKey(flowKey);
                nodeDef = callNodeDef;
            } else if (flowElement instanceof SubProcess) {
                SubProcessNodeDef subProcessDef = new SubProcessNodeDef();
                nodeDef = subProcessDef;
                subProcessDef.setNodeId(flowElement.getId());
                subProcessDef.setName(flowElement.getName());
                subProcessDef.setParentBpmNodeDef(parentNodeDef);
                subProcessDef.setBpmProcessDef(bpmProcessDef);
                SubProcess subProcess = (SubProcess)flowElement;
                this.handSubProcess(subProcessDef, subProcess, bpmProcessDef);
            }
        } else if (flowElement instanceof StartEvent) {
            nodeDef = new BaseBpmNodeDef();
            ((BaseBpmNodeDef)nodeDef).setType(NodeType.START);
        } else if (flowElement instanceof EndEvent) {
            nodeDef = new BaseBpmNodeDef();
            ((BaseBpmNodeDef)nodeDef).setType(NodeType.END);
        } else if (flowElement instanceof Gateway) {
            nodeDef = new GateWayBpmNodeDef();
            if (flowElement instanceof ParallelGateway) {
                ((BaseBpmNodeDef)nodeDef).setType(NodeType.PARALLELGATEWAY);
            } else if (flowElement instanceof InclusiveGateway) {
                ((BaseBpmNodeDef)nodeDef).setType(NodeType.INCLUSIVEGATEWAY);
            } else if (flowElement instanceof ExclusiveGateway) {
                ((BaseBpmNodeDef)nodeDef).setType(NodeType.EXCLUSIVEGATEWAY);
            }
        }

        ((BaseBpmNodeDef)nodeDef).setParentBpmNodeDef(parentNodeDef);
        ((BaseBpmNodeDef)nodeDef).setNodeId(flowElement.getId());
        ((BaseBpmNodeDef)nodeDef).setName(flowElement.getName());
        return (BaseBpmNodeDef)nodeDef;
    }


    private void handSubProcess(BaseBpmNodeDef nodeDef, SubProcess subProcess, DefaultBpmProcessDef parentProcessDef) {
        DefaultBpmProcessDef bpmProcessDef = new DefaultBpmProcessDef();
        bpmProcessDef.setProcessDefinitionId(subProcess.getId());
        bpmProcessDef.setName(subProcess.getName());
        bpmProcessDef.setDefKey(subProcess.getId());
        bpmProcessDef.setParentProcessDef(parentProcessDef);

        SubProcessNodeDef subNodeDef = (SubProcessNodeDef) nodeDef;

        subNodeDef.setBpmProcessDef((BpmProcessDef) parentProcessDef);
        subNodeDef.setChildBpmProcessDef((BpmProcessDef) bpmProcessDef);
        Collection<FlowElement> list = subProcess.getFlowElements();
        setProcessDefNodes((BpmNodeDef) nodeDef, list, bpmProcessDef);
    }


    private void addNode(FlowElement flowElement, Map<String, FlowElement> map, Class<? extends FlowElement>... flowTypes) {
        for (Class<? extends FlowElement> flowType : flowTypes) {
            if (flowType.isInstance(flowElement)) {
                map.put(flowElement.getId(), flowElement);
                break;
            }
        }
    }


    private String getNodeDefLoop(Activity flowElement) {
        MultiInstanceLoopCharacteristics jaxbloop = flowElement.getLoopCharacteristics();
        if (jaxbloop == null) return null;
        return jaxbloop.isSequential() ? "sequence" : "parallel";
    }


    private List<SequenceFlow> getSequenceFlowList(Collection<FlowElement> flowElementList) {
        List<SequenceFlow> nodeList = new ArrayList<>();
        for (FlowElement flowElement : flowElementList) {
            if (flowElement instanceof SequenceFlow) {
                nodeList.add((SequenceFlow) flowElement);
            }
        }
        return nodeList;
    }
}