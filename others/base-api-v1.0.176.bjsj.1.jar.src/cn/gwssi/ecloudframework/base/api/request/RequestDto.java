/*    */ package cn.gwssi.ecloudframework.base.api.request;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestDto<E>
/*    */ {
/*    */   private RequestHead head;
/*    */   private E data;
/*    */   
/*    */   public RequestHead getHead() {
/* 16 */     return this.head;
/*    */   }
/*    */   
/*    */   public void setHead(RequestHead head) {
/* 20 */     this.head = head;
/*    */   }
/*    */   
/*    */   public E getData() {
/* 24 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(E data) {
/* 28 */     this.data = data;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/request/RequestDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */