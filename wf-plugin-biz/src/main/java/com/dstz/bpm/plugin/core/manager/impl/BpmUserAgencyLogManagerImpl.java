/*    */ package com.dstz.bpm.plugin.core.manager.impl;
/*    */ 
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.base.manager.impl.BaseManager;
/*    */ import com.dstz.bpm.api.constant.OpinionStatus;
/*    */ import com.dstz.bpm.plugin.core.dao.BpmUserAgencyLogDao;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
/*    */
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
/*    */ import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.org.api.service.UserService;
/*    */ import com.dstz.sys.util.ContextUtil;
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
/*    */ @Service("bpmUserAgencyLogManager")
/*    */ public class BpmUserAgencyLogManagerImpl
/*    */   extends BaseManager<String, BpmUserAgencyLog>
/*    */   implements BpmUserAgencyLogManager
/*    */ {
/*    */   @Autowired
/*    */   private BpmUserAgencyLogDao bpmUserAgencyLogDao;
/*    */   @Autowired
/*    */   UserService userService;
/*    */   
/*    */   public int insertSelective(BpmUserAgencyLog record) {
/* 37 */     return this.bpmUserAgencyLogDao.insertSelective(record);
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateByPrimaryKeySelective(BpmUserAgencyLog record) {
/* 42 */     return this.bpmUserAgencyLogDao.updateByPrimaryKeySelective(record);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter queryFilter) {
/* 47 */     queryFilter.addFilter("config.CONFIG_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 48 */     return selectBpmAgencyLogList(queryFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmUserAgencyLogVO> selectBpmTargetUserAgencyLogList(QueryFilter queryFilter) {
/* 53 */     queryFilter.addFilter("config.TARGET_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 54 */     return selectBpmAgencyLogList(queryFilter);
/*    */   }
/*    */   
/*    */   private List<BpmUserAgencyLogVO> selectBpmAgencyLogList(QueryFilter queryFilter) {
/* 58 */     List<BpmUserAgencyLogVO> list = this.bpmUserAgencyLogDao.selectBpmUserAgencyLogList(queryFilter);
/* 59 */     list.forEach(logVo -> {
/*    */           IUser user = this.userService.getUserById(logVo.getConfigUserId());
/*    */           
/*    */           if (null == user) {
/*    */             logVo.setConfigUserName("用户丢失");
/*    */           } else {
/*    */             logVo.setConfigUserName(user.getFullname());
/*    */           } 
/*    */           
/*    */           if (OpinionStatus.AWAITING_CHECK.getKey().equals(logVo.getApproveStatus()) && StringUtil.isNotEmpty(logVo.getAssignInfo())) {
/*    */             StringBuilder assignNames = new StringBuilder();
/*    */             
/*    */             String[] assingInfoArr = logVo.getAssignInfo().split(",");
/*    */             
/*    */             for (String assingnInfo : assingInfoArr) {
/*    */               if (StringUtil.isEmpty(assingnInfo)) {
/*    */                 return;
/*    */               }
/*    */               String[] userInfo = assingnInfo.split("-");
/*    */               if (userInfo.length > 2) {
/*    */                 if (assignNames.length() > 0) {
/*    */                   assignNames.append(",");
/*    */                 }
/*    */                 assignNames.append(userInfo[1]);
/*    */               } 
/*    */             } 
/*    */             logVo.setApproverName(assignNames.toString());
/*    */           } 
/*    */         });
/* 88 */     return list;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmUserAgencyLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */