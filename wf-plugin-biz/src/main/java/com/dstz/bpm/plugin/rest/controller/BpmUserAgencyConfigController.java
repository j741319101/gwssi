package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.rest.BaseController;
import com.dstz.sys.util.ContextUtil;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/userAgencyConfig"})
public class BpmUserAgencyConfigController extends BaseController<BpmUserAgencyConfig> {
   @Autowired
   private BpmUserAgencyConfigManager bpmUserAgencyConfigManager;

   protected String getModelDesc() {
      return "业务流程用户代理配置";
   }

   @CatchErr
   @RequestMapping({"/tabList"})
   public PageResult tabList(BpmUserAgencyConfigTabDTO bpmUserAgencyConfigTabDTO) {
      bpmUserAgencyConfigTabDTO.setConfigUserId(ContextUtil.getCurrentUserId());
      Page<BpmUserAgencyConfig> bpmUserAgencyConfigPage = this.bpmUserAgencyConfigManager.selectTabList(bpmUserAgencyConfigTabDTO);
      return new PageResult(bpmUserAgencyConfigPage, (int)bpmUserAgencyConfigPage.getTotal());
   }

   @CatchErr
   @RequestMapping({"/tablistJson"})
   public PageResult<BpmUserAgencyConfig> tablistJson(HttpServletRequest request) {
      String tab = request.getParameter("tab");
      String name = request.getParameter("name");
      QueryFilter queryFilter = this.getQueryFilter(request);
      queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page)this.bpmUserAgencyConfigManager.selectTabListJson(tab, name, queryFilter);
      return new PageResult(bpmUserAgencyConfig);
   }

   @CatchErr
   @RequestMapping({"/targetTablistJson"})
   public PageResult<BpmUserAgencyConfig> targetTablistJson(HttpServletRequest request) {
      String tab = request.getParameter("tab");
      String name = request.getParameter("name");
      QueryFilter queryFilter = this.getQueryFilter(request);
      queryFilter.addFilter("TARGET_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page)this.bpmUserAgencyConfigManager.selectTargetListJson(tab, name, queryFilter);
      return new PageResult(bpmUserAgencyConfig);
   }

   @CatchErr
   @RequestMapping({"/checkAgencyConfig"})
   public PageResult<BpmUserAgencyConfig> checkAgencyConfig(HttpServletRequest request) {
      QueryFilter queryFilter = this.getQueryFilter(request);
      Page bpmUserAgencyConfig = null;

      try {
         queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
         bpmUserAgencyConfig = (Page)this.bpmUserAgencyConfigManager.checkTabListJson(queryFilter);
      } catch (Exception var5) {
         ;
      }

      return new PageResult(bpmUserAgencyConfig);
   }

   @CatchErr
   @RequestMapping({"/disable"})
   public ResultMsg<String> disable(@RequestParam("id") String id) {
      BpmUserAgencyConfig bpmUserAgencyConfig = new BpmUserAgencyConfig();
      bpmUserAgencyConfig.setId(id);
      bpmUserAgencyConfig.setEnable(Boolean.FALSE);
      this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
      return this.getSuccessResult();
   }

   public ResultMsg<String> save(@RequestBody BpmUserAgencyConfig bpmUserAgencyConfig) throws Exception {
      if (bpmUserAgencyConfig.getEnable()) {
         QueryFilter queryFilter = new DefaultQueryFilter();
         queryFilter.addFilter("start_datetime_", DateUtil.format(bpmUserAgencyConfig.getStartDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
         queryFilter.addFilter("end_datetime_", DateUtil.format(bpmUserAgencyConfig.getEndDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
         queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
         if (StringUtil.isNotEmpty(bpmUserAgencyConfig.getId())) {
            queryFilter.addFilter("id_", bpmUserAgencyConfig.getId(), QueryOP.NOT_EQUAL);
         }

         queryFilter.addFilter("agency_flow_key_", bpmUserAgencyConfig.getAgencyFlowKey(), QueryOP.EQUAL);
         List<BpmUserAgencyConfig> configs = this.bpmUserAgencyConfigManager.checkTabListJson(queryFilter);
         if (configs.size() > 0) {
            return this.getWarnResult(String.format("存在%s条代理配置已生效或存在相同代理配置，请检查代理配置记录", configs.size()));
         }
      }

      bpmUserAgencyConfig.setAgencyFlowKey(StringUtils.trimToEmpty(bpmUserAgencyConfig.getAgencyFlowKey()));
      bpmUserAgencyConfig.setAgencyFlowName(StringUtils.trimToEmpty(bpmUserAgencyConfig.getAgencyFlowName()));
      String desc;
      if (StringUtil.isEmpty(bpmUserAgencyConfig.getId())) {
         desc = "添加%s成功";
         bpmUserAgencyConfig.setConfigUserId(ContextUtil.getCurrentUserId());
         this.bpmUserAgencyConfigManager.insertSelective(bpmUserAgencyConfig);
      } else {
         this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
         desc = "更新%s成功";
      }

      return this.getSuccessResult(String.format(desc, this.getModelDesc()));
   }
}
