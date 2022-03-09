/*     */ package cn.gwssi.ecloudframework.base.api.query;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum QueryOP
/*     */ {
/*   7 */   EQUAL("EQ", "=", "等于", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  11 */   EQUAL_IGNORE_CASE("EIC", "=", "等于忽略大小写", new String[] { "varchar"
/*     */ 
/*     */     
/*     */     }),
/*  15 */   LESS("LT", "<", "小于", new String[] { "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  19 */   GREAT("GT", ">", "大于", new String[] { "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  23 */   LESS_EQUAL("LE", "<=", "小于等于", new String[] { "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  27 */   GREAT_EQUAL("GE", ">=", "大于等于", new String[] { "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  31 */   NOT_EQUAL("NE", "!=", "不等于", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  35 */   LIKE("LK", "like", "相似", new String[] { "varchar"
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  40 */   NOT_LIKE("KL", "not like", "不相似", new String[] { "varchar"
/*     */ 
/*     */     
/*     */     }),
/*  44 */   LEFT_LIKE("LFK", "like", "左相似", new String[] { "varchar"
/*     */ 
/*     */     
/*     */     }),
/*  48 */   RIGHT_LIKE("RHK", "like", "右相似", new String[] { "varchar"
/*     */ 
/*     */     
/*     */     }),
/*  52 */   IS_NULL("INL", "is null", "为空", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  56 */   NOTNULL("NNL", "is not null", "非空", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  60 */   IN("IN", "in", "在...中", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  64 */   NOT_IN("NI", "not in", "不在...中", new String[] { "varchar", "number", "date"
/*     */ 
/*     */     
/*     */     }),
/*  68 */   BETWEEN("BT", "between", "在...之间", new String[] { "number", "date" });
/*     */   private String val;
/*     */   private String op;
/*     */   private String desc;
/*     */   private String[] supports;
/*     */   
/*     */   QueryOP(String val, String op, String desc, String[] supports) {
/*  75 */     this.val = val;
/*  76 */     this.op = op;
/*  77 */     this.desc = desc;
/*  78 */     this.supports = supports;
/*     */   }
/*     */   
/*     */   public String value() {
/*  82 */     return this.val;
/*     */   }
/*     */   
/*     */   public String op() {
/*  86 */     return this.op;
/*     */   }
/*     */   
/*     */   public String desc() {
/*  90 */     return this.desc;
/*     */   }
/*     */   
/*     */   public String[] supports() {
/*  94 */     return this.supports;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryOP getByOP(String op) {
/* 106 */     for (QueryOP queryOP : values()) {
/* 107 */       if (queryOP.op().equals(op)) {
/* 108 */         return queryOP;
/*     */       }
/*     */     } 
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   public static QueryOP getByVal(String val) {
/* 115 */     for (QueryOP queryOP : values()) {
/* 116 */       if (queryOP.val.equals(val)) {
/* 117 */         return queryOP;
/*     */       }
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsWithVal(String val) {
/* 132 */     return this.val.equals(val);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/query/QueryOP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */