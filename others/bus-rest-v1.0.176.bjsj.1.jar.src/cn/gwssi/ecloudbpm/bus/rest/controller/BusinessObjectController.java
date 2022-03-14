/*     */ package cn.gwssi.ecloudbpm.bus.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusDecentralizationService;
/*     */ import cn.gwssi.ecloudbpm.bus.util.BusinessObjectCacheUtil;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.base.rest.tree.DeleteAuth;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.api.model.ISysTreeNode;
/*     */ import com.dstz.sys.api.service.ISysTreeNodeService;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bus/businessObject/"})
/*     */ public class BusinessObjectController
/*     */   extends BaseController<BusinessObject>
/*     */   implements DeleteAuth
/*     */ {
/*     */   @Resource
/*     */   BusinessObjectManager businessObjectManager;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Autowired
/*     */   BusDecentralizationService decentralizationService;
/*     */   @Autowired
/*     */   ISysTreeNodeService sysTreeNodeService;
/*     */   
/*     */   @RequestMapping({"getObject"})
/*     */   @CatchErr("获取businessObject异常")
/*     */   public ResultMsg<BusinessObject> getObject(HttpServletRequest request, HttpServletResponse response) {
/*  72 */     String id = RequestUtil.getString(request, "id");
/*  73 */     String key = RequestUtil.getString(request, "key");
/*     */     
/*  75 */     boolean fill = RequestUtil.getBoolean(request, "fill");
/*  76 */     BusinessObject object = null;
/*  77 */     if (StringUtil.isNotEmpty(id)) {
/*  78 */       object = (BusinessObject)this.businessObjectManager.get(id);
/*  79 */     } else if (StringUtil.isNotEmpty(key)) {
/*  80 */       object = this.businessObjectManager.getByKey(key);
/*     */     } 
/*  82 */     if (fill && object != null) {
/*  83 */       object = this.businessObjectManager.getFilledByKey(object.getKey());
/*     */     }
/*     */     
/*  86 */     return getSuccessResult(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultMsg<String> save(@RequestBody BusinessObject businessObject) throws Exception {
/*  91 */     ResultMsg<String> resultMsg = super.save((IDModel)businessObject);
/*  92 */     BusinessObjectCacheUtil.putDataSourcesKeys(businessObject.getKey(), this.businessObjectManager.getFilledByKey(businessObject.getKey()).calDataSourceKeys());
/*  93 */     return resultMsg;
/*     */   }
/*     */   
/*     */   @RequestMapping({"saveOverallArrangement"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> saveOverallArrangement(HttpServletRequest request) {
/*  99 */     String overallArrangement = RequestUtil.getString(request, "overallArrangement");
/* 100 */     String boCode = RequestUtil.getString(request, "boCode");
/* 101 */     this.businessObjectManager.updateOverallArrangementByCode(boCode, overallArrangement);
/* 102 */     return getSuccessResult();
/*     */   }
/*     */   
/*     */   @RequestMapping({"getOverallArrangement"})
/*     */   @CatchErr
/*     */   public ResultMsg<JSONObject> getOverallArrangement(@RequestParam String boCode) {
/* 108 */     String param = this.businessObjectManager.getOverallArrangementByCode(boCode);
/* 109 */     JSONObject object = new JSONObject();
/* 110 */     if (StringUtil.isNotEmpty(param)) {
/* 111 */       object = JSON.parseObject(param);
/*     */     }
/* 113 */     return getSuccessResult(object);
/*     */   }
/*     */   
/*     */   @RequestMapping({"getBoStruct"})
/*     */   @CatchErr("获取businessObject结构异常")
/*     */   public ResultMsg<JSONObject> getBoStruct(@RequestParam String key) {
/* 119 */     BusinessObject bo = this.businessObjectManager.getFilledByKey(key);
/* 120 */     return getSuccessResult(getBoStruct(bo.getRelation()));
/*     */   }
/*     */   
/*     */   @RequestMapping({"listJsonByKey"})
/*     */   @ResponseBody
/*     */   public List listJsonByKey(String tableKey, String tableGroupKey) {
/* 126 */     return this.businessObjectManager.listJsonByKey(tableKey, tableGroupKey);
/*     */   }
/*     */   
/*     */   @RequestMapping({"updateDiagramJson"})
/*     */   @ResponseBody
/*     */   public ResultMsg<String> updateDiagramJson(String id, String diagramJson) {
/* 132 */     this.businessObjectManager.updateDiagramJson(id, diagramJson);
/* 133 */     return getSuccessResult(id, "更新业务对象成功");
/*     */   }
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
/*     */   private JSONObject getBoStruct(BusTableRel rel) {
/* 146 */     JSONObject json = new JSONObject();
/* 147 */     rel.getTable().getColumns().forEach(column -> json.put(column.getKey(), (column.initValue() == null) ? "" : column.initValue()));
/* 148 */     rel.getChildren().forEach(r -> {
/*     */           if (BusTableRelType.ONE_TO_ONE.equalsWithKey(r.getType())) {
/*     */             json.put(r.getTableKey(), getBoStruct(r));
/*     */           } else {
/*     */             JSONObject jo = getBoStruct(r);
/*     */             
/*     */             JSONArray ja = new JSONArray();
/*     */             ja.add(jo);
/*     */             json.put(r.getTableKey() + "List", ja);
/*     */           } 
/*     */         });
/* 159 */     return json;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 164 */     return "业务对象";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/* 173 */     String resultType = RequestUtil.getString(request, "resultType");
/* 174 */     QueryFilter queryFilter = getQueryFilter(request);
/* 175 */     if (this.decentralizationService.decentralizationEnable("business")) {
/* 176 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 177 */       List<String> lstOrgId = new ArrayList<>();
/* 178 */       if (null != user) {
/* 179 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 181 */       if (null != lstOrgId && lstOrgId.size() > 0) {
/* 182 */         queryFilter.addFilter("bus_object.org_id_", lstOrgId, QueryOP.IN);
/*     */       } else {
/* 184 */         queryFilter.addFilter("bus_object.org_id_", "", QueryOP.EQUAL);
/*     */       } 
/*     */     } 
/* 187 */     String sysTreeNodeId = RequestUtil.getString(request, "sysTreeNodeId");
/* 188 */     if (StringUtil.isNotEmpty(sysTreeNodeId)) {
/* 189 */       ISysTreeNode node = this.sysTreeNodeService.getById(sysTreeNodeId);
/* 190 */       if (null != node) {
/* 191 */         List<? extends ISysTreeNode> lstISysTreeNode = this.sysTreeNodeService.getTreeNodesByNodeId(node.getId());
/* 192 */         StringBuilder nodeIds = new StringBuilder();
/* 193 */         lstISysTreeNode.forEach(nodeTemp -> nodeIds.append(nodeTemp.getId()).append(","));
/* 194 */         if (nodeIds.length() > 0) {
/* 195 */           queryFilter.addFilter("bus_object.group_id_", nodeIds.toString(), QueryOP.IN);
/*     */         }
/*     */       } 
/*     */     } 
/* 199 */     if (StringUtils.isNotEmpty(resultType)) {
/* 200 */       queryFilter.addParamsFilter("resultType", resultType);
/*     */     }
/* 202 */     List<BusinessObject> pageList = this.businessObjectManager.query(queryFilter);
/* 203 */     return new PageResult(pageList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean deleteAuth(String typeId) {
/* 209 */     Integer count = this.businessObjectManager.countByTypeId(typeId);
/* 210 */     if (count.intValue() > 0) {
/* 211 */       return Boolean.valueOf(false);
/*     */     }
/* 213 */     return Boolean.valueOf(true);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/rest/controller/BusinessObjectController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */