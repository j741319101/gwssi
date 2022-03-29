package com.dstz.bpm.platform;

import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.model.task.IBpmTask;
import com.dstz.bpm.api.platform.IBpmTaskPlatFormService;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.manager.BpmTaskManager;
import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
import com.dstz.base.api.query.Direction;
import com.dstz.base.api.query.FieldSort;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultFieldSort;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.bpm.service.BpmSomeService;
import com.dstz.org.api.model.dto.PageDTO;
import com.dstz.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BpmTaskPlatFormServiceImpl implements IBpmTaskPlatFormService {
   @Resource
   private BpmSomeService bpmSomeService;
   @Resource
   private BpmTaskManager bpmTaskManager;
   @Autowired
   BpmInstanceManager bpmInstanceManager;

   public String doAction(FlowRequestParam flowParam, String userId) {
      DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
      return taskModel.executeCmd();
   }

   public Map<String, Object> handleNodeFreeSelectUser(FlowRequestParam flowParam, String userId) {
      return this.bpmSomeService.handleNodeFreeSelectUser(flowParam);
   }

   public PageResult<List<IBpmTask>> getTodoList(Integer offset, Integer limit, String userId) {
      QueryFilter queryFilter = new DefaultQueryFilter();
      RowBounds rowBounds = new RowBounds(offset, limit);
      DefaultPage page = new DefaultPage(rowBounds);
      queryFilter.setPage(page);
      return new PageResult(this.bpmTaskManager.getTodoList(userId, queryFilter));
   }

   public PageResult<List<IBpmTask>> getMyTodoList(PageDTO page, String nodeKey) {
      QueryFilter filter = this.getQueryFilter(page);
      if (StringUtils.isNotEmpty(nodeKey)) {
         filter.addFilter("node.key_", nodeKey, QueryOP.IN);
      }

      String userId = ContextUtil.getCurrentUserId();
      return new PageResult(this.bpmTaskManager.getTodoList(userId, filter));
   }

   public PageResult<List<IBpmTask>> getApproveList(PageDTO page, String nodeKey) {
      QueryFilter filter = this.getQueryFilter(page);
      if (StringUtils.isNotEmpty(nodeKey)) {
         filter.addFilter("node.key_", nodeKey, QueryOP.IN);
      }

      String userId = ContextUtil.getCurrentUserId();
      return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, filter));
   }

   public PageResult<List<IBpmTask>> getApplyTaskList(PageDTO page, String nodeKey, String status) {
      QueryFilter filter = this.getQueryFilter(page);
      if (StringUtils.isNotEmpty(nodeKey)) {
         filter.addFilter("node.key_", nodeKey, QueryOP.IN);
      }

      if (StringUtils.isNotEmpty(status)) {
         filter.addFilter("status_", status, QueryOP.EQUAL);
      }

      String userId = ContextUtil.getCurrentUserId();
      return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, filter));
   }

   private QueryFilter getQueryFilter(PageDTO page) {
      String noPage = page.getNoPage();
      DefaultQueryFilter queryFilter;
      String sort;
      String order;
      if (StringUtils.isNotEmpty(noPage)) {
         queryFilter = new DefaultQueryFilter(true);
      } else {
         queryFilter = new DefaultQueryFilter();
         sort = page.getOffset();
         order = page.getLimit();
         if (StringUtil.isNotEmpty(sort) && StringUtil.isNotEmpty(order)) {
            RowBounds rowBounds = new RowBounds(Integer.parseInt(sort), Integer.parseInt(order));
            DefaultPage pageTemp = new DefaultPage(rowBounds);
            queryFilter.setPage(pageTemp);
         }
      }

      try {
         sort = page.getSort();
         order = page.getOrder();
         if (StringUtil.isNotEmpty(sort)) {
            String[] sorts = sort.split(",");
            String[] orders = new String[0];
            if (StringUtils.isNotEmpty(order)) {
               orders = order.split(",");
            }

            List<FieldSort> fieldSorts = new ArrayList();

            for(int i = 0; i < sorts.length; ++i) {
               String sortTemp = sorts[i];
               String orderTemp = Direction.ASC.name();
               if (orders.length > i) {
                  orderTemp = orders[i];
               }

               fieldSorts.add(new DefaultFieldSort(sortTemp, Direction.fromString(orderTemp)));
            }

            queryFilter.setFieldSortList(fieldSorts);
         }
      } catch (Exception var12) {
         ;
      }

      return queryFilter;
   }
}
