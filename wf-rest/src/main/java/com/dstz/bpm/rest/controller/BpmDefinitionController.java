/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.model.form.BpmForm;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.rest.vo.UserTaskNodeVO;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/definition"})
/*     */ public class BpmDefinitionController
/*     */   extends BaseController<BpmDefinition>
/*     */ {
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionManager;
/*     */   @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Autowired
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  68 */     String formKey = request.getParameter("formKey");
/*  69 */     QueryFilter queryFilter = getQueryFilter(request);
/*  70 */     if (!queryFilter.getParams().containsKey("isVersions")) {
/*  71 */       queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/*     */     }
/*     */ 
/*     */     
/*  75 */     List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
/*  76 */     if (StringUtils.isNotEmpty(formKey)) {
/*  77 */       for (int i = 0; i < bpmDefinitionList.size(); i++) {
/*     */         try {
/*  79 */           if (StringUtils.isNotEmpty(((BpmDefinition)bpmDefinitionList.get(i)).getActDeployId())) {
/*     */             
/*  81 */             DefaultBpmProcessDef defaultBpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(((BpmDefinition)bpmDefinitionList.get(i)).getId());
/*  82 */             String defId = ((BpmDefinition)bpmDefinitionList.get(i)).getKey();
/*  83 */             BpmForm bpmForm = defaultBpmProcessDef.getGlobalForm();
/*  84 */             if (bpmForm == null || !bpmForm.getFormValue().equals(formKey)) {
/*  85 */               bpmDefinitionList.remove(i);
/*  86 */               i--;
/*     */             } 
/*     */           } else {
/*  89 */             bpmDefinitionList.remove(i);
/*  90 */             i--;
/*     */           } 
/*  92 */         } catch (Exception e) {
/*  93 */           bpmDefinitionList.remove(i);
/*  94 */           i--;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  99 */     return new PageResult(bpmDefinitionList);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"save"})
/*     */   @CatchErr(write2response = true, value = "保存流程定义失败")
/*     */   public ResultMsg<String> save(@RequestBody BpmDefinition bpmDefinition) throws Exception {
/* 106 */     this.bpmDefinitionManager.create(bpmDefinition);
/*     */     
/* 108 */     return getSuccessResult(bpmDefinition.getActModelId(), "创建成功");
/*     */   }
/*     */   
/*     */   @RequestMapping({"setMain"})
/*     */   public ResultMsg<String> setMain(HttpServletRequest request) throws Exception {
/* 113 */     String definitionId = RequestUtil.getString(request, "id");
/*     */     
/* 115 */     this.bpmDefinitionManager.setDefinition2Main(definitionId);
/* 116 */     return getSuccessResult("操作成功！");
/*     */   }
/*     */   
/*     */   @RequestMapping({"clearSysCache"})
/*     */   @CatchErr("清除缓存失败")
/*     */   public ResultMsg<String> clearCache() throws Exception {
/* 122 */     ((ICache)AppUtil.getBean(ICache.class)).clearAll();
/* 123 */     this.bpmDefinitionManager.clearBpmnModelCache(null);
/* 124 */     return getSuccessResult("成功清除所有系统缓存");
/*     */   }
/*     */   
/*     */   @ApiOperation(value = "获取流程定义数量", notes = "")
/*     */   @RequestMapping({"getDefNumByTree"})
/*     */   public ResultMsg<List<Map>> getDefNumByTree() {
/* 130 */     return getSuccessResult(this.bpmDefinitionManager.getDefNumByTree());
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 135 */     return "流程定义";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getAllUserTaskNode"}, produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
/*     */   public ResultMsg<List<UserTaskNodeVO>> getAllUserTaskNode(@RequestBody FlowRequestParam flowRequestParam) {
/* 147 */     String defId = "";
/* 148 */     if (StringUtils.isNotEmpty(flowRequestParam.getDefId())) {
/* 149 */       defId = flowRequestParam.getDefId();
/* 150 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getDefKey())) {
/* 151 */       BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(flowRequestParam.getDefKey());
/* 152 */       if (bpmDefinition == null) {
/* 153 */         return ResultMsg.ERROR(String.format("流程定义(%s)不存在", new Object[] { flowRequestParam.getDefKey() }));
/*     */       }
/* 155 */       defId = bpmDefinition.getId();
/* 156 */     } else if (StringUtils.isNotEmpty(flowRequestParam.getInstanceId())) {
/* 157 */       BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceManager.get(flowRequestParam.getInstanceId());
/* 158 */       if (bpmInstance == null) {
/* 159 */         return ResultMsg.ERROR(String.format("流程实例不存在(%s)", new Object[] { flowRequestParam.getInstanceId() }));
/*     */       }
/* 161 */       defId = bpmInstance.getDefId();
/*     */     } else {
/* 163 */       return ResultMsg.ERROR("参数为空, 请至少传递一个参数(defId、defKey、instanceId)");
/*     */     } 
/* 165 */     List<UserTaskNodeVO> userNodeS = new ArrayList<>();
/* 166 */     this.bpmProcessDefService.getAllNodeDef(defId).stream().filter(bpmNode -> (bpmNode.getType() == NodeType.USERTASK)).forEach(bpmNode -> userNodeS.add(new UserTaskNodeVO(bpmNode.getNodeId(), bpmNode.getName())));
/*     */ 
/*     */     
/* 169 */     return ResultMsg.SUCCESS(userNodeS);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmDefinitionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */