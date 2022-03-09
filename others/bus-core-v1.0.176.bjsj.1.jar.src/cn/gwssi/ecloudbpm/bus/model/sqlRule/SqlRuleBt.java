/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlRuleBt
/*    */   extends SqlRuleExpression
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   
/*    */   public String getType() {
/* 25 */     return SqlRuleType.BT.getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 30 */     String dbType = sqlRuleParam.getDbType();
/* 31 */     Map<String, Object> params = sqlRuleParam.getParams();
/* 32 */     Map<String, String> types = sqlRuleParam.getTypes();
/* 33 */     boolean hasParam = false;
/* 34 */     StringBuilder sql = new StringBuilder();
/* 35 */     String temp = "";
/* 36 */     if (StringUtils.isNotEmpty(this.filter)) {
/* 37 */       if (null != this.hidden && true == this.hidden.booleanValue()) {
/* 38 */         hasParam = true;
/* 39 */         temp = this.value;
/*    */       }
/* 41 */       else if (params.containsKey(this.filter)) {
/* 42 */         hasParam = true;
/* 43 */         temp = String.valueOf(params.get(this.filter));
/*    */       } 
/*    */     }
/*    */     
/* 47 */     if (hasParam) {
/* 48 */       List<String> lstParam = JSON.parseArray(temp, String.class);
/* 49 */       if (lstParam.size() == 2) {
/* 50 */         String param1 = lstParam.get(0);
/* 51 */         String param2 = lstParam.get(1);
/* 52 */         if (StringUtils.isNotEmpty(param1) && StringUtils.isNotEmpty(param2)) {
/* 53 */           sql.append("(");
/*    */         }
/* 55 */         if (StringUtils.isNotEmpty(param1)) {
/* 56 */           String id = IdUtil.getSuid();
/* 57 */           sql.append(this.filter).append(" >= ").append("#{").append(id).append("}");
/* 58 */           params.put(id, getFormatValue(param1, types.get(this.filter)));
/*    */         } 
/* 60 */         if (StringUtils.isNotEmpty(param1) && StringUtils.isNotEmpty(param2)) {
/* 61 */           sql.append(" and ");
/*    */         }
/* 63 */         if (StringUtils.isNotEmpty(param2)) {
/* 64 */           String id = IdUtil.getSuid();
/* 65 */           sql.append(this.filter).append(" <= ").append("#{").append(id).append("}");
/* 66 */           params.put(id, getFormatValue(param2, types.get(this.filter)));
/*    */         } 
/* 68 */         if (StringUtils.isNotEmpty(param1) && StringUtils.isNotEmpty(param2)) {
/* 69 */           sql.append(")");
/*    */         }
/*    */       } 
/*    */     } 
/* 73 */     return sql.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleBt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */