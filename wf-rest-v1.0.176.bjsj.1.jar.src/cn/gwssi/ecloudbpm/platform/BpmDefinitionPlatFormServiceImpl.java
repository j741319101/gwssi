/*    */ package cn.gwssi.ecloudbpm.platform;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*    */ import cn.gwssi.ecloudbpm.wf.api.platform.IBpmDefinitionPlatFormService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*    */ import cn.gwssi.ecloudframework.base.api.Page;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*    */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultPage;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.ibatis.session.RowBounds;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BpmDefinitionPlatFormServiceImpl implements IBpmDefinitionPlatFormService {
/*    */   @Resource
/*    */   private BpmDefinitionManager bpmDefinitionManager;
/*    */   @Resource
/*    */   private UserService userService;
/*    */   
/*    */   public PageResult<List<IBpmDefinition>> listJson(Integer offset, Integer limit, String defId, String defKey) {
/* 29 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 30 */     RowBounds rowBounds = new RowBounds(offset.intValue(), limit.intValue());
/* 31 */     DefaultPage page = new DefaultPage(rowBounds);
/* 32 */     defaultQueryFilter.setPage((Page)page);
/* 33 */     if (StringUtils.isNotEmpty(defId)) {
/* 34 */       defaultQueryFilter.addFilter("id_", defId, QueryOP.EQUAL);
/*    */     }
/* 36 */     if (StringUtils.isNotEmpty(defKey)) {
/* 37 */       defaultQueryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/* 38 */       defaultQueryFilter.addFilter("key_", defKey, QueryOP.EQUAL);
/*    */     } 
/* 40 */     return new PageResult(this.bpmDefinitionManager.query((QueryFilter)defaultQueryFilter));
/*    */   }
/*    */ 
/*    */   
/*    */   public PageResult<List<IBpmDefinition>> listMyJson(Integer offset, Integer limit, String defId, String defKey, String userId) {
/* 45 */     IUser user = this.userService.getUserById(userId);
/* 46 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 47 */     RowBounds rowBounds = new RowBounds(offset.intValue(), limit.intValue());
/* 48 */     DefaultPage page = new DefaultPage(rowBounds);
/* 49 */     defaultQueryFilter.setPage((Page)page);
/* 50 */     if (StringUtils.isNotEmpty(defId)) {
/* 51 */       defaultQueryFilter.addFilter("id_", defId, QueryOP.EQUAL);
/*    */     }
/* 53 */     if (StringUtils.isNotEmpty(defKey)) {
/* 54 */       defaultQueryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
/* 55 */       defaultQueryFilter.addFilter("key_", defKey, QueryOP.EQUAL);
/*    */     } 
/* 57 */     List<String> lstOrgId = new ArrayList<>();
/* 58 */     if (null != user) {
/* 59 */       lstOrgId = user.getManagerGroupIdList();
/*    */     }
/* 61 */     if (null != lstOrgId && lstOrgId.size() > 0) {
/* 62 */       defaultQueryFilter.addFilter("bpm_definition.org_id_", lstOrgId, QueryOP.IN);
/*    */     } else {
/* 64 */       defaultQueryFilter.addFilter("bpm_definition.org_id_", "", QueryOP.EQUAL);
/*    */     } 
/* 66 */     return new PageResult(this.bpmDefinitionManager.getMyDefinitionList(userId, (QueryFilter)defaultQueryFilter));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmDefinitionPlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */