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
/*     */ public class GroupQueryDTO
/*     */   implements Serializable
/*     */ {
/*  98 */   Boolean noPage = Boolean.valueOf(true);
/*  99 */   List<QueryConfDTO> lstQueryConf = new ArrayList<>();
/* 100 */   List<String> orgIds = new ArrayList<>();
/* 101 */   List<String> types = new ArrayList<>();
/* 102 */   List<String> noHasChildOrgIds = new ArrayList<>();
/*     */   
/*     */   Integer offset;
/*     */   
/*     */   Integer limit;
/*     */   String userId;
/*     */   Boolean orgHasChild;
/*     */   String groupType;
/*     */   String groupPath;
/*     */   
/*     */   public GroupQueryDTO page(int pageNo, int pageSize) {
/* 113 */     if (pageNo <= 0) {
/* 114 */       System.out.println("别闹，页数有负数或者零的吗");
/* 115 */       pageNo = 1;
/*     */     } 
/* 117 */     if (pageSize <= 0) {
/* 118 */       System.out.println("别闹，能显示负数或者零条记录吗");
/* 119 */       pageNo = 10;
/*     */     } 
/* 121 */     this.offset = Integer.valueOf((pageNo - 1) * pageSize);
/* 122 */     this.limit = Integer.valueOf(pageSize);
/* 123 */     this.noPage = Boolean.valueOf(false);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   String resultType;
/*     */   
/*     */   String sort;
/*     */   
/*     */   String order;
/*     */   
/*     */   public GroupQueryDTO pageOffset(int offset, int limit) {
/* 135 */     if (offset < 0) {
/* 136 */       System.out.println("别闹，偏移量有负数的吗");
/* 137 */       offset = 0;
/*     */     } 
/* 139 */     if (limit <= 0) {
/* 140 */       System.out.println("别闹，能显示负数或者零条记录吗");
/* 141 */       limit = 10;
/*     */     } 
/* 143 */     this.offset = Integer.valueOf(offset);
/* 144 */     this.limit = Integer.valueOf(limit);
/* 145 */     this.noPage = Boolean.valueOf(false);
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   String groupCodes;
/*     */   static final String RESULT_TYPE_ONLY_GROUP_ID = "onlyGroupId";
/*     */   static final String RESULT_TYPE_ONLY_GROUP_NAME = "onlyGroupName";
/*     */   static final String RESULT_TYPE_WITH_USER_NUM = "withUserNum";
/*     */   
/*     */   public GroupQueryDTO queryByUserId(String userId) {
/* 156 */     this.userId = userId;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupIds(List<String> lstGroupId) {
/* 167 */     String groupIds = "";
/* 168 */     if (null != lstGroupId) {
/* 169 */       groupIds = String.join(",", (Iterable)lstGroupId);
/*     */     }
/* 171 */     return queryByGroupIds(groupIds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupTypes(List<String> listGroupType) {
/* 181 */     String groupTypes = "";
/* 182 */     if (null != listGroupType) {
/* 183 */       groupTypes = String.join(",", (Iterable)listGroupType);
/*     */     }
/* 185 */     return queryByGroupTypes(groupTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupIds(String groupIds) {
/* 195 */     this.lstQueryConf.add(new QueryConfDTO("id_", QueryOP.IN.value(), groupIds));
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByParentGroupIds(String groupIds) {
/* 206 */     this.lstQueryConf.add(new QueryConfDTO("parent_id_", QueryOP.IN.value(), groupIds));
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupTypes(String groupTypes) {
/* 217 */     this.lstQueryConf.add(new QueryConfDTO("type_", QueryOP.IN.value(), groupTypes));
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupName(String groupName) {
/* 228 */     return queryByGroupName(groupName, QueryOP.LIKE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupName(String groupName, QueryOP queryType) {
/* 239 */     this.lstQueryConf.add(new QueryConfDTO("name_", queryType.value(), groupName));
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupPath(String groupPath) {
/* 250 */     this.groupPath = groupPath;
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryOrderBy(String sort, String order) {
/* 262 */     this.sort = sort;
/* 263 */     if (!StringUtils.isEmpty(sort)) {
/* 264 */       if (StringUtils.isEmpty(sort)) {
/* 265 */         sort = "asc";
/*     */       }
/* 267 */       this.order = order;
/*     */     } 
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupCodes(String groupCodes) {
/* 279 */     this.groupCodes = groupCodes;
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO queryByGroupCodes(List<String> listGroupCodes) {
/* 291 */     String groupCodes = "";
/* 292 */     if (null != listGroupCodes) {
/* 293 */       groupCodes = String.join(",", (Iterable)listGroupCodes);
/*     */     }
/* 295 */     this.groupCodes = groupCodes;
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO setResultTypeOnlyGroupId() {
/* 305 */     this.resultType = "onlyGroupId";
/* 306 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO setResultTypeOnlyGroupName() {
/* 315 */     this.resultType = "onlyGroupName";
/* 316 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupQueryDTO setResultTypeWithUserNum() {
/* 325 */     this.resultType = "withUserNum";
/* 326 */     return this;
/*     */   }
/*     */   
/*     */   public Boolean getNoPage() {
/* 330 */     return this.noPage;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setNoPage(Boolean noPage) {
/* 334 */     this.noPage = noPage;
/* 335 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getOffset() {
/* 339 */     return this.offset;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setOffset(Integer offset) {
/* 343 */     this.offset = offset;
/* 344 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getLimit() {
/* 348 */     return this.limit;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setLimit(Integer limit) {
/* 352 */     this.limit = limit;
/* 353 */     return this;
/*     */   }
/*     */   
/*     */   public String getUserId() {
/* 357 */     return this.userId;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setUserId(String userId) {
/* 361 */     this.userId = userId;
/* 362 */     return this;
/*     */   }
/*     */   
/*     */   public String getGroupType() {
/* 366 */     return this.groupType;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setGroupType(String groupType) {
/* 370 */     this.groupType = groupType;
/* 371 */     return this;
/*     */   }
/*     */   
/*     */   public String getGroupPath() {
/* 375 */     return this.groupPath;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setGroupPath(String groupPath) {
/* 379 */     this.groupPath = groupPath;
/* 380 */     return this;
/*     */   }
/*     */   
/*     */   public String getResultType() {
/* 384 */     return this.resultType;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setResultType(String resultType) {
/* 388 */     this.resultType = resultType;
/* 389 */     return this;
/*     */   }
/*     */   
/*     */   public List<QueryConfDTO> getLstQueryConf() {
/* 393 */     return this.lstQueryConf;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setLstQueryConf(List<QueryConfDTO> lstQueryConf) {
/* 397 */     this.lstQueryConf = lstQueryConf;
/* 398 */     return this;
/*     */   }
/*     */   
/*     */   public String getSort() {
/* 402 */     return this.sort;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setSort(String sort) {
/* 406 */     this.sort = sort;
/* 407 */     return this;
/*     */   }
/*     */   
/*     */   public String getOrder() {
/* 411 */     return this.order;
/*     */   }
/*     */   
/*     */   public GroupQueryDTO setOrder(String order) {
/* 415 */     this.order = order;
/* 416 */     return this;
/*     */   }
/*     */   
/*     */   public List<String> getOrgIds() {
/* 420 */     return this.orgIds;
/*     */   }
/*     */   
/*     */   public void setOrgIds(List<String> orgIds) {
/* 424 */     this.orgIds = orgIds;
/*     */   }
/*     */   
/*     */   public Boolean getOrgHasChild() {
/* 428 */     return this.orgHasChild;
/*     */   }
/*     */   
/*     */   public void setOrgHasChild(Boolean orgHasChild) {
/* 432 */     this.orgHasChild = orgHasChild;
/*     */   }
/*     */   
/*     */   public List<String> getTypes() {
/* 436 */     return this.types;
/*     */   }
/*     */   
/*     */   public void setTypes(List<String> types) {
/* 440 */     this.types = types;
/*     */   }
/*     */   
/*     */   public List<String> getNoHasChildOrgIds() {
/* 444 */     return this.noHasChildOrgIds;
/*     */   }
/*     */   
/*     */   public void setNoHasChildOrgIds(List<String> noHasChildOrgIds) {
/* 448 */     this.noHasChildOrgIds = noHasChildOrgIds;
/*     */   }
/*     */   
/*     */   public String getGroupCodes() {
/* 452 */     return this.groupCodes;
/*     */   }
/*     */   
/*     */   public void setGroupCodes(String groupCodes) {
/* 456 */     this.groupCodes = groupCodes;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-api/v1.0.176.bjsj.1/org-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/api/model/dto/GroupQueryDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */