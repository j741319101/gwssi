/*    */ package cn.gwssi.ecloudframework.base.dao;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.Page;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.mybatis.spring.SqlSessionTemplate;
/*    */ import org.springframework.stereotype.Repository;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Repository
/*    */ public class CommonDao<T>
/*    */ {
/*    */   @Resource(name = "abSqlSessionTemplate")
/*    */   protected SqlSessionTemplate sqlSessionTemplate;
/*    */   private static final String NAME_SPACE = "cn.gwssi.ecloudframework.sql.common";
/*    */   
/*    */   public int execute(String sql) {
/* 34 */     Map<String, String> map = new HashMap<>();
/* 35 */     map.put("sql", sql);
/* 36 */     String key = getNameSpace("execute");
/* 37 */     return this.sqlSessionTemplate.update(key, map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> query(String sql) {
/* 47 */     Map<String, String> map = new HashMap<>();
/* 48 */     map.put("sql", sql);
/* 49 */     String key = getNameSpace("query");
/* 50 */     return this.sqlSessionTemplate.selectList(key, map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> query(String sql, Page page) {
/* 61 */     DefaultQueryFilter query = new DefaultQueryFilter();
/* 62 */     query.addParamsFilter("sql", sql);
/* 63 */     query.setPage(page);
/*    */     
/* 65 */     String key = getNameSpace("query");
/* 66 */     return this.sqlSessionTemplate.selectList(key, query);
/*    */   }
/*    */   
/*    */   private String getNameSpace(String sqlKey) {
/* 70 */     return "cn.gwssi.ecloudframework.sql.common." + sqlKey;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> queryForListPage(String sql, QueryFilter queryFilter) {
/* 82 */     Assert.notNull(sql);
/* 83 */     queryFilter.addParamsFilter("sql", sql);
/* 84 */     return this.sqlSessionTemplate.selectList(getNameSpace("queryFormList"), queryFilter);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/dao/CommonDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */