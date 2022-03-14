package com.dstz.bpm.plugin.rest.controller;

import com.dstz.bpm.plugin.core.manager.BpmReminderLogManager;
import com.dstz.bpm.plugin.core.model.BpmReminderLog;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.rest.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/bpmReminderLog"})
public class BpmReminderLogController extends BaseController<BpmReminderLog> {
   @Resource
   BpmReminderLogManager bpmReminderLogManager;

   protected String getModelDesc() {
      return "流程催办日志";
   }

   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
      QueryFilter queryFilter = this.getQueryFilter(request);
      List<BpmReminderLog> pageList = this.bpmReminderLogManager.query(queryFilter);
      return new PageResult(pageList);
   }
}
