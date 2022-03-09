/*     */ package com.dstz.bpm.engine.action.cmd;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.action.cmd.BaseActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*     */ import com.dstz.bpm.api.exception.WorkFlowException;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultInstanceActionCmd
/*     */   extends BaseActionCmd
/*     */   implements InstanceActionCmd
/*     */ {
/*     */   protected ExecutionEntity executionEntity;
/*     */   
/*     */   public DefaultInstanceActionCmd(FlowRequestParam flowParam) {
/*  31 */     super(flowParam);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultInstanceActionCmd() {}
/*     */ 
/*     */   
/*     */   public String getFlowKey() {
/*  39 */     return getBpmDefinition().getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSubject() {
/*  44 */     return getBpmInstance().getSubject();
/*     */   }
/*     */ 
/*     */   
/*     */   public ExecutionEntity getExecutionEntity() {
/*  49 */     return this.executionEntity;
/*     */   }
/*     */   
/*     */   public void setExecutionEntity(ExecutionEntity executionEntity) {
/*  53 */     this.executionEntity = executionEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getVariable(String variableName) {
/*  58 */     if (this.executionEntity == null) {
/*  59 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/*  61 */     return this.executionEntity.getVariable(variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasVariable(String variableName) {
/*  66 */     if (this.executionEntity == null) {
/*  67 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/*  69 */     return this.executionEntity.hasVariable(variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeVariable(String variableName) {
/*  74 */     if (this.executionEntity == null) {
/*  75 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/*     */     
/*  78 */     this.executionEntity.removeVariable(variableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVariable(String name, Object value) {
/*  83 */     if (this.executionEntity == null) {
/*  84 */       throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
/*     */     }
/*  86 */     this.executionEntity.setVariable(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getVariables() {
/*  91 */     if (this.executionEntity == null) {
/*  92 */       return Collections.EMPTY_MAP;
/*     */     }
/*  94 */     return this.executionEntity.getVariables();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initSpecialParam(FlowRequestParam flowParam) {
/*  99 */     if (StringUtil.isEmpty(flowParam.getDefId()) && StringUtil.isNotEmpty(flowParam.getDefKey())) {
/* 100 */       BpmDefinition bpmDefinition = ((BpmDefinitionManager)AppUtil.getBean(BpmDefinitionManager.class)).getByKey(flowParam.getDefKey());
/* 101 */       if (bpmDefinition != null) {
/* 102 */         flowParam.setDefId(bpmDefinition.getId());
/* 103 */         setDefId(bpmDefinition.getId());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 110 */     if (this.executionEntity == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     return this.executionEntity.getActivityId();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/cmd/DefaultInstanceActionCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */