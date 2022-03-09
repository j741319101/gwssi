/*    */ package cn.gwssi.ecloudframework.base.db.model.query;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.query.FieldLogic;
/*    */ import cn.gwssi.ecloudframework.base.api.query.FieldRelation;
/*    */ import cn.gwssi.ecloudframework.base.api.query.WhereClause;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultFieldLogic
/*    */   implements FieldLogic
/*    */ {
/* 18 */   private List<WhereClause> whereClauses = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/* 22 */   private FieldRelation fieldRelation = FieldRelation.AND;
/*    */ 
/*    */   
/*    */   public DefaultFieldLogic() {}
/*    */   
/*    */   public DefaultFieldLogic(FieldRelation fieldRelation) {
/* 28 */     this.fieldRelation = fieldRelation;
/*    */   }
/*    */   
/*    */   public List<WhereClause> getWhereClauses() {
/* 32 */     return this.whereClauses;
/*    */   }
/*    */   
/*    */   public void setWhereClauses(List<WhereClause> whereClauses) {
/* 36 */     this.whereClauses = whereClauses;
/*    */   }
/*    */   
/*    */   public FieldRelation getFieldRelation() {
/* 40 */     return this.fieldRelation;
/*    */   }
/*    */   
/*    */   public void setFieldRelation(FieldRelation fieldRelation) {
/* 44 */     this.fieldRelation = fieldRelation;
/*    */   }
/*    */   
/*    */   public String getSql() {
/* 48 */     if (this.whereClauses.isEmpty()) {
/* 49 */       return "";
/*    */     }
/*    */     
/* 52 */     StringBuilder sb = new StringBuilder("(");
/* 53 */     if (!this.whereClauses.isEmpty() && FieldRelation.NOT == this.fieldRelation) {
/* 54 */       sb.append(" NOT (");
/* 55 */       for (WhereClause wc : this.whereClauses) {
/* 56 */         if (!sb.toString().endsWith("NOT (")) {
/* 57 */           sb.append(" " + FieldRelation.AND.value() + " ");
/*    */         }
/* 59 */         sb.append(wc.getSql());
/*    */       } 
/* 61 */       sb.append(")");
/* 62 */       return sb.toString();
/*    */     } 
/*    */     
/* 65 */     for (WhereClause wc : this.whereClauses) {
/* 66 */       if (!sb.toString().endsWith("(")) {
/* 67 */         sb.append(" " + this.fieldRelation.value() + " ");
/*    */       }
/* 69 */       sb.append(wc.getSql());
/*    */     } 
/* 71 */     sb.append(")");
/*    */     
/* 73 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/query/DefaultFieldLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */