/*     */ package com.dstz.bpm.plugin.rest.controller;
/*     */ 
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.github.pagehelper.Page;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/userAgencyConfig"})
/*     */ public class BpmUserAgencyConfigController
/*     */   extends BaseController<BpmUserAgencyConfig>
/*     */ {
/*     */   @Autowired
/*     */   private BpmUserAgencyConfigManager bpmUserAgencyConfigManager;
/*     */   
/*     */   protected String getModelDesc() {
/*  42 */     return "业务流程用户代理配置";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/tabList"})
/*     */   public PageResult tabList(BpmUserAgencyConfigTabDTO bpmUserAgencyConfigTabDTO) {
/*  54 */     bpmUserAgencyConfigTabDTO.setConfigUserId(ContextUtil.getCurrentUserId());
/*  55 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfigPage = this.bpmUserAgencyConfigManager.selectTabList(bpmUserAgencyConfigTabDTO);
/*  56 */     return new PageResult((List)bpmUserAgencyConfigPage, Integer.valueOf((int)bpmUserAgencyConfigPage.getTotal()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/tablistJson"})
/*     */   public PageResult<BpmUserAgencyConfig> tablistJson(HttpServletRequest request) {
/*  68 */     String tab = request.getParameter("tab");
/*  69 */     String name = request.getParameter("name");
/*  70 */     QueryFilter queryFilter = getQueryFilter(request);
/*  71 */     queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  72 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.selectTabListJson(tab, name, queryFilter);
/*  73 */     return new PageResult((List)bpmUserAgencyConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/targetTablistJson"})
/*     */   public PageResult<BpmUserAgencyConfig> targetTablistJson(HttpServletRequest request) {
/*  84 */     String tab = request.getParameter("tab");
/*  85 */     String name = request.getParameter("name");
/*  86 */     QueryFilter queryFilter = getQueryFilter(request);
/*  87 */     queryFilter.addFilter("TARGET_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  88 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.selectTargetListJson(tab, name, queryFilter);
/*  89 */     return new PageResult((List)bpmUserAgencyConfig);
/*     */   }
/*     */   @CatchErr
/*     */   @RequestMapping({"/checkAgencyConfig"})
/*     */   public PageResult<BpmUserAgencyConfig> checkAgencyConfig(HttpServletRequest request) {
/*  94 */     QueryFilter queryFilter = getQueryFilter(request);
/*  95 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = null;
/*     */     try {
/*  97 */       queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  98 */       bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.checkTabListJson(queryFilter);
/*  99 */     } catch (Exception exception) {}
/*     */     
/* 101 */     return new PageResult((List)bpmUserAgencyConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CatchErr
/*     */   @RequestMapping({"/disable"})
/*     */   public ResultMsg<String> disable(@RequestParam("id") String id) {
/* 113 */     BpmUserAgencyConfig bpmUserAgencyConfig = new BpmUserAgencyConfig();
/* 114 */     bpmUserAgencyConfig.setId(id);
/* 115 */     bpmUserAgencyConfig.setEnable(Boolean.FALSE);
/* 116 */     this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
/* 117 */     return getSuccessResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultMsg<String> save(@RequestBody BpmUserAgencyConfig bpmUserAgencyConfig) throws Exception {
/*     */     String desc;
/* 123 */     if (bpmUserAgencyConfig.getEnable().booleanValue()) {
/* 124 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 125 */       defaultQueryFilter.addFilter("start_datetime_", DateUtil.format(bpmUserAgencyConfig.getStartDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
/* 126 */       defaultQueryFilter.addFilter("end_datetime_", DateUtil.format(bpmUserAgencyConfig.getEndDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
/* 127 */       defaultQueryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 128 */       if (StringUtil.isNotEmpty(bpmUserAgencyConfig.getId())) {
/* 129 */         defaultQueryFilter.addFilter("id_", bpmUserAgencyConfig.getId(), QueryOP.NOT_EQUAL);
/*     */       }
/* 131 */       defaultQueryFilter.addFilter("agency_flow_key_", bpmUserAgencyConfig.getAgencyFlowKey(), QueryOP.EQUAL);
/* 132 */       List<BpmUserAgencyConfig> configs = this.bpmUserAgencyConfigManager.checkTabListJson((QueryFilter)defaultQueryFilter);
/* 133 */       if (configs.size() > 0) {
/* 134 */         return getWarnResult(String.format("存在%s条代理配置已生效或存在相同代理配置，请检查代理配置记录", new Object[] { Integer.valueOf(configs.size()) }));
/*     */       }
/*     */     } 
/* 137 */     bpmUserAgencyConfig.setAgencyFlowKey(StringUtils.trimToEmpty(bpmUserAgencyConfig.getAgencyFlowKey()));
/* 138 */     bpmUserAgencyConfig.setAgencyFlowName(StringUtils.trimToEmpty(bpmUserAgencyConfig.getAgencyFlowName()));
/* 139 */     if (StringUtil.isEmpty(bpmUserAgencyConfig.getId())) {
/* 140 */       desc = "添加%s成功";
/* 141 */       bpmUserAgencyConfig.setConfigUserId(ContextUtil.getCurrentUserId());
/* 142 */       this.bpmUserAgencyConfigManager.insertSelective(bpmUserAgencyConfig);
/*     */     } else {
/* 144 */       this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
/* 145 */       desc = "更新%s成功";
/*     */     } 
/* 147 */     return getSuccessResult(String.format(desc, new Object[] { getModelDesc() }));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/rest/controller/BpmUserAgencyConfigController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */