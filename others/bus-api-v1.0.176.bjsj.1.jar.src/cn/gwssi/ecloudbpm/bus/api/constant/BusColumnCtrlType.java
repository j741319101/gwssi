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
/*     */ public enum BusColumnCtrlType
/*     */ {
/*  13 */   HIDDEN("hidden", "隐藏", new String[] { "varchar", "number", "date", "clob"
/*     */ 
/*     */     
/*     */     }),
/*  17 */   ONETEXT("onetext", "单行文本", new String[] { "varchar", "number"
/*     */ 
/*     */     
/*     */     }),
/*  21 */   MULTITEXT("multitext", "多行文本", new String[] { "varchar", "clob"
/*     */ 
/*     */     
/*     */     }),
/*  25 */   SELECT("select", "下拉框", new String[] { "varchar", "number"
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  33 */   CHECKBOX("checkbox", "复选框", new String[] { "varchar"
/*     */ 
/*     */     
/*     */     }),
/*  37 */   RADIO("radio", "单选按钮", new String[] { "varchar", "number"
/*     */ 
/*     */     
/*     */     }),
/*  41 */   DATE("date", "日期控件", new String[] { "date"
/*     */ 
/*     */     
/*     */     }),
/*  45 */   DIC("dic", "数据字典", new String[] { "varchar", "number"
/*     */ 
/*     */     
/*     */     }),
/*  49 */   SERIALNO("serialno", "流水号", new String[] { "varchar", "number"
/*     */ 
/*     */     
/*     */     }),
/*  53 */   FILE("file", "附件上传", new String[] { "varchar", "clob" });
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
/*     */   private String[] supports;
/*     */ 
/*     */ 
/*     */   
/*     */   BusColumnCtrlType(String key, String desc, String[] supports) {
/*  69 */     this.key = key;
/*  70 */     this.desc = desc;
/*  71 */     this.supports = supports;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  80 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  84 */     return this.desc;
/*     */   }
/*     */   
/*     */   public String[] getSupports() {
/*  88 */     return this.supports;
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
/*     */   public static BusColumnCtrlType getByKey(String key) {
/* 100 */     for (BusColumnCtrlType f : values()) {
/* 101 */       if (f.key.equals(key)) {
/* 102 */         return f;
/*     */       }
/*     */     } 
/* 105 */     return null;
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
/* 117 */     return this.key.equals(key);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/constant/BusColumnCtrlType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */