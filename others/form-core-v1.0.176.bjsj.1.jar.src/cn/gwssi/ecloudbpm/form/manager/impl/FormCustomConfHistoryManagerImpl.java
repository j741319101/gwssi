/*    */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.form.dao.FormCustomConfHistoryDao;
/*    */ import cn.gwssi.ecloudbpm.form.manager.FormCustomConfHistoryManager;
/*    */ import cn.gwssi.ecloudbpm.form.model.FormCustomConfHistory;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*    */ import cn.gwssi.ecloudframework.base.core.util.UserUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class FormCustomConfHistoryManagerImpl
/*    */   extends BaseManager<String, FormCustomConfHistory>
/*    */   implements FormCustomConfHistoryManager
/*    */ {
/* 30 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   @Resource
/*    */   FormCustomConfHistoryDao formCustomConfHistoryDao;
/*    */   @Autowired
/*    */   UserService userService;
/*    */   
/*    */   public void save(FormCustomConfHistory formCustomConfHistory) {
/* 38 */     saveEntity(formCustomConfHistory);
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
/*    */   public void saveEntity(FormCustomConfHistory formCustomConfHistory) {
/* 50 */     if (StringUtils.isNotEmpty(formCustomConfHistory.getId())) {
/* 51 */       this.formCustomConfHistoryDao.updateByPrimaryKeySelective(formCustomConfHistory);
/*    */     } else {
/* 53 */       this.formCustomConfHistoryDao.create(formCustomConfHistory);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FormCustomConfHistory> query(QueryFilter queryFilter) {
/* 59 */     List<FormCustomConfHistory> lst = this.dao.query(queryFilter);
/* 60 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet(lst));
/* 61 */     UserUtil.makeUserInfo(mapUser, lst);
/* 62 */     return lst;
/*    */   }
/*    */ 
/*    */   
/*    */   public FormCustomConfHistory get(String entityId) {
/* 67 */     FormCustomConfHistory temp = (FormCustomConfHistory)this.dao.get(entityId);
/* 68 */     makeUserInfo(temp);
/* 69 */     return temp;
/*    */   }
/*    */   
/*    */   public void makeUserInfo(FormCustomConfHistory temp) {
/* 73 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet((BaseModel)temp));
/* 74 */     UserUtil.makeUserInfo(mapUser, (BaseModel)temp);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormCustomConfHistoryManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */