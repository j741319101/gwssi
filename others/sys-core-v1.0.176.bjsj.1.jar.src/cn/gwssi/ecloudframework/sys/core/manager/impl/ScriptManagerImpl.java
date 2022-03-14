/*    */ package com.dstz.sys.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.sys.core.dao.ScriptDao;
/*    */ import com.dstz.sys.core.manager.ScriptManager;
/*    */ import com.dstz.sys.core.model.Script;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("scriptManager")
/*    */ public class ScriptManagerImpl
/*    */   extends BaseManager<String, Script>
/*    */   implements ScriptManager
/*    */ {
/*    */   @Resource
/*    */   private ScriptDao scriptDao;
/*    */   
/*    */   public List<String> getDistinctCategory() {
/* 22 */     return this.scriptDao.getDistinctCategory();
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer isNameExists(String name) {
/* 27 */     return this.scriptDao.isNameExists(name);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/ScriptManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */