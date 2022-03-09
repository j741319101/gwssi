/*    */ package cn.gwssi.ecloudbpm.wf.rest.vo;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskOpinionVO;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class BpmInstanceOptionVO extends BpmInstanceOption {
/*    */   private String parentTaskId;
/*    */   private String parentNodeId;
/*    */   private String orgName;
/*    */   private Integer orgSn;
/*    */   private String taskType;
/*    */   private String dynamicTaskId;
/*    */   private String defId;
/*    */   private String parentDefId;
/* 16 */   private List<BpmTaskOpinionVO> bpmTaskOpinionVOS = new ArrayList<>();
/*    */   
/*    */   public String getParentTaskId() {
/* 19 */     return this.parentTaskId;
/*    */   }
/*    */   
/*    */   public void setParentTaskId(String parentTaskId) {
/* 23 */     this.parentTaskId = parentTaskId;
/*    */   }
/*    */   
/*    */   public String getParentNodeId() {
/* 27 */     return this.parentNodeId;
/*    */   }
/*    */   
/*    */   public void setParentNodeId(String parentNodeId) {
/* 31 */     this.parentNodeId = parentNodeId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getOrgName() {
/* 36 */     return this.orgName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOrgName(String orgName) {
/* 41 */     this.orgName = orgName;
/*    */   }
/*    */   
/*    */   public Integer getOrgSn() {
/* 45 */     return this.orgSn;
/*    */   }
/*    */   
/*    */   public void setOrgSn(Integer orgSn) {
/* 49 */     this.orgSn = orgSn;
/*    */   }
/*    */   
/*    */   public String getTaskType() {
/* 53 */     return this.taskType;
/*    */   }
/*    */   
/*    */   public void setTaskType(String taskType) {
/* 57 */     this.taskType = taskType;
/*    */   }
/*    */   
/*    */   public String getDynamicTaskId() {
/* 61 */     return this.dynamicTaskId;
/*    */   }
/*    */   
/*    */   public void setDynamicTaskId(String dynamicTaskId) {
/* 65 */     this.dynamicTaskId = dynamicTaskId;
/*    */   }
/*    */   
/*    */   public String getDefId() {
/* 69 */     return this.defId;
/*    */   }
/*    */   
/*    */   public void setDefId(String defId) {
/* 73 */     this.defId = defId;
/*    */   }
/*    */   
/*    */   public String getParentDefId() {
/* 77 */     return this.parentDefId;
/*    */   }
/*    */   
/*    */   public void setParentDefId(String parentDefId) {
/* 81 */     this.parentDefId = parentDefId;
/*    */   }
/*    */   
/*    */   public List<BpmTaskOpinionVO> getBpmTaskOpinionVOS() {
/* 85 */     return this.bpmTaskOpinionVOS;
/*    */   }
/*    */   
/*    */   public void setBpmTaskOpinionVOS(List<BpmTaskOpinionVO> bpmTaskOpinionVOS) {
/* 89 */     this.bpmTaskOpinionVOS = bpmTaskOpinionVOS;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/BpmInstanceOptionVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */