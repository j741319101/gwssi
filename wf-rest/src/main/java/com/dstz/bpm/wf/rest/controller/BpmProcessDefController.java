package com.dstz.bpm.wf.rest.controller;

import com.dstz.bpm.api.engine.action.button.ButtonFactory;
import com.dstz.bpm.api.model.def.BpmDataModel;
import com.dstz.bpm.api.model.def.BpmProcessDef;
import com.dstz.bpm.api.model.def.BpmVariableDef;
import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.bpm.api.model.nodedef.Button;
import com.dstz.bpm.api.service.BpmProcessDefService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.vo.BpmDefinitionVO;
import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.EnumUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.ControllerTools;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.bus.api.model.IBusinessColumn;
import com.dstz.bus.api.model.IBusinessObject;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.bus.api.service.IBusinessObjectService;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.service.GroupService;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/processDef/"})
public class BpmProcessDefController extends ControllerTools {
   @Resource
   BpmDefinitionManager bpmDefinitionManager;
   @Resource
   BpmProcessDefService bpmProcessDefService;
   @Resource
   IBusinessDataService bizDataService;
   @Resource
   GroupService userGroupService;
   @Autowired
   IBusinessObjectService businessObjectService;

   @RequestMapping({"getDefaultNodeBtns"})
   public List<Button> getDefaultNodeBtns(HttpServletRequest request, HttpServletResponse response) throws Exception {
      String nodeId = RequestUtil.getString(request, "nodeId");
      String defId = RequestUtil.getString(request, "defId");
      Boolean isDefault = RequestUtil.getBoolean(request, "isDefault", false);
      BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, nodeId);
      return "instance_".equals(nodeId) ? ButtonFactory.getInstanceButtons(isDefault) : ButtonFactory.generateButtons(nodeDef, isDefault);
   }

   @RequestMapping({"variablesTree"})
   public Object variablesTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
      String defId = RequestUtil.getString(request, "defId");
      String flowKey = RequestUtil.getString(request, "flowKey");
      if (StringUtil.isEmpty(defId)) {
         BpmDefinition definition = this.bpmDefinitionManager.getByKey(flowKey);
         defId = definition.getId();
      }

      DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
      JSONArray treeJA = new JSONArray();
      List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
      if (CollectionUtil.isNotEmpty(boDefs)) {
         Iterator var8 = boDefs.iterator();

         while(var8.hasNext()) {
            BpmDataModel boDef = (BpmDataModel)var8.next();
            List<JSONObject> jsonObject = this.businessObjectService.boTreeData(boDef.getCode());
            treeJA.addAll(jsonObject);
         }
      }

      JSONObject flowVarJson = this.getFlowVarJson(bpmProcessDef);
      if (flowVarJson != null) {
         treeJA.add(flowVarJson);
      }

      return treeJA;
   }

   private JSONObject getFlowVarJson(DefaultBpmProcessDef procDef) {
      List<BpmVariableDef> variables = procDef.getVariableList();
      JSONObject flowVariable = JSONObject.parseObject("{name:\"流程变量\",icon:\"fa fa-bold dark\",\"nodeType\":\"root\"}");
      JSONArray varList = new JSONArray();
      if (CollectionUtil.isNotEmpty(variables)) {
         Iterator var5 = variables.iterator();

         while(var5.hasNext()) {
            BpmVariableDef variable = (BpmVariableDef)var5.next();
            String name = variable.getName();
            variable.setName(variable.getKey());
            JSONObject obj = (JSONObject)JSONObject.toJSON(variable);
            obj.put("nodeType", "var");
            varList.add(obj);
         }
      }

      flowVariable.put("children", varList);
      return flowVariable;
   }

   @RequestMapping({"getGroupTypes"})
   @CatchErr
   public ResultMsg<JSONArray> getGroupTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
      return this.getSuccessResult(EnumUtil.toJSONArray(GroupTypeConstant.class, "bpm"));
   }

   @RequestMapping({"getSubjectVariable"})
   @CatchErr
   public JSONArray getSubjectVariable(@RequestParam String defId, @RequestParam(required = false) String boCodes) throws Exception {
      List<String> boCodeList = null;
      BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
      if (def != null && StringUtil.isNotEmpty(def.getActDefId())) {
         DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(defId);
         List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
         if (boDefs != null) {
            boCodeList = (List)boDefs.stream().map((boDef) -> {
               return boDef.getCode();
            }).collect(Collectors.toList());
         }
      }

      if (StringUtil.isNotEmpty(boCodes)) {
         boCodeList = Arrays.asList(boCodes.split(","));
      }

      return this.getSubjectParam(boCodeList);
   }

   private JSONArray getSubjectParam(List<String> boCodeList) {
      JSONArray jsonArray = new JSONArray();
      if (CollectionUtil.isNotEmpty(boCodeList)) {
         Iterator var3 = boCodeList.iterator();

         while(var3.hasNext()) {
            String bocode = (String)var3.next();
            IBusinessObject bo = this.businessObjectService.getFilledByKey(bocode);
            if (bo == null) {
               throw new BusinessException("业务对象丢失！请核查[" + bocode + "]");
            }

            Iterator var6 = bo.getRelation().getTable().getColumns().iterator();

            while(var6.hasNext()) {
               IBusinessColumn column = (IBusinessColumn)var6.next();
               JSONObject json = new JSONObject();
               json.put("name", bo.getName() + "-" + column.getComment());
               json.put("key", bo.getKey() + "." + column.getKey());
               jsonArray.add(json);
            }
         }
      }

      JSONObject json = JSONObject.parseObject("{name:\"发起人\",key:\"startorName\"}");
      jsonArray.add(json);
      JSONObject json1 = JSONObject.parseObject("{name:\"发起时间\",key:\"startDate\"}");
      jsonArray.add(json1);
      JSONObject json2 = JSONObject.parseObject("{name:\"流程标题\",key:\"title\"}");
      jsonArray.add(json2);
      return jsonArray;
   }

   @RequestMapping(
      value = {"getPorcessDef"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "获取流程配置",
      notes = ""
   )
   public ResultMsg<BpmProcessDef> approveListTypeTree(@RequestParam("flowKey") @ApiParam("flowKey") String flowKey) throws Exception {
      BpmDefinition def = this.bpmDefinitionManager.getByKey(flowKey);
      BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(def.getId());
      return this.getSuccessResult(processDef);
   }

   @RequestMapping(
      value = {"getBpmDefinitonVo"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "获取流程定义VO",
      notes = ""
   )
   public PageResult<BpmDefinitionVO> getBpmDefinitonVo(HttpServletRequest request, HttpServletResponse response) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      if (!queryFilter.getParams().containsKey("isVersions")) {
         queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
      }

      List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
      PageResult pageList = new PageResult(bpmDefinitionList);
      List<BpmDefinitionVO> defVo = new ArrayList();
      Iterator var7 = bpmDefinitionList.iterator();

      while(var7.hasNext()) {
         BpmDefinition def = (BpmDefinition)var7.next();
         if (!StringUtil.isEmpty(def.getActDefId())) {
            DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.bpmProcessDefService.getBpmProcessDef(def.getId());
            defVo.add(new BpmDefinitionVO(processDef, def));
         }
      }

      pageList.setRows(defVo);
      return pageList;
   }

   @RequestMapping(
      value = {"getProcessDefList"},
      method = {RequestMethod.POST}
   )
   @ApiOperation(
      value = "分页获取流程配置",
      notes = ""
   )
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "offset",
   value = "offset"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "limit",
   value = "分页大小"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "sort",
   value = "排序字段"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "order",
   value = "order",
   defaultValue = "ASC"
), @ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "filter$VEQ",
   value = "其他过滤参数"
)})
   public PageResult<BpmProcessDef> getProcessDefList(HttpServletRequest request, HttpServletResponse response) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      if (!queryFilter.getParams().containsKey("isVersions")) {
         queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
      }

      List<BpmDefinition> bpmDefinitionList = this.bpmDefinitionManager.query(queryFilter);
      PageResult pageList = new PageResult(bpmDefinitionList);
      List<BpmProcessDef> processDef = new ArrayList();
      Iterator var7 = bpmDefinitionList.iterator();

      while(var7.hasNext()) {
         BpmDefinition def = (BpmDefinition)var7.next();
         processDef.add(this.bpmProcessDefService.getBpmProcessDef(def.getId()));
      }

      pageList.setRows(processDef);
      return pageList;
   }
}
