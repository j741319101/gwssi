package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.manager.BpmTaskStackManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.plugin.core.dao.DynamicTaskDao;
import com.dstz.bpm.plugin.core.manager.DynamicTaskManager;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.base.api.query.Direction;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultFieldSort;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("dynamicTaskManager")
public class DynamicTaskManagerImpl extends BaseManager<String, DynamicTask> implements DynamicTaskManager {
   @Resource
   DynamicTaskDao dynamicTaskDao;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmTaskStackManager bpmTaskStackManager;

   public DynamicTask getByStatus(String nodeId, String actExecutionId, String status) {
      QueryFilter query = new DefaultQueryFilter(true);
      query.addFilter("node_id_", nodeId, QueryOP.EQUAL);
      query.addFilter("act_execution_id_", actExecutionId, QueryOP.EQUAL);
      query.addFilter("status_", status, QueryOP.EQUAL);
      query.addFieldSort("create_time_", "desc");
      return (DynamicTask)this.queryOne(query);
   }

   public DynamicTask getDynamicTaskSettingByInstanceId(String instanceId, String nodeId) {
      QueryFilter query = new DefaultQueryFilter(true);
      query.addFilter("inst_id_", instanceId, QueryOP.EQUAL);
      query.addFilter("node_id_", nodeId, QueryOP.EQUAL);
      query.addFieldSort("status_", "desc");
      query.addFieldSort("ammount_", "desc");
      return (DynamicTask)this.queryOne(query);
   }

   public List<BpmTaskStack> getTaskStackByInstIdAndNodeId(String instId, String nodeId) {
      QueryFilter queryFilter = new DefaultQueryFilter();
      queryFilter.addFilter("inst_id_", instId, QueryOP.EQUAL);
      queryFilter.addFilter("task_key_", nodeId, QueryOP.EQUAL);
      queryFilter.addFilter("approver_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      DefaultPage page = (DefaultPage)queryFilter.getPage();
      page.getOrders().add(new DefaultFieldSort("approver_time_", Direction.DESC));
      List<BpmTaskOpinion> bpmTaskOpinions = this.bpmTaskOpinionManager.query(queryFilter);
      if (CollectionUtil.isNotEmpty(bpmTaskOpinions)) {
         BpmTaskOpinion taskOpinion = (BpmTaskOpinion)bpmTaskOpinions.get(0);
         if (!StringUtils.equals(taskOpinion.getTaskId(), "0")) {
            BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskOpinion.getTaskId());
            return this.getTaskStackByParentId(bpmTaskStack, NodeType.USERTASK.getKey().toLowerCase(), NodeType.CALLACTIVITY.getKey().toLowerCase());
         }
      }

      return Collections.emptyList();
   }

   public List<Map> getDynamicTaskByInstIdAndNodeId(String instId, String nodeId) {
      List<BpmTaskStack> bpmTaskStacks = this.getTaskStackByInstIdAndNodeId(instId, nodeId);
      String nextNodeId = "";

      String dynamixNodeId;
      for(Iterator var5 = bpmTaskStacks.iterator(); var5.hasNext(); nextNodeId = dynamixNodeId) {
         BpmTaskStack bpmTaskStack = (BpmTaskStack)var5.next();
         dynamixNodeId = bpmTaskStack.getNodeId();
         if (StringUtil.isNotEmpty(nextNodeId) && !StringUtils.equals(nextNodeId, dynamixNodeId)) {
            return Collections.emptyList();
         }
      }

      QueryFilter queryFilter = new DefaultQueryFilter(true);
      queryFilter.addFilter("inst_id_", instId, QueryOP.EQUAL);
      queryFilter.addFilter("node_id_", nextNodeId, QueryOP.EQUAL);
      queryFilter.addFilter("status_", "runtime", QueryOP.EQUAL);
      DynamicTask dynamicTask = (DynamicTask)this.queryOne(queryFilter);
      List<Map> taskMap = new ArrayList();
      if (dynamicTask != null) {
         List<JSONObject> identitys = JSONObject.parseArray(dynamicTask.getIdentityNode(), JSONObject.class);

         for(int i = 0; i < identitys.size(); ++i) {
            Map task = new HashMap();
            JSONObject user = (JSONObject)((JSONArray)((JSONObject)identitys.get(i)).get("nodeIdentitys")).get(0);
            task.put("userId", user.get("id"));
            task.put("userName", user.get("name"));
            task.put("taskName", ((JSONObject)identitys.get(i)).get("taskName"));
            if (i < bpmTaskStacks.size()) {
               task.put("taskId", ((BpmTaskStack)bpmTaskStacks.get(i)).getTaskId());
               task.put("nodeType", ((BpmTaskStack)bpmTaskStacks.get(i)).getNodeType());
               task.put("nodeId", ((BpmTaskStack)bpmTaskStacks.get(i)).getNodeId());
            }

            taskMap.add(task);
         }
      }

      return taskMap;
   }

   private List<BpmTaskStack> getTaskStackByParentId(BpmTaskStack taskStack, String... taskType) {
      List<BpmTaskStack> bpmTaskStacks = this.getTaskStackByPrevParentId(taskStack.getId());
      List<BpmTaskStack> returnBpmTaskStacks = new ArrayList();
      bpmTaskStacks.forEach((bpmTaskStack) -> {
         if (CollectionUtil.contains(Arrays.asList(taskType), bpmTaskStack.getNodeType().toLowerCase())) {
            if (StringUtils.equals(bpmTaskStack.getNodeType().toLowerCase(), NodeType.CALLACTIVITY.getKey().toLowerCase())) {
               List<BpmTaskStack> callActivityUserTasks = this.getTaskStackByParentId(bpmTaskStack, NodeType.USERTASK.getKey().toLowerCase());
               if (callActivityUserTasks.size() > 0) {
                  Iterator var5 = callActivityUserTasks.iterator();

                  while(var5.hasNext()) {
                     BpmTaskStack callActivityUserTask = (BpmTaskStack)var5.next();
                     if (!StringUtils.equals(callActivityUserTask.getInstId(), bpmTaskStack.getInstId())) {
                        bpmTaskStack.setTaskId(callActivityUserTask.getTaskId());
                        returnBpmTaskStacks.add(bpmTaskStack);
                     }
                  }
               }
            } else {
               returnBpmTaskStacks.add(bpmTaskStack);
            }
         } else {
            returnBpmTaskStacks.addAll(this.getTaskStackByParentId(bpmTaskStack, taskType));
         }

      });
      return returnBpmTaskStacks;
   }

   private List<BpmTaskStack> getTaskStackByPrevParentId(String parentId) {
      QueryFilter queryFilter = new DefaultQueryFilter();
      queryFilter.addFilter("parent_id_", parentId, QueryOP.EQUAL);
      return this.bpmTaskStackManager.query(queryFilter);
   }

   public void removeByInstId(String instId) {
      this.dynamicTaskDao.removeByInstId(instId);
   }

   public List<DynamicTask> getByTaskId(String taskId) {
      QueryFilter queryFilter = new DefaultQueryFilter(true);
      queryFilter.addFilter("task_id_", taskId, QueryOP.EQUAL);
      return this.dynamicTaskDao.query(queryFilter);
   }

   public void updateEndByInstId(String instId) {
      this.dynamicTaskDao.updateEndByInstId(instId);
   }
}
