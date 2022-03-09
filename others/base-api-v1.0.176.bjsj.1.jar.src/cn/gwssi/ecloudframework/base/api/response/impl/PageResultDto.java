/*    */ package cn.gwssi.ecloudframework.base.api.response.impl;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class PageResultDto<T>
/*    */   extends BaseResult
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 18 */   private Integer pageSize = Integer.valueOf(0);
/*    */ 
/*    */ 
/*    */   
/* 22 */   private Integer page = Integer.valueOf(1);
/*    */ 
/*    */ 
/*    */   
/* 26 */   private Integer total = Integer.valueOf(0);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   private List rows = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getPageSize() {
/* 38 */     return this.pageSize;
/*    */   }
/*    */   
/*    */   public void setPageSize(Integer pageSize) {
/* 42 */     this.pageSize = pageSize;
/*    */   }
/*    */   
/*    */   public List<T> getRows() {
/* 46 */     return this.rows;
/*    */   }
/*    */   
/*    */   public void setRows(List<T> rows) {
/* 50 */     this.rows = rows;
/*    */   }
/*    */   
/*    */   public Integer getPage() {
/* 54 */     return this.page;
/*    */   }
/*    */   
/*    */   public void setPage(Integer page) {
/* 58 */     this.page = page;
/*    */   }
/*    */   
/*    */   public Integer getTotal() {
/* 62 */     return this.total;
/*    */   }
/*    */   
/*    */   public void setTotal(Integer total) {
/* 66 */     this.total = total;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/response/impl/PageResultDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */