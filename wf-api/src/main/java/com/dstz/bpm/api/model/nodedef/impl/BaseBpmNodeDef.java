/*     */ package com.dstz.bpm.api.model.nodedef.impl;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.button.ButtonFactory;
/*     */ import com.dstz.bpm.api.engine.plugin.context.BpmPluginContext;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.def.NodeInit;
/*     */ import com.dstz.bpm.api.model.def.NodeProperties;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */
import org.springframework.beans.BeanUtils;
/*     */ 
/*     */ 
/*     */ public class BaseBpmNodeDef
/*     */   implements BpmNodeDef
/*     */ {
/*     */   private static final long serialVersionUID = -2044846605763777657L;
/*     */   private String nodeId;
/*     */   private String name;
/*     */   private NodeType type;
/*     */   private BpmNodeDef parentBpmNodeDef;
/*  34 */   private List<BpmNodeDef> incomeNodes = new ArrayList<>();
/*  35 */   private List<BpmNodeDef> outcomeNodes = new ArrayList<>();
/*     */   
/*  37 */   private List<BpmPluginContext> bpmPluginContexts = new ArrayList<>();
/*  38 */   private Map<String, String> attrMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private BpmProcessDef bpmProcessDef;
/*     */ 
/*     */ 
/*     */   
/*     */   private BpmForm mobileForm;
/*     */ 
/*     */   
/*     */   private BpmForm form;
/*     */ 
/*     */   
/*  52 */   private List<NodeInit> nodeInits = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private NodeProperties nodeProperties = new NodeProperties();
/*     */   
/*  59 */   private List<Button> buttons = null;
/*     */   
/*  61 */   private List<Button> buttonList = null;
/*     */   
/*     */   public String getNodeId() {
/*  64 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/*  68 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  72 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  76 */     this.name = name;
/*     */   }
/*     */   
/*     */   public NodeType getType() {
/*  80 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(NodeType type) {
/*  84 */     this.type = type;
/*     */   }
/*     */   
/*     */   public List<BpmNodeDef> getIncomeNodes() {
/*  88 */     return this.incomeNodes;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getIncomeTaskNodes() {
/*  93 */     return getNodeDefList(this.incomeNodes, true, null, new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK });
/*     */   }
/*     */   
/*     */   public void setIncomeNodes(List<BpmNodeDef> incomeNodes) {
/*  97 */     this.incomeNodes = incomeNodes;
/*     */   }
/*     */   
/*     */   public List<BpmNodeDef> getOutcomeNodes() {
/* 101 */     return this.outcomeNodes;
/*     */   }
/*     */   
/*     */   public void setOutcomeNodes(List<BpmNodeDef> outcomeNodes) {
/* 105 */     this.outcomeNodes = outcomeNodes;
/*     */   }
/*     */   
/*     */   public List<BpmPluginContext> getBpmPluginContexts() {
/* 109 */     return this.bpmPluginContexts;
/*     */   }
/*     */   
/*     */   public void setBpmPluginContexts(List<BpmPluginContext> bpmPluginContexts) {
/* 113 */     Collections.sort(bpmPluginContexts);
/* 114 */     this.bpmPluginContexts = bpmPluginContexts;
/*     */   }
/*     */   
/*     */   public void setAttribute(String name, String value) {
/* 118 */     name = name.toLowerCase().trim();
/* 119 */     this.attrMap.put(name.toLowerCase(), value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAttribute(String name) {
/* 125 */     name = name.toLowerCase().trim();
/* 126 */     return this.attrMap.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIncomeNode(BpmNodeDef bpmNodeDef) {
/* 132 */     this.incomeNodes.add(bpmNodeDef);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutcomeNode(BpmNodeDef bpmNodeDef) {
/* 137 */     this.outcomeNodes.add(bpmNodeDef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmNodeDef getParentBpmNodeDef() {
/* 143 */     return this.parentBpmNodeDef;
/*     */   }
/*     */   
/*     */   public void setParentBpmNodeDef(BpmNodeDef parentBpmNodeDef) {
/* 147 */     this.parentBpmNodeDef = parentBpmNodeDef;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealPath() {
/* 152 */     String id = getNodeId();
/* 153 */     BpmNodeDef parent = getParentBpmNodeDef();
/* 154 */     while (parent != null) {
/* 155 */       id = parent.getNodeId() + "/" + id;
/* 156 */       parent = parent.getParentBpmNodeDef();
/*     */     } 
/* 158 */     return id;
/*     */   }
/*     */   
/*     */   public BpmProcessDef getBpmProcessDef() {
/* 162 */     return this.bpmProcessDef;
/*     */   }
/*     */   
/*     */   public void setBpmProcessDef(BpmProcessDef bpmProcessDef) {
/* 166 */     this.bpmProcessDef = bpmProcessDef;
/*     */   }
/*     */   
/*     */   public BpmProcessDef getRootProcessDef() {
/* 170 */     BpmProcessDef processDef = this.bpmProcessDef;
/* 171 */     BpmProcessDef parent = processDef.getParentProcessDef();
/* 172 */     while (parent != null) {
/* 173 */       processDef = parent;
/* 174 */       parent = parent.getParentProcessDef();
/*     */     } 
/* 176 */     return processDef;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getOutcomeTaskNodes() {
/* 182 */     return getNodeDefList(this.outcomeNodes, false, null, new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK, NodeType.END });
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getOutcomeTaskNodesIncludeCall() {
/* 187 */     return getNodeDefList(this.outcomeNodes, false, null, new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK, NodeType.END, NodeType.CALLACTIVITY });
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
/*     */ 
/*     */   
/*     */   private List<BpmNodeDef> getNodeDefList(List<BpmNodeDef> bpmNodeDefs, boolean isIn, BpmNodeDef activityNode, NodeType... nodeTypes) {
/* 201 */     if (nodeTypes == null || nodeTypes.length == 0) {
/* 202 */       nodeTypes = new NodeType[] { NodeType.USERTASK, NodeType.SIGNTASK };
/*     */     }
/*     */     
/* 205 */     List<BpmNodeDef> bpmNodeList = new ArrayList<>();
/* 206 */     for (BpmNodeDef def : bpmNodeDefs) {
/* 207 */       if (CollUtil.newArrayList((Object[])nodeTypes).contains(def.getType())) {
/* 208 */         if (activityNode != null && activityNode instanceof CallActivityNodeDef) {
/* 209 */           CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)activityNode;
/* 210 */           BpmNodeDef bpmNodeDef = new BaseBpmNodeDef();
/* 211 */           BeanUtils.copyProperties(def, bpmNodeDef);
/* 212 */           bpmNodeDef.setName(callActivityNodeDef.getName() + "-" + def.getName());
/* 213 */           bpmNodeDef.setNodeId(callActivityNodeDef.getNodeId() + "&" + def.getNodeId());
/* 214 */           bpmNodeList.add(bpmNodeDef); continue;
/* 215 */         }  if (activityNode != null && NodeType.INCLUSIVEGATEWAY.equals(activityNode.getType())) {
/* 216 */           BaseBpmNodeDef bpmNodeDef = new BaseBpmNodeDef();
/* 217 */           BeanUtils.copyProperties(def, bpmNodeDef);
/* 218 */           bpmNodeDef.setName(activityNode.getName() + "-" + def.getName());
/* 219 */           bpmNodeDef.setNodeId(def.getNodeId());
/* 220 */           bpmNodeDef.setType(activityNode.getType());
/* 221 */           List<BpmNodeDef> incomeNodes = new ArrayList<>();
/* 222 */           incomeNodes.add(activityNode);
/* 223 */           bpmNodeDef.setIncomeNodes(incomeNodes);
/* 224 */           bpmNodeList.add(bpmNodeDef); continue;
/*     */         } 
/* 226 */         bpmNodeList.add(def); continue;
/*     */       } 
/* 228 */       if (NodeType.SUBPROCESS.equals(def.getType())) {
/* 229 */         SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)def;
/*     */         
/* 231 */         BpmNodeDef startNodeDef = subProcessNodeDef.getChildBpmProcessDef().getStartEvent();
/* 232 */         if (isIn) {
/* 233 */           bpmNodeList.addAll(getNodeDefList(startNodeDef.getIncomeNodes(), isIn, null, nodeTypes)); continue;
/*     */         } 
/* 235 */         bpmNodeList.addAll(getNodeDefList(startNodeDef.getOutcomeNodes(), isIn, null, nodeTypes)); continue;
/*     */       } 
/* 237 */       if (NodeType.END.equals(def.getType()) && def.getParentBpmNodeDef() != null && NodeType.SUBPROCESS.equals(def.getParentBpmNodeDef().getType())) {
/* 238 */         SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef)def.getParentBpmNodeDef();
/* 239 */         if (isIn) {
/* 240 */           bpmNodeList.addAll(getNodeDefList(subProcessNodeDef.getIncomeNodes(), isIn, null, nodeTypes)); continue;
/*     */         } 
/* 242 */         bpmNodeList.addAll(getNodeDefList(subProcessNodeDef.getOutcomeNodes(), isIn, null, nodeTypes)); continue;
/*     */       } 
/* 244 */       if (NodeType.CALLACTIVITY.equals(def.getType())) {
/* 245 */         CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef)def;
/* 246 */         BpmProcessDefService defService = AppUtil.getImplInstanceArray(BpmProcessDefService.class).get(0);
/* 247 */         IBpmDefinition bpmDefinition = defService.getDefinitionByKey(callActivityNodeDef.getFlowKey());
/* 248 */         BpmNodeDef startNodeDef = defService.getStartEvent(bpmDefinition.getId());
/* 249 */         callActivityNodeDef.setFlowName(bpmDefinition.getName());
/* 250 */         if (isIn) {
/* 251 */           bpmNodeList.addAll(getNodeDefList(startNodeDef.getIncomeNodes(), isIn, callActivityNodeDef, nodeTypes)); continue;
/*     */         } 
/* 253 */         bpmNodeList.addAll(getNodeDefList(startNodeDef.getOutcomeNodes(), isIn, callActivityNodeDef, nodeTypes)); continue;
/*     */       } 
/* 255 */       if (NodeType.INCLUSIVEGATEWAY.equals(def.getType())) {
/* 256 */         if (isIn) {
/* 257 */           bpmNodeList.addAll(getNodeDefList(def.getIncomeNodes(), isIn, def, nodeTypes)); continue;
/*     */         } 
/* 259 */         bpmNodeList.addAll(getNodeDefList(def.getOutcomeNodes(), isIn, def, nodeTypes));
/*     */         continue;
/*     */       } 
/* 262 */       if (isIn) {
/* 263 */         bpmNodeList.addAll(getNodeDefList(def.getIncomeNodes(), isIn, null, nodeTypes)); continue;
/*     */       } 
/* 265 */       bpmNodeList.addAll(getNodeDefList(def.getOutcomeNodes(), isIn, null, nodeTypes));
/*     */     } 
/*     */ 
/*     */     
/* 269 */     return bpmNodeList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean includeSignTask) {
/* 274 */     List<BpmNodeDef> bpmNodeList = getInnerOutcomeTaskNodes(this.outcomeNodes, includeSignTask);
/* 275 */     return bpmNodeList;
/*     */   }
/*     */   
/*     */   private List<BpmNodeDef> getInnerOutcomeTaskNodes(List<BpmNodeDef> bpmNodeDefs, boolean includeSignTask) {
/* 279 */     List<BpmNodeDef> bpmNodeList = new ArrayList<>();
/* 280 */     for (BpmNodeDef def : bpmNodeDefs) {
/* 281 */       if (NodeType.USERTASK.equals(def.getType()) || (includeSignTask && NodeType.SIGNTASK.equals(def.getType()))) {
/* 282 */         bpmNodeList.add(def); continue;
/* 283 */       }  if (NodeType.SUBPROCESS.equals(def.getType()) || NodeType.CALLACTIVITY.equals(def.getType()) || NodeType.END.equals(def.getType())) {
/*     */         continue;
/*     */       }
/* 286 */       bpmNodeList.addAll(getInnerOutcomeTaskNodes(def.getOutcomeNodes(), includeSignTask));
/*     */     } 
/*     */     
/* 289 */     return bpmNodeList;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getPluginContext(Class<T> cls) {
/* 294 */     BpmPluginContext ctx = null;
/* 295 */     List<BpmPluginContext> list = this.bpmPluginContexts;
/* 296 */     for (BpmPluginContext context : list) {
/* 297 */       if (context.getClass().isAssignableFrom(cls)) {
/* 298 */         ctx = context;
/*     */         break;
/*     */       } 
/*     */     } 
/* 302 */     return (T)ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public NodeProperties getNodeProperties() {
/* 307 */     return this.nodeProperties;
/*     */   }
/*     */   
/*     */   public void setNodeProperties(NodeProperties nodeProperties) {
/* 311 */     this.nodeProperties = nodeProperties;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setButtons(List<Button> buttons) {
/* 316 */     this.buttons = buttons;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Button> getButtons() {
/* 321 */     if (this.buttons != null) return this.buttons; 
/* 322 */     if (this.buttonList != null) return this.buttonList;
/*     */     
/*     */     try {
/* 325 */       this.buttonList = ButtonFactory.generateButtons(this, true);
/* 326 */     } catch (Exception e) {
/* 327 */       throw new BusinessException(BpmStatusCode.TASK_ACTION_BTN_ERROR, e);
/*     */     } 
/* 329 */     return this.buttonList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefaultBtn() {
/* 338 */     if (this.buttons != null)
/* 339 */       return false; 
/* 340 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 345 */     StringBuilder sb = new StringBuilder();
/* 346 */     sb.append(getName()).append(":").append(getNodeId())
/* 347 */       .append(super.toString());
/* 348 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public BpmForm getMobileForm() {
/* 352 */     return this.mobileForm;
/*     */   }
/*     */   
/*     */   public void setMobileForm(BpmForm mobileForm) {
/* 356 */     this.mobileForm = mobileForm;
/*     */   }
/*     */   
/*     */   public BpmForm getForm() {
/* 360 */     return this.form;
/*     */   }
/*     */   
/*     */   public void setForm(BpmForm form) {
/* 364 */     this.form = form;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/nodedef/impl/BaseBpmNodeDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */