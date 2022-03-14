/*     */ package com.dstz.org.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.constant.RelationTypeConstant;
/*     */ import com.dstz.org.api.constant.UserTypeConstant;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.org.core.manager.OrgRelationManager;
/*     */ import com.dstz.org.core.model.OrgRelation;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/org/orgRelation/default"})
/*     */ @Api("用户组织关系服务接口")
/*     */ public class OrgRelationController
/*     */   extends BaseController<OrgRelation>
/*     */ {
/*     */   @Resource
/*     */   OrgRelationManager orgRelationManager;
/*     */   @Autowired
/*     */   ICurrentContext currentContext;
/*     */   @Autowired
/*     */   @Qualifier("defaultUserService")
/*     */   UserService userService;
/*     */   
/*     */   protected String getModelDesc() {
/*  54 */     return "用户组织关系";
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
/*     */   @RequestMapping({"queryGroupUser"})
/*     */   @CatchErr
/*     */   public PageResult queryGroupUser(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "path", required = false) String path, @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "userType", required = false) String userType, @RequestParam(value = "relationType", required = false) String relationType, @RequestParam(name = "groupId", required = false) String groupId) {
/*  68 */     if (StringUtils.isEmpty(relationType)) {
/*  69 */       relationType = RelationTypeConstant.GROUP_USER.getKey();
/*     */     }
/*     */     
/*  72 */     if (StringUtils.isEmpty(userType)) {
/*  73 */       userType = UserTypeConstant.NORMAL.key();
/*     */     }
/*     */     
/*  76 */     QueryFilter filter = getQueryFilter(request);
/*  77 */     filter.getParams().put("relationType", relationType);
/*  78 */     filter.getParams().put("orderBySql", "userCreateTime desc,relation.IS_MASTER_ desc");
/*     */     
/*  80 */     if (StringUtils.isNotEmpty(path) && 
/*  81 */       RelationTypeConstant.GROUP_USER.getKey().equals(relationType)) {
/*  82 */       filter.addFilter("tgroup.path_", path, QueryOP.RIGHT_LIKE);
/*     */     }
/*     */     
/*  85 */     if (StringUtils.isNotEmpty(groupId)) {
/*  86 */       filter.addFilter("relation.group_id_", groupId, QueryOP.EQUAL);
/*     */     }
/*  88 */     if (StringUtils.isNotEmpty(userName)) {
/*  89 */       filter.addFilter("tuser.fullname_", userName, QueryOP.LIKE);
/*     */     }
/*  91 */     filter.addFilter("relation.type_", relationType, QueryOP.EQUAL);
/*  92 */     filter.addFilter("tuser.type_", userType, QueryOP.IN);
/*     */     
/*  94 */     List<OrgRelation> pageList = this.orgRelationManager.query(filter);
/*  95 */     return new PageResult(pageList);
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
/*     */   
/*     */   @PostMapping({"setMaster"})
/*     */   @ApiOperation("设置主组织")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "groupId", value = "组织id"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "userId", value = "用户id"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "groupType", value = "组织类型")})
/*     */   @CatchErr
/*     */   public ResultMsg<String> setMaster(HttpServletRequest request, HttpServletResponse response) {
/* 115 */     String groupId = RequestUtil.getString(request, "groupId");
/* 116 */     String userId = RequestUtil.getString(request, "userId");
/* 117 */     String groupType = RequestUtil.getString(request, "groupType");
/* 118 */     this.orgRelationManager.updateUserGroupRelationIsMaster(groupId, userId, groupType);
/*     */     
/* 120 */     return getSuccessResult("设置用户主组织成功!");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"disable"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> disable(HttpServletRequest request, HttpServletResponse response) {
/* 127 */     String id = RequestUtil.getRQString(request, "id");
/* 128 */     this.orgRelationManager.changeStatus(id, 0);
/*     */     
/* 130 */     return getSuccessResult("禁用成功!");
/*     */   }
/*     */   
/*     */   @RequestMapping({"enable"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> enable(HttpServletRequest request, HttpServletResponse response) {
/* 136 */     String id = RequestUtil.getRQString(request, "id");
/* 137 */     this.orgRelationManager.changeStatus(id, 1);
/*     */     
/* 139 */     return getSuccessResult("启用成功!");
/*     */   }
/*     */   
/*     */   @RequestMapping({"saveGroupUserRel"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> saveGroupUserRel(HttpServletRequest request, HttpServletResponse response) {
/* 145 */     String groupId = RequestUtil.getRQString(request, "groupId");
/* 146 */     String[] roleIds = RequestUtil.getStringAryByStr(request, "roleIds");
/* 147 */     String[] userIds = RequestUtil.getStringAryByStr(request, "userIds");
/* 148 */     if (ArrayUtil.isEmpty((Object[])userIds)) {
/* 149 */       throw new BusinessError("请选择用户");
/*     */     }
/*     */     
/* 152 */     this.orgRelationManager.saveUserGroupRelation(groupId, roleIds, userIds);
/*     */     
/* 154 */     return getSuccessResult("添加成功");
/*     */   }
/*     */   
/*     */   @RequestMapping({"saveRoleUsers"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> saveRoleUsers(HttpServletRequest request, HttpServletResponse response) {
/* 160 */     String roleId = RequestUtil.getString(request, "roleId");
/* 161 */     String[] userIds = RequestUtil.getStringAryByStr(request, "userIds");
/*     */     
/* 163 */     int i = this.orgRelationManager.saveRoleUsers(roleId, userIds);
/*     */     
/* 165 */     return getSuccessResult(i + "条用户角色添加成功");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"roleJson"})
/*     */   public PageResult roleJson(HttpServletRequest request, HttpServletResponse response) {
/* 173 */     String roleId = RequestUtil.getRQString(request, "roleId", "查询 角色ID不能为空");
/*     */     
/* 175 */     QueryFilter filter = getQueryFilter(request);
/* 176 */     filter.addFilter("role.id_", roleId, QueryOP.EQUAL);
/* 177 */     filter.addFilter("relation.type_", RelationTypeConstant.USER_ROLE.getKey(), QueryOP.NOT_EQUAL);
/*     */     
/* 179 */     List<OrgRelation> pageList = this.orgRelationManager.query(filter);
/* 180 */     return new PageResult(pageList);
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
/*     */   @RequestMapping({"removeCheck"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> removeCheck(HttpServletRequest request) {
/* 197 */     String groupId = RequestUtil.getString(request, "groupId");
/* 198 */     this.orgRelationManager.removeCheck(groupId);
/* 199 */     return getSuccessResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"batchModityUserOrg"})
/*     */   public ResultMsg<String> batchModifyUserOrg(@RequestBody List<OrgRelation> relations) {
/* 210 */     if (CollectionUtil.isNotEmpty(relations)) {
/* 211 */       this.orgRelationManager.modifyUserOrg(relations);
/*     */     }
/* 213 */     return getSuccessResult();
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
/*     */   @RequestMapping({"modifyAllUserGroup"})
/*     */   public ResultMsg<String> modifyAllUserGroup(@RequestParam(value = "path", required = false) String path, @RequestParam(value = "oldGroupId", required = false) String oldGroupId, @RequestParam("newGroupId") String newGroupId, @RequestParam("type") String type) {
/* 229 */     this.orgRelationManager.modifyAllUserGroup(path, oldGroupId, newGroupId, type);
/* 230 */     return getSuccessResult();
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
/*     */   @PostMapping({"batchAdd"})
/*     */   @ApiOperation("批量添加关系")
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> batchAdd(@RequestBody @ApiParam(value = "关系列表", required = true) List<OrgRelation> relations) {
/* 246 */     if (CollectionUtil.isNotEmpty(relations)) {
/* 247 */       this.orgRelationManager.batchAdd(relations);
/*     */     }
/* 249 */     return getSuccessResult();
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
/*     */   @PostMapping({"batchRemove"})
/*     */   @ApiOperation("批量删除关系")
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> batchRemove(@RequestBody @ApiParam(value = "关系列表", required = true) List<OrgRelation> relations) {
/* 265 */     if (CollectionUtil.isNotEmpty(relations)) {
/* 266 */       this.orgRelationManager.batchRemove(relations);
/*     */     }
/* 268 */     return getSuccessResult();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/controller/OrgRelationController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */