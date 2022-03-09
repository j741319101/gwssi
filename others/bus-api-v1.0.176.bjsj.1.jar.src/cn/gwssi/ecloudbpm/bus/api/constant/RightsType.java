/*     */ package cn.gwssi.ecloudbpm.bus.api.constant;
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
/*     */ public enum RightsType
/*     */ {
/*  21 */   REQUIRED("b", "必填", true, true),
/*     */ 
/*     */ 
/*     */   
/*  25 */   WRITE("w", "编辑", true, true),
/*     */ 
/*     */ 
/*     */   
/*  29 */   READ("r", "只读", true, false),
/*     */ 
/*     */ 
/*     */   
/*  33 */   NONE("n", "无", false, false);
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
/*     */   private boolean dbReadable;
/*     */ 
/*     */   
/*     */   private boolean dbEditable;
/*     */ 
/*     */ 
/*     */   
/*     */   RightsType(String key, String desc, boolean dbReadable, boolean dbEditable) {
/*  52 */     this.key = key;
/*  53 */     this.desc = desc;
/*  54 */     this.dbReadable = dbReadable;
/*  55 */     this.dbEditable = dbEditable;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  59 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  63 */     return this.desc;
/*     */   }
/*     */   
/*     */   public boolean isDbReadable() {
/*  67 */     return this.dbReadable;
/*     */   }
/*     */   
/*     */   public boolean isDbEditable() {
/*  71 */     return this.dbEditable;
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
/*     */   public boolean equalsWithKey(String key) {
/*  83 */     return this.key.equals(key);
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
/*     */   public static RightsType getByKey(String key) {
/*  95 */     for (RightsType value : values()) {
/*  96 */       if (value.key.equals(key)) {
/*  97 */         return value;
/*     */       }
/*     */     } 
/* 100 */     return null;
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
/*     */ 
/*     */   
/*     */   public static RightsType getDefalut() {
/* 114 */     return WRITE;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/RightsType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */