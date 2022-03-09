/*    */ package com.dstz.bpm.core.model.overallview;
/*    */ 
/*    */ import com.dstz.bpm.core.model.BpmDefinition;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlRootElement(name = "ecloudbpmXml")
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class OverallViewExport
/*    */ {
/*    */   @XmlElement(name = "bpmDefinition", type = BpmDefinition.class)
/*    */   private BpmDefinition bpmDefinition;
/*    */   @XmlElement(name = "bpmnXml")
/*    */   private String bpmnXml;
/*    */   @XmlElement(name = "modelEditorJson")
/*    */   private String modelEditorJson;
/*    */   @XmlElement(name = "permission")
/* 26 */   private JSONArray permission = new JSONArray();
/*    */ 
/*    */ 
/*    */   
/*    */   public BpmDefinition getBpmDefinition() {
/* 31 */     return this.bpmDefinition;
/*    */   }
/*    */   public void setBpmDefinition(BpmDefinition bpmDefinition) {
/* 34 */     this.bpmDefinition = bpmDefinition;
/*    */   }
/*    */   public String getBpmnXml() {
/* 37 */     return this.bpmnXml;
/*    */   }
/*    */   public JSONArray getPermission() {
/* 40 */     return this.permission;
/*    */   }
/*    */   public void setPermission(JSONArray permission) {
/* 43 */     this.permission = permission;
/*    */   }
/*    */   public void setBpmnXml(String bpmnXml) {
/* 46 */     this.bpmnXml = bpmnXml;
/*    */   }
/*    */   public String getModelEditorJson() {
/* 49 */     return this.modelEditorJson;
/*    */   }
/*    */   public void setModelEditorJson(String modelEditorJson) {
/* 52 */     this.modelEditorJson = modelEditorJson;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/overallview/OverallViewExport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */