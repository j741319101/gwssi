/*    */ package cn.gwssi.ecloudframework.sys.api.jms.model.msg;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotifyMessage
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8026259230005823226L;
/*    */   private String subject;
/*    */   private String htmlContent;
/*    */   private String textContent;
/*    */   private IUser sender;
/*    */   private String tag;
/*    */   private List<SysIdentity> receivers;
/*    */   private Map<String, Object> extendVars;
/*    */   
/*    */   public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers) {
/* 51 */     this.extendVars = new HashMap<>(); this.subject = subject; this.sender = sender; this.receivers = receivers; this.htmlContent = htmlContent; } public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers, String tag, Map<String, Object> extendVars) { this.extendVars = new HashMap<>(); this.subject = subject; this.sender = sender; this.receivers = receivers; this.htmlContent = htmlContent;
/*    */     this.tag = tag;
/*    */     this.extendVars = extendVars; }
/* 54 */   public String getTag() { return this.tag; } public String getSubject() { return this.subject; }
/*    */    public void setTag(String tag) {
/*    */     this.tag = tag;
/*    */   } public void setSubject(String subject) {
/* 58 */     this.subject = subject;
/*    */   }
/*    */ 
/*    */   
/*    */   public IUser getSender() {
/* 63 */     return this.sender;
/*    */   }
/*    */   
/*    */   public void setSender(IUser sender) {
/* 67 */     this.sender = sender;
/*    */   }
/*    */   
/*    */   public List<SysIdentity> getReceivers() {
/* 71 */     return this.receivers;
/*    */   }
/*    */   
/*    */   public void setReceivers(List<SysIdentity> receivers) {
/* 75 */     this.receivers = receivers;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getExtendVars() {
/* 79 */     return this.extendVars;
/*    */   }
/*    */   
/*    */   public void setExtendVars(Map<String, Object> extendVars) {
/* 83 */     this.extendVars = extendVars;
/*    */   }
/*    */   
/*    */   public String getHtmlContent() {
/* 87 */     return this.htmlContent;
/*    */   }
/*    */   
/*    */   public void setHtmlContent(String htmlContent) {
/* 91 */     this.htmlContent = htmlContent;
/*    */   }
/*    */   
/*    */   public String getTextContent() {
/* 95 */     return this.textContent;
/*    */   }
/*    */   
/*    */   public void setTextContent(String textContent) {
/* 99 */     this.textContent = textContent;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/jms/model/msg/NotifyMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */