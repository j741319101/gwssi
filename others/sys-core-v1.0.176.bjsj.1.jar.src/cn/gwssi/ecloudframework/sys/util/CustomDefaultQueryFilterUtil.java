/*    */ package cn.gwssi.ecloudframework.sys.util;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.Page;
/*    */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultPage;
/*    */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*    */ import org.apache.ibatis.session.RowBounds;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomDefaultQueryFilterUtil
/*    */ {
/*    */   public static QueryFilter setDefaultQueryFilter() {
/* 17 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 18 */     DefaultPage page = new DefaultPage(new RowBounds());
/* 19 */     defaultQueryFilter.setPage((Page)page);
/* 20 */     return (QueryFilter)defaultQueryFilter;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/CustomDefaultQueryFilterUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */