/*     */ package cn.gwssi.ecloudframework.org.service;
/*     */ import cn.gwssi.ecloudframework.base.api.Page;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.Direction;
/*     */ import cn.gwssi.ecloudframework.base.api.query.FieldSort;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.PageResultDto;
/*     */ import cn.gwssi.ecloudframework.base.core.util.BeanCopierUtils;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultFieldSort;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultPage;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.constant.RelationTypeConstant;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUserRole;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.QueryConfDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.UserDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.UserQueryDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.UserRoleDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.BpmUserManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.OrgRelationManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.PostManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.UserManager;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Group;
/*     */ import cn.gwssi.ecloudframework.org.core.model.OrgRelation;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Post;
/*     */ import cn.gwssi.ecloudframework.org.core.model.User;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.ibatis.session.RowBounds;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service("defaultUserService")
/*     */ public class ABUserService implements UserService {
/*  47 */   private Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   UserManager userManager;
/*     */   
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */   @Resource
/*     */   PostManager postManager;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationMananger;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationManager;
/*     */   @Resource
/*     */   BpmUserManager bpmUserManager;
/*     */   
/*     */   public IUser getUserById(String userId) {
/*  66 */     User user = (User)this.userManager.get(userId);
/*  67 */     return (IUser)getUserDetailInfo(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public IUser getUserByAccount(String account) {
/*  72 */     User user = this.userManager.getByAccount(account);
/*  73 */     return (IUser)getUserDetailInfo(user);
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
/*     */   public UserDTO getUserDetailInfo(User user) {
/*  85 */     UserDTO userDTO = (UserDTO)BeanCopierUtils.transformBean(user, UserDTO.class);
/*  86 */     if (null != user) {
/*  87 */       user.getOrgRelationList().stream().filter(orgRelation -> (RelationTypeConstant.POST_USER.getKey().equals(orgRelation.getType()) && null != orgRelation.getIsMaster() && 1 == orgRelation.getIsMaster().intValue())).findFirst().ifPresent(postRelation -> {
/*     */             userDTO.setPostId(postRelation.getPostId());
/*     */             userDTO.setPostName(postRelation.getPostName());
/*     */             Post post = (Post)this.postManager.get(postRelation.getGroupId());
/*     */             if (null != post) {
/*     */               userDTO.setPostCode(post.getCode());
/*     */             }
/*     */           });
/*  95 */       user.getOrgRelationList().stream().filter(orgRelation -> (RelationTypeConstant.GROUP_USER.getKey().equals(orgRelation.getType()) && null != orgRelation.getIsMaster() && 1 == orgRelation.getIsMaster().intValue())).findFirst().ifPresent(orgRelation -> {
/*     */             userDTO.setOrgId(orgRelation.getGroupId());
/*     */             userDTO.setOrgName(orgRelation.getGroupName());
/*     */             Group group = (Group)this.groupManager.get(orgRelation.getGroupId());
/*     */             if (null != group) {
/*     */               userDTO.setOrgCode(group.getCode());
/*     */             }
/*     */           });
/* 103 */       List<OrgRelation> lstOrgRelation = this.orgRelationMananger.getUserRole(user.getUserId());
/* 104 */       List<IUserRole> lstUserRoleDTO = new ArrayList<>();
/* 105 */       lstOrgRelation.forEach(temp -> lstUserRoleDTO.add(new UserRoleDTO(temp.getRoleId(), temp.getRoleName(), temp.getRoleAlias())));
/* 106 */       userDTO.setRoles(lstUserRoleDTO);
/*     */     } 
/* 108 */     return userDTO;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<? extends IUser> getUserListByGroup(String groupType, String groupId) {
/* 114 */     RelationTypeConstant relationType = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
/* 115 */     if (relationType == null) {
/* 116 */       throw new BusinessException(groupType + "查找不到对应用户的类型！");
/*     */     }
/* 118 */     List<User> userList = null;
/* 119 */     if (RelationTypeConstant.GROUP_USER.getKey().equals(relationType.getKey())) {
/* 120 */       userList = this.bpmUserManager.getUserListByOrgId(groupId);
/* 121 */     } else if (RelationTypeConstant.POST_USER.getKey().equals(relationType.getKey())) {
/* 122 */       userList = this.bpmUserManager.getUserListByPostId(groupId);
/*     */     } else {
/* 124 */       userList = this.bpmUserManager.getUserListByRelation(groupId, relationType.getKey());
/*     */     } 
/*     */     
/* 127 */     if (CollectionUtil.isNotEmpty(userList)) {
/* 128 */       return BeanCopierUtils.transformList(userList, UserDTO.class);
/*     */     }
/*     */     
/* 131 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IUserRole> getUserRole(String userId) {
/* 136 */     List<OrgRelation> orgRelationList = this.orgRelationMananger.getUserRole(userId);
/* 137 */     List<UserRoleDTO> userRoleList = new ArrayList<>();
/*     */     
/* 139 */     for (OrgRelation orgRelation : orgRelationList) {
/* 140 */       UserRoleDTO userRole = new UserRoleDTO(orgRelation.getRoleId(), orgRelation.getUserId(), orgRelation.getUserName(), orgRelation.getRoleName());
/* 141 */       userRole.setAlias(orgRelation.getRoleAlias());
/* 142 */       userRoleList.add(userRole);
/*     */     } 
/*     */     
/* 145 */     return (List)userRoleList;
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
/*     */   public List<? extends IUser> getAllUser() {
/* 157 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 158 */     defaultQueryFilter.addFilter("tuser.status_", Integer.valueOf(1), QueryOP.EQUAL);
/*     */     
/* 160 */     List<User> userList = this.userManager.query((QueryFilter)defaultQueryFilter);
/*     */     
/* 162 */     return BeanCopierUtils.transformList(userList, UserDTO.class);
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
/*     */   public List<? extends IUser> getUsersByUsername(String username) {
/* 175 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 176 */     defaultQueryFilter.addFilter("tuser.status_", Integer.valueOf(1), QueryOP.EQUAL);
/* 177 */     if (StringUtils.isNotEmpty(username)) {
/* 178 */       defaultQueryFilter.addFilter("tuser.fullname_", username, QueryOP.LIKE);
/*     */     }
/*     */     
/* 181 */     List<User> userList = this.userManager.query((QueryFilter)defaultQueryFilter);
/*     */     
/* 183 */     return BeanCopierUtils.transformList(userList, UserDTO.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<? extends IUser> getUserListByGroupPath(String path) {
/* 189 */     List<User> userList = this.userManager.getUserListByGroupPath(path);
/* 190 */     return BeanCopierUtils.transformList(userList, UserDTO.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public IUser getUserByOpenid(String openid) {
/* 195 */     User user = this.userManager.getByOpneid(openid);
/* 196 */     return (IUser)BeanCopierUtils.transformBean(user, UserDTO.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateUserOpenId(String account, String openid) {
/* 201 */     User user = this.userManager.getByAccount(account);
/* 202 */     if (user == null) {
/* 203 */       throw new BusinessMessage("账户不存在:" + account);
/*     */     }
/* 205 */     user.setOpenid(openid);
/* 206 */     this.userManager.update(user);
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
/*     */   public Map<String, ? extends IUser> getUserByIds(String userIds) {
/* 219 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 220 */     defaultQueryFilter.addFilter("tuser.id_", userIds, QueryOP.IN);
/* 221 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 222 */     Map<String, IUser> mapUser = new HashMap<>();
/* 223 */     lstUser.forEach(user -> (IUser)mapUser.put(user.getId(), BeanCopierUtils.transformBean(user, UserDTO.class)));
/* 224 */     return mapUser;
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
/*     */   public List<? extends IUser> getUsersByOrgIds(String groupIds) {
/* 237 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 238 */     Map<String, Object> params = new HashMap<>();
/* 239 */     params.put("orgIds", groupIds.split(","));
/* 240 */     defaultQueryFilter.addParams(params);
/* 241 */     defaultQueryFilter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
/* 242 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 243 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
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
/*     */   public List<? extends IUser> getUsersByRoleIds(String groupIds) {
/* 256 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 257 */     Map<String, Object> params = new HashMap<>();
/* 258 */     params.put("roleIds", groupIds.split(","));
/* 259 */     defaultQueryFilter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
/* 260 */     defaultQueryFilter.addParams(params);
/* 261 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 262 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
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
/*     */   public List<? extends IUser> getUsersByPostIds(String groupIds) {
/* 275 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 276 */     Map<String, Object> params = new HashMap<>();
/* 277 */     params.put("postIds", groupIds.split(","));
/* 278 */     defaultQueryFilter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
/* 279 */     defaultQueryFilter.addParams(params);
/* 280 */     List<User> lstUser = this.bpmUserManager.query((QueryFilter)defaultQueryFilter);
/* 281 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
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
/*     */   public Integer countEnabledUser() {
/* 293 */     return this.userManager.getAllEnableUserNum();
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
/*     */   public List<? extends IUser> getUsersByUserName(String userName, String postId, Integer offset, Integer limit) {
/* 309 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 310 */     Map<String, Object> params = new HashMap<>();
/* 311 */     params.put("postId", postId);
/* 312 */     defaultQueryFilter.addParams(params);
/* 313 */     if (StringUtils.isNotEmpty(userName)) {
/* 314 */       defaultQueryFilter.addFilter("tuser.fullname_", userName, QueryOP.LEFT_LIKE);
/*     */     }
/* 316 */     RowBounds rowBounds = new RowBounds(offset.intValue(), limit.intValue());
/* 317 */     DefaultPage page = new DefaultPage(rowBounds);
/* 318 */     defaultQueryFilter.setPage((Page)page);
/* 319 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 320 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
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
/*     */   public List<? extends IUser> getUsersByOrgPath(String orgPath) {
/* 333 */     List<User> lstUser = this.userManager.getUsersByOrgPath(orgPath);
/* 334 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IUser> getUserByAccounts(String accounts) {
/* 339 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 340 */     defaultQueryFilter.addFilter("tuser.account_", accounts, QueryOP.IN);
/* 341 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 342 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
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
/*     */   public List<? extends IUser> getUsersByGroup(String orgIds, String postIds) {
/* 356 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 357 */     Map<String, Object> params = new HashMap<>();
/* 358 */     params.put("orgIds", orgIds.split(","));
/* 359 */     if (StringUtils.isNotEmpty(postIds)) {
/* 360 */       params.put("postIds", postIds.split(","));
/*     */     }
/* 362 */     defaultQueryFilter.addParams(params);
/* 363 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 364 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IUser> getUsersByMobiles(String mobiles) {
/* 369 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 370 */     defaultQueryFilter.addFilter("tuser.mobile_", mobiles, QueryOP.IN);
/* 371 */     List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 372 */     return BeanCopierUtils.transformList(lstUser, UserDTO.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResultDto<? extends IUser> getUsersByUserQuery(UserQueryDTO userQuery) {
/* 377 */     DefaultQueryFilter filter = new DefaultQueryFilter(userQuery.getNoPage().booleanValue());
/* 378 */     if (!userQuery.getNoPage().booleanValue()) {
/* 379 */       filter.setPage((Page)new DefaultPage(new RowBounds(userQuery.getOffset().intValue(), userQuery.getLimit().intValue())));
/*     */     }
/* 381 */     if (null != userQuery.getLstQueryConf()) {
/* 382 */       userQuery.getLstQueryConf().forEach(queryConf -> filter.addFilter(queryConf.getName(), queryConf.getValue(), QueryOP.getByVal(queryConf.getType())));
/*     */     }
/*     */     
/* 385 */     String sort = userQuery.getSort();
/* 386 */     String order = userQuery.getOrder();
/* 387 */     if (StringUtil.isNotEmpty(sort)) {
/* 388 */       String[] sorts = sort.split(",");
/* 389 */       String[] orders = new String[0];
/* 390 */       if (StringUtils.isNotEmpty(order)) {
/* 391 */         orders = order.split(",");
/*     */       }
/* 393 */       List<FieldSort> fieldSorts = new ArrayList<>();
/* 394 */       for (int i = 0; i < sorts.length; i++) {
/* 395 */         String sortTemp = sorts[i];
/* 396 */         String orderTemp = Direction.ASC.name();
/* 397 */         if (orders.length > i) {
/* 398 */           orderTemp = orders[i];
/*     */         }
/* 400 */         fieldSorts.add(new DefaultFieldSort(sortTemp, Direction.fromString(orderTemp)));
/*     */       } 
/* 402 */       filter.setFieldSortList(fieldSorts);
/*     */     } 
/* 404 */     if (StringUtils.isNotEmpty(userQuery.getOrgIds())) {
/* 405 */       String orgIds = userQuery.getOrgIds();
/* 406 */       filter.getParams().put("orgIds", orgIds.split(","));
/* 407 */       Boolean orgHasChild = userQuery.getOrgHasChild();
/* 408 */       if (null == orgHasChild) {
/* 409 */         orgHasChild = Boolean.valueOf(false);
/*     */       }
/* 411 */       filter.getParams().put("orgHasChild", orgHasChild);
/*     */     } 
/* 413 */     if (StringUtils.isNotEmpty(userQuery.getOrgCodes())) {
/* 414 */       String orgCodes = userQuery.getOrgCodes();
/* 415 */       filter.getParams().put("orgCodes", orgCodes.split(","));
/* 416 */       Boolean orgHasChild = userQuery.getOrgHasChild();
/* 417 */       if (null == orgHasChild) {
/* 418 */         orgHasChild = Boolean.valueOf(false);
/*     */       }
/* 420 */       filter.getParams().put("orgHasChild", orgHasChild);
/*     */     } 
/* 422 */     if (StringUtils.isNotEmpty(userQuery.getOrgPath())) {
/* 423 */       filter.getParams().put("orgPath", userQuery.getOrgPath().concat("%"));
/*     */     }
/* 425 */     if (StringUtils.isNotEmpty(userQuery.getRoleIds())) {
/* 426 */       String roleIds = userQuery.getRoleIds();
/* 427 */       filter.getParams().put("roleIds", roleIds.split(","));
/*     */     } 
/* 429 */     if (StringUtils.isNotEmpty(userQuery.getRoleCodes())) {
/* 430 */       String roleCodes = userQuery.getRoleCodes();
/* 431 */       filter.getParams().put("roleCodes", roleCodes.split(","));
/*     */     } 
/* 433 */     if (StringUtils.isNotEmpty(userQuery.getPostIds())) {
/* 434 */       String postIds = userQuery.getPostIds();
/* 435 */       filter.getParams().put("postIds", postIds.split(","));
/*     */     } 
/* 437 */     if (StringUtils.isNotEmpty(userQuery.getPostCodes())) {
/* 438 */       String postCodes = userQuery.getPostCodes();
/* 439 */       filter.getParams().put("postCodes", postCodes.split(","));
/*     */     } 
/* 441 */     if (StringUtils.isNotEmpty(userQuery.getResultType())) {
/* 442 */       filter.getParams().put("resultType", userQuery.getResultType());
/*     */     }
/* 444 */     if (StringUtils.isNotEmpty(userQuery.getTeamIds())) {
/* 445 */       String teamIds = userQuery.getTeamIds();
/* 446 */       filter.getParams().put("teamIds", teamIds.split(","));
/*     */     } 
/* 448 */     if (StringUtils.isNotEmpty(userQuery.getTeamCustomIds())) {
/* 449 */       String teamCustomIds = userQuery.getTeamCustomIds();
/* 450 */       filter.getParams().put("teamCustomIds", teamCustomIds.split(","));
/*     */     } 
/* 452 */     if (null != userQuery.getUserSelectHistory() && userQuery.getUserSelectHistory().booleanValue()) {
/* 453 */       filter.getParams().put("teamHistory", Boolean.valueOf(true));
/* 454 */       filter.getParams().put("currentUserId", this.iCurrentContext.getCurrentUserId());
/*     */     } 
/*     */     
/* 457 */     List<User> lstUser = this.bpmUserManager.query((QueryFilter)filter);
/* 458 */     PageResult result = new PageResult(lstUser);
/* 459 */     result.setRows(BeanCopierUtils.transformList(result.getRows(), UserDTO.class));
/* 460 */     return (PageResultDto<? extends IUser>)BeanCopierUtils.transformBean(result, PageResultDto.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String addUser(UserDTO userDTO) {
/* 465 */     if (null != this.iCurrentContext.getCurrentUserId()) {
/* 466 */       User user = (User)BeanCopierUtils.transformBean(userDTO, User.class);
/* 467 */       List<OrgRelation> lstOrgRelation = BeanCopierUtils.transformList(userDTO.getOrgRelationList(), OrgRelation.class);
/* 468 */       user.setOrgRelationList(lstOrgRelation);
/* 469 */       user.setId(null);
/* 470 */       this.userManager.saveUserInfo(user);
/* 471 */       return user.getId();
/*     */     } 
/* 473 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer editUser(UserDTO userDTO) {
/*     */     try {
/* 479 */       if (null == this.iCurrentContext.getCurrentUserId()) {
/* 480 */         User user1 = new User();
/* 481 */         user1.setUserId("0");
/* 482 */         user1.setFullname("系统");
/* 483 */         this.iCurrentContext.setCurrentUser((IUser)user1);
/*     */       } 
/* 485 */       User user = (User)BeanCopierUtils.transformBean(userDTO, User.class);
/* 486 */       if (null != userDTO.getOrgRelationList()) {
/* 487 */         List<OrgRelation> lstOrgRelation = BeanCopierUtils.transformList(userDTO.getOrgRelationList(), OrgRelation.class);
/* 488 */         user.setOrgRelationList(lstOrgRelation);
/*     */       } 
/* 490 */       if (StringUtils.isNotEmpty(user.getId())) {
/* 491 */         this.userManager.saveUserInfo(user);
/*     */       } else {
/* 493 */         throw new BusinessMessage("ID必填");
/*     */       } 
/* 495 */       return Integer.valueOf(1);
/* 496 */     } catch (Exception e) {
/* 497 */       this.logger.error("修改用户信息出错", e);
/* 498 */       return Integer.valueOf(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAdmin(IUser user) {
/* 504 */     return this.iCurrentContext.isAdmin(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserDTO> getUserOrgInfos(String userIds) {
/* 509 */     return this.bpmUserManager.getUserOrgInfos(userIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getUserMapByUserIds(Set<String> userIdSet) {
/* 514 */     Map<String, String> mapUser = new HashMap<>();
/* 515 */     if (CollectionUtil.isNotEmpty(userIdSet)) {
/* 516 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 517 */       defaultQueryFilter.addFilter("tuser.id_", new ArrayList<>(userIdSet), QueryOP.IN);
/* 518 */       defaultQueryFilter.addFilter("tuser.STATUS_", "1", QueryOP.EQUAL);
/* 519 */       defaultQueryFilter.getParams().put("resultType", "onlyUserUserName");
/* 520 */       List<User> lstUser = this.userManager.query((QueryFilter)defaultQueryFilter);
/* 521 */       if (CollectionUtil.isNotEmpty(lstUser)) {
/* 522 */         lstUser.forEach(temp -> mapUser.put(temp.getUserId(), temp.getFullname()));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 527 */     return mapUser;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/service/ABUserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */