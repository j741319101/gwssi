/*     */ package com.dstz.org.rest.service;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.response.impl.PageResultDto;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.IUserRole;
/*     */ import com.dstz.org.api.model.dto.UserQueryDTO;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.org.core.manager.UserManager;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
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
/*     */ @RequestMapping({"/org/userService/default"})
/*     */ @Api(description = "用户信息服务接口")
/*     */ public class UserServiceController
/*     */ {
/*     */   @Resource
/*     */   UserManager userManager;
/*     */   @Autowired
/*     */   @Qualifier("defaultUserService")
/*     */   UserService userService;
/*     */   
/*     */   @GetMapping({"/getUserById/{userId}"})
/*     */   @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取用户的对象")
/*     */   public ResultMsg<IUser> getUserById(@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
/*  51 */     IUser user = this.userService.getUserById(userId);
/*  52 */     return new ResultMsg(user);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/getUserByAccount/{account}"})
/*     */   @ApiOperation(value = "获取用户信息", notes = "根据用户帐号获取用户对象")
/*     */   @CatchErr
/*     */   public ResultMsg<IUser> getUserByAccount(@PathVariable("account") @ApiParam(value = "用户账户", required = true) String account) {
/*  66 */     IUser user = this.userService.getUserByAccount(account);
/*  67 */     if (null == user) {
/*  68 */       throw new BusinessException("账号不存在");
/*     */     }
/*  70 */     return new ResultMsg(user);
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
/*     */   @PostMapping({"/getUserListByGroup"})
/*     */   @ApiOperation(value = "获取组用户", notes = "根据某组织下的用户")
/*     */   public ResultMsg<List<? extends IUser>> getUserListByGroup(@RequestParam("groupType") @ApiParam(value = "组类型：org,post,role", required = true) String groupType, @RequestParam("groupId") @ApiParam(value = "组ID", required = true) String groupId) {
/*  85 */     List<? extends IUser> userList = this.userService.getUserListByGroup(groupType, groupId);
/*  86 */     return new ResultMsg(userList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/getUserRole/{userId}"})
/*     */   @ApiOperation(value = "获取组用户的角色", notes = "根据用户ID 获取该用户的角色")
/*     */   public ResultMsg<List<? extends IUserRole>> getUserRole(@PathVariable("userId") String userId) {
/*  98 */     List<? extends IUserRole> userRoleList = this.userService.getUserRole(userId);
/*  99 */     return new ResultMsg(userRoleList);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/getUsersByUserQuery"})
/*     */   @ApiOperation(value = "查询用户汇总接口(支持分页信息)", notes = "根据查询对象获取的用户集合")
/*     */   public PageResultDto getUsersByUserQuery(@RequestBody @ApiParam(value = "noPage:是否不要分页 默认true<br/>offset:分页查询偏移量<br/>limit:分页查询记录条数<br/>orgIds:机构id条件 例子：id1,id2<br/>orgCodes:机构code条件 例子：code1,code2<br/>orgPath:机构path条件<br/>orgHasChild:是否包含子机构条件<br/>roleIds:角色id条件<br/>roleCodes:角色code条件<br/>postIds:岗位id条件<br/>postCodes:岗位code条件<br/>teamIds:用户群组id条件 例子：id1,id2<br/>teamCustomIds:常用组id条件 例子：id1,id2<br/>userSelectHistory:是否最近选择true|false<br/>resultType:返回结果类型 onlyUserId|onlyUserAccount<br/>status:状态 1启动|0不启用 默认1<br/>lstQueryConf:灵活开放查询<br/>&nbsp;&nbsp;例子[{name:'',type:'',value:''},{name:'',type:'',value:''}]<br/>&nbsp;&nbsp;用户ids {name:'tuser.id_',type:'IN',value:'1,2'}<br/>&nbsp;&nbsp;用户名 {name:'tuser.fullname_',type:'LK',value:'张三'}<br/>&nbsp;&nbsp;用户账号 {name:'tuser.account_',type:'IN',value:'1,2'}<br/>", required = true) UserQueryDTO userQuery) {
/* 134 */     return this.userService.getUsersByUserQuery(userQuery);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/service/UserServiceController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */