/*     */ package com.dstz.bpm.plugin.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.query.FieldRelation;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.query.WhereClause;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultFieldLogic;
/*     */ import com.dstz.base.db.model.query.DefaultQueryField;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.bpm.plugin.core.dao.BpmUserAgencyConfigDao;
/*     */ import com.dstz.bpm.plugin.core.dao.BpmUserAgencyLogDao;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */
import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
/*     */ import com.dstz.bpm.plugin.enums.BpmUserAgencyConfigTabEnum;
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
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("bpmUserAgencyConfigManager")
/*     */ public class BpmUserAgencyConfigManagerImpl
/*     */   extends BaseManager<String, BpmUserAgencyConfig>
/*     */   implements BpmUserAgencyConfigManager
/*     */ {
/*     */   @Autowired
/*     */   private BpmUserAgencyConfigDao bpmUserAgencyConfigDao;
/*     */   @Autowired
/*     */   private BpmUserAgencyLogDao bpmUserAgencyLogDao;
/*     */   @Resource
/*     */   UserService UserService;
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTakeEffectingList(String configUserId, Date currentTime) {
/*  50 */     return this.bpmUserAgencyConfigDao.selectTakeEffectingList(configUserId, DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Page<BpmUserAgencyConfig> selectTabList(BpmUserAgencyConfigTabDTO bpmUserAgencyConfigTabDTO) {
/*     */     List<BpmUserAgencyConfig> bpmUserAgencyConfigList;
/*  56 */     Date currentTime = new Date();
/*  57 */     PageHelper.startPage(bpmUserAgencyConfigTabDTO.getPageNo().intValue(), bpmUserAgencyConfigTabDTO.getPageSize().intValue());
/*  58 */     if (StringUtil.isNotEmpty(bpmUserAgencyConfigTabDTO.getName())) {
/*  59 */       bpmUserAgencyConfigTabDTO.setName("%" + bpmUserAgencyConfigTabDTO.getName() + "%");
/*     */     }
/*  61 */     if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  62 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectInvalidList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*  63 */     } else if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  64 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectTakeEffectingList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*  65 */     } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
/*  66 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectWaitEffectList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
/*     */     } else {
/*     */       
/*  69 */       bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectAllList(bpmUserAgencyConfigTabDTO.getConfigUserId(), bpmUserAgencyConfigTabDTO.getName());
/*     */     } 
/*  71 */     return (Page<BpmUserAgencyConfig>)bpmUserAgencyConfigList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTabListJson(String tab, String name, QueryFilter queryFilter) {
/*  76 */     DefaultQueryFilter defaultQueryFilter = (DefaultQueryFilter)queryFilter;
/*  77 */     String c = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
/*  78 */     List<WhereClause> whereClauses = defaultQueryFilter.getFieldLogic().getWhereClauses();
/*     */ 
/*     */     
/*  81 */     if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(tab)) {
/*  82 */       whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LE"), c));
/*  83 */       whereClauses.add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GE"), c));
/*  84 */       whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(1)));
/*  85 */     } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(tab)) {
/*  86 */       whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GE"), c));
/*  87 */       whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(1)));
/*  88 */     } else if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(tab)) {
/*  89 */       DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/*  90 */       orFieldLogic.setFieldRelation(FieldRelation.OR);
/*  91 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("LE"), c));
/*  92 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), Integer.valueOf(0)));
/*  93 */       whereClauses.add(orFieldLogic);
/*     */     } 
/*  95 */     if (StringUtil.isNotEmpty(name)) {
/*  96 */       DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/*  97 */       orFieldLogic.setFieldRelation(FieldRelation.OR);
/*  98 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("target_user_name_", QueryOP.getByVal("LK"), name));
/*  99 */       orFieldLogic.getWhereClauses().add(new DefaultQueryField("agency_flow_name_", QueryOP.getByVal("LK"), name));
/* 100 */       whereClauses.add(orFieldLogic);
/*     */     } 
/*     */     
/* 103 */     return this.bpmUserAgencyConfigDao.selectTabListJson((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> selectTargetListJson(String tab, String name, QueryFilter queryFilter) {
/* 108 */     List<BpmUserAgencyConfig> configVOS = selectTabListJson(tab, name, queryFilter);
/* 109 */     configVOS.forEach(conf -> {
/*     */           IUser user = this.UserService.getUserById(conf.getConfigUserId());
/*     */           if (null == user) {
/*     */             conf.setConfigUserName("用户丢失");
/*     */           } else {
/*     */             conf.setConfigUserName(user.getFullname());
/*     */           } 
/*     */         });
/* 117 */     return configVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmUserAgencyConfig> checkTabListJson(QueryFilter queryFilter) throws Exception {
/* 122 */     DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/* 123 */     orFieldLogic.setFieldRelation(FieldRelation.OR);
/* 124 */     DefaultFieldLogic leftBegin = new DefaultFieldLogic();
/* 125 */     String end_datetime_ = null, start_datetime_ = null;
/* 126 */     List<WhereClause> whereClauses = queryFilter.getFieldLogic().getWhereClauses();
/* 127 */     DefaultQueryField startDatetime = null;
/* 128 */     DefaultQueryField endDatetime = null;
/* 129 */     for (WhereClause clause : whereClauses) {
/* 130 */       if (clause instanceof DefaultQueryField) {
/* 131 */         DefaultQueryField defaultQueryField = (DefaultQueryField)clause;
/* 132 */         if ("start_datetime_".equalsIgnoreCase(defaultQueryField.getField())) {
/* 133 */           start_datetime_ = (defaultQueryField.getValue() == null) ? "" : defaultQueryField.getValue().toString();
/* 134 */           startDatetime = defaultQueryField; continue;
/* 135 */         }  if ("end_datetime_".equalsIgnoreCase(defaultQueryField.getField())) {
/* 136 */           end_datetime_ = (defaultQueryField.getValue() == null) ? "" : defaultQueryField.getValue().toString();
/* 137 */           endDatetime = defaultQueryField;
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     if (StringUtil.isEmpty(end_datetime_) || StringUtil.isEmpty(start_datetime_)) {
/* 142 */       throw new Exception("起始时间或结束时间为空");
/*     */     }
/* 144 */     whereClauses.remove(startDatetime);
/* 145 */     whereClauses.remove(endDatetime);
/* 146 */     leftBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), start_datetime_));
/* 147 */     leftBegin.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GT"), start_datetime_));
/* 148 */     DefaultFieldLogic rightBegin = new DefaultFieldLogic();
/* 149 */     rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GT"), start_datetime_));
/* 150 */     rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), end_datetime_));
/*     */     
/* 152 */     orFieldLogic.getWhereClauses().add(leftBegin);
/* 153 */     orFieldLogic.getWhereClauses().add(rightBegin);
/* 154 */     whereClauses.add(orFieldLogic);
/*     */     
/* 156 */     whereClauses.add(new DefaultQueryField("enable_", QueryOP.EQUAL, "1"));
/*     */ 
/*     */     
/* 159 */     DefaultQueryField allField = new DefaultQueryField("agency_flow_key_", QueryOP.IS_NULL, "");
/* 160 */     whereClauses.add(allField);
/* 161 */     List<BpmUserAgencyConfig> allConfigs = selectTabListJson(null, null, queryFilter);
/* 162 */     if (allConfigs.size() > 0) {
/* 163 */       return allConfigs;
/*     */     }
/* 165 */     whereClauses.remove(allField);
/* 166 */     DefaultQueryField flowKey = null;
/* 167 */     for (WhereClause clause : whereClauses) {
/* 168 */       if (clause instanceof DefaultQueryField) {
/* 169 */         DefaultQueryField clauseField = (DefaultQueryField)clause;
/* 170 */         if ("agency_flow_key_".equalsIgnoreCase(clauseField.getField())) {
/* 171 */           flowKey = clauseField;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 176 */     if (null != flowKey) {
/* 177 */       String value = flowKey.getValue().toString();
/* 178 */       if (value.indexOf(",") != -1) {
/* 179 */         whereClauses.remove(flowKey);
/* 180 */         List<BpmUserAgencyConfig> configs = new ArrayList<>();
/* 181 */         for (String key : value.split(",")) {
/* 182 */           DefaultQueryField keyField = new DefaultQueryField("agency_flow_key_", QueryOP.getByVal("LK"), key);
/* 183 */           whereClauses.add(keyField);
/* 184 */           configs.addAll(selectTabListJson(null, null, queryFilter));
/* 185 */           whereClauses.remove(keyField);
/* 186 */           if (configs.size() > 0) {
/* 187 */             return configs;
/*     */           }
/*     */         } 
/* 190 */         if (configs.size() == 0) {
/* 191 */           return configs;
/*     */         }
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


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/core/manager/impl/BpmUserAgencyConfigManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */