/*    */ package cn.gwssi.ecloudbpm.bus.model.permission;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusColumnPermission;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusColumnPermission
/*    */   extends AbstractPermission
/*    */   implements IBusColumnPermission
/*    */ {
/*    */   private String key;
/*    */   private String comment;
/*    */   
/*    */   public String getKey() {
/* 26 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 30 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getComment() {
/* 34 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(String comment) {
/* 38 */     this.comment = comment;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/permission/BusColumnPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */