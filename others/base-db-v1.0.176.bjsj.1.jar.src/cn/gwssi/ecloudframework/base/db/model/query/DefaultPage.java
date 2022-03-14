/*     */ package com.dstz.base.db.model.query;
/*     */ 
/*     */ import com.dstz.base.api.Page;
/*     */ import com.dstz.base.api.query.FieldSort;
/*     */ import com.dstz.base.core.util.ToStringUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.ibatis.session.RowBounds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultPage
/*     */   extends RowBounds
/*     */   implements Page, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1500879478877475515L;
/*     */   public static final int NO_PAGE = 1;
/*  25 */   protected int pageNo = 1;
/*     */ 
/*     */ 
/*     */   
/*  29 */   protected int pageSize = 10;
/*     */ 
/*     */ 
/*     */   
/*  33 */   protected List<FieldSort> orders = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private boolean isShowTotal = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPage() {}
/*     */ 
/*     */   
/*     */   public DefaultPage(RowBounds rowBounds) {
/*  44 */     this.pageNo = rowBounds.getOffset() / rowBounds.getLimit() + 1;
/*  45 */     this.pageSize = rowBounds.getLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPage(int pageNo, int pageSize) {
/*  54 */     this(pageNo, pageSize, new ArrayList<>(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPage(List<FieldSort> orders) {
/*  63 */     this(1, 2147483647, orders, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPage(FieldSort... order) {
/*  72 */     this(1, 2147483647, order);
/*  73 */     this.isShowTotal = false;
/*     */   }
/*     */   
/*     */   public DefaultPage(int page, int limit, FieldSort... order) {
/*  77 */     this(page, limit, Arrays.asList(order), true);
/*     */   }
/*     */   
/*     */   public DefaultPage(int page, int limit, List<FieldSort> orders) {
/*  81 */     this(page, limit, orders, true);
/*     */   }
/*     */   
/*     */   public DefaultPage(int pageNo, int pageSize, List<FieldSort> orders, boolean isShowTotal) {
/*  85 */     this.pageNo = pageNo;
/*  86 */     this.pageSize = pageSize;
/*  87 */     this.orders = orders;
/*  88 */     this.isShowTotal = isShowTotal;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPage() {
/*  93 */     return this.pageNo;
/*     */   }
/*     */   
/*     */   public void setPage(int page) {
/*  97 */     this.pageNo = page;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLimit() {
/* 102 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setLimit(int limit) {
/* 106 */     this.pageSize = limit;
/*     */   }
/*     */   
/*     */   public List<FieldSort> getOrders() {
/* 110 */     List<FieldSort> list = this.orders;
/* 111 */     return list;
/*     */   }
/*     */   
/*     */   public void setOrders(List<FieldSort> orders) {
/* 115 */     this.orders = orders;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 120 */     if (this.pageNo >= 1) {
/* 121 */       return (this.pageNo - 1) * this.pageSize;
/*     */     }
/* 123 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return ToStringUtil.toString(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPageNo() {
/* 134 */     return Integer.valueOf(getPage());
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getPageSize() {
/* 139 */     return Integer.valueOf(getLimit());
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getStartIndex() {
/* 144 */     return Integer.valueOf(getOffset());
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getTotal() {
/* 149 */     throw new RuntimeException("total not support");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShowTotal(boolean isShowTotal) {
/* 154 */     this.isShowTotal = isShowTotal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShowTotal() {
/* 159 */     return this.isShowTotal;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/query/DefaultPage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */