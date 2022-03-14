/*    */ package com.dstz.sys.core.model;
/*    */ 
/*    */ import com.dstz.base.core.model.BaseModel;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MetaColumnRelation
/*    */   extends BaseModel
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String metaId;
/*    */   private String columnId;
/*    */   private String tableId;
/*    */   private String defaultValue;
/*    */   private String displayControl;
/*    */   private String inputValid;
/*    */   
/*    */   public String getMetaId() {
/* 23 */     return this.metaId;
/*    */   }
/*    */   
/*    */   public void setMetaId(String metaId) {
/* 27 */     this.metaId = metaId;
/*    */   }
/*    */   
/*    */   public String getColumnId() {
/* 31 */     return this.columnId;
/*    */   }
/*    */   
/*    */   public void setColumnId(String columnId) {
/* 35 */     this.columnId = columnId;
/*    */   }
/*    */   
/*    */   public String getTableId() {
/* 39 */     return this.tableId;
/*    */   }
/*    */   
/*    */   public void setTableId(String tableId) {
/* 43 */     this.tableId = tableId;
/*    */   }
/*    */   
/*    */   public String getDefaultValue() {
/* 47 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public void setDefaultValue(String defaultValue) {
/* 51 */     this.defaultValue = defaultValue;
/*    */   }
/*    */   
/*    */   public String getDisplayControl() {
/* 55 */     return this.displayControl;
/*    */   }
/*    */   
/*    */   public void setDisplayControl(String displayControl) {
/* 59 */     this.displayControl = displayControl;
/*    */   }
/*    */   
/*    */   public String getInputValid() {
/* 63 */     return this.inputValid;
/*    */   }
/*    */   
/*    */   public void setInputValid(String inputValid) {
/* 67 */     this.inputValid = inputValid;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/MetaColumnRelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */