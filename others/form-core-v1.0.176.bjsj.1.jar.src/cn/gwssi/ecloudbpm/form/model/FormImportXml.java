/*    */ package cn.gwssi.ecloudbpm.form.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name = "bpmFormImport")
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class FormImportXml
/*    */ {
/*    */   @XmlElement(name = "formXml", type = FormXml.class)
/* 15 */   List<FormXml> formXmlList = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<FormXml> getFormXmlList() {
/* 19 */     return this.formXmlList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFormXmlList(List<FormXml> formXmlList) {
/* 24 */     this.formXmlList = formXmlList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addFormXml(FormXml formXml) {
/* 29 */     this.formXmlList.add(formXml);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormImportXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */