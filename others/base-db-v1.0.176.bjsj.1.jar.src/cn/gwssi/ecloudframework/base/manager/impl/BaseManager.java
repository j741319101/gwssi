/*    */ package com.dstz.base.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.Page;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.dao.BaseDao;
/*    */ import com.dstz.base.manager.Manager;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseManager<PK extends Serializable, T extends Serializable>
/*    */   implements Manager<PK, T>
/*    */ {
/*    */   @Autowired
/*    */   protected BaseDao<PK, T> dao;
/*    */   
/*    */   public void create(T entity) {
/* 25 */     this.dao.create(entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(T entity) {
/* 30 */     this.dao.update(entity);
/*    */   }
/*    */   
/*    */   public void remove(PK entityId) {
/* 34 */     this.dao.remove((Serializable)entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(PK entityId) {
/* 39 */     return (T)this.dao.get((Serializable)entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeByIds(PK... ids) {
/* 44 */     if (ids != null) {
/* 45 */       for (PK pk : ids) {
/* 46 */         remove(pk);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> query(QueryFilter queryFilter) {
/* 53 */     return this.dao.query(queryFilter);
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
/*    */   public T queryOne(QueryFilter queryFilter) {
/* 65 */     List<T> list = query(queryFilter);
/* 66 */     if (list.isEmpty()) {
/* 67 */       return null;
/*    */     }
/* 69 */     return list.get(0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> getAll() {
/* 75 */     return this.dao.query();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> getAllByPage(Page page) {
/* 80 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/manager/impl/BaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */