package com.dstz.bpm.wf.rest.controller;

import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.rest.ControllerTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dstz.bpm.service.BpmExtendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm"})
@Api(
   description = "流程实例服务接口"
)
public class BpmOptionsController extends ControllerTools {
   @Resource
   BpmExtendService bpmExtendService;
   @Resource
   private BpmTaskOpinionManager bpmTaskOpinionManager;
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;
   protected Logger logger = LoggerFactory.getLogger(this.getClass());

   @RequestMapping(
      value = {"/instanceBus/getInstanceOpinionWithProcess"},
      method = {RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "流程实例审批意见信息",
      notes = "通过流程实例ID 获取该流程实例下所有的审批意见、并按处理时间排序"
   )
   public ResultMsg getInstanceOpinionWithProcess(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId, @RequestParam(required = false) @ApiParam("组织ID") String orgId, @RequestParam(required = false) @ApiParam("审批状态") String status, @RequestParam(required = false,defaultValue = "false") @ApiParam("查询主子流程意见") Boolean extend) throws Exception {
      List<BpmTaskOpinionVO> taskOpinions = (List)this.getInstancesOpinion(instId, taskId, orgId, status, extend).getData();
      if (!CollectionUtils.isEmpty(taskOpinions)) {
         return extend ? this.getSuccessResult(this.bpmExtendService.packSingleInstanceOpitionWithOutSub(taskOpinions), "获取流程意见(包括外部子流程意见)成功") : this.getSuccessResult(this.bpmExtendService.packSingleInstanceOpition(taskOpinions), "获取流程意见成功");
      } else {
         return this.getSuccessResult("获取流程意见成功");
      }
   }

   @RequestMapping(
      value = {"instance/getInstanceOpinion"},
      method = {RequestMethod.GET, RequestMethod.POST}
   )
   @CatchErr
   @ApiOperation(
      value = "获取流程意见",
      notes = "通过流程实例ID 获取该流程实例下所有的审批意见、并按处理时间排序"
   )
   public ResultMsg getInstancesOpinion(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId, @RequestParam(required = false) @ApiParam("组织ID") String orgId, @RequestParam(required = false) @ApiParam("审批状态 awaiting_check:未审批，check:已审核") String status, @RequestParam(required = false,defaultValue = "true") @ApiParam("查询主子流程意见") Boolean extend) {
      return this.getSuccessResult(this.bpmTaskOpinionManager.getByInstsOpinion(instId, taskId, orgId, status, extend), "获取流程意见成功");
   }

   @RequestMapping(
      value = {"instance/getInstanceOpinionStruct"},
      method = {RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "流程实例审批意见信息结构",
      notes = "通过流程实例ID 获取该流程实例下所有的审批意见的展示结构"
   )
   public ResultMsg getInstanceOpinionStruct(@RequestParam @ApiParam("流程实例ID") String instId) {
      BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
      if (null != inst) {
         String defId = inst.getDefId();
         BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
         if (null != bpmDefinition) {
            String defSetting = bpmDefinition.getDefSetting();
            JSONObject jsonObject = JSON.parseObject(defSetting);
            JSONObject flowObject = (JSONObject)jsonObject.get("flow");
            JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
            JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
            return this.getSuccessResult(opinionObject, "获取审批意见结构成功");
         }
      }

      return this.getSuccessResult("获取审批意见结构成功");
   }

   @RequestMapping(
      value = {"instance/getInstanceOpinionStructAll"},
      method = {RequestMethod.GET}
   )
   @CatchErr
   @ApiOperation(
      value = "流程实例相关的所有流程的审批意见信息结构",
      notes = "通过流程实例ID 获取该流程实例相关的所有流程的（包括主子流程，外部子流程）所有的审批意见的展示结构"
   )
   public ResultMsg getInstanceOpinionStructAll(@RequestParam @ApiParam("流程实例ID") String instId) {
      Map<String, Object> map = new HashMap();
      List<BpmInstance> bpmInstanceList = this.bpmInstanceManager.listParentAndSubById(instId);
      Iterator var4 = bpmInstanceList.iterator();

      while(var4.hasNext()) {
         BpmInstance bpmInstance = (BpmInstance)var4.next();
         BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(bpmInstance.getId());
         if (null != inst) {
            String defId = inst.getDefId();
            BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
            if (null != bpmDefinition) {
               String defSetting = bpmDefinition.getDefSetting();
               JSONObject jsonObject = JSON.parseObject(defSetting);
               JSONObject flowObject = (JSONObject)jsonObject.get("flow");
               JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
               JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
               map.put(defId, opinionObject);
            }
         }
      }

      return this.getSuccessResult(map, "获取审批意见结构成功");
   }
}
