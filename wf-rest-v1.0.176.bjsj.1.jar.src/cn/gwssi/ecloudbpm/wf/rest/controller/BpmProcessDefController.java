/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*     */ import com.dstz.bpm.api.engine.action.button.ButtonFactory;
/*     */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.def.BpmVariableDef;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.EnumUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.constant.GroupTypeConstant;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/processDef/"})
/*     */ public class BpmProcessDefController
/*     */   extends ControllerTools
/*     */ {
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   IBusinessDataService bizDataService;
/*     */   @Resource
/*     */   GroupService userGroupService;
/*     */   @Autowired
/*     */   IBusinessObjectService businessObjectService;
/*     */   
/*     */   @RequestMapping({"getDefaultNodeBtns"})
/*     */   public List<Button> getDefaultNodeBtns(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  76 */     String nodeId = RequestUtil.getString(request, "nodeId");
/*  77 */     String defId = RequestUtil.getString(request, "defId");
/*  78 */     Boolean isDefault = Boolean.valueOf(RequestUtil.getBoolean(request, "isDefault", false));
/*     */     
/*  80 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/*  81 */     if ("instance_".equals(nodeId)) {
/*  82 */       return ButtonFactory.getInstanceButtons(isDefault.booleanValue());
/*     */     }
/*  84 */     return ButtonFactory.generateButtons(nodeDef, isDefault.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"variablesTree"})
/*     */   public Object variablesTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  93 */     String defId = RequestUtil.getString(request, "defId");
/*  94 */     String flowKey = RequestUtil.getString(request, "flowKey");
/*     */     
/*  96 */     if (StringUtil.isEmpty(defId)) {
/*  97 */       BpmDefinition definition = this.bpmDefinitionManager.getByKey(flowKey);
/*  98 */       defId = definition.getId();
/*     */     } 
/*     */ 
/*     */     
/* 102 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 103 */     JSONArray treeJA = new JSONArray();
/*     */ 
/*     */     
/* 106 */     List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
/* 107 */     if (CollectionUtil.isNotEmpty(boDefs)) {
/* 108 */       for (BpmDataModel boDef : boDefs) {
/* 109 */         List<JSONObject> jsonObject = this.businessObjectService.boTreeData(boDef.getCode());
/* 110 */         treeJA.addAll(jsonObject);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 115 */     JSONObject flowVarJson = getFlowVarJson(bpmProcessDef);
/* 116 */     if (flowVarJson != null) {
/* 117 */       treeJA.add(flowVarJson);
/*     */     }
/*     */     
/* 120 */     return treeJA;
/*     */   }
/*     */   
/*     */   private JSONObject getFlowVarJson(DefaultBpmProcessDef procDef) {
/* 124 */     List<BpmVariableDef> variables = procDef.getVariableList();
/* 125 */     JSONObject flowVariable = JSONObject.parseObject("{name:\"流程变量\",icon:\"fa fa-bold dark\",\"nodeType\":\"root\"}");
/*     */     
/* 127 */     JSONArray varList = new JSONArray();
/* 128 */     if (CollectionUtil.isNotEmpty(variables)) {
/* 129 */       for (BpmVariableDef variable : variables) {
/* 130 */         String name = variable.getName();
/* 131 */         variable.setName(variable.getKey());
/*     */         
/* 133 */         JSONObject obj = (JSONObject)JSONObject.toJSON(variable);
/* 134 */         obj.put("nodeType", "var");
/* 135 */         varList.add(obj);
/*     */       } 
/*     */     }
/*     */     
/* 139 */     flowVariable.put("children", varList);
/* 140 */     return flowVariable;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"getGroupTypes"})
/*     */   @CatchErr
/*     */   public ResultMsg<JSONArray> getGroupTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 147 */     return getSuccessResult(EnumUtil.toJSONArray(GroupTypeConstant.class, "bpm"));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"getSubjectVariable"})
/*     */   @CatchErr
/*     */   public JSONArray getSubjectVariable(@RequestParam String defId, @RequestParam(required = false) String boCodes) throws Exception {
/* 154 */     List<String> boCodeList = null;
/*     */     
/* 156 */     BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 157 */     if (def != null && StringUtil.isNotEmpty(def.getActDefId())) {
/* 158 */       DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 159 */       List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
/* 160 */       if (boDefs != null) {
/* 161 */         boCodeList = (List<String>)boDefs.stream().map(boDef -> boDef.getCode()).collect(Collectors.toList());
/*     */       }
/*     */     } 
/*     */     
/* 165 */     if (StringUtil.isNotEmpty(boCodes)) {
/* 166 */       boCodeList = Arrays.asList(boCodes.split(","));
/*     */     }
/*     */ 
/*     */     
/* 170 */     return getSubjectParam(boCodeList);
/*     */   }
/*     */   
/*     */   private JSONArray getSubjectParam(List<String> boCodeList) {
/* 174 */     JSONArray jsonArray = new JSONArray();
/*     */     
/* 176 */     if (CollectionUtil.isNotEmpty(boCodeList)) {
/* 177 */       for (String bocode : boCodeList) {
/* 178 */         IBusinessObject bo = this.businessObjectService.getFilledByKey(bocode);
/* 179 */         if (bo == null) {
/* 180 */           throw new BusinessException("业务对象丢失！请核查[" + bocode + "]");
/*     */         }
/*     */         
/* 183 */         for (IBusinessColumn column : bo.getRelation().getTable().getColumns()) {
/* 184 */           JSONObject jSONObject = new JSONObject();
/* 185 */           jSONObject.put("name", bo.getName() + "-" + column.getComment());
/* 186 */           jSONObject.put("key", bo.getKey() + "." + column.getKey());
/* 187 */           jsonArray.add(jSONObject);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 192 */     JSONObject json = JSONObject.parseObject("{name:\"发起人\",key:\"startorName\"}");
/* 193 */     jsonArray.add(json);
/*     */     
/* 195 */     JSONObject json1 = JSONObject.parseObject("{name:\"发起时间\",key:\"startDate\"}");
/* 196 */     jsonArray.add(json1);
/*     */     
/* 198 */     JSONObject json2 = JSONObject.parseObject("{name:\"流程标题\",key:\"title\"}");
/* 199 */     jsonArray.add(json2);
/* 200 */     return jsonArray;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getPorcessDef"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取流程配置", notes = "")
/*     */   public ResultMsg<BpmProcessDef> approveListTypeTree(@RequestParam("flowKey") @ApiParam("flowKey") String flowKey) throws Exception {
/* 207 */     BpmDefinition def = this.bpmDefinitionManager.getByKey(flowKey);
/* 208 */     BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(def.getId());
/* 209 */     return getSuccessResult(processDef);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getBpmDefinitonVo"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取流程定义VO", notes = "")
/*     */   public PageResult<BpmDefinitionVO> getBpmDefinitonVo(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 215 */     QueryFilter queryFilter = getQueryFilter(request);
/* 216 */     if (!queryFilter.getParams().containsKey("isVersions")) {
/* 217 */       queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/*     */     }
/*     */ 
/*     */     
/* 221 */     List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
/* 222 */     PageResult<BpmDefinitionVO> pageList = new PageResult(bpmDefinitionList);
/*     */ 
/*     */     
/* 225 */     List<BpmDefinitionVO> defVo = new ArrayList<>();
/* 226 */     for (BpmDefinition def : bpmDefinitionList) {
/* 227 */       if (StringUtil.isEmpty(def.getActDefId()))
/* 228 */         continue;  DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
/* 229 */       defVo.add(new BpmDefinitionVO(processDef, def));
/*     */     } 
/* 231 */     pageList.setRows(defVo);
/*     */     
/* 233 */     return pageList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getProcessDefList"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "分页获取流程配置", notes = "")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "offset", value = "offset"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "limit", value = "分页大小"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "排序字段"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "order", defaultValue = "ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "filter$VEQ", value = "其他过滤参数")})
/*     */   public PageResult<BpmProcessDef> getProcessDefList(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 241 */     QueryFilter queryFilter = getQueryFilter(request);
/* 242 */     if (!queryFilter.getParams().containsKey("isVersions")) {
/* 243 */       queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/*     */     }
/*     */ 
/*     */     
/* 247 */     List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
/* 248 */     PageResult<BpmProcessDef> pageList = new PageResult(bpmDefinitionList);
/*     */ 
/*     */     
/* 251 */     List<BpmProcessDef> processDef = new ArrayList<>();
/* 252 */     for (BpmDefinition def : bpmDefinitionList) {
/* 253 */       processDef.add(this.bpmProcessDefService.getBpmProcessDef(def.getId()));
/*     */     }
/* 255 */     pageList.setRows(processDef);
/*     */     
/* 257 */     return pageList;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmProcessDefController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */