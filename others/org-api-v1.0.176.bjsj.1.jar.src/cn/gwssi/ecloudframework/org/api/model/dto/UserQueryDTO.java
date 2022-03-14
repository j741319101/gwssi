/*     */ package com.dstz.org.api.model.dto;
/*     */ 
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserQueryDTO
/*     */   implements Serializable
/*     */ {
/* 135 */   Boolean noPage = Boolean.valueOf(true);
/* 136 */   String status = "1";
/* 137 */   List<QueryConfDTO> lstQueryConf = new ArrayList<>();
/*     */   
/*     */   Integer offset;
/*     */   
/*     */   Integer limit;
/*     */   
/*     */   String orgIds;
/*     */   String orgCodes;
/*     */   String orgPath;
/*     */   
/*     */   public UserQueryDTO page(int pageNo, int pageSize) {
/* 148 */     if (pageNo <= 0) {
/* 149 */       System.out.println("别闹，页数有负数或者零的吗");
/* 150 */       pageNo = 1;
/*     */     } 
/* 152 */     if (pageSize <= 0) {
/* 153 */       System.out.println("别闹，能显示负数或者零条记录吗");
/* 154 */       pageNo = 10;
/*     */     } 
/* 156 */     this.offset = Integer.valueOf((pageNo - 1) * pageSize);
/* 157 */     this.limit = Integer.valueOf(pageSize);
/* 158 */     this.noPage = Boolean.valueOf(false);
/* 159 */     return this;
/*     */   }
/*     */   Boolean orgHasChild; String roleIds; String roleCodes; String postIds; String postCodes;
/*     */   String teamIds;
/*     */   String teamCustomIds;
/*     */   Boolean userSelectHistory;
/*     */   String resultType;
/*     */   String sort;
/*     */   String order;
/*     */   
/*     */   public UserQueryDTO pageOffset(int offset, int limit) {
/* 170 */     if (offset < 0) {
/* 171 */       System.out.println("别闹，偏移量有负数的吗");
/* 172 */       offset = 0;
/*     */     } 
/* 174 */     if (limit <= 0) {
/* 175 */       System.out.println("别闹，能显示负数或者零条记录吗");
/* 176 */       limit = 10;
/*     */     } 
/* 178 */     this.offset = Integer.valueOf(offset);
/* 179 */     this.limit = Integer.valueOf(limit);
/* 180 */     this.noPage = Boolean.valueOf(false);
/* 181 */     return this;
/*     */   }
/*     */   static final String TABLE_NAME_USER = "tuser";
/*     */   static final String RESULT_TYPE_ONLY_USER_ID = "onlyUserId";
/*     */   static final String RESULT_TYPE_ONLY_USER_ACCOUNT = "onlyUserAccount";
/*     */   static final String RESULT_TYPE_WEB_RESULT = "webResult";
/*     */   static final String USER_STATUS_ALIVE = "1";
/*     */   static final String USER_STATUS_NOT_ALIVE = "0";
/*     */   
/*     */   public UserQueryDTO queryByUserIds(List<String> lstUserId) {
/* 191 */     String userIds = "";
/* 192 */     if (null != lstUserId) {
/* 193 */       userIds = String.join(",", (Iterable)lstUserId);
/*     */     }
/* 195 */     return queryByUserIds(userIds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserIds(String userIds) {
/* 205 */     this.lstQueryConf.add(new QueryConfDTO("tuser.id_", QueryOP.IN.value(), userIds));
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserName(String userName) {
/* 216 */     return queryByUserName(userName, QueryOP.LIKE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserName(String userName, QueryOP queryType) {
/* 227 */     this.lstQueryConf.add(new QueryConfDTO("tuser.fullname_", queryType.value(), userName));
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserAccounts(List<String> lstUserAccount) {
/* 238 */     String userAccounts = "";
/* 239 */     if (null != lstUserAccount) {
/* 240 */       userAccounts = String.join(",", (Iterable)lstUserAccount);
/*     */     }
/* 242 */     return queryByUserAccounts(userAccounts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserAccounts(String userAccounts) {
/* 252 */     this.lstQueryConf.add(new QueryConfDTO("tuser.account_", QueryOP.IN.value(), userAccounts));
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgIds(List<String> orgIds) {
/* 263 */     return queryByOrgIds(orgIds, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgIds(String orgIds) {
/* 273 */     return queryByOrgIds(orgIds, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgIds(List<String> orgIds, boolean orgHasChild) {
/* 284 */     String temp = "";
/* 285 */     if (!StringUtils.isEmpty(orgIds)) {
/* 286 */       temp = String.join(",", (Iterable)orgIds);
/*     */     }
/* 288 */     return queryByOrgIds(temp, orgHasChild);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgIds(String orgIds, boolean orgHasChild) {
/* 299 */     this.orgIds = orgIds;
/* 300 */     this.orgHasChild = Boolean.valueOf(orgHasChild);
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgCodes(List<String> orgCodes) {
/* 311 */     return queryByOrgCodes(orgCodes, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgCodes(String orgCodes) {
/* 321 */     return queryByOrgCodes(orgCodes, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgCodes(List<String> orgCodes, boolean orgHasChild) {
/* 332 */     String temp = "";
/* 333 */     if (!StringUtils.isEmpty(orgCodes)) {
/* 334 */       temp = String.join(",", (Iterable)orgCodes);
/*     */     }
/* 336 */     return queryByOrgCodes(temp, orgHasChild);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgCodes(String orgCodes, boolean orgHasChild) {
/* 347 */     this.orgCodes = orgCodes;
/* 348 */     this.orgHasChild = Boolean.valueOf(orgHasChild);
/* 349 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByOrgPath(String orgPath) {
/* 359 */     this.orgPath = orgPath;
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByRoleIds(List<String> roleIds) {
/* 370 */     if (!StringUtils.isEmpty(roleIds)) {
/* 371 */       this.roleIds = String.join(",", (Iterable)roleIds);
/*     */     }
/* 373 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByRoleIds(String roleIds) {
/* 383 */     this.roleIds = roleIds;
/* 384 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByRoleCodes(List<String> postCodes) {
/* 394 */     if (!StringUtils.isEmpty(postCodes)) {
/* 395 */       this.postCodes = String.join(",", (Iterable)postCodes);
/*     */     }
/* 397 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByRoleCodes(String roleCodes) {
/* 407 */     this.roleCodes = roleCodes;
/* 408 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByPostIds(List<String> postIds) {
/* 418 */     if (!StringUtils.isEmpty(postIds)) {
/* 419 */       this.postIds = String.join(",", (Iterable)postIds);
/*     */     }
/* 421 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByPostIds(String postIds) {
/* 431 */     this.postIds = postIds;
/* 432 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByPostCodes(List<String> postCodes) {
/* 442 */     if (!StringUtils.isEmpty(postCodes)) {
/* 443 */       this.postCodes = String.join(",", (Iterable)postCodes);
/*     */     }
/* 445 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByPostCodes(String postCodes) {
/* 455 */     this.postCodes = postCodes;
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO setResultTypeOnlyUserId() {
/* 465 */     this.resultType = "onlyUserId";
/* 466 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO setResultTypeOnlyUserAccount() {
/* 475 */     this.resultType = "onlyUserAccount";
/* 476 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO setResultTypeWebResult() {
/* 485 */     this.resultType = "webResult";
/* 486 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByTeamIds(List<String> teamIds) {
/* 496 */     if (!StringUtils.isEmpty(teamIds)) {
/* 497 */       this.teamIds = String.join(",", (Iterable)teamIds);
/*     */     }
/* 499 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByTeamIds(String teamIds) {
/* 509 */     this.teamIds = teamIds;
/* 510 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByTeamCustomIds(List<String> teamCustomIds) {
/* 520 */     if (!StringUtils.isEmpty(teamCustomIds)) {
/* 521 */       this.teamCustomIds = String.join(",", (Iterable)teamCustomIds);
/*     */     }
/* 523 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByTeamCustomIds(String teamCustomIds) {
/* 533 */     this.teamCustomIds = teamCustomIds;
/* 534 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryByUserSelectHistory() {
/* 543 */     this.userSelectHistory = Boolean.valueOf(true);
/* 544 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryAliveStatusUser() {
/* 553 */     this.status = "1";
/* 554 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryNotAliveStatusUser() {
/* 563 */     this.status = "0";
/* 564 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryAllStatusUser() {
/* 573 */     this.status = null;
/* 574 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserQueryDTO queryOrder(String sort, String order) {
/* 583 */     this.sort = sort;
/* 584 */     this.order = order;
/* 585 */     return this;
/*     */   }
/*     */   
/*     */   public Boolean getNoPage() {
/* 589 */     return this.noPage;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setNoPage(Boolean noPage) {
/* 593 */     this.noPage = noPage;
/* 594 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getOffset() {
/* 598 */     return this.offset;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOffset(Integer offset) {
/* 602 */     this.offset = offset;
/* 603 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getLimit() {
/* 607 */     return this.limit;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setLimit(Integer limit) {
/* 611 */     this.limit = limit;
/* 612 */     return this;
/*     */   }
/*     */   
/*     */   public String getOrgIds() {
/* 616 */     return this.orgIds;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOrgIds(String orgIds) {
/* 620 */     this.orgIds = orgIds;
/* 621 */     return this;
/*     */   }
/*     */   
/*     */   public String getOrgPath() {
/* 625 */     return this.orgPath;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOrgPath(String orgPath) {
/* 629 */     this.orgPath = orgPath;
/* 630 */     return this;
/*     */   }
/*     */   
/*     */   public String getRoleIds() {
/* 634 */     return this.roleIds;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setRoleIds(String roleIds) {
/* 638 */     this.roleIds = roleIds;
/* 639 */     return this;
/*     */   }
/*     */   
/*     */   public String getPostIds() {
/* 643 */     return this.postIds;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setPostIds(String postIds) {
/* 647 */     this.postIds = postIds;
/* 648 */     return this;
/*     */   }
/*     */   
/*     */   public String getResultType() {
/* 652 */     return this.resultType;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setResultType(String resultType) {
/* 656 */     this.resultType = resultType;
/* 657 */     return this;
/*     */   }
/*     */   
/*     */   public Boolean getOrgHasChild() {
/* 661 */     return this.orgHasChild;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOrgHasChild(Boolean orgHasChild) {
/* 665 */     this.orgHasChild = orgHasChild;
/* 666 */     return this;
/*     */   }
/*     */   
/*     */   public String getTeamIds() {
/* 670 */     return this.teamIds;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setTeamIds(String teamIds) {
/* 674 */     this.teamIds = teamIds;
/* 675 */     return this;
/*     */   }
/*     */   
/*     */   public String getTeamCustomIds() {
/* 679 */     return this.teamCustomIds;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setTeamCustomIds(String teamCustomIds) {
/* 683 */     this.teamCustomIds = teamCustomIds;
/* 684 */     return this;
/*     */   }
/*     */   
/*     */   public Boolean getUserSelectHistory() {
/* 688 */     return this.userSelectHistory;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setUserSelectHistory(Boolean userSelectHistory) {
/* 692 */     this.userSelectHistory = userSelectHistory;
/* 693 */     return this;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 697 */     return this.status;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setStatus(String status) {
/* 701 */     this.status = status;
/* 702 */     return this;
/*     */   }
/*     */   
/*     */   public List<QueryConfDTO> getLstQueryConf() {
/* 706 */     return this.lstQueryConf;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setLstQueryConf(List<QueryConfDTO> lstQueryConf) {
/* 710 */     this.lstQueryConf = lstQueryConf;
/* 711 */     return this;
/*     */   }
/*     */   
/*     */   public String getOrgCodes() {
/* 715 */     return this.orgCodes;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOrgCodes(String orgCodes) {
/* 719 */     this.orgCodes = orgCodes;
/* 720 */     return this;
/*     */   }
/*     */   
/*     */   public String getRoleCodes() {
/* 724 */     return this.roleCodes;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setRoleCodes(String roleCodes) {
/* 728 */     this.roleCodes = roleCodes;
/* 729 */     return this;
/*     */   }
/*     */   
/*     */   public String getPostCodes() {
/* 733 */     return this.postCodes;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setPostCodes(String postCodes) {
/* 737 */     this.postCodes = postCodes;
/* 738 */     return this;
/*     */   }
/*     */   
/*     */   public String getSort() {
/* 742 */     return this.sort;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setSort(String sort) {
/* 746 */     this.sort = sort;
/* 747 */     return this;
/*     */   }
/*     */   
/*     */   public String getOrder() {
/* 751 */     return this.order;
/*     */   }
/*     */   
/*     */   public UserQueryDTO setOrder(String order) {
/* 755 */     this.order = order;
/* 756 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/UserQueryDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */