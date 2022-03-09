/*    */ package com.dstz.bpm.plugin.rest.controller;
/*    */ 
/*    */ import com.dstz.base.api.aop.annotion.CatchErr;
/*    */ import com.dstz.base.api.query.QueryFilter;
/*    */ import com.dstz.base.api.query.QueryOP;
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.db.model.page.PageResult;
/*    */ import com.dstz.base.rest.ControllerTools;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
/*    */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
/*    */ import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import java.util.Arrays;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestParam;
/*    */ import org.springframework.web.bind.annotation.RestController;
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
/*    */ @RequestMapping({"/bpm/carbonCopy"})
/*    */ @RestController
/*    */ public class BpmCarbonCopyController
/*    */   extends ControllerTools
/*    */ {
/*    */   @Autowired
/*    */   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*    */   @Autowired
/*    */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*    */   
/*    */   @CatchErr
/*    */   @RequestMapping({"/updateRead"})
/*    */   public ResultMsg<String> updateRead(@RequestParam("id") String id) {
/* 52 */     Set<String> ids = (Set<String>)Arrays.<String>stream(StringUtils.split(id, ",")).collect(Collectors.toSet());
/* 53 */     IUser currentUser = ContextUtil.getCurrentUser();
/* 54 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 55 */     bpmCarbonCopyReceive.setRead(Boolean.TRUE);
/* 56 */     bpmCarbonCopyReceive.setReceiveUserId(currentUser.getUserId());
/* 57 */     bpmCarbonCopyReceive.setUpdateBy(currentUser.getUserId());
/* 58 */     bpmCarbonCopyReceive.setUpdateTime(new Date());
/* 59 */     this.bpmCarbonCopyReceiveManager.updateRead(bpmCarbonCopyReceive, ids);
/* 60 */     return new ResultMsg("更新成功");
/*    */   }
/*    */   @RequestMapping({"/updateReadUser"})
/*    */   public ResultMsg<String> updateReadUser() {
/* 64 */     this.bpmCarbonCopyReceiveManager.updateReadByUser();
/* 65 */     return new ResultMsg("更新成功");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CatchErr
/*    */   @GetMapping({"/{id}/record"})
/*    */   public ResultMsg<BpmCarbonCopyRecord> getCarbonCopyRecord(@PathVariable("id") String id) {
/* 76 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = (BpmCarbonCopyRecord)this.bpmCarbonCopyRecordManager.get(id);
/* 77 */     return new ResultMsg(bpmCarbonCopyRecord);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @RequestMapping({"/receiveList"})
/*    */   public PageResult<BpmUserReceiveCarbonCopyRecordVO> receiveList(HttpServletRequest request) {
/* 88 */     QueryFilter queryFilter = getQueryFilter(request);
/* 89 */     queryFilter.addFilter("a.receive_user_id", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 90 */     List<BpmUserReceiveCarbonCopyRecordVO> list = this.bpmCarbonCopyReceiveManager.listUserReceive(queryFilter);
/* 91 */     return new PageResult(list);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmCarbonCopyController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */