/*    */ package cn.gwssi.ecloudbpm.wf.api.model.def;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.hibernate.validator.constraints.NotBlank;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeInit
/*    */   implements Serializable
/*    */ {
/*    */   @NotBlank(message = "节点不能为空")
/* 13 */   private String nodeId = "";
/*    */   
/*    */   @NotBlank(message = "节点初始化描述不能为空")
/* 16 */   private String desc = "";
/*    */ 
/*    */   
/*    */   private String beforeShow;
/*    */   
/*    */   private String whenSave;
/*    */ 
/*    */   
/*    */   public String getNodeId() {
/* 25 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 29 */     this.nodeId = nodeId;
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 33 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 37 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getBeforeShow() {
/* 41 */     return this.beforeShow;
/*    */   }
/*    */   
/*    */   public void setBeforeShow(String beforeShow) {
/* 45 */     this.beforeShow = beforeShow;
/*    */   }
/*    */   
/*    */   public String getWhenSave() {
/* 49 */     return this.whenSave;
/*    */   }
/*    */   
/*    */   public void setWhenSave(String whenSave) {
/* 53 */     this.whenSave = whenSave;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/model/def/NodeInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */