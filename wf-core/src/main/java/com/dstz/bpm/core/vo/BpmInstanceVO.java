/*    */ package com.dstz.bpm.core.vo;
/*    */ 
/*    */ import com.dstz.bpm.core.model.BpmInstance;
/*    */ import com.dstz.bpm.core.model.BpmTask;
/*    */ import java.util.List;
/*    */ 
/*    */ public class BpmInstanceVO
/*    */   extends BpmInstance {
/*    */   private String nodeTypeKey;
/*    */   private String nodeTypeName;
/*    */   private String remindNum;
/*    */   private String taskInfo;
/*    */   private List<BpmTask> bpmTasks;
/*    */   
/*    */   public String getTaskInfo() {
/* 16 */     return this.taskInfo;
/*    */   }
/*    */   
/*    */   public void setTaskInfo(String taskInfo) {
/* 20 */     this.taskInfo = taskInfo;
/*    */   }
/*    */   
/*    */   public List<BpmTask> getBpmTasks() {
/* 24 */     return this.bpmTasks;
/*    */   }
/*    */   
/*    */   public void setBpmTasks(List<BpmTask> bpmTasks) {
/* 28 */     this.bpmTasks = bpmTasks;
/*    */   }
/*    */   
/*    */   public String getRemindNum() {
/* 32 */     return this.remindNum;
/*    */   }
/*    */   
/*    */   public void setRemindNum(String remindNum) {
/* 36 */     this.remindNum = remindNum;
/*    */   }
/*    */   
/*    */   public String getNodeTypeKey() {
/* 40 */     return this.nodeTypeKey;
/*    */   }
/*    */   
/*    */   public void setNodeTypeKey(String nodeTypeKey) {
/* 44 */     this.nodeTypeKey = nodeTypeKey;
/*    */   }
/*    */   
/*    */   public String getNodeTypeName() {
/* 48 */     return this.nodeTypeName;
/*    */   }
/*    */   
/*    */   public void setNodeTypeName(String nodeTypeName) {
/* 52 */     this.nodeTypeName = nodeTypeName;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/vo/BpmInstanceVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */