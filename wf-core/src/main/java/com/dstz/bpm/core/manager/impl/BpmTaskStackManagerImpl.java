/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.model.inst.BpmExecutionStack;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.core.dao.BpmTaskStackDao;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.model.BpmTaskStack;
/*     */ import com.dstz.base.api.query.FieldLogic;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.query.WhereClause;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.datasource.DbContextHolder;
/*     */ import com.dstz.base.db.model.query.DefaultQueryField;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("bpmExecutionStackManager")
/*     */ public class BpmTaskStackManagerImpl
/*     */   extends BaseManager<String, BpmTaskStack>
/*     */   implements BpmTaskStackManager
/*     */ {
/*     */   @Resource
/*     */   BpmTaskStackDao bpmTaskStackDao;
/*     */   
/*     */   public BpmTaskStack getByTaskId(String taskId) {
/*  41 */     return this.bpmTaskStackDao.getByTaskId(taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmTaskStack createStackByTask(IBpmTask task, BpmExecutionStack parentStack) {
/*  46 */     BpmTaskStack stack = new BpmTaskStack();
/*  47 */     String id = IdUtil.getSuid();
/*  48 */     stack.setId(id);
/*  49 */     stack.setNodeId(task.getNodeId());
/*  50 */     stack.setNodeName(task.getName());
/*  51 */     stack.setTaskId(task.getId());
/*     */     
/*  53 */     stack.setStartTime(new Date());
/*  54 */     stack.setInstId(task.getInstId());
/*  55 */     stack.setNodeType("userTask");
/*  56 */     stack.setActionName(BpmContext.getActionModel());
/*     */     
/*  58 */     if (parentStack == null) {
/*  59 */       stack.setParentId("0");
/*     */     } else {
/*  61 */       stack.setParentId(parentStack.getId());
/*     */     } 
/*     */     
/*  64 */     stack.setTrace(BpmContext.popMulInstOpTrace());
/*     */     
/*  66 */     create(stack);
/*     */     
/*  68 */     return stack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void removeByInstanceId(String instId) {
/*  77 */     this.bpmTaskStackDao.removeByInstanceId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskStack> getByInstanceId(String instId) {
/*  82 */     return this.bpmTaskStackDao.getByInstanceId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskStack> getByInstIdAndTrace(String instId, String trace) {
/*  87 */     List<BpmTaskStack> stacks = this.bpmTaskStackDao.getByInstanceId(instId);
/*  88 */     if (StringUtil.isEmpty(trace)) {
/*  89 */       return stacks;
/*     */     }
/*  91 */     List<BpmTaskStack> ss = new ArrayList<>();
/*  92 */     stacks.forEach(s -> {
/*     */           if (StringUtil.isEmpty(s.getTrace()) || trace.startsWith(s.getTrace())) {
/*     */             ss.add(s);
/*     */           }
/*     */         });
/*  97 */     return ss;
/*     */   }
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public List<BpmTaskStack> getTaskStackByIteration(QueryFilter queryFilter) {
/*     */     try {
/* 104 */       if (StringUtils.equals(DbContextHolder.getDbType(), "mysql") || 
/* 105 */         StringUtils.equals(DbContextHolder.getDbType(), "drds") || 
/* 106 */         StringUtils.equals(DbContextHolder.getDbType(), "highgo")) {
/* 107 */         String taskId = (String)queryFilter.getParams().get("taskId");
/* 108 */         String prior = (String)queryFilter.getParams().get("prior");
/* 109 */         String orderBySql = queryFilter.getOrderBySql();
/* 110 */         FieldLogic fieldLogic = queryFilter.getFieldLogic();
/* 111 */         StringBuffer whereSql = new StringBuffer(" 1=1 ");
/* 112 */         fieldLogic.getWhereClauses().forEach(whereClause -> {
/*     */               if (whereClause instanceof DefaultQueryField) {
/*     */                 DefaultQueryField field = (DefaultQueryField)whereClause;
/*     */ 
/*     */                 
/*     */                 Object value = field.getValue();
/*     */ 
/*     */                 
/*     */                 if (field.getCompare() == QueryOP.NOTNULL) {
/*     */                   whereSql.append(" and ").append(field.getField()).append(" is not null ");
/*     */                 } else if (field.getCompare() == QueryOP.IS_NULL) {
/*     */                   whereSql.append(" and ").append(field.getField()).append(" is null ");
/*     */                 } else if (value instanceof String) {
/*     */                   whereSql.append(" and ").append(field.getField()).append(field.getCompare().op()).append("'").append(field.getValue()).append("'");
/*     */                 } else {
/*     */                   whereSql.append(" and ").append(field.getField()).append(field.getCompare().op()).append(field.getValue());
/*     */                 } 
/*     */               } 
/*     */             });
/*     */         
/* 132 */         if (StringUtils.equals(DbContextHolder.getDbType(), "highgo")) {
/* 133 */           return this.bpmTaskStackDao.getTaskStackByIterationHighGo(taskId, prior, whereSql.toString(), orderBySql);
/*     */         }
/* 135 */         return this.bpmTaskStackDao.getTaskStackByIterationMysql(taskId, prior, whereSql.toString(), orderBySql);
/*     */       } 
/* 137 */       return this.bpmTaskStackDao.getTaskStackByIteration(queryFilter);
/* 138 */     } catch (Exception e) {
/* 139 */       e.printStackTrace();
/*     */       
/* 141 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateStackEndByInstId(String instId) {
/* 147 */     this.bpmTaskStackDao.updateStackEndByInstId(instId, new Date());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmTaskStackManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */