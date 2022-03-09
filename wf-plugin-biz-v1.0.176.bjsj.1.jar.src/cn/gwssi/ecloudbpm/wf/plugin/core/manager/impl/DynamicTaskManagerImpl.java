/*     */ package cn.gwssi.ecloudbpm.wf.plugin.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.NodeType;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskStackManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskStack;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.dao.DynamicTaskDao;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.DynamicTaskManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.DynamicTask;
/*     */ import cn.gwssi.ecloudframework.base.api.query.Direction;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultFieldSort;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultPage;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service("dynamicTaskManager")
/*     */ public class DynamicTaskManagerImpl
/*     */   extends BaseManager<String, DynamicTask>
/*     */   implements DynamicTaskManager
/*     */ {
/*     */   @Resource
/*     */   DynamicTaskDao dynamicTaskDao;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmTaskStackManager bpmTaskStackManager;
/*     */   
/*     */   public DynamicTask getByStatus(String nodeId, String actExecutionId, String status) {
/*  47 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  48 */     defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/*  49 */     defaultQueryFilter.addFilter("act_execution_id_", actExecutionId, QueryOP.EQUAL);
/*  50 */     defaultQueryFilter.addFilter("status_", status, QueryOP.EQUAL);
/*  51 */     defaultQueryFilter.addFieldSort("create_time_", "desc");
/*  52 */     return (DynamicTask)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DynamicTask getDynamicTaskSettingByInstanceId(String instanceId, String nodeId) {
/*  58 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  59 */     defaultQueryFilter.addFilter("inst_id_", instanceId, QueryOP.EQUAL);
/*  60 */     defaultQueryFilter.addFilter("node_id_", nodeId, QueryOP.EQUAL);
/*  61 */     defaultQueryFilter.addFieldSort("status_", "desc");
/*  62 */     defaultQueryFilter.addFieldSort("ammount_", "desc");
/*  63 */     return (DynamicTask)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskStack> getTaskStackByInstIdAndNodeId(String instId, String nodeId) {
/*  68 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  69 */     defaultQueryFilter.addFilter("inst_id_", instId, QueryOP.EQUAL);
/*  70 */     defaultQueryFilter.addFilter("task_key_", nodeId, QueryOP.EQUAL);
/*  71 */     defaultQueryFilter.addFilter("approver_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  72 */     DefaultPage page = (DefaultPage)defaultQueryFilter.getPage();
/*  73 */     page.getOrders().add(new DefaultFieldSort("approver_time_", Direction.DESC));
/*  74 */     List<BpmTaskOpinion> bpmTaskOpinions = this.bpmTaskOpinionManager.query((QueryFilter)defaultQueryFilter);
/*  75 */     if (CollectionUtil.isNotEmpty(bpmTaskOpinions)) {
/*  76 */       BpmTaskOpinion taskOpinion = bpmTaskOpinions.get(0);
/*  77 */       if (!StringUtils.equals(taskOpinion.getTaskId(), "0")) {
/*  78 */         BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskOpinion.getTaskId());
/*  79 */         return getTaskStackByParentId(bpmTaskStack, new String[] { NodeType.USERTASK.getKey().toLowerCase(), NodeType.CALLACTIVITY.getKey().toLowerCase() });
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<Map> getDynamicTaskByInstIdAndNodeId(String instId, String nodeId) {
/*  87 */     List<BpmTaskStack> bpmTaskStacks = getTaskStackByInstIdAndNodeId(instId, nodeId);
/*  88 */     String nextNodeId = "";
/*  89 */     for (BpmTaskStack bpmTaskStack : bpmTaskStacks) {
/*  90 */       String dynamixNodeId = bpmTaskStack.getNodeId();
/*  91 */       if (StringUtil.isNotEmpty(nextNodeId) && !StringUtils.equals(nextNodeId, dynamixNodeId)) {
/*  92 */         return Collections.emptyList();
/*     */       }
/*  94 */       nextNodeId = dynamixNodeId;
/*     */     } 
/*     */     
/*  97 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/*  98 */     defaultQueryFilter.addFilter("inst_id_", instId, QueryOP.EQUAL);
/*  99 */     defaultQueryFilter.addFilter("node_id_", nextNodeId, QueryOP.EQUAL);
/* 100 */     defaultQueryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
/* 101 */     DynamicTask dynamicTask = (DynamicTask)queryOne((QueryFilter)defaultQueryFilter);
/* 102 */     List<Map> taskMap = new ArrayList<>();
/* 103 */     if (dynamicTask != null) {
/* 104 */       List<JSONObject> identitys = JSONObject.parseArray(dynamicTask.getIdentityNode(), JSONObject.class);
/* 105 */       for (int i = 0; i < identitys.size(); i++) {
/* 106 */         Map<Object, Object> task = new HashMap<>();
/* 107 */         JSONObject user = (JSONObject)((JSONArray)((JSONObject)identitys.get(i)).get("nodeIdentitys")).get(0);
/* 108 */         task.put("userId", user.get("id"));
/* 109 */         task.put("userName", user.get("name"));
/* 110 */         task.put("taskName", ((JSONObject)identitys.get(i)).get("taskName"));
/* 111 */         if (i < bpmTaskStacks.size()) {
/* 112 */           task.put("taskId", ((BpmTaskStack)bpmTaskStacks.get(i)).getTaskId());
/* 113 */           task.put("nodeType", ((BpmTaskStack)bpmTaskStacks.get(i)).getNodeType());
/* 114 */           task.put("nodeId", ((BpmTaskStack)bpmTaskStacks.get(i)).getNodeId());
/*     */         } 
/* 116 */         taskMap.add(task);
/*     */       } 
/*     */     } 
/* 119 */     return taskMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BpmTaskStack> getTaskStackByParentId(BpmTaskStack taskStack, String... taskType) {
/* 130 */     List<BpmTaskStack> bpmTaskStacks = getTaskStackByPrevParentId(taskStack.getId());
/* 131 */     List<BpmTaskStack> returnBpmTaskStacks = new ArrayList<>();
/* 132 */     bpmTaskStacks.forEach(bpmTaskStack -> {
/*     */           if (CollectionUtil.contains(Arrays.asList(taskType), bpmTaskStack.getNodeType().toLowerCase())) {
/*     */             if (StringUtils.equals(bpmTaskStack.getNodeType().toLowerCase(), NodeType.CALLACTIVITY.getKey().toLowerCase())) {
/*     */               List<BpmTaskStack> callActivityUserTasks = getTaskStackByParentId(bpmTaskStack, new String[] { NodeType.USERTASK.getKey().toLowerCase() });
/*     */               
/*     */               if (callActivityUserTasks.size() > 0) {
/*     */                 for (BpmTaskStack callActivityUserTask : callActivityUserTasks) {
/*     */                   if (!StringUtils.equals(callActivityUserTask.getInstId(), bpmTaskStack.getInstId())) {
/*     */                     bpmTaskStack.setTaskId(callActivityUserTask.getTaskId());
/*     */                     returnBpmTaskStacks.add(bpmTaskStack);
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             } else {
/*     */               returnBpmTaskStacks.add(bpmTaskStack);
/*     */             } 
/*     */           } else {
/*     */             returnBpmTaskStacks.addAll(getTaskStackByParentId(bpmTaskStack, taskType));
/*     */           } 
/*     */         });
/* 152 */     return returnBpmTaskStacks;
/*     */   }
/*     */   
/*     */   private List<BpmTaskStack> getTaskStackByPrevParentId(String parentId) {
/* 156 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 157 */     defaultQueryFilter.addFilter("parent_id_", parentId, QueryOP.EQUAL);
/* 158 */     return this.bpmTaskStackManager.query((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByInstId(String instId) {
/* 163 */     this.dynamicTaskDao.removeByInstId(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DynamicTask> getByTaskId(String taskId) {
/* 168 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 169 */     defaultQueryFilter.addFilter("task_id_", taskId, QueryOP.EQUAL);
/* 170 */     return this.dynamicTaskDao.query((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEndByInstId(String instId) {
/* 175 */     this.dynamicTaskDao.updateEndByInstId(instId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/DynamicTaskManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */