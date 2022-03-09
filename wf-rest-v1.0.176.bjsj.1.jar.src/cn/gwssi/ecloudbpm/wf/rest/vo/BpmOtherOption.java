/*    */ package cn.gwssi.ecloudbpm.wf.rest.vo;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BpmOtherOption
/*    */ {
/*    */   private String nodeName;
/*    */   private String nodeId;
/*    */   private String time;
/*    */   private List<BpmMetaPartentOption> children;
/*    */   
/*    */   public String getNodeName() {
/* 22 */     return this.nodeName;
/*    */   }
/*    */   
/*    */   public void setNodeName(String nodeName) {
/* 26 */     this.nodeName = nodeName;
/*    */   }
/*    */   
/*    */   public String getTime() {
/* 30 */     return this.time;
/*    */   }
/*    */   
/*    */   public void setTime(String time) {
/* 34 */     this.time = time;
/*    */   }
/*    */   
/*    */   public List<BpmMetaPartentOption> getChildren() {
/* 38 */     return this.children;
/*    */   }
/*    */   
/*    */   public void setChildren(List<BpmMetaPartentOption> children) {
/* 42 */     this.children = children;
/*    */   }
/*    */   
/*    */   public String getNodeId() {
/* 46 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 50 */     this.nodeId = nodeId;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/BpmOtherOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */