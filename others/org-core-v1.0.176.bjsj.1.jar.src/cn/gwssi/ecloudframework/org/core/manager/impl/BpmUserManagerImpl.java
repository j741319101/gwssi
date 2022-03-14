/*    */ package com.dstz.org.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.org.api.model.dto.BpmOrgDTO;
/*    */ import com.dstz.org.api.model.dto.BpmUserDTO;
/*    */ import com.dstz.org.core.dao.BpmUserDao;
/*    */ import com.dstz.org.core.manager.BpmUserManager;
/*    */ import com.dstz.org.core.model.User;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BpmUserManagerImpl
/*    */   implements BpmUserManager
/*    */ {
/*    */   @Autowired
/*    */   BpmUserDao userDao;
/*    */   
/*    */   public List<User> getUserListByOrgId(String groupId) {
/* 28 */     return this.userDao.getUserListByOrgId(groupId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<User> getUserListByRelation(String groupId, String type) {
/* 33 */     return this.userDao.getUserListByRelation(groupId, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<User> query(QueryFilter filter) {
/* 38 */     return this.userDao.query(filter);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmUserDTO> getUserOrgInfos(String userIds) {
/* 43 */     String[] arrUserId = userIds.split(",");
/* 44 */     return this.userDao.getUserOrgInfos(arrUserId);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmOrgDTO> getOrgInfos(String orgIds) {
/* 49 */     String[] arrOrgId = orgIds.split(",");
/* 50 */     return this.userDao.getOrgInfos(arrOrgId);
/*    */   }
/*    */   
/*    */   public List<User> getUserListByPostId(String postId) {
/* 54 */     return this.userDao.getUserListByPostId(postId);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/impl/BpmUserManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */