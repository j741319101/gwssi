/*    */ package com.dstz.org.api.model.dto;
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
/*    */ 
/*    */ public class PageDTO
/*    */ {
/*    */   private static final long serialVersionUID = -700694295167942753L;
/*    */   protected String offset;
/*    */   protected String limit;
/*    */   protected String noPage;
/*    */   protected String order;
/*    */   protected String sort;
/*    */   
/*    */   PageDTO() {}
/*    */   
/*    */   public PageDTO(String offset, String limit, String noPage, String order, String sort) {
/* 25 */     this.offset = offset;
/* 26 */     this.limit = limit;
/* 27 */     this.noPage = noPage;
/* 28 */     this.order = order;
/* 29 */     this.sort = sort;
/*    */   }
/*    */   
/*    */   public String getOffset() {
/* 33 */     return this.offset;
/*    */   }
/*    */   
/*    */   public void setOffset(String offset) {
/* 37 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public String getLimit() {
/* 41 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(String limit) {
/* 45 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public String getNoPage() {
/* 49 */     return this.noPage;
/*    */   }
/*    */   
/*    */   public void setNoPage(String noPage) {
/* 53 */     this.noPage = noPage;
/*    */   }
/*    */   
/*    */   public String getOrder() {
/* 57 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(String order) {
/* 61 */     this.order = order;
/*    */   }
/*    */   
/*    */   public String getSort() {
/* 65 */     return this.sort;
/*    */   }
/*    */   
/*    */   public void setSort(String sort) {
/* 69 */     this.sort = sort;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/PageDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */