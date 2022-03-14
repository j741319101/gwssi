package com.dstz.bpm.plugin.global.carboncopy.def;

import com.dstz.bpm.api.engine.plugin.def.UserAssignRule;
import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;

public class BpmCarbonCopy extends AbstractBpmExecutionPluginDef {
   private static final long serialVersionUID = 7296293350024659302L;
   @NotBlank(
      message = "节点抄送消息描述"
   )
   private String desc;
   private String formType;
   private String nodeId;
   @NotBlank
   private String event;
   private List<UserAssignRule> userRules;
   @NotBlank
   private String msgType;
   private String htmlTemplate;
   private String textTemplate;

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getFormType() {
      return this.formType;
   }

   public void setFormType(String formType) {
      this.formType = formType;
   }

   public String getNodeId() {
      return this.nodeId;
   }

   public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
   }

   public String getEvent() {
      return this.event;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   public List<UserAssignRule> getUserRules() {
      return this.userRules;
   }

   public void setUserRules(List<UserAssignRule> userRules) {
      this.userRules = userRules;
   }

   public String getMsgType() {
      return this.msgType;
   }

   public void setMsgType(String msgType) {
      this.msgType = msgType;
   }

   public String getHtmlTemplate() {
      return this.htmlTemplate;
   }

   public void setHtmlTemplate(String htmlTemplate) {
      this.htmlTemplate = htmlTemplate;
   }

   public String getTextTemplate() {
      return this.textTemplate;
   }

   public void setTextTemplate(String textTemplate) {
      this.textTemplate = textTemplate;
   }
}
