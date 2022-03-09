/*     */ package cn.gwssi.ecloudframework.org.service;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.PageResultDto;
/*     */ import cn.gwssi.ecloudframework.base.core.util.BeanCopierUtils;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.constant.GroupTypeConstant;
/*     */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.GroupDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.GroupQueryDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.QueryConfDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.BpmUserManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.PostManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.RoleManager;
/*     */ import cn.gwssi.ecloudframework.org.core.manager.UserManager;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Group;
/*     */ import cn.gwssi.ecloudframework.org.core.model.OrgRelation;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Post;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Role;
/*     */ import cn.gwssi.ecloudframework.org.core.model.User;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.ibatis.session.RowBounds;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service("defaultGroupService")
/*     */ public class ABGroupService implements GroupService {
/*  41 */   private Logger log = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   UserManager userManager;
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */   @Autowired
/*     */   RoleManager roleManager;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationManager;
/*     */   @Resource
/*     */   PostManager postManager;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Resource
/*     */   BpmUserManager bpmUserManager;
/*     */   
/*     */   public List<? extends IGroup> getGroupsByGroupTypeUserId(String groupType, String userId) {
/*  59 */     List listGroup = null;
/*     */     
/*  61 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/*  62 */       listGroup = this.groupManager.getByUserId(userId);
/*  63 */     } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
/*  64 */       listGroup = this.roleManager.getByUserId(userId);
/*  65 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/*  66 */       listGroup = this.postManager.getByUserId(userId);
/*     */     } 
/*     */     
/*  69 */     if (listGroup != null) {
/*  70 */       return BeanCopierUtils.transformList(listGroup, GroupDTO.class);
/*     */     }
/*     */     
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<? extends IGroup>> getAllGroupByAccount(String account) {
/*  78 */     User user = this.userManager.getByAccount(account);
/*  79 */     if (user == null) {
/*  80 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*  83 */     return getAllGroupByUserId(user.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<? extends IGroup>> getAllGroupByUserId(String userId) {
/*  88 */     Map<String, List<? extends IGroup>> listMap = new HashMap<>();
/*     */     
/*  90 */     List<? extends IGroup> listOrg = this.groupManager.getByUserId(userId);
/*  91 */     if (CollectionUtil.isNotEmpty(listOrg)) {
/*  92 */       List<? extends IGroup> groupList = BeanCopierUtils.transformList(listOrg, GroupDTO.class);
/*  93 */       listMap.put(GroupTypeConstant.ORG.key(), groupList);
/*     */     } 
/*  95 */     List<? extends IGroup> listRole = this.roleManager.getByUserId(userId);
/*  96 */     if (CollectionUtil.isNotEmpty(listRole)) {
/*  97 */       List<? extends IGroup> groupList = BeanCopierUtils.transformList(listRole, GroupDTO.class);
/*  98 */       listMap.put(GroupTypeConstant.ROLE.key(), groupList);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     List<OrgRelation> listOrgRel = this.orgRelationManager.getPostByUserId(userId);
/* 109 */     if (CollectionUtil.isNotEmpty(listOrgRel)) {
/* 110 */       List<IGroup> userGroups = new ArrayList<>();
/* 111 */       listOrgRel.forEach(post -> userGroups.add(new GroupDTO(post.getPostId(), post.getPostName(), GroupTypeConstant.POST.key())));
/* 112 */       listMap.put(GroupTypeConstant.POST.key(), userGroups);
/*     */     } 
/* 114 */     return listMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getGroupsByUserId(String userId) {
/* 119 */     List<IGroup> userGroups = new ArrayList<>();
/* 120 */     List<? extends IGroup> listOrg = this.groupManager.getByUserId(userId);
/* 121 */     if (CollectionUtil.isNotEmpty(listOrg)) {
/* 122 */       userGroups.addAll(listOrg);
/*     */     }
/* 124 */     List<? extends IGroup> listRole = this.roleManager.getByUserId(userId);
/* 125 */     if (CollectionUtil.isNotEmpty(listRole)) {
/* 126 */       userGroups.addAll(listRole);
/*     */     }
/* 128 */     List<? extends IGroup> listPost = this.postManager.getByUserId(userId);
/* 129 */     if (CollectionUtil.isNotEmpty(listPost)) {
/* 130 */       userGroups.addAll(listPost);
/*     */     }
/*     */     
/* 133 */     List<? extends IGroup> groupList = BeanCopierUtils.transformList(userGroups, GroupDTO.class);
/* 134 */     return groupList;
/*     */   }
/*     */ 
/*     */   
/*     */   public IGroup getById(String groupType, String groupId) {
/* 139 */     IGroup group = null;
/* 140 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 141 */       group = (IGroup)this.groupManager.get(groupId);
/* 142 */     } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
/* 143 */       group = (IGroup)this.roleManager.get(groupId);
/* 144 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/* 145 */       group = (IGroup)this.postManager.get(groupId);
/*     */     } 
/*     */     
/* 148 */     if (group == null) {
/* 149 */       return null;
/*     */     }
/*     */     
/* 152 */     return (IGroup)new GroupDTO(group);
/*     */   }
/*     */   
/*     */   public IGroup getByCode(String groupType, String code) {
/*     */     Post post;
/* 157 */     IGroup group = null;
/*     */     
/* 159 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 160 */       Group group1 = this.groupManager.getByCode(code);
/* 161 */     } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
/* 162 */       Role role = this.roleManager.getByAlias(code);
/* 163 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/* 164 */       post = this.postManager.getByAlias(code);
/*     */     } 
/*     */     
/* 167 */     if (post == null) {
/* 168 */       return null;
/*     */     }
/* 170 */     return (IGroup)new GroupDTO((IGroup)post);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IGroup getMainGroup(String userId) {
/* 176 */     return (IGroup)this.groupManager.getMainGroup(userId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getSiblingsGroups(String code) {
/* 182 */     List<IGroup> lstGroup = null;
/*     */     
/* 184 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 185 */     Group group = this.groupManager.getByCode(code);
/* 186 */     if (null != group && null != group.getParentId()) {
/* 187 */       defaultQueryFilter.addFilter("torg.parent_id_", group.getParentId(), QueryOP.EQUAL);
/* 188 */       defaultQueryFilter.addFilter("torg.code_", code, QueryOP.NOT_EQUAL);
/* 189 */       return BeanCopierUtils.transformList(this.groupManager.query((QueryFilter)defaultQueryFilter), GroupDTO.class);
/*     */     } 
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getRoleList(QueryFilter queryFilter) {
/* 196 */     List<Role> pageList = this.roleManager.query(queryFilter);
/* 197 */     return (List)pageList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getSameLevel(String groupType, String groupId) {
/* 202 */     List<IGroup> lstGroup = null;
/*     */     
/* 204 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 205 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 206 */       IGroup group = (IGroup)this.groupManager.get(groupId);
/* 207 */       if (null != group && null != group.getParentId()) {
/* 208 */         defaultQueryFilter.addFilter("torg.parent_id_", group.getParentId(), QueryOP.EQUAL);
/* 209 */         return BeanCopierUtils.transformList(this.groupManager.query((QueryFilter)defaultQueryFilter), GroupDTO.class);
/*     */       } 
/*     */     } 
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getGroupListByType(String groupType) {
/* 217 */     List<? extends IGroup> groups = null;
/*     */     
/* 219 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 220 */       groups = this.groupManager.getAll();
/* 221 */     } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
/* 222 */       groups = this.roleManager.getAll();
/* 223 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/* 224 */       groups = this.postManager.getAll();
/*     */     } 
/* 226 */     return groups;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getGroupsById(String groupType, String groupIds) {
/* 231 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 232 */     List<? extends IGroup> lstIGroup = null;
/* 233 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 234 */       defaultQueryFilter.addFilter("torg.id_", groupIds, QueryOP.IN);
/* 235 */       lstIGroup = this.groupManager.query((QueryFilter)defaultQueryFilter);
/* 236 */     } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
/* 237 */       defaultQueryFilter.addFilter("trole.id_", groupIds, QueryOP.IN);
/* 238 */       lstIGroup = this.roleManager.query((QueryFilter)defaultQueryFilter);
/* 239 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/* 240 */       defaultQueryFilter.addFilter("tpost.id_", groupIds, QueryOP.IN);
/* 241 */       lstIGroup = this.postManager.query((QueryFilter)defaultQueryFilter);
/*     */     } 
/* 243 */     return lstIGroup;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends IGroup> getChildrenGroupsById(String groupType, String groupId) {
/* 248 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 249 */     List<? extends IGroup> lstIGroup = null;
/* 250 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 251 */       defaultQueryFilter.addFilter("torg.parent_id_", groupId, QueryOP.EQUAL);
/* 252 */       lstIGroup = this.groupManager.query((QueryFilter)defaultQueryFilter);
/*     */     } 
/* 254 */     return lstIGroup;
/*     */   }
/*     */   public IGroup getMasterGroupByUserId(String groupType, String userId) {
/*     */     Post post;
/*     */     GroupDTO groupDTO;
/* 259 */     IGroup group = null;
/*     */     
/* 261 */     if (groupType.equals(GroupTypeConstant.ORG.key())) {
/* 262 */       Group group1 = this.groupManager.getMainGroup(userId);
/* 263 */     } else if (groupType.equals(GroupTypeConstant.POST.key())) {
/* 264 */       post = this.postManager.getMasterByUserId(userId);
/*     */     } 
/*     */     
/* 267 */     if (post != null) {
/* 268 */       groupDTO = new GroupDTO((IGroup)post);
/*     */     }
/*     */     
/* 271 */     return (IGroup)groupDTO;
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResultDto<? extends IGroup> getGroupsByGroupQuery(GroupQueryDTO groupQuery) {
/* 276 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 277 */     if (!groupQuery.getNoPage().booleanValue()) {
/* 278 */       defaultQueryFilter.setPage((Page)new DefaultPage(new RowBounds(groupQuery.getOffset().intValue(), groupQuery.getLimit().intValue())));
/*     */     }
/* 280 */     String tableName = "";
/* 281 */     String type = groupQuery.getGroupType();
/* 282 */     if (type.equals(GroupTypeConstant.ORG.key())) {
/* 283 */       tableName = "torg";
/* 284 */     } else if (type.equals(GroupTypeConstant.POST.key())) {
/* 285 */       tableName = "tpost";
/* 286 */     } else if (type.equals(GroupTypeConstant.ROLE.key())) {
/* 287 */       tableName = "trole";
/*     */     } 
/* 289 */     if (null != groupQuery.getLstQueryConf()) {
/* 290 */       String finalTableName = tableName;
/* 291 */       groupQuery.getLstQueryConf().forEach(queryConf -> filter.addFilter(finalTableName + "." + queryConf.getName(), queryConf.getValue(), QueryOP.getByVal(queryConf.getType())));
/*     */     } 
/* 293 */     if (StringUtils.isNotEmpty(groupQuery.getUserId())) {
/* 294 */       String userId = groupQuery.getUserId();
/* 295 */       defaultQueryFilter.getParams().put("userId", userId);
/*     */     } 
/*     */     
/* 298 */     if (CollectionUtil.isNotEmpty(groupQuery.getOrgIds())) {
/* 299 */       List<String> orgIds = groupQuery.getOrgIds();
/* 300 */       defaultQueryFilter.getParams().put("orgIds", orgIds);
/*     */     } 
/* 302 */     if (StringUtils.isNotEmpty(groupQuery.getGroupCodes())) {
/* 303 */       String groupCodes = groupQuery.getGroupCodes();
/* 304 */       if (type.equals(GroupTypeConstant.ORG.key())) {
/* 305 */         defaultQueryFilter.getParams().put("orgCodes", Arrays.asList(groupCodes.split(",")));
/* 306 */       } else if (type.equals(GroupTypeConstant.POST.key())) {
/* 307 */         defaultQueryFilter.addFilter(tableName + ".code_", groupCodes, QueryOP.IN);
/* 308 */       } else if (type.equals(GroupTypeConstant.ROLE.key())) {
/* 309 */         defaultQueryFilter.addFilter(tableName + ".alias_", groupCodes, QueryOP.IN);
/*     */       } 
/*     */     } 
/*     */     
/* 313 */     if (CollectionUtil.isNotEmpty(groupQuery.getNoHasChildOrgIds())) {
/* 314 */       List<String> noHasChildOrgIds = groupQuery.getNoHasChildOrgIds();
/* 315 */       defaultQueryFilter.getParams().put("noHasChildOrgIds", noHasChildOrgIds);
/*     */     } 
/*     */     
/* 318 */     if (null != groupQuery.getOrgHasChild()) {
/* 319 */       boolean orgHasChild = groupQuery.getOrgHasChild().booleanValue();
/* 320 */       defaultQueryFilter.getParams().put("orgHasChild", Boolean.valueOf(orgHasChild));
/*     */     } 
/*     */     
/* 323 */     if (StringUtils.isNotEmpty(groupQuery.getResultType())) {
/* 324 */       defaultQueryFilter.getParams().put("resultType", groupQuery.getResultType());
/*     */     }
/* 326 */     if (StringUtils.isNotEmpty(groupQuery.getSort()) && StringUtils.isNotEmpty(groupQuery.getOrder())) {
/* 327 */       defaultQueryFilter.addFieldSort(groupQuery.getSort(), groupQuery.getOrder());
/*     */     }
/* 329 */     List<? extends IGroup> list = Lists.newArrayList();
/* 330 */     if (StringUtils.isNotEmpty(type)) {
/* 331 */       if (type.equals(GroupTypeConstant.ORG.key())) {
/* 332 */         if (StringUtils.isNotEmpty(groupQuery.getGroupPath())) {
/* 333 */           defaultQueryFilter.addFilter("torg.path_", groupQuery.getGroupPath(), QueryOP.RIGHT_LIKE);
/*     */         }
/* 335 */         list = this.groupManager.query((QueryFilter)defaultQueryFilter);
/* 336 */       } else if (type.equals(GroupTypeConstant.POST.key())) {
/* 337 */         list = this.postManager.query((QueryFilter)defaultQueryFilter);
/* 338 */       } else if (type.equals(GroupTypeConstant.ROLE.key())) {
/* 339 */         list = this.roleManager.query((QueryFilter)defaultQueryFilter);
/*     */       } 
/*     */     }
/* 342 */     PageResult result = new PageResult(list);
/* 343 */     result.setRows(BeanCopierUtils.transformList(result.getRows(), GroupDTO.class));
/* 344 */     return (PageResultDto<? extends IGroup>)BeanCopierUtils.transformBean(result, PageResultDto.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentManagerOrgIds() {
/* 349 */     IUser user = this.iCurrentContext.getCurrentUser();
/* 350 */     List<String> lstOrgId = new ArrayList<>();
/* 351 */     if (null != user) {
/* 352 */       lstOrgId = user.getManagerGroupIdList();
/*     */     }
/* 354 */     if (null == lstOrgId) {
/* 355 */       return "";
/*     */     }
/* 357 */     return String.join(",", (Iterable)lstOrgId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmOrgDTO> getOrgInfos(String orgIds) {
/* 362 */     return this.bpmUserManager.getOrgInfos(orgIds);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/service/ABGroupService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */