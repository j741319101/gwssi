/*    */ package com.dstz.bpm.plugin.node.dynamictask.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicTaskPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   public static final String dynamicType_user = "user";
/*    */   public static final String dynamicType_interface = "interface";
/* 18 */   private Boolean isEnabled = Boolean.valueOf(false);
/*    */   
/* 20 */   private String dynamicType = "user";
/*    */   
/* 22 */   private Boolean isParallel = Boolean.valueOf(false);
/*    */   
/*    */   private String interfaceName;
/*    */ 
/*    */   
/*    */   public Boolean getIsEnabled() {
/* 28 */     return this.isEnabled;
/*    */   }
/*    */   
/*    */   public void setIsEnabled(Boolean isEnabled) {
/* 32 */     this.isEnabled = isEnabled;
/*    */   }
/*    */   
/*    */   public String getDynamicType() {
/* 36 */     return this.dynamicType;
/*    */   }
/*    */   
/*    */   public void setDynamicType(String dynamicType) {
/* 40 */     this.dynamicType = dynamicType;
/*    */   }
/*    */   
/*    */   public Boolean getIsParallel() {
/* 44 */     return this.isParallel;
/*    */   }
/*    */   
/*    */   public void setIsParallel(Boolean isParallel) {
/* 48 */     this.isParallel = isParallel;
/*    */   }
/*    */   
/*    */   public String getInterfaceName() {
/* 52 */     return this.interfaceName;
/*    */   }
/*    */   
/*    */   public void setInterfaceName(String interfaceName) {
/* 56 */     this.interfaceName = interfaceName;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/node/dynamictask/def/DynamicTaskPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */