/*     */ package com.dstz.bpm.act.cache;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.event.BpmActivitiDefCache;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import cn.hutool.core.util.ObjectUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
/*     */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ActivitiDefCache
/*     */   implements DeploymentCache<ProcessDefinitionEntity>, BpmActivitiDefCache
/*     */ {
/*     */   public void clearLocal() {
/*  26 */     clearProcessCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static ActivitiDefCache activitiCache = null;
/*     */   
/*     */   public static void clearByDefId(String actDefId) {
/*  37 */     if (activitiCache == null) {
/*  38 */       activitiCache = (ActivitiDefCache)AppUtil.getBean(ActivitiDefCache.class);
/*     */     }
/*  40 */     activitiCache.clearProcessDefinitionEntity(actDefId);
/*  41 */     activitiCache.clearProcessCache();
/*     */   }
/*     */   
/*  44 */   private ThreadLocal<Map<String, ProcessDefinitionEntity>> processDefinitionCacheLocal = new ThreadLocal<>();
/*     */   
/*     */   private void clearProcessDefinitionEntity(String defId) {
/*  47 */     remove(defId);
/*  48 */     this.processDefinitionCacheLocal.remove();
/*     */   } @Resource
/*     */   ICache iCache;
/*     */   private void clearProcessCache() {
/*  52 */     this.processDefinitionCacheLocal.remove();
/*     */   }
/*     */   
/*     */   private void setThreadLocalDef(ProcessDefinitionEntity processEnt) {
/*  56 */     if (this.processDefinitionCacheLocal.get() == null) {
/*  57 */       Map<String, ProcessDefinitionEntity> map = new HashMap<>();
/*  58 */       map.put(processEnt.getId(), processEnt);
/*  59 */       this.processDefinitionCacheLocal.set(map);
/*     */     } else {
/*  61 */       Map<String, ProcessDefinitionEntity> map = this.processDefinitionCacheLocal.get();
/*  62 */       map.put(processEnt.getId(), processEnt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ProcessDefinitionEntity getThreadLocalDef(String processDefinitionId) {
/*  68 */     if (this.processDefinitionCacheLocal.get() == null) {
/*  69 */       return null;
/*     */     }
/*     */     
/*  72 */     Map<String, ProcessDefinitionEntity> map = this.processDefinitionCacheLocal.get();
/*  73 */     if (!map.containsKey(processDefinitionId)) {
/*  74 */       return null;
/*     */     }
/*     */     
/*  77 */     return map.get(processDefinitionId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProcessDefinitionEntity get(String id) {
/*  86 */     ProcessDefinitionEntity p = getThreadLocalDef(id);
/*     */     
/*  88 */     if (p != null) return p;
/*     */ 
/*     */     
/*  91 */     ProcessDefinitionEntity ent = (ProcessDefinitionEntity)this.iCache.getByKey(id);
/*  92 */     if (ent == null) return null;
/*     */     
/*  94 */     ProcessDefinitionEntity cloneEnt = null;
/*     */     
/*     */     try {
/*  97 */       cloneEnt = (ProcessDefinitionEntity)ObjectUtil.cloneByStream(ent);
/*  98 */     } catch (Exception e) {
/*  99 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 102 */     setThreadLocalDef(cloneEnt);
/* 103 */     return cloneEnt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String id, ProcessDefinitionEntity object) {
/* 109 */     this.iCache.add(id, object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String id) {
/* 115 */     this.iCache.delByKey(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 121 */     this.iCache.clearAll();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/cache/ActivitiDefCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */