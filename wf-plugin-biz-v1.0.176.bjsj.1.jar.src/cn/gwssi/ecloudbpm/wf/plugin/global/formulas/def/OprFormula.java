/*    */ package com.dstz.bpm.plugin.global.formulas.def;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ 
/*    */ public class OprFormula implements Serializable {
/*    */   private static final long serialVersionUID = -1120163905633604828L;
/*    */   private String id;
/*    */   private String name;
/*    */   private List<String> action;
/*    */   
/*    */   public String getId() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 17 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 25 */     this.name = name;
/*    */   }
/*    */   
/*    */   public List<String> getAction() {
/* 29 */     return this.action;
/*    */   }
/*    */   
/*    */   public void setAction(List<String> action) {
/* 33 */     this.action = action;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/formulas/def/OprFormula.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */