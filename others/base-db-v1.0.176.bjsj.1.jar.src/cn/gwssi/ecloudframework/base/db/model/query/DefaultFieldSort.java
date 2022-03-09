/*    */ package cn.gwssi.ecloudframework.base.db.model.query;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.query.Direction;
/*    */ import cn.gwssi.ecloudframework.base.api.query.FieldSort;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultFieldSort
/*    */   implements FieldSort, Serializable
/*    */ {
/*    */   private Direction direction;
/*    */   private String field;
/*    */   
/*    */   public DefaultFieldSort(String field) {
/* 19 */     this(field, Direction.ASC);
/*    */   }
/*    */   
/*    */   public DefaultFieldSort(String field, Direction direction) {
/* 23 */     this.direction = direction;
/* 24 */     this.field = field;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 28 */     return this.direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getField() {
/* 33 */     return this.field;
/*    */   }
/*    */   
/*    */   public void setDirection(Direction direction) {
/* 37 */     this.direction = direction;
/*    */   }
/*    */   
/*    */   public void setField(String field) {
/* 41 */     this.field = field;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return this.field + ((this.direction == null) ? "" : (" " + this.direction.name()));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/query/DefaultFieldSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */