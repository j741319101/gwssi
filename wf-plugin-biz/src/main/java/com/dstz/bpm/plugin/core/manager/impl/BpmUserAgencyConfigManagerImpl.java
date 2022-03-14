package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmUserAgencyConfigDao;
import com.dstz.bpm.plugin.core.dao.BpmUserAgencyLogDao;
import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
import com.dstz.bpm.plugin.enums.BpmUserAgencyConfigTabEnum;
import com.dstz.base.api.query.FieldRelation;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.query.WhereClause;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultFieldLogic;
import com.dstz.base.db.model.query.DefaultQueryField;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmUserAgencyConfigManager")
public class BpmUserAgencyConfigManagerImpl extends BaseManager<String, BpmUserAgencyConfig> implements BpmUserAgencyConfigManager {
   @Resource
   private BpmUserAgencyConfigDao bpmUserAgencyConfigDao;
   @Resource
   private BpmUserAgencyLogDao bpmUserAgencyLogDao;
   @Resource
   private UserService userService;

   public List<BpmUserAgencyConfig> selectTakeEffectingList(String configUserId, Date currentTime) {
      return this.bpmUserAgencyConfigDao.selectTakeEffectingList(configUserId, DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), (String)null);
   }

   public Page<BpmUserAgencyConfig> selectTabList(BpmUserAgencyConfigTabDTO bpmUserAgencyConfigTabDTO) {
      Date currentTime = new Date();
      PageHelper.startPage(bpmUserAgencyConfigTabDTO.getPageNo(), bpmUserAgencyConfigTabDTO.getPageSize());
      if (StringUtil.isNotEmpty(bpmUserAgencyConfigTabDTO.getName())) {
         bpmUserAgencyConfigTabDTO.setName("%" + bpmUserAgencyConfigTabDTO.getName() + "%");
      }

      List bpmUserAgencyConfigList;
      if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
         bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectInvalidList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
      } else if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
         bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectTakeEffectingList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
      } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(bpmUserAgencyConfigTabDTO.getTab())) {
         bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectWaitEffectList(bpmUserAgencyConfigTabDTO.getConfigUserId(), DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"), bpmUserAgencyConfigTabDTO.getName());
      } else {
         bpmUserAgencyConfigList = this.bpmUserAgencyConfigDao.selectAllList(bpmUserAgencyConfigTabDTO.getConfigUserId(), bpmUserAgencyConfigTabDTO.getName());
      }

      return (Page)bpmUserAgencyConfigList;
   }

   public List<BpmUserAgencyConfig> selectTabListJson(String tab, String name, QueryFilter queryFilter) {
      String c = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
      List<WhereClause> whereClauses = queryFilter.getFieldLogic().getWhereClauses();
      DefaultFieldLogic orFieldLogic;
      if (BpmUserAgencyConfigTabEnum.TAKE_EFFECTING.name().equals(tab)) {
         whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LE"), c));
         whereClauses.add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GE"), c));
         whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), 1));
      } else if (BpmUserAgencyConfigTabEnum.WAIT_EFFECT.name().equals(tab)) {
         whereClauses.add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GE"), c));
         whereClauses.add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), 1));
      } else if (BpmUserAgencyConfigTabEnum.INVALID.name().equals(tab)) {
         orFieldLogic = new DefaultFieldLogic();
         orFieldLogic.setFieldRelation(FieldRelation.OR);
         orFieldLogic.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("LE"), c));
         orFieldLogic.getWhereClauses().add(new DefaultQueryField("enable_", QueryOP.getByVal("EQ"), 0));
         whereClauses.add(orFieldLogic);
      }

      if (StringUtil.isNotEmpty(name)) {
         orFieldLogic = new DefaultFieldLogic();
         orFieldLogic.setFieldRelation(FieldRelation.OR);
         orFieldLogic.getWhereClauses().add(new DefaultQueryField("target_user_name_", QueryOP.getByVal("LK"), name));
         orFieldLogic.getWhereClauses().add(new DefaultQueryField("agency_flow_name_", QueryOP.getByVal("LK"), name));
         whereClauses.add(orFieldLogic);
      }

      return this.bpmUserAgencyConfigDao.selectTabListJson(queryFilter);
   }

   public List<BpmUserAgencyConfig> selectTargetListJson(String tab, String name, QueryFilter queryFilter) {
      List<BpmUserAgencyConfig> configVOS = this.selectTabListJson(tab, name, queryFilter);
      configVOS.forEach((conf) -> {
         IUser user = this.userService.getUserById(conf.getConfigUserId());
         if (null == user) {
            conf.setConfigUserName("用户丢失");
         } else {
            conf.setConfigUserName(user.getFullname());
         }

      });
      return configVOS;
   }

   public List<BpmUserAgencyConfig> checkTabListJson(QueryFilter queryFilter) throws Exception {
      DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
      orFieldLogic.setFieldRelation(FieldRelation.OR);
      DefaultFieldLogic leftBegin = new DefaultFieldLogic();
      String end_datetime_ = null;
      String start_datetime_ = null;
      List<WhereClause> whereClauses = queryFilter.getFieldLogic().getWhereClauses();
      DefaultQueryField startDatetime = null;
      DefaultQueryField endDatetime = null;
      Iterator var9 = whereClauses.iterator();

      DefaultQueryField allField;
      while(var9.hasNext()) {
         WhereClause clause = (WhereClause)var9.next();
         if (clause instanceof DefaultQueryField) {
            allField = (DefaultQueryField)clause;
            if ("start_datetime_".equalsIgnoreCase(allField.getField())) {
               start_datetime_ = allField.getValue() == null ? "" : allField.getValue().toString();
               startDatetime = allField;
            } else if ("end_datetime_".equalsIgnoreCase(allField.getField())) {
               end_datetime_ = allField.getValue() == null ? "" : allField.getValue().toString();
               endDatetime = allField;
            }
         }
      }

      if (!StringUtil.isEmpty(end_datetime_) && !StringUtil.isEmpty(start_datetime_)) {
         whereClauses.remove(startDatetime);
         whereClauses.remove(endDatetime);
         leftBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), start_datetime_));
         leftBegin.getWhereClauses().add(new DefaultQueryField("end_datetime_", QueryOP.getByVal("GT"), start_datetime_));
         DefaultFieldLogic rightBegin = new DefaultFieldLogic();
         rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("GT"), start_datetime_));
         rightBegin.getWhereClauses().add(new DefaultQueryField("start_datetime_", QueryOP.getByVal("LT"), end_datetime_));
         orFieldLogic.getWhereClauses().add(leftBegin);
         orFieldLogic.getWhereClauses().add(rightBegin);
         whereClauses.add(orFieldLogic);
         whereClauses.add(new DefaultQueryField("enable_", QueryOP.EQUAL, "1"));
         DefaultQueryField flowKey = null;
         Iterator var22 = whereClauses.iterator();

         while(var22.hasNext()) {
            WhereClause clause = (WhereClause)var22.next();
            if (clause instanceof DefaultQueryField) {
               DefaultQueryField clauseField = (DefaultQueryField)clause;
               if ("agency_flow_key_".equalsIgnoreCase(clauseField.getField())) {
                  flowKey = clauseField;
                  whereClauses.remove(clauseField);
                  break;
               }
            }
         }

         allField = new DefaultQueryField("agency_flow_key_", QueryOP.EQUAL, "");
         whereClauses.add(allField);
         List<BpmUserAgencyConfig> allConfigs = this.selectTabListJson((String)null, (String)null, queryFilter);
         if (allConfigs.size() > 0) {
            return allConfigs;
         } else {
            whereClauses.remove(allField);
            if (null != flowKey) {
               String value = flowKey.getValue().toString();
               if (value.indexOf(",") != -1) {
                  whereClauses.remove(flowKey);
                  List<BpmUserAgencyConfig> configs = new ArrayList();
                  String[] var15 = value.split(",");
                  int var16 = var15.length;

                  for(int var17 = 0; var17 < var16; ++var17) {
                     String key = var15[var17];
                     DefaultQueryField keyField = new DefaultQueryField("agency_flow_key_", QueryOP.getByVal("LK"), key);
                     whereClauses.add(keyField);
                     configs.addAll(this.selectTabListJson((String)null, (String)null, queryFilter));
                     whereClauses.remove(keyField);
                     if (configs.size() > 0) {
                        return configs;
                     }
                  }

                  if (configs.size() == 0) {
                     return configs;
                  }
               } else {
                  whereClauses.add(flowKey);
               }
            }

            return this.selectTabListJson((String)null, (String)null, queryFilter);
         }
      } else {
         throw new Exception("起始时间或结束时间为空");
      }
   }

   public int insertSelective(BpmUserAgencyConfig record) {
      return this.bpmUserAgencyConfigDao.insertSelective(record);
   }

   public int updateByPrimaryKeySelective(BpmUserAgencyConfig record) {
      return this.bpmUserAgencyConfigDao.updateByPrimaryKeySelective(record);
   }

   public void removeByIds(String... ids) {
      this.bpmUserAgencyLogDao.removeByConfigIds((Set)Arrays.stream(ids).collect(Collectors.toSet()));
      super.removeByIds(ids);
   }

   public void remove(String entityId) {
      HashSet<String> entityIds = new HashSet(1);
      entityIds.add(entityId);
      this.bpmUserAgencyLogDao.removeByConfigIds(entityIds);
      super.remove(entityId);
   }
}
