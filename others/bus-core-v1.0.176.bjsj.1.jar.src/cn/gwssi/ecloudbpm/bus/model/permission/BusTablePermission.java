/*    */ package cn.gwssi.ecloudbpm.bus.model.permission;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusTablePermission;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusTablePermission
/*    */   extends AbstractPermission
/*    */   implements IBusTablePermission
/*    */ {
/*    */   private String key;
/*    */   private String comment;
/* 20 */   private Map<String, BusColumnPermission> columnMap = new HashMap<>();
/*    */   
/*    */   public String getKey() {
/* 23 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 27 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getComment() {
/* 31 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(String comment) {
/* 35 */     this.comment = comment;
/*    */   }
/*    */   
/*    */   public Map<String, BusColumnPermission> getColumnMap() {
/* 39 */     return this.columnMap;
/*    */   }
/*    */   
/*    */   public void setColumnMap(Map<String, BusColumnPermission> columnMap) {
/* 43 */     this.columnMap = columnMap;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/permission/BusTablePermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */