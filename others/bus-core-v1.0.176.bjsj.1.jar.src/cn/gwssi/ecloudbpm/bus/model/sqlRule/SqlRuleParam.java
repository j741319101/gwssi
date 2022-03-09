/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlRuleParam
/*    */   implements ISqlRuleParam
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/* 20 */   private Logger LOG = LoggerFactory.getLogger(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   private String dbType;
/*    */ 
/*    */ 
/*    */   
/* 28 */   private Map<String, Object> params = new LinkedHashMap<>();
/*    */ 
/*    */ 
/*    */   
/* 32 */   private Map<String, String> types = new LinkedHashMap<>();
/*    */ 
/*    */   
/*    */   public SqlRuleParam() {}
/*    */ 
/*    */   
/*    */   public SqlRuleParam(String dbType, Map<String, Object> params) {
/* 39 */     this.dbType = dbType;
/* 40 */     this.params = params;
/*    */   }
/*    */   
/*    */   public SqlRuleParam(String dbType, Map<String, Object> params, Map<String, String> types) {
/* 44 */     this.dbType = dbType;
/* 45 */     this.params = params;
/* 46 */     this.types = types;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDbType() {
/* 51 */     return this.dbType;
/*    */   }
/*    */   
/*    */   public void setDbType(String dbType) {
/* 55 */     this.dbType = dbType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Object> getParams() {
/* 60 */     return this.params;
/*    */   }
/*    */   
/*    */   public void setParams(Map<String, Object> params) {
/* 64 */     this.params = params;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, String> getTypes() {
/* 69 */     return this.types;
/*    */   }
/*    */   
/*    */   public void setTypes(Map<String, String> types) {
/* 73 */     this.types = types;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */