/*     */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.user.def;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecutorVar
/*     */ {
/*     */   public static final String SOURCE_BO = "BO";
/*     */   public static final String SOURCE_FLOW_VAR = "flowVar";
/*     */   public static final String EXECUTOR_TYPE_USER = "user";
/*     */   public static final String EXECUTOR_TYPE_GROUP = "group";
/*  15 */   private String source = "";
/*     */ 
/*     */   
/*  18 */   private String name = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   private String executorType = "";
/*     */ 
/*     */   
/*  26 */   private String userValType = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   private String groupValType = "";
/*  32 */   private String dimension = "";
/*     */ 
/*     */   
/*     */   private String value;
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorVar() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorVar(String source, String name, String executorType, String userValType, String groupValType, String dimension) {
/*  44 */     this.source = source;
/*  45 */     this.name = name;
/*  46 */     this.executorType = executorType;
/*  47 */     this.userValType = userValType;
/*  48 */     this.groupValType = groupValType;
/*  49 */     this.dimension = dimension;
/*     */   }
/*     */   
/*     */   public String getSource() {
/*  53 */     return this.source;
/*     */   }
/*     */   
/*     */   public void setSource(String source) {
/*  57 */     this.source = source;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  61 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  65 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getExecutorType() {
/*  69 */     return this.executorType;
/*     */   }
/*     */   
/*     */   public void setExecutorType(String executorType) {
/*  73 */     this.executorType = executorType;
/*     */   }
/*     */   
/*     */   public String getUserValType() {
/*  77 */     return this.userValType;
/*     */   }
/*     */   
/*     */   public void setUserValType(String userValType) {
/*  81 */     this.userValType = userValType;
/*     */   }
/*     */   
/*     */   public String getGroupValType() {
/*  85 */     return this.groupValType;
/*     */   }
/*     */   
/*     */   public void setGroupValType(String groupValType) {
/*  89 */     this.groupValType = groupValType;
/*     */   }
/*     */   
/*     */   public String getDimension() {
/*  93 */     return this.dimension;
/*     */   }
/*     */   
/*     */   public void setDimension(String dimension) {
/*  97 */     this.dimension = dimension;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 101 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 105 */     this.value = value;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/user/def/ExecutorVar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */