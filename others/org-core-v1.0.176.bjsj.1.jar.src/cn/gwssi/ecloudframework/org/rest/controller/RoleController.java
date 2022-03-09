/*     */ package cn.gwssi.ecloudframework.org.rest.controller;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.OperateLog;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.rest.BaseController;
/*     */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.GroupManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.OrgRelationManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.RoleManager;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Role;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
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
/*     */ @RestController
/*     */ @RequestMapping({"/org/role/default"})
/*     */ public class RoleController
/*     */   extends BaseController<Role>
/*     */ {
/*     */   @Resource
/*     */   RoleManager roleManager;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationMananger;
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */   @Autowired
/*     */   ICurrentContext currentContext;
/*     */   
/*     */   protected String getModelDesc() {
/*  50 */     return "角色";
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
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   @RequestMapping({"save"})
/*     */   public ResultMsg<String> save(@RequestBody Role role) throws Exception {
/*  65 */     boolean isExist = this.roleManager.isRoleExist(role);
/*  66 */     if (isExist) {
/*  67 */       throw new BusinessMessage("角色在系统中已存在!");
/*     */     }
/*  69 */     return super.save((IDModel)role);
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
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   @RequestMapping({"remove"})
/*     */   public ResultMsg<String> remove(String id) throws Exception {
/*  84 */     this.orgRelationMananger.removeCheck(id);
/*  85 */     return super.remove(id);
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
/*     */   @OperateLog
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/* 100 */     String name = request.getParameter("name");
/* 101 */     String userId = request.getParameter("userId");
/* 102 */     String excludeUserId = request.getParameter("excludeUserId");
/* 103 */     QueryFilter filter = getQueryFilter(request);
/* 104 */     if (StringUtils.isNotEmpty(name)) {
/* 105 */       filter.addFilter("trole.name_", name, QueryOP.LIKE);
/*     */     }
/* 107 */     if (StringUtils.isNotEmpty(userId)) {
/* 108 */       filter.getParams().put("userId", userId);
/*     */     }
/* 110 */     if (StringUtils.isNotEmpty(excludeUserId)) {
/* 111 */       filter.getParams().put("excludeUserId", excludeUserId);
/*     */     }
/* 113 */     List<Role> pageList = this.roleManager.query(filter);
/* 114 */     return new PageResult(pageList);
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
/*     */   @RequestMapping({"setStatus"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> setStatus(@RequestParam(name = "id") String id, @RequestParam(name = "status") Integer status) {
/* 129 */     this.roleManager.setStatus(id, status);
/* 130 */     return getSuccessResult();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/controller/RoleController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */