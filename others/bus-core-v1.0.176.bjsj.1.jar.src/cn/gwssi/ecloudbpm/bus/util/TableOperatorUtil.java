/*    */ package cn.gwssi.ecloudbpm.bus.util;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*    */ import com.dstz.base.api.constant.DbType;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.base.db.model.table.Table;
/*    */ import com.dstz.base.db.tableoper.MysqlTableOperator;
/*    */ import com.dstz.base.db.tableoper.TableOperator;
/*    */ import com.dstz.sys.api.service.ISysDataSourceService;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TableOperatorUtil
/*    */ {
/*    */   public static TableOperator newOperator(BusinessTable table) {
/* 22 */     JdbcTemplate jdbcTemplate = ((ISysDataSourceService)AppUtil.getBean(ISysDataSourceService.class)).getJdbcTemplateByKey(table.getDsKey());
/* 23 */     if (jdbcTemplate == null) {
/* 24 */       throw new RuntimeException("当前系统不存在的数据源:" + table.getDsKey());
/*    */     }
/*    */     
/* 27 */     String type = DbType.MYSQL.getKey();
/*    */     
/* 29 */     if (DbType.MYSQL.equalsWithKey(type)) {
/* 30 */       return (TableOperator)new MysqlTableOperator((Table)table, jdbcTemplate);
/*    */     }
/*    */     
/* 33 */     throw new RuntimeException("找不到类型[" + type + "]的数据库处理者(TableOperator)");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/util/TableOperatorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */