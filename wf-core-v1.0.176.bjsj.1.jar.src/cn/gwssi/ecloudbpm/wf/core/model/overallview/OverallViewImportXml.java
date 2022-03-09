/*    */ package cn.gwssi.ecloudbpm.wf.core.model.overallview;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlElements;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name = "ecloudbpmXmlList")
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class OverallViewImportXml
/*    */ {
/*    */   @XmlElements({@XmlElement(name = "ecloudbpmXml", type = OverallViewExport.class)})
/* 16 */   private List<OverallViewExport> bpmXmlList = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<OverallViewExport> getBpmXmlList() {
/* 20 */     return this.bpmXmlList;
/*    */   }
/*    */   
/*    */   public void setBpmXmlList(List<OverallViewExport> bpmXmlList) {
/* 24 */     this.bpmXmlList = bpmXmlList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addOverallViewExport(OverallViewExport def) {
/* 29 */     this.bpmXmlList.add(def);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/overallview/OverallViewImportXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */