/*    */ package com.dstz.bpm.plugin.usercalc.group.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ public class GroupPluginDef
/*    */   extends AbstractUserCalcPluginDef
/*    */ {
/*    */   @NotEmpty(message = "人员插件组类型不能为空")
/* 10 */   private String type = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   private String typeName = "";
/*    */ 
/*    */   
/*    */   @NotEmpty(message = "人员插件组KEY不能为空")
/* 20 */   private String groupKey = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   private String groupName = "";
/*    */ 
/*    */   
/*    */   public String getGroupKey() {
/* 30 */     return this.groupKey;
/*    */   }
/*    */   
/*    */   public void setGroupKey(String groupKey) {
/* 34 */     this.groupKey = groupKey;
/*    */   }
/*    */   
/*    */   public String getGroupName() {
/* 38 */     return this.groupName;
/*    */   }
/*    */   
/*    */   public void setGroupName(String groupName) {
/* 42 */     this.groupName = groupName;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 46 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 50 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getTypeName() {
/* 54 */     return this.typeName;
/*    */   }
/*    */   
/*    */   public void setTypeName(String typeName) {
/* 58 */     this.typeName = typeName;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/group/def/GroupPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */