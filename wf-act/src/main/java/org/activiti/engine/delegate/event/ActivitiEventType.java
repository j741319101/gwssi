/*     */ package org.activiti.engine.delegate.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.activiti.engine.ActivitiIllegalArgumentException;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ActivitiEventType
/*     */ {
/*  34 */   ENTITY_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   ENTITY_INITIALIZED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   ENTITY_UPDATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   ENTITY_DELETED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   ENTITY_SUSPENDED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   ENTITY_ACTIVATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   TIMER_FIRED,
/*     */ 
/*     */ 
/*     */   
/*  69 */   TIMER_REMIND,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   JOB_CANCELED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   JOB_EXECUTION_SUCCESS,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   JOB_EXECUTION_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   JOB_RETRIES_DECREMENTED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   CUSTOM,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   ENGINE_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   ENGINE_CLOSED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   ACTIVITY_STARTED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   ACTIVITY_COMPLETED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   ACTIVITY_CANCELLED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   ACTIVITY_SIGNALED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   ACTIVITY_COMPENSATE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   ACTIVITY_MESSAGE_RECEIVED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   ACTIVITY_ERROR_RECEIVED,
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
/* 157 */   HISTORIC_ACTIVITY_INSTANCE_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   HISTORIC_ACTIVITY_INSTANCE_ENDED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   SEQUENCEFLOW_TAKEN,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   UNCAUGHT_BPMN_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   VARIABLE_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   VARIABLE_UPDATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   VARIABLE_DELETED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   TASK_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   TASK_ASSIGNED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   TASK_COMPLETED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   PROCESS_STARTED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   PROCESS_COMPLETED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   PROCESS_COMPLETED_WITH_ERROR_END_EVENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   PROCESS_CANCELLED,
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
/* 246 */   HISTORIC_PROCESS_INSTANCE_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   HISTORIC_PROCESS_INSTANCE_ENDED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   MEMBERSHIP_CREATED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   MEMBERSHIP_DELETED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   MEMBERSHIPS_DELETED;
/*     */   static {
/* 275 */     EMPTY_ARRAY = new ActivitiEventType[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static final ActivitiEventType[] EMPTY_ARRAY;
/*     */ 
/*     */   
/*     */   public static ActivitiEventType[] getTypesFromString(String string) {
/* 283 */     List<ActivitiEventType> result = new ArrayList<>();
/* 284 */     if (string != null && !string.isEmpty()) {
/* 285 */       String[] split = StringUtils.split(string, ",");
/* 286 */       for (String typeName : split) {
/* 287 */         boolean found = false;
/* 288 */         for (ActivitiEventType type : values()) {
/* 289 */           if (typeName.equals(type.name())) {
/* 290 */             result.add(type);
/* 291 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 295 */         if (!found) {
/* 296 */           throw new ActivitiIllegalArgumentException("Invalid event-type: " + typeName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 301 */     return result.<ActivitiEventType>toArray(EMPTY_ARRAY);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/org/activiti/engine/delegate/event/ActivitiEventType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */