/*     */ package com.dstz.bpm.plugin.core.model;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
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
/*     */ public class DynamicTask
/*     */   extends BaseModel
/*     */ {
/*     */   public static final String status_runtime = "runtime";
/*     */   public static final String status_complated = "complated";
/*     */   protected String instId;
/*     */   protected String actExecutionId;
/*     */   protected String nodeId;
/*     */   protected Integer currentIndex;
/*     */   protected String identityNode;
/*  49 */   protected String status = "runtime";
/*     */ 
/*     */   
/*     */   protected boolean isParallel;
/*     */ 
/*     */   
/*     */   protected Integer ammount;
/*     */ 
/*     */ 
/*     */   
/*     */   public DynamicTask(IBpmTask task, List<SysIdentity> identitys, boolean isParallel) {
/*  60 */     this.instId = task.getInstId();
/*  61 */     this.actExecutionId = task.getActExecutionId();
/*  62 */     this.nodeId = task.getNodeId();
/*  63 */     this.currentIndex = Integer.valueOf(0);
/*  64 */     this.ammount = Integer.valueOf(identitys.size());
/*  65 */     this.isParallel = isParallel;
/*     */     
/*  67 */     List<DynamicTaskIdentitys> identityNodes = new ArrayList<>();
/*     */     
/*  69 */     for (int i = 0; i < identitys.size(); i++) {
/*  70 */       List<SysIdentity> list = new ArrayList<>(1);
/*  71 */       list.add(identitys.get(i));
/*     */       
/*  73 */       identityNodes.add(new DynamicTaskIdentitys(String.format("%s-%d", new Object[] { task.getName(), Integer.valueOf(i + 1) }), list));
/*     */     } 
/*     */     
/*  76 */     this.identityNode = JSON.toJSONString(identityNodes);
/*     */   }
/*     */   public DynamicTask(IBpmTask task) {
/*  79 */     this.instId = task.getInstId();
/*  80 */     this.actExecutionId = task.getActExecutionId();
/*  81 */     this.nodeId = task.getNodeId();
/*  82 */     this.currentIndex = Integer.valueOf(0);
/*  83 */     this.isParallel = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicTask() {}
/*     */ 
/*     */   
/*     */   public DynamicTaskIdentitys loadCurrentTaskIdentitys() {
/*  91 */     return loadAllTaskIdentitys().get(this.currentIndex.intValue());
/*     */   }
/*     */   
/*     */   public List<DynamicTaskIdentitys> loadAllTaskIdentitys() {
/*  95 */     if (StringUtil.isEmpty(this.identityNode)) return null;
/*     */     
/*  97 */     List<DynamicTaskIdentitys> identitys = JSONArray.parseArray(this.identityNode, DynamicTaskIdentitys.class);
/*  98 */     if (identitys.size() != this.ammount.intValue()) {
/*  99 */       throw new BusinessException("动态任务分配候选人失败，候选人数与统计数不同！");
/*     */     }
/* 101 */     return identitys;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInstId(String instId) {
/* 107 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/* 115 */     return this.instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActExecutionId(String actExecutionId) {
/* 122 */     this.actExecutionId = actExecutionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActExecutionId() {
/* 130 */     return this.actExecutionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeId(String nodeId) {
/* 137 */     this.nodeId = nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNodeId() {
/* 145 */     return this.nodeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentIndex(Integer currentIndex) {
/* 152 */     this.currentIndex = currentIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCurrentIndex() {
/* 160 */     return this.currentIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentityNode(String identityNode) {
/* 167 */     this.identityNode = identityNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentityNode() {
/* 175 */     return this.identityNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/* 182 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 190 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsParallel(boolean isParallel) {
/* 197 */     this.isParallel = isParallel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIsParallel() {
/* 205 */     return this.isParallel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAmmount(Integer ammount) {
/* 212 */     this.ammount = ammount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getAmmount() {
/* 220 */     return this.ammount;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/model/DynamicTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */