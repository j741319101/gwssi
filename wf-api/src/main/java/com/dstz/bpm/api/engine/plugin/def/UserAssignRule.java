/*    */ package com.dstz.bpm.api.engine.plugin.def;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.context.UserCalcPluginContext;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserAssignRule
/*    */   implements Comparable<UserAssignRule>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 17 */   private String name = "";
/*    */   
/* 19 */   private String description = "";
/*    */   
/* 21 */   private String condition = "";
/*    */ 
/*    */ 
/*    */   
/* 25 */   private int groupNo = 1;
/*    */   
/* 27 */   private List<UserCalcPluginContext> calcPluginContextList = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 39 */     if (StringUtil.isEmpty(this.description)) {
/* 40 */       String desc = "";
/* 41 */       for (UserCalcPluginContext ctx : this.calcPluginContextList) {
/* 42 */         desc = desc + "　　　【" + ctx.getTitle() + "】" + ctx.getDescription() + ";";
/*    */       }
/* 44 */       return desc;
/*    */     } 
/* 46 */     return this.description;
/*    */   }
/*    */   
/*    */   public void setDescription(String description) {
/* 50 */     this.description = description;
/*    */   }
/*    */   
/*    */   public String getCondition() {
/* 54 */     return this.condition;
/*    */   }
/*    */   
/*    */   public void setCondition(String condition) {
/* 58 */     this.condition = condition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getGroupNo() {
/* 71 */     return this.groupNo;
/*    */   }
/*    */   
/*    */   public void setGroupNo(int groupNo) {
/* 75 */     this.groupNo = groupNo;
/*    */   }
/*    */   
/*    */   public List<UserCalcPluginContext> getCalcPluginContextList() {
/* 79 */     return this.calcPluginContextList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCalcPluginContextList(List<UserCalcPluginContext> calcPluginContextList) {
/* 84 */     this.calcPluginContextList = calcPluginContextList;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(UserAssignRule userRule) {
/* 89 */     if (this.groupNo > userRule.groupNo) return 1; 
/* 90 */     if (this.groupNo < userRule.groupNo) return -1; 
/* 91 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/def/UserAssignRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */