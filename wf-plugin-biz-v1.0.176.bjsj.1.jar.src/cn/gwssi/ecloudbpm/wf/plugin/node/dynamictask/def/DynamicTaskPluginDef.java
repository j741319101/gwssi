/*     */ package cn.gwssi.ecloudbpm.wf.plugin.node.dynamictask.def;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicTaskPluginDef
/*     */   extends AbstractBpmExecutionPluginDef
/*     */ {
/*     */   public static final String dynamicType_user = "user";
/*     */   public static final String dynamicType_interface = "interface";
/*  16 */   private Boolean isEnabled = Boolean.valueOf(false);
/*     */   
/*  18 */   private String dynamicType = "user";
/*     */   
/*  20 */   private Boolean isParallel = Boolean.valueOf(false);
/*     */ 
/*     */ 
/*     */   
/*     */   private String interfaceName;
/*     */ 
/*     */   
/*     */   private boolean needSupervise = false;
/*     */ 
/*     */   
/*     */   private boolean canBeRecycledEnd = false;
/*     */ 
/*     */   
/*     */   private boolean reset = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getIsEnabled() {
/*  38 */     return this.isEnabled;
/*     */   }
/*     */   
/*     */   public void setIsEnabled(Boolean isEnabled) {
/*  42 */     this.isEnabled = isEnabled;
/*     */   }
/*     */   
/*     */   public String getDynamicType() {
/*  46 */     return this.dynamicType;
/*     */   }
/*     */   
/*     */   public void setDynamicType(String dynamicType) {
/*  50 */     this.dynamicType = dynamicType;
/*     */   }
/*     */   
/*     */   public Boolean getIsParallel() {
/*  54 */     return this.isParallel;
/*     */   }
/*     */   
/*     */   public void setIsParallel(Boolean isParallel) {
/*  58 */     this.isParallel = isParallel;
/*     */   }
/*     */   
/*     */   public String getInterfaceName() {
/*  62 */     return this.interfaceName;
/*     */   }
/*     */   
/*     */   public void setInterfaceName(String interfaceName) {
/*  66 */     this.interfaceName = interfaceName;
/*     */   }
/*     */   
/*     */   public boolean isNeedSupervise() {
/*  70 */     return this.needSupervise;
/*     */   }
/*     */   
/*     */   public void setNeedSupervise(boolean needSupervise) {
/*  74 */     this.needSupervise = needSupervise;
/*     */   }
/*     */   
/*     */   public boolean isCanBeRecycledEnd() {
/*  78 */     return this.canBeRecycledEnd;
/*     */   }
/*     */   
/*     */   public void setCanBeRecycledEnd(boolean canBeRecycledEnd) {
/*  82 */     this.canBeRecycledEnd = canBeRecycledEnd;
/*     */   }
/*     */   
/*     */   public boolean isReset() {
/*  86 */     return this.reset;
/*     */   }
/*     */   
/*     */   public void setReset(boolean reset) {
/*  90 */     this.reset = reset;
/*     */   }
/*     */   
/*     */   public static String getDynamicType_user() {
/*  94 */     return "user";
/*     */   }
/*     */   
/*     */   public static String getDynamicType_interface() {
/*  98 */     return "interface";
/*     */   }
/*     */   
/*     */   public Boolean getEnabled() {
/* 102 */     return this.isEnabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(Boolean enabled) {
/* 106 */     this.isEnabled = enabled;
/*     */   }
/*     */   
/*     */   public Boolean getParallel() {
/* 110 */     return this.isParallel;
/*     */   }
/*     */   
/*     */   public void setParallel(Boolean parallel) {
/* 114 */     this.isParallel = parallel;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/node/dynamictask/def/DynamicTaskPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */