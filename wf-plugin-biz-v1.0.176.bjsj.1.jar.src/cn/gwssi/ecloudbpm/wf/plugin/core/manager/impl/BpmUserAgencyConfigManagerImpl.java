/*     */ package com.dstz.bpm.plugin.core.manager.impl;
/*     */ 
/*     */ import com.dstz.bpm.plugin.core.dao.BpmUserAgencyConfigDao;
/*     */ import com.dstz.bpm.plugin.core.dao.BpmUserAgencyLogDao;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
/*     */ import com.dstz.bpm.plugin.enums.BpmUserAgencyConfigTabEnum;
/*     */ import com.dstz.base.api.query.FieldRelation;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.query.WhereClause;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultFieldLogic;
/*     */ import com.dstz.base.db.model.query.DefaultQueryField;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.github.pagehelper.Page;
/*     */ import com.github.pagehelper.PageHelper;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ @Service("bpmUserAgencyConfigManager")
/*     */ public class BpmUserAgencyConfigManagerImpl
/*     */   extends BaseManager<String, BpmUserAgencyConfig>
/*     */   implements BpmUserAgencyConfigManager
/*     */ {
/*     */   @Resource
/*     */   private BpmUserAgencyConfigDao bpmUserAgencyConfigDao;
/*     */   @Resource
/*     */   private BpmUserAgencyLogDao bpmUserAgencyLogDao;
/*     */   @Resource
/*     */   private UserService userService;
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTakeEffectingList(String configUserId, Date currentTime) {
/*  47 */     return this.bpmUserAgencyConfigDao.selectTakeEffectingList(configUserId, DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Page<BpmUserAgencyConfig> selectTabList(BpmUserAgencyConfigTabDTO bpmUserAgencyConfigTabDTO) {
/*     */     List<BpmUserAgencyConfig> bpmUserAgencyConfigList;
/*  53 */     Date currentTime = new Date();
/*  54 */     PageHelper.startPage(bpmUserAgencyConfigTabDTO.getPageNo().intValue(), bpmUserAgencyConfigTabDTO.getPageSize().intValue());
/*  55 */     if (StringUtil.isNotEmpty(bpmUserAgencyConfigTabDTO.getName())) {
/*  56 */       bpmUserAgencyConfigTabDTO.setName("%" + bpmUserAgencyConfigTabDTO.getName() + "%");
/*     */     }
/*  58 */     if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  59 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectInvalidList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*  60 */     } else if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  61 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectTakeEffectingList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*  62 */     } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  63 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectWaitEffectList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*     */     } else {
/*     */       
/*  66 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectAllList(bpmUserAgencyConfigTabDTO.getConfigUserId(), bpmUserAgencyConfigTabDTO.getName());
/*     */     } 
/*  68 */     return (Page<BpmUserAgencyConfig>)bpmUserAgencyConfigList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTabListJson(String tab, String name, QueryFilter queryFilter) {
/*  73 */     String c = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
/*  74 */     List<WhereClause> whereClauses = queryFilter.getFieldLogic().getWhereClauses();
/*     */ 
/*     */     
/*  77 */     if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(tab)) {
/*  78 */       whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LE"), c));
/*  79 */       whereClauses.add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GE"), c));
/*  80 */       whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(1)));
/*  81 */     } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(tab)) {
/*  82 */       whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GE"), c));
/*  83 */       whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(1)));
/*  84 */     } else if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(tab)) {
/*  85 */       DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/*  86 */       orFieldLogic.setFieldRelation(FieldRelation.OR);
/*  87 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("LE"), c));
/*  88 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(0)));
/*  89 */       whereClauses.add(orFieldLogic);
/*     */     } 
/*  91 */     if (StringUtil.isNotEmpty(name)) {
/*  92 */       DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/*  93 */       orFieldLogic.setFieldRelation(FieldRelation.OR);
/*  94 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("target_user_name_", QueryOP.getByVal("LK"), name));
/*  95 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("agency_flow_name_", QueryOP.getByVal("LK"), name));
/*  96 */       whereClauses.add(orFieldLogic);
/*     */     } 
/*     */     
/*  99 */     return this.bpmUserAgencyConfigDao.selectTabListJson(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTargetListJson(String tab, String name, QueryFilter queryFilter) {
/* 104 */     List<BpmUserAgencyConfig> configVOS = selectTabListJson(tab, name, queryFilter);
/* 105 */     configVOS.forEach(conf -> {
/*     */           IUser user = this.userService.getUserById(conf.getConfigUserId());
/*     */           if (null == user) {
/*     */             conf.setConfigUserName("用户丢失");
/*     */           } else {
/*     */             conf.setConfigUserName(user.getFullname());
/*     */           } 
/*     */         });
/* 113 */     return configVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> checkTabListJson(QueryFilter queryFilter) throws Exception {
/* 118 */     DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/* 119 */     orFieldLogic.setFieldRelation(FieldRelation.OR);
/* 120 */     DefaultFieldLogic leftBegin = new DefaultFieldLogic();
/* 121 */     String end_datetime_ = null, start_datetime_ = null;
/* 122 */     List<WhereClause> whereClauses = queryFilter.getFieldLogic().getWhereClauses();
/* 123 */     DefaultQueryField startDatetime = null;
/* 124 */     DefaultQueryField endDatetime = null;
/* 125 */     for (WhereClause clause : whereClauses) {
/* 126 */       if (clause instanceof DefaultQueryField) {
/* 127 */         DefaultQueryField defaultQueryField = (DefaultQueryField)clause;
/* 128 */         if ("start_datetime_".equalsIgnoreCase(defaultQueryField.getField())) {
/* 129 */           start_datetime_ = (defaultQueryField.getValue() == null) ? "" : defaultQueryField.getValue().toString();
/* 130 */           startDatetime = defaultQueryField; continue;
/* 131 */         }  if ("end_datetime_".equalsIgnoreCase(defaultQueryField.getField())) {
/* 132 */           end_datetime_ = (defaultQueryField.getValue() == null) ? "" : defaultQueryField.getValue().toString();
/* 133 */           endDatetime = defaultQueryField;
/*     */         } 
/*     */       } 
/*     */     } 
/* 137 */     if (StringUtil.isEmpty(end_datetime_) || StringUtil.isEmpty(start_datetime_)) {
/* 138 */       throw new Exception("起始时间或结束时间为空");
/*     */     }
/* 140 */     whereClauses.remove(startDatetime);
/* 141 */     whereClauses.remove(endDatetime);
/* 142 */     leftBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), start_datetime_));
/* 143 */     leftBegin.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GT"), start_datetime_));
/* 144 */     DefaultFieldLogic rightBegin = new DefaultFieldLogic();
/* 145 */     rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GT"), start_datetime_));
/* 146 */     rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), end_datetime_));
/*     */     
/* 148 */     orFieldLogic.getWhereClauses().add(leftBegin);
/* 149 */     orFieldLogic.getWhereClauses().add(rightBegin);
/* 150 */     whereClauses.add(orFieldLogic);
/*     */     
/* 152 */     whereClauses.add(new DefaultQueryField("enable_", QueryOP.EQUAL, "1"));
/*     */     
/* 154 */     DefaultQueryField flowKey = null;
/* 155 */     for (WhereClause clause : whereClauses) {
/* 156 */       if (clause instanceof DefaultQueryField) {
/* 157 */         DefaultQueryField clauseField = (DefaultQueryField)clause;
/* 158 */         if ("agency_flow_key_".equalsIgnoreCase(clauseField.getField())) {
/* 159 */           flowKey = clauseField;
/* 160 */           whereClauses.remove(flowKey);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 166 */     DefaultQueryField allField = new DefaultQueryField("agency_flow_key_", QueryOP.EQUAL, "");
/* 167 */     whereClauses.add(allField);
/* 168 */     List<BpmUserAgencyConfig> allConfigs = selectTabListJson(null, null, queryFilter);
/* 169 */     if (allConfigs.size() > 0) {
/* 170 */       return allConfigs;
/*     */     }
/* 172 */     whereClauses.remove(allField);
/*     */     
/* 174 */     if (null != flowKey) {
/* 175 */       String value = flowKey.getValue().toString();
/* 176 */       if (value.indexOf(",") != -1) {
/* 177 */         whereClauses.remove(flowKey);
/* 178 */         List<BpmUserAgencyConfig> configs = new ArrayList<>();
/* 179 */         for (String key : value.split(",")) {
/* 180 */           DefaultQueryField keyField = new DefaultQueryField("agency_flow_key_", QueryOP.getByVal("LK"), key);
/* 181 */           whereClauses.add(keyField);
/* 182 */           configs.addAll(selectTabListJson(null, null, queryFilter));
/* 183 */           whereClauses.remove(keyField);
/* 184 */           if (configs.size() > 0) {
/* 185 */             return configs;
/*     */           }
/*     */         } 
/* 188 */         if (configs.size() == 0) {
/* 189 */           return configs;
/*     */         }
/*     */       } else {
/* 192 */         whereClauses.add(flowKey);
/*     */       } 
/*     */     } 
/* 195 */     return selectTabListJson(null, null, queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int insertSelective(BpmUserAgencyConfig record) {
/* 200 */     return this.bpmUserAgencyConfigDao.insertSelective(record);
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateByPrimaryKeySelective(BpmUserAgencyConfig record) {
/* 205 */     return this.bpmUserAgencyConfigDao.updateByPrimaryKeySelective(record);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByIds(String... ids) {
/* 210 */     this.bpmUserAgencyLogDao.removeByConfigIds((Set)Arrays.<String>stream(ids).collect(Collectors.toSet()));
/* 211 */     super.removeByIds((Serializable[])ids);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String entityId) {
/* 216 */     HashSet<String> entityIds = new HashSet<>(1);
/* 217 */     entityIds.add(entityId);
/* 218 */     this.bpmUserAgencyLogDao.removeByConfigIds(entityIds);
/* 219 */     super.remove(entityId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmUserAgencyConfigManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */