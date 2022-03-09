/*     */ package cn.gwssi.ecloudbpm.bus.constant;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleAnd;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleBt;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleEic;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleGe;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleIn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleLe;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleLfk;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleLk;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleLt;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleNe;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleNi;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleOr;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleOrLike;
/*     */ import cn.gwssi.ecloudbpm.bus.model.sqlRule.SqlRuleRhk;
/*     */ 
/*     */ public enum SqlRuleType {
/*  19 */   AND("AND", "与", (Class)SqlRuleAnd.class, "and"),
/*     */ 
/*     */ 
/*     */   
/*  23 */   OR("OR", "或", (Class)SqlRuleOr.class, "or"),
/*     */ 
/*     */ 
/*     */   
/*  27 */   EQ("EQ", "等于", (Class)SqlRuleEq.class, "="),
/*     */ 
/*     */ 
/*     */   
/*  31 */   EIC("EIC", "等于忽略大小写", (Class)SqlRuleEic.class, "="),
/*     */ 
/*     */ 
/*     */   
/*  35 */   LT("LT", "小于", (Class)SqlRuleLt.class, "<"),
/*     */ 
/*     */ 
/*     */   
/*  39 */   GT("GT", "大于", (Class)SqlRuleGt.class, ">"),
/*     */ 
/*     */ 
/*     */   
/*  43 */   LE("LE", "小于等于", (Class)SqlRuleLe.class, "<="),
/*     */ 
/*     */ 
/*     */   
/*  47 */   GE("GE", "大于等于", (Class)SqlRuleGe.class, ">="),
/*     */ 
/*     */ 
/*     */   
/*  51 */   NE("NE", "不等于", (Class)SqlRuleNe.class, "!="),
/*     */ 
/*     */ 
/*     */   
/*  55 */   LK("LK", "相似", (Class)SqlRuleLk.class, "like"),
/*     */ 
/*     */ 
/*     */   
/*  59 */   LFK("LFK", "左相似", (Class)SqlRuleLfk.class, "like"),
/*     */ 
/*     */ 
/*     */   
/*  63 */   RHK("RHK", "右相似", (Class)SqlRuleRhk.class, "like"),
/*     */ 
/*     */ 
/*     */   
/*  67 */   INL("INL", "为空", (Class)SqlRuleInl.class, "is null"),
/*     */ 
/*     */ 
/*     */   
/*  71 */   NNL("NNL", "不为空", (Class)SqlRuleNnl.class, "is not null"),
/*     */ 
/*     */ 
/*     */   
/*  75 */   IN("IN", "在......里", (Class)SqlRuleIn.class, "in"),
/*     */ 
/*     */ 
/*     */   
/*  79 */   NI("NI", "不在......里", (Class)SqlRuleNi.class, "not in"),
/*     */ 
/*     */ 
/*     */   
/*  83 */   BT("BT", "在......之间", (Class)SqlRuleBt.class, "between"),
/*     */ 
/*     */ 
/*     */   
/*  87 */   ORLIKE("ORLIKE", "交集", (Class)SqlRuleOrLike.class, "");
/*     */ 
/*     */ 
/*     */   
/*     */   private String key;
/*     */ 
/*     */ 
/*     */   
/*     */   private String desc;
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<? extends ISqlRule> clazz;
/*     */ 
/*     */ 
/*     */   
/*     */   private String keyword;
/*     */ 
/*     */ 
/*     */   
/*     */   SqlRuleType(String key, String desc, Class<? extends ISqlRule> clazz) {
/* 108 */     this.key = key;
/* 109 */     this.desc = desc;
/* 110 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   SqlRuleType(String key, String desc, Class<? extends ISqlRule> clazz, String keyword) {
/* 114 */     this.key = key;
/* 115 */     this.desc = desc;
/* 116 */     this.clazz = clazz;
/* 117 */     this.keyword = keyword;
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 121 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/* 125 */     return this.desc;
/*     */   }
/*     */   
/*     */   public Class getClazz() {
/* 129 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public String getKeyword() {
/* 133 */     return this.keyword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsWithKey(String key) {
/* 143 */     return this.key.equals(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SqlRuleType getByKey(String key) {
/* 153 */     for (SqlRuleType value : values()) {
/* 154 */       if (value.key.equals(key)) {
/* 155 */         return value;
/*     */       }
/*     */     } 
/* 158 */     return null;
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
/*     */   public static Class<? extends ISqlRule> getClassByKey(String key) {
/* 170 */     for (SqlRuleType value : values()) {
/* 171 */       if (value.key.equals(key)) {
/* 172 */         return value.getClazz();
/*     */       }
/*     */     } 
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/constant/SqlRuleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */