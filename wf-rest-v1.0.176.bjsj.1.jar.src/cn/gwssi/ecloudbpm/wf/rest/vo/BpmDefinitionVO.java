/*    */ package cn.gwssi.ecloudbpm.wf.rest.vo;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
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
/*    */ 
/*    */ 
/*    */ public class BpmDefinitionVO
/*    */   extends BpmDefinition
/*    */ {
/*    */   protected String createUser;
/*    */   protected String updateUser;
/*    */   protected String lockUser;
/*    */   
/*    */   public String getCreateUser() {
/* 28 */     return this.createUser;
/*    */   }
/*    */   
/*    */   public void setCreateUser(String createUser) {
/* 32 */     this.createUser = createUser;
/*    */   }
/*    */   
/*    */   public String getUpdateUser() {
/* 36 */     return this.updateUser;
/*    */   }
/*    */   
/*    */   public void setUpdateUser(String updateUser) {
/* 40 */     this.updateUser = updateUser;
/*    */   }
/*    */   
/*    */   public String getLockUser() {
/* 44 */     return this.lockUser;
/*    */   }
/*    */   
/*    */   public void setLockUser(String lockUser) {
/* 48 */     this.lockUser = lockUser;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/vo/BpmDefinitionVO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */