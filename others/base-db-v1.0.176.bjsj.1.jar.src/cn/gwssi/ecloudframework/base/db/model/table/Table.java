/*     */ package com.dstz.base.db.model.table;
/*     */ 
/*     */ import com.alibaba.fastjson.annotation.JSONType;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.validation.Valid;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
/*     */ public class Table<C extends Column>
/*     */   implements Serializable
/*     */ {
/*     */   @NotEmpty
/*     */   protected String name;
/*     */   protected String comment;
/*     */   @Valid
/*     */   protected List<C> columns;
/*     */   
/*     */   public String getName() {
/*  39 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  43 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getComment() {
/*  47 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(String comment) {
/*  51 */     this.comment = comment;
/*     */   }
/*     */   
/*     */   public List<C> getColumns() {
/*  55 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void setColumns(List<C> columns) {
/*  59 */     this.columns = columns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C getPkColumn() {
/*  70 */     if (this.columns == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     List<C> list = new ArrayList<>();
/*  74 */     for (Column column : getColumns()) {
/*  75 */       if (column.isPrimary()) {
/*  76 */         list.add((C)column);
/*     */       }
/*     */     } 
/*  79 */     if (list.isEmpty()) {
/*  80 */       return null;
/*     */     }
/*  82 */     return list.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C getColumn(String name) {
/*  94 */     if (this.columns == null) {
/*  95 */       return null;
/*     */     }
/*  97 */     for (Column column : this.columns) {
/*  98 */       if (name.equalsIgnoreCase(column.getName())) {
/*  99 */         return (C)column;
/*     */       }
/*     */     } 
/* 102 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/table/Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */