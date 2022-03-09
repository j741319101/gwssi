/*    */ package com.github.pagehelper.dialect.helper;
/*    */ 
/*    */ import com.github.pagehelper.Page;
/*    */ import com.github.pagehelper.dialect.AbstractHelperDialect;
/*    */ import org.apache.ibatis.cache.CacheKey;
/*    */ 
/*    */ public class DmsqlDialect
/*    */   extends AbstractHelperDialect {
/*    */   public String getPageSql(String sql, Page page, CacheKey pageKey) {
/* 10 */     StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
/* 11 */     if (page.getStartRow() > 0) {
/* 12 */       sqlBuilder.append("SELECT * FROM ( ");
/*    */     }
/* 14 */     if (page.getEndRow() > 0) {
/* 15 */       sqlBuilder.append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
/*    */     }
/* 17 */     sqlBuilder.append(sql);
/* 18 */     if (page.getEndRow() > 0) {
/* 19 */       sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= ");
/* 20 */       sqlBuilder.append(page.getEndRow());
/* 21 */       pageKey.update(Integer.valueOf(page.getEndRow()));
/*    */     } 
/* 23 */     if (page.getStartRow() > 0) {
/* 24 */       sqlBuilder.append(" ) WHERE ROW_ID > ");
/* 25 */       sqlBuilder.append(page.getStartRow());
/* 26 */       pageKey.update(Integer.valueOf(page.getStartRow()));
/*    */     } 
/* 28 */     return sqlBuilder.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-act/0.2-SNAPSHOT/wf-act-0.2-SNAPSHOT.jar!/com/github/pagehelper/dialect/helper/DmsqlDialect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */