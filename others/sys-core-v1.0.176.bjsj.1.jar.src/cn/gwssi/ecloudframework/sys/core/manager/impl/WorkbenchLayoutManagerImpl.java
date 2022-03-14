/*    */ package com.dstz.sys.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.sys.core.dao.WorkbenchLayoutDao;
/*    */ import com.dstz.sys.core.manager.WorkbenchLayoutManager;
/*    */ import com.dstz.sys.core.model.WorkbenchLayout;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("workbenchLayoutManager")
/*    */ public class WorkbenchLayoutManagerImpl
/*    */   extends BaseManager<String, WorkbenchLayout>
/*    */   implements WorkbenchLayoutManager
/*    */ {
/*    */   @Resource
/*    */   WorkbenchLayoutDao workbenchLayoutDao;
/*    */   
/*    */   public void savePanelLayout(List<WorkbenchLayout> layOutList, String userId) {
/* 24 */     this.workbenchLayoutDao.removeByUserId(userId);
/*    */     
/* 26 */     for (WorkbenchLayout layOut : layOutList) {
/* 27 */       layOut.setUserId(userId);
/* 28 */       this.workbenchLayoutDao.create(layOut);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<WorkbenchLayout> getByUserId(String userId) {
/* 35 */     List<WorkbenchLayout> list = this.workbenchLayoutDao.getByUserId(userId);
/*    */     
/* 37 */     if (CollectionUtil.isEmpty(list)) {
/* 38 */       list = this.workbenchLayoutDao.getByUserId("default_layout");
/*    */     }
/* 40 */     return list;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/WorkbenchLayoutManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */