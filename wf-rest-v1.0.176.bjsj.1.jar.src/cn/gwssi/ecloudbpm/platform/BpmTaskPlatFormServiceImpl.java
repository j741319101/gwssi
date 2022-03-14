/*     */ package cn.gwssi.ecloudbpm.platform;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.service.BpmSomeService;
/*     */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.bpm.api.platform.IBpmTaskPlatFormService;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.engine.action.cmd.DefualtTaskActionCmd;
/*     */ import com.dstz.base.api.Page;
/*     */ import com.dstz.base.api.query.Direction;
/*     */ import com.dstz.base.api.query.FieldSort;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.db.model.query.DefaultFieldSort;
/*     */ import com.dstz.base.db.model.query.DefaultPage;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.org.api.model.dto.PageDTO;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.ibatis.session.RowBounds;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class BpmTaskPlatFormServiceImpl
/*     */   implements IBpmTaskPlatFormService
/*     */ {
/*     */   @Resource
/*     */   private BpmSomeService bpmSomeService;
/*     */   @Resource
/*     */   private BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   public String doAction(FlowRequestParam flowParam, String userId) {
/*  43 */     DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
/*  44 */     return taskModel.executeCmd();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> handleNodeFreeSelectUser(FlowRequestParam flowParam, String userId) {
/*  49 */     return this.bpmSomeService.handleNodeFreeSelectUser(flowParam);
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResult<List<IBpmTask>> getTodoList(Integer offset, Integer limit, String userId) {
/*  54 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  55 */     RowBounds rowBounds = new RowBounds(offset.intValue(), limit.intValue());
/*  56 */     DefaultPage page = new DefaultPage(rowBounds);
/*  57 */     defaultQueryFilter.setPage((Page)page);
/*  58 */     return new PageResult(this.bpmTaskManager.getTodoList(userId, (QueryFilter)defaultQueryFilter));
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResult<List<IBpmTask>> getMyTodoList(PageDTO page, String nodeKey) {
/*  63 */     QueryFilter filter = getQueryFilter(page);
/*  64 */     if (StringUtils.isNotEmpty(nodeKey)) {
/*  65 */       filter.addFilter("node.key_", nodeKey, QueryOP.IN);
/*     */     }
/*  67 */     String userId = ContextUtil.getCurrentUserId();
/*  68 */     return new PageResult(this.bpmTaskManager.getTodoList(userId, filter));
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResult<List<IBpmTask>> getApproveList(PageDTO page, String nodeKey) {
/*  73 */     QueryFilter filter = getQueryFilter(page);
/*  74 */     if (StringUtils.isNotEmpty(nodeKey)) {
/*  75 */       filter.addFilter("node.key_", nodeKey, QueryOP.IN);
/*     */     }
/*  77 */     String userId = ContextUtil.getCurrentUserId();
/*  78 */     return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, filter));
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResult<List<IBpmTask>> getApplyTaskList(PageDTO page, String nodeKey, String status) {
/*  83 */     QueryFilter filter = getQueryFilter(page);
/*  84 */     if (StringUtils.isNotEmpty(nodeKey)) {
/*  85 */       filter.addFilter("node.key_", nodeKey, QueryOP.IN);
/*     */     }
/*  87 */     if (StringUtils.isNotEmpty(status)) {
/*  88 */       filter.addFilter("status_", status, QueryOP.EQUAL);
/*     */     }
/*  90 */     String userId = ContextUtil.getCurrentUserId();
/*  91 */     return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, filter));
/*     */   }
/*     */   private QueryFilter getQueryFilter(PageDTO page) {
/*     */     DefaultQueryFilter queryFilter;
/*  95 */     String noPage = page.getNoPage();
/*     */     
/*  97 */     if (StringUtils.isNotEmpty(noPage)) {
/*  98 */       queryFilter = new DefaultQueryFilter(true);
/*     */     } else {
/* 100 */       queryFilter = new DefaultQueryFilter();
/* 101 */       String offset = page.getOffset();
/* 102 */       String limit = page.getLimit();
/* 103 */       if (StringUtil.isNotEmpty(offset) && StringUtil.isNotEmpty(limit)) {
/* 104 */         RowBounds rowBounds = new RowBounds(Integer.parseInt(offset), Integer.parseInt(limit));
/* 105 */         DefaultPage pageTemp = new DefaultPage(rowBounds);
/* 106 */         queryFilter.setPage((Page)pageTemp);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 111 */       String sort = page.getSort();
/* 112 */       String order = page.getOrder();
/* 113 */       if (StringUtil.isNotEmpty(sort)) {
/* 114 */         String[] sorts = sort.split(",");
/* 115 */         String[] orders = new String[0];
/* 116 */         if (StringUtils.isNotEmpty(order)) {
/* 117 */           orders = order.split(",");
/*     */         }
/* 119 */         List<FieldSort> fieldSorts = new ArrayList<>();
/* 120 */         for (int i = 0; i < sorts.length; i++) {
/* 121 */           String sortTemp = sorts[i];
/* 122 */           String orderTemp = Direction.ASC.name();
/* 123 */           if (orders.length > i) {
/* 124 */             orderTemp = orders[i];
/*     */           }
/* 126 */           fieldSorts.add(new DefaultFieldSort(sortTemp, Direction.fromString(orderTemp)));
/*     */         } 
/* 128 */         queryFilter.setFieldSortList(fieldSorts);
/*     */       } 
/* 130 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 133 */     return (QueryFilter)queryFilter;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmTaskPlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */