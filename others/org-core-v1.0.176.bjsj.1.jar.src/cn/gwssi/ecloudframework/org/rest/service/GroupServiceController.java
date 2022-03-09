/*     */ package cn.gwssi.ecloudframework.org.rest.service;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.PageResultDto;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.GroupQueryDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/org/groupService/default"})
/*     */ @Api(description = "用户组服务接口")
/*     */ public class GroupServiceController
/*     */ {
/*     */   @Autowired
/*     */   @Qualifier("defaultGroupService")
/*     */   GroupService groupService;
/*     */   @Autowired
/*     */   @Qualifier("defaultUserService")
/*     */   UserService userService;
/*     */   
/*     */   @ApiOperation(value = "获取用户下的某种组", notes = "根据用户ID，组类别  来获取相关的组")
/*     */   @PostMapping({"/getGroupsByGroupTypeUserId"})
/*     */   public ResultMsg<List<? extends IGroup>> getGroupsByGroupTypeUserId(@RequestParam("groupType") @ApiParam(value = "组织类型：org,post,role", required = true) String groupType, @RequestParam("userId") @ApiParam(value = "用户ID", required = true) String userId) {
/*  49 */     List<? extends IGroup> groupList = this.groupService.getGroupsByGroupTypeUserId(groupType, userId);
/*  50 */     return new ResultMsg(groupList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "通过用户账户，获取用户下的组织，结果以组类型为mapKey", notes = "根据用户账户，来获取相关的组，结果以组类型为mapKey")
/*     */   @GetMapping({"/getAllGroupByAccount/{account}"})
/*     */   public ResultMsg<Map<String, List<? extends IGroup>>> getAllGroupByAccount(@PathVariable("account") @ApiParam(value = "用户账户", required = true) String account) {
/*  63 */     Map<String, List<? extends IGroup>> map = this.groupService.getAllGroupByAccount(account);
/*  64 */     return new ResultMsg(map);
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
/*     */   @GetMapping({"/getAllGroupByUserId/{userId}"})
/*     */   @ApiOperation(value = "获取用户下的组织，结果以组类型为mapKey", notes = "根据用户id 获取用户当前所在的组，结果以组类型为mapKey")
/*     */   public ResultMsg<Map<String, List<? extends IGroup>>> getAllGroupByUserId(@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
/*  78 */     Map<String, List<? extends IGroup>> map = this.groupService.getAllGroupByUserId(userId);
/*  79 */     return new ResultMsg(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/getGroupsByUserId/{userId}"})
/*     */   @ApiOperation(value = "根据用户id，获取改用户所有组织列表", notes = "根据用户id 获取用户当前所在的组")
/*     */   public ResultMsg<List<? extends IGroup>> getGroupsByUserId(@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
/*  92 */     List<? extends IGroup> groupList = this.groupService.getGroupsByUserId(userId);
/*  93 */     return new ResultMsg(groupList);
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
/*     */   @PostMapping({"/getById"})
/*     */   @ApiOperation(value = "根据组织ID,获取组织详情信息", notes = "根据用户id,组织类型查找组织信息")
/*     */   public ResultMsg<IGroup> getById(@RequestParam("groupType") @ApiParam(value = "组织类型：GroupTypeConstant", required = true) String groupType, @RequestParam("groupId") @ApiParam(value = "组织ID", required = true) String groupId) {
/* 108 */     IGroup group = this.groupService.getById(groupType, groupId);
/* 109 */     return new ResultMsg(group);
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
/*     */   @PostMapping({"/getByCode"})
/*     */   @ApiOperation(value = "根据组织CODE,获取组织详情信息", notes = "根据用户CODE,组织类型查找组织信息")
/*     */   public ResultMsg<IGroup> getByCode(@RequestParam("groupType") @ApiParam(value = "组织类型：GroupTypeConstant", required = true) String groupType, @RequestParam("code") @ApiParam(value = "组织code", required = true) String code) {
/* 124 */     IGroup group = this.groupService.getByCode(groupType, code);
/* 125 */     return new ResultMsg(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "获取用户的主组织", notes = "获取用户的主岗位、若未配置主岗位默认获取查询的第一个组织")
/*     */   @GetMapping({"/getMainGroup/{userId}"})
/*     */   public ResultMsg<IGroup> getMainGroup(@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
/* 138 */     IGroup group = this.groupService.getMainGroup(userId);
/* 139 */     return new ResultMsg(group);
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
/*     */   @ApiOperation(value = "根据grouptype获取所有组信息（机构 角色 岗位）", notes = "根据grouptype获取所有组信息（机构 角色 岗位）")
/*     */   @GetMapping({"getGroupListByType"})
/*     */   public ResultMsg<List<? extends IGroup>> getGroupListByType(@RequestParam("groupType") @ApiParam(value = "组类型", required = true) String groupType) {
/* 153 */     List<? extends IGroup> groups = this.groupService.getGroupListByType(groupType);
/* 154 */     return new ResultMsg(groups);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(value = "查询组汇总接口(支持分页信息)", notes = "所有的组信息的查询")
/*     */   @PostMapping({"getGroupsByGroupQuery"})
/*     */   public PageResultDto getGroupsByGroupQuery(@RequestBody @ApiParam(value = "noPage:是否不要分页 默认true<br/>offset:分页查询偏移量<br/>limit:分页查询记录条数<br/>userId:用户id条件 例子：id1<br/>groupType:组类型（org 机构 post 岗位 role 角色 team 用户群组（常用组） custom_team 用户自定义组（常用组））<br/>groupCode:组织编号<br/>groupPath:组path条件<br/>resultType:返回结果类型（onlyGroupId 只返回组id,onlyGroupName 只返回组名）<br/>lstQueryConf:灵活开放查询<br/>&nbsp;&nbsp;例子[{name:'',type:'',value:''},{name:'',type:'',value:''}]<br/>&nbsp;&nbsp;组ids {name:'id_',type:'IN',value:'1,2'}<br/>&nbsp;&nbsp;组名 {name:'name_',type:'LK',value:'组名'}<br/>", required = true) GroupQueryDTO groupQuery) {
/* 181 */     return this.groupService.getGroupsByGroupQuery(groupQuery);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/getCurrentManagerOrgIds"})
/*     */   @ApiOperation("查询当前用户管理的机构ids")
/*     */   ResultMsg<String> getCurrentManagerOrgIds() {
/* 194 */     return new ResultMsg(this.groupService.getCurrentManagerOrgIds());
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/service/GroupServiceController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */