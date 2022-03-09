/*    */ package cn.gwssi.ecloudframework.base.dao.baseinterceptor;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.Page;
/*    */ import cn.gwssi.ecloudframework.base.api.query.FieldSort;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.hutool.core.util.ArrayUtil;
/*    */ import com.github.pagehelper.PageHelper;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.ibatis.cache.CacheKey;
/*    */ import org.apache.ibatis.executor.Executor;
/*    */ import org.apache.ibatis.mapping.BoundSql;
/*    */ import org.apache.ibatis.mapping.MappedStatement;
/*    */ import org.apache.ibatis.plugin.Interceptor;
/*    */ import org.apache.ibatis.plugin.Intercepts;
/*    */ import org.apache.ibatis.plugin.Invocation;
/*    */ import org.apache.ibatis.plugin.Plugin;
/*    */ import org.apache.ibatis.plugin.Signature;
/*    */ import org.apache.ibatis.session.ResultHandler;
/*    */ import org.apache.ibatis.session.RowBounds;
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
/*    */ @Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
/*    */ public class QueryInterceptor
/*    */   implements Interceptor
/*    */ {
/*    */   public Object intercept(Invocation invocation) throws Throwable {
/* 40 */     Object[] args = invocation.getArgs();
/* 41 */     if (ArrayUtil.isEmpty(args) || args.length < 2) {
/* 42 */       return invocation.proceed();
/*    */     }
/*    */     
/* 45 */     Object param = args[1];
/*    */ 
/*    */     
/* 48 */     if (param instanceof QueryFilter) {
/* 49 */       QueryFilter filter = (QueryFilter)param;
/*    */ 
/*    */       
/* 52 */       Map<String, Object> params = getQueryParamsByFilter(filter);
/* 53 */       args[1] = params;
/*    */       
/* 55 */       Page page = filter.getPage();
/* 56 */       if (page != null) {
/* 57 */         PageHelper.startPage(page.getPageNo().intValue(), page.getPageSize().intValue(), true);
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     return invocation.proceed();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Map<String, Object> getQueryParamsByFilter(QueryFilter filter) {
/* 71 */     String dynamicWhereSql = filter.getFieldLogic().getSql();
/* 72 */     Map<String, Object> params = filter.getParams();
/*    */ 
/*    */     
/* 75 */     String defaultWhere = params.containsKey("defaultWhere") ? params.get("defaultWhere").toString() : "";
/* 76 */     if (StringUtils.isNotEmpty(defaultWhere)) {
/* 77 */       dynamicWhereSql = StringUtils.isNotEmpty(dynamicWhereSql) ? (dynamicWhereSql + " and " + defaultWhere) : defaultWhere;
/*    */     }
/*    */     
/* 80 */     if (StringUtils.isNotEmpty(dynamicWhereSql)) {
/* 81 */       params.put("whereSql", dynamicWhereSql);
/*    */     }
/*    */     
/* 84 */     if (filter.getFieldSortList().size() > 0) {
/* 85 */       StringBuilder sb = new StringBuilder();
/* 86 */       for (FieldSort fieldSort : filter.getFieldSortList()) {
/* 87 */         sb.append(fieldSort.getField()).append(" ").append(fieldSort.getDirection()).append(",");
/*    */       }
/* 89 */       sb.deleteCharAt(sb.length() - 1);
/* 90 */       params.put("orderBySql", sb.toString());
/*    */     } 
/* 92 */     return params;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object plugin(Object target) {
/* 97 */     return Plugin.wrap(target, this);
/*    */   }
/*    */   
/*    */   public void setProperties(Properties properties) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/dao/baseinterceptor/QueryInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */