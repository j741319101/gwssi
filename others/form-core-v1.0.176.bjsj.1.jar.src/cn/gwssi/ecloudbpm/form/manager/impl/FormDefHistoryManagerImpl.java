/*    */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.dao.FormDefHistoryDao;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormDefHistoryManager;
/*    */ import cn.gwssi.ecloudbpm.form.model.FormDefHistory;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*    */ import cn.gwssi.ecloudframework.base.core.util.UserUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("formDefHistoryManager")
/*    */ public class FormDefHistoryManagerImpl
/*    */   extends BaseManager<String, FormDefHistory>
/*    */   implements FormDefHistoryManager
/*    */ {
/* 30 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   @Resource
/*    */   FormDefHistoryDao formDefHistoryDao;
/*    */   @Resource
/*    */   ICurrentContext iCurrentContext;
/*    */   @Autowired
/*    */   UserService userService;
/*    */   
/*    */   public FormDefHistory getWithoutHtml(String id) {
/* 40 */     FormDefHistory temp = this.formDefHistoryDao.getWithoutHtml(id);
/* 41 */     makeUserInfo(temp);
/* 42 */     return temp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(FormDefHistory entity) {
/* 47 */     entity.setUpdateBy(this.iCurrentContext.getCurrentUserId());
/* 48 */     this.formDefHistoryDao.updateByPrimaryKeySelective(entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FormDefHistory> query(QueryFilter queryFilter) {
/* 53 */     List<FormDefHistory> lst = this.dao.query(queryFilter);
/* 54 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet(lst));
/* 55 */     UserUtil.makeUserInfo(mapUser, lst);
/* 56 */     return lst;
/*    */   }
/*    */ 
/*    */   
/*    */   public FormDefHistory get(String entityId) {
/* 61 */     FormDefHistory temp = (FormDefHistory)this.dao.get(entityId);
/* 62 */     makeUserInfo(temp);
/* 63 */     return temp;
/*    */   }
/*    */   
/*    */   public void makeUserInfo(FormDefHistory temp) {
/* 67 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet((BaseModel)temp));
/* 68 */     UserUtil.makeUserInfo(mapUser, (BaseModel)temp);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormDefHistoryManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */