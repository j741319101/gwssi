/*    */ package cn.gwssi.ecloudframework.base.api.request;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.query.FieldSort;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestPage
/*    */   extends RequestParam
/*    */ {
/* 17 */   private int pageNo = 1;
/* 18 */   private int pageSize = 20;
/* 19 */   private List<FieldSort> orders = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public RequestPage() {}
/*    */ 
/*    */   
/*    */   public RequestPage(int pageNo, int pageSize) {
/* 27 */     this.pageNo = pageNo;
/* 28 */     this.pageSize = pageSize;
/*    */   }
/*    */   
/*    */   public int getPageNo() {
/* 32 */     return this.pageNo;
/*    */   }
/*    */   
/*    */   public void setPageNo(int pageNo) {
/* 36 */     this.pageNo = pageNo;
/*    */   }
/*    */   
/*    */   public List<FieldSort> getOrders() {
/* 40 */     return this.orders;
/*    */   }
/*    */   
/*    */   public void setOrders(List<FieldSort> orders) {
/* 44 */     this.orders = orders;
/*    */   }
/*    */   
/*    */   public int getPageSize() {
/* 48 */     return this.pageSize;
/*    */   }
/*    */   
/*    */   public void setPageSize(int pageSize) {
/* 52 */     this.pageSize = pageSize;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/request/RequestPage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */