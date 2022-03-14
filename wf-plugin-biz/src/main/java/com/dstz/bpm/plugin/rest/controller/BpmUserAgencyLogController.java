package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.BaseController;
import com.github.pagehelper.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/userAgencyLog"})
public class BpmUserAgencyLogController extends BaseController<BpmUserAgencyLog> {
   @Autowired
   private BpmUserAgencyLogManager bpmUserAgencyLogManager;

   protected String getModelDesc() {
      return "业务流程用户代理日志";
   }

   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      String tab = request.getParameter("tab");
      Page<BpmUserAgencyLogVO> pageList = null;
      if ("target".equals(tab)) {
         pageList = (Page)this.bpmUserAgencyLogManager.selectBpmTargetUserAgencyLogList(queryFilter);
      } else {
         pageList = (Page)this.bpmUserAgencyLogManager.selectBpmUserAgencyLogList(queryFilter);
      }

      return new PageResult(pageList);
   }
}
