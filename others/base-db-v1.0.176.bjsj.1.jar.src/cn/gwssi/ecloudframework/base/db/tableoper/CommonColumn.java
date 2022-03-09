/*    */ package cn.gwssi.ecloudframework.base.db.tableoper;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.stereotype.Service;
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
/*    */ @Service
/*    */ public class CommonColumn
/*    */ {
/*    */   @Value("${ecloud.common-column.createUser:create_by}")
/*    */   private String createUser;
/*    */   @Value("${ecloud.common-column.createTime:create_time}")
/*    */   private String createTime;
/*    */   @Value("${ecloud.common-column.updateUser:update_by}")
/*    */   private String updateUser;
/*    */   @Value("${ecloud.common-column.updateTime:update_time}")
/*    */   private String updateTime;
/*    */   
/*    */   public String getCreateUser() {
/* 28 */     return this.createUser;
/*    */   }
/*    */   
/*    */   public void setCreateUser(String createUser) {
/* 32 */     this.createUser = createUser;
/*    */   }
/*    */   
/*    */   public String getCreateTime() {
/* 36 */     return this.createTime;
/*    */   }
/*    */   
/*    */   public void setCreateTime(String createTime) {
/* 40 */     this.createTime = createTime;
/*    */   }
/*    */   
/*    */   public String getUpdateUser() {
/* 44 */     return this.updateUser;
/*    */   }
/*    */   
/*    */   public void setUpdateUser(String updateUser) {
/* 48 */     this.updateUser = updateUser;
/*    */   }
/*    */   
/*    */   public String getUpdateTime() {
/* 52 */     return this.updateTime;
/*    */   }
/*    */   
/*    */   public void setUpdateTime(String updateTime) {
/* 56 */     this.updateTime = updateTime;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/tableoper/CommonColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */