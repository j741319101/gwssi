/*    */ package cn.gwssi.ecloudframework.org.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.org.core.dao.RoleDao;
/*    */ import cn.gwssi.ecloudframework.org.core.manager.RoleManager;
/*    */ import cn.gwssi.ecloudframework.org.core.model.Role;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
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
/*    */ public class RoleManagerImpl
/*    */   extends BaseManager<String, Role>
/*    */   implements RoleManager
/*    */ {
/*    */   @Resource
/*    */   RoleDao roleDao;
/*    */   
/*    */   public Role getByAlias(String alias) {
/* 32 */     return this.roleDao.getByAlias(alias);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Role> getByUserId(String userId) {
/* 37 */     if (StringUtil.isEmpty(userId)) {
/* 38 */       return Collections.emptyList();
/*    */     }
/* 40 */     return this.roleDao.getByUserId(userId);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRoleExist(Role role) {
/* 45 */     return (this.roleDao.isRoleExist(role).intValue() != 0);
/*    */   }
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
/*    */   public void setStatus(String id, Integer status) {
/* 58 */     String currentUserId = ContextUtil.getCurrentUserId();
/* 59 */     Role role = new Role();
/* 60 */     role.setId(id);
/* 61 */     role.setEnabled(status);
/* 62 */     role.setUpdateBy(currentUserId);
/* 63 */     this.roleDao.updateByPrimaryKeySelective(role);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/RoleManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */