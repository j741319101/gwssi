/*     */ package cn.gwssi.ecloudbpm.bus.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.rest.controller.vo.BusinessTableVO;
/*     */ import cn.gwssi.ecloudbpm.bus.service.BusDecentralizationService;
/*     */ import cn.gwssi.ecloudbpm.bus.util.BusinessTableCacheUtil;
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.core.util.BeanCopierUtils;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.db.tableoper.TableOperator;
/*     */ import cn.gwssi.ecloudframework.base.rest.BaseController;
/*     */ import cn.gwssi.ecloudframework.base.rest.tree.DeleteAuth;
/*     */ import cn.gwssi.ecloudframework.base.rest.util.RequestUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.ISysTreeNode;
/*     */ import cn.gwssi.ecloudframework.sys.api.service.ISysTreeNodeService;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bus/businessTable/"})
/*     */ public class BusinessTableController
/*     */   extends BaseController<BusinessTable>
/*     */   implements DeleteAuth
/*     */ {
/*     */   @Resource
/*     */   BusinessTableManager businessTableManager;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Autowired
/*     */   BusDecentralizationService decentralizationService;
/*     */   @Autowired
/*     */   ISysTreeNodeService sysTreeNodeService;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   @RequestMapping({"save"})
/*     */   @CatchErr("保存业务表失败")
/*     */   public ResultMsg<String> save(@RequestBody BusinessTable businessTable) {
/*  65 */     this.businessTableManager.save(businessTable);
/*  66 */     return getSuccessResult("保存业务表成功");
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
/*     */   @RequestMapping({"newSave"})
/*     */   @CatchErr("保存业务表失败")
/*     */   public ResultMsg<String> newSave(@RequestBody BusinessTable businessTable) {
/*  80 */     this.businessTableManager.save(businessTable);
/*  81 */     return getSuccessResult(businessTable.getId(), "保存业务表成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"getEntities"})
/*     */   public PageResult getEntities(HttpServletRequest request, HttpServletResponse response) {
/*  90 */     boolean fill = RequestUtil.getBoolean(request, "fill");
/*  91 */     List<BusinessTable> pageList = this.businessTableManager.getEntities(request.getParameter("groupId"));
/*  92 */     if (fill) {
/*  93 */       pageList.forEach(table -> {
/*     */             BusinessTable tablee = this.businessTableManager.getFilledByKey(table.getKey());
/*     */             table.setExternal(tablee.isExternal());
/*     */             table.setCreatedTable(tablee.isCreatedTable());
/*     */           });
/*     */     }
/*  99 */     return new PageResult(pageList);
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
/*     */ 
/*     */   
/*     */   @RequestMapping({"getObject"})
/*     */   @CatchErr(write2response = true, value = "获取BusinessTable异常")
/*     */   public ResultMsg<BusinessTable> getObject(HttpServletRequest request, HttpServletResponse response) {
/* 116 */     String id = RequestUtil.getString(request, "id");
/* 117 */     String key = RequestUtil.getString(request, "key");
/* 118 */     boolean fill = RequestUtil.getBoolean(request, "fill");
/* 119 */     BusinessTable table = null;
/* 120 */     if (StringUtil.isNotEmpty(id)) {
/* 121 */       table = (BusinessTable)this.businessTableManager.get(id);
/* 122 */     } else if (StringUtil.isNotEmpty(key)) {
/* 123 */       table = this.businessTableManager.getByKey(key);
/*     */     } 
/* 125 */     if (fill && table != null) {
/* 126 */       table = this.businessTableManager.getFilledByKey(table.getKey());
/*     */     }
/*     */     
/* 129 */     return getSuccessResult(table);
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
/*     */   
/*     */   @RequestMapping({"createTable"})
/*     */   @CatchErr(write2response = true, value = "建表失败")
/*     */   public ResultMsg<String> createTable(HttpServletRequest request, HttpServletResponse response) {
/* 145 */     String id = RequestUtil.getString(request, "id");
/* 146 */     BusinessTable businessTable = (BusinessTable)this.businessTableManager.get(id);
/* 147 */     businessTable = this.businessTableManager.getFilledByKey(businessTable.getKey());
/* 148 */     TableOperator tableOperator = this.businessTableManager.newTableOperator(businessTable);
/* 149 */     tableOperator.createTable();
/* 150 */     businessTable.setCreatedTable(true);
/* 151 */     BusinessTableCacheUtil.put(businessTable);
/* 152 */     return getSuccessResult("建表成功");
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
/*     */   @RequestMapping({"cleanCache"})
/*     */   @CatchErr(write2response = true, value = "清空缓存失败")
/*     */   public ResultMsg<String> cleanCache(HttpServletRequest request, HttpServletResponse response) {
/* 167 */     BusinessTableCacheUtil.clean();
/* 168 */     return getSuccessResult("建表成功");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 173 */     return "业务表";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/* 182 */     QueryFilter queryFilter = getQueryFilter(request);
/* 183 */     if (this.decentralizationService.decentralizationEnable("entity")) {
/* 184 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 185 */       List<String> lstOrgId = new ArrayList<>();
/* 186 */       if (null != user) {
/* 187 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 189 */       if (null != lstOrgId && lstOrgId.size() > 0) {
/* 190 */         queryFilter.addFilter("bus_table.org_id_", lstOrgId, QueryOP.IN);
/*     */       } else {
/* 192 */         queryFilter.addFilter("bus_table.org_id_", "", QueryOP.EQUAL);
/*     */       } 
/*     */     } 
/* 195 */     String sysTreeNodeId = RequestUtil.getString(request, "sysTreeNodeId");
/* 196 */     if (StringUtil.isNotEmpty(sysTreeNodeId)) {
/* 197 */       ISysTreeNode node = this.sysTreeNodeService.getById(sysTreeNodeId);
/* 198 */       if (null != node) {
/* 199 */         List<? extends ISysTreeNode> lstISysTreeNode = this.sysTreeNodeService.getTreeNodesByNodeId(node.getId());
/* 200 */         StringBuilder nodeIds = new StringBuilder();
/* 201 */         lstISysTreeNode.forEach(nodeTemp -> nodeIds.append(nodeTemp.getId()).append(","));
/* 202 */         if (nodeIds.length() > 0) {
/* 203 */           queryFilter.addFilter("bus_table.group_id_", nodeIds.toString(), QueryOP.IN);
/*     */         }
/*     */       } 
/*     */     } 
/* 207 */     List<BusinessTable> pageList = this.businessTableManager.query(queryFilter);
/* 208 */     return new PageResult(pageList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"listJsonForMetadata"})
/*     */   public PageResult listJsonForMetadata(HttpServletRequest request, HttpServletResponse response) {
/* 216 */     String metadataId = RequestUtil.getString(request, "metadataId");
/* 217 */     QueryFilter queryFilter = getQueryFilter(request);
/* 218 */     if (this.decentralizationService.decentralizationEnable("entity")) {
/* 219 */       IUser user = this.iCurrentContext.getCurrentUser();
/* 220 */       List<String> lstOrgId = new ArrayList<>();
/* 221 */       if (null != user) {
/* 222 */         lstOrgId = user.getManagerGroupIdList();
/*     */       }
/* 224 */       if (null != lstOrgId && lstOrgId.size() > 0) {
/* 225 */         queryFilter.addFilter("bus_table.org_id_", lstOrgId, QueryOP.IN);
/*     */       } else {
/* 227 */         queryFilter.addFilter("bus_table.org_id_", "", QueryOP.EQUAL);
/*     */       } 
/*     */     } 
/* 230 */     queryFilter.getParams().put("metadataId", metadataId);
/* 231 */     List<BusinessTable> pageList = this.businessTableManager.queryByMetadata(queryFilter);
/* 232 */     List<BusinessTableVO> pageListVO = BeanCopierUtils.transformList(pageList, BusinessTableVO.class);
/* 233 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(getUserIdSet(pageListVO));
/* 234 */     makeUserInfo(mapUser, pageListVO);
/* 235 */     return new PageResult(pageListVO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, List<BusinessTableVO> lstModel) {
/* 245 */     if (null != mapUser && mapUser.size() > 0 && null != lstModel && lstModel.size() > 0) {
/* 246 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               temp.setCreateUser((String)mapUser.get(temp.getCreateBy()));
/*     */               temp.setUpdateUser((String)mapUser.get(temp.getUpdateBy()));
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static Set<String> getUserIdSet(List<BusinessTableVO> lstModel) {
/* 256 */     Set<String> set = new HashSet<>();
/* 257 */     if (null != lstModel && lstModel.size() > 0) {
/* 258 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               set.add(temp.getCreateBy());
/*     */               set.add(temp.getUpdateBy());
/*     */             } 
/*     */           });
/*     */     }
/* 265 */     set.remove(null);
/* 266 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, BusinessTableVO model) {
/* 276 */     if (null != model) {
/* 277 */       model.setCreateUser(mapUser.get(model.getCreateBy()));
/* 278 */       model.setUpdateUser(mapUser.get(model.getUpdateBy()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean deleteAuth(String typeId) {
/* 285 */     Integer count = this.businessTableManager.countByTypeId(typeId);
/* 286 */     if (count.intValue() > 0) {
/* 287 */       return Boolean.valueOf(false);
/*     */     }
/* 289 */     return Boolean.valueOf(true);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/rest/controller/BusinessTableController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */