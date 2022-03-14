/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.sys.api.model.SysIdentity;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicTask
/*     */   extends BaseModel
/*     */ {
/*     */   public static final String status_runtime = "runtime";
/*     */   public static final String status_completed = "completed";
/*     */   protected String instId;
/*     */   protected String actExecutionId;
/*     */   protected String nodeId;
/*     */   protected Integer currentIndex;
/*     */   protected String identityNode;
/*  54 */   protected String status = "runtime";
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isParallel = true;
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected Integer ammount = Integer.valueOf(0);
/*     */   
/*     */   protected String taskId;
/*     */   
/*     */   public DynamicTask(IBpmTask task, List<SysIdentity> identitys, boolean isParallel) {
/*  67 */     this.instId = task.getInstId();
/*  68 */     this.actExecutionId = task.getActExecutionId();
/*  69 */     this.nodeId = task.getNodeId();
/*  70 */     this.currentIndex = Integer.valueOf(0);
/*  71 */     this.ammount = Integer.valueOf(identitys.size());
/*  72 */     this.isParallel = isParallel;
/*     */     
/*  74 */     List<DynamicTaskIdentitys> identityNodes = new ArrayList<>();
/*     */     
/*  76 */     for (int i = 0; i < identitys.size(); i++) {
/*  77 */       List<SysIdentity> list = new ArrayList<>(1);
/*  78 */       list.add(identitys.get(i));
/*     */       
/*  80 */       identityNodes.add(new DynamicTaskIdentitys(String.format("%s-%d", new Object[] { task.getName(), Integer.valueOf(i + 1) }), list));
/*     */     } 
/*     */     
/*  83 */     this.identityNode = JSON.toJSONString(identityNodes);
/*     */   }
/*     */   
/*     */   public DynamicTask(IBpmTask task) {
/*  87 */     this.instId = task.getInstId();
/*  88 */     this.actExecutionId = task.getActExecutionId();
/*  89 */     this.nodeId = task.getNodeId();
/*  90 */     this.currentIndex = Integer.valueOf(0);
/*  91 */     this.isParallel = true;
/*  92 */     this.taskId = task.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicTask() {}
/*     */ 
/*     */   
/*     */   public DynamicTaskIdentitys loadCurrentTaskIdentitys() {
/* 100 */     return loadAllTaskIdentitys().get(this.currentIndex.intValue());
/*     */   }
/*     */   
/*     */   public List<DynamicTaskIdentitys> loadAllTaskIdentitys() {
/* 104 */     if (StringUtil.isEmpty(this.identityNode)) return null;
/*     */     
/* 106 */     List<DynamicTaskIdentitys> identitys = JSONArray.parseArray(this.identityNode, DynamicTaskIdentitys.class);
/* 107 */     if (identitys.size() != this.ammount.intValue()) {
/* 108 */       throw new BusinessException("动态任务分配候选人失败，候选人数与统计数不同！");
/*     */     }
/* 110 */     return identitys;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInstId(String instId) {
/* 115 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 124 */     return this.instId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActExecutionId(String actExecutionId) {
/* 129 */     this.actExecutionId = actExecutionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActExecutionId() {
/* 138 */     return this.actExecutionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 143 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 152 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentIndex(Integer currentIndex) {
/* 157 */     this.currentIndex = currentIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCurrentIndex() {
/* 166 */     return this.currentIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdentityNode(String identityNode) {
/* 171 */     this.identityNode = identityNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentityNode() {
/* 180 */     return this.identityNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/* 185 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 194 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsParallel(boolean isParallel) {
/* 199 */     this.isParallel = isParallel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIsParallel() {
/* 208 */     return this.isParallel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAmmount(Integer ammount) {
/* 213 */     this.ammount = ammount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getAmmount() {
/* 222 */     return this.ammount;
/*     */   }
/*     */   
/*     */   public String getTaskId() {
/* 226 */     return this.taskId;
/*     */   }
/*     */   
/*     */   public void setTaskId(String taskId) {
/* 230 */     this.taskId = taskId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/model/DynamicTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */