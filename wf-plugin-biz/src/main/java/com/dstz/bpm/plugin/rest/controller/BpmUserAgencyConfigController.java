/*     */ package com.dstz.bpm.plugin.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.bpm.plugin.core.manager.BpmUserAgencyConfigManager;
/*     */ import com.dstz.bpm.plugin.core.model.BpmUserAgencyConfig;
/*     */ import com.dstz.bpm.plugin.dto.BpmUserAgencyConfigTabDTO;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.github.pagehelper.Page;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*  41 */     return "业务流程用户代理配置";
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
/*  53 */     bpmUserAgencyConfigTabDTO.setConfigUserId(ContextUtil.getCurrentUserId());
/*  54 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfigPage = this.bpmUserAgencyConfigManager.selectTabList(bpmUserAgencyConfigTabDTO);
/*  55 */     return new PageResult((List)bpmUserAgencyConfigPage, Integer.valueOf((int)bpmUserAgencyConfigPage.getTotal()));
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
/*  67 */     String tab = request.getParameter("tab");
/*  68 */     String name = request.getParameter("name");
/*  69 */     QueryFilter queryFilter = getQueryFilter(request);
/*  70 */     queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  71 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.selectTabListJson(tab, name, queryFilter);
/*  72 */     return new PageResult((List)bpmUserAgencyConfig);
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
/*  83 */     String tab = request.getParameter("tab");
/*  84 */     String name = request.getParameter("name");
/*  85 */     QueryFilter queryFilter = getQueryFilter(request);
/*  86 */     queryFilter.addFilter("TARGET_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  87 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.selectTargetListJson(tab, name, queryFilter);
/*  88 */     return new PageResult((List)bpmUserAgencyConfig);
/*     */   }
/*     */   @CatchErr
/*     */   @RequestMapping({"/checkAgencyConfig"})
/*     */   public PageResult<BpmUserAgencyConfig> checkAgencyConfig(HttpServletRequest request) {
/*  93 */     QueryFilter queryFilter = getQueryFilter(request);
/*  94 */     Page<BpmUserAgencyConfig> bpmUserAgencyConfig = null;
/*     */     try {
/*  96 */       queryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/*  97 */       bpmUserAgencyConfig = (Page<BpmUserAgencyConfig>)this.bpmUserAgencyConfigManager.checkTabListJson(queryFilter);
/*  98 */     } catch (Exception exception) {}
/*     */     
/* 100 */     return new PageResult((List)bpmUserAgencyConfig);
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
/* 112 */     BpmUserAgencyConfig bpmUserAgencyConfig = new BpmUserAgencyConfig();
/* 113 */     bpmUserAgencyConfig.setId(id);
/* 114 */     bpmUserAgencyConfig.setEnable(Boolean.FALSE);
/* 115 */     this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
/* 116 */     return getSuccessResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultMsg<String> save(@RequestBody BpmUserAgencyConfig bpmUserAgencyConfig) throws Exception {
/*     */     String desc;
/* 122 */     if (bpmUserAgencyConfig.getEnable().booleanValue()) {
/* 123 */       DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 124 */       defaultQueryFilter.addFilter("start_datetime_", DateUtil.format(bpmUserAgencyConfig.getStartDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
/* 125 */       defaultQueryFilter.addFilter("end_datetime_", DateUtil.format(bpmUserAgencyConfig.getEndDatetime(), "yyyy-MM-dd HH:mm:ss"), QueryOP.EQUAL);
/* 126 */       defaultQueryFilter.addFilter("config_user_id_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
/* 127 */       if (StringUtil.isNotEmpty(bpmUserAgencyConfig.getId())) {
/* 128 */         defaultQueryFilter.addFilter("id_", bpmUserAgencyConfig.getId(), QueryOP.NOT_EQUAL);
/*     */       }
/* 130 */       defaultQueryFilter.addFilter("agency_flow_key_", bpmUserAgencyConfig.getAgencyFlowKey(), QueryOP.EQUAL);
/* 131 */       List<BpmUserAgencyConfig> configs = this.bpmUserAgencyConfigManager.checkTabListJson((QueryFilter)defaultQueryFilter);
/* 132 */       if (configs.size() > 0) {
/* 133 */         return getWarnResult(String.format("存在%s条代理配置已生效或存在相同代理配置，请检查代理配置记录", new Object[] { Integer.valueOf(configs.size()) }));
/*     */       }
/*     */     } 
/* 136 */     if (StringUtil.isEmpty(bpmUserAgencyConfig.getId())) {
/* 137 */       desc = "添加%s成功";
/* 138 */       bpmUserAgencyConfig.setConfigUserId(ContextUtil.getCurrentUserId());
/* 139 */       this.bpmUserAgencyConfigManager.insertSelective(bpmUserAgencyConfig);
/*     */     } else {
/* 141 */       this.bpmUserAgencyConfigManager.updateByPrimaryKeySelective(bpmUserAgencyConfig);
/* 142 */       desc = "更新%s成功";
/*     */     } 
/* 144 */     return getSuccessResult(String.format(desc, new Object[] { getModelDesc() }));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/rest/controller/BpmUserAgencyConfigController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */