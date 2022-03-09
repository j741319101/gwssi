/*    */ package cn.gwssi.ecloudbpm.form.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlRootElement(name = "bpmForms")
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class FormXml
/*    */ {
/*    */   @XmlElement(name = "bpmForm", type = FormDef.class)
/*    */   private IFormDef bpmForm;
/*    */   @XmlElement(name = "formBusSet", type = FormBusSet.class)
/*    */   private FormBusSet formBusSet;
/*    */   @XmlElement(name = "boCodes")
/*    */   private List<String> boCodes;
/*    */   
/*    */   public List<String> getBoCodes() {
/* 31 */     return this.boCodes;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBoCodes(List<String> boCodes) {
/* 36 */     this.boCodes = boCodes;
/*    */   }
/*    */ 
/*    */   
/*    */   public IFormDef getBpmForm() {
/* 41 */     return this.bpmForm;
/*    */   }
/*    */   
/*    */   public FormBusSet getFormBusSet() {
/* 45 */     return this.formBusSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFormBusSet(FormBusSet formBusSet) {
/* 50 */     this.formBusSet = formBusSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBpmForm(IFormDef bpmForm) {
/* 55 */     this.bpmForm = bpmForm;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */