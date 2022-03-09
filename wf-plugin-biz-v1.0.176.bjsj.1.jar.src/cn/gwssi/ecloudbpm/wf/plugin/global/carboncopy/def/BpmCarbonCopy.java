/*     */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.def;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.UserAssignRule;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*     */ import java.util.List;
/*     */ import org.hibernate.validator.constraints.NotBlank;
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
/*     */ public class BpmCarbonCopy
/*     */   extends AbstractBpmExecutionPluginDef
/*     */ {
/*     */   private static final long serialVersionUID = 7296293350024659302L;
/*     */   @NotBlank(message = "节点抄送消息描述")
/*     */   private String desc;
/*     */   private String formType;
/*     */   private String nodeId;
/*     */   @NotBlank
/*     */   private String event;
/*     */   private List<UserAssignRule> userRules;
/*     */   @NotBlank
/*     */   private String msgType;
/*     */   private String htmlTemplate;
/*     */   private String textTemplate;
/*     */   
/*     */   public String getDesc() {
/*  66 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/*  70 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public String getFormType() {
/*  74 */     return this.formType;
/*     */   }
/*     */   
/*     */   public void setFormType(String formType) {
/*  78 */     this.formType = formType;
/*     */   }
/*     */   
/*     */   public String getNodeId() {
/*  82 */     return this.nodeId;
/*     */   }
/*     */   
/*     */   public void setNodeId(String nodeId) {
/*  86 */     this.nodeId = nodeId;
/*     */   }
/*     */   
/*     */   public String getEvent() {
/*  90 */     return this.event;
/*     */   }
/*     */   
/*     */   public void setEvent(String event) {
/*  94 */     this.event = event;
/*     */   }
/*     */   
/*     */   public List<UserAssignRule> getUserRules() {
/*  98 */     return this.userRules;
/*     */   }
/*     */   
/*     */   public void setUserRules(List<UserAssignRule> userRules) {
/* 102 */     this.userRules = userRules;
/*     */   }
/*     */   
/*     */   public String getMsgType() {
/* 106 */     return this.msgType;
/*     */   }
/*     */   
/*     */   public void setMsgType(String msgType) {
/* 110 */     this.msgType = msgType;
/*     */   }
/*     */   
/*     */   public String getHtmlTemplate() {
/* 114 */     return this.htmlTemplate;
/*     */   }
/*     */   
/*     */   public void setHtmlTemplate(String htmlTemplate) {
/* 118 */     this.htmlTemplate = htmlTemplate;
/*     */   }
/*     */   
/*     */   public String getTextTemplate() {
/* 122 */     return this.textTemplate;
/*     */   }
/*     */   
/*     */   public void setTextTemplate(String textTemplate) {
/* 126 */     this.textTemplate = textTemplate;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/def/BpmCarbonCopy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */