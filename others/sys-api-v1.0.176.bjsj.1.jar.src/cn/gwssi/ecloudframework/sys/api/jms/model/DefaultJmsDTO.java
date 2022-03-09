/*    */ package cn.gwssi.ecloudframework.sys.api.jms.model;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultJmsDTO<T extends Serializable>
/*    */   implements JmsDTO
/*    */ {
/*    */   private String type;
/*    */   private T data;
/*    */   
/*    */   public DefaultJmsDTO(String type, T data) {
/* 19 */     this.type = type;
/* 20 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 25 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 29 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getData() {
/* 34 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(T data) {
/* 38 */     this.data = data;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/jms/model/DefaultJmsDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */