/*     */ package cn.gwssi.ecloudbpm.wf.plugin.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.CarbonCopyStatus;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.manager.BpmCarbonCopyRecordManager;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyReceive;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.core.model.BpmCarbonCopyRecord;
/*     */ import cn.gwssi.ecloudbpm.wf.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.rest.ControllerTools;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RequestMapping({"/bpm/carbonCopy"})
/*     */ @RestController
/*     */ @Api(description = "流程抄送服务接口")
/*     */ public class BpmCarbonCopyController
/*     */   extends ControllerTools
/*     */ {
/*     */   @Autowired
/*     */   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
/*     */   @Autowired
/*     */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/updateRead"})
/*     */   @ApiOperation(value = "更新已读", notes = "")
/*     */   public ResultMsg<Void> updateRead(@RequestParam("id") String id) {
/*  62 */     Set<String> ids = (Set<String>)Arrays.<String>stream(StringUtils.split(id, ",")).collect(Collectors.toSet());
/*  63 */     IUser currentUser = ContextUtil.getCurrentUser();
/*  64 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/*  65 */     bpmCarbonCopyReceive.setStatus(CarbonCopyStatus.READ.getKey());
/*  66 */     bpmCarbonCopyReceive.setReceiveUserId(currentUser.getUserId());
/*  67 */     bpmCarbonCopyReceive.setUpdateBy(currentUser.getUserId());
/*  68 */     bpmCarbonCopyReceive.setUpdateTime(new Date());
/*  69 */     this.bpmCarbonCopyReceiveManager.updateRead(bpmCarbonCopyReceive, ids);
/*  70 */     return new ResultMsg(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @GetMapping({"/{id}/record"})
/*     */   @ApiOperation(value = "获取抄送", notes = "")
/*     */   public ResultMsg<BpmCarbonCopyRecord> getCarbonCopyRecord(@PathVariable("id") String id) {
/*  84 */     BpmCarbonCopyRecord bpmCarbonCopyRecord = (BpmCarbonCopyRecord)this.bpmCarbonCopyRecordManager.get(id);
/*  85 */     return new ResultMsg(bpmCarbonCopyRecord);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/receiveList"})
/*     */   @ApiOperation(value = "获取用户的抄送记录", notes = "")
/*     */   public PageResult<BpmUserReceiveCarbonCopyRecordVO> receiveList(HttpServletRequest request) {
/*  98 */     QueryFilter queryFilter = getQueryFilter(request);
/*  99 */     queryFilter.addFilter("a.receive_user_id", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 100 */     List<BpmUserReceiveCarbonCopyRecordVO> list = this.bpmCarbonCopyReceiveManager.listUserReceive(queryFilter);
/* 101 */     return new PageResult(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/instReceiveList"})
/*     */   @ApiOperation(value = "获取所有抄送记录", notes = "")
/*     */   public PageResult<BpmUserReceiveCarbonCopyRecordVO> instReceiveList(HttpServletRequest request) {
/* 114 */     QueryFilter queryFilter = getQueryFilter(request);
/* 115 */     List<BpmUserReceiveCarbonCopyRecordVO> list = this.bpmCarbonCopyReceiveManager.listUserReceive(queryFilter);
/* 116 */     return new PageResult(list);
/*     */   }
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/opinion"})
/*     */   public ResultMsg<BpmTaskOpinion> getOpinion(@RequestParam("id") String id) {
/* 122 */     BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(id);
/* 123 */     return new ResultMsg(opinion);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmCarbonCopyController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */