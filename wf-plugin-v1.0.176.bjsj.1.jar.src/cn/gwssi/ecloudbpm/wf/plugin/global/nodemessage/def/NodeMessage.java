/*    */ package com.dstz.bpm.plugin.global.nodemessage.def;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.List;
/*    */ import org.hibernate.validator.constraints.NotBlank;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeMessage
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   @NotBlank(message = "节点消息描述不能为空")
/*    */   private String desc;
/*    */   private String nodeId;
/*    */   @NotBlank
/*    */   private String event;
/*    */   private String condition;
/*    */   private List<UserAssignRule> userRules;
/*    */   @NotBlank
/*    */   private String msgType;
/*    */   private String htmlTemplate;
/*    */   private String textTemplate;
/*    */   
/*    */   public String getDesc() {
/* 29 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 33 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public String getNodeId() {
/* 37 */     return this.nodeId;
/*    */   }
/*    */   
/*    */   public void setNodeId(String nodeId) {
/* 41 */     this.nodeId = nodeId;
/*    */   }
/*    */   
/*    */   public String getEvent() {
/* 45 */     return this.event;
/*    */   }
/*    */   
/*    */   public void setEvent(String event) {
/* 49 */     this.event = event;
/*    */   }
/*    */   
/*    */   public String getCondition() {
/* 53 */     return this.condition;
/*    */   }
/*    */   
/*    */   public void setCondition(String condition) {
/* 57 */     this.condition = condition;
/*    */   }
/*    */   
/*    */   public List<UserAssignRule> getUserRules() {
/* 61 */     return this.userRules;
/*    */   }
/*    */   
/*    */   public void setUserRules(List<UserAssignRule> userRules) {
/* 65 */     this.userRules = userRules;
/*    */   }
/*    */   
/*    */   public String getMsgType() {
/* 69 */     return this.msgType;
/*    */   }
/*    */   
/*    */   public void setMsgType(String msgType) {
/* 73 */     this.msgType = msgType;
/*    */   }
/*    */   
/*    */   public String getHtmlTemplate() {
/* 77 */     return this.htmlTemplate;
/*    */   }
/*    */   
/*    */   public void setHtmlTemplate(String htmlTemplate) {
/* 81 */     this.htmlTemplate = htmlTemplate;
/*    */   }
/*    */   
/*    */   public String getTextTemplate() {
/* 85 */     return this.textTemplate;
/*    */   }
/*    */   
/*    */   public void setTextTemplate(String textTemplate) {
/* 89 */     this.textTemplate = textTemplate;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/nodemessage/def/NodeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */