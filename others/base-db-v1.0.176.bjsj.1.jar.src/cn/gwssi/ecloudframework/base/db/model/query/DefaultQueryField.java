/*     */ package cn.gwssi.ecloudframework.base.db.model.query;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryField;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultQueryField
/*     */   implements QueryField
/*     */ {
/*     */   private String field;
/*     */   private QueryOP compare;
/*     */   private Object value;
/*     */   
/*     */   public DefaultQueryField() {}
/*     */   
/*     */   public DefaultQueryField(String field, Object value) {
/*  34 */     this.field = field;
/*  35 */     this.value = value;
/*     */   }
/*     */   
/*     */   public DefaultQueryField(String field, QueryOP compare, Object value) {
/*  39 */     this.value = value;
/*  40 */     this.field = field;
/*  41 */     this.compare = compare;
/*     */     
/*  43 */     if (QueryOP.IN == compare || QueryOP.NOT_IN == compare) {
/*  44 */       this.value = getInValueSql();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getInValueSql() {
/*  55 */     List<String> listValue = null;
/*  56 */     if (this.value instanceof String) {
/*  57 */       listValue = Arrays.asList(((String)this.value).split(","));
/*     */     }
/*  59 */     if (this.value instanceof List) {
/*  60 */       listValue = (List<String>)this.value;
/*     */     }
/*     */     
/*  63 */     if (CollectionUtil.isEmpty(listValue)) {
/*  64 */       return "";
/*     */     }
/*     */     
/*  67 */     StringBuilder inSqlStr = new StringBuilder("(");
/*  68 */     for (String obj : listValue) {
/*  69 */       if (obj == null || "".equals(obj)) {
/*     */         continue;
/*     */       }
/*  72 */       inSqlStr.append("'");
/*  73 */       inSqlStr.append(obj.toString());
/*  74 */       inSqlStr.append("'");
/*  75 */       inSqlStr.append(",");
/*     */     } 
/*  77 */     inSqlStr = new StringBuilder(inSqlStr.substring(0, inSqlStr.length() - 1));
/*  78 */     inSqlStr.append(")");
/*  79 */     return inSqlStr.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getField() {
/*  84 */     return this.field;
/*     */   }
/*     */   
/*     */   public int getValueHashCode() {
/*  88 */     if (this.value == null) {
/*  89 */       return 0;
/*     */     }
/*  91 */     int hashCode = this.value.hashCode();
/*  92 */     if (hashCode < 0) {
/*  93 */       return -hashCode;
/*     */     }
/*  95 */     return hashCode;
/*     */   }
/*     */   public void setField(String field) {
/*  98 */     this.field = field;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue() {
/* 103 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(Object value) {
/* 107 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public QueryOP getCompare() {
/* 112 */     return this.compare;
/*     */   }
/*     */   
/*     */   public void setCompare(QueryOP compare) {
/* 116 */     this.compare = compare;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSql() {
/* 121 */     if (this.compare == null) {
/* 122 */       this.compare = QueryOP.EQUAL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     String fieldParam = "#{%s}";
/* 130 */     String sql = this.field;
/* 131 */     if (sql.lastIndexOf("^") != -1) {
/* 132 */       sql = sql.substring(0, sql.lastIndexOf("^"));
/*     */     }
/* 134 */     switch (this.compare) {
/*     */       case EQUAL:
/* 136 */         sql = sql + " = " + fieldParam;
/*     */         break;
/*     */       case EQUAL_IGNORE_CASE:
/* 139 */         sql = "upper(" + sql + ") = " + fieldParam;
/*     */         break;
/*     */       case LESS:
/* 142 */         sql = sql + " < " + fieldParam;
/*     */         break;
/*     */       case LESS_EQUAL:
/* 145 */         sql = sql + " <= " + fieldParam;
/*     */         break;
/*     */       case GREAT:
/* 148 */         sql = sql + " > " + fieldParam;
/*     */         break;
/*     */       case GREAT_EQUAL:
/* 151 */         sql = sql + " >= " + fieldParam;
/*     */         break;
/*     */       case NOT_EQUAL:
/* 154 */         sql = sql + " != " + fieldParam;
/*     */         break;
/*     */       case LEFT_LIKE:
/* 157 */         setValue("%" + this.value);
/* 158 */         sql = sql + " like " + fieldParam;
/*     */         break;
/*     */       case RIGHT_LIKE:
/* 161 */         setValue(this.value + "%");
/* 162 */         sql = sql + " like  " + fieldParam;
/*     */         break;
/*     */       case LIKE:
/* 165 */         setValue("%" + this.value + "%");
/* 166 */         sql = sql + " like  " + fieldParam;
/*     */         break;
/*     */       case NOT_LIKE:
/* 169 */         setValue("%" + this.value + "%");
/* 170 */         sql = sql + " not like  " + fieldParam;
/*     */         break;
/*     */       case IS_NULL:
/* 173 */         sql = sql + " is null";
/*     */         break;
/*     */       case NOTNULL:
/* 176 */         sql = sql + " is not null";
/*     */         break;
/*     */       case IN:
/* 179 */         sql = sql + " in  " + this.value;
/*     */         break;
/*     */       case NOT_IN:
/* 182 */         sql = sql + " not in  " + this.value;
/*     */         break;
/*     */       case BETWEEN:
/* 185 */         if (this.field.endsWith("-end")) {
/* 186 */           sql = this.field.substring(0, this.field.length() - 4) + " <= " + fieldParam; break;
/*     */         } 
/* 188 */         sql = sql + " >= " + fieldParam;
/*     */         break;
/*     */       
/*     */       default:
/* 192 */         sql = sql + " =  " + fieldParam;
/*     */         break;
/*     */     } 
/* 195 */     if (this.field.contains(".")) {
/* 196 */       fieldParam = this.field.replace(".", "") + "_" + getValueHashCode();
/*     */     } else {
/* 198 */       fieldParam = this.field + "_" + getValueHashCode();
/*     */     } 
/* 200 */     return String.format(sql, new Object[] { fieldParam });
/*     */   }
/*     */   
/*     */   private String getOrSql() {
/* 204 */     if (this.field.endsWith("~")) {
/* 205 */       this.field = this.field.substring(0, this.field.length() - 1);
/*     */     }
/* 207 */     String[] fields = this.field.split("~");
/* 208 */     StringBuilder fieldParam = new StringBuilder("#{");
/* 209 */     for (String field : fields) {
/* 210 */       if (field.contains(".")) {
/*     */         
/* 212 */         fieldParam.append(field.replace(".", "")).append("~");
/*     */       } else {
/* 214 */         fieldParam.append(field).append("~");
/*     */       } 
/*     */     } 
/* 217 */     fieldParam = new StringBuilder(fieldParam.substring(0, fieldParam.length() - 1) + "_" + getValueHashCode() + "}");
/*     */     
/* 219 */     StringBuilder sql = new StringBuilder("(");
/* 220 */     for (String field : fields) {
/* 221 */       if (!StringUtil.isEmpty(field))
/*     */       {
/*     */         
/* 224 */         if (field.contains("^"))
/* 225 */           field = field.substring(0, field.lastIndexOf("^")); 
/*     */       }
/*     */     } 
/* 228 */     switch (this.compare)
/*     */     { case EQUAL:
/* 230 */         for (i = 0; i < fields.length; i++) {
/* 231 */           if (i == fields.length - 1) {
/* 232 */             sql.append(fields[i]).append(" = ").append(fieldParam).append(" ) ");
/*     */           } else {
/* 234 */             sql.append(fields[i]).append(" = ").append(fieldParam).append(" OR ");
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 358 */         return sql.toString();case EQUAL_IGNORE_CASE: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append("upper(").append(fields[i]).append(") = ").append(fieldParam).append(" ) "); } else { sql.append("upper(").append(fields[i]).append(") = ").append(fieldParam).append(" OR "); }  }  return sql.toString();case LESS: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" < ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" < ").append(fieldParam).append(" OR "); }  }  return sql.toString();case LESS_EQUAL: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" <= ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" <= ").append(fieldParam).append(" OR "); }  }  return sql.toString();case GREAT: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" > ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" > ").append(fieldParam).append(" OR "); }  }  return sql.toString();case GREAT_EQUAL: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" >= ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" >= ").append(fieldParam).append(" OR "); }  }  return sql.toString();case NOT_EQUAL: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" != ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" != ").append(fieldParam).append(" OR "); }  }  return sql.toString();case LEFT_LIKE: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" like ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" like ").append(fieldParam).append(" OR "); }  }  setValue("%" + this.value); return sql.toString();case RIGHT_LIKE: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" like ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" like ").append(fieldParam).append(" OR "); }  }  setValue(this.value + "%"); return sql.toString();case LIKE: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" like ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" like ").append(fieldParam).append(" OR "); }  }  setValue("%" + this.value + "%"); return sql.toString();case IS_NULL: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" is null )"); } else { sql.append(fields[i]).append(" is null OR "); }  }  return sql.toString();case IN: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" in ").append(this.value).append(" ) "); } else { sql.append(fields[i]).append(" in ").append(this.value).append(" OR "); }  }  return sql.toString();case NOT_IN: for (i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" not in ").append(this.value).append(" ) "); } else { sql.append(fields[i]).append(" not in ").append(this.value).append(" OR "); }  }  return sql.toString(); }  for (int i = 0; i < fields.length; i++) { if (i == fields.length - 1) { sql.append(fields[i]).append(" = ").append(fieldParam).append(" ) "); } else { sql.append(fields[i]).append(" = ").append(fieldParam).append(" OR "); }  }  return sql.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/query/DefaultQueryField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */