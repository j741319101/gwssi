/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
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
/*     */ import com.dstz.bus.api.model.IBusinessColumn;
/*     */ import com.dstz.bus.api.model.IBusinessObject;
/*     */ import com.dstz.bus.api.service.IBusinessDataService;
/*     */ import com.dstz.bus.api.service.IBusinessObjectService;
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
/*  77 */     String nodeId = RequestUtil.getString(request, "nodeId");
/*  78 */     String defId = RequestUtil.getString(request, "defId");
/*  79 */     Boolean isDefault = Boolean.valueOf(RequestUtil.getBoolean(request, "isDefault", false));
/*     */     
/*  81 */     BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
/*  82 */     if ("instance_".equals(nodeId)) {
/*  83 */       return ButtonFactory.getInstanceButtons(isDefault.booleanValue());
/*     */     }
/*  85 */     return ButtonFactory.generateButtons(nodeDef, isDefault.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"variablesTree"})
/*     */   public Object variablesTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  94 */     String defId = RequestUtil.getString(request, "defId");
/*  95 */     String flowKey = RequestUtil.getString(request, "flowKey");
/*     */     
/*  97 */     if (StringUtil.isEmpty(defId)) {
/*  98 */       BpmDefinition definition = this.bpmDefinitionManager.getByKey(flowKey);
/*  99 */       defId = definition.getId();
/*     */     } 
/*     */ 
/*     */     
/* 103 */     DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 104 */     JSONArray treeJA = new JSONArray();
/*     */ 
/*     */     
/* 107 */     List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
/* 108 */     if (CollectionUtil.isNotEmpty(boDefs)) {
/* 109 */       for (BpmDataModel boDef : boDefs) {
/* 110 */         List<JSONObject> jsonObject = this.businessObjectService.boTreeData(boDef.getCode());
/* 111 */         treeJA.addAll(jsonObject);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 116 */     JSONObject flowVarJson = getFlowVarJson(bpmProcessDef);
/* 117 */     if (flowVarJson != null) {
/* 118 */       treeJA.add(flowVarJson);
/*     */     }
/*     */     
/* 121 */     return treeJA;
/*     */   }
/*     */   
/*     */   private JSONObject getFlowVarJson(DefaultBpmProcessDef procDef) {
/* 125 */     List<BpmVariableDef> variables = procDef.getVariableList();
/* 126 */     JSONObject flowVariable = JSONObject.parseObject("{name:\"流程变量\",icon:\"fa fa-bold dark\",\"nodeType\":\"root\"}");
/*     */     
/* 128 */     JSONArray varList = new JSONArray();
/* 129 */     if (CollectionUtil.isNotEmpty(variables)) {
/* 130 */       for (BpmVariableDef variable : variables) {
/* 131 */         String name = variable.getName();
/* 132 */         variable.setName(variable.getKey());
/*     */         
/* 134 */         JSONObject obj = (JSONObject)JSONObject.toJSON(variable);
/* 135 */         obj.put("nodeType", "var");
/* 136 */         varList.add(obj);
/*     */       } 
/*     */     }
/*     */     
/* 140 */     flowVariable.put("children", varList);
/* 141 */     return flowVariable;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"getGroupTypes"})
/*     */   @CatchErr
/*     */   public ResultMsg<JSONArray> getGroupTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 148 */     return getSuccessResult(EnumUtil.toJSONArray(GroupTypeConstant.class));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"getSubjectVariable"})
/*     */   @CatchErr
/*     */   public JSONArray getSubjectVariable(@RequestParam String defId, @RequestParam(required = false) String boCodes) throws Exception {
/* 155 */     List<String> boCodeList = null;
/*     */     
/* 157 */     BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/* 158 */     if (def != null && StringUtil.isNotEmpty(def.getActDefId())) {
/* 159 */       DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
/* 160 */       List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
/* 161 */       if (boDefs != null) {
/* 162 */         boCodeList = (List<String>)boDefs.stream().map(boDef -> boDef.getCode()).collect(Collectors.toList());
/*     */       }
/*     */     } 
/*     */     
/* 166 */     if (StringUtil.isNotEmpty(boCodes)) {
/* 167 */       boCodeList = Arrays.asList(boCodes.split(","));
/*     */     }
/*     */ 
/*     */     
/* 171 */     return getSubjectParam(boCodeList);
/*     */   }
/*     */   
/*     */   private JSONArray getSubjectParam(List<String> boCodeList) {
/* 175 */     JSONArray jsonArray = new JSONArray();
/*     */     
/* 177 */     if (CollectionUtil.isNotEmpty(boCodeList)) {
/* 178 */       for (String bocode : boCodeList) {
/* 179 */         IBusinessObject bo = this.businessObjectService.getFilledByKey(bocode);
/* 180 */         if (bo == null) {
/* 181 */           throw new BusinessException("业务对象丢失！请核查[" + bocode + "]");
/*     */         }
/*     */         
/* 184 */         for (IBusinessColumn column : bo.getRelation().getTable().getColumns()) {
/* 185 */           JSONObject jSONObject = new JSONObject();
/* 186 */           jSONObject.put("name", bo.getName() + "-" + column.getComment());
/* 187 */           jSONObject.put("key", bo.getKey() + "." + column.getKey());
/* 188 */           jsonArray.add(jSONObject);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 193 */     JSONObject json = JSONObject.parseObject("{name:\"发起人\",key:\"startorName\"}");
/* 194 */     jsonArray.add(json);
/*     */     
/* 196 */     JSONObject json1 = JSONObject.parseObject("{name:\"发起时间\",key:\"startDate\"}");
/* 197 */     jsonArray.add(json1);
/*     */     
/* 199 */     JSONObject json2 = JSONObject.parseObject("{name:\"流程标题\",key:\"title\"}");
/* 200 */     jsonArray.add(json2);
/* 201 */     return jsonArray;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"getPorcessDef"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取流程配置", notes = "")
/*     */   public ResultMsg<BpmProcessDef> approveListTypeTree(@RequestParam("flowKey") @ApiParam("flowKey") String flowKey) throws Exception {
/* 208 */     BpmDefinition def = this.bpmDefinitionManager.getByKey(flowKey);
/* 209 */     BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(def.getId());
/* 210 */     return getSuccessResult(processDef);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"getBpmDefinitonVo"}, method = {RequestMethod.POST})
/*     */   @ApiOperation(value = "获取流程定义VO", notes = "")
/*     */   public PageResult<BpmDefinitionVO> getBpmDefinitonVo(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 216 */     QueryFilter queryFilter = getQueryFilter(request);
/* 217 */     if (!queryFilter.getParams().containsKey("isVersions")) {
/* 218 */       queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/*     */     }
/*     */ 
/*     */     
/* 222 */     List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
/* 223 */     PageResult<BpmDefinitionVO> pageList = new PageResult(bpmDefinitionList);
/*     */ 
/*     */     
/* 226 */     List<BpmDefinitionVO> defVo = new ArrayList<>();
/* 227 */     for (BpmDefinition def : bpmDefinitionList) {
/* 228 */       DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmProcessDefController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */