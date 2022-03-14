/*    */ package cn.gwssi.ecloudbpm.bus.executor.generateSql;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*    */ import com.dstz.base.api.constant.DbType;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.base.db.datasource.DbContextHolder;
/*    */ import com.dstz.base.db.model.table.Table;
/*    */ import com.dstz.base.db.tableoper.GenerateSqlByDbType;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class GenerateSqlDrds
/*    */   implements GenerateSqlByDbType {
/*    */   public boolean getCreateTableSql(StringBuilder sql, Table table) {
/* 17 */     if (StringUtils.equals(DbContextHolder.getDbType(), DbType.DRDS.getKey())) {
/* 18 */       BusinessTable businessTable = (BusinessTable)table;
/* 19 */       if (StringUtil.isNotEmpty(businessTable.getDbpartition())) {
/* 20 */         sql.append(" dbpartition by ").append(businessTable.getDbpartitionType())
/* 21 */           .append(" (").append(businessTable.getDbpartition()).append(") ");
/* 22 */         if (StringUtil.isNotEmpty(businessTable.getTbpartition())) {
/* 23 */           sql.append(" tbpartition by ").append(businessTable.getTbpartitionType())
/* 24 */             .append(" (").append(businessTable.getTbpartition()).append(") ");
/* 25 */           if (StringUtil.isNotEmpty(businessTable.getTbpartitions())) {
/* 26 */             sql.append(" tbpartitions ").append(businessTable.getTbpartitions());
/*    */           }
/*    */         } 
/*    */       } 
/* 30 */       return true;
/*    */     } 
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getUpdateTableSql(StringBuilder sql, Table table) {
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkUpdateTableColumn(Map<String, Object> columns, Table table) {
/* 42 */     if (StringUtils.equals(DbContextHolder.getDbType(), DbType.DRDS.getKey())) {
/* 43 */       BusinessTable businessTable = (BusinessTable)table;
/* 44 */       if (StringUtil.isNotEmpty(businessTable.getDbpartition())) {
/* 45 */         columns.remove(businessTable.getDbpartition());
/* 46 */         if (StringUtil.isNotEmpty(businessTable.getTbpartition())) {
/* 47 */           columns.remove(businessTable.getTbpartition());
/*    */         }
/*    */       } 
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/executor/generateSql/GenerateSqlDrds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */