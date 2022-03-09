/*     */ package cn.gwssi.ecloudbpm.wf.service;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.handler.IExtendTaskAction;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.parser.BpmDefNodeHandler;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.parser.BpmProcessDefParser;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.SerializeException;
/*     */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class DefaultBpmProcessDefService
/*     */   implements BpmProcessDefService
/*     */ {
/*  47 */   protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultBpmProcessDefService.class);
/*     */ 
/*     */   
/*     */   @Resource
/*     */   ICache<DefaultBpmProcessDef> iCache;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private BpmDefNodeHandler bpmDefNodeHandler;
/*     */   
/*     */   @Resource
/*     */   RepositoryService repositoryService;
/*     */ 
/*     */   
/*     */   public BpmProcessDef getBpmProcessDef(String processDefId) {
/*  66 */     return (BpmProcessDef)getProcessDefByDefId(processDefId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getNodeDefs(String processDefinitionId) {
/*  72 */     DefaultBpmProcessDef processDef = getProcessDefByDefId(processDefinitionId);
/*  73 */     return processDef.getBpmnNodeDefs();
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmNodeDef getBpmNodeDef(String processDefinitionId, String nodeId) {
/*  78 */     List<BpmNodeDef> list = getNodeDefs(processDefinitionId);
/*  79 */     List<SubProcessNodeDef> listSub = new ArrayList<>();
/*  80 */     for (BpmNodeDef nodeDef : list) {
/*  81 */       if (nodeDef.getNodeId().equals(nodeId)) {
/*  82 */         return nodeDef;
/*     */       }
/*  84 */       if (nodeDef instanceof SubProcessNodeDef) {
/*  85 */         listSub.add((SubProcessNodeDef)nodeDef);
/*     */       }
/*     */     } 
/*  88 */     if (listSub.size() > 0)
/*  89 */       return findSubProcessNodeDefByNodeId(listSub, nodeId); 
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   private BpmNodeDef findSubProcessNodeDefByNodeId(List<SubProcessNodeDef> subList, String nodeId) {
/*  94 */     for (SubProcessNodeDef nodeDef : subList) {
/*  95 */       List<BpmNodeDef> nodeList = nodeDef.getChildBpmProcessDef().getBpmnNodeDefs();
/*  96 */       List<SubProcessNodeDef> nestSub = new ArrayList<>();
/*  97 */       for (BpmNodeDef tmpDef : nodeList) {
/*  98 */         if (tmpDef.getNodeId().equals(nodeId)) {
/*  99 */           return tmpDef;
/*     */         }
/* 101 */         if (tmpDef instanceof SubProcessNodeDef) {
/* 102 */           nestSub.add((SubProcessNodeDef)tmpDef);
/*     */         }
/*     */       } 
/* 105 */       if (nestSub.size() > 0)
/* 106 */         return findSubProcessNodeDefByNodeId(nestSub, nodeId); 
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmNodeDef getStartEvent(String processDefId) {
/* 114 */     DefaultBpmProcessDef processDef = getProcessDefByDefId(processDefId);
/* 115 */     List<BpmNodeDef> list = processDef.getBpmnNodeDefs();
/* 116 */     for (BpmNodeDef nodeDef : list) {
/* 117 */       if (nodeDef.getType().equals(NodeType.START))
/* 118 */         return nodeDef; 
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getEndEvents(String processDefId) {
/* 125 */     List<BpmNodeDef> nodeList = new ArrayList<>();
/* 126 */     DefaultBpmProcessDef processDef = getProcessDefByDefId(processDefId);
/* 127 */     List<BpmNodeDef> list = processDef.getBpmnNodeDefs();
/* 128 */     for (BpmNodeDef nodeDef : list) {
/* 129 */       if (nodeDef.getType().equals(NodeType.END)) {
/* 130 */         nodeList.add(nodeDef);
/*     */       }
/*     */     } 
/* 133 */     return nodeList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clean(String processDefId) {
/* 138 */     this.iCache.delByKey("procdef_" + processDefId);
/* 139 */     BpmContext.cleanTread();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getStartNodes(String processDefId) {
/* 144 */     BpmNodeDef nodeDef = getStartEvent(processDefId);
/* 145 */     return nodeDef.getOutcomeNodes();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStartNode(String defId, String nodeId) {
/* 151 */     List<BpmNodeDef> nodes = getStartNodes(defId);
/* 152 */     for (BpmNodeDef node : nodes) {
/* 153 */       if (node.getNodeId().equals(nodeId)) {
/* 154 */         return true;
/*     */       }
/*     */     } 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validNodeDefType(String defId, String nodeId, NodeType nodeDefType) {
/* 163 */     BpmNodeDef nodeDef = getBpmNodeDef(defId, nodeId);
/* 164 */     return nodeDef.getType().equals(nodeDefType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContainCallActivity(String defId) {
/* 170 */     DefaultBpmProcessDef processDef = getProcessDefByDefId(defId);
/* 171 */     List<BpmNodeDef> list = processDef.getBpmnNodeDefs();
/* 172 */     for (BpmNodeDef nodeDef : list) {
/* 173 */       if (nodeDef.getType().equals(NodeType.CALLACTIVITY)) {
/* 174 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultBpmProcessDef getProcessDefByDefId(String processDefinitionId) {
/* 189 */     DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)BpmContext.getProcessDef(processDefinitionId);
/* 190 */     if (processDef != null) {
/* 191 */       return processDef;
/*     */     }
/*     */     
/* 194 */     processDef = getProcessDefByDefId(processDefinitionId, Boolean.valueOf(true));
/* 195 */     if (processDef == null) return null;
/*     */     
/* 197 */     BpmContext.addProcessDef(processDefinitionId, (BpmProcessDef)processDef);
/*     */     
/* 199 */     return processDef;
/*     */   }
/*     */   
/*     */   private synchronized DefaultBpmProcessDef getProcessDefByDefId(String processDefinitionId, Boolean isCache) {
/* 203 */     DefaultBpmProcessDef bpmProcessDef = null;
/* 204 */     if (isCache.booleanValue()) {
/*     */       try {
/* 206 */         bpmProcessDef = (DefaultBpmProcessDef)this.iCache.getByKey("procdef_" + processDefinitionId);
/* 207 */       } catch (SerializeException e) {
/* 208 */         this.iCache.delByKey("procdef_" + processDefinitionId);
/* 209 */         bpmProcessDef = null;
/*     */       } 
/*     */     }
/*     */     
/* 213 */     if (bpmProcessDef != null) return bpmProcessDef;
/*     */     
/* 215 */     BpmDefinition bpmDef = (BpmDefinition)this.bpmDefinitionManager.get(processDefinitionId);
/* 216 */     if (bpmDef == null) {
/* 217 */       throw new BusinessException(BpmStatusCode.DEF_LOST);
/*     */     }
/*     */     
/* 220 */     Assert.notEmpty(bpmDef.getActDefId(), "BpmDefinition actDefId cannot empty ！ 可能原因为：尚未完成流程设计，请检查流程定义 ", new Object[0]);
/*     */     
/* 222 */     bpmProcessDef = initBpmProcessDef(bpmDef);
/*     */     
/* 224 */     List<IExtendTaskAction> taskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 225 */     for (IExtendTaskAction extendTaskAction : taskActions) {
/* 226 */       extendTaskAction.parseMultInstContainNode((BpmProcessDef)bpmProcessDef);
/*     */     }
/* 228 */     if (isCache.booleanValue()) this.iCache.add("procdef_" + processDefinitionId, bpmProcessDef);
/*     */     
/* 230 */     return bpmProcessDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBpmProcessDef initBpmProcessDef(BpmDefinition bpmDef) {
/* 242 */     if (bpmDef == null) return null;
/*     */     
/* 244 */     JSONObject bpmDefSetting = JSONObject.parseObject(bpmDef.getDefSetting());
/* 245 */     if (bpmDefSetting == null) {
/* 246 */       throw new BusinessException("流程配置 bpmDefSetting 丢失！请检查流程定义 " + bpmDef.getKey());
/*     */     }
/*     */     
/* 249 */     DefaultBpmProcessDef bpmProcessDef = new DefaultBpmProcessDef();
/* 250 */     bpmProcessDef.setProcessDefinitionId(bpmDef.getId());
/* 251 */     bpmProcessDef.setName(bpmDef.getName());
/* 252 */     bpmProcessDef.setDefKey(bpmDef.getKey());
/*     */ 
/*     */     
/* 255 */     this.bpmDefinitionManager.clearBpmnModelCache(bpmDef.getActDefId());
/*     */     
/* 257 */     BpmnModel bpmnModel = this.repositoryService.getBpmnModel(bpmDef.getActDefId());
/* 258 */     Process process = bpmnModel.getProcesses().get(0);
/* 259 */     this.bpmDefNodeHandler.setProcessDefNodes(bpmProcessDef, process.getFlowElements());
/*     */     
/* 261 */     BpmProcessDefParser.processDefParser(bpmProcessDef, bpmDefSetting);
/* 262 */     bpmProcessDef.setJson(bpmDefSetting);
/* 263 */     return bpmProcessDef;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getNodesByType(String processDefinitionId, NodeType nodeType) {
/* 269 */     DefaultBpmProcessDef processDef = getProcessDefByDefId(processDefinitionId);
/* 270 */     List<BpmNodeDef> list = processDef.getBpmnNodeDefs();
/* 271 */     List<BpmNodeDef> rtnList = new ArrayList<>();
/* 272 */     for (BpmNodeDef nodeDef : list) {
/* 273 */       if (nodeDef.getType().equals(nodeType)) {
/* 274 */         rtnList.add(nodeDef);
/*     */       }
/*     */     } 
/* 277 */     return rtnList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getAllNodeDef(String processDefinitionId) {
/* 283 */     List<BpmNodeDef> bpmNodeDefs = getNodeDefs(processDefinitionId);
/* 284 */     List<BpmNodeDef> rtnList = new ArrayList<>();
/* 285 */     getBpmNodeDefs(bpmNodeDefs, rtnList);
/* 286 */     return rtnList;
/*     */   }
/*     */ 
/*     */   
/*     */   private void getBpmNodeDefs(List<BpmNodeDef> bpmNodeDefs, List<BpmNodeDef> rtnList) {
/* 291 */     for (BpmNodeDef def : bpmNodeDefs) {
/* 292 */       rtnList.add(def);
/* 293 */       if (!NodeType.SUBPROCESS.equals(def.getType()))
/* 294 */         continue;  SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)def;
/* 295 */       BpmProcessDef processDef = subProcessNodeDef.getChildBpmProcessDef();
/* 296 */       if (processDef == null)
/* 297 */         continue;  List<BpmNodeDef> subBpmNodeDefs = processDef.getBpmnNodeDefs();
/* 298 */       getBpmNodeDefs(subBpmNodeDefs, rtnList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getSignUserNode(String processDefinitionId) {
/* 309 */     List<BpmNodeDef> bpmNodeDefs = getAllNodeDef(processDefinitionId);
/* 310 */     List<BpmNodeDef> rtnList = new ArrayList<>();
/* 311 */     for (BpmNodeDef bnd : bpmNodeDefs) {
/* 312 */       if (bnd.getType().equals(NodeType.START) || bnd.getType().equals(NodeType.SIGNTASK) || bnd.getType().equals(NodeType.USERTASK)) {
/* 313 */         rtnList.add(bnd);
/*     */       }
/*     */     } 
/* 316 */     return rtnList;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBpmDefinition getDefinitionById(String defId) {
/* 321 */     return (IBpmDefinition)this.bpmDefinitionManager.get(defId);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBpmDefinition getDefinitionByKey(String defKey) {
/* 326 */     return (IBpmDefinition)this.bpmDefinitionManager.getByKey(defKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmProcessDef initBpmProcessDef(IBpmDefinition bpmDef) {
/*     */     try {
/* 332 */       DefaultBpmProcessDef def = initBpmProcessDef((BpmDefinition)bpmDef);
/* 333 */       BpmContext.cleanTread();
/* 334 */       this.iCache.delByKey("procdef_" + bpmDef.getId());
/* 335 */       return (BpmProcessDef)def;
/* 336 */     } catch (Exception e) {
/* 337 */       throw new BusinessException(e.getMessage(), BpmStatusCode.PARSER_FLOW_ERROR, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBpmDefinition getDefinitionByActDefId(String actDefId) {
/* 344 */     return (IBpmDefinition)this.bpmDefinitionManager.getDefinitionByActDefId(actDefId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/service/DefaultBpmProcessDefService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */