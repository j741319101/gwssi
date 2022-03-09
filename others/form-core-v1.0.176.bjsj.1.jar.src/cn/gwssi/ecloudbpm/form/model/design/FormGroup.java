/*    */ package cn.gwssi.ecloudbpm.form.model.design;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class FormGroup
/*    */ {
/*    */   private String comment;
/*    */   private String key;
/*    */   private IBusTableRel tableRelation;
/*    */   private List<FormColumn> columnList;
/*    */   
/*    */   public boolean hasTitle() {
/* 16 */     return StringUtil.isNotEmpty(this.comment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<IBusTableRel> getOne2OneChildsOne2ManyRelations() {
/* 24 */     List<IBusTableRel> grandson = new ArrayList<>();
/*    */     
/* 26 */     this.tableRelation.getChildren("oneToOne").forEach(one2One -> grandson.addAll(one2One.getChildren("oneToMany")));
/*    */ 
/*    */     
/* 29 */     return grandson;
/*    */   }
/*    */   
/*    */   public String getComment() {
/* 33 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(String comment) {
/* 37 */     this.comment = comment;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 41 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 45 */     this.key = key;
/*    */   }
/*    */   
/*    */   public List<FormColumn> getColumnList() {
/* 49 */     return this.columnList;
/*    */   }
/*    */   
/*    */   public IBusTableRel getTableRelation() {
/* 53 */     return this.tableRelation;
/*    */   }
/*    */   
/*    */   public void setTableRelation(IBusTableRel tableRelation) {
/* 57 */     this.tableRelation = tableRelation;
/*    */   }
/*    */   
/*    */   public void setColumnList(List<FormColumn> columnList) {
/* 61 */     this.columnList = columnList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/design/FormGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */