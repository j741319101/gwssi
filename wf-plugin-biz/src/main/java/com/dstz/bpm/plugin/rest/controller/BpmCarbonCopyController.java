package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.api.constant.CarbonCopyStatus;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmTaskOpinion;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyRecordManager;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
import com.dstz.bpm.plugin.core.model.BpmCarbonCopyRecord;
import com.dstz.bpm.plugin.vo.BpmUserReceiveCarbonCopyRecordVO;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.ControllerTools;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/carbonCopy"})
@RestController
@Api(
   description = "流程抄送服务接口"
)
public class BpmCarbonCopyController extends ControllerTools {
   @Autowired
   private BpmCarbonCopyRecordManager bpmCarbonCopyRecordManager;
   @Autowired
   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;

   @CatchErr
   @RequestMapping({"/updateRead"})
   @ApiOperation(
      value = "更新已读",
      notes = ""
   )
   public ResultMsg<Void> updateRead(@RequestParam("id") String id) {
      Set<String> ids = (Set)Arrays.stream(StringUtils.split(id, ",")).collect(Collectors.toSet());
      IUser currentUser = ContextUtil.getCurrentUser();
      BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
      bpmCarbonCopyReceive.setStatus(CarbonCopyStatus.READ.getKey());
      bpmCarbonCopyReceive.setReceiveUserId(currentUser.getUserId());
      bpmCarbonCopyReceive.setUpdateBy(currentUser.getUserId());
      bpmCarbonCopyReceive.setUpdateTime(new Date());
      this.bpmCarbonCopyReceiveManager.updateRead(bpmCarbonCopyReceive, ids);
      return new ResultMsg((Object)null);
   }

   @CatchErr
   @GetMapping({"/{id}/record"})
   @ApiOperation(
      value = "获取抄送",
      notes = ""
   )
   public ResultMsg<BpmCarbonCopyRecord> getCarbonCopyRecord(@PathVariable("id") String id) {
      BpmCarbonCopyRecord bpmCarbonCopyRecord = (BpmCarbonCopyRecord)this.bpmCarbonCopyRecordManager.get(id);
      return new ResultMsg(bpmCarbonCopyRecord);
   }

   @RequestMapping({"/receiveList"})
   @ApiOperation(
      value = "获取用户的抄送记录",
      notes = ""
   )
   public PageResult<BpmUserReceiveCarbonCopyRecordVO> receiveList(HttpServletRequest request) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      queryFilter.addFilter("a.receive_user_id", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      List<BpmUserReceiveCarbonCopyRecordVO> list = this.bpmCarbonCopyReceiveManager.listUserReceive(queryFilter);
      return new PageResult(list);
   }

   @RequestMapping({"/instReceiveList"})
   @ApiOperation(
      value = "获取所有抄送记录",
      notes = ""
   )
   public PageResult<BpmUserReceiveCarbonCopyRecordVO> instReceiveList(HttpServletRequest request) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      List<BpmUserReceiveCarbonCopyRecordVO> list = this.bpmCarbonCopyReceiveManager.listUserReceive(queryFilter);
      return new PageResult(list);
   }

   @CatchErr
   @RequestMapping({"/opinion"})
   public ResultMsg<BpmTaskOpinion> getOpinion(@RequestParam("id") String id) {
      BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(id);
      return new ResultMsg(opinion);
   }
}
