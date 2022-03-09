/*    */ package cn.gwssi.ecloudframework.sys.permission.impl.group;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.permission.impl.GroupPermissionCalculator;
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
/*    */ public class OrgPermissionCalculator
/*    */   extends GroupPermissionCalculator
/*    */ {
/*    */   public String getType() {
/* 20 */     return "org";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 25 */     return "组织";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/group/OrgPermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */