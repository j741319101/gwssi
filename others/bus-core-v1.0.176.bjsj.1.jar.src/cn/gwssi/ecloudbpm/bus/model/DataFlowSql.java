/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowSql;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataFlowSql
/*    */   implements IDataFlowSql
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   private String[] sql;
/*    */   private String dsKey;
/*    */   private String dbType;
/*    */   
/*    */   public DataFlowSql() {}
/*    */   
/*    */   public DataFlowSql(String[] sql, String dsKey, String dbType) {
/* 33 */     this.sql = sql;
/* 34 */     this.dsKey = dsKey;
/* 35 */     this.dbType = dbType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSql() {
/* 40 */     return this.sql;
/*    */   }
/*    */   
/*    */   public void setSql(String[] sql) {
/* 44 */     this.sql = sql;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDsKey() {
/* 49 */     return this.dsKey;
/*    */   }
/*    */   
/*    */   public void setDsKey(String dsKey) {
/* 53 */     this.dsKey = dsKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDbType() {
/* 58 */     return this.dbType;
/*    */   }
/*    */   
/*    */   public void setDbType(String dbType) {
/* 62 */     this.dbType = dbType;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/DataFlowSql.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */