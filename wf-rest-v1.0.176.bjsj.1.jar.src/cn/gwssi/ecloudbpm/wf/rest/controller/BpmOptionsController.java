/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.service.BpmExtendService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm"})
/*     */ @Api(description = "流程实例服务接口")
/*     */ public class BpmOptionsController
/*     */   extends ControllerTools
/*     */ {
/*     */   @Resource
/*     */   BpmExtendService bpmExtendService;
/*     */   @Resource
/*     */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*  54 */   protected Logger logger = LoggerFactory.getLogger(getClass());
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
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/instanceBus/getInstanceOpinionWithProcess"}, method = {RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例审批意见信息", notes = "通过流程实例ID 获取该流程实例下所有的审批意见、并按处理时间排序")
/*     */   public ResultMsg getInstanceOpinionWithProcess(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId, @RequestParam(required = false) @ApiParam("组织ID") String orgId, @RequestParam(required = false) @ApiParam("审批状态") String status, @RequestParam(required = false, defaultValue = "false") @ApiParam("查询主子流程意见") Boolean extend) throws Exception {
/*  72 */     List<BpmTaskOpinionVO> taskOpinions = (List<BpmTaskOpinionVO>)getInstancesOpinion(instId, taskId, orgId, status, extend).getData();
/*  73 */     if (!CollectionUtils.isEmpty(taskOpinions)) {
/*  74 */       if (extend.booleanValue()) {
/*  75 */         return getSuccessResult(this.bpmExtendService.packSingleInstanceOpitionWithOutSub(taskOpinions), "获取流程意见(包括外部子流程意见)成功");
/*     */       }
/*  77 */       return getSuccessResult(this.bpmExtendService.packSingleInstanceOpition(taskOpinions), "获取流程意见成功");
/*     */     } 
/*     */     
/*  80 */     return getSuccessResult("获取流程意见成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/getInstanceOpinion"}, method = {RequestMethod.GET, RequestMethod.POST})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "获取流程意见", notes = "通过流程实例ID 获取该流程实例下所有的审批意见、并按处理时间排序")
/*     */   public ResultMsg getInstancesOpinion(@RequestParam @ApiParam("流程实例ID") String instId, @RequestParam(required = false) @ApiParam("任务ID") String taskId, @RequestParam(required = false) @ApiParam("组织ID") String orgId, @RequestParam(required = false) @ApiParam("审批状态 awaiting_check:未审批，check:已审核") String status, @RequestParam(required = false, defaultValue = "true") @ApiParam("查询主子流程意见") Boolean extend) {
/*  92 */     return getSuccessResult(this.bpmTaskOpinionManager.getByInstsOpinion(instId, taskId, orgId, status, extend), "获取流程意见成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"instance/getInstanceOpinionStruct"}, method = {RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例审批意见信息结构", notes = "通过流程实例ID 获取该流程实例下所有的审批意见的展示结构")
/*     */   public ResultMsg getInstanceOpinionStruct(@RequestParam @ApiParam("流程实例ID") String instId) {
/*  99 */     BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 100 */     if (null != inst) {
/* 101 */       String defId = inst.getDefId();
/* 102 */       BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 103 */       if (null != bpmDefinition) {
/* 104 */         String defSetting = bpmDefinition.getDefSetting();
/* 105 */         JSONObject jsonObject = JSON.parseObject(defSetting);
/* 106 */         JSONObject flowObject = (JSONObject)jsonObject.get("flow");
/* 107 */         JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
/* 108 */         JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
/* 109 */         return getSuccessResult(opinionObject, "获取审批意见结构成功");
/*     */       } 
/*     */     } 
/* 112 */     return getSuccessResult("获取审批意见结构成功");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"instance/getInstanceOpinionStructAll"}, method = {RequestMethod.GET})
/*     */   @CatchErr
/*     */   @ApiOperation(value = "流程实例相关的所有流程的审批意见信息结构", notes = "通过流程实例ID 获取该流程实例相关的所有流程的（包括主子流程，外部子流程）所有的审批意见的展示结构")
/*     */   public ResultMsg getInstanceOpinionStructAll(@RequestParam @ApiParam("流程实例ID") String instId) {
/* 119 */     Map<String, Object> map = new HashMap<>();
/*     */     
/* 121 */     List<BpmInstance> bpmInstanceList = this.bpmInstanceManager.listParentAndSubById(instId);
/* 122 */     for (BpmInstance bpmInstance : bpmInstanceList) {
/* 123 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(bpmInstance.getId());
/* 124 */       if (null != inst) {
/* 125 */         String defId = inst.getDefId();
/* 126 */         BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 127 */         if (null != bpmDefinition) {
/* 128 */           String defSetting = bpmDefinition.getDefSetting();
/* 129 */           JSONObject jsonObject = JSON.parseObject(defSetting);
/* 130 */           JSONObject flowObject = (JSONObject)jsonObject.get("flow");
/* 131 */           JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
/* 132 */           JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
/*     */           
/* 134 */           map.put(defId, opinionObject);
/*     */         } 
/*     */       } 
/*     */     } 
/* 138 */     return getSuccessResult(map, "获取审批意见结构成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmOptionsController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */