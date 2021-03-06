/*    */ package cn.gwssi.ecloudbpm.bus.model.sqlRule;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRuleParam;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import com.dstz.base.core.id.IdUtil;
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
/*    */ public class SqlRuleOrLike
/*    */   extends SqlRuleExpression
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   
/*    */   public String getType() {
/* 25 */     return SqlRuleType.ORLIKE.getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   String getDefaultSql(ISqlRuleParam sqlRuleParam) {
/* 30 */     String dbType = sqlRuleParam.getDbType();
/* 31 */     Map<String, Object> params = sqlRuleParam.getParams();
/* 32 */     Map<String, String> types = sqlRuleParam.getTypes();
/* 33 */     String defaultSql = this.filter + " ";
/* 34 */     StringBuilder sql = new StringBuilder();
/* 35 */     String temp = "";
/* 36 */     if (StringUtils.isNotEmpty(this.filter)) {
/* 37 */       if (null != this.hidden && true == this.hidden.booleanValue()) {
/* 38 */         sql = new StringBuilder(" ( " + defaultSql);
/* 39 */         temp = this.value;
/*    */       }
/* 41 */       else if (params.containsKey(this.filter)) {
/* 42 */         sql = new StringBuilder(" ( " + defaultSql);
/* 43 */         temp = String.valueOf(params.get(this.filter));
/*    */       } 
/*    */     }
/*    */     
/* 47 */     if (StringUtils.isNotEmpty(sql.toString())) {
/* 48 */       List<String> lstParam = JSON.parseArray(temp, String.class);
/* 49 */       if (lstParam.size() == 0) {
/* 50 */         lstParam.add("");
/*    */       }
/* 52 */       for (int i = 0; i < lstParam.size(); i++) {
/* 53 */         String param = lstParam.get(i);
/* 54 */         String id = IdUtil.getSuid();
/* 55 */         if (i != 0) {
/* 56 */           sql.append(" or ").append(defaultSql);
/*    */         }
/* 58 */         sql.append(" like CONCAT('%',#{" + id + "},'%')");
/* 59 */         params.put(id, getFormatValue(param, types.get(this.filter)));
/*    */       } 
/* 61 */       sql.append(")");
/*    */     } 
/* 63 */     return sql.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/sqlRule/SqlRuleOrLike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */