package com.dstz.bpm.plugin.core.model;

import com.dstz.bpm.api.engine.plugin.def.DynamicTaskIdentitys;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.model.BaseModel;
import com.dstz.base.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dstz.sys.api.model.SysIdentity;

import java.util.ArrayList;
import java.util.List;

public class DynamicTask extends BaseModel {
   public static final String status_runtime = "runtime";
   public static final String status_completed = "completed";
   protected String instId;
   protected String actExecutionId;
   protected String nodeId;
   protected Integer currentIndex;
   protected String identityNode;
   protected String status = "runtime";
   protected boolean isParallel = true;
   protected Integer ammount = 0;
   protected String taskId;

   public DynamicTask(IBpmTask task, List<SysIdentity> identitys, boolean isParallel) {
      this.instId = task.getInstId();
      this.actExecutionId = task.getActExecutionId();
      this.nodeId = task.getNodeId();
      this.currentIndex = 0;
      this.ammount = identitys.size();
      this.isParallel = isParallel;
      List<DynamicTaskIdentitys> identityNodes = new ArrayList();

      for(int i = 0; i < identitys.size(); ++i) {
         List<SysIdentity> list = new ArrayList(1);
         list.add(identitys.get(i));
         identityNodes.add(new DynamicTaskIdentitys(String.format("%s-%d", task.getName(), i + 1), list));
      }

      this.identityNode = JSON.toJSONString(identityNodes);
   }

   public DynamicTask(IBpmTask task) {
      this.instId = task.getInstId();
      this.actExecutionId = task.getActExecutionId();
      this.nodeId = task.getNodeId();
      this.currentIndex = 0;
      this.isParallel = true;
      this.taskId = task.getId();
   }

   public DynamicTask() {
   }

   public DynamicTaskIdentitys loadCurrentTaskIdentitys() {
      return (DynamicTaskIdentitys)this.loadAllTaskIdentitys().get(this.currentIndex);
   }

   public List<DynamicTaskIdentitys> loadAllTaskIdentitys() {
      if (StringUtil.isEmpty(this.identityNode)) {
         return null;
      } else {
         List<DynamicTaskIdentitys> identitys = JSONArray.parseArray(this.identityNode, DynamicTaskIdentitys.class);
         if (identitys.size() != this.ammount) {
            throw new BusinessException("动态任务分配候选人失败，候选人数与统计数不同！");
         } else {
            return identitys;
         }
      }
   }

   public void setInstId(String instId) {
      this.instId = instId;
   }

   public String getInstId() {
      return this.instId;
   }

   public void setActExecutionId(String actExecutionId) {
      this.actExecutionId = actExecutionId;
   }

   public String getActExecutionId() {
      return this.actExecutionId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setCurrentIndex(Integer currentIndex) {
      this.currentIndex = currentIndex;
   }

   public Integer getCurrentIndex() {
      return this.currentIndex;
   }

   public void setIdentityNode(String identityNode) {
      this.identityNode = identityNode;
   }

   public String getIdentityNode() {
      return this.identityNode;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getStatus() {
      return this.status;
   }

   public void setIsParallel(boolean isParallel) {
      this.isParallel = isParallel;
   }

   public boolean getIsParallel() {
      return this.isParallel;
   }

   public void setAmmount(Integer ammount) {
      this.ammount = ammount;
   }

   public Integer getAmmount() {
      return this.ammount;
   }

   public String getTaskId() {
      return this.taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }
}
